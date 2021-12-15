package com.madslee.simplejdbc

import java.sql.Connection

fun save(any: Any, table: String, connection: Connection) {
    TODO("Implement")
}

fun save(table: String, columnsValues: Map<String, Any>, connection: Connection) {
    connection.prepareStatement(
        parameterizableInsertString(table, columnsValues.map { allLowerCaseSnakeCase(it.key) })
    ).let { preparedStatement ->
        columnsValues
            .map { it.value }
            .forEachIndexed { index, any -> preparedStatement.setObject(index + 1, any) }
        preparedStatement.executeUpdate()
    }
}

val parameterizableInsertString = { table: String, columns: List<String> ->
    "insert into $table (${columns.joinToString(", ")}) values (${columns.map { "?" }.joinToString(", ")});"
}

