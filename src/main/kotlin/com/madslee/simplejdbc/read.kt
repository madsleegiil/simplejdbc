package com.madslee.simplejdbc

import java.sql.Connection
import kotlin.reflect.KClass


fun <T: Any> getAll(clazz: KClass<T>, table: String): List<out T> {
    TODO("Not implemented")
}

fun getAll(table: String, columns: List<String>, connection: Connection): List<Map<String, Any>> =
    connection.prepareStatement("select ${columns.map { it }.joinToString(", ")} from $table")
        .executeQuery()
        .let { resultSet ->
            generateSequence {
                if (resultSet.next()) {
                    columns.map {
                        it to resultSet.getObject(it)
                    }.toMap()
                } else null
            }
        }.toList()

