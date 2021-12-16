package com.madslee.simplejdbc


import com.madslee.simplejdbc.util.map
import com.madslee.simplejdbc.util.selectColumnsStatement
import java.sql.Connection
import kotlin.reflect.KClass


fun <T: Any> getAll(clazz: KClass<T>, table: String): List<out T> {
    TODO("Not implemented")
}


fun getAll(table: String, columns: List<String>, connection: Connection): List<Map<String, Any>> =
    connection.prepareStatement(selectColumnsStatement(table, columns))
        .executeQuery()
        .map { resultSet ->
            columns.associateWith {
                resultSet.getObject(it)
            }
        }
