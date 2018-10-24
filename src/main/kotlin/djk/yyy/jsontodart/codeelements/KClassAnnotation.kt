package main.kotlin.djk.yyy.jsontodart.codeelements

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.TargetJsonConverter
import main.kotlin.djk.yyy.jsontodart.supporter.CustomJsonLibSupporter
import main.kotlin.djk.yyy.jsontodart.supporter.LoganSquareSupporter

/**
 * class annotation for json lib
 * Created by Seal.Wu on 2017/11/1.
 */

/**
 * class annotation for json lib
 */
interface IClassAnnotation {
    /**
     * get the annotation string be in append before the class name
     */
    fun getClassAnnotation(rawClassName:String):String
}


object KClassAnnotation : IClassAnnotation {

    override fun getClassAnnotation(rawClassName: String): String {

        if (ConfigManager.targetJsonConverterLib == TargetJsonConverter.LoganSquare) {

            return LoganSquareSupporter.getClassAnnotationBlockString(rawClassName)

        }else if (ConfigManager.targetJsonConverterLib == TargetJsonConverter.Custom) {

            return CustomJsonLibSupporter.getClassAnnotationBlockString(rawClassName)
        }

        return ""
    }

}