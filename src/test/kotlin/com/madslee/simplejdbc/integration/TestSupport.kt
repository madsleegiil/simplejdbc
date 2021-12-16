package com.madslee.simplejdbc.integration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.sql.ResultSet
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

    fun toItem(resultSet: ResultSet) = Item(
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