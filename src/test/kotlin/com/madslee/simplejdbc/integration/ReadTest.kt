package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.getAll
import com.madslee.simplejdbc.save
import com.madslee.simplejdbc.whereEqual
import com.madslee.simplejdbc.whereNotEqual
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ReadTest : TestSupport() {

    private val connection = dataSource.connection

    @Test
    fun `get saved items by specifying class only`() {
        val firstItem = anItem
        val secondItem = anItem.copy(id = "987654321")
        connection.save(firstItem)
        connection.save(secondItem)

        val allRows = connection.getAll(Item::class)

        assertThat(allRows.size).isEqualTo(2)
        assertItemsAreEqual(allRows[0], firstItem)
        assertItemsAreEqual(allRows[0], firstItem)
    }

    @Test
    fun `get saved items with equals clause on number`() {
        val queryPrice = 12.1
        val itemWithQueryPrice = anItem.copy(price = queryPrice)
        val otherItem = anItem.copy(id = "987654321", price = 123.21)
        connection.save(itemWithQueryPrice)
        connection.save(otherItem)

        val rows = connection.getAll(Item::class, whereEqual("price", queryPrice))

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryPrice)
    }

    @Test
    fun `get saved items with equals clause on string`() {
        val queryDescription = "Query description"
        val itemWithQueryDescription = anItem.copy(description = queryDescription)
        val otherItem = anItem.copy(id = "987654321", description = "other description")
        connection.save(itemWithQueryDescription)
        connection.save(otherItem)

        val rows = connection.getAll(Item::class, whereEqual("description", queryDescription))

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemWithQueryDescription)
    }

    @Test
    fun `get saved items with equals clause on date and number`() {
        val localDateTime = LocalDateTime.now()
        val price = 12.1
        val itemWithQueryValues = anItem.copy(localDateTimeField = localDateTime, price = price)
        val otherItem = anItem.copy(id = "987654321", localDateTimeField = LocalDateTime.now().minusDays(1), price = 12211.1)
        connection.save(itemWithQueryValues)
        connection.save(otherItem)

        val rows = connection.getAll(
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
        connection.save(itemExpectedWithQuery)
        connection.save(otherItem)

        val rows = connection.getAll(
            Item::class,
            whereEqual("local_date_time_field", localDateTime),
            whereNotEqual("price", unequalPrice)
        )

        assertThat(rows.size).isEqualTo(1)
        assertItemsAreEqual(rows.first(), itemExpectedWithQuery)
    }
}
