package com.madslee.simplejdbc.util

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDate

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

internal fun ResultSet.get(column: String): Any =
    when (this.getObject(column).javaClass.typeName) {
        "org.h2.jdbc.JdbcClob" -> this.getString(column) // TODO: Test with postgres
        else -> this.getObject(column)
    }

internal fun PreparedStatement.addParams(orderedParams: Collection<Any>) =
    orderedParams.forEachIndexed { index, any ->
        this.setObject(index + 1, any)
    }