package main.kotlin.djk.yyy.jsontodart.utils

import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor
import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import com.intellij.codeInsight.actions.RearrangeCodeProcessor
import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import main.kotlin.djk.yyy.jsontodart.ConfigManager
import main.kotlin.djk.yyy.jsontodart.codeelements.getDefaultValue
import main.kotlin.djk.yyy.jsontodart.filetype.DartFileType
import main.kotlin.djk.yyy.jsontodart.interceptor.IDartClassInterceptor
import main.kotlin.djk.yyy.jsontodart.interceptor.InterceptorManager
import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.ClassCodeParser
import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.NestedClassModelClassesCodeParser
import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.NormalClassesCodeParser
import main.kotlin.djk.yyy.jsontodart.utils.classblockparse.ParsedDartClass

class DartClassFileGenerator(private val interceptors: List<IDartClassInterceptor> = InterceptorManager.getEnabledDartClassInterceptors()) {

    /**
     * record the renamed class name when generate multiple files
     */
    private val renamedClassNames = mutableListOf<Pair<String, String>>()

    fun generateSingleDataClassFile(
            className: String,
            packageDeclare: String,
            removeDuplicateClassCode: String,
            project: Project?,
            psiFileFactory: PsiFileFactory,
            directory: PsiDirectory
    ) {
        var fileName = className

        fileName = changeDartFileNameIfCurrentDirectoryExistTheSameFileNameWithoutSuffix(fileName, directory)

        generateDartClassFile(
                fileName,
                packageDeclare,
                removeDuplicateClassCode,
                project,
                psiFileFactory,
                directory
        )
        val notifyMessage = "Dart Class file generated successful"
        showNotify(notifyMessage, project)
    }

    fun generateMultipleDataClassFiles(
            removeDuplicateClassCode: String,
            packageDeclare: String,
            project: Project?,
            psiFileFactory: PsiFileFactory,
            directory: PsiDirectory
    ) {

        val classes =
                getClassesStringList(removeDuplicateClassCode).map {
                    ClassCodeParser(it).getDartClass()
                }

        /**
         * Build Property Type reference to ParsedDartClass
         * Only pre class property type could reference behind classes
         */
        val buildRefClasses = buildTypeReference(classes)

        val newClassNames = getNoneConflictClassNames(buildRefClasses, directory)

        val newKotlinClasses = updateClassNames(buildRefClasses, newClassNames)

        val tobeGenerateFilesClasses =
                synchronizedPropertyTypeWithTypeRef(newKotlinClasses)

        tobeGenerateFilesClasses.forEach { kotlinDataClass ->
            generateDartClassFile(
                    kotlinDataClass.name,
                    packageDeclare,
                    kotlinDataClass.toString(),
                    project,
                    psiFileFactory,
                    directory
            )
        }
        val notifyMessage = buildString {
            append("${tobeGenerateFilesClasses.size} Dart Class files generated successful")
            if (renamedClassNames.isNotEmpty()) {
                append("\n")
                append("These class names has been auto renamed to new names:\n ${renamedClassNames.map { it.first + " -> " + it.second }.toList()}")
            }
        }
        showNotify(notifyMessage, project)

    }

    fun updateClassNames(
            dataClasses: List<ParsedDartClass>,
            newClassNames: List<String>
    ): List<ParsedDartClass> {

        val newDartClasses = dataClasses.toMutableList()

        newDartClasses.forEachIndexed { index, kotlinDataClass ->

            val newClassName = newClassNames[index]
            val originClassName = kotlinDataClass.name

            if (newClassName != originClassName) {
                renamedClassNames.add(Pair(originClassName, newClassName))
                val newKotlinDataClass = kotlinDataClass.copy(name = newClassName)
                newDartClasses[index] = newKotlinDataClass
                updateTypeRef(dataClasses, kotlinDataClass, newKotlinDataClass)
            }
        }

        return newDartClasses
    }

    /**
     * None conflict with current directory files and exist class
     */
    private fun getNoneConflictClassNames(
            buildRefClasses: List<ParsedDartClass>,
            directory: PsiDirectory
    ): List<String> {
        val resolveSameConflictClassesNames = mutableListOf<String>()
        buildRefClasses.forEach {
            val originClassName = it.name
            var newClassName =
                    changeDartFileNameIfCurrentDirectoryExistTheSameFileNameWithoutSuffix(originClassName, directory)
            newClassName = changeClassNameIfCurrentListContains(resolveSameConflictClassesNames, newClassName)
            resolveSameConflictClassesNames.add(newClassName)
        }

        return resolveSameConflictClassesNames
    }

