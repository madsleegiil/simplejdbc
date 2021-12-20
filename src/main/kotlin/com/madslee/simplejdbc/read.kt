package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

fun <T: Any> getAll(clazz: KClass<T>, connection: Connection) = getAll(clazz, clazz.javaObjectType.simpleName, connection)

fun <T: Any> getAll(clazz: KClass<T>, table: String, connection: Connection): List<Any> =
    getAll(
        table = table,
        columns = clazz.memberProperties.map { allLowerCaseSnakeCase(it.name) },
        connection = connection
    ).map { databaseRow ->
        clazz.primaryConstructor!!.call(
            *databaseRow.valuesWithKeySorting(
                primaryConstructorParameterNames(clazz).map { allLowerCaseSnakeCase(it) }
            ).values.toTypedArray()
        )
    }

fun getAll(table: String, columns: List<String>, connection: Connection): List<Map<String, Any>> =
    connection.prepareStatement(selectColumnsStatement(table, columns))
        .executeQuery()
        .map { resultSet ->
            columns.associateWith {
                when (resultSet.getObject(it).javaClass.typeName) {
                    "org.h2.jdbc.JdbcClob" -> resultSet.getString(it) // TODO: Test with postgres
                    else -> resultSet.getObject(it)
                }
            }
        }
