package com.madslee.simplejdbc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClassAnalysisTest {

    @Test
    fun `can get all fields for given class`() {
        val classInstance = SimpleTestClass("Certain Name", 19)
        val fields = fieldNames(classInstance)
        assertThat(fields).containsExactlyInAnyOrder("name", "age")
    }

    @Test
    fun `can get map of fields for given class`() {
        val classInstance = SimpleTestClass("Certain Name", 19)
        val fieldMap = fieldMap(classInstance)
        assertThat(fieldMap).isEqualTo(mapOf("name" to "Certain Name", "age" to 19))
    }
}

data class SimpleTestClass(
    val name: String,
    val age: Int
)