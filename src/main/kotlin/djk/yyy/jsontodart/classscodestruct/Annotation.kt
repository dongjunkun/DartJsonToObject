package main.kotlin.djk.yyy.jsontodart.classscodestruct

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.utils.numberOf

data class Annotation(val annotationTemplate: String, val rawName: String) {

    fun getAnnotationString(): String {
        if (annotationTemplate.contains("%s")) {
            val countS = annotationTemplate.numberOf("%s")
            val args = Array(countS, { rawName })
            return annotationTemplate.format(*args)
        } else {
            return annotationTemplate
        }
    }

    companion object {

        private fun fromOneLineAnnotationString(annotationString: String): Annotation {
            if (annotationString.contains("\n")) {
                throw IllegalArgumentException("Only support one line annotation!! current is $annotationString")
            }

            if (annotationString.contains("\"")) {
                if (annotationString.numberOf("\"") != 2) {
                    throw IllegalArgumentException("Only support one line annotation with one couple Double quotes!! current is $annotationString")
                }
                val rawName = annotationString.substringAfter("\"").substringBefore("\"")
                val annotationTemplate =
                    annotationString.substringBefore("\"") + "\"%s\"" + annotationString.substringAfterLast("\"")
                return Annotation(annotationTemplate, rawName)
            } else {
                return Annotation(annotationString, "")
            }

        }

        private fun fromMultipleLineAnnotationString(multipleLineString: String): Annotation {
            val annotations = multipleLineString.trim().split("\n").map { fromOneLineAnnotationString(it) }
            val annotationTemplate = annotations.joinToString(separator = "\n") { it.annotationTemplate }
            val rawName = annotations.firstOrNull { it.rawName.isNotBlank() }?.rawName ?: ""
            return Annotation(annotationTemplate, rawName)
        }

        fun fromAnnotationString(annotationString: String): Annotation {
            return try {
                if (annotationString.trim().contains("\n").not()) {
                    fromOneLineAnnotationString(annotationString)
                } else {
                    fromMultipleLineAnnotationString(annotationString)
                }
            } catch (e: IllegalArgumentException) {
                val annotationTemplate = ConfigManager.customPropertyAnnotationFormatString
                if (annotationTemplate.contains("%s").not()) {
                    Annotation(annotationTemplate, "")
                } else {
                    val pre = annotationTemplate.substringBefore("%s")
                    val rawName = annotationString.substringAfter(pre).substringBefore("\"")
                    Annotation(annotationTemplate, rawName)
                }

            }

        }
    }
}