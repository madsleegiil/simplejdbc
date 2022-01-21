# SimpleJDBC

SimpleJDBS is a small Kotlin library with extension functions on java.sql.Connection making it easy to perform simple CRUD operations with Kotlin data classes.  

Still under development, no guarantees given.

## Usage

As a general rule, the name of the data class must be equal to the name of a DB table. Likewise, all data class fields must have corresponding table columns.
The library handles the conversion of camelCase in Kotlin to snake_case in SQL.

### Insert

Using object of a data class as only parameter:

    connection.insert(dataClassObject)

If the DB table has a different name than the data class, a second parameter can be given:

    connection.insert(dataClassObject, "my_db_table")

### Select

Using the data class type as only parameter:

    val myDataClassObject = connection.select(DataClass::class)

If the DB table has a different name than the data class, a second parameter can be given:

    val myDataClassObject = connection.select(DataClass::class, "my_db_table"). 

Select operations support constraints. One or more constraints are accepted as varargs:

    val myDataClassObject = connection.select(DataClass::class, whereEqual("price", 165), whereEqual(name, "name))
    
    val otherDataClassObject = connection.select(DataClass::class, "my_db_table", whereEqual("price", 165)

The following constraints are offered:

    whereEqual("columnName", Any?)
    whereNotEqual("columnName", Any?)
    whereGreaterThan("columnName", Any)
    whereLessThan("columName", Any)
    whereGreaterThanOrEqualTo("columName", Any)
    whereLessThanOrEqualTo("columnName", Any)

## Supported data types

- Int
- Double
- String
- LocalDate
- LocalDateTime
- ZonedDateTime

More data types may function, but have not been tested. 

### Future additions
- update operations
- delete operations
- batch savings
- allow saving the whole object as one Json string
- allow saving all fields of an object as Json strings
