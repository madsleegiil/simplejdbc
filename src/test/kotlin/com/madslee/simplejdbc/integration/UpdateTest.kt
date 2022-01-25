package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.insert
import com.madslee.simplejdbc.select
import com.madslee.simplejdbc.updateById
import com.madslee.simplejdbc.whereEqual
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.sql.DataSource

class UpdateTest: TestSupport() {

    // TODO: Add tests updating row with composite primary key
    // TODO: Add test failing because of no primary key

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
        val item = connection.insertTestItem()

        val itemWithUpdatedPrice = item.copy(price = 12434343.3)
        connection.updateById(itemWithUpdatedPrice)

        val allRows = connection.select(Item::class)
        assertThat(allRows.size).isEqualTo(1)
        assertItemsAreEqual(allRows.first(), itemWithUpdatedPrice)
    }
}