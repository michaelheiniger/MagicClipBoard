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
        create("release") {
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
                storeFile = file("keystore.jks")
            }
        }
        getByName("debug") {
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
                storeFile = file("keystore.jks")
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
            signingConfig = signingConfigs.getByName("release")
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
        kotlinCompilerExtensionVersion = "1.3.0"
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
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.activity:activity-compose:1.5.1")

    // Required despite what dependency-analysis (README) is saying
    implementation("androidx.camera:camera-camera2:1.0.0-beta07")
    implementation("androidx.camera:camera-lifecycle:1.0.0-beta07")
    implementation("androidx.camera:camera-view:1.0.0-alpha14")

    implementation("androidx.compose.material:material:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.5.1")
    implementation("com.firebaseui:firebase-ui-auth:8.0.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.26.2-beta")
    implementation("com.google.android.gms:play-services-auth:20.2.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-common-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx") // Realtime Database library
    implementation("com.google.zxing:core:3.4.1")
    // koin 3.2.0 seems incompatible with kotlin 1.7.10: java.lang.IllegalStateException: Compose Runtime internal error. Unexpected or incorrect use of the Compose internal runtime API (Start/end imbalance).
    implementation("io.insert-koin:koin-android:3.1.6")
    implementation("io.insert-koin:koin-androidx-compose:3.1.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    implementation("org.tinylog:tinylog-api-kotlin:2.4.1")
    implementation("org.tinylog:tinylog-impl:2.4.1")
    implementation(platform("com.google.firebase:firebase-bom:29.0.0")) // Firebase platform BoM
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Required to use androidx.arch.core.executor.testing.InstantTaskExecutorRule in ViewModel unit tests
    testImplementation("android.arch.core:core-testing:1.1.1")
    testImplementation("io.insert-koin:koin-test-junit5:3.1.6")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.8.2")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.1")
    // Espresso (needed for CounterIdlingResource)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("io.insert-koin:koin-test-junit4:3.1.6")
    androidTestImplementation("org.assertj:assertj-core:3.21.0")
}

// For jUnit5 tests
tasks.withType<Test> {
    useJUnitPlatform()
}
