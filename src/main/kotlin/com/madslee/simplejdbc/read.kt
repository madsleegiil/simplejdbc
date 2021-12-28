package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import kotlin.reflect.KClass

fun <T: Any> Connection.getAll(clazz: KClass<T>) =
    getAll(
        clazz = clazz,
        table = clazz.name
    )

fun <T: Any> Connection.getAll(clazz: KClass<T>, table: String): List<Any> =
    getAll(
        table = table,
        columns = clazz.fields.map { it.camelCasetoSqlCase() }
    ).map { databaseRow ->
        clazz.callConstructor(databaseRow.map { it.key.sqlCaseToCamelCase() to it.value }.toMap())
    }

fun Connection.getAll(table: String, columns: List<String>): List<Map<String, Any>> =
    prepareStatement(createSelectColumnsStatement(table, columns))
        .executeQuery()
        .map { resultSetRow ->
            columns.associateWith { resultSetRow.get(it) }
        }
