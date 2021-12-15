package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.save
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.sql.DataSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleJdbcTest {

    @BeforeAll
    fun beforeAll() {
        flywayMigrations(dataSource)
    }

    // TODO: AfterEach drop all content

    @Test
    fun `insert Item-object using columnsValues-map`() {
        val item = Item(
            id = "123456789",
            description = "something something",
            price = 123.4,
            numberOfSales = 12
        )

        save(
            table = "item",
            columnsValues = mapOf(
                "id" to item.id,
                "description" to item.description,
                "price" to item.price,
                "numberOfSales" to item.numberOfSales
            ),
            connection = dataSource.connection
        )
    }

    @Test
    fun `insert Item-object using simple method`() {
        val item = Item(
            id = "123456789",
            description = "something something",
            price = 123.4,
            numberOfSales = 12
        )
        save(item, dataSource.connection)
    }

    data class Item(
        val id: String,
        val description: String,
        val price: Double,
        val numberOfSales: Int
    )

    private val dataSource: DataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
            validate()
        })

    fun flywayMigrations(dataSource: DataSource) {
        Flyway.configure()
            .dataSource(dataSource)
            .load()
            .migrate()

    }
}
