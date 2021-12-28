package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.getAll
import com.madslee.simplejdbc.save
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReadTest: TestSupport() {

    private val connection = dataSource.connection

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
        connection.save(firstItem)
        connection.save(secondItem)

        // Action
        val wantedColumns = listOf("number_of_sales", "price", "id")
        val allRows = connection.getAll(table, wantedColumns)

        // Results
        assertThat(allRows.size).isEqualTo(2)

        val expectedSavedItems = listOf(firstItem, secondItem)
        allRows.forEachIndexed { index, map ->
            assertThat(allRows[index]["number_of_sales"]).isEqualTo(expectedSavedItems[index].numberOfSales)
            assertThat(allRows[index]["price"]).isEqualTo(expectedSavedItems[index].price)
            assertThat(allRows[index]["id"]).isEqualTo(expectedSavedItems[index].id)
        }
    }

   @Test
   fun `get saved items by specifying class only`() {
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
       connection.save(firstItem)
       connection.save(secondItem)

       // Action
       val allRows = connection.getAll(Item::class)

       // Results
       assertThat(allRows.size).isEqualTo(2)
       assertThat(allRows).containsExactly(firstItem, secondItem)
   }
}
