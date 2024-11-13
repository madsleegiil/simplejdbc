package com.madslee.simplejdbc

import java.sql.Connection

class SimpleConnection(val connection: Connection) : Connection by connection {

    override fun prepareStatement(sql: String): SimplePreparedStatement =
        SimplePreparedStatement(connection.prepareStatement(sql))

}