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


-keep class com.hannibal.scalpel.Hannibal {*;}
-keep class com.hannibal.scalpel.task.PickOutTask {*;}
-keep class com.hannibal.scalpel.bean.** {*;}


-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-keepattributes SourceFile,LineNumberTable,Exceptions # 不混淆日志的line号
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!code/allocation/variable  # 混淆时所采用的算法
#过滤注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
#过滤泛型
-keepattributes Signature
# 忽略警告
-ignorewarnings
#保证是独立的jar,没有任何项目引用,如果不写就会认为我们所有的代码是无用的,从而把所有的代码压缩掉,导出一个空的jar
-dontshrink
#表示不跳过library中的非public的类
#-dontskipnonpubliclibraryclasses


-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.support.multidex.MultiDexApplication  # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class * extends android.view.View     # 保持哪些类不被混淆
-keep public class * extends android.view.MotionEvent
-keep public class * extends com.hannibal.scalpel.hook.MotionEventMethodHook

-keep class android.support.** {*;} ## 保留android.support下的所有类及其内部类
-keep class androidx.** {*;}   ## 保留androidx下的所有类及其内部类
-keep class com.google.android.material.** {*;}

-keep public class com.android.vending.licensing.ILicensingService
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}


#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}


# 阿里云日志  greenDAO
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class org.greenrobot.greendao.** { *; }
-dontwarn org.greenrobot.greendao.database.**
-dontwarn org.greenrobot.greendao.rx.**
-dontwarn org.greenrobot.greendao.**

# fastJson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

# epic
-keep class android.taobao.** { *; }
-keep class com.taobao.** { *; }
-keep class me.weishu.epic.art.** { *; }

# delete log in release mode.
#-assumenosideeffects class com.taobao.android.dexposed.utility.Logger {
#          public static void i(...);
#          public static void w(...);
#          public static void d(...);
#          public static void e(...);
#}
#
#-assumenosideeffects class com.taobao.android.dexposed.utility.Debug {
#          public static *** hexdump(...);
#}
