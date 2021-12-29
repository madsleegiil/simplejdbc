package com.madslee.simplejdbc.util


internal fun <K, V> Map<K, V>.valuesWithKeySorting(keySorting: List<K>): Map<K, V> {
    val newMap: MutableMap<K, V> = mutableMapOf()

    keySorting.forEach {
        newMap[it] = this[it]!!
    }
    return newMap
}