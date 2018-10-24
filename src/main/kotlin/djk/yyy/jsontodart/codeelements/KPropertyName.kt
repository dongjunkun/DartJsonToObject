package main.kotlin.djk.yyy.jsontodart.codeelements

/**
 * make name to be camel case
 * Created by Sealwu on 2017/9/18.
 */

interface IPropertyNameMaker {

    /**
     * make legal property name from a input raw string
     */
    fun makePropertyName(rawString: String): String


    /**
     * make legal property name from a input raw string
     */
    fun makePropertyName(rawString: String, needTransformToLegalName: Boolean): String


}

object KPropertyName : KName(), IPropertyNameMaker {


    override fun getName(rawName: String): String {

        return makePropertyName(rawName, true)
    }

    override fun makePropertyName(rawString: String): String {

        return rawString
    }

    override fun makePropertyName(rawString: String, needTransformToLegalName: Boolean): String {

        if (needTransformToLegalName) {

            val camelCaseLegalName = makeCamelCaseLegalName(rawString)
            return if (camelCaseLegalName.isEmpty()) makeCamelCaseLegalName("x-" + rawString) else camelCaseLegalName

        } else {
            return rawString
        }

    }

    /**
     * this function may return empty string when the raw string is only make of number and illegal character
     */
    private fun makeCamelCaseLegalName(rawString: String): String {
        /**
         * keep nameSeparator character
         */
        val pattern = "${illegalCharacter}".replace(Regex(nameSeparator.toString()), "")

        val temp = rawString.replace(Regex(pattern), "").let {

            return@let removeStartNumberAndIllegalCharacter(it)

        }

        val lowerCamelCaseName = toLowerCamelCase(temp)

        val legalName = toBeLegalName(lowerCamelCaseName)

        return legalName
    }


    /**
     * this function can remove the rest white space
     */
    private fun toLowerCamelCase(temp: String): String {

        val stringBuilder = StringBuilder()

        temp.split(Regex(nameSeparator.toString())).forEach {
            if (it.isNotBlank()) {
                stringBuilder.append(it.substring(0, 1).toUpperCase().plus(it.substring(1)))
            }
        }

        val camelCaseName = stringBuilder.toString()

        if (camelCaseName.isNotEmpty()) {

            val lowerCamelCaseName = camelCaseName.substring(0, 1).toLowerCase().plus(camelCaseName.substring(1))

            return lowerCamelCaseName
        } else {

            return camelCaseName
        }


    }


}