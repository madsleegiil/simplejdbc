package com.madslee.simplejdbc.util

import java.lang.reflect.Method
import kotlin.reflect.full.primaryConstructor

// TODO: Change all to kotlin reflect

val primaryConstructor = { any: Any ->
    any::class.primaryConstructor
}

val fieldNames = { any: Any ->
    any.javaClass.declaredFields.map { it.name }
}

val fieldMap: (Any) -> Map<String, Any> = { any: Any ->
    any::class.java.methods
        .filter { isCustomGetter(it) }
        .map { fieldNameOfGetter(it) to it.invoke(any) }
        .toMap()
}

val className = { any: Any -> any::class.simpleName }

private val isCustomGetter = {method: Method ->
    method.name.substring(0, 3) == "get" && method.name != "getClass"
}

private val fieldNameOfGetter = { method: Method ->
    method.name
        .substring(3)
        .replaceFirstChar { it.lowercaseChar() }
}