package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass

/**
 * Interceptor for code transform
 */
interface IDartClassInterceptor {

    /**
     * intercept the kotlindataclass and modify the class,the function will return a new  Dart Class Object
     * warn: the new returned object  is a new object ,not the original
     */
    fun intercept(dartClass: DartClass): DartClass

}