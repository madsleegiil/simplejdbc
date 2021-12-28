package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.save
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CreateTest: TestSupport() {

    private val connection = dataSource.connection

    @Test
    fun `insert Item-object using columnsValues-map`() {
        val rowsAffected = connection.save(
            table = "item",
            columnsValues = item.asColumnsValues()
        )

        Assertions.assertThat(rowsAffected).isEqualTo(1)
        assertWasSaved(item)
    }

    @Test
    fun `insert Item-object using simple method`() {
        val rowsAffected = connection.save(item)

        Assertions.assertThat(rowsAffected).isEqualTo(1)
        assertWasSaved(item)
    }

    @Test
    fun `insert Item-object using overriding method`() {
        val overridingPrice = 99.1
        val overridingColumnValues = mapOf("price" to overridingPrice)

        val rowsAffected = connection.save(any = item, overridingColumnValues = overridingColumnValues)

        Assertions.assertThat(rowsAffected).isEqualTo(1)
        val overridenItem = item.copy(price = overridingPrice)
        assertWasSaved(overridenItem)
    }

    private fun assertWasSaved(item: Item) {
        val resultSet = getAllFromTable(table)
        val allItems =  generateSequence {
            if (resultSet.next()) toItem(resultSet) else null
        }.toList()
        Assertions.assertThat(allItems).contains(item)
    }
}