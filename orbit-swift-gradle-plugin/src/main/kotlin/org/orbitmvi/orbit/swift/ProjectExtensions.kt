package org.orbitmvi.orbit.swift

import org.gradle.api.Project

internal fun Project.asValidFrameworkName() = name.replace('-', '_')
