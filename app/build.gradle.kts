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
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // UI
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.circleimageview)

    implementation("androidx.cardview:cardview:1.0.0")
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // text editor
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.ui.text.android)

    // TEST
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

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

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // advertisement
//    implementation("com.google.android.gms:play-services-ads:23.1.0")

    // text editor - k1
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc05")

    // text editor - k2 → k1, k2 차이는 잘 모르겠음
//    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc05-k2")
}