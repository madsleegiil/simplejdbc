package com.madslee.simplejdbc.util

import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

// TODO: Change all to kotlin reflect

internal val primaryConstructor = { any: Any ->
    any::class.primaryConstructor
}

internal val fieldNames = { any: Any ->
    any::class.declaredMemberProperties.map { it.name }
}

internal val fieldMap: (Any) -> Map<String, Any> = { any: Any ->
    any::class.java.methods
        .filter { isCustomGetter(it) }
        .map { fieldNameOfGetter(it) to it.invoke(any) }
        .toMap()
}

internal val className = { any: Any -> any::class.simpleName }

private val isCustomGetter = { method: Method ->
    method.name.substring(0, 3) == "get" && method.name != "getClass"
}

private val fieldNameOfGetter = { method: Method ->
    method.name
        .substring(3)
        .replaceFirstChar { it.lowercaseChar() }
}

internal val primaryConstructorParameterNames: (clazz: KClass<*>) -> List<String> = { clazz ->
    clazz.primaryConstructor?.parameters
        ?.mapNotNull { it.name }
        ?: listOf()
}

internal val sortMapValuesByPrimaryConstructorParameterOrder: (clazz: KClass<*>, map: Map<String, Any>) -> Array<Any> = { clazz, map ->
    val constructorParameterNames = primaryConstructorParameterNames(clazz)
    val constructorParameterNamesWithSqlCasing = constructorParameterNames.map { allLowerCaseSnakeCase(it) }
    map.valuesWithKeySorting(constructorParameterNamesWithSqlCasing).values.toTypedArray()
}