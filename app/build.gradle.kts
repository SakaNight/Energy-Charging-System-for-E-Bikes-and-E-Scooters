plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.echarging"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    namespace = "com.example.echarging"
}

dependencies {
    implementation(libs.androidx.core.ktx.v190)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx.v251)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v251)
    implementation(libs.kotlin.stdlib)
    // Compose dependencies
    implementation(libs.androidx.activity.compose.v190)
    implementation(libs.androidx.ui.v140)
    implementation(libs.androidx.ui.tooling.preview.v140)
    implementation(libs.androidx.material3.v100alpha15)
    // Firebase dependencies
    implementation(platform(libs.firebase.bom.v3312))
    implementation(libs.com.google.firebase.firebase.auth.ktx9)
    implementation(libs.com.google.firebase.firebase.firestore.ktx4)
    implementation(libs.firebase.auth.ktx.v2110)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.activity.compose.v160)
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.ui.tooling)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3.v121)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.ui.tooling.v147)
    implementation(libs.androidx.material3.android)
    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v113)
    androidTestImplementation(libs.androidx.espresso.core.v340)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling.v140)
    debugImplementation(libs.ui.test.manifest)
    // ZXing dependencies for QR code scanning
    implementation(libs.zxing.android.embedded)
    implementation(libs.core.v333)
    implementation(libs.core)
    implementation(libs.zxing.android.embedded.v410)

    implementation(libs.androidx.recyclerview)
    implementation(libs.google.firebase.firestore)
    implementation(libs.material.v150)
}
