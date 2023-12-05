// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.1.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
    classpath("com.google.gms:google-services:4.3.15")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.6")
    classpath("com.google.firebase:perf-plugin:1.4.2")
    classpath("com.diffplug.spotless:spotless-plugin-gradle:6.6.0")
  }
}

plugins {
  id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}