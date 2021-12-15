package com.madslee.simplejdbc


val fieldNames = { any: Any ->
    any.javaClass.declaredFields.map { it.name }
}

val fieldMap = { any: Any ->
    any.javaClass.declaredFields.forEach {  it.trySetAccessible() }
    any.javaClass.declaredFields.associate { it.name to it.get(it) }
}