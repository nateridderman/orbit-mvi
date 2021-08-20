/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("PackageDirectoryMismatch") // Old package for compatibility
package org.orbitmvi.orbit.swift

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension.CocoapodsDependency
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension.PodspecPlatformSettings

/**
 * The task generates a podspec file which allows a user to
 * integrate a Kotlin/Native framework into a CocoaPods project.
 */
open class PodspecTask : DefaultTask() {

    @get:Input
    internal var specName: String = project.asValidFrameworkName() + "OrbitSwift"

    @get:OutputFile
    internal val outputFileProvider: Provider<File>
        get() = project.provider { project.file("$specName.podspec") }

    @get:Nested
    val pods = project.objects.listProperty(CocoapodsDependency::class.java)

    @get:Input
    internal lateinit var version: Provider<String>

    @get:Input
    @get:Optional
    internal val homepage = project.objects.property(String::class.java)

    @get:Input
    @get:Optional
    internal val license = project.objects.property(String::class.java)

    @get:Input
    @get:Optional
    internal val authors = project.objects.property(String::class.java)

    @get:Input
    @get:Optional
    internal val summary = project.objects.property(String::class.java)

    @get:Nested
    internal lateinit var ios: Provider<PodspecPlatformSettings>

    @get:Nested
    internal lateinit var osx: Provider<PodspecPlatformSettings>

    @get:Nested
    internal lateinit var tvos: Provider<PodspecPlatformSettings>

    @get:Nested
    internal lateinit var watchos: Provider<PodspecPlatformSettings>

    @TaskAction
    fun generate() {
        val root = project.buildDir.resolve("cocoapods")
        val framework = root.resolve("framework")

        val frameworkDir = framework.relativeTo(outputFileProvider.get().parentFile).path
        val dependencies = pods.get().joinToString(separator = "\n") { pod ->
            val versionSuffix = if (pod.version != null) ", '${pod.version}'" else ""
            "|    spec.dependency '${pod.name}'$versionSuffix"
        }

        val deploymentTargets = run {
            listOf(ios, osx, tvos, watchos).map { it.get() }.filter { it.deploymentTarget != null }.joinToString("\n") {
                "|    ${"spec.${it.name}.deployment_target".padEnd(30, ' ')}= '${it.deploymentTarget}'"
            }
        }

        with(outputFileProvider.get()) {
            writeText(
                """
                |Pod::Spec.new do |spec|
                |    spec.name                     = '$specName'
                |    spec.version                  = '${version.get()}'
                |    spec.homepage                 = '${homepage.getOrEmpty()}'
                |    spec.source                   = { :git => "Not Published", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
                |    spec.authors                  = '${authors.getOrEmpty()}'
                |    spec.license                  = '${license.getOrEmpty()}'
                |    spec.summary                  = '${summary.getOrEmpty()}'
                |
                |    spec.static_framework         = true
                |    spec.source_files             = "$frameworkDir/$specName/**/*.{h,m,swift}"
                |    spec.module_name              = "#{spec.name}"
                |
                $deploymentTargets
                |
                $dependencies
                |
                |end
        """.trimMargin()
            )

            if (hasPodfileOwnOrParent(project)) {
                logger.quiet(
                    """
                    Generated a podspec file at: ${absolutePath}.
                    To include it in your Xcode project, check that the following dependency snippet exists in your Podfile:

                    pod '$specName', :path => '${parentFile.absolutePath}'

            """.trimIndent()
                )
            }

        }
    }

    private fun Provider<String>.getOrEmpty() = getOrElse("")

    companion object {
        private val KotlinMultiplatformExtension?.cocoapodsExtensionOrNull: CocoapodsExtension?
            get() = (this as? ExtensionAware)?.extensions?.findByType(CocoapodsExtension::class.java)

        private fun hasPodfileOwnOrParent(project: Project): Boolean =
            if (project.rootProject == project) project.multiplatformExtensionOrNull?.cocoapodsExtensionOrNull?.podfile != null
            else project.multiplatformExtensionOrNull?.cocoapodsExtensionOrNull?.podfile != null
                    || (project.parent?.let { hasPodfileOwnOrParent(it) } ?: false)

        private val Project.multiplatformExtensionOrNull: KotlinMultiplatformExtension?
            get() = extensions.findByType(KotlinMultiplatformExtension::class.java)
    }
}
