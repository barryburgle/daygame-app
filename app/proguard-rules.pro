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

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keepattributes Exceptions
-keepattributes Signature

# OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Gson
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Model classes
-keepclassmembers class ** {
    @retrofit2.http.* <methods>;
}

# Additional rules for coroutines
-keepclassmembers class kotlinx.coroutines.** {
    <fields>;
    <methods>;
}
-dontwarn kotlinx.coroutines.**

# Keep MainActivity
-keep class com.barryburgle.gameapp.MainActivity {
    <fields>;
    <methods>;
}

# Keep Github Response
-keep class com.barryburgle.gameapp.api.response.** { *; }

# Keep Model
-keep class com.barryburgle.gameapp.model.** { *; }