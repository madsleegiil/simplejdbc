package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import java.sql.JDBCType
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

// TODO: kall på konstruktør er riktig, men typen for string er feil, blir jdbcClob eller noe
fun <T: Any> getAll(clazz: KClass<T>, connection: Connection) = getAll(clazz, clazz.javaObjectType.simpleName, connection)

fun <T: Any> getAll(clazz: KClass<T>, table: String, connection: Connection): List<Any> =
    getAll(
        table = table,
        columns = clazz.memberProperties.map { allLowerCaseSnakeCase(it.name) },
        connection = connection
    ).map { databaseRow ->
        //clazz.primaryConstructor!!.call("id", "de", 12.3, 1)
        clazz.primaryConstructor!!.call(
            *databaseRow.valuesWithKeySorting(primaryConstructorParameterNames(clazz).map { allLowerCaseSnakeCase(it) }).values.toTypedArray()
        )
    }

fun getAll(table: String, columns: List<String>, connection: Connection): List<Map<String, Any>> =
    connection.prepareStatement(selectColumnsStatement(table, columns))
        .executeQuery()
        .map { resultSet ->
            columns.associateWith {
                when (resultSet.getObject(it).javaClass.typeName) {
                    is "Double" -> resultSet.getDouble(it)
                    is Int -> resultSet.getInt(it)
                    is String -> resultSet.getString(it)
                    else -> resultSet.getObject(it)
                }
            }
        }
