package main.kotlin.djk.yyy.jsontodart.codeannotations

import main.kotlin.djk.yyy.jsontodart.classscodestruct.Annotation
import main.kotlin.djk.yyy.jsontodart.codeannotations.AnnotationTemplate
import main.kotlin.djk.yyy.jsontodart.supporter.MoShiSupporter

class MoshiPropertyAnnotationTemplate(val rawName: String) : AnnotationTemplate {

    private val annotation = Annotation(MoShiSupporter.propertyAnnotationFormat, rawName)

    override fun getCode(): String {
        return annotation.getAnnotationString()
    }

    override fun getAnnotations(): List<Annotation> {
        return listOf(annotation)
    }
}