package com.madslee.simplejdbc.integration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.sql.DataSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class TestSupport {

    val table = "item"

    @BeforeAll
    fun beforeAll() {
        flywayMigrations(dataSource)
    }

    @AfterEach
    fun afterEach() {
        deleteAllFromTable(table)
    }


    val dataSource: DataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
            validate()
        })

    init {
        flywayMigrations(dataSource)
    }

    fun flywayMigrations(dataSource: DataSource) {
        Flyway.configure()
            .dataSource(dataSource)
            .load()
            .migrate()
    }

    fun deleteAllFromTable(table: String) {
        dataSource.connection.prepareStatement("delete from $table where 1 = 1;").executeUpdate()
    }

    fun getAllFromTable(table: String): ResultSet {
        return dataSource.connection.prepareStatement("select * from $table").executeQuery()
    }

    val item = Item(
        id = "123456789",
        description = "something something",
        price = 123.4,
        numberOfSales = 12,
        firstSale = LocalDate.now(),
        localDateTimeField = LocalDateTime.now(),
        zonedDateTimeField = ZonedDateTime.now()
    )

    fun toItem(resultSet: ResultSet) = Item(
        id = resultSet.getString("id"),
        description = resultSet.getString("description"),
        price = resultSet.getDouble("price"),
        numberOfSales = resultSet.getInt("number_of_sales"),
        firstSale = LocalDate.parse(resultSet.getString("first_sale")),
        localDateTimeField = resultSet.getObject("local_date_time_field", LocalDateTime::class.java),
        zonedDateTimeField = resultSet.getObject("zoned_date_time_field", ZonedDateTime::class.java)
    )

    data class Item(
        val id: String,
        val description: String,
        val price: Double,
        val numberOfSales: Int,
        val firstSale: LocalDate,
        val localDateTimeField: LocalDateTime,
        val zonedDateTimeField: ZonedDateTime,
    ) {
        fun asColumnsValues() = mapOf(
            "id" to id,
            "description" to description,
            "price" to price,
            "numberOfSales" to numberOfSales,
            "firstSale" to firstSale,
            "localDateTimeField" to localDateTimeField,
            "zonedDateTimeField" to zonedDateTimeField
        )
    }
}