# MEELANO Android Direct SQL

این پروژه نسخه **اندروید واقعی و قابل نصب** برای MEELANO است که برای تست شخصی با **اتصال مستقیم به SQL Server** آماده شده است.

## ویژگی‌های نسخه 3.2.0-direct-sql-datafix

- اپلیکیشن نصب‌شدنی Android با UI کاملاً Native
- بدون PWA، بدون HTML و بدون مرورگر داخلی
- اتصال مستقیم به SQL Server از داخل اپلیکیشن با JDBC
- عدم نمایش تنظیمات فنی اتصال در رابط کاربری
- ورود کاربر فقط با نام کاربری و رمز Atiran
- اعتبارسنجی روی جداول واقعی `visitors` و `sys_users`
- داشبورد Native با KPI و نمودار Line/Bar اختصاصی
- مشتریان با جستجو و Customer 360 Native
- کالا و انبار با کارت‌های Native
- نمایش Native برای فروش و چک‌ها از جدول‌های مجاز
- گزارش‌های مدیریتی پایه با نمودارهای Native
- پیام «عدم اتصال» و امکان تلاش مجدد در صورت قطع ارتباط
- تم تیره/طلایی هماهنگ با برند MEELANO و پشتیبانی RTL فارسی

## نکته امنیتی

این نسخه بنا به درخواست برای تست شخصی Direct SQL ساخته شده است. برای انتشار عمومی یا استفاده سازمانی، معماری امن‌تر زیر توصیه می‌شود:

```text
Android Native App → HTTPS API → SQL Server
```

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
MEELANO-Android-DirectSQL-v3.2.0.apk
```
