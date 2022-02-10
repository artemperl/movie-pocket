import org.jetbrains.kotlin.kapt3.base.Kapt.kapt
import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

val privateProperties = Properties()
privateProperties.load(FileInputStream(rootProject.file("private.properties")))

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.ap.moviepocket"
        minSdk = 28
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        forEach {
            it.buildConfigField("String", "TMDB_API_KEY", privateProperties.getProperty("TMDB_API_KEY"))
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    // androidx
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    // Gson
    implementation("com.google.code.gson:gson:2.8.9")

    // junit
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")

    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // mockito
    implementation("org.mockito:mockito-core:4.3.1")

    // model
    implementation(project(":model"))
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}