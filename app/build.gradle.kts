plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.calmscope"
    compileSdk = 34

    aaptOptions{
        noCompress += ("tflite");
    }

    defaultConfig {
        applicationId = "com.example.calmscope"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        mlModelBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.0")
    implementation("androidx.compose.ui:ui-graphics-android:1.7.5")
    implementation("androidx.navigation:navigation-runtime:2.8.4")
    testImplementation("junit:junit:4.13.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("org.tensorflow:tensorflow-lite-task-vision:0.4.2")
    implementation("org.tensorflow:tensorflow-lite:2.12.0")
    implementation("androidx.camera:camera-core:1.2.3")
    implementation("androidx.camera:camera-view:1.2.3")
    implementation("androidx.camera:camera-lifecycle:1.2.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10") // Use the Kotlin version you want
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10") // Make sure both are the same version
}
