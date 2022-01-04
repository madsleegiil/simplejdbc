package com.madslee.simplejdbc

private val equalOperator = "="
private val unequalOperator = "!="
private val isNullOperator = "is null"
private val isNotNullOperator = "is not null"

fun whereEqual(column: String, match: Any?): Clause {
    val sql = if (match is Number) numberCompare(column, match, equalOperator)
        else if (match == null) nullCompare(column, isNullOperator)
        else generalCompare(column, match, equalOperator)

    return Clause(sql)
}

fun whereNotEqual(column: String, match: Any?): Clause {
    val sql = if (match is Number) numberCompare(column, match, unequalOperator)
        else if (match == null) nullCompare(column, isNotNullOperator)
        else generalCompare(column, match, unequalOperator)

    return Clause(sql)
}

private fun generalCompare(column: String, match: Any?, operator: String) = "$column $operator '$match'"
private fun numberCompare(column: String, match: Any?, operator: String) = "$column $operator $match"
private fun nullCompare(column: String, operator: String) = "$column $operator"

data class Clause(
    val sql: String
)
