plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.annasolox.kipon"
    compileSdk = 35

    lint {
        baseline = file("lint-baseline.xml")
    }

    defaultConfig {
        applicationId = "com.annasolox.kipon"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/*.*"
        }
    }

    buildFeatures{
        buildConfig = true
    }

    buildTypes {
        debug {
            enableAndroidTestCoverage = true
            //buildConfigField("String", "HOST_URL",  "\"kipon-api.us-east-1.elasticbeanstalk.com\"")
            buildConfigField("String", "HOST_URL",  "\"10.0.2.2\"")
            //buildConfigField("String", "HOST_URL", "\"192.168.1.244\"")
            buildConfigField("int", "HOST_PORT", "8080")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "HOST_URL",  "\"kipon-api.us-east-1.elasticbeanstalk.com\"")
            //buildConfigField("String", "HOST_URL",  "\"10.0.2.2\"")
            buildConfigField("int", "HOST_PORT", "8080")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-test
    implementation(libs.kotlinx.coroutines.test)
    // https://mvnrepository.com/artifact/androidx.arch.core/core-testing
    testImplementation(libs.androidx.core.testing)
    // https://mvnrepository.com/artifact/io.mockk/mockk
    testImplementation(libs.mockk)
    // https://mvnrepository.com/artifact/androidx.navigation/navigation-testing
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.mockk.instrumented)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.kotlinx.serialization.json)

    //compose-navigation
    implementation(libs.androidx.navigation.compose)
    //compose-viewmodel
    implementation(libs.androidx.runtime.livedata)
    //compose-constraintlayout
    implementation(libs.androidx.constraintlayout.compose)

    //material3-icons-extended
    implementation(libs.androidx.material.icons.extended)

    //ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlin.ktor.serialization)
    testImplementation(libs.ktor.client.mock)

    //koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.coroutines)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.ktor)
    implementation(libs.koin.ktor.logger)
    implementation(libs.koin.test)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}