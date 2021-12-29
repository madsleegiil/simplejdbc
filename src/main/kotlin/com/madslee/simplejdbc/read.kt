package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import kotlin.reflect.KClass

fun <T : Any> Connection.getAll(clazz: KClass<T>) =
    getAll(
        clazz = clazz,
        table = clazz.name
    )

fun <T : Any> Connection.getAll(clazz: KClass<T>, table: String): List<T> =
    getAll(
        table = table,
        columns = clazz.fields.entries.associate { it.key.camelCasetoSqlCase() to it.value.kotlin }
    ).map { databaseRow ->
        clazz.callConstructor(databaseRow.map { it.key.sqlCaseToCamelCase() to it.value }.toMap()) as T
    }

fun Connection.getAll(table: String, columns: Map<String, KClass<*>>): List<Map<String, Any>> =
    prepareStatement(createSelectColumnsStatement(table, columns.keys.toList()))
        .executeQuery()
        .map { resultSetRow ->
            columns.map { (columnName, columnClass) ->
                columnName to if (columnClass.isJavaPrimitive) resultSetRow.getObject(columnName) else resultSetRow.getObject(
                    columnName,
                    columnClass.java
                )
            }.toMap()
        }
