package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection

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