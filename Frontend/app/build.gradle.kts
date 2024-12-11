plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.hiveeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hiveeapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    configurations.all {
        resolutionStrategy {
            // Ensure the correct version of Hamcrest is resolved
            force("org.hamcrest:hamcrest:2.2")
        }
    }
}

dependencies {
    // Core libraries
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.fragment:fragment:1.8.5")
    implementation("androidx.cardview:cardview:1.0.0")

    // Additional libraries
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("org.java-websocket:Java-WebSocket:1.5.7")
    implementation(libs.uiautomator)

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest:2.2") // Hamcrest for testing

    // Instrumented Testing
    androidTestImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")

    implementation(kotlin("script-runtime"))

    // UI Libraries
    implementation("com.maxkeppeler.sheets:core:2.3.1")
    implementation("com.tbuonomo:dotsindicator:4.2")
    implementation("com.lorentzos.swipecards:library:1.0.9")
    implementation("com.airbnb.android:lottie:5.2.0")
}
