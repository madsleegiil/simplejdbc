package com.madslee.simplejdbc

import java.time.temporal.Temporal

private val equalOperator = "="
private val unequalOperator = "!="
private val isNullOperator = "is null"
private val isNotNullOperator = "is not null"
private val greaterOperator = ">"
private val lessOperator = "<"
private val greaterOrEqualOperator = ">="
private val lessOrEqualOperator = "<="

fun whereEqual(column: String, compareTo: Any?): WhereEqualClause {
    val sql =
        if (compareTo is Number) numberCompare(column, compareTo, equalOperator)
        else if (compareTo == null) nullCompare(column, isNullOperator)
        else generalCompare(column, compareTo, equalOperator)

    return WhereEqualClause(sql, column)
}

fun whereNotEqual(column: String, compareTo: Any?): WhereNotEqualClause {
    val sql =
        if (compareTo is Number) numberCompare(column, compareTo, unequalOperator)
        else if (compareTo == null) nullCompare(column, isNotNullOperator)
        else generalCompare(column, compareTo, unequalOperator)

    return WhereNotEqualClause(sql)
}

fun whereGreaterThan(column: String, compareTo: Any): CompareClause {
    val sql =
        if (compareTo is Number) numberCompare(column, compareTo, greaterOperator)
        else if (compareTo is Temporal) generalCompare(column, compareTo, greaterOperator)
        else throw UnsupportedOperationException("Not possible with 'greater than' clause on other than numbers and dates")

    return CompareClause(sql)
}

fun whereLessThan(column: String, compareTo: Any): CompareClause {
    val sql =
        if (compareTo is Number) numberCompare(column, compareTo, lessOperator)
        else if (compareTo is Temporal) generalCompare(column, compareTo, lessOperator)
        else throw UnsupportedOperationException("Not possible with 'less than' clause on other than numbers and dates")

    return CompareClause(sql)
}

fun whereGreaterThanOrEqualTo(column: String, compareTo: Any): CompareClause {
    val sql =
        if (compareTo is Number) numberCompare(column, compareTo, greaterOrEqualOperator)
        else if (compareTo is Temporal) generalCompare(column, compareTo, greaterOrEqualOperator)
        else throw UnsupportedOperationException("Not possible with 'greater than or equal to' clause on other than numbers and dates")

    return CompareClause(sql)
}

fun whereLessThanOrEqualTo(column: String, compareTo: Any): CompareClause {
    val sql =
        if (compareTo is Number) numberCompare(column, compareTo, lessOrEqualOperator)
        else if (compareTo is Temporal) generalCompare(column, compareTo, lessOrEqualOperator)
        else throw UnsupportedOperationException("Not possible with 'less than or equal to' clause on other than numbers and dates")

    return CompareClause(sql)
}

private fun generalCompare(column: String, compareTo: Any?, operator: String) = "$column $operator '$compareTo'"
private fun numberCompare(column: String, compareTo: Any?, operator: String) = "$column $operator $compareTo"
private fun nullCompare(column: String, compareTo: String) = "$column $compareTo"

interface Clause {
    val sql: String
}

data class WhereEqualClause(
    override val sql: String,
    val column: String
): Clause

data class WhereNotEqualClause(
    override val sql: String
): Clause

data class CompareClause(
    override val sql: String
): Clause