    fun updateTypeRef(
            classes: List<ParsedDartClass>,
            originDataClass: ParsedDartClass,
            newKotlinDataClass: ParsedDartClass
    ) {
        classes.forEach {
            it.properties.forEach {
                if (it.dartClassPropertyTypeRef == originDataClass) {
                    it.dartClassPropertyTypeRef = newKotlinDataClass
                }
            }
        }
    }

    fun synchronizedPropertyTypeWithTypeRef(unSynchronizedTypeClasses: List<ParsedDartClass>): List<ParsedDartClass> {
        val synchronizedPropertyTypeClassList = unSynchronizedTypeClasses.map {

            val dataClass = it
            val newProperties = dataClass.properties.map {
                if (it.dartClassPropertyTypeRef != ParsedDartClass.NONE) {
                    val rawPropertyReferenceType = getRawType(getChildType(it.propertyType))
                    val tobeReplaceNewType =
                            it.propertyType.replace(rawPropertyReferenceType, it.dartClassPropertyTypeRef.name)
                    if (it.propertyValue.isNotBlank()) {
                        it.copy(propertyType = tobeReplaceNewType, propertyValue = getDefaultValue(tobeReplaceNewType))
                    } else
                        it.copy(propertyType = tobeReplaceNewType)
                } else {
                    it
                }
            }
            dataClass.copy(properties = newProperties)
        }
        return synchronizedPropertyTypeClassList
    }

    fun buildTypeReference(classes: List<ParsedDartClass>): List<ParsedDartClass> {
        val classNameList = classes.map { it.name }

        /**
         * Build Property Type reference to ParsedDartClass
         * Only pre class property type could reference behind classes
         */
        classes.forEachIndexed { index, kotlinDataClass ->
            kotlinDataClass.properties.forEachIndexed { _, property ->
                val indexOfClassName =
                        classNameList.firstIndexAfterSpecificIndex(getRawType(getChildType(property.propertyType)), index)
                if (indexOfClassName != -1) {
                    property.dartClassPropertyTypeRef = classes[indexOfClassName]
                }
            }
        }

        return classes
    }

    private fun generateDartClassFile(
            fileName: String,
            packageDeclare: String,
            classCodeContent: String,
            project: Project?,
            psiFileFactory: PsiFileFactory,
            directory: PsiDirectory
    ) {
        val classCode = if (interceptors.isNotEmpty()) {
            if (ConfigManager.isInnerClassModel) {
                NestedClassModelClassesCodeParser(classCodeContent).parse().applyInterceptors(interceptors).getCode()
            } else {
                NormalClassesCodeParser(classCodeContent).parse()[0].applyInterceptors(interceptors).getCode()
            }
        } else {
            classCodeContent
        }
        val dartFileContent = buildString {
            if (packageDeclare.isNotEmpty()) {
                append(packageDeclare)
                append("\n\n")
            }
            val importClassDeclaration = ImportClassDeclaration.getImportClassDeclaration()
            if (importClassDeclaration.isNotBlank()) {
                append(importClassDeclaration)
                append("\n\n")
            }
            append(classCode)
        }

        executeCouldRollBackAction(project) {
            val file = psiFileFactory.createFileFromText("$fileName.dart", DartFileType(), classCodeContent)

            val fileAdded = directory.add(file)

            if (ConfigManager.enableAutoReformat) {
                var processor: AbstractLayoutCodeProcessor =
                        ReformatCodeProcessor(project, fileAdded as PsiFile, null, false)
                processor = OptimizeImportsProcessor(processor)
                processor = RearrangeCodeProcessor(processor)
                processor.run()
            }
        }
    }

    private fun changeDartFileNameIfCurrentDirectoryExistTheSameFileNameWithoutSuffix(
            fileName: String,
            directory: PsiDirectory
    ): String {
        var newFileName = fileName
        val dartFileSuffix = ".dart"
        val fileNamesWithoutSuffix =
                directory.files.filter { it.name.endsWith(dartFileSuffix) }
                        .map { it.name.dropLast(dartFileSuffix.length) }
        while (fileNamesWithoutSuffix.contains(newFileName)) {
            newFileName += "X"
        }
        return newFileName
    }

    private fun changeClassNameIfCurrentListContains(classesNames: List<String>, className: String): String {
        var newClassName = className
        while (classesNames.contains(newClassName)) {
            newClassName += "X"
        }
        return newClassName
    }
}