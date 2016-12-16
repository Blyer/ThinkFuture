# BasePlatform
基础框架，打出的包未进行混淆。若使用者需要使用混淆，添加如下配置至应用混淆配置文件中：
################### region for xUtils
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
#################### end region for xUtils

-keep class org.base.platform.bean.**{*;}
-keep class org.base.platform.callback.**{*;}
