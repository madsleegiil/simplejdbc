package com.madslee.simplejdbc.unit

import com.madslee.simplejdbc.util.parameterizableInsertString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SqlUtilsTest {

    @Test
    fun `create parameterizable input string`() {
        val table = "animal"
        val columns = listOf("age", "number_of_legs", "weight")
        assertThat(parameterizableInsertString(table, columns)).isEqualTo("insert into animal (age, number_of_legs, weight) values (?, ?, ?);")
    }

}