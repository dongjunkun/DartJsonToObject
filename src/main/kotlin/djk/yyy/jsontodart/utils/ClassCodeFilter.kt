package main.kotlin.djk.yyy.jsontodart.utils

import main.kotlin.djk.yyy.jsontodart.ConfigManager

/**
 * Class Code Filter
 * Created by Seal.Wu on 2018/4/18.
 */
object ClassCodeFilter {

    /**
     * when not in `innerClassModel` and the class spit with `\n\n` then remove the duplicate class
     */
    fun removeDuplicateClassCode(generateClassesString: String): String {

        if (ConfigManager.isInnerClassModel.not()) {

            val set = mutableSetOf<String>()
            set.addAll(generateClassesString.split("\n\n"))
            return set.joinToString("\n\n")

        } else {
            return generateClassesString
        }
    }
}