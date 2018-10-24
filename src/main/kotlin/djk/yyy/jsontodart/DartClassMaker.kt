package main.kotlin.djk.yyy.jsontodart

import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass
import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.NestedClassModelClassesCodeParser
import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.NormalClassesCodeParser


class DartClassMaker(private val rootClassName: String, private val json: String) {


    fun makeDartClasses(): List<DartClass> {

        val code = DartCodeMaker(rootClassName, json).makeDart()
        return if (ConfigManager.isInnerClassModel) {
            listOf(NestedClassModelClassesCodeParser(code).parse())
        } else {
            return NormalClassesCodeParser(code).parse()
        }
    }

}