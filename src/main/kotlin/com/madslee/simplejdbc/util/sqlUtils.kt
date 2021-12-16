package com.madslee.simplejdbc.util

val parameterizableInsertString = { table: String, columns: List<String> ->
    "insert into $table (${columns.joinToString(", ")}) values (${columns.map { "?" }.joinToString(", ")});"
}