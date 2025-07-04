# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keep class io.mesalabs.knoxpatch.MainHook

# Caused by KavaRef dependency (com.highcapable.kavaref:kavaref-core)
-dontwarn java.lang.reflect.AnnotatedType

-repackageclasses
-allowaccessmodification
-overloadaggressively