package com.madslee.simplejdbc.util

import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

internal val Any.fieldsValuesMap: Map<String, Any>
    get() = this::class.java.methods
        .filter { it.isCustomGetter }
        .associate { it.relatedFieldName to it.invoke(this) }

internal val Any.className: String
    get() = this::class.simpleName ?: throw RuntimeException("Unable to get class name for $this")

internal val KClass<*>.name: String
    get() = this.simpleName ?: throw RuntimeException("Unable to get class name for $this")

internal val Method.isCustomGetter: Boolean
    get() = this.name.substring(0, 3) == "get" && this.name != "getClass"

internal val KClass<*>.fields: List<String>
    get() = this.memberProperties.map { it.name }

internal val Method.relatedFieldName: String
    get() = this.name
        .substring(3)
        .replaceFirstChar { it.lowercaseChar() }

internal fun KClass<*>.callConstructor(argumentNamesValues: Map<String, Any>): Any {
    // TODO: convert paramNames and argumentNames to lower case before matching
    val paramNames = primaryConstructorParameterNames
    val argumentsInCorrectOrder = argumentNamesValues.valuesWithKeySorting(paramNames).values.toList()
    return primaryConstructor?.call(*argumentsInCorrectOrder.toTypedArray())
        ?: throw RuntimeException("Unable to get primary constructor for class ${this.name}")
}

internal val KClass<*>.primaryConstructorParameterNames: List<String>
    get() = this.primaryConstructor?.parameters
        ?.mapNotNull { it.name }
        ?: listOf()
