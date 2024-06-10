import com.android.build.api.dsl.ApplicationDefaultConfig
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    id("kotlin-parcelize")
    id("com.google.android.gms.oss-licenses-plugin")
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("apikey.properties")))
}

fun ApplicationDefaultConfig.addManifestPlaceholdersAndBuildConfig(key: String) {
    (properties[key] as? String)?.let {
        manifestPlaceholders[key] = it
        buildConfigField("String", key, "\"$it\"")
    }
}

android {
    namespace = "com.stopsmoke.kekkek"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stopsmoke.kekkek"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        addManifestPlaceholdersAndBuildConfig("KAKAO_NATIVE_API_KEY")
        addManifestPlaceholdersAndBuildConfig("FIRBASE_AUTH_SERVCER_CLIENT_KEY")
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
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    // CORE
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment.ktx)

    // UI
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.circleimageview)
    implementation("androidx.cardview:cardview:1.0.0")
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // text editor
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.activity)

    // TEST
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)

    // DESUGAR
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // KAKAO
    implementation(libs.kakao.sdk.v2.user) // 카카오 로그인 API 모듈

    //Naver Sns
    implementation("com.navercorp.nid:oauth:5.9.1") // jdk 11

    // FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    releaseImplementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation("com.google.firebase:firebase-auth")

    //Navigation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.android.material:material:1.12.0") // for Tablayout

    //admob
    implementation("com.google.android.gms:play-services-ads-lite:23.1.0")

    // PAGING
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")
    testImplementation("androidx.paging:paging-common-ktx:3.3.0")

    //google SNS with FireBase
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //Coil
    implementation("io.coil-kt:coil:2.1.0")

    //Oss-licenses
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")
    implementation(libs.android.gms.oos.licenses)

    //Datastore
    implementation("androidx.datastore:datastore-preferences-android:1.1.1")

    //Google Map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    //Google place
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.android.libraries.places:places:3.5.0")
}