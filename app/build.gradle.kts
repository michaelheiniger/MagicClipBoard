import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "ch.qscqlmpa.magicclipboard"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Versions.appVersionCode
        versionName = Versions.appVersionName

        testInstrumentationRunner = "ch.qscqlmpa.magicclipboard.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val keystoreProperties = Properties()
    val keystorePropertiesFile = rootProject.file("key.properties")

    signingConfigs {
        register("release") {
            if (keystorePropertiesFile.exists()) {
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                storeFile = file(keystoreProperties.getProperty("storeFile"))
            } else {
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                storeFile = file("keystore/keystore.jks")
            }
        }
        named("debug") {
            if (keystorePropertiesFile.exists()) {
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                storeFile = file(keystoreProperties.getProperty("storeFile"))
            } else {
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                storeFile = file("keystore/keystore.jks")
            }
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true // Enables code shrinking, obfuscation, and optimization

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        named("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }

    // To prevent the error message (AndroidTest): " 2 files found with path 'META-INF/AL2.0' from inputs: ..."
    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }

    testOptions {
        // Among other things: launches the App for each test (prevents cross-tests dependencies)
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

dependencies {
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.camera:camera-camera2:${Versions.camera}") // Required despite what's dependency-analysis (README) is saying
    implementation("androidx.camera:camera-lifecycle:${Versions.camera}")
    implementation("androidx.camera:camera-view:1.0.0-alpha14")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidLifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.androidLifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidLifecycle}")
    implementation("androidx.navigation:navigation-compose:2.4.0-beta02")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.21.3-beta")
    implementation("com.google.android.gms:play-services-auth:20.0.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-common-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx") // Realtime Database library
    implementation("com.google.zxing:core:3.4.1")
    implementation("io.insert-koin:koin-android:${Versions.koin}")
    implementation("io.insert-koin:koin-androidx-compose:${Versions.koin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinxCoroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.kotlinxCoroutines}")
    implementation("org.tinylog:tinylog-api-kotlin:${Versions.tinyLog}")
    implementation("org.tinylog:tinylog-impl:${Versions.tinyLog}")
    implementation(platform("com.google.firebase:firebase-bom:29.0.0")) // Firebase platform BoM

    testImplementation("android.arch.core:core-testing:1.1.1") // Required to use androidx.arch.core.executor.testing.InstantTaskExecutorRule in ViewModel unit tests
    testImplementation("io.insert-koin:koin-test-junit5:${Versions.koin}")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("org.assertj:assertj-core:${Versions.assertJ}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.jUnit5}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.jUnit5}")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:${Versions.jUnit5}")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0") // Espresso (needed for CounterIdlingResource)
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("io.insert-koin:koin-test-junit4:${Versions.koin}")
    androidTestImplementation("org.assertj:assertj-core:${Versions.assertJ}")
}

// For jUnit5 tests
tasks.withType<Test> {
    useJUnitPlatform()
}
