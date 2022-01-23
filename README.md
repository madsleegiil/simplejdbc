# SimpleJDBC

SimpleJDBS is a small Kotlin library with extension functions on java.sql.Connection making it easy to perform simple CRUD operations with Kotlin data classes. The library does not supportany database relations, such as one-to-one, many-to-one etc.

Still under development, no guarantees given.

## Usage

As a general rule, the name of the data class must be equal to the name of a DB table. Likewise, all data class fields must have corresponding table columns.
The library handles the conversion of camelCase in Kotlin to snake_case in SQL.

### Insert

Using the object of a data class as only parameter:

    connection.insert(dataClassObject)

If the DB table has a different name than the data class, a second parameter can be given:

    connection.insert(dataClassObject, "my_db_table")

### Select

Using the data class type as only parameter:

    val myList: List<DataClass> = connection.select(DataClass::class)

If the DB table has a different name than the data class, a second parameter can be given:

    val myList: List<DataClass> = connection.select(DataClass::class, "my_db_table"). 

Select operations support constraints. One or more constraints are accepted as varargs:

    val myList: List<DataClass> = connection.select(DataClass::class, whereEqual("price", 165), whereEqual(name, "name))
    
    val myList: List<DataClass> = connection.select(DataClass::class, "my_db_table", whereEqual("price", 165)

### Update

Update one object by specifying a where clause:

    val rowsAffected = connection.update(updatedDataClassObject, whereEqual("field", oldValue)

If the DB table has a different name than the data class, the table name can be specified:

    val rowsAffected = connection.update(updatedDataClassObject, "table", whereEqual("field", oldValue) 

Take caution when specifying a column in the where clause that is not the primary key, as this may result in an Exception.

### Constraints
The following constraints are offered on select, update and delete:

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
- update multiple rows
- delete operations
- batch savings
- allow saving the whole object as one Json string
- allow saving all fields of an object as Json strings
