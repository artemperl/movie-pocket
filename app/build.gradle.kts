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

    val corektxVersion = "1.7.0"
    val appcompatVersion = "1.4.1"
    val constraintLayoutVersion = "2.1.3"
    val materialVersion = "1.5.0"
    val navigationVersion = "2.4.2"
    val lifecycleVersion = "2.4.1"
    val archVersion = "2.1.0"
    val swiperefreshVersion = "1.1.0"
    val hiltVersion = "2.38.1"
    val timberVersion = "5.0.1"
    val retrofitVersion = "2.9.0"
    val picassoVersion = "2.71828"
    val gsonVersion = "2.8.9"
    val junitVersion = "4.13.2"
    val espressoVersion = "3.4.0"
    val mockitoVersion = "4.3.1"

    // androidx
    implementation("androidx.core:core-ktx:$corektxVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swiperefreshVersion")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:$timberVersion")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Picasso
    implementation("com.squareup.picasso:picasso:$picassoVersion")

    // Gson
    implementation("com.google.code.gson:gson:$gsonVersion")

    // junit
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitVersion")

    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

    // Architecture testing
    testImplementation("androidx.arch.core:core-testing:$archVersion")

    // mockito
    implementation("org.mockito:mockito-core:$mockitoVersion")

    // model
    implementation(project(":model"))
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}