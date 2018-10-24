package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.supporter.MoShiSupporter

/**
 * try to add import class declarations of Moshi Generation
 */
class AddMoshiCodeGenClassDeclarationInterceptor : IImportClassDeclarationInterceptor {

    override fun intercept(originImportClasses: String): String {

        val propertyAnnotationImportClassString = MoShiSupporter.annotationImportClassString

        val classAnnotationImportClassString = "import com.squareup.moshi.JsonClass"

        val importClassDeclaration = propertyAnnotationImportClassString.plus("\n").plus(classAnnotationImportClassString)

        return importClassDeclaration
    }
}