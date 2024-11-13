package com.madslee.simplejdbc

import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.ZonedDateTime

class SimplePreparedStatement(val statement: PreparedStatement): PreparedStatement by statement {

    override fun setObject(parameterIndex: Int, any: Any) {
        val castedObject = getCastedObject(any)
        statement.setObject(parameterIndex, castedObject)
    }

    private fun getCastedObject(any: Any): Any {
        return when (any) {
            is ZonedDateTime -> Timestamp.from(any.toInstant())
            else -> any
        }
    }
}