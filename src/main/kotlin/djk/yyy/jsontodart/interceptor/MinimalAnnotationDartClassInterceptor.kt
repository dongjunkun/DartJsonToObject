package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass

/**
 * interceptor to make the code to be like the minimal annotation
 * which means that if the property name is the same as raw name then remove the
 * annotations contains %s
 */
class MinimalAnnotationDartClassInterceptor() : IDartClassInterceptor {

    override fun intercept(dartClass: DartClass): DartClass {
        val newProperties = dartClass.properties.map {
            if (it.originName == it.name) {
                it.copy(annotations = it.annotations.filter { it.rawName.isBlank() })
            } else {
                it
            }
        }

        return dartClass.copy(properties = newProperties)

    }


}