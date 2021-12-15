package com.madslee.simplejdbc.unit

import com.madslee.simplejdbc.className
import com.madslee.simplejdbc.fieldMap
import com.madslee.simplejdbc.fieldNames
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClassAnalysisTest {

    @Test
    fun `can get name of class`() {
        val classInstance = SimpleTestClass("Certain Name", 19)
        val name = className(classInstance)
        assertThat(name).isEqualTo("SimpleTestClass")
    }

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
        assertThat(fieldMap).containsExactlyInAnyOrder(Pair("name", "Certain Name"), Pair("age", 19))
    }
}

data class SimpleTestClass(
    val name: String,
    val age: Int
)