package org.orbitmvi.orbit.swift

import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

val Framework.architectureName: String
    get() {
        val m: Matcher = Pattern.compile("_([a-z])").matcher(linkTask.target)
        val architectureNameStringBuilder = StringBuilder()
        while (m.find()) {
            m.appendReplacement(architectureNameStringBuilder, m.group(1).toUpperCase(Locale.ROOT))
        }
        m.appendTail(architectureNameStringBuilder)
        return architectureNameStringBuilder.toString()
    }
