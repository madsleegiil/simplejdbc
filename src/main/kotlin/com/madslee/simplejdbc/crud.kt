package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import kotlin.reflect.KClass

fun save(any: Any, connection: Connection) =
    save(
        table = className(any)!!,
        columnsValues = fieldMap(any),
        connection = connection
    )

fun save(any: Any, table: String = className(any)!!, overridingColumnValues: Map<String, Any>, connection: Connection) =
    save(
        table = table,
        columnsValues = fieldMap(any).toMutableMap().joinWith(overridingColumnValues),
        connection = connection
    )

fun save(table: String, columnsValues: Map<String, Any>, connection: Connection) =
    connection.prepareStatement(
        parameterizableInsertString(table, columnsValues.map { allLowerCaseSnakeCase(it.key) })
    ).let { preparedStatement ->
        columnsValues
            .map { it.value }
            .forEachIndexed { index, any -> preparedStatement.setObject(index + 1, any) }
        preparedStatement.executeUpdate()
    }

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

