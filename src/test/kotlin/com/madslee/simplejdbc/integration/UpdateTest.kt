package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.insert
import com.madslee.simplejdbc.select
import com.madslee.simplejdbc.update
import com.madslee.simplejdbc.whereEqual
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.sql.DataSource

class UpdateTest: TestSupport() {

    private val dataSource: DataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
            validate()
        })

    private val connection = dataSource.connection

    @Test
    fun `Update price of one object by ID`() {
        val item = anItem
        val rowsAffected = connection.insert(item)
        assertThat(rowsAffected).isEqualTo(1)

        val itemWithUpdatedPrice = anItem.copy(price = 12434343.3)
        connection.update(itemWithUpdatedPrice, whereEqual("id", anItem.id))

        val allRows = connection.select(Item::class)
        assertThat(allRows.size).isEqualTo(1)
        assertItemsAreEqual(allRows.first(), itemWithUpdatedPrice)
    }
}