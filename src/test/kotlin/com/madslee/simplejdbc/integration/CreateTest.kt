package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.insert
import com.madslee.simplejdbc.util.fieldsValuesMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateTest: TestSupport() {

    private val connection = dataSource.connection

    @Test
    fun `insert Item-object`() {
        val rowsAffected = connection.insert(anItem)

        assertThat(rowsAffected).isEqualTo(1)
        assertWasSavedCorrectly(anItem)
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