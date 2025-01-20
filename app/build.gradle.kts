plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("kotlin-parcelize")
    id("org.jetbrains.dokka")
    alias(libs.plugins.ktlint.gradle)
}

android {
    namespace = "pl.maciejwojs.ar00k.bestnotepadevercreaated"
    compileSdk = 35

    defaultConfig {
        applicationId = "pl.maciejwojs.ar00k.bestnotepadevercreaated"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    signingConfigs {
//        create("release") {
//            storeFile = project.file("../keystore.jks")
//            storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
//            keyAlias = System.getenv("RELEASE_KEYSTORE_ALIAS")
//            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
//        }
//    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    subprojects {
        apply(plugin = "org.jetbrains.dokka")
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
}
ktlint {
    android.set(true) // Enable Android-specific linting rules
    ignoreFailures.set(true) // Prevents build from failing due to linting errors
}

tasks.named("preBuild") {
    dependsOn("ktlintFormat")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // biometric i fragment ktx
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    val PageNavLibVersion = "2.1.0-beta14"
//    implementation("io.github.raamcosta.compose-destinations:core:$PageNavLibVersion")
//    ksp("io.github.raamcosta.compose-destinations:ksp:$PageNavLibVersion")

//    val activity_version = "1.9.3"

    // Java language implementation
    implementation(libs.androidx.activity)
    // Kotlin
    implementation(libs.androidx.activity.ktx)

//    val nav_version = "2.8.3"
    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)

    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Feature module support for Fragments
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

//    val room_version = "2.6.1"

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    // To use Kotlin Symbol Processing (KSP)
    ksp(libs.androidx.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    // optional - RxJava2 support for Room
    implementation(libs.androidx.room.rxjava2)

    // optional - RxJava3 support for Room
    implementation(libs.androidx.room.rxjava3)

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(libs.androidx.room.guava)

    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    // Biblioteka do kamery
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    // Ikonki
//    implementation(libs.androidx.material.extended.icons)
    implementation(libs.material3)
    implementation(libs.androidx.material.icons.extended)
}
