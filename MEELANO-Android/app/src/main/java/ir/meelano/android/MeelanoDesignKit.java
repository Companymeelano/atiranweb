package ir.meelano.android;

/**
 * Central visual language tokens for the native Meelano Android app.
 * Semantic labels, vector icon resources and compatibility glyph fallbacks live
 * here so all Java/View screens share one design vocabulary.
 */
final class MeelanoDesignKit {
    private MeelanoDesignKit() {}

    static int iconRes(String key) {
        if ("dashboard".equals(key) || "visitor_dashboard".equals(key)) return R.drawable.icon_dashboard;
        if ("customers".equals(key)) return R.drawable.icon_customers;
        if ("products".equals(key)) return R.drawable.icon_products;
        if ("showcase".equals(key)) return R.drawable.icon_showcase;
        if ("reports".equals(key) || "visitor_reports".equals(key)) return R.drawable.icon_reports;
        if ("command".equals(key)) return R.drawable.icon_command;
        if ("assistant".equals(key)) return R.drawable.icon_assistant;
        if ("chat".equals(key)) return R.drawable.icon_chat;
        if ("personnel".equals(key)) return R.drawable.icon_personnel;
        if ("attendance".equals(key)) return R.drawable.icon_attendance;
        if ("taxpayers".equals(key)) return R.drawable.icon_taxpayers;
        if ("cameras".equals(key)) return R.drawable.icon_cameras;
        if ("alarm".equals(key)) return R.drawable.icon_alarm;
        if ("cart".equals(key)) return R.drawable.icon_cart;
        if ("settings".equals(key)) return R.drawable.icon_settings;
        if ("management".equals(key) || "more".equals(key)) return R.drawable.icon_management;
        if ("health".equals(key)) return R.drawable.icon_health;
        if ("barcode".equals(key)) return R.drawable.icon_barcode;
        return R.drawable.icon_dashboard;
    }

    static String glyph(String key) {
        if ("dashboard".equals(key)) return "⌂";
        if ("customers".equals(key)) return "♙";
        if ("products".equals(key)) return "◍";
        if ("reports".equals(key) || "visitor_reports".equals(key)) return "↗";
        if ("command".equals(key)) return "⌘";
        if ("assistant".equals(key)) return "✦";
        if ("chat".equals(key)) return "✉";
        if ("personnel".equals(key)) return "ID";
        if ("attendance".equals(key)) return "⏱";
        if ("taxpayers".equals(key)) return "٪";
        if ("cameras".equals(key)) return "▣";
        if ("alarm".equals(key)) return "◬";
        if ("visitor_dashboard".equals(key)) return "◎";
        if ("showcase".equals(key)) return "◈";
        if ("cart".equals(key)) return "⊕";
        if ("settings".equals(key)) return "⚙";
        if ("management".equals(key)) return "♛";
        if ("health".equals(key)) return "◌";
        return "◆";
    }

    static String label(String key) {
        if ("dashboard".equals(key)) return "داشبورد";
        if ("customers".equals(key)) return "مشتریان";
        if ("products".equals(key)) return "کالاها";
        if ("reports".equals(key) || "visitor_reports".equals(key)) return "گزارشات";
        if ("command".equals(key)) return "فرماندهی";
        if ("assistant".equals(key)) return "دستیار";
        if ("chat".equals(key)) return "گفتگو";
        if ("personnel".equals(key)) return "پرسنل";
        if ("attendance".equals(key)) return "حضور";
        if ("taxpayers".equals(key)) return "مودیان";
        if ("cameras".equals(key)) return "دوربین";
        if ("alarm".equals(key)) return "دزدگیر";
        if ("visitor_dashboard".equals(key)) return "ماموریت";
        if ("showcase".equals(key)) return "کالا";
        if ("cart".equals(key)) return "سبد";
        if ("settings".equals(key)) return "تنظیمات";
        if ("management".equals(key)) return "مدیریت";
        if ("health".equals(key)) return "اتصال";
        return key == null || key.trim().isEmpty() ? "بخش" : key;
    }

    static String beautyHint(String key) {
        if ("dashboard".equals(key)) return "نمای مدیریتی glass با KPIهای سریع";
        if ("customers".equals(key)) return "کارت مشتری با ریسک، تماس و اولویت وصول";
        if ("products".equals(key)) return "کارت کالا با تصویر، قیمت و نمودار ریزگردش";
        if ("showcase".equals(key)) return "کالای مشتری‌محور، سریع و آماده ارائه";
        if ("cart".equals(key)) return "سبد ساده پیش‌فاکتور برای ویزیتور";
        if ("visitor_dashboard".equals(key)) return "ماموریت، مسیر، کالا و پیش‌فاکتور بدون بخش اضافه";
        if ("personnel".equals(key)) return "پرونده پرسنلی با خلاصه مالی و حضور";
        if ("reports".equals(key) || "visitor_reports".equals(key)) return "گزارشات طلایی ویزیتور، فاکتورهای من و صف آفلاین";
        if ("taxpayers".equals(key)) return "ارسال سازمانی با وضعیت روشن";
        if ("cameras".equals(key) || "alarm".equals(key)) return "کنترل سخت‌افزار با کارت وضعیت";
        if ("settings".equals(key)) return "آزمایشگاه تم، حرکت و امنیت محلی";
        return "زبان بصری یکپارچه Meelano";
    }
}
