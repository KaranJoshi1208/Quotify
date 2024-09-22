// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.devtools.ksp") version "2.0.10-1.0.24" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.21" apply false
}