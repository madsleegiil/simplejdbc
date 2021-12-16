package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.getAll
import com.madslee.simplejdbc.save
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReadTest: TestSupport() {

    @Test
    fun `get values of saved Item-objects by specifying columns`() {
        // Setup
        val firstItem = Item(
            id = "123456789",
            description = "something something",
            price = 123.4,
            numberOfSales = 12
        )
        val secondItem = Item(
            id = "987654321",
            description = "something something",
            price = 123.4,
            numberOfSales = 12
        )
        save(firstItem, dataSource.connection)
        save(secondItem, dataSource.connection)

        // Action
        val wantedColumns = listOf("number_of_sales", "price", "id")
        val allRows = getAll(table, wantedColumns, dataSource.connection)

        // Results
        assertThat(allRows.size).isEqualTo(2)

        val expectedSavedItems = listOf(firstItem, secondItem)
        allRows.forEachIndexed { index, map ->
            assertThat(allRows[index]["number_of_sales"]).isEqualTo(expectedSavedItems[index].numberOfSales)
            assertThat(allRows[index]["price"]).isEqualTo(expectedSavedItems[index].price)
            assertThat(allRows[index]["id"]).isEqualTo(expectedSavedItems[index].id)
        }
    }
//
//    @Test
//    fun `get saved items by specifying table only`() {
//        // Setup
//        val firstItem = Item(
//            id = "123456789",
//            description = "something something",
//            price = 123.4,
//            numberOfSales = 12
//        )
//        val secondItem = Item(
//            id = "987654321",
//            description = "something something",
//            price = 123.4,
//            numberOfSales = 12
//        )
//        save(firstItem, dataSource.connection)
//        save(secondItem, dataSource.connection)
//
//        // Action
//        val allRows = getAll(Item::class, table)
//
//        // Results
//        assertThat(allRows.size).isEqualTo(2)
//        assertThat(allRows).containsExactly(firstItem, secondItem)
//    }
}
