plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.kapt")
  id("com.diffplug.spotless")
  id("dagger.hilt.android.plugin")
  id("com.google.gms.google-services")
//  id("com.google.firebase.crashlytics")
  id("com.google.firebase.firebase-perf")
  id("com.google.devtools.ksp") version "1.8.10-1.0.9"
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
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.3"
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
    format("xml") {
      target("**/*.xml")
      indentWithSpaces()
      trimTrailingWhitespace()
      endWithNewline()
    }
  }

  kapt {
    javacOptions {
      option("-Xmaxerrs", 500)
      option("-Xlint:deprecation", true)
      option("-Xlint:unchecked", true)
    }
    arguments {
      arg("plugin", "org.jetbrains.kotlin.kapt3:kotlin-allopen")
      arg("plugin", "org.jetbrains.kotlin.kapt3:kotlin-noarg")
    }
    useBuildCache = true
  }

  applicationVariants.all {
    addJavaSourceFoldersToModel(
      File(buildDir, "generated/ksp/$name/kotlin")
    )
  }

}

dependencies {

  // Firebase
  implementation("com.google.firebase:firebase-core:21.1.1")
//  implementation("com.google.firebase:firebase-crashlytics:18.6.0")
  implementation("com.google.firebase:firebase-analytics:21.5.0")
  implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
  implementation("com.google.firebase:firebase-perf-ktx:20.5.1")

  // Kotlin
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

  // Compose
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation(platform("androidx.compose:compose-bom:2023.10.01"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3:1.1.2")
  implementation("androidx.navigation:navigation-compose:2.7.6")

  // Test
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  // Dagger Hilt
  implementation("com.google.dagger:hilt-android:2.43.2")
  kapt("com.google.dagger:hilt-android-compiler:2.43.2")
  kapt("androidx.hilt:hilt-compiler:1.0.0")
  implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

  // Room
  implementation("androidx.room:room-runtime:2.6.1")
  kapt("androidx.room:room-compiler:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")

  //Paging 3
  implementation("androidx.paging:paging-runtime-ktx:3.2.1")
  implementation("androidx.room:room-paging:2.6.1")

  //Retrofit
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

  // DataStore
  implementation("androidx.datastore:datastore-preferences:1.1.0-alpha07")

  // Coil
  implementation("io.coil-kt:coil-compose:2.5.0")

  // Third party
  implementation("com.airbnb.android:lottie-compose:6.2.0")

  // Preference
  implementation("androidx.preference:preference-ktx:1.2.1")

  // Splash Screen
  implementation("androidx.core:core-splashscreen:1.0.1")

  implementation("io.github.raamcosta.compose-destinations:core:1.8.42-beta")
  ksp("io.github.raamcosta.compose-destinations:ksp:1.8.42-beta")

  // Kotlin stdlib
  constraints {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
      because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
      because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
    }
  }

  implementation("com.google.accompanist:accompanist-flowlayout:0.23.1")

}
