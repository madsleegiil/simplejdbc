package com.madslee.simplejdbc

import java.sql.Connection

fun save(any: Any, table: String, connection: Connection) {

}

/*
fun save(table: String, columnsValues: Map<String, Any>, connection: Connection) {

}
 */

val insertString: (any: Any, table: String) -> String = { any, table ->
    TODO("NOT implemented")
}