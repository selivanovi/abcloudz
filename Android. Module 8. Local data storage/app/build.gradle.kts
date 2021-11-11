plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.example.localdatastorage"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile ("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility  = JavaVersion.VERSION_1_8
        targetCompatibility =  JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}



dependencies {

    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.material)
    implementation(Dependencies.Android.constraintLayout)

    testImplementation(Dependencies.Test.jUnit)
    androidTestImplementation(Dependencies.Test.androidJUnit)
    androidTestImplementation(Dependencies.Test.espresso)

    //NavigationUI
    implementation(Dependencies.NavigationUI.navigationFragmentKtx)
    implementation(Dependencies.NavigationUI.navigationUIKtx)

    //Biometric
    implementation(Dependencies.Biometric.biometricKtx)

    //Crypto
    implementation(Dependencies.Crypto.crypto)

    //GSON
    implementation(Dependencies.GSON.gson)

    //Room
    implementation(Dependencies.Room.roomRuntime)
    implementation(Dependencies.Room.roomKtx)
    kapt(Dependencies.Room.roomCompiler)

}