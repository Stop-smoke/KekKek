# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

##################### KAKAO SDK ############################
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# https://github.com/square/okhttp/pull/6792
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**
##################### KAKAO SDK ############################

########################################################
# AndroidX / Jetpack
########################################################
# Parcelable 구현체 유지
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Room / DataStore / Lifecycle 등 리플렉션 사용 가능성
-keep class androidx.lifecycle.** { *; }
-keep class androidx.datastore.** { *; }

# Navigation SafeArgs 등
-keep class * extends androidx.navigation.NavDirections { *; }

########################################################
# Kotlin / Coroutines / Serialization
########################################################
# 코틀린 코루틴 내부 클래스 유지
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Kotlinx.serialization
-keepclassmembers class ** {
    @kotlinx.serialization.* <fields>;
}
-keep @kotlinx.serialization.Serializable class ** { *; }
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

########################################################
# Hilt / Dagger
########################################################
# Hilt / Dagger 생성 클래스 유지
-keep class dagger.hilt.internal.generated.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }
-dontwarn dagger.**

########################################################
# Firebase / Play Services
########################################################
-dontwarn com.google.firebase.messaging.**
-dontwarn com.google.firebase.installations.**
-dontwarn com.google.android.gms.**
-dontwarn com.google.ads.**

# Firebase Analytics & Crashlytics
-keep class com.google.firebase.analytics.** { *; }
-keep class com.google.firebase.crashlytics.** { *; }

# Firebase Messaging 서비스
-keep class * extends com.google.firebase.messaging.FirebaseMessagingService {
    <init>(...);
}
-keepclassmembers class * extends com.google.firebase.messaging.FirebaseMessagingService {
    public void onMessageReceived(...);
    public void onNewToken(...);
}

########################################################
# Google Play Services (Auth, Maps, Ads, Location)
########################################################
-keep class com.google.android.gms.common.internal.safeparcel.SafeParcelable { *; }
-keepclassmembers class * implements com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** CREATOR;
}

# Maps
-keep class com.google.android.libraries.maps.** { *; }
-keep class com.google.maps.android.** { *; }

########################################################
# Glide / Coil / Lottie
########################################################
# Glide 모델 파서 / API 유지
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule { *; }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** { *; }
-dontwarn com.bumptech.glide.**

# Coil
-dontwarn coil.**
-keep class coil.** { *; }

# Lottie
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

########################################################
# Kakao SDK
########################################################
-keep class com.kakao.sdk.** { *; }
-dontwarn com.kakao.sdk.**

########################################################
# 기타 라이브러리
########################################################
# Aztec Editor
-keep class org.wordpress.aztec.** { *; }
-dontwarn org.wordpress.aztec.**

# Algolia InstantSearch
-keep class com.algolia.instantsearch.** { *; }
-dontwarn com.algolia.instantsearch.**

# CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

########################################################
# 테스트 관련 (릴리즈엔 필요 없지만 라이브러리 경고 방지)
########################################################
-dontwarn org.junit.**
-dontwarn androidx.test.**
-dontwarn androidx.test.espresso.**

########################################################
# 안전 장치
########################################################
# 리플렉션 사용 클래스명 유지
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, AnnotationDefault

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder