# MEELANO Android

نسخه Android قابل نصب برای MEELANO با پوسته WebView لوکس، RTL فارسی و اتصال امن به نسخه PWA/API.

## ویژگی‌های نسخه 1.1.0

- پوسته Android تیره و طلایی هماهنگ با برند MEELANO
- صفحه شروع بسیار زیبا برای ثبت آدرس سرویس
- نوار بالایی اختصاصی با لوگو، وضعیت اتصال، Home، Back، Refresh و Settings
- WebView بهینه‌شده برای PWA، JavaScript، Cookie، DOM Storage و دانلود خروجی‌ها
- صفحه خطای اختصاصی با دکمه «تلاش دوباره» و «تغییر آدرس اتصال»
- پشتیبانی از HTTPS و همچنین HTTP برای شبکه داخلی
- هیچ رمز SQL یا اطلاعات حساس داخل APK ذخیره نمی‌شود؛ فقط آدرس سرویس ذخیره می‌شود

## آدرس سرویس

در اولین اجرا آدرس سایت/API MEELANO را وارد کنید، مانند:

- `https://meelano.example.com`
- `http://192.168.1.150:5000`

برای اینترنت عمومی از HTTPS استفاده کنید.

## ساخت APK در Android Studio

1. Android Studio را نصب کنید.
2. پروژه `MEELANO-Android` را باز کنید.
3. SDK Platform 35 و Build Tools متناظر را نصب کنید.
4. Gradle Sync را انجام دهید.
5. برای فایل قابل نصب مستقیم:
   - `Build > Build Bundle(s) / APK(s) > Build APK(s)`
   - یا از ترمینال: `gradle :app:assembleRelease`

> در این پروژه، Release برای تحویل مستقیم با signing debug امضا می‌شود تا APK قابل نصب باشد. برای انتشار رسمی در Play Store یا تحویل نهایی بلندمدت، حتماً signingConfig اختصاصی و امن خودتان را جایگزین کنید.

## خروجی پیشنهادی

پس از Build، فایل قابل نصب در مسیر زیر ساخته می‌شود:

```text
app/build/outputs/apk/release/app-release.apk
```

نام پیشنهادی برای تحویل به مشتری:

```text
MEELANO-Android-v1.1.0.apk
```
