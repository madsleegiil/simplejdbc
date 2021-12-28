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

fun Connection.save(any: Any, overridingColumnValues: Map<String, Any>) =
    save(
        any = any,
        table = any.className,
        overridingColumnValues = overridingColumnValues
    )

fun Connection.save(any: Any, table: String, overridingColumnValues: Map<String, Any>) =
    this.save(
        table = table,
        columnsValues = any.fieldsValuesMap.joinWith(overridingColumnValues)
    )

fun Connection.save(table: String, columnsValues: Map<String, Any>) =
    prepareStatement(
        createParameterizableInsertStatement(table, columnsValues.keys.map { it.camelCasetoSqlCase() })
    ).let { preparedStatement ->
        preparedStatement.addParams(columnsValues.values)
        preparedStatement.executeUpdate()
    }