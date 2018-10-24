package main.kotlin.djk.yyy.jsontodart.supporter

import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyKeyword
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyName
import main.kotlin.djk.yyy.jsontodart.codeelements.getDefaultValue
import main.kotlin.djk.yyy.jsontodart.utils.getIndent

/**
 * Jackson json lib supporter
 * Created by Seal.Wu on 2017/9/27.
 */

interface IJacksonSupporter {

    val annotationImportClassString: String
        get() = "import com.fasterxml.jackson.annotation.JsonProperty"

    /**
     * get the jackson supporter property string block
     */
    fun getJacksonSupporterProperty(rawPropertyName: String, propertyType: String): String
}

object JacksonSupporter : IJacksonSupporter {

    val anotaionFormat = "@JsonProperty(\"%s\")"

    override fun getJacksonSupporterProperty(rawPropertyName: String, propertyType: String): String {

        val jacksonSupportPropertyBuilder = StringBuilder()

        jacksonSupportPropertyBuilder.append(getIndent())

        jacksonSupportPropertyBuilder.append(JacksonSupporter.anotaionFormat.format(rawPropertyName))

        jacksonSupportPropertyBuilder.append(" ")

        jacksonSupportPropertyBuilder.append(KPropertyKeyword.get())

        jacksonSupportPropertyBuilder.append(" ")

        jacksonSupportPropertyBuilder.append(KPropertyName.getName(rawPropertyName))

        jacksonSupportPropertyBuilder.append(": ")

        jacksonSupportPropertyBuilder.append(propertyType)

        if (main.kotlin.djk.yyy.jsontodart.ConfigManager.initWithDefaultValue) {
            jacksonSupportPropertyBuilder.append(" = ").append(getDefaultValue(propertyType))
        }

        return jacksonSupportPropertyBuilder.toString()
    }

}