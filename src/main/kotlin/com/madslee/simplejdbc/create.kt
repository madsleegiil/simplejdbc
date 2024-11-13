package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection

fun Connection.insert(any: Any) =
    insert(
        connection = this,
        table = any.className,
        columnsValues = any.fieldsValuesMap
    )

fun Connection.insert(any: Any, table: String) =
    insert(
        connection = this,
        table = table,
        columnsValues = any.fieldsValuesMap
    )

private fun insert(connection: Connection, table: String, columnsValues: Map<String, Any>) {
    val simpleConnection = SimpleConnection(connection)
    val sql = createParameterizableInsertStatement(table, columnsValues.keys.map { it.camelCasetoSqlCase() })
    val preparedStatement = simpleConnection.prepareStatement(sql)

    columnsValues.values.forEachIndexed { index, any ->
        preparedStatement.setObject(index + 1, any)
    }

    preparedStatement.executeUpdate()
}
