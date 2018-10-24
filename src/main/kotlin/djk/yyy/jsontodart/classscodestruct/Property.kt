package main.kotlin.djk.yyy.jsontodart.classscodestruct

import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.ParsedDartClass

data class Property(
        val annotations: List<Annotation>,
        val keyword: String,
        val name: String,
        val type: String,
        val value: String,
        val comment: String,
        val isLast: Boolean,
        val refTypeId: Int = -1, // the id of property type,if can't reference in current generate classes ,use the default value -1
        val originName: String = ""
) {

    fun getCode(): String {

        val code = buildString {
            if (annotations.isNotEmpty()) {
                val annotationsCode = annotations.map { it.getAnnotationString() }.joinToString("\n")
                if (annotationsCode.isNotBlank()) {
                    append(annotationsCode).append("\n")
                }
            }
            append(keyword).append(" ").append(name).append(": ").append(type)
            if (value.isNotBlank()) {
                append(" = ").append(value)
            }
        }
        return code
    }

    fun toParsedProperty(): ParsedDartClass.Property {

        val propertyAnnotationCodeList = annotations.map { annotation -> annotation.getAnnotationString() }

        return ParsedDartClass.Property(
            propertyAnnotationCodeList,
            keyword,
            name,
            type,
            value,
            comment,
            isLast
        )

    }

    fun getRawName(): String {

        return if (annotations.isNotEmpty()) {
            val notBlankRawNames = annotations.map { it.rawName }.filter { it.isNotBlank() }
            if (notBlankRawNames.isNotEmpty()) {
                notBlankRawNames[0]
            } else {
                ""
            }
        } else {
            ""
        }
    }

    companion object {

        fun fromParsedProperty(parsedProperty: ParsedDartClass.Property): Property {
            val annotations = parsedProperty.annotations.map { Annotation.fromAnnotationString(it) }
            return Property(
                annotations = annotations,
                keyword = parsedProperty.keyword,
                name = parsedProperty.propertyName,
                type = parsedProperty.propertyType,
                value = parsedProperty.propertyValue,
                comment = parsedProperty.propertyComment,
                isLast = parsedProperty.isLastProperty,
                refTypeId = -1
            )
        }
    }
}