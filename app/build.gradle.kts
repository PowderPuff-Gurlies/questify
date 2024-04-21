plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.and101_capstone"
    compileSdk = 34

    packaging {
        resources.merges.add("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        applicationId = "com.example.and101_capstone"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
//    implementation ("com.github.bumptech.glide:glide:4.11.0")
//    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
//    implementation("com.google.api-client:google-api-client:2.0.0")
//    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("net.openid:appauth:0.11.1")


    implementation("androidx.credentials:credentials:1.2.2")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    implementation("com.google.apis:google-api-services-calendar:v3-rev20220715-2.0.0")

    implementation("com.google.api-client:google-api-client:1.32.1")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.30.4")
   // implementation("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")
    implementation("com.google.android.gms:play-services-auth:19.2.0")

    implementation ("com.loopj.android:android-async-http:1.4.11") //for ASync
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation("com.codepath.libraries:asynchttpclient:2.2.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.base)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}