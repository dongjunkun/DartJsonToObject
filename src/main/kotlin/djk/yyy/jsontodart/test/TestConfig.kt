package main.kotlin.djk.yyy.jsontodart.test

import main.kotlin.djk.yyy.jsontodart.PropertyTypeStrategy
import main.kotlin.djk.yyy.jsontodart.TargetJsonConverter
import main.kotlin.djk.yyy.jsontodart.supporter.GsonSupporter

/**
 *
 * Created by Seal.Wu on 2018/2/7.
 */
/** 
 * config for test unit
 */
object TestConfig {
    /**
     * If it is in test model
     */
    var isTestModel = false
    var isCommentOff = false
    var isOrderByAlphabetical = false
    var isPropertiesVar = false
    var targetJsonConvertLib = TargetJsonConverter.Gson
    var propertyTypeStrategy = PropertyTypeStrategy.NotNullable
    var initWithDefaultValue = true
    var isNestedClassModel = true

    var customPropertyAnnotationFormatString = "@Optional\n@SerialName(\"%s\")"
    var customAnnotaionImportClassString = "import kotlinx.serialization.SerialName\n" +
            "import kotlinx.serialization.Serializable" + "\n" + "import kotlinx.serialization.Optional"

    var customClassAnnotationFormatString = "@Serializable"

    var indent: Int = 4

    var enableMapType: Boolean = true
    var enableAutoReformat: Boolean = true

    var enableMinimalAnnotation = false

    var parenClassTemplate = ""

    var isKeywordPropertyValid = true

    private var state = State()

    fun setToTestInitState() {
        isTestModel = true
        isCommentOff = false
        isOrderByAlphabetical = true
        isPropertiesVar = false
        targetJsonConvertLib = TargetJsonConverter.Gson
        propertyTypeStrategy = PropertyTypeStrategy.NotNullable
        initWithDefaultValue = true
        isNestedClassModel = true
        customPropertyAnnotationFormatString = "@Optional\n@SerialName(\"%s\")"
        customAnnotaionImportClassString = "import kotlinx.serialization.SerialName\n" +
                "import kotlinx.serialization.Serializable" + "\n" + "import kotlinx.serialization.Optional"

        customClassAnnotationFormatString = "@Serializable"

        enableMinimalAnnotation = false

        indent = 4

        parenClassTemplate = ""

        isKeywordPropertyValid = true
    }


    fun saveState() {
        val newState = State()
        newState.isTestModel = isTestModel
        newState.isCommentOff = isCommentOff
        newState.isOrderByAlphabetical = isOrderByAlphabetical
        newState.isPropertiesVar = isPropertiesVar
        newState.targetJsonConvertLib = targetJsonConvertLib
        newState.propertyTypeStrategy = propertyTypeStrategy
        newState.initWithDefaultValue = initWithDefaultValue
        newState.isNestedClassModel = isNestedClassModel

        newState.customPropertyAnnotationFormatString = customPropertyAnnotationFormatString
        newState.customClassAnnotationFormatString = customClassAnnotationFormatString
        newState.customAnnotaionImportClassString = customAnnotaionImportClassString
        newState.enableMinimalAnnotation = enableMinimalAnnotation
        newState.parenClassTemplate = parenClassTemplate
        newState.isKeywordPropertyValid = isKeywordPropertyValid
        state = newState
    }

    fun restoreState() {
        isTestModel = state.isTestModel
        isCommentOff = state.isCommentOff
        isOrderByAlphabetical = state.isOrderByAlphabetical
        isPropertiesVar = state.isPropertiesVar
        targetJsonConvertLib = state.targetJsonConvertLib
        propertyTypeStrategy = state.propertyTypeStrategy
        initWithDefaultValue = state.initWithDefaultValue
        isNestedClassModel = state.isNestedClassModel
        customPropertyAnnotationFormatString = state.customPropertyAnnotationFormatString
        customClassAnnotationFormatString = state.customClassAnnotationFormatString
        customAnnotaionImportClassString = state.customAnnotaionImportClassString
        enableMinimalAnnotation = state.enableMinimalAnnotation
        parenClassTemplate = state.parenClassTemplate
        isKeywordPropertyValid = state.isKeywordPropertyValid
    }

    class State {
        var isTestModel = false
        var isCommentOff = false
        var isOrderByAlphabetical = true
        var isPropertiesVar = false
        var targetJsonConvertLib = TargetJsonConverter.Gson
        var propertyTypeStrategy = PropertyTypeStrategy.NotNullable
        var initWithDefaultValue = true
        var isNestedClassModel = true

        var customPropertyAnnotationFormatString = GsonSupporter.propertyAnnotationFormat
        var customClassAnnotationFormatString = ""
        var customAnnotaionImportClassString = GsonSupporter.annotationImportClassString

        var indent: Int = 4

        var enableMapType: Boolean = true
        var enableAutoReformat: Boolean = true
        var enableMinimalAnnotation = false

        var parenClassTemplate = ""

        var isKeywordPropertyValid = true
    }
}
