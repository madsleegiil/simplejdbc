package com.madslee.simplejdbc.util

internal val allLowerCaseSnakeCase: (camelCase: String) -> String = { word ->
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

    convert(toConvert = word)
}

fun Char.isUppercase() = this.uppercaseChar() == this