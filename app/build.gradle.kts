import com.google.gms.googleservices.GoogleServicesTask

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.compose)
  id("com.google.devtools.ksp")
  id("com.diffplug.spotless")
  id("dagger.hilt.android.plugin")
  id("org.jetbrains.kotlin.kapt")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
  id("com.google.firebase.firebase-perf")
}

project.afterEvaluate {
  tasks.withType<GoogleServicesTask> {
    gmpAppId.set(project.layout.buildDirectory.file("$name-gmpAppId.txt"))
  }
}

android {
  compileSdk = 35
  namespace = "net.techandgraphics.hymn"

  defaultConfig {
    applicationId = "net.techandgraphics.hymn"
    minSdk = 21
    targetSdk = 35
    versionCode = 22
    versionName = "3.1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    vectorDrawables {
      useSupportLibrary = true
    }

  }
  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1,ASL-2.0.txt,LGPL-3.0.txt}"
    }
  }

  buildTypes {

    create("dev") {
      applicationIdSuffix = ".dev"
      versionNameSuffix = "-dev"
      firebaseCrashlytics {
        mappingFileUploadEnabled = false
      }
      signingConfig = signingConfigs.getByName("debug")
    }


    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    debug {
      firebaseCrashlytics {
        mappingFileUploadEnabled = false
      }
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
      option("-Xmaxerrs", 500.toString())
      option("-Xlint:deprecation", true.toString())
      option("-Xlint:unchecked", true.toString())
    }
    arguments {
      arg("plugin", "org.jetbrains.kotlin.kapt3:kotlin-allopen")
      arg("plugin", "org.jetbrains.kotlin.kapt3:kotlin-noarg")
    }
    useBuildCache = true
  }

}

dependencies {

  // Firebase
  implementation(libs.firebase.core)
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)
  implementation(libs.firebase.messaging.ktx)
  implementation(libs.firebase.perf.ktx)

  //Kotlin Core
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.navigation.runtime.ktx)
  implementation(libs.androidx.navigation.compose)

  // Room
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  implementation(libs.androidx.room.ktx)

  //Paging 3
  implementation(libs.androidx.paging.runtime.ktx)
  implementation(libs.androidx.paging.compose)
  implementation(libs.androidx.room.paging)

  //Serialization
  implementation(libs.kotlinx.serialization.json)

  //Dagger Hilt
  implementation(libs.hilt.android)
  kapt(libs.hilt.android.compiler)
  kapt(libs.androidx.hilt.compiler)
  implementation(libs.androidx.hilt.navigation.compose)

  //DataStore
  implementation(libs.androidx.datastore.preferences)

  //Gson
  implementation(libs.gson)

  // Splash Screen
  implementation(libs.androidx.core.splashscreen)


  implementation(libs.accompanist.systemuicontroller)


  //Coil
  implementation(libs.coil.compose)

  //Testing
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
  implementation(kotlin("reflect"))
}
