package ir.meelano.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final String PREFS = "meelano_android_native";
    private static final String KEY_BASE_URL = "api_base_url";
    private static final String KEY_COOKIE = "session_cookie";
    private static final String KEY_USER = "user_name";

    private static final int NAVY = Color.rgb(7, 9, 16);
    private static final int NAVY_2 = Color.rgb(12, 15, 24);
    private static final int SURFACE = Color.rgb(18, 22, 31);
    private static final int SURFACE_2 = Color.rgb(24, 30, 42);
    private static final int SURFACE_3 = Color.rgb(31, 39, 54);
    private static final int GOLD = Color.rgb(231, 177, 90);
    private static final int GOLD_2 = Color.rgb(242, 207, 138);
    private static final int SUCCESS = Color.rgb(72, 199, 163);
    private static final int INFO = Color.rgb(102, 170, 245);
    private static final int WARNING = Color.rgb(244, 181, 95);
    private static final int DANGER = Color.rgb(241, 106, 117);
    private static final int TEXT = Color.rgb(246, 248, 252);
    private static final int MUTED = Color.rgb(154, 166, 183);
    private static final int BORDER = Color.argb(42, 255, 255, 255);

    private SharedPreferences prefs;
    private FrameLayout stage;
    private TextView status;
    private TextView serviceLine;
    private LinearLayout content;
    private LinearLayout navStrip;
    private String activePage = "dashboard";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        buildFrame();
        boot();
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int alpha(int color, int amount) {
        return Color.argb(amount, Color.red(color), Color.green(color), Color.blue(color));
    }

    private GradientDrawable rounded(int color, float radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private GradientDrawable roundedStroke(int color, float radius, int strokeColor) {
        GradientDrawable d = rounded(color, radius);
        d.setStroke(dp(1), strokeColor);
        return d;
    }

    private GradientDrawable gradient(int[] colors, GradientDrawable.Orientation orientation, float radius) {
        GradientDrawable d = new GradientDrawable(orientation, colors);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private TextView text(String value, float size, int color, int style) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setTypeface(Typeface.DEFAULT, style);
        t.setIncludeFontPadding(true);
        t.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            t.setTextDirection(View.TEXT_DIRECTION_RTL);
        }
        return t;
    }

    private void buildFrame() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(NAVY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(10), dp(7), dp(10), dp(7));
        header.setBackground(gradient(new int[]{Color.rgb(9, 12, 20), Color.rgb(22, 26, 38)}, GradientDrawable.Orientation.LEFT_RIGHT, 0));

        ImageView logo = new ImageView(this);
        logo.setImageResource(ir.meelano.android.R.drawable.meelano_3d);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        logo.setPadding(dp(3), dp(3), dp(3), dp(3));
        logo.setBackground(roundedStroke(alpha(GOLD, 18), 16, alpha(GOLD, 72)));
        header.addView(logo, new LinearLayout.LayoutParams(dp(48), dp(48)));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(10), 0, dp(10), 0);
        TextView appTitle = text("MEELANO Native", 16, TEXT, Typeface.BOLD);
        status = text("اپلیکیشن اندروید واقعی، بدون PWA و بدون مرورگر داخلی", 10.5f, MUTED, Typeface.NORMAL);
        serviceLine = text("", 9.5f, alpha(TEXT, 155), Typeface.NORMAL);
        titles.addView(appTitle, new LinearLayout.LayoutParams(-1, 0, 1f));
        titles.addView(status, new LinearLayout.LayoutParams(-1, 0, 1f));
        titles.addView(serviceLine, new LinearLayout.LayoutParams(-1, 0, 1f));
        header.addView(titles, new LinearLayout.LayoutParams(0, dp(50), 1f));

        TextView refresh = iconButton("↻", "بازخوانی");
        refresh.setOnClickListener(v -> refreshActivePage());
        header.addView(refresh, new LinearLayout.LayoutParams(dp(43), dp(43)));

        TextView settings = iconButton("⚙", "تنظیمات");
        settings.setOnClickListener(v -> showApp("settings"));
        LinearLayout.LayoutParams settingLp = new LinearLayout.LayoutParams(dp(43), dp(43));
        settingLp.setMargins(dp(6), 0, 0, 0);
        header.addView(settings, settingLp);

        root.addView(header, new LinearLayout.LayoutParams(-1, dp(66)));

        stage = new FrameLayout(this);
        stage.setBackgroundColor(NAVY);
        root.addView(stage, new LinearLayout.LayoutParams(-1, 0, 1f));
        setContentView(root);
    }

    private TextView iconButton(String glyph, String description) {
        TextView b = new TextView(this);
        b.setText(glyph);
        b.setTextSize(21);
        b.setGravity(Gravity.CENTER);
        b.setTextColor(GOLD_2);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(roundedStroke(alpha(Color.WHITE, 10), 14, alpha(GOLD, 48)));
        b.setContentDescription(description);
        b.setClickable(true);
        b.setFocusable(true);
        return b;
    }

    private Button primaryButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextColor(Color.rgb(20, 16, 10));
        b.setTextSize(13.5f);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(gradient(new int[]{GOLD_2, GOLD}, GradientDrawable.Orientation.LEFT_RIGHT, 17));
        return b;
    }

    private Button secondaryButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextColor(TEXT);
        b.setTextSize(13f);
        b.setBackground(roundedStroke(SURFACE_2, 16, alpha(GOLD, 54)));
        return b;
    }

    private EditText input(String hint, String value, boolean password, boolean ltr) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setHint(hint);
        e.setText(value == null ? "" : value);
        e.setHintTextColor(Color.rgb(112, 122, 138));
        e.setTextColor(TEXT);
        e.setTextSize(14);
        e.setSelectAllOnFocus(true);
        e.setPadding(dp(14), 0, dp(14), 0);
        e.setBackground(roundedStroke(SURFACE_2, 16, alpha(Color.WHITE, 40)));
        e.setInputType(password ? (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) : InputType.TYPE_CLASS_TEXT);
        if (ltr && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            e.setTextDirection(View.TEXT_DIRECTION_LTR);
            e.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        } else {
            e.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
        return e;
    }

    private LinearLayout card() {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(15), dp(15), dp(15), dp(15));
        c.setBackground(roundedStroke(SURFACE, 22, BORDER));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) c.setElevation(dp(6));
        return c;
    }

    private void boot() {
        String base = prefs.getString(KEY_BASE_URL, "");
        String cookie = prefs.getString(KEY_COOKIE, "");
        if (!base.trim().isEmpty() && !cookie.trim().isEmpty()) {
            showFullLoading("در حال بررسی جلسه کاربری…");
            callApi("GET", "/api/session", null, new ApiCallback() {
                @Override public void onSuccess(String body) {
                    try {
                        JSONObject j = new JSONObject(body);
                        if (j.optBoolean("authenticated")) {
                            prefs.edit().putString(KEY_USER, j.optString("userName", prefs.getString(KEY_USER, "کاربر Atiran"))).apply();
                            showApp("dashboard");
                        } else {
                            prefs.edit().remove(KEY_COOKIE).apply();
                            showLogin("برای ورود به نسخه اندروید، اطلاعات Atiran را وارد کنید.");
                        }
                    } catch (Exception e) {
                        showLogin("جلسه قبلی معتبر نبود. دوباره وارد شوید.");
                    }
                }
                @Override public void onError(Exception e) {
                    showLogin("اتصال به سرویس برقرار نشد؛ آدرس و اینترنت را بررسی کنید.");
                }
            });
        } else {
            showLogin("نسخه اندروید واقعی MEELANO آماده است؛ برای شروع آدرس API و حساب Atiran را وارد کنید.");
        }
    }

    private void showFullLoading(String message) {
        stage.removeAllViews();
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(dp(24), dp(24), dp(24), dp(24));
        ProgressBar p = new ProgressBar(this);
        box.addView(p, new LinearLayout.LayoutParams(dp(58), dp(58)));
        TextView m = text(message, 13, MUTED, Typeface.NORMAL);
        m.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2);
        mp.setMargins(0, dp(18), 0, 0);
        box.addView(m, mp);
        stage.addView(box, new FrameLayout.LayoutParams(-1, -1));
    }

    private void showLogin(String message) {
        activePage = "login";
        serviceLine.setText("ورود مستقیم به API");
        status.setText("اپلیکیشن اندروید واقعی، بدون PWA و بدون مرورگر داخلی");
        stage.removeAllViews();

        FrameLayout backdrop = new FrameLayout(this);
        backdrop.setBackground(gradient(new int[]{NAVY, Color.rgb(14, 18, 29), NAVY}, GradientDrawable.Orientation.TOP_BOTTOM, 0));

        View glow1 = new View(this);
        GradientDrawable g1 = new GradientDrawable();
        g1.setShape(GradientDrawable.OVAL);
        g1.setColor(alpha(GOLD, 28));
        glow1.setBackground(g1);
        FrameLayout.LayoutParams g1p = new FrameLayout.LayoutParams(dp(250), dp(250), Gravity.TOP | Gravity.RIGHT);
        g1p.setMargins(0, dp(-90), dp(-90), 0);
        backdrop.addView(glow1, g1p);

        View glow2 = new View(this);
        GradientDrawable g2 = new GradientDrawable();
        g2.setShape(GradientDrawable.OVAL);
        g2.setColor(Color.argb(24, 93, 196, 255));
        glow2.setBackground(g2);
        FrameLayout.LayoutParams g2p = new FrameLayout.LayoutParams(dp(270), dp(270), Gravity.BOTTOM | Gravity.LEFT);
        g2p.setMargins(dp(-100), 0, 0, dp(-90));
        backdrop.addView(glow2, g2p);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        outer.setGravity(Gravity.CENTER);
        outer.setPadding(dp(18), dp(20), dp(18), dp(24));
        scroll.addView(outer, new ScrollView.LayoutParams(-1, -1));

        LinearLayout loginCard = card();
        loginCard.setGravity(Gravity.CENTER_HORIZONTAL);
        loginCard.setPadding(dp(22), dp(24), dp(22), dp(22));
        loginCard.setBackground(roundedStroke(alpha(SURFACE, 242), 30, alpha(GOLD, 62)));
        outer.addView(loginCard, new LinearLayout.LayoutParams(-1, -2));

        ImageView logo = new ImageView(this);
        logo.setImageResource(ir.meelano.android.R.drawable.meelano_3d);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        loginCard.addView(logo, new LinearLayout.LayoutParams(dp(124), dp(124)));

        TextView h = text("MEELANO Android Native", 23, TEXT, Typeface.BOLD);
        h.setGravity(Gravity.CENTER);
        loginCard.addView(h, new LinearLayout.LayoutParams(-1, -2));

        TextView sub = text("بدون PWA، بدون مرورگر داخلی؛ رابط کاملاً Native و اتصال مستقیم به API امن MEELANO", 12.5f, MUTED, Typeface.NORMAL);
        sub.setGravity(Gravity.CENTER);
        sub.setLineSpacing(dp(2), 1.05f);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.setMargins(0, dp(7), 0, dp(14));
        loginCard.addView(sub, sp);

        if (message != null && !message.trim().isEmpty()) {
            TextView msg = text(message, 12, alpha(TEXT, 215), Typeface.NORMAL);
            msg.setGravity(Gravity.CENTER);
            msg.setPadding(dp(12), dp(10), dp(12), dp(10));
            msg.setBackground(roundedStroke(alpha(GOLD, 16), 16, alpha(GOLD, 46)));
            LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2);
            mp.setMargins(0, 0, 0, dp(14));
            loginCard.addView(msg, mp);
        }

        TextView serviceLabel = text("آدرس API / سرور MEELANO", 12, MUTED, Typeface.BOLD);
        loginCard.addView(serviceLabel, new LinearLayout.LayoutParams(-1, -2));
        EditText baseUrl = input("https://meelano.example.com", prefs.getString(KEY_BASE_URL, ""), false, true);
        baseUrl.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1, dp(54));
        ip.setMargins(0, dp(6), 0, dp(12));
        loginCard.addView(baseUrl, ip);

        TextView userLabel = text("نام کاربری Atiran", 12, MUTED, Typeface.BOLD);
        loginCard.addView(userLabel, new LinearLayout.LayoutParams(-1, -2));
        EditText username = input("username", "", false, true);
        LinearLayout.LayoutParams up = new LinearLayout.LayoutParams(-1, dp(54));
        up.setMargins(0, dp(6), 0, dp(12));
        loginCard.addView(username, up);

        TextView passLabel = text("رمز عبور Atiran", 12, MUTED, Typeface.BOLD);
        loginCard.addView(passLabel, new LinearLayout.LayoutParams(-1, -2));
        EditText password = input("password", "", true, true);
        password.setImeOptions(EditorInfo.IME_ACTION_DONE);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(54));
        pp.setMargins(0, dp(6), 0, dp(16));
        loginCard.addView(password, pp);

        Button login = primaryButton("ورود به اپلیکیشن اندروید");
        loginCard.addView(login, new LinearLayout.LayoutParams(-1, dp(54)));

        TextView safe = text("اطلاعات دیتابیس داخل APK قرار ندارد. اپ فقط با API منتشرشده شما صحبت می‌کند و Session امن سرور را نگه می‌دارد.", 10.5f, MUTED, Typeface.NORMAL);
        safe.setGravity(Gravity.CENTER);
        safe.setLineSpacing(dp(2), 1.05f);
        LinearLayout.LayoutParams safeLp = new LinearLayout.LayoutParams(-1, -2);
        safeLp.setMargins(0, dp(14), 0, 0);
        loginCard.addView(safe, safeLp);

        View.OnClickListener doLogin = v -> {
            String url = normalizeBase(baseUrl.getText().toString());
            String u = username.getText().toString().trim();
            String p = password.getText().toString();
            if (url.length() < 10) { baseUrl.setError("آدرس معتبر وارد کنید"); return; }
            if (u.isEmpty()) { username.setError("نام کاربری الزامی است"); return; }
            if (p.isEmpty()) { password.setError("رمز عبور الزامی است"); return; }
            prefs.edit().putString(KEY_BASE_URL, url).remove(KEY_COOKIE).apply();
            login.setEnabled(false);
            login.setText("در حال ورود…");
            JSONObject payload = new JSONObject();
            try {
                payload.put("username", u);
                payload.put("password", p);
            } catch (Exception ignored) { }
            callApi("POST", "/api/login", payload, new ApiCallback() {
                @Override public void onSuccess(String body) {
                    login.setEnabled(true);
                    login.setText("ورود به اپلیکیشن اندروید");
                    try {
                        JSONObject j = new JSONObject(body);
                        prefs.edit().putString(KEY_USER, j.optString("userName", u)).apply();
                    } catch (Exception ignored) {
                        prefs.edit().putString(KEY_USER, u).apply();
                    }
                    Toast.makeText(MainActivity.this, "ورود موفق بود", Toast.LENGTH_SHORT).show();
                    showApp("dashboard");
                }
                @Override public void onError(Exception e) {
                    login.setEnabled(true);
                    login.setText("ورود به اپلیکیشن اندروید");
                    Toast.makeText(MainActivity.this, readableError(e), Toast.LENGTH_LONG).show();
                }
            });
        };
        login.setOnClickListener(doLogin);
        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doLogin.onClick(login);
                return true;
            }
            return false;
        });

        backdrop.addView(scroll, new FrameLayout.LayoutParams(-1, -1));
        stage.addView(backdrop, new FrameLayout.LayoutParams(-1, -1));
    }

    private void showApp(String page) {
        activePage = page;
        serviceLine.setText(shortHost(prefs.getString(KEY_BASE_URL, "")));
        status.setText("اتصال Native به API");
        stage.removeAllViews();

        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setBackgroundColor(NAVY);

        HorizontalScrollView navScroll = new HorizontalScrollView(this);
        navScroll.setHorizontalScrollBarEnabled(false);
        navScroll.setFillViewport(false);
        navStrip = new LinearLayout(this);
        navStrip.setOrientation(LinearLayout.HORIZONTAL);
        navStrip.setGravity(Gravity.CENTER_VERTICAL);
        navStrip.setPadding(dp(10), dp(10), dp(10), dp(6));
        navScroll.addView(navStrip, new HorizontalScrollView.LayoutParams(-2, -1));
        shell.addView(navScroll, new LinearLayout.LayoutParams(-1, dp(62)));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(14), dp(8), dp(14), dp(28));
        scroll.addView(content, new ScrollView.LayoutParams(-1, -2));
        shell.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1f));

        stage.addView(shell, new FrameLayout.LayoutParams(-1, -1));
        buildNav();
        renderActivePage();
    }

    private void buildNav() {
        navStrip.removeAllViews();
        addNav("dashboard", "داشبورد", "◈");
        addNav("customers", "مشتریان", "👥");
        addNav("products", "کالا", "◼");
        addNav("sales", "فروش", "₿");
        addNav("checks", "چک‌ها", "✓");
        addNav("reports", "گزارش", "⌁");
        addNav("settings", "تنظیمات", "⚙");
    }

    private void addNav(String key, String label, String icon) {
        TextView b = text(icon + "  " + label, 12.5f, key.equals(activePage) ? Color.rgb(22, 16, 8) : TEXT, Typeface.BOLD);
        b.setGravity(Gravity.CENTER);
        b.setSingleLine(true);
        b.setPadding(dp(15), 0, dp(15), 0);
        b.setBackground(key.equals(activePage)
                ? gradient(new int[]{GOLD_2, GOLD}, GradientDrawable.Orientation.LEFT_RIGHT, 999)
                : roundedStroke(SURFACE_2, 999, BORDER));
        b.setOnClickListener(v -> showApp(key));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, dp(42));
        lp.setMargins(dp(4), 0, dp(4), 0);
        navStrip.addView(b, lp);
    }

    private void renderActivePage() {
        if (content == null) return;
        switch (activePage) {
            case "customers": loadCustomers(""); break;
            case "products": loadProducts(""); break;
            case "sales": loadTable("فروش و اسناد", "نمای Native از جدول اسناد فروش Atiran", "sailfact"); break;
            case "checks": loadTable("چک‌ها و وصول", "نمای Native از چک‌های دریافتی", "getchk"); break;
            case "reports": loadReports(); break;
            case "settings": renderSettings(); break;
            case "dashboard":
            default: loadDashboard(); break;
        }
    }

    private void refreshActivePage() {
        if ("login".equals(activePage)) {
            boot();
        } else {
            showApp(activePage);
        }
    }

    private void addHero(String title, String subtitle) {
        LinearLayout hero = card();
        hero.setBackground(gradient(new int[]{Color.rgb(26, 32, 45), Color.rgb(15, 19, 28)}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
        TextView h = text(title, 22, TEXT, Typeface.BOLD);
        TextView s = text(subtitle, 12, MUTED, Typeface.NORMAL);
        s.setLineSpacing(dp(2), 1.05f);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img = new ImageView(this);
        img.setImageResource(ir.meelano.android.R.drawable.meelano_3d);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        row.addView(img, new LinearLayout.LayoutParams(dp(58), dp(58)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(10), 0);
        copy.addView(h, new LinearLayout.LayoutParams(-1, -2));
        copy.addView(s, new LinearLayout.LayoutParams(-1, -2));
        row.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        hero.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.setMargins(0, 0, 0, dp(12));
        content.addView(hero, hp);
    }

    private void addLoading(LinearLayout parent, String message) {
        LinearLayout c = card();
        c.setGravity(Gravity.CENTER);
        ProgressBar p = new ProgressBar(this);
        c.addView(p, new LinearLayout.LayoutParams(dp(44), dp(44)));
        TextView t = text(message, 12, MUTED, Typeface.NORMAL);
        t.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.setMargins(0, dp(12), 0, 0);
        c.addView(t, tp);
        parent.addView(c, new LinearLayout.LayoutParams(-1, -2));
    }

    private void showPageError(String title, Exception error, Runnable retry) {
        content.removeAllViews();
        addHero(title, "خطا در دریافت اطلاعات از API");
        LinearLayout c = card();
        TextView h = text("ارتباط برقرار نشد", 17, TEXT, Typeface.BOLD);
        TextView m = text(readableError(error), 12, MUTED, Typeface.NORMAL);
        m.setLineSpacing(dp(3), 1.05f);
        c.addView(h, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2);
        mp.setMargins(0, dp(8), 0, dp(14));
        c.addView(m, mp);
        Button b = primaryButton("تلاش دوباره");
        b.setOnClickListener(v -> retry.run());
        c.addView(b, new LinearLayout.LayoutParams(-1, dp(50)));
        if (error instanceof ApiException && ((ApiException) error).code == 401) {
            prefs.edit().remove(KEY_COOKIE).apply();
            Button login = secondaryButton("ورود دوباره");
            login.setOnClickListener(v -> showLogin("جلسه کاربری منقضی شده است."));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(50));
            lp.setMargins(0, dp(10), 0, 0);
            c.addView(login, lp);
        }
        content.addView(c, new LinearLayout.LayoutParams(-1, -2));
    }

    private void loadDashboard() {
        content.removeAllViews();
        addHero("مرکز فرماندهی", "KPIها و نمودارهای واقعی Atiran در رابط Native اندروید");
        addLoading(content, "در حال دریافت داشبورد…");
        callApi("GET", "/api/dashboard", null, new ApiCallback() {
            @Override public void onSuccess(String body) {
                try {
                    JSONObject j = new JSONObject(body);
                    content.removeAllViews();
                    addHero("مرکز فرماندهی", "KPIها و نمودارهای واقعی Atiran در رابط Native اندروید");
                    addKpis(j.optJSONArray("kpis"));
                    JSONObject a = j.optJSONObject("analytics");
                    if (a != null) {
                        addChartCard("روند فروش هفتگی", "بر پایه فروش واقعی", new LineChartView(MainActivity.this, a.optJSONArray("weeklySales"), GOLD));
                        addChartCard("مشتریان برتر", "TOP مشتریان از API", new BarChartView(MainActivity.this, a.optJSONArray("topCustomers"), SUCCESS));
                        addChartCard("سن مطالبات", "تحلیل بدهی و وصول", new BarChartView(MainActivity.this, a.optJSONArray("debtAging"), WARNING));
                    }
                } catch (Exception e) {
                    showPageError("داشبورد", e, () -> showApp("dashboard"));
                }
            }
            @Override public void onError(Exception e) { showPageError("داشبورد", e, () -> showApp("dashboard")); }
        });
    }

    private void addKpis(JSONArray kpis) {
        if (kpis == null || kpis.length() == 0) {
            addEmpty("KPI قابل نمایش وجود ندارد.");
            return;
        }
        LinearLayout row = null;
        for (int i = 0; i < kpis.length(); i++) {
            if (i % 2 == 0) {
                row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
                rp.setMargins(0, 0, 0, dp(10));
                content.addView(row, rp);
            }
            JSONObject item = kpis.optJSONObject(i);
            LinearLayout c = card();
            c.setBackground(gradient(new int[]{Color.rgb(20, 25, 36), Color.rgb(30, 36, 50)}, GradientDrawable.Orientation.TOP_BOTTOM, 20));
            TextView icon = text(i % 3 == 0 ? "◈" : (i % 3 == 1 ? "◆" : "●"), 24, GOLD_2, Typeface.BOLD);
            icon.setGravity(Gravity.RIGHT);
            TextView title = text(item == null ? "شاخص" : item.optString("title", "شاخص"), 11.5f, MUTED, Typeface.NORMAL);
            TextView value = text(formatNumber(item == null ? 0 : item.opt("value")), 21, TEXT, Typeface.BOLD);
            TextView live = text("داده واقعی API", 10, SUCCESS, Typeface.NORMAL);
            c.addView(icon, new LinearLayout.LayoutParams(-1, -2));
            c.addView(title, new LinearLayout.LayoutParams(-1, -2));
            c.addView(value, new LinearLayout.LayoutParams(-1, -2));
            c.addView(live, new LinearLayout.LayoutParams(-1, -2));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(142), 1f);
            cp.setMargins(dp(4), 0, dp(4), 0);
            if (row != null) row.addView(c, cp);
        }
        if (kpis.length() % 2 == 1 && content.getChildAt(content.getChildCount() - 1) instanceof LinearLayout) {
            LinearLayout last = (LinearLayout) content.getChildAt(content.getChildCount() - 1);
            Space s = new Space(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 1, 1f);
            lp.setMargins(dp(4), 0, dp(4), 0);
            last.addView(s, lp);
        }
    }

    private void loadCustomers(String query) {
        content.removeAllViews();
        addHero("مشتریان", "جستجو، مانده حساب و Customer 360 در UI Native");
        addSearchBox("جستجوی مشتری…", query, q -> loadCustomers(q));
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت مشتریان…");
        callApi("GET", "/api/customers?search=" + enc(query), null, new ApiCallback() {
            @Override public void onSuccess(String body) {
                try {
                    JSONArray rows = new JSONArray(body);
                    list.removeAllViews();
                    if (rows.length() == 0) {
                        addEmptyTo(list, "مشتری مطابق جستجو پیدا نشد.");
                        return;
                    }
                    for (int i = 0; i < Math.min(rows.length(), 70); i++) addCustomerCard(list, rows.optJSONObject(i));
                    if (rows.length() > 70) addEmptyTo(list, "برای سرعت بیشتر فقط ۷۰ رکورد اول نمایش داده شد. جستجو را دقیق‌تر کنید.");
                } catch (Exception e) {
                    showPageError("مشتریان", e, () -> loadCustomers(query));
                }
            }
            @Override public void onError(Exception e) { showPageError("مشتریان", e, () -> loadCustomers(query)); }
        });
    }

    private void addCustomerCard(LinearLayout parent, JSONObject r) {
        if (r == null) return;
        LinearLayout c = card();
        c.setClickable(true);
        c.setOnClickListener(v -> showCustomerDialog(r));
        TextView name = text(r.optString("name", "بدون نام"), 16, TEXT, Typeface.BOLD);
        TextView code = text("کد: " + r.optString("shmo", "-") + "   |   همراه: " + r.optString("phone", "-"), 11.5f, MUTED, Typeface.NORMAL);
        TextView balance = text("مانده: " + money(r.opt("balance")), 13, r.optDouble("balance", 0) > 0 ? WARNING : SUCCESS, Typeface.BOLD);
        TextView address = text(r.optString("address", "-"), 11, alpha(TEXT, 190), Typeface.NORMAL);
        address.setMaxLines(2);
        c.addView(name, new LinearLayout.LayoutParams(-1, -2));
        c.addView(code, new LinearLayout.LayoutParams(-1, -2));
        c.addView(balance, new LinearLayout.LayoutParams(-1, -2));
        c.addView(address, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        parent.addView(c, lp);
    }

    private void showCustomerDialog(JSONObject r) {
        String message = "کد مشتری: " + r.optString("shmo", "-") + "\n"
                + "همراه: " + r.optString("phone", "-") + "\n"
                + "تلفن: " + r.optString("phone2", "-") + "\n"
                + "مانده: " + money(r.opt("balance")) + "\n"
                + "ویزیتور: " + r.optString("visitorId", "-") + "\n\n"
                + "نشانی: " + r.optString("address", "-");
        new AlertDialog.Builder(this)
                .setTitle(r.optString("name", "Customer 360"))
                .setMessage(message)
                .setPositiveButton("بستن", null)
                .show();
    }

    private void loadProducts(String query) {
        content.removeAllViews();
        addHero("کالا و انبار", "Product Intelligence در رابط Native اندروید");
        addSearchBox("جستجوی کالا…", query, q -> loadProducts(q));
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت کالاها…");
        callApi("GET", "/api/products?search=" + enc(query), null, new ApiCallback() {
            @Override public void onSuccess(String body) {
                try {
                    JSONArray rows = new JSONArray(body);
                    list.removeAllViews();
                    if (rows.length() == 0) {
                        addEmptyTo(list, "کالایی مطابق جستجو پیدا نشد.");
                        return;
                    }
                    for (int i = 0; i < Math.min(rows.length(), 70); i++) addProductCard(list, rows.optJSONObject(i));
                    if (rows.length() > 70) addEmptyTo(list, "برای سرعت بیشتر فقط ۷۰ رکورد اول نمایش داده شد. جستجو را دقیق‌تر کنید.");
                } catch (Exception e) {
                    showPageError("کالا", e, () -> loadProducts(query));
                }
            }
            @Override public void onError(Exception e) { showPageError("کالا", e, () -> loadProducts(query)); }
        });
    }

    private void addProductCard(LinearLayout parent, JSONObject r) {
        if (r == null) return;
        LinearLayout c = card();
        TextView name = text(r.optString("name", "بدون نام"), 16, TEXT, Typeface.BOLD);
        TextView code = text("کد: " + firstNonEmpty(r.optString("code", ""), r.optString("shka", "-")), 11.5f, MUTED, Typeface.NORMAL);
        LinearLayout metrics = new LinearLayout(this);
        metrics.setOrientation(LinearLayout.HORIZONTAL);
        metrics.addView(metric("قیمت", money(r.opt("price"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics.addView(metric("موجودی", formatNumber(r.opt("stock"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics.addView(metric("فروش", money(r.opt("salesAmount"))), new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(name, new LinearLayout.LayoutParams(-1, -2));
        c.addView(code, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2);
        mp.setMargins(0, dp(10), 0, 0);
        c.addView(metrics, mp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        parent.addView(c, lp);
    }

    private LinearLayout metric(String label, String value) {
        LinearLayout m = new LinearLayout(this);
        m.setOrientation(LinearLayout.VERTICAL);
        m.setPadding(dp(6), dp(8), dp(6), dp(8));
        m.setBackground(roundedStroke(SURFACE_2, 14, alpha(Color.WHITE, 24)));
        TextView l = text(label, 9.5f, MUTED, Typeface.NORMAL);
        l.setGravity(Gravity.CENTER);
        TextView v = text(value, 11, TEXT, Typeface.BOLD);
        v.setGravity(Gravity.CENTER);
        m.addView(l, new LinearLayout.LayoutParams(-1, -2));
        m.addView(v, new LinearLayout.LayoutParams(-1, -2));
        return m;
    }

    private interface SearchAction { void run(String query); }

    private void addSearchBox(String hint, String query, SearchAction action) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setGravity(Gravity.CENTER_VERTICAL);
        box.setPadding(0, 0, 0, dp(12));
        EditText q = input(hint, query, false, false);
        q.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        Button b = secondaryButton("جستجو");
        box.addView(q, new LinearLayout.LayoutParams(0, dp(50), 1f));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(dp(92), dp(50));
        bp.setMargins(dp(8), 0, 0, 0);
        box.addView(b, bp);
        b.setOnClickListener(v -> action.run(q.getText().toString().trim()));
        q.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                action.run(q.getText().toString().trim());
                return true;
            }
            return false;
        });
        content.addView(box, new LinearLayout.LayoutParams(-1, -2));
    }

    private void loadTable(String title, String subtitle, String table) {
        content.removeAllViews();
        addHero(title, subtitle);
        addSearchBox("جستجو در جدول…", "", q -> loadTableWithQuery(title, subtitle, table, q));
        loadTableWithQuery(title, subtitle, table, "");
    }

    private void loadTableWithQuery(String title, String subtitle, String table, String query) {
        content.removeAllViews();
        addHero(title, subtitle);
        addSearchBox("جستجو در جدول…", query, q -> loadTableWithQuery(title, subtitle, table, q));
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت داده…");
        callApi("GET", "/api/table/" + table + "?q=" + enc(query) + "&pageSize=80", null, new ApiCallback() {
            @Override public void onSuccess(String body) {
                try {
                    JSONObject j = new JSONObject(body);
                    JSONArray cols = j.optJSONArray("columns");
                    JSONArray rows = j.optJSONArray("rows");
                    list.removeAllViews();
                    if (rows == null || rows.length() == 0) {
                        addEmptyTo(list, "رکوردی برای نمایش وجود ندارد.");
                        return;
                    }
                    for (int i = 0; i < rows.length(); i++) addGenericRow(list, cols, rows.optJSONObject(i));
                    TextView total = text("مجموع رکوردها: " + formatNumber(j.opt("total")), 11, MUTED, Typeface.NORMAL);
                    total.setGravity(Gravity.CENTER);
                    list.addView(total, new LinearLayout.LayoutParams(-1, -2));
                } catch (Exception e) {
                    showPageError(title, e, () -> loadTableWithQuery(title, subtitle, table, query));
                }
            }
            @Override public void onError(Exception e) { showPageError(title, e, () -> loadTableWithQuery(title, subtitle, table, query)); }
        });
    }

    private void addGenericRow(LinearLayout parent, JSONArray cols, JSONObject row) {
        if (row == null) return;
        LinearLayout c = card();
        int shown = 0;
        if (cols != null) {
            for (int i = 0; i < cols.length() && shown < 6; i++) {
                String col = cols.optString(i, "");
                if (col.trim().isEmpty()) continue;
                Object val = row.opt(col);
                TextView cell = text(col + ": " + (val == null || JSONObject.NULL.equals(val) ? "-" : String.valueOf(val)), shown == 0 ? 13 : 11.5f, shown == 0 ? TEXT : MUTED, shown == 0 ? Typeface.BOLD : Typeface.NORMAL);
                c.addView(cell, new LinearLayout.LayoutParams(-1, -2));
                shown++;
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        parent.addView(c, lp);
    }

    private void loadReports() {
        content.removeAllViews();
        addHero("گزارش‌های مدیریتی", "نمودارهای Native اندروید، بدون HTML و بدون PWA");
        addLoading(content, "در حال دریافت گزارش‌ها…");
        callApi("GET", "/api/analytics", null, new ApiCallback() {
            @Override public void onSuccess(String body) {
                try {
                    JSONObject a = new JSONObject(body);
                    content.removeAllViews();
                    addHero("گزارش‌های مدیریتی", "نمودارهای Native اندروید، بدون HTML و بدون PWA");
                    addChartCard("فروش هفتگی", "روند فروش", new LineChartView(MainActivity.this, a.optJSONArray("weeklySales"), GOLD));
                    addChartCard("سود خالص ۱۲ ماه", "تحلیل سود", new LineChartView(MainActivity.this, a.optJSONArray("monthlyProfit"), SUCCESS));
                    addChartCard("مشتریان برتر", "خریداران اصلی", new BarChartView(MainActivity.this, a.optJSONArray("topCustomers"), INFO));
                    addChartCard("سهم دسته‌های کالا", "گروه‌های پرفروش", new BarChartView(MainActivity.this, a.optJSONArray("categoryShare"), GOLD));
                    addChartCard("سن مطالبات", "بدهی مشتریان", new BarChartView(MainActivity.this, a.optJSONArray("debtAging"), WARNING));
                } catch (Exception e) {
                    showPageError("گزارش‌ها", e, () -> showApp("reports"));
                }
            }
            @Override public void onError(Exception e) { showPageError("گزارش‌ها", e, () -> showApp("reports")); }
        });
    }

    private void addChartCard(String title, String subtitle, View chart) {
        LinearLayout c = card();
        TextView h = text(title, 16, TEXT, Typeface.BOLD);
        TextView s = text(subtitle, 11, MUTED, Typeface.NORMAL);
        c.addView(h, new LinearLayout.LayoutParams(-1, -2));
        c.addView(s, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, dp(230));
        cp.setMargins(0, dp(12), 0, 0);
        c.addView(chart, cp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void renderSettings() {
        content.removeAllViews();
        addHero("تنظیمات", "مدیریت اتصال، Session و اطلاعات نسخه Native");

        LinearLayout connection = card();
        connection.addView(text("اتصال فعلی", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        connection.addView(text(prefs.getString(KEY_BASE_URL, "تنظیم نشده"), 12, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        connection.addView(text("کاربر: " + prefs.getString(KEY_USER, "-"), 12, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        Button change = secondaryButton("تغییر آدرس یا ورود با کاربر دیگر");
        change.setOnClickListener(v -> showLogin("آدرس API یا حساب کاربری را تغییر دهید."));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, dp(50));
        cp.setMargins(0, dp(14), 0, 0);
        connection.addView(change, cp);
        Button logout = primaryButton("خروج امن");
        logout.setOnClickListener(v -> logout());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(50));
        lp.setMargins(0, dp(10), 0, 0);
        connection.addView(logout, lp);
        LinearLayout.LayoutParams outer = new LinearLayout.LayoutParams(-1, -2);
        outer.setMargins(0, 0, 0, dp(12));
        content.addView(connection, outer);

        LinearLayout about = card();
        about.addView(text("درباره نسخه", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView desc = text("MEELANO Android Native v2.0.0\nاین نسخه PWA یا مرورگر داخلی نیست؛ تمام صفحات، کارت‌ها، لیست‌ها و نمودارها با کامپوننت‌های Native اندروید ساخته شده‌اند و فقط داده‌ها از API امن MEELANO دریافت می‌شود.", 12, MUTED, Typeface.NORMAL);
        desc.setLineSpacing(dp(3), 1.05f);
        about.addView(desc, new LinearLayout.LayoutParams(-1, -2));
        content.addView(about, new LinearLayout.LayoutParams(-1, -2));
    }

    private void logout() {
        callApi("POST", "/api/logout", new JSONObject(), new ApiCallback() {
            @Override public void onSuccess(String body) {
                prefs.edit().remove(KEY_COOKIE).remove(KEY_USER).apply();
                showLogin("با موفقیت خارج شدید.");
            }
            @Override public void onError(Exception e) {
                prefs.edit().remove(KEY_COOKIE).remove(KEY_USER).apply();
                showLogin("Session محلی پاک شد.");
            }
        });
    }

    private void addEmpty(String message) {
        addEmptyTo(content, message);
    }

    private void addEmptyTo(LinearLayout parent, String message) {
        LinearLayout c = card();
        TextView t = text(message, 12.5f, MUTED, Typeface.NORMAL);
        t.setGravity(Gravity.CENTER);
        c.addView(t, new LinearLayout.LayoutParams(-1, -2));
        parent.addView(c, new LinearLayout.LayoutParams(-1, -2));
    }

    private String normalizeBase(String raw) {
        String url = raw == null ? "" : raw.trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "https://" + url;
        while (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url;
    }

    private String shortHost(String url) {
        try {
            URL u = new URL(url);
            return u.getHost();
        } catch (Exception ignored) {
            return url == null ? "" : url;
        }
    }

    private String enc(String value) {
        try {
            return URLEncoder.encode(value == null ? "" : value, "UTF-8");
        } catch (Exception ignored) {
            return "";
        }
    }

    private String firstNonEmpty(String a, String b) {
        return a == null || a.trim().isEmpty() ? b : a;
    }

    private String formatNumber(Object value) {
        if (value == null || JSONObject.NULL.equals(value)) return "۰";
        try {
            if (value instanceof Number) return numberFormat.format(((Number) value).doubleValue());
            return numberFormat.format(Double.parseDouble(String.valueOf(value)));
        } catch (Exception ignored) {
            return String.valueOf(value);
        }
    }

    private String money(Object value) {
        return formatNumber(value) + " ریال";
    }

    private void callApi(String method, String path, JSONObject payload, ApiCallback callback) {
        status.setText("در حال ارتباط با API…");
        executor.execute(() -> {
            try {
                String body = request(method, path, payload);
                runOnUiThread(() -> {
                    status.setText("اتصال Native فعال");
                    callback.onSuccess(body);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    status.setText("خطا در اتصال API");
                    callback.onError(e);
                });
            }
        });
    }

    private String request(String method, String path, JSONObject payload) throws Exception {
        String base = normalizeBase(prefs.getString(KEY_BASE_URL, ""));
        if (base.length() < 10) throw new ApiException(0, "آدرس API تنظیم نشده است.");
        String cleanPath = path.startsWith("/") ? path : "/" + path;
        URL url = new URL(base + cleanPath);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod(method);
        c.setConnectTimeout(15000);
        c.setReadTimeout(30000);
        c.setRequestProperty("Accept", "application/json");
        c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        String cookie = prefs.getString(KEY_COOKIE, "");
        if (!cookie.trim().isEmpty()) c.setRequestProperty("Cookie", cookie);
        if (payload != null) {
            c.setDoOutput(true);
            byte[] bytes = payload.toString().getBytes(StandardCharsets.UTF_8);
            c.setFixedLengthStreamingMode(bytes.length);
            try (OutputStream out = c.getOutputStream()) { out.write(bytes); }
        }
        int code = c.getResponseCode();
        captureCookies(c);
        InputStream stream = code >= 400 ? c.getErrorStream() : c.getInputStream();
        String body = readAll(stream);
        c.disconnect();
        if (code < 200 || code >= 300) {
            String msg = extractMessage(body);
            if (code == 401 && msg.trim().isEmpty()) msg = "نام کاربری/رمز عبور معتبر نیست یا جلسه منقضی شده است.";
            if (msg.trim().isEmpty()) msg = "خطای سرویس: " + code;
            throw new ApiException(code, msg);
        }
        return body;
    }

    private String readAll(InputStream stream) throws Exception {
        if (stream == null) return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }

    private void captureCookies(HttpURLConnection c) {
        Map<String, List<String>> headers = c.getHeaderFields();
        if (headers == null) return;
        StringBuilder cookies = new StringBuilder();
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            if (e.getKey() != null && "Set-Cookie".equalsIgnoreCase(e.getKey())) {
                for (String raw : e.getValue()) {
                    if (raw == null) continue;
                    String first = raw.split(";", 2)[0].trim();
                    if (first.length() > 0) {
                        if (cookies.length() > 0) cookies.append("; ");
                        cookies.append(first);
                    }
                }
            }
        }
        if (cookies.length() > 0) prefs.edit().putString(KEY_COOKIE, cookies.toString()).apply();
    }

    private String extractMessage(String body) {
        try {
            JSONObject j = new JSONObject(body == null ? "" : body);
            return j.optString("message", j.optString("title", body));
        } catch (Exception ignored) {
            return body == null ? "" : body;
        }
    }

    private String readableError(Exception e) {
        if (e instanceof ApiException) return e.getMessage();
        String m = e == null ? "" : e.getMessage();
        if (m == null || m.trim().isEmpty()) return "خطای ناشناخته در اتصال.";
        if (m.contains("Failed to connect") || m.contains("timed out") || m.contains("Unable to resolve")) {
            return "اتصال به سرور برقرار نشد. آدرس API، اینترنت، VPN یا روشن بودن سرور را بررسی کنید.";
        }
        return m;
    }

    private interface ApiCallback {
        void onSuccess(String body);
        void onError(Exception e);
    }

    private static class ApiException extends Exception {
        final int code;
        ApiException(int code, String message) {
            super(message);
            this.code = code;
        }
    }

    private class LineChartView extends View {
        private final JSONArray data;
        private final int color;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        LineChartView(Context context, JSONArray data, int color) {
            super(context);
            this.data = data;
            this.color = color;
            setBackground(roundedStroke(SURFACE_2, 18, alpha(Color.WHITE, 25)));
            setPadding(dp(10), dp(10), dp(10), dp(10));
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();
            int left = dp(20), right = dp(16), top = dp(18), bottom = dp(34);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp(1));
            paint.setColor(alpha(Color.WHITE, 35));
            for (int i = 0; i < 4; i++) {
                float y = top + (h - top - bottom) * i / 3f;
                canvas.drawLine(left, y, w - right, y, paint);
            }
            if (data == null || data.length() == 0) {
                drawCentered(canvas, getWidth(), getHeight(), "داده‌ای برای نمودار وجود ندارد.");
                return;
            }
            double max = 1;
            for (int i = 0; i < data.length(); i++) max = Math.max(max, Math.abs(valueOf(data.optJSONObject(i))));
            int n = data.length();
            Path line = new Path();
            Path area = new Path();
            for (int i = 0; i < n; i++) {
                double v = valueOf(data.optJSONObject(i));
                float x = left + (w - left - right) * (n == 1 ? 0.5f : i / (float) (n - 1));
                float y = (float) (h - bottom - (v / max) * (h - top - bottom));
                if (i == 0) {
                    line.moveTo(x, y);
                    area.moveTo(x, h - bottom);
                    area.lineTo(x, y);
                } else {
                    line.lineTo(x, y);
                    area.lineTo(x, y);
                }
            }
            float lastX = left + (w - left - right);
            area.lineTo(lastX, h - bottom);
            area.close();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(alpha(color, 34));
            canvas.drawPath(area, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp(3));
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setColor(color);
            canvas.drawPath(line, paint);
            paint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < n; i++) {
                double v = valueOf(data.optJSONObject(i));
                float x = left + (w - left - right) * (n == 1 ? 0.5f : i / (float) (n - 1));
                float y = (float) (h - bottom - (v / max) * (h - top - bottom));
                canvas.drawCircle(x, y, dp(4), paint);
            }
            paint.setColor(MUTED);
            paint.setTextSize(dp(10));
            paint.setTextAlign(Paint.Align.CENTER);
            String first = data.optJSONObject(0).optString("label", "");
            String last = data.optJSONObject(n - 1).optString("label", "");
            canvas.drawText(first, left + dp(18), h - dp(12), paint);
            canvas.drawText(last, w - right - dp(28), h - dp(12), paint);
        }
    }

    private class BarChartView extends View {
        private final JSONArray data;
        private final int color;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        BarChartView(Context context, JSONArray data, int color) {
            super(context);
            this.data = data;
            this.color = color;
            setBackground(roundedStroke(SURFACE_2, 18, alpha(Color.WHITE, 25)));
            setPadding(dp(10), dp(10), dp(10), dp(10));
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();
            int left = dp(18), right = dp(18), top = dp(20), bottom = dp(40);
            if (data == null || data.length() == 0) {
                drawCentered(canvas, getWidth(), getHeight(), "داده‌ای برای نمودار وجود ندارد.");
                return;
            }
            int n = Math.min(data.length(), 8);
            double max = 1;
            for (int i = 0; i < n; i++) max = Math.max(max, Math.abs(valueOf(data.optJSONObject(i))));
            float gap = dp(8);
            float barW = Math.max(dp(16), (w - left - right - gap * (n - 1)) / n);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            for (int i = 0; i < n; i++) {
                JSONObject item = data.optJSONObject(i);
                double v = valueOf(item);
                float bh = (float) ((v / max) * (h - top - bottom));
                float x = left + i * (barW + gap);
                float y = h - bottom - bh;
                RectF rect = new RectF(x, y, x + barW, h - bottom);
                paint.setColor(alpha(color, 220));
                canvas.drawRoundRect(rect, dp(9), dp(9), paint);
                paint.setColor(MUTED);
                paint.setTextSize(dp(9));
                String label = item == null ? "" : item.optString("label", item.optString("series", ""));
                if (label.length() > 8) label = label.substring(0, 8) + "…";
                canvas.drawText(label, x + barW / 2, h - dp(14), paint);
            }
        }
    }

    private double valueOf(JSONObject item) {
        if (item == null) return 0;
        if (item.has("value")) return item.optDouble("value", 0);
        if (item.has("count")) return item.optDouble("count", 0);
        if (item.has("balance")) return item.optDouble("balance", 0);
        if (item.has("amount")) return item.optDouble("amount", 0);
        return 0;
    }

    private void drawCentered(Canvas canvas, int width, int height, String message) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(MUTED);
        p.setTextSize(dp(12));
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(message, width / 2f, height / 2f, p);
    }

    @Override
    public void onBackPressed() {
        if (!"dashboard".equals(activePage) && !"login".equals(activePage)) {
            showApp("dashboard");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }
}
