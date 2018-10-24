package main.kotlin.djk.yyy.jsontodart.interceptor

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass

class ParentClassTemplateDartClassInterceptor : IDartClassInterceptor {

    override fun intercept(dartClass: DartClass): DartClass {

        val parentClassTemplateSimple = ConfigManager.parenClassTemplate.substringAfterLast(".")
        return dartClass.copy(parentClassTemplate = parentClassTemplateSimple)
    }


}