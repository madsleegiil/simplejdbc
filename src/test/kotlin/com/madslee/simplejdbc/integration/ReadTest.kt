package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import javax.sql.DataSource

class ReadTest : TestSupport() {

    private val dataSource: DataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
            validate()
        })

    private val connection = dataSource.connection

    @Test
    fun `get saved items by specifying class only`() {
        val firstItem = connection.insertTestItem()
        val secondItem = connection.insertTestItem()

        val allRows = connection.select(Item::class)

        assertThat(allRows.size).isEqualTo(2)
        assertItemsAreEqual(allRows[0], firstItem)
        assertItemsAreEqual(allRows[1], secondItem)
    }

    @Test
    fun `get saved items with equals clause on number`() {
        val queryPrice = 45454.1
        val itemWithQueryPrice = connection.insertTestItem(price = queryPrice)
        connection.insertTestItem()

        val rows = connection.select(Item::class, whereEqual("price", queryPrice))

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryPrice)
    }

    @Test
    fun `get saved items with equals clause on string`() {
        val queryDescription = "Query description"
        val itemWithQueryDescription = connection.insertTestItem(description = queryDescription)
        connection.insertTestItem()

        val rows = connection.select(Item::class, whereEqual("description", queryDescription))

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryDescription)
    }

    @Test
    fun `get saved items with equals clause on date and number`() {
        val localDateTime = LocalDateTime.now()
        val price = 66.1
        val itemWithQueryValues = connection.insertTestItem(localDateTimeField = localDateTime, price = price)
        connection.insertTestItem()

        val rows = connection.select(
            Item::class,
            whereEqual("local_date_time_field", localDateTime),
            whereEqual("price", price)
        )

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryValues)
    }

    @Test
    fun `get saved items that equals on date but is unequal on number`() {
        val localDateTime = LocalDateTime.now()
        val unequalPrice = 1221121.93
        val price = 1.1
        val itemWithQueryValues = connection.insertTestItem(localDateTimeField = localDateTime, price = price)
        connection.insertTestItem()

        val rows = connection.select(
            Item::class,
            whereEqual("local_date_time_field", localDateTime),
            whereNotEqual("price", unequalPrice)
        )

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryValues)
    }

    @Test
    fun `get saved items that equals on one date field but where other date field is null`() {
        val localDateTime = LocalDateTime.now()
        val itemExpectedWithQuery = connection.insertTestItem(localDateTimeField = localDateTime, firstSale = null)
        connection.insertTestItem()

        val rows = connection.select(
            Item::class,
            whereEqual("local_date_time_field", localDateTime),
            whereEqual("first_sale", null)
        )

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
    }

    @Test
    fun `get saved items that with date greater than comparison`() {
        val compareDate = LocalDateTime.now().minusMonths(2)
        val itemExpectedWithQuery = connection.insertTestItem(localDateTimeField = compareDate.plusDays(1))
        connection.insertTestItem(localDateTimeField = compareDate)

        val rows = connection.select(
            Item::class,
            whereGreaterThan("local_date_time_field", compareDate)
        )

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
    }

    @Test
    fun `get saved items that with date lesser than comparison`() {
        val compareDate = LocalDateTime.now()
        val itemExpectedWithQuery = connection.insertTestItem(localDateTimeField = compareDate.minusDays(1))
        connection.insertTestItem(localDateTimeField = compareDate)

        val rows = connection.select(
            Item::class,
            whereLessThan("local_date_time_field", compareDate)
        )

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
    }

    @Test
    fun `get saved items that with date greater than or equal to comparison`() {
        val compareDate = LocalDateTime.now().minusMonths(2)
        val itemExpectedWithQuery = connection.insertTestItem(localDateTimeField = compareDate.plusDays(1))
        val otherItemExpectedWithQuery = connection.insertTestItem(localDateTimeField = compareDate)
        connection.insertTestItem(localDateTimeField = compareDate.minusDays(1))

        val rows = connection.select(
            Item::class,
            whereGreaterThanOrEqualTo("local_date_time_field", compareDate)
        )

        assertThat(rows.size).isEqualTo(2)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
        assertItemsAreEqual(rows[1], otherItemExpectedWithQuery)
    }

    @Test
    fun `get saved items that with date lesser than or equal to comparison`() {
        val compareDate = LocalDateTime.now()
        val itemExpectedWithQuery = connection.insertTestItem(localDateTimeField = compareDate.minusDays(1))
        val otherItemExpectedWithQuery = connection.insertTestItem(localDateTimeField = compareDate)
        connection.insertTestItem(localDateTimeField = compareDate.plusDays(1))

        val rows = connection.select(
            Item::class,
            whereLessThanOrEqualTo("local_date_time_field", compareDate)
        )

        assertThat(rows.size).isEqualTo(2)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
        assertItemsAreEqual(rows[1], otherItemExpectedWithQuery)
    }
}
