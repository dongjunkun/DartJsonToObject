package main.kotlin.djk.yyy.jsontodart.utils.classblockparse

import main.kotlin.djk.yyy.jsontodart.classscodestruct.DartClass
import main.kotlin.djk.yyy.jsontodart.utils.getClassesStringList

class NormalClassesCodeParser(private val classesCode: String) {
    fun parse(): List<DartClass> {
        return getClassesStringList(classesCode)
            .map {
                DartClass.fromParsedDartClass(ClassCodeParser(it).getDartClass())
            }
    }
}