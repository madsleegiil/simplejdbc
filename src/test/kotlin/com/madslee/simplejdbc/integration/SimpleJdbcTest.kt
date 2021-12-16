package com.madslee.simplejdbc.integration

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
