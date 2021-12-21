package com.madslee.simplejdbc.util

import java.sql.ResultSet

val parameterizableInsertStatement = { table: String, columns: List<String> ->
    "insert into $table (${columns.joinToString(", ")}) values (${columns.map { "?" }.joinToString(", ")});"
}

val selectColumnsStatement = { table: String, columns: List<String> ->
    "select ${columns.map { it }.joinToString(", ")} from $table"
}

fun <T> ResultSet.map(mapper: (ResultSet) -> T): List<T> {
    return generateSequence {
        if (this.next()) {
            mapper(this)
        } else {
            null
        }
    }.toList()
}

fun ResultSet.get(column: String): Any =
    when (this.getObject(column).javaClass.typeName) {
        "org.h2.jdbc.JdbcClob" -> this.getString(column) // TODO: Test with postgres
        else -> this.getObject(column)
    }