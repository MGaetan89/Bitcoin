-dontwarn kotlin.**
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }

-keepclassmembers class **$WhenMappings {
    <fields>;
}

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

-keep class io.bitcoin.model.** { *; }

-keepclassmembers class io.bitcoin.model.** {
   <fields>;
}
