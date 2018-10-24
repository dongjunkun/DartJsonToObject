package main.kotlin.djk.yyy.jsontodart.supporter

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyKeyword
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyName
import main.kotlin.djk.yyy.jsontodart.codeelements.getDefaultValue
import main.kotlin.djk.yyy.jsontodart.utils.getIndent

/**
 * LoganSquare Json Lib supporter file
 * Created by Seal.Wu on 2017/11/1.
 */

object LoganSquareSupporter : IJsonLibSupporter {

    val classAnnotation = "@JsonObject"
    val propertyAnnotationFormat = "@JsonField(name = arrayOf(\"%s\"))"

    override val annotationImportClassString: String
        get() = "import com.bluelinelabs.logansquare.annotation.JsonField\nimport com.bluelinelabs.logansquare.annotation.JsonObject"


    override fun getClassAnnotationBlockString(rawClassName: String): String {
        return classAnnotation
    }
    override fun getJsonLibSupportPropertyBlockString(rawPropertyName: String, propertyType: String): String {

        val loganSquareSupportPropertyBuilder = StringBuilder()

        loganSquareSupportPropertyBuilder.append(getIndent())

        loganSquareSupportPropertyBuilder.append(LoganSquareSupporter.propertyAnnotationFormat.format(rawPropertyName))

        loganSquareSupportPropertyBuilder.append(" ")

        loganSquareSupportPropertyBuilder.append(KPropertyKeyword.get())

        loganSquareSupportPropertyBuilder.append(" ")

        loganSquareSupportPropertyBuilder.append(KPropertyName.getName(rawPropertyName))

        loganSquareSupportPropertyBuilder.append(": ")

        loganSquareSupportPropertyBuilder.append(propertyType)

        if (ConfigManager.initWithDefaultValue) {
            loganSquareSupportPropertyBuilder.append(" = ").append(getDefaultValue(propertyType))
        }

        return loganSquareSupportPropertyBuilder.toString()

    }

}