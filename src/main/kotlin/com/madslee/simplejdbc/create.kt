package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection

fun Connection.insert(any: Any) =
    insert(
        table = any.className,
        columnsValues = any.fieldsValuesMap
    )

fun Connection.insert(any: Any, table: String) =
    insert(
        table = table,
        columnsValues = any.fieldsValuesMap
    )

private fun Connection.insert(table: String, columnsValues: Map<String, Any>) =
    prepareStatement(
        createParameterizableInsertStatement(table, columnsValues.keys.map { it.camelCasetoSqlCase() })
    ).let { preparedStatement ->
        preparedStatement.addParams(columnsValues.values)
        preparedStatement.executeUpdate()
    }