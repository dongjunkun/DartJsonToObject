package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.interceptor.IImportClassDeclarationInterceptor
import main.kotlin.djk.yyy.jsontodart.ConfigManager


/**
 * insert parent class declaration code
 */
class ParentClassImportClassDeclarationInterceptor : IImportClassDeclarationInterceptor {

    override fun intercept(originImportClasses: String): String {
        val parentClassImportDeclaration = "import ${ConfigManager.parenClassTemplate.substringBeforeLast("(").trim()}"
        return "$originImportClasses\n$parentClassImportDeclaration".trim()
    }
}