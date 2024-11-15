package com.madslee.simplejdbc.util

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

internal fun createParameterizableInsertStatement(table: String, columns: List<String>) =
    "insert into $table (${columns.joinToString(", ")}) values (${columns.map { "?" }.joinToString(", ")});"

internal fun createSelectColumnsStatement(table: String, columns: List<String>, whereClauses: List<String>) =
    "select ${columns.map { it }.joinToString(", ")} from $table ${createWhereClausesStatement(whereClauses)}"

internal fun createParametrizableUpdateStatement(table: String, columns: List<String>, whereClauses: List<String>) =
    "update $table ${createSetColumnsParameterizableSubstatement(columns)} ${createWhereClausesStatement(whereClauses)}"

private fun createSetColumnsParameterizableSubstatement(columns: List<String>) =
    if (columns.isEmpty()) ""
    else columns
        .map { "$it = ?" }
        .joinToString(prefix = "set ", separator = ", ")

private fun createWhereClausesStatement(whereClauses: List<String>) =
    if (whereClauses.isEmpty()) ""
    else whereClauses.joinToString(separator = " and ", prefix = "where ")

internal fun <T> ResultSet.map(mapper: (ResultSet) -> T): List<T> {
    return generateSequence {
        if (this.next()) {
            mapper(this)
        } else {
            null
        }
    }.toList()
}

internal fun PreparedStatement.addParams(orderedParams: Collection<Any>) {
    orderedParams.forEachIndexed { index, any ->
        this.setObject(index + 1, any)
    }
}

internal fun Connection.getPrimaryKeys(table: String) =
    metaData.getPrimaryKeys(catalog, schema, table.uppercase())
        .map { resultSetRow ->
            resultSetRow.getString("COLUMN_NAME")
        }
