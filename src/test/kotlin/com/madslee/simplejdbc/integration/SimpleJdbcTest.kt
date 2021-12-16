package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.getAll
import com.madslee.simplejdbc.save
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.ResultSet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleJdbcTest: TestSupport() {

    private val table = "item"

    @BeforeAll
    fun beforeAll() {
        flywayMigrations(dataSource)
    }

    @AfterEach
    fun afterEach() {
        deleteAllFromTable(table)
    }

    @Test
    fun `insert Item-object using columnsValues-map`() {
        val item = Item(
            id = "123456789",
            description = "something something",
            price = 123.4,
            numberOfSales = 12
        )

        val rowsAffected = save(
            table = "item",
            columnsValues = mapOf(
                "id" to item.id,
                "description" to item.description,
                "price" to item.price,
                "numberOfSales" to item.numberOfSales
            ),
            connection = dataSource.connection
        )

        assertThat(rowsAffected).isEqualTo(1)
        assertWasSaved(item)
    }

    @Test
    fun `insert Item-object using simple method`() {
        val item = Item(
            id = "123456789",
            description = "something something",
            price = 123.4,
            numberOfSales = 12
        )

        val rowsAffected = save(item, dataSource.connection)

        assertThat(rowsAffected).isEqualTo(1)
        assertWasSaved(item)
    }

    @Test
    fun `insert Item-object using overriding method`() {
        val item = Item(
            id = "123456789",
            description = "something something",
            price = 123.4,
            numberOfSales = 12
        )
        val overridingPrice = 99.1
        val overridingColumnValues = mapOf("price" to overridingPrice)

        val rowsAffected = save(any = item, overridingColumnValues = overridingColumnValues, connection = dataSource.connection)

        assertThat(rowsAffected).isEqualTo(1)
        val overridenItem = item.copy(price = overridingPrice)
        assertWasSaved(overridenItem)
    }

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

    @Test
    fun `get saved items by specifying table only`() {
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
        val allRows = getAll(Item::class, table)

        // Results
        assertThat(allRows.size).isEqualTo(2)
        assertThat(allRows).containsExactly(firstItem, secondItem)
    }

    private fun assertWasSaved(item: Item) {
        val resultSet = getAllFromTable(table)
        val allItems =  generateSequence {
            if (resultSet.next()) toItem(resultSet) else null
        }.toList()
        assertThat(allItems).contains(item)
    }

    private fun toItem(resultSet: ResultSet) = Item(
        id = resultSet.getString("id"),
        description = resultSet.getString("description"),
        price = resultSet.getDouble("price"),
        numberOfSales = resultSet.getInt("number_of_sales")
    )

    data class Item(
        val id: String,
        val description: String,
        val price: Double,
        val numberOfSales: Int
    )
}
