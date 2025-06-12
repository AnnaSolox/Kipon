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

# Ktor rules
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

## ktor http client
-keep class io.ktor.client.engine.cio.**

## ktor plugins
-keep class io.ktor.client.plugins.contentnegotiation.** { *; }
-keep class io.ktor.client.plugins.logging.** { *; }

# Serialization rules
-keepattributes *Annotation*
-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * { *; }
-keep,includedescriptorclasses class **$$serializer { *; }
-keepclassmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}

# BuildConfig
-keep class **.BuildConfig { *; }

# Koin
-keep class org.koin.** { *; }

# Eliminar logs
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

-assumenosideeffects class java.io.PrintStream {
    public *** println(...);
    public *** print(...);
}

# Jetpack Navigation
-keep class androidx.navigation.NavHostController { *; }
-keep class androidx.navigation.compose.** { *; }
-keepclassmembers class * {
    @androidx.navigation.* <methods>;
}
-keep class * extends androidx.lifecycle.ViewModel

-keepclassmembers class * {
    @androidx.navigation.* <methods>;
}

# Compose runtime (mantiene funciones composables necesarias)
-keep class androidx.compose.runtime.** { *; }

# Mantener NavBackStackEntry internamente
-keep class androidx.navigation.NavBackStackEntry { *; }

# Mantener composables por anotaciones
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

-keepnames class com.annasolox.kipon.core.navigation.**