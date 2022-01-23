package com.madslee.simplejdbc

import com.madslee.simplejdbc.util.*
import com.madslee.simplejdbc.util.addParams
import com.madslee.simplejdbc.util.camelCasetoSqlCase
import com.madslee.simplejdbc.util.createParametrizableUpdateStatement
import com.madslee.simplejdbc.util.fieldsValuesMap
import java.sql.Connection

fun Connection.updateOne(updatedObject: Any, clause: WhereEqualClause) =
    updateOne(
        table = updatedObject.className,
        updatedObject = updatedObject,
        clause = clause
    )

fun Connection.updateOne(updatedObject: Any, table: String, clause: WhereEqualClause) {
    updateOne(
        table = table,
        columnsValues = updatedObject.fieldsValuesMap,
        clause = clause
    )
}

private fun Connection.updateOne(table: String, columnsValues: Map<String, Any>, clause: WhereEqualClause) =
    if (columnsValues.isEmpty()) throw IllegalArgumentException("Can't update with empty values")
    else {
        prepareStatement(
            createParametrizableUpdateStatement(table, columnsValues.keys.map { it.camelCasetoSqlCase() }, listOf(clause.sql))
        ).let { preparedStatement ->
            preparedStatement.addParams(columnsValues.values)
            preparedStatement.executeUpdate()
        }
    }