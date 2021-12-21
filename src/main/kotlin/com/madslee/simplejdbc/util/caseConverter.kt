package com.madslee.simplejdbc.util

internal fun String.sqlCase(): String {
    tailrec fun convert(toConvert: String, result: String = "", lastConvertedWasUppercase: Boolean = false): String {
        return if (toConvert.isEmpty()) {
            result
        } else {
            val char = toConvert[0]
            val lowerCaseChar = char.lowercaseChar()
            val restOfString = toConvert.substring(1)

            val addUnderscore = char.isUppercase() && !lastConvertedWasUppercase
            val temporaryResult = if (addUnderscore) "${result}_$lowerCaseChar" else "$result$lowerCaseChar"

            convert(restOfString, temporaryResult, char.isUppercase())
        }
    }

    return convert(toConvert = this)
}

fun Char.isUppercase() = this.uppercaseChar() == this