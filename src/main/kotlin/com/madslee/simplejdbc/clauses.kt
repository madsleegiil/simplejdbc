package com.madslee.simplejdbc

private val equalOperator = "="
private val unequalOperator = "!="
private val isNullOperator = "is null"
private val isNotNullOperator = "is not null"

fun whereEqual(column: String, match: Any?): Clause {
    val sql = if (match is Number) {
        "$column = $match"
    } else if (match == null) {
        "$column is null"
    } else {
        "$column = '$match'"
    }

    return Clause(sql)
}

fun whereNotEqual(column: String, match: Any?): Clause {
    val sql = if (match is Number) {
        "$column != $match"
    } else if (match == null) {
        "$column is not null"
    } else {
        "$column != '$match'"
    }

    return Clause(sql)
}

data class Clause(
    val sql: String
)
