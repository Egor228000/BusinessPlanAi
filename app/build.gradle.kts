import com.android.build.api.dsl.Packaging
import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("com.google.devtools.ksp") version "2.0.21-1.0.25"
    kotlin("plugin.serialization") version "2.1.20"
}

android {
    namespace = "com.example.businessplanai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.businessplanai"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE.txt",
                "META-INF/LICENSE-LGPL-2.1.txt",
                "META-INF/LICENSE-LGPL-3.txt",
                "META-INF/NOTICE.txt",
                "META-INF/DEPENDENCIES",
                "META-INF/ASL2.0",
                "META-INF/NOTICE.md",
                "META-INF/LICENSE-W3C-TEST"
            )
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    configurations.all {
        exclude(group = "jakarta.activation", module = "jakarta.activation-api")
        exclude(group = "jakarta.mail", module = "jakarta.mail-api")
        exclude(group = "commons-logging", module = "commons-logging")
    }
}

dependencies {
    implementation (libs.androidx.material3.window.size.class1)
    implementation (libs.androidx.navigation.compose)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation(libs.lottie.compose)
    implementation (libs.androidx.core.splashscreen)

    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.multiplatform.markdown.renderer)

    // Для Word
    implementation(libs.flexmark.all)
    implementation(libs.poi.ooxml)
    implementation (libs.html2pdf)
// Для PDF
    implementation(libs.itext7.core)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}