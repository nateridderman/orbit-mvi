package org.orbitmvi.orbit.swift.feature

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import java.io.File

class PublisherProcessor : Processor {

    override fun visit(processorContext: ProcessorContext) {
        val outputData = template.execute(
            mapOf(
                "frameworkName" to processorContext.framework.baseName
            )
        )

        File(processorContext.outputDir, "Publisher.swift").apply {
            createNewFile()
            writeText(outputData)
        }
    }

    companion object {
        private val template: Template = Mustache.compiler()
            .compile(StateObjectProcessor::class.java.classLoader.getResource("Publisher.swift.mustache")!!.readText())
    }
}
