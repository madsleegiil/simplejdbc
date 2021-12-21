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
        columns = clazz.fields.map { it.camelCasetoSqlCase() },
        connection = connection
    ).map { databaseRow ->

        clazz.callConstructor(databaseRow.map { it.key.sqlCaseToCamelCase() to it.value }.toMap())
    }

fun getAll(table: String, columns: List<String>, connection: Connection): List<Map<String, Any>> =
    connection.prepareStatement(createSelectColumnsStatement(table, columns))
        .executeQuery()
        .map { resultSetRow ->
            columns.associateWith { resultSetRow.get(it) }
        }
