package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import kotlin.reflect.KClass

fun <T: Any> getAll(clazz: KClass<T>, connection: Connection) =
    getAll(
        clazz = clazz,
        table = clazz.name,
        connection = connection)

fun <T: Any> getAll(clazz: KClass<T>, table: String, connection: Connection): List<Any> =
    getAll(
        table = table,
        columns = clazz.fields.map { it.sqlCase() },
        connection = connection
    ).map { databaseRow ->
        clazz.callConstructor(databaseRow)
    }

fun getAll(table: String, columns: List<String>, connection: Connection): List<Map<String, Any>> =
    connection.prepareStatement(selectColumnsStatement(table, columns))
        .executeQuery()
        .map { resultSetRow ->
            columns.associateWith { resultSetRow.get(it) }
        }
