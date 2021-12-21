package com.madslee.simplejdbc.util

/**
 * Converts the string to all lower case snake case
 */
internal fun String.toSqlCase(): String {
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

private fun Char.isUppercase() = this.uppercaseChar() == this