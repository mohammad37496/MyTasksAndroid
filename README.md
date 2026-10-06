# MyTasksAndroid

اولین اپ آزمایشی ما: یک Task Manager فارسی و ساده با Kotlin + Jetpack Compose.

## قابلیت‌های نسخه 1

- رابط کاملاً فارسی و RTL
- نمایش فهرست کارها
- افزودن کار جدید
- علامت‌گذاری کار به‌عنوان انجام‌شده
- حذف کار
- نمایش درصد پیشرفت روزانه
- رابط کاربری مناسب برای تست سریع با Live Edit

## مشخصات فنی

- Kotlin 2.4.20
- Android Gradle Plugin 9.3.0
- Jetpack Compose
- Compose BOM 2026.09.00
- compileSdk 37
- minSdk 24
- Java 17

## اجرا در Android Studio

1. Android Studio به‌روز را نصب کنید.
2. این Repository را Clone کنید.
3. پروژه را به‌صورت Gradle Project باز کنید.
4. Gradle Sync را انجام دهید.
5. گوشی را با USB Debugging یا Wireless Debugging وصل کنید.
6. Run را بزنید.

در مرحله بعد، ساختار پروژه را برای توسعه سریع‌تر و تست زنده روی گوشی مرتب‌تر می‌کنیم و سپس ذخیره دائمی اطلاعات، ویرایش کار، جستجو و قابلیت‌های بیشتر را اضافه می‌کنیم.


## همگام‌سازی خودکار ویندوز

فایل `tools/auto-sync.ps1` می‌تواند Repository محلی را هر ۳۰ ثانیه با `main` در GitHub مقایسه کند و فقط وقتی تغییر محلی وجود ندارد، `git pull --ff-only` انجام دهد.

برای اجرا در ویندوز، فایل `tools/Start-AutoSync.cmd` را اجرا کنید. بهتر است این پنجره در پس‌زمینه باز بماند.
