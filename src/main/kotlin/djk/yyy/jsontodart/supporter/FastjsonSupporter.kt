package main.kotlin.djk.yyy.jsontodart.supporter

import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyKeyword
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyName
import main.kotlin.djk.yyy.jsontodart.codeelements.getDefaultValue
import main.kotlin.djk.yyy.jsontodart.utils.getIndent

/**
 * supporter for alibaba fastjson
 * Created by Seal.Wu on 2017/9/28.
 */


object FastjsonSupporter : IJsonLibSupporter {


    override val annotationImportClassString: String
        get() = "import com.alibaba.fastjson.annotation.JSONField"

    val propertyAnnotationFormat = "@JSONField(name = \"%s\")"


    override fun getJsonLibSupportPropertyBlockString(rawPropertyName: String, propertyType: String): String {

        val fastjsonSupportPropertyBuilder = StringBuilder()

        fastjsonSupportPropertyBuilder.append(getIndent())

        fastjsonSupportPropertyBuilder.append(FastjsonSupporter.propertyAnnotationFormat.format(rawPropertyName))

        fastjsonSupportPropertyBuilder.append(" ")

        fastjsonSupportPropertyBuilder.append(KPropertyKeyword.get())

        fastjsonSupportPropertyBuilder.append(" ")

        /**
         * todo // the logic below also has problem ,remove 'is' when it end with uppercase,or don't do anything
         */
        fastjsonSupportPropertyBuilder.append(KPropertyName.getName(removeStartIsCharactors(rawPropertyName)))

        fastjsonSupportPropertyBuilder.append(": ")

        fastjsonSupportPropertyBuilder.append(propertyType)

        if (main.kotlin.djk.yyy.jsontodart.ConfigManager.initWithDefaultValue) {
            fastjsonSupportPropertyBuilder.append(" = ").append(getDefaultValue(propertyType))
        }

        return fastjsonSupportPropertyBuilder.toString()
    }


    private fun removeStartIsCharactors(rawPropertyName: String): String {
        if (rawPropertyName.startsWith("is")) {

            var modifyPropertyName = rawPropertyName.removePrefix("is")
            while (modifyPropertyName.startsWith("is")) {
                modifyPropertyName = modifyPropertyName.removePrefix("is")
            }
            return modifyPropertyName
        } else {
            return rawPropertyName
        }
    }
}