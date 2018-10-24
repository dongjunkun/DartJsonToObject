package main.kotlin.djk.yyy.jsontodart

import main.kotlin.djk.yyy.jsontodart.interceptor.IDartClassInterceptor
import main.kotlin.djk.yyy.jsontodart.interceptor.InterceptorManager

class DartClassCodeMaker(private val rootClassName: String, private val json: String) {

    fun makeDartClassCode(): String {

        return if (needMakeKotlinCodeByKotlinDataClass()) {

            makeDartClassCode(InterceptorManager.getEnabledDartClassInterceptors())

        } else {
            DartCodeMaker(rootClassName, json).makeDart()
        }
    }

    private fun needMakeKotlinCodeByKotlinDataClass(): Boolean {
        return InterceptorManager.getEnabledDartClassInterceptors().isNotEmpty()
    }

    fun makeDartClassCode(interceptors: List<IDartClassInterceptor>): String {
        val dartClasses = DartClassMaker(rootClassName = rootClassName, json = json).makeDartClasses()
        val interceptedDataClasses = dartClasses.map {it.applyInterceptors(interceptors)}
        val code = interceptedDataClasses.joinToString("\n\n") {
            it.getCode()
        }
        return code
    }
}