# Preserve line numbers for readable crash reports, without leaking source file names.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- Kotlinx Serialization ----------------------------------------------------
# Keep @Serializable classes, their companion $serializer, and generated metadata.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keep,includedescriptorclasses class com.faridev.gameradar.**$$serializer { *; }
-keepclassmembers class com.faridev.gameradar.** {
    *** Companion;
}
-keepclasseswithmembers class com.faridev.gameradar.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# --- Ktor ---------------------------------------------------------------------
-dontwarn io.ktor.**
-keep class io.ktor.** { *; }

# --- Kotlin coroutines --------------------------------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# --- Compose / reflection used by Koin ----------------------------------------
-keep class org.koin.** { *; }
-dontwarn org.koin.**
