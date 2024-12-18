import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.calmscope"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.calmscope"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //load the values from .properties file
        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //return empty key in case something goes wrong
        val apiKey = properties.getProperty("apiKey") ?: ""
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        manifestPlaceholders["apiKey"] = apiKey
    }

    buildFeatures{
        buildConfig = true
    }

    buildTypes {
        android.buildFeatures.buildConfig = true
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment)
    implementation(libs.play.services.maps)
    implementation (libs.play.services.location)
    implementation(libs.navigation.runtime)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    testImplementation(libs.junit)
    implementation ("com.google.android.libraries.places:places:4.1.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
}