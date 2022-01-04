package com.madslee.simplejdbc

private val equalOperator = "="
private val unequalOperator = "!="
private val isNullOperator = "is null"
private val isNotNullOperator = "is not null"

fun whereEqual(column: String, match: Any?): Clause {
    val sql = if (match is Number) {
        numberCompare(column, match, equalOperator)
    } else {
        val operator = if (match == null) isNullOperator else equalOperator
        generalCompare(column, match, operator)
    }

    return Clause(sql)
}

fun whereNotEqual(column: String, match: Any?): Clause {
    val sql = if (match is Number) {
        numberCompare(column, match, unequalOperator)
    } else {
        val operator = if (match == null) isNotNullOperator else unequalOperator
        generalCompare(column, match, operator)
    }

    return Clause(sql)
}

private fun generalCompare(column: String, match: Any?, operator: String) = "$column $operator '$match'"
private fun numberCompare(column: String, match: Any?, operator: String) = "$column $operator $match"

data class Clause(
    val sql: String
)
