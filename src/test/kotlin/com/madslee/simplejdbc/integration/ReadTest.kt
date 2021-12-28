package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.getAll
import com.madslee.simplejdbc.save
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ReadTest: TestSupport() {

    private val connection = dataSource.connection

    @Test
    fun `get values of saved Item-objects by specifying columns`() {
        val firstItem = item
        val secondItem = item.copy(id = "987654321")
        connection.save(firstItem)
        connection.save(secondItem)

        val wantedColumns = mapOf(
            "number_of_sales" to Int::class,
            "price" to Float::class,
            "id" to String::class,
            "first_sale" to LocalDate::class,
        )
        val allRows = connection.getAll(table, wantedColumns)

        assertThat(allRows.size).isEqualTo(2)
        val expectedSavedItems = listOf(firstItem, secondItem)
        allRows.forEachIndexed { index, map ->
            assertThat(allRows[index]["number_of_sales"]).isEqualTo(expectedSavedItems[index].numberOfSales)
            assertThat(allRows[index]["price"]).isEqualTo(expectedSavedItems[index].price)
            assertThat(allRows[index]["id"]).isEqualTo(expectedSavedItems[index].id)
            assertThat(allRows[index]["first_sale"]).isEqualTo(expectedSavedItems[index].firstSale)
        }
    }

   @Test
   fun `get saved items by specifying class only`() {
       val firstItem = item
       val secondItem = item.copy(id = "987654321")
       connection.save(firstItem)
       connection.save(secondItem)

       val allRows = connection.getAll(Item::class)

       assertThat(allRows.size).isEqualTo(2)
       assertThat(allRows).containsExactly(firstItem, secondItem)
   }
}
