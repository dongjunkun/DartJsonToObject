package main.kotlin.djk.yyy.jsontodart.supporter

import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.codeelements.KPropertyKeyword
import main.kotlin.djk.yyy.jsontodart.codeelements.getDefaultValue
import main.kotlin.djk.yyy.jsontodart.utils.getIndent

/**
 *
 * Created by Seal.Wu on 2017/9/18.
 */

interface INoneLibSupporter {
    /**
     * create property String block
     */
    fun getNoneLibSupporterProperty(rawPropertyName: String, propertyType: String): String


    fun getNoneLibSupporterClassName(rawClassName: String):String

}


object NoneSupporter : INoneLibSupporter {


    override fun getNoneLibSupporterClassName(rawClassName: String):String {
        return ""
    }


    override fun getNoneLibSupporterProperty(rawPropertyName: String, propertyType: String): String {

        val blockBuilder = StringBuilder()

        blockBuilder.append(getIndent())
        blockBuilder.append(KPropertyKeyword.get())
        blockBuilder.append(" ")
        blockBuilder.append(rawPropertyName)
        blockBuilder.append(": ").append(propertyType)
        if (ConfigManager.initWithDefaultValue) {
            blockBuilder.append(" = ").append(getDefaultValue(propertyType))
        }

        return blockBuilder.toString()
    }

}

