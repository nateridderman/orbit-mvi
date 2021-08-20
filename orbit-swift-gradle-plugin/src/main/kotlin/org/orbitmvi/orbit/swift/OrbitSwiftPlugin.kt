package org.orbitmvi.orbit.swift

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.orbitmvi.orbit.swift.feature.ProcessorContext

class OrbitSwiftPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.withPlugin("org.jetbrains.kotlin.native.cocoapods") {
            val multiplatformExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
            val cocoapodsExtension = (multiplatformExtension as ExtensionAware).extensions.getByType(CocoapodsExtension::class.java)

            project.tasks.register("orbitPodspec", PodspecTask::class.java) {
                specName = cocoapodsExtension.frameworkName + "OrbitSwift"
                group = KotlinCocoapodsPlugin.TASK_GROUP
                description = "Generates a podspec file for CocoaPods import for Orbit Multiplatform"
                pods.set(
                    listOf(CocoapodsExtension.CocoapodsDependency(cocoapodsExtension.frameworkName, cocoapodsExtension.frameworkName))
                )
                version = project.provider { cocoapodsExtension.version }
                homepage.set(cocoapodsExtension.homepage)
                license.set(cocoapodsExtension.license)
                authors.set(cocoapodsExtension.authors)
                summary.set(cocoapodsExtension.summary)
                ios = project.provider { cocoapodsExtension.ios }
                osx = project.provider { cocoapodsExtension.osx }
                tvos = project.provider { cocoapodsExtension.tvos }
                watchos = project.provider { cocoapodsExtension.watchos }
                //dependsOn(dummyFrameworkTaskProvider)
                //val generateWrapper = project.findProperty(KotlinCocoapodsPlugin.GENERATE_WRAPPER_PROPERTY)?.toString()?.toBoolean() ?: false
                //if (generateWrapper) {
                //    it.dependsOn(":wrapper")
                //}
            }

            multiplatformExtension.targets.withType<KotlinNativeTarget>().matching { it.konanTarget.family.isAppleFamily }.configureEach {
                binaries.withType<Framework>().configureEach {
                    val framework = this

                    val processorContext = ProcessorContext(
                        cocoapodsExtension = cocoapodsExtension,
                        framework = framework
                    )

                    val taskName = lowerCamelCaseName("generateOrbitSwift", framework.name, framework.architectureName)

                    val orbitTask = project.tasks.register(taskName, GenerateOrbitSwiftTask::class.java, processorContext).apply {
                        configure {
                            group = BasePlugin.BUILD_GROUP
                            description =
                                "Generate Orbit Multiplatform Swift code for framework '${framework.name}' and target '${framework.architectureName}'"
                        }
                    }

                    framework.linkTask.finalizedBy(orbitTask)
                }
            }
        }
    }

    private fun lowerCamelCaseName(vararg nameParts: String?): String {
        val nonEmptyParts = nameParts.mapNotNull { it?.takeIf(String::isNotEmpty) }
        return nonEmptyParts.drop(1).joinToString(
            separator = "",
            prefix = nonEmptyParts.firstOrNull().orEmpty(),
            transform = String::capitalize
        )
    }
}
