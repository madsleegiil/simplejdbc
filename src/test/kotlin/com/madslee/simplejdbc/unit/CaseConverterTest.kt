package com.madslee.simplejdbc.unit

import com.madslee.simplejdbc.util.allLowerCaseSnakeCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CaseConverterTest {

    @Test
    fun `can convert from camelCase to snake_case`() {
        assertThat(allLowerCaseSnakeCase("anExampleOfCamelCase")).isEqualTo("an_example_of_camel_case")
    }

    @Test
    fun `can convert from camelCase to snake_case when an uppercase char is immediately after other uppercase char`() {
        assertThat(allLowerCaseSnakeCase("anExampleOfSTRAngeCamelCase")).isEqualTo("an_example_of_strange_camel_case")
    }
}