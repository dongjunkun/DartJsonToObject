package main.kotlin.djk.yyy.jsontodart.codeelements

/**
 * keyword relative
 * Created by Seal.Wu on 2017/9/13.
 */

interface IPropertyKeyword {

    val varProperty: String
        get() = "var"
    val valProperty: String
        get() = "val"

    fun get(): String

}

object KPropertyKeyword : IPropertyKeyword {
    override fun get() = if (main.kotlin.djk.yyy.jsontodart.ConfigManager.isPropertiesVar) KPropertyKeyword.varProperty else KPropertyKeyword.valProperty

}