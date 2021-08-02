buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://www.lightstreamer.com/repo/maven") }
        maven { setUrl("https://dl.bintray.com/lisawray/maven") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
