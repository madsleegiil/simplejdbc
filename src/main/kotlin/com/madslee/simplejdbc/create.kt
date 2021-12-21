package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import java.sql.Connection

fun save(any: Any, connection: Connection) =
    save(
        table = any.className,
        columnsValues = any.fieldsValuesMap,
        connection = connection
    )

fun save(any: Any, table: String, connection: Connection) =
    save(
        table = table,
        columnsValues = any.fieldsValuesMap,
        connection = connection
    )

fun save(any: Any, overridingColumnValues: Map<String, Any>, connection: Connection) =
    save(
        any = any,
        table = any.className,
        overridingColumnValues = overridingColumnValues,
        connection = connection
    )

fun save(any: Any, table: String, overridingColumnValues: Map<String, Any>, connection: Connection) =
    save(
        table = table,
        columnsValues = any.fieldsValuesMap.joinWith(overridingColumnValues),
        connection = connection
    )

fun save(table: String, columnsValues: Map<String, Any>, connection: Connection) =
    connection.prepareStatement(
        createParameterizableInsertStatement(table, columnsValues.keys.map { it.toSqlCase() })
    ).let { preparedStatement ->
        preparedStatement.addParams(columnsValues.values)
        preparedStatement.executeUpdate()
    }