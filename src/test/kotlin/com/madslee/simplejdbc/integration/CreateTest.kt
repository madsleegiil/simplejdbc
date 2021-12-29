package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.save
import com.madslee.simplejdbc.util.fieldsValuesMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateTest: TestSupport() {

    private val connection = dataSource.connection

    @Test
    fun `insert Item-object using columnsValues-map`() {
        val rowsAffected = connection.save(
            table = "item",
            columnsValues = anItem.asColumnsValues()
        )

        assertThat(rowsAffected).isEqualTo(1)
        assertWasSavedCorrectly(anItem)
    }

    @Test
    fun `insert Item-object using simple method`() {
        val rowsAffected = connection.save(anItem)

        assertThat(rowsAffected).isEqualTo(1)
        assertWasSavedCorrectly(anItem)
    }

    @Test
    fun `insert Item-object using overriding method`() {
        val overridingPrice = 99.1
        val overridingColumnValues = mapOf("price" to overridingPrice)

        val rowsAffected = connection.save(any = anItem, overridingColumnValues = overridingColumnValues)

        assertThat(rowsAffected).isEqualTo(1)
        val overridenItem = anItem.copy(price = overridingPrice)
        assertWasSavedCorrectly(overridenItem)
    }

    private fun assertWasSavedCorrectly(item: Item) {
        val resultSet = getAllFromTable(table)
        val allItems =  generateSequence {
            if (resultSet.next()) toItem(resultSet) else null
        }.toList()

        val databaseItem = allItems.first { it.id == item.id }

        val numberOfFieldsInItemClass = databaseItem.fieldsValuesMap.size - 1
        val numberOfAsserts = 6

        if (numberOfFieldsInItemClass != numberOfAsserts) {
            throw RuntimeException("Remember to add new asserts in assertWasSavedCorrectly(Item)")
        }

        assertThat(databaseItem.description).isEqualTo(item.description)
        assertThat(databaseItem.price).isEqualTo(item.price)
        assertThat(databaseItem.numberOfSales).isEqualTo(item.numberOfSales)
        assertThat(databaseItem.firstSale).isEqualTo(item.firstSale)
        assertThat(databaseItem.localDateTimeField).isEqualTo(item.localDateTimeField)
        assertThat(databaseItem.zonedDateTimeField).isEqualTo(item.zonedDateTimeField)
    }
}