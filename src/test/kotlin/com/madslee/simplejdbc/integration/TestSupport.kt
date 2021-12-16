package com.madslee.simplejdbc.integration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import java.sql.ResultSet
import javax.sql.DataSource

open class TestSupport {

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
}