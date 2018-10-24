package main.kotlin.djk.yyy.jsontodart.utils

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.TargetJsonConverter
import main.kotlin.djk.yyy.jsontodart.interceptor.IImportClassDeclarationInterceptor
import main.kotlin.djk.yyy.jsontodart.interceptor.InterceptorManager
import main.kotlin.djk.yyy.jsontodart.supporter.*

/**
 * import class declaration
 * Created by Seal.Wu on 2018/4/18.
 */
object ImportClassDeclaration {

    /**
     * import class declaration getter
     */
    fun getImportClassDeclaration(): String {

        val importClassDeclaration = when (ConfigManager.targetJsonConverterLib) {

            TargetJsonConverter.Gson -> {
                GsonSupporter.annotationImportClassString
            }
            TargetJsonConverter.FastJson -> {
                FastjsonSupporter.annotationImportClassString
            }
            TargetJsonConverter.Jackson -> {
                JacksonSupporter.annotationImportClassString
            }
            TargetJsonConverter.MoShi -> {
                MoShiSupporter.annotationImportClassString
            }
            TargetJsonConverter.LoganSquare -> {
                LoganSquareSupporter.annotationImportClassString
            }
            TargetJsonConverter.Custom -> {
                CustomJsonLibSupporter.annotationImportClassString
            }

            else -> {
                ""
            }
        }


        return applyImportClassDeclarationInterceptors(
            importClassDeclaration,
            InterceptorManager.getEnabledImportClassDeclarationInterceptors()
        )

    }


    fun applyImportClassDeclarationInterceptors(
        originImportClassDeclaration: String,
        interceptors: List<IImportClassDeclarationInterceptor>
    ): String {
        var importClassDeclaration = originImportClassDeclaration
        interceptors.forEach {
            importClassDeclaration = it.intercept(importClassDeclaration)
        }
        return importClassDeclaration
    }
}