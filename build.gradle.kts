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
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("com.google.gms:google-services:4.3.10")
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
                "-Xuse-experimental=androidx.compose.animation.ExperimentalAnimationApi",
                "-Xuse-experimental=androidx.compose.ExperimentalComposeApi",
                "-Xuse-experimental=androidx.compose.foundation.ExperimentalFoundationApi",
                "-Xuse-experimental=androidx.compose.material.ExperimentalMaterialApi",
                "-Xuse-experimental=androidx.compose.runtime.ExperimentalComposeApi",
                "-Xuse-experimental=androidx.compose.ui.ExperimentalComposeUiApi",
                "-Xuse-experimental=androidx.compose.ui.test.ExperimentalTestApi",
                "-Xuse-experimental=coil.annotation.ExperimentalCoilApi",
                "-Xuse-experimental=kotlin.ExperimentalUnsignedTypes",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xuse-experimental=kotlinx.coroutines.InternalCoroutinesApi",
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
    debug.set(true)
    verbose.set(true)
}

detekt {
    toolVersion = Versions.detekt
    config = files("config/detekt/detekt.yml")

    reports {
        xml {
            enabled = true
            destination = file("reports/detekt-report.xml")
        }
        html {
            enabled = true
            destination = file("reports/detekt-report.html")
        }
        txt {
            enabled = true
            destination = file("reports/detekt-report.txt")
        }
    }
}
