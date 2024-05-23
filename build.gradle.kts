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
}