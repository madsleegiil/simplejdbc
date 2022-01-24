package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import com.madslee.simplejdbc.util.addParams
import com.madslee.simplejdbc.util.camelCasetoSqlCase
import com.madslee.simplejdbc.util.createParametrizableUpdateStatement
import com.madslee.simplejdbc.util.fieldsValuesMap
import java.sql.Connection

fun Connection.updateById(updatedObject: Any) =
    updateById(
        table = updatedObject.className,
        updatedObject = updatedObject
    )

fun Connection.updateById(updatedObject: Any, table: String) {
    updateById(
        table = table,
        columnsValues = updatedObject.fieldsValuesMap
    )
}

private fun Connection.updateById(table: String, columnsValues: Map<String, Any>): Int {
    if (columnsValues.isEmpty()) throw IllegalArgumentException("Can't update with empty values")

    val tablePrimaryKeys = getPrimaryKeys(table)
    require(tablePrimaryKeys.isNotEmpty()) { "Can't update an object mapped to a table without any primary key" }

    val primaryKeysValues = columnsValues.filter { tablePrimaryKeys.contains(it.key.uppercase()) }
    val primaryKeysWhereClauses = primaryKeysValues.map { whereEqual(it.key, it.value) }.map { it.sql }

    return prepareStatement(
        createParametrizableUpdateStatement(table, columnsValues.keys.map { it.camelCasetoSqlCase() }, primaryKeysWhereClauses)
    ).let { preparedStatement ->
        preparedStatement.addParams(columnsValues.values)
        preparedStatement.executeUpdate()
    }
}