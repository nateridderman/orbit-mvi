package org.orbitmvi.orbit.swift.feature

import java.io.File
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

data class ProcessorContext(
    private val cocoapodsExtension: CocoapodsExtension,
    val framework: Framework
) {
    val outputDir: File by lazy {
        File(framework.outputDirectory, "${cocoapodsExtension.frameworkName}OrbitSwift")
    }
}
