package com.madslee.simplejdbc.unit

import com.madslee.simplejdbc.util.className
import com.madslee.simplejdbc.util.fieldsValuesMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClassAnalysisTest {

    @Test
    fun `can get name of class`() {
        val classInstance = SimpleTestClass("Certain Name", 19)
        val name = classInstance.className
        assertThat(name).isEqualTo("SimpleTestClass")
    }

    @Test
    fun `can get map of fields for given class`() {
        val classInstance = SimpleTestClass("Certain Name", 19)
        val fieldMap = classInstance.fieldsValuesMap
        assertThat(fieldMap).containsExactlyInAnyOrderEntriesOf(mapOf(Pair("name", "Certain Name"), Pair("age", 19)))
    }
}

data class SimpleTestClass(
    val name: String,
    val age: Int
)