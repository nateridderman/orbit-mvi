/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.orbitmvi.orbit.swift.feature

import kotlinx.metadata.KmClass
import kotlinx.metadata.KmFunction
import kotlinx.metadata.KmPackage
import kotlinx.metadata.klib.KlibModuleMetadata

interface Processor {

    /**
     * Called once per task execution
     */
    fun visit(processorContext: ProcessorContext) = Unit

    fun visitLibrary(processorContext: ProcessorContext, libraryMetadata: KlibModuleMetadata) = Unit

    fun visitPackage(processorContext: ProcessorContext, pkg: KmPackage) = Unit

    fun visitClass(processorContext: ProcessorContext, clazz: KmClass) = Unit

    fun visitPackageFunction(processorContext: ProcessorContext, func: KmFunction) = Unit
}
