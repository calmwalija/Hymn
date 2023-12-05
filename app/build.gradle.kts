plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("dagger.hilt.android.plugin")
  id("androidx.navigation.safeargs.kotlin")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
  id("com.google.firebase.firebase-perf")
  id("com.diffplug.spotless")
}

android {
  compileSdk = 34
  namespace = "net.techandgraphics.hymn"

  defaultConfig {
    applicationId = "net.techandgraphics.hymn"
    minSdk = 21
    targetSdk = 34
    versionCode = 19
    versionName = "2.0.01"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    vectorDrawables {
      useSupportLibrary = true
    }

  }
  buildFeatures {
    dataBinding = true
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.5"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1,ASL-2.0.txt,LGPL-3.0.txt}"
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  spotless {
    kotlin {
      target("**/*.kt")
      trimTrailingWhitespace()
      indentWithSpaces()
      ktlint("0.41.0").userData(
        mapOf(
          "indent_size" to "2", "continuation_indent_size" to "2"
        )
      )
    }
  }

}

dependencies {
  // Core
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.9.0")
  implementation("androidx.palette:palette-ktx:1.0.0")
  implementation("androidx.core:core-splashscreen:1.0.1")
  implementation("androidx.preference:preference-ktx:1.2.1")
  implementation("com.google.android.flexbox:flexbox:3.0.0")

  // Firebase
  implementation("com.google.firebase:firebase-core:21.1.1")
  implementation("com.google.firebase:firebase-crashlytics:18.3.7")
  implementation("com.google.firebase:firebase-analytics:21.3.0")
  implementation("com.google.firebase:firebase-messaging-ktx:23.1.2")
  implementation("com.google.firebase:firebase-perf-ktx:20.3.3")


  // Dagger Hilt
  implementation("com.google.dagger:hilt-android:2.43.2")
  implementation("androidx.hilt:hilt-work:1.0.0")
  implementation("androidx.work:work-runtime-ktx:2.8.1")
  kapt("com.google.dagger:hilt-android-compiler:2.43.2")
  kapt("androidx.hilt:hilt-compiler:1.0.0")

  // Room
  implementation("androidx.room:room-runtime:2.5.1")
  kapt("androidx.room:room-compiler:2.5.1")
  implementation("androidx.room:room-ktx:2.5.1")

  // Fragment
  implementation("androidx.fragment:fragment-ktx:1.5.7")

  // Navigation components
  implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
  implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

  // Lifecycle
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
  implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

  // Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")

  // Activity KTX for viewModels()
  implementation("androidx.activity:activity-ktx:1.7.1")

  //Paging 3
  implementation("androidx.paging:paging-runtime-ktx:3.1.1")
  implementation("androidx.room:room-paging:2.5.1")

  // GSON
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")

  // Retrofit
  implementation("com.squareup.retrofit2:retrofit:2.9.0")

  // DataStore
  implementation("androidx.datastore:datastore-preferences:1.1.0-alpha05")

  // Third party
  implementation("com.airbnb.android:lottie:6.0.0")
  implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")

  // Glide
//    implementation("jp.wasabeef:glide-transformations:4.3.0")
  implementation("com.github.bumptech.glide:glide:4.16.0")

  // Tests
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  // Bubble-showcase
  implementation("com.elconfidencial.bubbleshowcase:bubbleshowcase:1.3.1")

  constraints {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
      because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
      because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
    }
  }

  //Work Manager
  implementation("androidx.work:work-runtime:2.8.1")


  implementation(platform("androidx.compose:compose-bom:2023.03.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.navigation:navigation-compose:2.7.2")


  androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

}
