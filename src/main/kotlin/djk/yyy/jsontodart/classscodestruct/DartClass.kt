package main.kotlin.djk.yyy.jsontodart.classscodestruct

import main.kotlin.djk.yyy.jsontodart.interceptor.IDartClassInterceptor
import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.ParsedDartClass
import main.kotlin.djk.yyy.jsontodart.utils.getCommentCode
import main.kotlin.djk.yyy.jsontodart.utils.getIndent

data class DartClass(
        val id: Int = -1, // -1 represent the default unknown id
        val annotations: List<Annotation>,
        val name: String,
        val properties: List<Property>,
        val nestedClasses: List<DartClass> = listOf(),
        val parentClassTemplate: String = ""
) {

    fun getCode(extraIndent: String = ""): String {
        val indent = getIndent()
        val code = buildString {
            if (annotations.isNotEmpty()) {
                val annotationsCode = annotations.map { it.getAnnotationString() }.joinToString("\n")
                if (annotationsCode.isNotBlank()) {
                    append(annotationsCode).append("\n")
                }
            }
            append("class ").append(name).append("(").append("\n")
            properties.forEach {
                val code = it.getCode()
                val addIndentCode = code.split("\n").joinToString("\n") { indent + it }
                append(addIndentCode)
                if (it.isLast.not()) append(",")
                if (it.comment.isNotBlank()) append(" // ").append(getCommentCode(it.comment))
                append("\n")
            }
            append(")")
            if (parentClassTemplate.isNotBlank()) {
                append(" : ")
                append(parentClassTemplate)
            }
            if (nestedClasses.isNotEmpty()) {
                append(" {")
                append("\n")
                val nestedClassesCode = nestedClasses.joinToString("\n\n") { it.getCode(extraIndent = indent) }
                append(nestedClassesCode)
                append("\n")
                append("}")
            }
        }
        if (extraIndent.isNotEmpty()) {
            return code.split("\n").map {
                if (it.isNotBlank()) {
                    extraIndent + it
                } else {
                    it
                }
            }.joinToString("\n")
        } else {
            return code
        }
    }

    fun toParsedKotlinDataClass(): ParsedDartClass {

        val annotationCodeList = annotations.map { it.getAnnotationString() }

        val parsedProperties = properties.map { it.toParsedProperty() }

        return ParsedDartClass(annotationCodeList, name, parsedProperties)
    }

    fun applyInterceptors(interceptors: List<IDartClassInterceptor>): DartClass {
        var kotlinDataClass = this
        interceptors.forEach {
            kotlinDataClass = kotlinDataClass.applyInterceptorWithNestedClasses(it)
        }
        return kotlinDataClass
    }

    fun applyInterceptorWithNestedClasses(interceptor: IDartClassInterceptor): DartClass {
        if (nestedClasses.isNotEmpty()) {
            val newNestedClasses = nestedClasses.map { it.applyInterceptorWithNestedClasses(interceptor) }
            return interceptor.intercept(this).copy(nestedClasses = newNestedClasses)
        }
        return interceptor.intercept(this)
    }

    companion object {

        fun fromParsedDartClass(parsedDartClass: ParsedDartClass): DartClass {
            val annotations = parsedDartClass.annotations.map { Annotation.fromAnnotationString(it) }
            val properties = parsedDartClass.properties.map { Property.fromParsedProperty(it) }
            return DartClass(
                annotations = annotations,
                id = -1,
                name = parsedDartClass.name,
                properties = properties
            )
        }

    }

}