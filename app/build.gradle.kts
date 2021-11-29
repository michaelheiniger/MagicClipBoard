plugins {
    id("com.android.application")
    kotlin("android")
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

    buildTypes {
        named("release") {
            isMinifyEnabled = true // Enables code shrinking, obfuscation, and optimization

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }

    // To prevent the error message (AndroidTest): " 2 files found with path 'META-INF/AL2.0' from inputs: ..."
    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

dependencies {
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("com.google.zxing:core:3.4.1")
    implementation("io.insert-koin:koin-android:${Versions.koin}")
    implementation("io.insert-koin:koin-androidx-compose:${Versions.koin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.tinylog:tinylog-api-kotlin:${Versions.tinyLog}")
    implementation("org.tinylog:tinylog-impl:${Versions.tinyLog}")

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
