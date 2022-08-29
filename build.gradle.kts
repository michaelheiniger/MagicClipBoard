// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("org.jlleitschuh.gradle.ktlint-idea") version Versions.ktlintGradlePlugin
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidGradlePlugin}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")
        classpath("com.google.gms:google-services:4.3.13")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:${Versions.androidJUnit5Plugin}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    apply(plugin = ("org.jlleitschuh.gradle.ktlint"))
    apply(plugin = ("io.gitlab.arturbosch.detekt"))

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-Xopt-in=androidx.compose.ExperimentalComposeApi",
                "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-Xopt-in=androidx.compose.runtime.ExperimentalComposeApi",
                "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-Xopt-in=androidx.compose.ui.test.ExperimentalTestApi",
                "-Xopt-in=coil.annotation.ExperimentalCoilApi",
                "-Xopt-in=kotlin.ExperimentalUnsignedTypes",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.InternalCoroutinesApi",
            )
        }
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

ktlint {
    debug.set(false)
    verbose.set(false)
}

detekt {
    toolVersion = Versions.detekt
    config = files("config/detekt/detekt.yml")
}
