package main.kotlin.djk.yyy.jsontodart.supporter

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyKeyword
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyName
import main.kotlin.djk.yyy.jsontodart.codeelements.getDefaultValue
import main.kotlin.djk.yyy.jsontodart.utils.getIndent
import main.kotlin.djk.yyy.jsontodart.utils.numberOf

/**
 * others json lib supporter by custom
 * Created by Seal.Wu on 2017/11/1.
 */

object CustomJsonLibSupporter : IJsonLibSupporter {


    private val propertyAnnotation
        get() = ConfigManager.customPropertyAnnotationFormatString


    private val classAnnotationFormat
        get() = ConfigManager.customClassAnnotationFormatString

    override val annotationImportClassString: String
        get() = ConfigManager.customAnnotaionImportClassString


    override fun getClassAnnotationBlockString(rawClassName: String): String {

        if (classAnnotationFormat.contains("%s")) {
            val count = classAnnotationFormat.numberOf("%s")
            val args = arrayOfNulls<String>(count).apply { fill(rawClassName) }
            return classAnnotationFormat.format(*args)
        } else {
            return classAnnotationFormat
        }
    }

    override fun getJsonLibSupportPropertyBlockString(rawPropertyName: String, propertyType: String): String {

        val customJsonLibSupportPropertyBuilder = StringBuilder()


        val propertyAnnotationString = getPropertyAnnotationString(rawPropertyName)

        propertyAnnotationString.split("\n").forEach {
            customJsonLibSupportPropertyBuilder.append(getIndent())
            customJsonLibSupportPropertyBuilder.append(it)
            customJsonLibSupportPropertyBuilder.append("\n")
        }

        customJsonLibSupportPropertyBuilder.append(getIndent())

        customJsonLibSupportPropertyBuilder.append(KPropertyKeyword.get())

        customJsonLibSupportPropertyBuilder.append(" ")

        val propertyName =
            if (needRenamePropertyNameToCamelCase()) KPropertyName.getName(rawPropertyName) else rawPropertyName

        customJsonLibSupportPropertyBuilder.append(propertyName)

        customJsonLibSupportPropertyBuilder.append(": ")

        customJsonLibSupportPropertyBuilder.append(propertyType)

        if (ConfigManager.initWithDefaultValue) {
            customJsonLibSupportPropertyBuilder.append(" = ").append(getDefaultValue(propertyType))
        }

        return customJsonLibSupportPropertyBuilder.toString()
    }

    fun getPropertyAnnotationString(rawPropertyName: String): String {

        return if (propertyAnnotation.contains("%s")) {

            val count = propertyAnnotation.numberOf("%s")
            val args = arrayOfNulls<String>(count).apply { fill(rawPropertyName) }

            propertyAnnotation.format(*args)

        } else {
            propertyAnnotation
        }

    }

    private fun needRenamePropertyNameToCamelCase() = propertyAnnotation.contains("%s")
}