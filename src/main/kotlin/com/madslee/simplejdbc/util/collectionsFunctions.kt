package com.madslee.simplejdbc.util


fun <K, V> Map<K, V>.joinWith(overridingEntries: Map<K, V>): Map<K, V> {
    val newMap: MutableMap<K, V> = overridingEntries.toMutableMap()

    this.forEach { (key, value) ->
        if (newMap[key] == null) newMap[key] = value
    }
    return newMap
}

