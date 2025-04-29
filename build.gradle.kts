plugins {
    id("com.android.application") version "8.5.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.gradle.v802)
        classpath(libs.google.services.v4314)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.withType<JavaCompile> {
    options.release.set(11)
}
