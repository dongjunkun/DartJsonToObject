package main.kotlin.djk.yyy.jsontodart.codeannotations

import main.kotlin.djk.yyy.jsontodart.classscodestruct.Annotation
import main.kotlin.djk.yyy.jsontodart.codeannotations.AnnotationTemplate
import main.kotlin.djk.yyy.jsontodart.supporter.LoganSquareSupporter

class LoganSquarePropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    private val annotation = Annotation(LoganSquareSupporter.propertyAnnotationFormat, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<Annotation> {
        return listOf<Annotation>(annotation)
    }
}