package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import kotlin.reflect.KClass

fun <T : Any> Connection.select(clazz: KClass<T>, vararg clause: Clause) =
    this.select(
        clazz = clazz,
        table = clazz.name,
        clause = clause
    )

fun <T : Any> Connection.select(clazz: KClass<T>, table: String, vararg clause: Clause): List<T> =
    this.select(
        table = table,
        columns = clazz.fieldsWithType.entries.associate { it.key.camelCasetoSqlCase() to it.value.kotlin },
        clause = clause
    ).map { databaseRow ->
        clazz.callConstructor(databaseRow.map { it.key.sqlCaseToCamelCase() to it.value }.toMap())
    }

private fun Connection.select(table: String, columns: Map<String, KClass<*>>, vararg clause: Clause): List<Map<String, Any>> =
    prepareStatement(createSelectColumnsStatement(table, columns.keys.toList(), clause.map { it.sql }))
        .executeQuery()
        .map { resultSetRow ->
            columns.map { (columnName, columnClass) ->
                columnName to if (columnClass.isJavaPrimitive) resultSetRow.getObject(columnName) else resultSetRow.getObject(
                    columnName,
                    columnClass.java
                )
            }.toMap()
        }
