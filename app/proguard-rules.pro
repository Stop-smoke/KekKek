########################################################
# R8 Global Settings
########################################################
# 최적화 및 축소는 수행하되, 이름 난독화(암호화)는 하지 않음
-dontobfuscate
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

# Firebase Messaging - 모든 클래스, 인터페이스, 생성자, 필드, 메서드 유지
-keep class com.google.firebase.messaging.** { *; }

# Firebase Events (Subscriber 인터페이스 포함) - 모든 클래스, 인터페이스, 생성자, 필드, 메서드 유지
-keep class com.google.firebase.events.** { *; }

# Firebase Components (FirebaseApp, ComponentRegistrar 등) - 잠재적 초기화 문제 방지
-keep class com.google.firebase.components.** { *; }

# Firebase Installations - Messaging이 의존할 수 있음
-keep class com.google.firebase.installations.** { *; }

# Firebase Common (내부 유틸리티)
-keep class com.google.firebase.common.** { *; }

# Google Android GMS Tasks API (Firebase가 내부적으로 많이 사용)
-keep class com.google.android.gms.tasks.** { *; }

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

# -keep class com.firebase.** { *; } 규칙은 좀 더 구체적인 com.google.firebase.** 규칙들로 대체되거나 보완될 수 있습니다.
# 혼란을 피하기 위해, 더 구체적인 위 규칙들을 사용하고, com.firebase.** 는 삭제하거나 주석 처리하는 것을 고려할 수 있습니다.
# 여기서는 일단 추가하는 방향으로 제안합니다.
-keep class com.firebase.** { *; } # 기존 규칙 유지

# 시스템 클래스인 tagsoup 관련 경고를 억제하고 클래스를 유지 (다른 라이브러리가 포함할 경우 대비)
# 이 규칙은 시스템 클래스 자체를 앱 패키지에 포함시키는 것이 아니라,
# 만약 다른 라이브러리가 이 클래스를 포함하고 R8이 이를 처리하려고 할 때를 대비합니다.
-keep class org.ccil.cowan.tagsoup.** { *; }
-dontwarn org.ccil.cowan.tagsoup.**

# 기존 기타 Firebase 관련 규칙들 (필요시 유지 또는 검토)
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**
-keep class com.shaded.fasterxml.jackson.** { *; }

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
#-keep public class * extends com.bumptech.glide.AppGlideModule { *; }
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
#-keep class org.wordpress.aztec.** { *; }
#-dontwarn org.wordpress.aztec.**

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
# Data classes for Deserialization
########################################################
-keep class com.agvber.kekkek.core.firestore.model.** { *; }

-keepclassmembers class ** {
    @com.google.firebase.* <fields>;
}
-keep @com.google.firebase.firestore.PropertyName class ** { *; }


########################################################
# 안전 장치
########################################################
# 리플렉션 사용 클래스명 유지
-keepattributes Signature,InnerClasses,EnclosingMethod,RuntimeVisibleAnnotations,AnnotationDefault,Deprecated,SourceFile,LineNumberTable,*Annotation*,kotlin.Metadata

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder

-keepclassmembers class * implements androidx.viewbinding.ViewBinding { *; }