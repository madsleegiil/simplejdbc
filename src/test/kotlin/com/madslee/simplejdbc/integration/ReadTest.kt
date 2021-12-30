package com.madslee.simplejdbc.integration

import com.madslee.simplejdbc.getAll
import com.madslee.simplejdbc.save
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReadTest: TestSupport() {

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
}
