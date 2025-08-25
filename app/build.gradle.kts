import com.android.build.api.dsl.ApplicationDefaultConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
    alias(libs.plugins.kotlin.serialization)
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
    namespace = "com.agvber.kekkek"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.agvber.kekkek"
        minSdk = 24
        targetSdk = 36
        versionCode = 8
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        addManifestPlaceholdersAndBuildConfig("KAKAO_NATIVE_API_KEY")
        addManifestPlaceholdersAndBuildConfig("FIRBASE_AUTH_SERVCER_CLIENT_KEY")
        addManifestPlaceholdersAndBuildConfig("ALGOLIA_APPLICATION_ID")
        addManifestPlaceholdersAndBuildConfig("ALGOLIA_SEARCH_API_KEY")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
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
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.coil.kt.coil)
    implementation(libs.glide)
    implementation(libs.lottie)

    // text editor
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.activity)
    implementation(libs.aztec)

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

    // Google Auth
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // KAKAO
    implementation(libs.kakao.sdk.v2.user) // 카카오 로그인 API 모듈

    // FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    releaseImplementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    //Navigation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.hilt.navigation.fragment)

    // PAGING
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.instantsearch.android.paging3)
    testImplementation(libs.androidx.paging.common.ktx)

    //google SNS with FireBase
    implementation(libs.play.services.auth)

    //Google Map
    implementation(libs.play.services.maps)
    implementation(libs.gms.play.services.location)

    //Google place
    implementation(libs.gms.play.services.location)
    implementation(libs.places)

    //Oss-licenses
    implementation(libs.android.gms.oos.licenses)

    //Datastore
    implementation(libs.androidx.datastore.preferences.android)

    //ExifInterface - 이미지의 EXIF 데이터를 제대로 처리(사진을 찍을 때 기기의 방향 정보를 포함)
    implementation(libs.androidx.exifinterface)

    //splash 아이콘 숨기기
    implementation(libs.androidx.core.splashscreen)

    //rxjava
    implementation("io.reactivex.rxjava3:rxjava:3.0.13")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    //admob
    implementation(libs.play.services.ads)
    implementation(libs.firebase.ads)
}