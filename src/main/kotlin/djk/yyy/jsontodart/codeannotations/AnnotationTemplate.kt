package main.kotlin.djk.yyy.jsontodart.codeannotations

import main.kotlin.djk.yyy.jsontodart.classscodestruct.Annotation

interface AnnotationTemplate {
    fun getCode():String
    fun getAnnotations():List<Annotation>
}