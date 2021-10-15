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
