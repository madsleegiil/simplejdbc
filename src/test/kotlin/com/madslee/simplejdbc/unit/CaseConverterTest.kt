package com.madslee.simplejdbc.unit

import com.madslee.simplejdbc.util.camelCasetoSqlCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CaseConverterTest {

    @Test
    fun `can convert from camelCase to sql_case`() {
        assertThat("anExampleOfCamelCase".camelCasetoSqlCase()).isEqualTo("an_example_of_camel_case")
    }

    @Test
    fun `can convert from camelCase to sql_case when an uppercase char is immediately after other uppercase char`() {
        assertThat("anExampleOfSTRAngeCamelCase".camelCasetoSqlCase()).isEqualTo("an_example_of_strange_camel_case")
    }
}