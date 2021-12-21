package com.madslee.simplejdbc.util

import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

internal val Any.fieldsValuesMap: Map<String, Any>
    get() = this::class.java.methods
        .filter { isCustomGetter(it) }
        .map { fieldNameOfGetter(it) to it.invoke(this) }
        .toMap()

internal val Any.className: String
    get() = this::class.simpleName ?: throw RuntimeException("Unable to get class name for $this")

internal val KClass<*>.name: String
    get() = this.simpleName ?: throw RuntimeException("Unable to get class name for $this")

private val isCustomGetter = { method: Method ->
    method.name.substring(0, 3) == "get" && method.name != "getClass"
}

internal val KClass<*>.fields: List<String>
    get() = this.memberProperties.map { it.name }

private val fieldNameOfGetter = { method: Method ->
    method.name
        .substring(3)
        .replaceFirstChar { it.lowercaseChar() }
}

fun KClass<*>.callConstructor(argumentNamesValues: Map<String, Any>): Any {
    val paramNamesWithSqlCasing = primaryConstructorParameterNames.map { it.sqlCase() }
    val argumentsInCorrectOrder = argumentNamesValues.valuesWithKeySorting(paramNamesWithSqlCasing).values.toList()
    return primaryConstructor?.call(*argumentsInCorrectOrder.toTypedArray())
        ?: throw RuntimeException("Unable to get primary constructor for class ${this.name}")
}

internal val KClass<*>.primaryConstructorParameterNames: List<String>
    get() = this.primaryConstructor?.parameters
        ?.mapNotNull { it.name }
        ?: listOf()
