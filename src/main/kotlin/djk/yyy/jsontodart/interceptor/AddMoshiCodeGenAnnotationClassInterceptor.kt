package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.classscodestruct.Annotation
import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass
import main.kotlin.djk.yyy.jsontodart.codeannotations.MoshiPropertyAnnotationTemplate
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyName

/**
 * This interceptor try to add Moshi(code gen) annotation
 */
class AddMoshiCodeGenAnnotationClassInterceptor : IDartClassInterceptor {

    override fun intercept(dartClass: DartClass): DartClass {

        val addMoshiCodeGenAnnotationProperties = dartClass.properties.map {

            val camelCaseName = KPropertyName.makePropertyName(it.originName, true)
            it.copy(annotations =  MoshiPropertyAnnotationTemplate(it.originName).getAnnotations(),name = camelCaseName)
        }

        val classAnnotationString = "@JsonClass(generateAdapter = true)"

        val classAnnotation = Annotation.fromAnnotationString(classAnnotationString)

        return dartClass.copy(properties = addMoshiCodeGenAnnotationProperties,annotations = listOf(classAnnotation))
    }
}