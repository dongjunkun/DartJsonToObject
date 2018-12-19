package main.kotlin.djk.yyy.jsontodart

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import main.kotlin.djk.yyy.jsontodart.codeelements.KClassAnnotation
import main.kotlin.djk.yyy.jsontodart.codeelements.KProperty
import main.kotlin.djk.yyy.jsontodart.utils.*
import org.codehaus.groovy.vmplugin.v7.TypeHelper
import java.util.*

/**
 * Kotlin code maker
 * Created by seal.wu on 2017/8/21.
 */
class DartCodeMaker {

    private var className: String? = null
    private var inputElement: JsonElement? = null

    private var originElement: JsonElement

    private val indent = getIndent()

    private val toBeAppend = HashSet<String>()

    constructor(className: String, inputText: String) {
        originElement = Gson().fromJson<JsonElement>(inputText, JsonElement::class.java)
        this.inputElement = TargetJsonElement(inputText).getTargetJsonElementForGeneratingCode()
        this.className = className
    }

    constructor(className: String, jsonElement: JsonElement) {
        originElement = jsonElement
        this.inputElement = TargetJsonElement(jsonElement).getTargetJsonElementForGeneratingCode()
        this.className = className
    }

    private fun getClassName(className: String): String {
        val words = className.split("_")
        val sb = StringBuilder()

        for (i in words.indices) {
            val idTokens = words[i].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val chars = idTokens[idTokens.size - 1].toCharArray()
            if (chars.isNotEmpty())
                chars[0] = Character.toUpperCase(chars[0])

            sb.append(chars)
        }

        return sb.toString()
    }

    fun makeDart(): String {
        className = getClassName(className!!)
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n")

        val jsonElement = inputElement
        checkIsNotEmptyObjectJSONElement(jsonElement)

        appendClassName(stringBuilder)
        appendCodeMember(stringBuilder, jsonElement?.asJsonObject!!, 1)
        stringBuilder.append(className).append("(\n")
        appendCodeMember(stringBuilder, jsonElement?.asJsonObject!!, 0)
        stringBuilder.append("factory $className.fromJson").append("(\n").append("Map<String, dynamic> json) => _\$${className}FromJson(json);")

        stringBuilder.append("\n}")

        stringBuilder.append("$className _\$${className}FromJson").append("(\n").append("Map<String, dynamic> json) => $className(")

        appendCodeMember(stringBuilder, jsonElement?.asJsonObject!!, 2)

        stringBuilder.append("abstract class _${className}SerializerMiXin {")
        appendCodeMember(stringBuilder, jsonElement?.asJsonObject!!, 3)
        stringBuilder.append("Map<String, dynamic> toJson() => <String, dynamic>{")
        appendCodeMember(stringBuilder, jsonElement?.asJsonObject!!, 4)
        stringBuilder.append("};\n}")


        if (toBeAppend.isNotEmpty()) {
            appendSubClassCode(stringBuilder)
        }

        return stringBuilder.toString()
    }

    //the fucking code
    private fun checkIsNotEmptyObjectJSONElement(jsonElement: JsonElement?) {
        if (jsonElement!!.isJsonObject) {
            if (jsonElement.asJsonObject.entrySet().isEmpty() && originElement.isJsonArray) {
                //when [[[{}]]]
                if (originElement.asJsonArray.onlyHasOneElementRecursive()) {
                    val unSupportJsonException = UnSupportJsonException("Unsupported Json String")
                    val adviceType = getArrayType("Any", originElement.asJsonArray).replace(Regex("Int|Float|String|Boolean"), "Any")
                    unSupportJsonException.advice = """No need converting, just use $adviceType is enough for your json string"""
                    throw unSupportJsonException
                } else {
                    //when [1,"a"]
                    val unSupportJsonException = UnSupportJsonException("Unsupported Json String")
                    unSupportJsonException.advice = """No need converting,  List<Any> may be a good class type for your json string"""
                    throw unSupportJsonException
                }
            }
        } else {
            /**
             * in this condition the only result it that we just give the json a List<Any> type is enough, No need to
             * do any convert to make class type
             */
            val unSupportJsonException = UnSupportJsonException("Unsupported Json String")
            val adviceType = getArrayType("Any", originElement.asJsonArray).replace("AnyX", "Any")
            unSupportJsonException.advice = """No need converting, just use $adviceType is enough for your json string"""
            throw unSupportJsonException
        }
    }

    private fun appendSubClassCode(stringBuilder: StringBuilder) {
        if (ConfigManager.isInnerClassModel) {
            appendNormalSubClassCode(stringBuilder)
        } else {
            appendNormalSubClassCode(stringBuilder)
        }
    }

    private fun appendInnerClassModelSubClassCode(stringBuilder: StringBuilder) {
        stringBuilder.append(" {")
        stringBuilder.append("\n")
        val nestedClassCode = toBeAppend.joinToString("\n\n") {
            it.split("\n").joinToString("\n") {
                indent + it
            }
        }
        stringBuilder.append(nestedClassCode)
        stringBuilder.append("\n}")
    }

