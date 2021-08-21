/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.orbitmvi.orbit.swift

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.orbitmvi.orbit.swift.feature.ProcessorContext

class OrbitSwiftPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.withPlugin("org.jetbrains.kotlin.native.cocoapods") {
            val multiplatformExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
            val cocoapodsExtension = (multiplatformExtension as ExtensionAware).extensions.getByType(CocoapodsExtension::class.java)

            registerPodspecTask(project, cocoapodsExtension)
            val generateTasks = registerGenerateTasks(project, multiplatformExtension, cocoapodsExtension)

            //createSyncTask(project, multiplatformExtension, generateTasks)

            project.registerTask<Task>("syncOrbitSwift")
        }
    }

    private fun createSyncTask(
        project: Project,
        kotlinExtension: KotlinMultiplatformExtension,
        generateTasks: Map<Framework, TaskProvider<GenerateOrbitSwiftTask>>
    ) {
        val requestedTargetName = project.findProperty(KotlinCocoapodsPlugin.TARGET_PROPERTY)?.toString() ?: return
        val requestedBuildType = project.findProperty(KotlinCocoapodsPlugin.CONFIGURATION_PROPERTY)?.toString()?.toUpperCase() ?: return

        // We create a fat framework only for device platforms which have several
        // device architectures: iosArm64, iosArm32, watchosArm32 and watchosArm64.
        val frameworkPlatforms: List<KonanTarget> = when (requestedTargetName) {
            KotlinCocoapodsPlugin.KOTLIN_TARGET_FOR_IOS_DEVICE -> listOf(KonanTarget.IOS_ARM64, KonanTarget.IOS_ARM32)
            KotlinCocoapodsPlugin.KOTLIN_TARGET_FOR_WATCHOS_DEVICE -> listOf(KonanTarget.WATCHOS_ARM32, KonanTarget.WATCHOS_ARM64)
            // A request parameter can be comma separated list of targets.
            else -> requestedTargetName.split(",").map { HostManager().targetByName(it) }.toList()
        }

        val frameworkTargets = frameworkPlatforms.flatMap { kotlinExtension.targetsForPlatform(it) }
        // Ignoring fat at the moment and just picking the first...
        createSyncForRegularFramework(project, kotlinExtension, requestedBuildType, frameworkTargets.first().konanTarget, generateTasks)
    }

    private fun KotlinMultiplatformExtension.supportedTargets() = targets
        .withType(KotlinNativeTarget::class.java)
        .matching { it.konanTarget.family.isAppleFamily }

    private fun KotlinMultiplatformExtension.targetsForPlatform(requestedPlatform: KonanTarget) =
        supportedTargets().matching { it.konanTarget == requestedPlatform }

    private fun createSyncForRegularFramework(
        project: Project,
        kotlinExtension: KotlinMultiplatformExtension,
        requestedBuildType: String,
        requestedPlatform: KonanTarget,
        generateTasks: Map<Framework, TaskProvider<GenerateOrbitSwiftTask>>
    ) {
        val targets = kotlinExtension.targetsForPlatform(requestedPlatform)

        check(targets.isNotEmpty()) { "The project doesn't contain a target for the requested platform: `${requestedPlatform.visibleName}`" }
        check(targets.size == 1) { "The project has more than one target for the requested platform: `${requestedPlatform.visibleName}`" }

        val frameworkLinkTask = generateTasks.getValue(targets.single().binaries.getFramework(requestedBuildType))

        val dir = frameworkLinkTask.map { it.outputDirectoryProvider.get() }

        //targets.single().binaries.getFramework(requestedBuildType).linkTaskProvider
        //project.createSyncFrameworkTask(dir, frameworkLinkTask)

        project.registerTask<Sync>("syncOrbitSwift").configure {
            group = KotlinCocoapodsPlugin.TASK_GROUP
            description =
                "Copies the generated Orbit Multiplatform Swift code for given platform and build type into the CocoaPods build directory"

            dependsOn(frameworkLinkTask)
            from(dir)
            destinationDir = project.buildDir.resolve("cocoapods/framework")
        }
    }

    internal inline fun <reified T : Task> Project.registerTask(
        name: String,
        args: List<Any> = emptyList(),
        noinline body: ((T) -> (Unit))? = null
    ): TaskProvider<T> =
        this@registerTask.registerTask(name, T::class.java, args, body)

    internal fun <T : Task> Project.registerTask(
        name: String,
        type: Class<T>,
        constructorArgs: List<Any> = emptyList(),
        body: ((T) -> (Unit))? = null
    ): TaskProvider<T> {
        val resultProvider = project.tasks.register(name, type, *constructorArgs.toTypedArray())
        if (body != null) {
            resultProvider.configure(body)
        }
        return resultProvider
    }

    private fun registerPodspecTask(
        project: Project,
        cocoapodsExtension: CocoapodsExtension
    ) {
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
    }

    private fun registerGenerateTasks(
        project: Project,
        multiplatformExtension: KotlinMultiplatformExtension,
        cocoapodsExtension: CocoapodsExtension
    ): Map<Framework, TaskProvider<GenerateOrbitSwiftTask>> {
        val tasks = mutableMapOf<Framework, TaskProvider<GenerateOrbitSwiftTask>>()

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

                tasks[framework] = orbitTask
            }
        }

        return tasks.toMap()
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
