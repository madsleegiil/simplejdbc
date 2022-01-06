package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.util.fieldsValuesMap
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions
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

    private val dataSource: DataSource = HikariDataSource(
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

    val anItem = Item(
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

    fun assertItemsAreEqual(actualItem: Item, expectedItem: Item) {
        val numberOfFieldsInItemClass = actualItem.fieldsValuesMap.size
        val numberOfAsserts = 7

        if (numberOfFieldsInItemClass != numberOfAsserts) {
            throw RuntimeException("Remember to add new asserts in assertWasSavedCorrectly(Item)")
        }

        Assertions.assertThat(actualItem.id).isEqualTo(expectedItem.id)
        Assertions.assertThat(actualItem.description).isEqualTo(expectedItem.description)
        Assertions.assertThat(actualItem.price).isEqualTo(expectedItem.price)
        Assertions.assertThat(actualItem.numberOfSales).isEqualTo(expectedItem.numberOfSales)
        Assertions.assertThat(actualItem.firstSale).isEqualTo(expectedItem.firstSale)
        Assertions.assertThat(actualItem.localDateTimeField).isEqualTo(expectedItem.localDateTimeField)
        Assertions.assertThat(actualItem.zonedDateTimeField).isEqualTo(expectedItem.zonedDateTimeField)
    }

    data class Item(
        val id: String,
        val description: String,
        val price: Double,
        val numberOfSales: Int,
        val firstSale: LocalDate?,
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