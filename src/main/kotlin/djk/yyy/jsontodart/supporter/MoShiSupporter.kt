package main.kotlin.djk.yyy.jsontodart.supporter

import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyKeyword
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyName
import main.kotlin.djk.yyy.jsontodart.codeelements.getDefaultValue
import main.kotlin.djk.yyy.jsontodart.utils.getIndent

/**
 * MoShiSupporter File
 * Created by Seal.Wu on 2017/10/31.
 */

object MoShiSupporter : IJsonLibSupporter {

    override val annotationImportClassString: String
        get() = "import com.squareup.moshi.Json"


    val propertyAnnotationFormat = "@Json(name = \"%s\")"


    override fun getJsonLibSupportPropertyBlockString(rawPropertyName: String, propertyType: String): String {
        val moShijsonSupportPropertyBuilder = StringBuilder()

        moShijsonSupportPropertyBuilder.append(getIndent())

        moShijsonSupportPropertyBuilder.append(MoShiSupporter.propertyAnnotationFormat.format(rawPropertyName))

        moShijsonSupportPropertyBuilder.append(" ")

        moShijsonSupportPropertyBuilder.append(KPropertyKeyword.get())

        moShijsonSupportPropertyBuilder.append(" ")

        moShijsonSupportPropertyBuilder.append(KPropertyName.getName(rawPropertyName))

        moShijsonSupportPropertyBuilder.append(": ")

        moShijsonSupportPropertyBuilder.append(propertyType)

        if (main.kotlin.djk.yyy.jsontodart.ConfigManager.initWithDefaultValue) {
            moShijsonSupportPropertyBuilder.append(" = ").append(getDefaultValue(propertyType))
        }

        return moShijsonSupportPropertyBuilder.toString()
    }

}