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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(Libs.activityCompose)
    implementation(Libs.composeMaterial)
    implementation(Libs.composeUi)
    implementation(Libs.composeUiToolingPreview)
    implementation(Libs.coreKtx)
    implementation(Libs.kotlinxCoroutinesCore)
    implementation(Libs.kotlinxCoroutinesAndroid)
    implementation(Libs.lifecycleRuntimeKtx)
    implementation(Libs.tinyLogApi)
    implementation(Libs.tinyLogImpl)

    testImplementation(Libs.jUnit5Api)
    testRuntimeOnly(Libs.jUnit5Engine)
    testRuntimeOnly(Libs.jUnit5Vintage)
    androidTestImplementation(Libs.testExtJUnit)
    testImplementation(Libs.mockk)
    testImplementation(Libs.assertJCore)
    androidTestImplementation(Libs.assertJCore)
    androidTestImplementation(Libs.testEspressoCore)
    androidTestImplementation(Libs.composeUiTestJUnit4)
    debugImplementation(Libs.composeUiTooling)
    testImplementation(Libs.kotlinxCoroutinesTest)
}

// For jUnit5 tests
tasks.withType<Test> {
    useJUnitPlatform()
}
