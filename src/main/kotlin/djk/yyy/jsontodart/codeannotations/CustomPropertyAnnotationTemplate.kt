package main.kotlin.djk.yyy.jsontodart.codeannotations

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.classscodestruct.Annotation
import main.kotlin.djk.yyy.jsontodart.codeannotations.AnnotationTemplate

class CustomPropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    private val annotation = Annotation(ConfigManager.customPropertyAnnotationFormatString, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<Annotation> {
        return listOf(annotation)
    }

}