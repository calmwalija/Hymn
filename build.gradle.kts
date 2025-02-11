// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath(libs.spotless.plugin.gradle)
    classpath(libs.hilt.android.gradle.plugin)
    classpath(libs.google.services)
    classpath(libs.perf.plugin)
    classpath(libs.firebase.crashlytics.gradle)
  }
}

plugins {
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.jetbrainsKotlinAndroid) apply false
  alias(libs.plugins.hiltAndroid) apply false
  alias(libs.plugins.kotlin.compose) apply false
  id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
  alias(libs.plugins.google.gms.google.services) apply false
  alias(libs.plugins.google.firebase.crashlytics) apply false
}