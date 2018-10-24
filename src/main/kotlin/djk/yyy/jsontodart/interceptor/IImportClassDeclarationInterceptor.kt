package main.kotlin.djk.yyy.jsontodart.interceptor

/**
 * Insert import class code
 */
interface IImportClassDeclarationInterceptor {

    /**
     * intercept the import class declaration code insert to the origin import classes declaration code
     */
    fun intercept(originImportClasses: String):String
}