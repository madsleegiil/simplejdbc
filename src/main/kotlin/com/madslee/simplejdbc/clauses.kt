package com.madslee.simplejdbc


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

data class Clause(
    val sql: String
)
