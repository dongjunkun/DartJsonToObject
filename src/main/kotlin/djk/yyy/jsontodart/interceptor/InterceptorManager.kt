package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.TargetJsonConverter

object InterceptorManager {

    fun getEnabledDartClassInterceptors(): List<IDartClassInterceptor> {

        return mutableListOf<IDartClassInterceptor>().apply {
            if (ConfigManager.targetJsonConverterLib == TargetJsonConverter.MoshiCodeGen) {
                add(AddMoshiCodeGenAnnotationClassInterceptor())
            }

            if (ConfigManager.enableMinimalAnnotation) {
                add(MinimalAnnotationDartClassInterceptor())
            }

            if (ConfigManager.parenClassTemplate.isNotBlank()) {
                add(ParentClassTemplateDartClassInterceptor())
            }

            if (ConfigManager.keywordPropertyValid) {
                add(MakeKeywordNamedPropertyValidInterceptor())
            }

        }.apply {

            if (size >= 1) {
                add(0, MakePropertyOriginNameInterceptor())
            }
        }
    }


    fun getEnabledImportClassDeclarationInterceptors(): List<IImportClassDeclarationInterceptor> {

        return mutableListOf<IImportClassDeclarationInterceptor>().apply {

            if (ConfigManager.targetJsonConverterLib == TargetJsonConverter.MoshiCodeGen) {

                add(AddMoshiCodeGenClassDeclarationInterceptor())
            }
            if (ConfigManager.parenClassTemplate.isNotBlank()) {

                add(ParentClassImportClassDeclarationInterceptor())
            }
        }
    }

}
