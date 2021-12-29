package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection

fun Connection.save(any: Any) =
    save(
        table = any.className,
        columnsValues = any.fieldsValuesMap
    )

fun Connection.save(any: Any, table: String) =
    save(
        table = table,
        columnsValues = any.fieldsValuesMap
    )

private fun Connection.save(table: String, columnsValues: Map<String, Any>) =
    prepareStatement(
        createParameterizableInsertStatement(table, columnsValues.keys.map { it.camelCasetoSqlCase() })
    ).let { preparedStatement ->
        preparedStatement.addParams(columnsValues.values)
        preparedStatement.executeUpdate()
    }