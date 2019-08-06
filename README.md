# NTP Client

## 0、原理
`Sntp`是参照`android.util.NtpTrustedTime`、`android.net.SntpClient`重构的可__自定义NTP服务器地址__的工具类，其中`SntpClient`实现NTP时间同步。

## 1、依赖
```Gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url 'https://dl.bintray.com/ztone/maven'
        }
    }
}

dependencies {
    implementation 'com.xrj:lib-ntp:0.1'

    implementation 'android.ztone:ztone-lang:0.3.25'
    implementation 'com.squareup.okio:okio:1.14.0'
}
```

## 2、初始化
```Java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Sntp.Impl.load(getApplicationContext());
    }
}
```

## 3、NTP时间同步和设置
- 设置自定义NTP服务器

```Java
Sntp.Impl.setNtpAddress(ntpAddress)
```

- 时间同步和设置系统时间

```Java
    boolean ntpResult = Sntp.Impl.forceRefresh();
    if (ntpResult) {
        mCmdResult = Sntp.Impl.setTimeMillis();
    }
```
> - `forceRefresh()`时会执行网络操作，故必须在 `_子线_` 程中执行
> - Lib中是通过 `date -s yyyyMMdd.HHmmss`的方式设置已_ROOT_的系统；