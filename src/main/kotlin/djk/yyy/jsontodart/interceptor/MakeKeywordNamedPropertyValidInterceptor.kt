package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass
import main.kotlin.djk.yyy.jsontodart.utils.KOTLIN_KEYWORD_LIST

/**
 * Interceptor to make kotlin keyword property names valid
 */
class MakeKeywordNamedPropertyValidInterceptor : IDartClassInterceptor {

    override fun intercept(dartClass: DartClass): DartClass {

        val keywordValidProperties = dartClass.properties.map {
            if (KOTLIN_KEYWORD_LIST.contains(it.name)) {
                it.copy(name = "`${it.name}`")
            } else {
                it
            }
        }

        return dartClass.copy(properties = keywordValidProperties)
    }
}
