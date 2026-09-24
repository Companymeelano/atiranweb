# MEELANO Android Native

این پروژه نسخه **اندروید واقعی و قابل نصب** برای MEELANO است. این نسخه PWA نیست و از WebView/مرورگر داخلی استفاده نمی‌کند؛ تمام صفحه‌ها، کارت‌ها، لیست‌ها، فرم ورود و نمودارها با کامپوننت‌های Native اندروید در Java ساخته شده‌اند.

## ویژگی‌های نسخه 2.0.0-native

- اپلیکیشن نصب‌شدنی Android با UI کاملاً Native
- بدون PWA، بدون HTML و بدون WebView
- ورود مستقیم به API امن MEELANO با نام کاربری و رمز Atiran
- ذخیره Session امن سرور از طریق Cookie؛ بدون ذخیره رمز عبور
- داشبورد Native با KPI و نمودار Line/Bar اختصاصی
- مشتریان با جستجو و Customer 360 Native
- کالا و انبار با کارت‌های Product Intelligence
- نمایش Native برای فروش و چک‌ها از endpoint جدول‌های مجاز
- گزارش‌های مدیریتی و Executive Analytics با نمودارهای Native
- صفحه تنظیمات، تغییر آدرس API و خروج امن
- تم تیره/طلایی هماهنگ با برند MEELANO و پشتیبانی RTL فارسی
- پشتیبانی از HTTPS و همچنین HTTP برای شبکه داخلی

## معماری اتصال

اپ اندروید به SQL Server وصل نمی‌شود و هیچ credential دیتابیس داخل APK نیست.

جریان اتصال:

```text
MEELANO Android Native → HTTPS/HTTP API → SQL Server Atiran
```

API همان endpointهای امن MEELANO را ارائه می‌کند:

- `POST /api/login`
- `GET /api/session`
- `GET /api/dashboard`
- `GET /api/customers`
- `GET /api/products`
- `GET /api/analytics`
- `GET /api/table/{table}`
- `POST /api/logout`

## آدرس سرویس

در اولین اجرا آدرس API/سرور MEELANO را وارد کنید، مانند:

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

خروجی Release:

```text
app/build/outputs/apk/release/app-release.apk
```

نام فایل تحویلی این نسخه:

```text
MEELANO-Android-Native-v2.0.0.apk
```

> برای انتشار رسمی در Play Store یا تحویل سازمانی بلندمدت، signingConfig اختصاصی و امن خودتان را جایگزین signing debug کنید.