    private fun appendNormalSubClassCode(stringBuilder: StringBuilder) {
        for (append in toBeAppend) {
            stringBuilder.append("\n")
            stringBuilder.append(append)
        }
    }

    private fun appendClassName(stringBuilder: StringBuilder) {
        val classAnnotation = KClassAnnotation.getClassAnnotation(className.toString())
        stringBuilder.append(classAnnotation)
        if (classAnnotation.isNotBlank()) stringBuilder.append("\n")
        stringBuilder.append("class ").append(className).append(" extends Object with _")
                .append(className).append("SerializerMiXin").append("{\n")
    }


    private fun appendCodeMember(stringBuilder: StringBuilder, jsonObject: JsonObject, pType: Int) {

        val size = jsonObject.entrySet().size

        val entryList =
                if (ConfigManager.isOrderByAlphabetical) jsonObject.entrySet().sortedBy { it.key }
                else jsonObject.entrySet()
        entryList.forEachIndexed { index, (property, jsonElementValue) ->
            val isLast = (index == size - 1)

            if (jsonElementValue.isJsonArray) {
                val type = getArrayType(property, jsonElementValue.asJsonArray)

                if (isExpectedJsonObjArrayType(jsonElementValue.asJsonArray) || jsonElementValue.asJsonArray.onlyHasOneObjectElementRecursive()
                        || jsonElementValue.asJsonArray.onlyOneSubArrayContainsElementAndAllObjectRecursive()) {

                    toBeAppend.add(DartCodeMaker(getChildType(getRawType(type)), jsonElementValue).makeDart())
                }
                addProperty(stringBuilder, property, type, "", isLast, pType)

            } else if (jsonElementValue.isJsonPrimitive) {
                val type = getPrimitiveType(jsonElementValue.asJsonPrimitive)
                addProperty(stringBuilder, property, type, jsonElementValue.asString, isLast, pType)

            } else if (jsonElementValue.isJsonObject) {
                if (ConfigManager.enableMapType && maybeJsonObjectBeMapType(jsonElementValue.asJsonObject)) {
                    val mapKeyType = getMapKeyTypeConvertFromJsonObject(jsonElementValue.asJsonObject)
                    val mapValueType = getMapValueTypeConvertFromJsonObject(jsonElementValue.asJsonObject)
                    if (mapValueIsObjectType(mapValueType)) {
                        toBeAppend.add(
                                DartCodeMaker(
                                        getChildType(mapValueType),
                                        jsonElementValue.asJsonObject.entrySet().first().value
                                ).makeDart()
                        )
                    }
                    val mapType = "Map<$mapKeyType,$mapValueType>"
                    addProperty(stringBuilder, property, mapType, "", isLast, pType)

                } else {
                    val type = getJsonObjectType(property)
                    toBeAppend.add(DartCodeMaker(getRawType(type), jsonElementValue).makeDart())
                    addProperty(stringBuilder, property, type, "", isLast, pType)
                }

            } else if (jsonElementValue.isJsonNull) {
                addProperty(stringBuilder, property, DEFAULT_TYPE, null, isLast, pType)
            }
        }
    }

    private fun mapValueIsObjectType(mapValueType: String) = (mapValueType == MAP_DEFAULT_OBJECT_VALUE_TYPE
            || mapValueType.contains(MAP_DEFAULT_ARRAY_ITEM_VALUE_TYPE))


    private fun addProperty(
            stringBuilder: StringBuilder,
            property: String,
            type: String,
            value: String?,
            isLast: Boolean = false,
            pType: Int
    ) {
        var innerValue = value
        if (innerValue == null) {
            innerValue = "null"
        }
        val p = KProperty(property, getOutType(type, value), innerValue)
        if (pType == 0) {
            stringBuilder.append("this.$property")
            if (isLast) {
                stringBuilder.append(");")
            } else {
                stringBuilder.append(",")
            }
        } else if (pType == 1) {
            stringBuilder.append("final $type $property;")
        } else if (pType == 2) {
            if (type.startsWith("List")) {
                stringBuilder.append("(json['$property'] as List)\n" +
                        "        ?.map(\n" +
                        "            (e) => e == null ? null : ${getChildType(type)}.fromJson(e as Map<String, dynamic>))\n" +
                        "        ?.toList()")
            } else if (isRawType(type).not()) {
                stringBuilder.append("$type.fromJson(json['$property'])")
            } else {
                stringBuilder.append("  json['$property'] as $type")
            }
            if (isLast) {
                stringBuilder.append(");")
            } else {
                stringBuilder.append(",")
            }
        } else if (pType == 3) {
            stringBuilder.append("$type get $property;")
        } else {
            stringBuilder.append(" '$property': $property")
            if (!isLast)
                stringBuilder.append(",")
        }

//        if (!isLast)
//        stringBuilder.append(",")

        val propertyComment = p.getPropertyComment()
        if (!ConfigManager.isCommentOff && propertyComment.isNotBlank())
            stringBuilder.append(" // ")
                    .append(getCommentCode(propertyComment))
        stringBuilder.append("\n")
    }

}
