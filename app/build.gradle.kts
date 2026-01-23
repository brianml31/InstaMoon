plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.brianml31.insta_moon"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.brianml31.insta_moon"
        minSdk = 26
        targetSdk = 35
        versionCode = 11
        versionName = "11"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(project(":InstaMoon"))
    implementation(project(":ACRA"))
    implementation(project(":Instagram"))
    implementation(project(":WVersionManager"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}