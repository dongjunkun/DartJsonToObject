package main.kotlin.djk.yyy.jsontodart.utils.classblockparse

import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass
import main.kotlin.djk.yyy.jsontodart.classscodestruct.Property
import main.kotlin.djk.yyy.jsontodart.utils.getClassesStringList

/**
 * parser which to parse the code generate in nested class model and no comment and no annotation, means pure.
 */
@Deprecated("Please use #NestedClassModelClassesCodeParser")
class PureNestedClassModelClassesCodeParser(private val classesCode: String) {

    fun parse(): DartClass {

        if (classesCode.contains("//") || classesCode.contains("@")) {
            throw IllegalArgumentException("Can't support this classes code for it has comment or annotations $classesCode")
        }

        if (classesCode.contains("{").not()) {
            return parsedToKotlinDataClass(classesCode)
        } else {
            val trimedClassesCode = classesCode.trim()
            val tobeParsedCode = trimedClassesCode.substringBefore("{")
            val tobeParsedNestedClassesCode = trimedClassesCode.substringAfter("{").substringBeforeLast("}")
            val parentClass = parsedToKotlinDataClass(tobeParsedCode)
            val subClasses = getClassesStringList(tobeParsedNestedClassesCode).map { parsedToKotlinDataClass(it) }
            return parentClass.copy(nestedClasses = subClasses)
        }


    }

    private fun parsedToKotlinDataClass(classCode: String): DartClass {
        val tobeParsedCode = classCode.trim()
        val parsedKotlinDataClass = ClassCodeParser(tobeParsedCode).getDartClass()
        return toKotlinDataClass(parsedKotlinDataClass)
    }

    private fun toKotlinDataClass(parsedKotlinDataClass: ParsedDartClass): DartClass {
        val properties = parsedKotlinDataClass.properties.map {
            Property(
                annotations = listOf(),
                keyword = it.keyword,
                name = it.propertyName,
                type = it.propertyType,
                comment = it.propertyComment,
                value = it.propertyValue,
                isLast = it.isLastProperty
            )
        }
        return DartClass(annotations = listOf(), name = parsedKotlinDataClass.name, properties = properties)
    }

}