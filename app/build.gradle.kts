plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.firebase-perf")
    id("com.google.firebase.crashlytics")
    id("com.github.ben-manes.versions")
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "nz.memes.xkcdthing"
        minSdk = 21
        targetSdk = 33
        versionCode = 2
        versionName = "0.1"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    namespace = "nz.memes.xkcdthing"
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.room:room-compiler:2.4.3")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.22")
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("androidx.paging:paging-compose:1.0.0-alpha17")
    implementation(platform("com.google.firebase:firebase-bom:29.2.1"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.compose.ui:ui:1.3.2")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")
    implementation("androidx.compose.ui:ui-tooling:1.3.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.google.dagger:hilt-android:2.44.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.3.2")
    kapt("com.google.dagger:hilt-android-compiler:2.43.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.2")
    testImplementation("io.mockk:mockk-android:1.13.3")
    testImplementation("io.mockk:mockk-agent:1.13.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}
