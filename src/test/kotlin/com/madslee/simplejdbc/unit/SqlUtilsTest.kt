package com.madslee.simplejdbc.unit

import com.madslee.simplejdbc.integration.TestSupport
import com.madslee.simplejdbc.util.createParameterizableInsertStatement
import com.madslee.simplejdbc.util.getPrimaryKeys
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.sql.DataSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqlUtilsTest {

    val dataSource: DataSource = EmbeddedPostgres.start().postgresDatabase

    @BeforeAll
    fun setup() {
        TestSupport().flywayMigrations(dataSource)
    }

    @Test
    fun `create parameterizable input string`() {
        val table = "animal"
        val columns = listOf("age", "number_of_legs", "weight")
        assertThat(createParameterizableInsertStatement(table, columns)).isEqualTo("insert into animal (age, number_of_legs, weight) values (?, ?, ?);")
    }

    @Test
    fun `get primary keys of table`() {
        val table = "item"
        val primaryKeyColumns = dataSource.connection.getPrimaryKeys(table)
        assertThat(primaryKeyColumns).containsExactly("ID")
    }
}