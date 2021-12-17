package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun <T: Any> getAll(clazz: KClass<T>, connection: Connection) = getAll(clazz, clazz.javaObjectType.simpleName, connection)

fun <T: Any> getAll(clazz: KClass<T>, table: String, connection: Connection): List<Any> =
    getAll(
        table = table,
        columns = clazz.memberProperties.map { allLowerCaseSnakeCase(it.name) },
        connection = connection
    ).map {

    }.map {
        //primaryConstructor(clazz)!!.callBy()
    }

fun getAll(table: String, columns: List<String>, connection: Connection): List<Map<String, Any>> =
    connection.prepareStatement(selectColumnsStatement(table, columns))
        .executeQuery()
        .map { resultSet ->
            columns.associateWith {
                resultSet.getObject(it)
            }
        }
