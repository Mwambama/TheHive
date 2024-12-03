plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("jacoco")
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.register("createDebugCoverageReport", JacocoReport::class) {
    dependsOn("connectedDebugAndroidTest")

    val debugTree = fileTree("${buildDir}/intermediates/classes/debug") {
        exclude("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*")
    }
    val debugExecutionData = fileTree("${buildDir}") {
        include("**/*.exec")
    }

    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(debugTree)
    executionData.setFrom(debugExecutionData)

    reports {
        html.required.set(true)
        html.outputLocation.set(file("${buildDir}/reports/jacoco/debugCoverageReport"))
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.fragment:fragment:1.8.5")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("org.java-websocket:Java-WebSocket:1.5.7")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    implementation(kotlin("script-runtime"))

    implementation("com.maxkeppeler.sheets:core:2.3.1")
    implementation("com.tbuonomo:dotsindicator:4.2")
    implementation("com.lorentzos.swipecards:library:1.0.9")
}
