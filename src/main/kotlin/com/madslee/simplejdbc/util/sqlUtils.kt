package com.madslee.simplejdbc.util

import java.sql.PreparedStatement
import java.sql.ResultSet

internal fun createParameterizableInsertStatement(table: String, columns: List<String>) =
    "insert into $table (${columns.joinToString(", ")}) values (${columns.map { "?" }.joinToString(", ")});"

internal fun createSelectColumnsStatement(table: String, columns: List<String>) =
    "select ${columns.map { it }.joinToString(", ")} from $table"

internal fun <T> ResultSet.map(mapper: (ResultSet) -> T): List<T> {
    return generateSequence {
        if (this.next()) {
            mapper(this)
        } else {
            null
        }
    }.toList()
}

internal fun PreparedStatement.addParams(orderedParams: Collection<Any>) =
    orderedParams.forEachIndexed { index, any ->
        this.setObject(index + 1, any)
    }