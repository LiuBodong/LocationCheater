plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "org.codebase.locationcheater"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.codebase.locationcheater"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            merges += "META-INF/xposed/*"
            excludes += "**"
        }
    }
    lint {
        abortOnError = true
        checkReleaseBuilds = false
    }
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    compileOnly(libs.libxposed.api)
    implementation(libs.libxposed.service)
    implementation(libs.room)
    annotationProcessor(libs.room.compiler)
    implementation(libs.jackson)
    implementation(libs.commons.lang3)
}