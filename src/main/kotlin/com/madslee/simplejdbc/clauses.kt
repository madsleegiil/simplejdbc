package com.madslee.simplejdbc

private val equalOperator = "="
private val unequalOperator = "!="
private val isNullOperator = "is null"
private val isNotNullOperator = "is not null"

fun whereEqual(column: String, compareTo: Any?): Clause {
    val sql = if (compareTo is Number) numberCompare(column, compareTo, equalOperator)
        else if (compareTo == null) nullCompare(column, isNullOperator)
        else generalCompare(column, compareTo, equalOperator)

    return Clause(sql)
}

fun whereNotEqual(column: String, compareTo: Any?): Clause {
    val sql = if (compareTo is Number) numberCompare(column, compareTo, unequalOperator)
        else if (compareTo == null) nullCompare(column, isNotNullOperator)
        else generalCompare(column, compareTo, unequalOperator)

    return Clause(sql)
}

private fun generalCompare(column: String, compareTo: Any?, operator: String) = "$column $operator '$compareTo'"
private fun numberCompare(column: String, compareTo: Any?, operator: String) = "$column $operator $compareTo"
private fun nullCompare(column: String, compareTo: String) = "$column $compareTo"

data class Clause(
    val sql: String
)
