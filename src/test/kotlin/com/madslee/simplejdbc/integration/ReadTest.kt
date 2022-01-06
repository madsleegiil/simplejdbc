package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class ReadTest : TestSupport() {

    private val connection = dataSource.connection

    @Test
    fun `get saved items by specifying class only`() {
        val firstItem = anItem
        val secondItem = anItem.copy(id = "987654321")
        connection.insert(firstItem)
        connection.insert(secondItem)

        val allRows = connection.select(Item::class)

        assertThat(allRows.size).isEqualTo(2)
        assertItemsAreEqual(allRows[0], firstItem)
        assertItemsAreEqual(allRows[0], firstItem)
    }

    @Test
    fun `get saved items with equals clause on number`() {
        val queryPrice = 12.1
        val itemWithQueryPrice = anItem.copy(price = queryPrice)
        val otherItem = anItem.copy(id = "987654321", price = 123.21)
        connection.insert(itemWithQueryPrice)
        connection.insert(otherItem)

        val rows = connection.select(Item::class, whereEqual("price", queryPrice))

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryPrice)
    }

    @Test
    fun `get saved items with equals clause on string`() {
        val queryDescription = "Query description"
        val itemWithQueryDescription = anItem.copy(description = queryDescription)
        val otherItem = anItem.copy(id = "987654321", description = "other description")
        connection.insert(itemWithQueryDescription)
        connection.insert(otherItem)

        val rows = connection.select(Item::class, whereEqual("description", queryDescription))

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryDescription)
    }

    @Test
    fun `get saved items with equals clause on date and number`() {
        val localDateTime = LocalDateTime.now()
        val price = 12.1
        val itemWithQueryValues = anItem.copy(localDateTimeField = localDateTime, price = price)
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = LocalDateTime.now().minusDays(1), price = 12211.1)
        connection.insert(itemWithQueryValues)
        connection.insert(otherItem)

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
        val unequalPrice = 12.1
        val itemExpectedWithQuery = anItem.copy(localDateTimeField = localDateTime, price = 12421.12)
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = LocalDateTime.now().minusDays(1), price = unequalPrice)
        connection.insert(itemExpectedWithQuery)
        connection.insert(otherItem)

        val rows = connection.select(
            Item::class,
            whereEqual("local_date_time_field", localDateTime),
            whereNotEqual("price", unequalPrice)
        )

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
    }

    @Test
    fun `get saved items that equals on one date field but where other date field is null`() {
        val localDateTime = LocalDateTime.now()
        val itemExpectedWithQuery = anItem.copy(localDateTimeField = localDateTime, firstSale = null)
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = LocalDateTime.now().minusDays(1), firstSale = LocalDate.now())
        connection.insert(itemExpectedWithQuery)
        connection.insert(otherItem)

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
        val itemExpectedWithQuery = anItem.copy(localDateTimeField = compareDate.plusDays(1))
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = compareDate)
        connection.insert(itemExpectedWithQuery)
        connection.insert(otherItem)

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
        val itemExpectedWithQuery = anItem.copy(localDateTimeField = compareDate.minusDays(1))
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = compareDate)
        connection.insert(itemExpectedWithQuery)
        connection.insert(otherItem)

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
        val itemExpectedWithQuery = anItem.copy(localDateTimeField = compareDate.plusDays(1))
        val otherItemExpectedWithQuery = anItem.copy(id = "3265", localDateTimeField = compareDate)
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = compareDate.minusDays(1))
        connection.insert(itemExpectedWithQuery)
        connection.insert(otherItemExpectedWithQuery)
        connection.insert(otherItem)

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
        val itemExpectedWithQuery = anItem.copy(localDateTimeField = compareDate.minusDays(1))
        val otherItemExpectedWithQuery = anItem.copy(id = "656565", localDateTimeField = compareDate)
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = compareDate.plusDays(1))
        connection.insert(itemExpectedWithQuery)
        connection.insert(otherItemExpectedWithQuery)
        connection.insert(otherItem)

        val rows = connection.select(
            Item::class,
            whereLessThanOrEqualTo("local_date_time_field", compareDate)
        )

        assertThat(rows.size).isEqualTo(2)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
        assertItemsAreEqual(rows[1], otherItemExpectedWithQuery)
    }
}
