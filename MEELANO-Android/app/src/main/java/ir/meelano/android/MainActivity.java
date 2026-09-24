package ir.meelano.android;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.text.NumberFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final String PREFS = "meelano_android_direct_sql";
    private static final String KEY_LAST_USER = "last_atiran_user";

    private static final int[] S_HOST = {122, 126, 103, 120, 125, 122, 103, 120, 125, 126, 103, 120, 112};
    private static final int[] S_USER = {8, 45, 36, 32, 39, 8, 39};
    private static final int[] S_PASS = {26, 61, 9, 27, 123, 121, 123, 123, 109};
    private static final int[] S_DB = {8, 61, 32, 59, 40, 39, 123};
    private static final int S_KEY = 73;
    private static final int SQL_PORT = 1433;

    private static final int NAVY = Color.rgb(7, 9, 16);
    private static final int SURFACE = Color.rgb(18, 22, 31);
    private static final int SURFACE_2 = Color.rgb(24, 30, 42);
    private static final int GOLD = Color.rgb(231, 177, 90);
    private static final int GOLD_2 = Color.rgb(242, 207, 138);
    private static final int SUCCESS = Color.rgb(72, 199, 163);
    private static final int INFO = Color.rgb(102, 170, 245);
    private static final int WARNING = Color.rgb(244, 181, 95);
    private static final int TEXT = Color.rgb(246, 248, 252);
    private static final int MUTED = Color.rgb(154, 166, 183);
    private static final int BORDER = Color.argb(42, 255, 255, 255);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
    private SharedPreferences prefs;
    private FrameLayout stage;
    private TextView status;
    private TextView subtitle;
    private LinearLayout content;
    private LinearLayout navStrip;
    private String activePage = "login";
    private UserSession session;

    private static final Set<String> SAFE_TABLES = new HashSet<>(Arrays.asList(
            "CUSTOMERS", "inventory", "sailfact", "subsailfact", "sailfact_pish", "subsailfact_pish",
            "buyfact", "subbuyfact", "getchk", "putchk", "CheckTypes", "visitors", "Visit", "masir",
            "vis_goals", "Variety", "UNITS", "BANK", "kagroup"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        buildFrame();
        showLogin("برای ورود، نام کاربری و رمز Atiran را وارد کنید.");
    }

    private static String hidden(int[] data) {
        char[] out = new char[data.length];
        for (int i = 0; i < data.length; i++) out[i] = (char) (data[i] ^ S_KEY);
        return new String(out);
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
        TextView appTitle = text("MEELANO Android", 16, TEXT, Typeface.BOLD);
        status = text("اتصال مستقیم به سرور", 10.5f, MUTED, Typeface.NORMAL);
        subtitle = text("ورود با حساب Atiran", 9.5f, alpha(TEXT, 155), Typeface.NORMAL);
        titles.addView(appTitle, new LinearLayout.LayoutParams(-1, 0, 1f));
        titles.addView(status, new LinearLayout.LayoutParams(-1, 0, 1f));
        titles.addView(subtitle, new LinearLayout.LayoutParams(-1, 0, 1f));
        header.addView(titles, new LinearLayout.LayoutParams(0, dp(50), 1f));

        TextView refresh = iconButton("↻", "تلاش مجدد");
        refresh.setOnClickListener(v -> refreshActivePage());
        header.addView(refresh, new LinearLayout.LayoutParams(dp(43), dp(43)));

        TextView settings = iconButton("⚙", "تنظیمات");
        settings.setOnClickListener(v -> {
            if (session == null) showLogin("ابتدا وارد شوید."); else showApp("settings");
        });
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

    private EditText input(String hint, String value, boolean password) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            e.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        e.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
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

    private void showLogin(String message) {
        activePage = "login";
        session = null;
        status.setText("اتصال مستقیم به سرور");
        subtitle.setText("ورود با حساب Atiran");
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

        TextView h = text("MEELANO Android", 23, TEXT, Typeface.BOLD);
        h.setGravity(Gravity.CENTER);
        loginCard.addView(h, new LinearLayout.LayoutParams(-1, -2));

        TextView sub = text("نسخه اندروید با اتصال مستقیم؛ فقط نام کاربری و رمز Atiran را وارد کنید.", 12.5f, MUTED, Typeface.NORMAL);
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

        TextView userLabel = text("نام کاربری Atiran", 12, MUTED, Typeface.BOLD);
        loginCard.addView(userLabel, new LinearLayout.LayoutParams(-1, -2));
        EditText username = input("username", prefs.getString(KEY_LAST_USER, ""), false);
        LinearLayout.LayoutParams up = new LinearLayout.LayoutParams(-1, dp(54));
        up.setMargins(0, dp(6), 0, dp(12));
        loginCard.addView(username, up);

        TextView passLabel = text("رمز عبور Atiran", 12, MUTED, Typeface.BOLD);
        loginCard.addView(passLabel, new LinearLayout.LayoutParams(-1, -2));
        EditText password = input("password", "", true);
        password.setImeOptions(EditorInfo.IME_ACTION_DONE);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(54));
        pp.setMargins(0, dp(6), 0, dp(16));
        loginCard.addView(password, pp);

        Button login = primaryButton("اتصال و ورود");
        loginCard.addView(login, new LinearLayout.LayoutParams(-1, dp(54)));

        TextView note = text("در صورت عدم اتصال، فقط پیام خطای اتصال و گزینه تلاش مجدد نمایش داده می‌شود.", 10.5f, MUTED, Typeface.NORMAL);
        note.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams noteLp = new LinearLayout.LayoutParams(-1, -2);
        noteLp.setMargins(0, dp(14), 0, 0);
        loginCard.addView(note, noteLp);

        final View.OnClickListener[] doLogin = new View.OnClickListener[1];
        doLogin[0] = v -> {
            String u = username.getText().toString().trim();
            String p = password.getText().toString();
            if (u.isEmpty()) { username.setError("نام کاربری الزامی است"); return; }
            if (p.isEmpty()) { password.setError("رمز عبور الزامی است"); return; }
            login.setEnabled(false);
            login.setText("در حال اتصال…");
            status.setText("در حال اتصال به سرور…");
            executor.execute(() -> {
                try {
                    UserSession s = authenticate(u, p);
                    runOnUiThread(() -> {
                        session = s;
                        prefs.edit().putString(KEY_LAST_USER, u).apply();
                        login.setEnabled(true);
                        login.setText("اتصال و ورود");
                        status.setText("اتصال برقرار شد");
                        Toast.makeText(this, "اتصال موفق بود", Toast.LENGTH_SHORT).show();
                        showApp("dashboard");
                    });
                } catch (Exception ex) {
                    runOnUiThread(() -> {
                        login.setEnabled(true);
                        login.setText("اتصال و ورود");
                        status.setText("عدم اتصال");
                        showLoginError(readableError(ex), () -> doLogin[0].onClick(login));
                    });
                }
            });
        };
        login.setOnClickListener(doLogin[0]);
        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doLogin[0].onClick(login);
                return true;
            }
            return false;
        });

        backdrop.addView(scroll, new FrameLayout.LayoutParams(-1, -1));
        stage.addView(backdrop, new FrameLayout.LayoutParams(-1, -1));
    }

    private void showLoginError(String message, Runnable retry) {
        new AlertDialog.Builder(this)
                .setTitle("عدم اتصال")
                .setMessage(message + "\n\nبرای اتصال مجدد تلاش کنید.")
                .setNegativeButton("بستن", null)
                .setPositiveButton("تلاش مجدد", (d, w) -> retry.run())
                .show();
    }

    private void showApp(String page) {
        if (session == null) {
            showLogin("ابتدا وارد شوید.");
            return;
        }
        activePage = page;
        status.setText("اتصال مستقیم فعال");
        subtitle.setText(session.userName);
        stage.removeAllViews();

        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setBackgroundColor(NAVY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            shell.setLayoutDirection(getRtlMode() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
            shell.setTextDirection(getRtlMode() ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);
        }

        HorizontalScrollView navScroll = new HorizontalScrollView(this);
        navScroll.setHorizontalScrollBarEnabled(false);
        navStrip = new LinearLayout(this);
        navStrip.setOrientation(LinearLayout.HORIZONTAL);
        navStrip.setGravity(Gravity.CENTER_VERTICAL);
        navStrip.setPadding(dp(10), dp(10), dp(10), dp(6));
        navScroll.addView(navStrip, new HorizontalScrollView.LayoutParams(-2, -1));
        shell.addView(navScroll, new LinearLayout.LayoutParams(-1, dp(62)));

        ScrollView scroll = new ScrollView(this);
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
        switch (activePage) {
            case "customers": loadCustomers(""); break;
            case "products": loadProducts(""); break;
            case "sales": loadTable("فروش و اسناد", "نمای مستقیم از جدول فروش", "sailfact", ""); break;
            case "checks": loadTable("چک‌ها و وصول", "نمای مستقیم از چک‌های دریافتی", "getchk", ""); break;
            case "reports": loadReports(); break;
            case "settings": renderSettings(); break;
            case "dashboard":
            default: loadDashboard(); break;
        }
    }

    private void refreshActivePage() {
        if ("login".equals(activePage)) showLogin("برای اتصال مجدد، اطلاعات Atiran را وارد کنید.");
        else showApp(activePage);
    }

    private void addHero(String title, String text) {
        LinearLayout hero = card();
        hero.setBackground(gradient(new int[]{Color.rgb(26, 32, 45), Color.rgb(15, 19, 28)}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
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
        TextView h = this.text(title, 22, TEXT, Typeface.BOLD);
        TextView s = this.text(text, 12, MUTED, Typeface.NORMAL);
        s.setLineSpacing(dp(2), 1.05f);
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
        addHero(title, "عدم اتصال به سرور");
        LinearLayout c = card();
        TextView h = text("عدم اتصال", 17, TEXT, Typeface.BOLD);
        TextView m = text(readableError(error) + "\n\nبرای اتصال مجدد تلاش کنید.", 12, MUTED, Typeface.NORMAL);
        m.setLineSpacing(dp(3), 1.05f);
        c.addView(h, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2);
        mp.setMargins(0, dp(8), 0, dp(14));
        c.addView(m, mp);
        Button b = primaryButton("تلاش مجدد");
        b.setOnClickListener(v -> retry.run());
        c.addView(b, new LinearLayout.LayoutParams(-1, dp(50)));
        content.addView(c, new LinearLayout.LayoutParams(-1, -2));
    }

    private interface DbJob { String run() throws Exception; }
    private interface DbCallback { void ok(String body); void fail(Exception e); }

    private void runDb(DbJob job, DbCallback callback) {
        status.setText("در حال ارتباط با سرور…");
        executor.execute(() -> {
            try {
                String body = job.run();
                runOnUiThread(() -> {
                    status.setText("اتصال مستقیم فعال");
                    callback.ok(body);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    status.setText("عدم اتصال");
                    callback.fail(e);
                });
            }
        });
    }

    private Connection openConnection() throws Exception {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        String url = "jdbc:jtds:sqlserver://" + hidden(S_HOST) + ":" + SQL_PORT + "/" + hidden(S_DB)
                + ";loginTimeout=10;socketTimeout=30;appName=MEELANOAndroid;";
        Properties props = new Properties();
        props.setProperty("user", hidden(S_USER));
        props.setProperty("password", hidden(S_PASS));
        props.setProperty("charset", "UTF-8");
        props.setProperty("sendStringParametersAsUnicode", "true");
        return DriverManager.getConnection(url, props);
    }

    private UserSession authenticate(String atiranUser, String atiranPassword) throws Exception {
        String user = cleanText(atiranUser);
        String pass = atiranPassword == null ? "" : atiranPassword;
        try (Connection c = openConnection()) {
            String visitorSql = "SELECT TOP (1) v.vis_rdf, v.vis_name, v.UserID FROM dbo.visitors AS v " +
                    "WHERE LTRIM(RTRIM(CONVERT(nvarchar(100),v.Username)))=? " +
                    "AND (CONVERT(nvarchar(200),v.Password)=? OR LTRIM(RTRIM(CONVERT(nvarchar(200),v.Password)))=?) " +
                    "AND (v.active='1' OR v.active='Y' OR v.active='y') ORDER BY v.vis_rdf";
            try (PreparedStatement ps = c.prepareStatement(visitorSql)) {
                ps.setString(1, user);
                ps.setString(2, pass);
                ps.setString(3, pass.trim());
                try (ResultSet r = ps.executeQuery()) {
                    if (r.next()) {
                        Integer uid = r.getObject(3) == null ? null : r.getInt(3);
                        return new UserSession(uid, r.getInt(1), stringOr(r.getString(2), user));
                    }
                }
            }

            // Users created in Atiran's user-management screen are stored in sys_users.
            // user_password may be VARBINARY encoded as ANSI bytes, Unicode bytes, or a text value.
            // We fetch candidate rows by user name and compare the password locally in several
            // compatible encodings instead of relying on one CONVERT(varchar, varbinary) shape.
            String userSql = "SELECT TOP (5) u.user_id, u.user_name, u.user_password, sv.shvis FROM dbo.sys_users AS u " +
                    "LEFT JOIN dbo.sys_vis AS sv ON sv.UserID=u.user_id " +
                    "WHERE LOWER(LTRIM(RTRIM(CONVERT(nvarchar(100),u.user_name))))=LOWER(LTRIM(RTRIM(?))) " +
                    "ORDER BY CASE WHEN sv.shvis IS NULL THEN 1 ELSE 0 END";
            boolean foundUser = false;
            try (PreparedStatement ps = c.prepareStatement(userSql)) {
                ps.setString(1, user);
                try (ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        foundUser = true;
                        byte[] rawPassword = r.getBytes(3);
                        String textPassword = r.getString(3);
                        if (passwordMatches(rawPassword, textPassword, pass)) {
                            Integer visitor = r.getObject(4) == null ? null : r.getInt(4);
                            return new UserSession(r.getInt(1), visitor, stringOr(r.getString(2), user));
                        }
                    }
                }
            }
            if (foundUser) throw new DbException("رمز عبور Atiran برای این کاربر تطبیق پیدا نکرد.");
        }
        throw new DbException("نام کاربری یا رمز عبور Atiran معتبر نیست.");
    }

    private boolean passwordMatches(byte[] rawPassword, String textPassword, String enteredPassword) {
        String entered = normalizePassword(enteredPassword);
        if (entered.isEmpty() && (enteredPassword == null || enteredPassword.isEmpty())) return false;
        if (textPassword != null && normalizePassword(textPassword).equals(entered)) return true;
        if (rawPassword == null) return false;

        if (bytesEqual(rawPassword, enteredPassword.getBytes(StandardCharsets.UTF_8))) return true;
        if (bytesEqual(rawPassword, enteredPassword.getBytes(StandardCharsets.UTF_16LE))) return true;
        if (bytesEqual(rawPassword, enteredPassword.getBytes(StandardCharsets.ISO_8859_1))) return true;
        try {
            if (bytesEqual(rawPassword, enteredPassword.getBytes(Charset.forName("windows-1256")))) return true;
        } catch (Exception ignored) { }

        if (normalizePassword(new String(rawPassword, StandardCharsets.UTF_8)).equals(entered)) return true;
        if (normalizePassword(new String(rawPassword, StandardCharsets.UTF_16LE)).equals(entered)) return true;
        if (normalizePassword(new String(rawPassword, StandardCharsets.ISO_8859_1)).equals(entered)) return true;
        try {
            if (normalizePassword(new String(rawPassword, Charset.forName("windows-1256"))).equals(entered)) return true;
        } catch (Exception ignored) { }
        return false;
    }

    private boolean bytesEqual(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;
        return true;
    }

    private String normalizePassword(String value) {
        return value == null ? "" : value.replace("\u0000", "").trim();
    }

    private String cleanText(String value) {
        return value == null ? "" : value.trim();
    }

    private void loadDashboard() {
        content.removeAllViews();
        addHero("مرکز فرماندهی", "KPIها و نمودارها با اتصال مستقیم به داده‌های Atiran");
        addLoading(content, "در حال دریافت داشبورد…");
        runDb(this::queryDashboard, new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONObject j = new JSONObject(body);
                    content.removeAllViews();
                    addHero("مرکز فرماندهی", "KPIها و نمودارها با اتصال مستقیم به داده‌های Atiran");
                    addKpis(j.optJSONArray("kpis"));
                    JSONObject a = j.optJSONObject("analytics");
                    if (a != null) {
                        addChartCard("روند فروش", "جمع فروش ماهانه/هفتگی", new LineChartView(MainActivity.this, a.optJSONArray("weeklySales"), GOLD));
                        addChartCard("مشتریان برتر", "بر اساس مبلغ فروش", new BarChartView(MainActivity.this, a.optJSONArray("topCustomers"), SUCCESS));
                        addChartCard("مانده مشتریان", "تحلیل مانده حساب", new BarChartView(MainActivity.this, a.optJSONArray("debtAging"), WARNING));
                    }
                } catch (Exception e) { showPageError("داشبورد", e, () -> showApp("dashboard")); }
            }
            @Override public void fail(Exception e) { showPageError("داشبورد", e, () -> showApp("dashboard")); }
        });
    }

    private String queryDashboard() throws Exception {
        try (Connection c = openConnection()) {
            JSONObject out = new JSONObject();
            JSONArray kpis = new JSONArray();
            String[][] targets = {
                    {"مشتریان", "CUSTOMERS"}, {"کالاها", "inventory"}, {"فروش", "sailfact"},
                    {"پیش‌فاکتور", "sailfact_pish"}, {"چک دریافتی", "getchk"}, {"چک پرداختی", "putchk"},
                    {"ویزیتورها", "visitors"}, {"اهداف", "vis_goals"}
            };
            for (String[] target : targets) {
                JSONObject item = new JSONObject();
                item.put("title", target[0]);
                try {
                    item.put("value", countTable(c, target[1]));
                    item.put("available", true);
                } catch (Exception ex) {
                    item.put("value", 0);
                    item.put("available", false);
                }
                kpis.put(item);
            }
            JSONObject a = new JSONObject();
            a.put("weeklySales", loadWeeklySales(c));
            a.put("topCustomers", loadTopCustomers(c));
            a.put("debtAging", loadDebtAging(c));
            a.put("categoryShare", loadCategoryShare(c));
            a.put("monthlyProfit", loadMonthlyProfit(c));
            out.put("kpis", kpis);
            out.put("analytics", a);
            return out.toString();
        }
    }

    private long countTable(Connection c, String table) throws Exception {
        if (!SAFE_TABLES.contains(table)) throw new DbException("جدول مجاز نیست.");
        Set<String> cols = columns(c, table);
        String where = "";
        if (session != null && session.visitorId != null && hasCol(cols, "vis_rdf") &&
                (table.equalsIgnoreCase("CUSTOMERS") || table.equalsIgnoreCase("sailfact") || table.equalsIgnoreCase("vis_goals"))) {
            where = " WHERE TRY_CONVERT(int,[vis_rdf])=?";
        }
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(1) FROM dbo.[" + table + "]" + where)) {
            if (!where.isEmpty()) ps.setInt(1, session.visitorId);
            try (ResultSet r = ps.executeQuery()) { return r.next() ? r.getLong(1) : 0; }
        }
    }

    private void addKpis(JSONArray kpis) {
        if (kpis == null || kpis.length() == 0) {
            addEmptyTo(content, "KPI قابل نمایش وجود ندارد.");
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
            TextView title = text(item == null ? "شاخص" : item.optString("title", "شاخص"), 11.5f, MUTED, Typeface.NORMAL);
            TextView value = text(formatNumber(item == null ? 0 : item.opt("value")), 21, TEXT, Typeface.BOLD);
            TextView live = text("داده مستقیم", 10, SUCCESS, Typeface.NORMAL);
            c.addView(icon, new LinearLayout.LayoutParams(-1, -2));
            c.addView(title, new LinearLayout.LayoutParams(-1, -2));
            c.addView(value, new LinearLayout.LayoutParams(-1, -2));
            c.addView(live, new LinearLayout.LayoutParams(-1, -2));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(142), 1f);
            cp.setMargins(dp(4), 0, dp(4), 0);
            if (row != null) row.addView(c, cp);
        }
    }

    private void loadCustomers(String query) {
        content.removeAllViews();
        addHero("مشتریان", "جستجو و Customer 360 با اتصال مستقیم");
        addSearchBox("جستجوی مشتری…", query, q -> loadCustomers(q));
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت مشتریان…");
        runDb(() -> queryCustomers(query), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONArray rows = new JSONArray(body);
                    list.removeAllViews();
                    if (rows.length() == 0) { addEmptyTo(list, "مشتری مطابق جستجو پیدا نشد."); return; }
                    for (int i = 0; i < rows.length(); i++) addCustomerCard(list, rows.optJSONObject(i));
                } catch (Exception e) { showPageError("مشتریان", e, () -> loadCustomers(query)); }
            }
            @Override public void fail(Exception e) { showPageError("مشتریان", e, () -> loadCustomers(query)); }
        });
    }

    private String queryCustomers(String search) throws Exception {
        try (Connection c = openConnection()) {
            Set<String> cols = columns(c, "CUSTOMERS");
            String shmo = resolve(cols, "SHMO");
            if (shmo == null) throw new DbException("ستون مشتری یافت نشد.");
            String name = resolve(cols, "MONAME", "Name", "CusName");
            String phone = resolve(cols, "cell", "tell1", "tell2");
            String phone2 = resolve(cols, "tell1", "tell2");
            String address = resolve(cols, "address", "Address", "adr", "addr", "manzel");
            String balance = resolve(cols, "man", "Balance", "Mandeh");
            String vis = resolve(cols, "vis_rdf", "VisitorID", "visid");

            List<String> select = new ArrayList<>();
            select.add("[" + shmo + "] AS shmo");
            select.add(name == null ? "CAST(NULL AS nvarchar(250)) AS name" : "TRY_CONVERT(nvarchar(250),[" + name + "]) AS name");
            select.add(phone == null ? "CAST(NULL AS nvarchar(100)) AS phone" : "TRY_CONVERT(nvarchar(100),[" + phone + "]) AS phone");
            select.add(phone2 == null ? "CAST(NULL AS nvarchar(100)) AS phone2" : "TRY_CONVERT(nvarchar(100),[" + phone2 + "]) AS phone2");
            select.add(address == null ? "CAST(NULL AS nvarchar(500)) AS address" : "TRY_CONVERT(nvarchar(500),[" + address + "]) AS address");
            select.add(balance == null ? "CAST(0 AS decimal(19,2)) AS balance" : "TRY_CONVERT(decimal(19,2),[" + balance + "]) AS balance");
            select.add(vis == null ? "CAST(NULL AS int) AS visitorId" : "TRY_CONVERT(int,[" + vis + "]) AS visitorId");

            List<String> where = new ArrayList<>();
            List<Object> params = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                List<String> parts = new ArrayList<>();
                for (String col : new String[]{name, phone, phone2, address, shmo}) {
                    if (col != null) { parts.add("TRY_CONVERT(nvarchar(500),[" + col + "]) LIKE N'%' + ? + N'%'"); params.add(search.trim()); }
                }
                where.add("(" + join(parts, " OR ") + ")");
            }
            if (session != null && session.visitorId != null && vis != null) {
                where.add("TRY_CONVERT(int,[" + vis + "])=?");
                params.add(session.visitorId);
            }
            String sql = "SELECT TOP (120) " + join(select, ",") + " FROM dbo.[CUSTOMERS]" +
                    (where.isEmpty() ? "" : " WHERE " + join(where, " AND ")) + " ORDER BY name, shmo";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                setParams(ps, params);
                try (ResultSet r = ps.executeQuery()) { return rowsToJson(r).toString(); }
            }
        }
    }

    private void addCustomerCard(LinearLayout parent, JSONObject r) {
        if (r == null) return;
        LinearLayout c = card();
        c.setClickable(true);
        c.setOnClickListener(v -> showCustomerDialog(r));
        c.addView(text(r.optString("name", "بدون نام"), 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("کد: " + r.optString("shmo", "-") + "   |   همراه: " + r.optString("phone", "-"), 11.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("مانده: " + money(r.opt("balance")), 13, r.optDouble("balance", 0) > 0 ? WARNING : SUCCESS, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView address = text(r.optString("address", "-"), 11, alpha(TEXT, 190), Typeface.NORMAL);
        address.setMaxLines(2);
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
        new AlertDialog.Builder(this).setTitle(r.optString("name", "Customer 360")).setMessage(message).setPositiveButton("بستن", null).show();
    }

    private void loadProducts(String query) {
        content.removeAllViews();
        addHero("کالا و انبار", "Product Intelligence با اتصال مستقیم");
        addSearchBox("جستجوی کالا…", query, q -> loadProducts(q));
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت کالاها…");
        runDb(() -> queryProducts(query), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONArray rows = new JSONArray(body);
                    list.removeAllViews();
                    if (rows.length() == 0) { addEmptyTo(list, "کالایی مطابق جستجو پیدا نشد."); return; }
                    for (int i = 0; i < rows.length(); i++) addProductCard(list, rows.optJSONObject(i));
                } catch (Exception e) { showPageError("کالا", e, () -> loadProducts(query)); }
            }
            @Override public void fail(Exception e) { showPageError("کالا", e, () -> loadProducts(query)); }
        });
    }

    private String queryProducts(String search) throws Exception {
        try (Connection c = openConnection()) {
            Set<String> cols = columns(c, "inventory");
            String shka = resolve(cols, "shka", "SHKA");
            if (shka == null) throw new DbException("ستون کالا یافت نشد.");
            String name = resolve(cols, "naka", "Name", "KalaName");
            String code = resolve(cols, "StuffCode", "Code", "Barcode", "KalaCode");
            String price = resolve(cols, "FinalSalePrice", "SalePrice", "Price");
            String stock = resolve(cols, "Mojoodi", "mojoodi", "Stock", "Qty", "tedad", "Tedad");
            List<String> select = new ArrayList<>();
            select.add("[" + shka + "] AS shka");
            select.add(name == null ? "CAST(NULL AS nvarchar(250)) AS name" : "TRY_CONVERT(nvarchar(250),[" + name + "]) AS name");
            select.add(code == null ? "CAST(NULL AS nvarchar(100)) AS code" : "TRY_CONVERT(nvarchar(100),[" + code + "]) AS code");
            select.add(price == null ? "CAST(0 AS decimal(19,2)) AS price" : "TRY_CONVERT(decimal(19,2),[" + price + "]) AS price");
            select.add(stock == null ? "CAST(0 AS decimal(19,3)) AS stock" : "TRY_CONVERT(decimal(19,3),[" + stock + "]) AS stock");
            List<String> where = new ArrayList<>();
            List<Object> params = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                List<String> parts = new ArrayList<>();
                for (String col : new String[]{name, code, shka}) {
                    if (col != null) { parts.add("TRY_CONVERT(nvarchar(500),[" + col + "]) LIKE N'%' + ? + N'%'"); params.add(search.trim()); }
                }
                where.add("(" + join(parts, " OR ") + ")");
            }
            String sql = "SELECT TOP (120) " + join(select, ",") + " FROM dbo.[inventory]" +
                    (where.isEmpty() ? "" : " WHERE " + join(where, " AND ")) + " ORDER BY name, shka";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                setParams(ps, params);
                try (ResultSet r = ps.executeQuery()) { return rowsToJson(r).toString(); }
            }
        }
    }

    private void addProductCard(LinearLayout parent, JSONObject r) {
        if (r == null) return;
        LinearLayout c = card();
        c.addView(text(r.optString("name", "بدون نام"), 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("کد: " + firstNonEmpty(r.optString("code", ""), r.optString("shka", "-")), 11.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout metrics = new LinearLayout(this);
        metrics.setOrientation(LinearLayout.HORIZONTAL);
        metrics.addView(metric("قیمت", money(r.opt("price"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics.addView(metric("موجودی", formatNumber(r.opt("stock"))), new LinearLayout.LayoutParams(0, -2, 1f));
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
        EditText q = input(hint, query, false);
        q.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        Button b = secondaryButton("جستجو");
        box.addView(q, new LinearLayout.LayoutParams(0, dp(50), 1f));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(dp(92), dp(50));
        bp.setMargins(dp(8), 0, 0, 0);
        box.addView(b, bp);
        b.setOnClickListener(v -> action.run(q.getText().toString().trim()));
        q.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { action.run(q.getText().toString().trim()); return true; }
            return false;
        });
        content.addView(box, new LinearLayout.LayoutParams(-1, -2));
    }

    private void loadTable(String title, String sub, String table, String query) {
        content.removeAllViews();
        addHero(title, sub);
        addSearchBox("جستجو در جدول…", query, q -> loadTable(title, sub, table, q));
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت داده…");
        runDb(() -> queryTable(table, query), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONObject j = new JSONObject(body);
                    JSONArray cols = j.optJSONArray("columns");
                    JSONArray rows = j.optJSONArray("rows");
                    list.removeAllViews();
                    if (rows == null || rows.length() == 0) { addEmptyTo(list, "رکوردی برای نمایش وجود ندارد."); return; }
                    for (int i = 0; i < rows.length(); i++) addGenericRow(list, cols, rows.optJSONObject(i));
                    TextView total = text("مجموع رکوردها: " + formatNumber(j.opt("total")), 11, MUTED, Typeface.NORMAL);
                    total.setGravity(Gravity.CENTER);
                    list.addView(total, new LinearLayout.LayoutParams(-1, -2));
                } catch (Exception e) { showPageError(title, e, () -> loadTable(title, sub, table, query)); }
            }
            @Override public void fail(Exception e) { showPageError(title, e, () -> loadTable(title, sub, table, query)); }
        });
    }

    private String queryTable(String table, String query) throws Exception {
        if (!SAFE_TABLES.contains(table)) throw new DbException("این جدول برای نمایش مجاز نیست.");
        try (Connection c = openConnection()) {
            List<String> cols = new ArrayList<>(columns(c, table));
            if (cols.isEmpty()) throw new DbException("ستون‌های جدول یافت نشد.");
            List<String> safeCols = new ArrayList<>();
            for (String col : cols) if (!isSensitiveColumn(col)) safeCols.add(col);
            List<String> showCols = safeCols.subList(0, Math.min(10, safeCols.size()));
            List<String> where = new ArrayList<>();
            List<Object> params = new ArrayList<>();
            if (query != null && !query.trim().isEmpty()) {
                List<String> parts = new ArrayList<>();
                for (String col : showCols) {
                    String l = col.toLowerCase(Locale.US);
                    if (l.contains("name") || l.contains("code") || l.contains("shmo") || l.contains("date") || l.contains("num")) {
                        parts.add("TRY_CONVERT(nvarchar(500),[" + col + "]) LIKE N'%' + ? + N'%'");
                        params.add(query.trim());
                    }
                }
                if (!parts.isEmpty()) where.add("(" + join(parts, " OR ") + ")");
            }
            String whereSql = where.isEmpty() ? "" : " WHERE " + join(where, " AND ");
            long total;
            try (PreparedStatement count = c.prepareStatement("SELECT COUNT_BIG(1) FROM dbo.[" + table + "]" + whereSql)) {
                setParams(count, params);
                try (ResultSet r = count.executeQuery()) { total = r.next() ? r.getLong(1) : 0; }
            }
            StringBuilder select = new StringBuilder();
            for (int i = 0; i < showCols.size(); i++) {
                if (i > 0) select.append(',');
                select.append('[').append(showCols.get(i)).append(']');
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT TOP (80) " + select + " FROM dbo.[" + table + "]" + whereSql)) {
                setParams(ps, params);
                try (ResultSet r = ps.executeQuery()) {
                    JSONObject out = new JSONObject();
                    JSONArray colJson = new JSONArray();
                    for (String col : showCols) colJson.put(col);
                    out.put("columns", colJson);
                    out.put("total", total);
                    out.put("rows", rowsToJson(r));
                    return out.toString();
                }
            }
        }
    }

    private void addGenericRow(LinearLayout parent, JSONArray cols, JSONObject row) {
        if (row == null) return;
        LinearLayout c = card();
        int shown = 0;
        if (cols != null) {
            for (int i = 0; i < cols.length() && shown < 6; i++) {
                String col = cols.optString(i, "");
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
        addHero("نمودارها و تحلیل‌های مدیریتی", "Executive Analytics مستقیم از SQL Server با کنترل نمایش مستقل");
        addLoading(content, "در حال تحلیل داده‌های مدیریتی…");
        runDb(this::queryAnalytics, new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONObject a = new JSONObject(body);
                    renderAnalytics(a);
                } catch (Exception e) { showPageError("گزارش‌ها", e, () -> showApp("reports")); }
            }
            @Override public void fail(Exception e) { showPageError("گزارش‌ها", e, () -> showApp("reports")); }
        });
    }

    private void renderAnalytics(JSONObject a) {
        content.removeAllViews();
        addHero("نمودارها و تحلیل‌های مدیریتی", "۱۰ ویژوال کلیدی، گزارش روزانه فروش/خرید و کنترل نمایش");
        addAnalyticsControls();

        addChartIfVisible("weeklySales", "روند فروش هفتگی", "فروش واقعی در بازه‌های هفتگی/ماهانه", new LineChartView(this, a.optJSONArray("weeklySales"), GOLD));
        addChartIfVisible("monthlyPurchaseSales", "روند ماهانه خرید در برابر فروش", "ستون طلایی = فروش، ستون آبی = خرید", new MultiBarChartView(this, a.optJSONArray("monthlyPurchaseSales"), GOLD, INFO));
        addChartIfVisible("checkStatuses", "وضعیت چک‌ها", "دریافتی/پرداختی با وضعیت واقعی CheckTypes", new BarChartView(this, a.optJSONArray("checkStatuses"), WARNING));
        addChartIfVisible("topCustomers", "مشتریان برتر", "رتبه‌بندی بر اساس مبلغ فروش", new BarChartView(this, a.optJSONArray("topCustomers"), SUCCESS));
        addChartIfVisible("debtAging", "شکاف مطالبات و سن بدهی", "۰، ۳۰، ۶۰، ۹۰، ۱۸۰+ روز بر اساس سررسید فاکتور", new BarChartView(this, a.optJSONArray("debtAging"), WARNING));
        addChartIfVisible("monthlyProfit", "سود ماهانه ۱۲ ماه اخیر", "فروش منهای بهای تمام‌شده ثبت‌شده", new LineChartView(this, a.optJSONArray("monthlyProfit"), SUCCESS));
        if (!isChartHidden("banks")) addBanksSection(a.optJSONArray("banks"));
        addChartIfVisible("categoryShare", "سهم دسته‌های کالایی", "گروه‌های پرفروش بر اساس مبلغ فروش", new BarChartView(this, a.optJSONArray("categoryShare"), GOLD));
        addChartIfVisible("customerGrowth", "رشد مشتریان فعال", "تعداد مشتریان فعال ماه‌به‌ماه در ۱۲ ماه", new LineChartView(this, a.optJSONArray("customerGrowth"), INFO));
        addChartIfVisible("netMargin", "حاشیه سود درصدی", "سود تقسیم بر فروش × ۱۰۰", new LineChartView(this, a.optJSONArray("netMargin"), GOLD_2));
        addDailyReportLaunchers(a.optString("latestSalesDate", ""), a.optString("latestPurchaseDate", ""));
    }

    private void addAnalyticsControls() {
        LinearLayout panel = card();
        panel.addView(text("کنترل نمایش", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView hint = text("برای هر نمودار می‌توانید نمایش/مخفی‌سازی را مستقل کنترل کنید.", 11, MUTED, Typeface.NORMAL);
        panel.addView(hint, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout quick = new LinearLayout(this);
        quick.setOrientation(LinearLayout.HORIZONTAL);
        quick.setGravity(Gravity.CENTER_VERTICAL);
        String[] quickLabels = {"نمایش همه", "مخفی کردن همه", getRtlMode() ? "RTL" : "LTR"};
        for (int i = 0; i < quickLabels.length; i++) {
            Button b = i == 0 ? primaryButton(quickLabels[i]) : secondaryButton(quickLabels[i]);
            final int idx = i;
            b.setOnClickListener(v -> {
                if (idx == 0) { prefs.edit().putString("hidden_charts", "").apply(); showApp("reports"); }
                else if (idx == 1) { prefs.edit().putString("hidden_charts", "weeklySales,monthlyPurchaseSales,checkStatuses,topCustomers,debtAging,monthlyProfit,banks,categoryShare,customerGrowth,netMargin").apply(); showApp("reports"); }
                else { prefs.edit().putBoolean("rtl_mode", !getRtlMode()).apply(); showApp("reports"); }
            });
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(0, dp(44), 1f);
            bp.setMargins(dp(3), dp(10), dp(3), dp(8));
            quick.addView(b, bp);
        }
        panel.addView(quick, new LinearLayout.LayoutParams(-1, -2));

        String[][] charts = chartDefinitions();
        LinearLayout row = null;
        for (int i = 0; i < charts.length; i++) {
            if (i % 2 == 0) {
                row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                panel.addView(row, new LinearLayout.LayoutParams(-1, -2));
            }
            String key = charts[i][0];
            String label = charts[i][1];
            Button chip = secondaryButton((isChartHidden(key) ? "نمایش " : "مخفی " ) + label);
            chip.setTextSize(10.5f);
            chip.setOnClickListener(v -> { toggleChart(key); showApp("reports"); });
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(42), 1f);
            cp.setMargins(dp(3), dp(3), dp(3), dp(3));
            if (row != null) row.addView(chip, cp);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        content.addView(panel, lp);
    }

    private String[][] chartDefinitions() {
        return new String[][]{
                {"weeklySales", "فروش"}, {"monthlyPurchaseSales", "خرید/فروش"},
                {"checkStatuses", "چک‌ها"}, {"topCustomers", "مشتریان"},
                {"debtAging", "مطالبات"}, {"monthlyProfit", "سود"},
                {"banks", "بانک‌ها"}, {"categoryShare", "دسته کالا"},
                {"customerGrowth", "رشد مشتری"}, {"netMargin", "حاشیه سود"}
        };
    }

    private boolean getRtlMode() { return prefs.getBoolean("rtl_mode", true); }

    private boolean isChartHidden(String key) {
        String hidden = prefs.getString("hidden_charts", "");
        return ("," + hidden + ",").contains("," + key + ",");
    }

    private void toggleChart(String key) {
        String hidden = prefs.getString("hidden_charts", "");
        List<String> list = new ArrayList<>();
        for (String x : hidden.split(",")) if (!x.trim().isEmpty() && !x.trim().equals(key)) list.add(x.trim());
        if (!("," + hidden + ",").contains("," + key + ",")) list.add(key);
        prefs.edit().putString("hidden_charts", join(list, ",")).apply();
    }

    private void addChartIfVisible(String key, String title, String sub, View chart) {
        if (!isChartHidden(key)) addChartCard(title, sub, chart);
    }

    private void addBanksSection(JSONArray banks) {
        LinearLayout c = card();
        c.addView(text("موجودی بانک‌ها", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("مانده بانک به‌همراه ورودی/خروجی چک‌های متصل به حساب", 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        if (banks == null || banks.length() == 0) {
            TextView empty = text("داده بانکی قابل نمایش نیست.", 12, MUTED, Typeface.NORMAL);
            empty.setGravity(Gravity.CENTER);
            c.addView(empty, new LinearLayout.LayoutParams(-1, dp(70)));
        } else {
            for (int i = 0; i < Math.min(10, banks.length()); i++) {
                JSONObject b = banks.optJSONObject(i);
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.VERTICAL);
                row.setPadding(dp(10), dp(10), dp(10), dp(10));
                row.setBackground(roundedStroke(SURFACE_2, 16, alpha(Color.WHITE, 22)));
                row.addView(text(b.optString("label", "بانک"), 13, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
                row.addView(text("مانده: " + money(b.opt("balance")) + "   |   ورودی: " + money(b.opt("inflow")) + "   |   خروجی: " + money(b.opt("outflow")), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
                LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
                rp.setMargins(0, dp(8), 0, 0);
                c.addView(row, rp);
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void addDailyReportLaunchers(String latestSales, String latestPurchase) {
        LinearLayout c = card();
        c.addView(text("گزارش روزانه فروش و خرید", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("تاریخ را وارد کنید؛ اگر خالی باشد آخرین روز ثبت‌شده واقعی در Atiran استفاده می‌شود.", 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));

        EditText salesDate = input(latestSales == null || latestSales.isEmpty() ? "تاریخ فروش، مثلا 1403/01/01" : latestSales, "", false);
        Button sales = primaryButton("گزارش روزانه فروش");
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1, dp(50)); ip.setMargins(0, dp(12), 0, dp(8));
        c.addView(salesDate, ip);
        c.addView(sales, new LinearLayout.LayoutParams(-1, dp(50)));
        sales.setOnClickListener(v -> showDailyReportPage("sales", salesDate.getText().toString().trim()));

        EditText purchaseDate = input(latestPurchase == null || latestPurchase.isEmpty() ? "تاریخ خرید، مثلا 1403/01/01" : latestPurchase, "", false);
        Button purchase = secondaryButton("گزارش روزانه خرید");
        LinearLayout.LayoutParams ip2 = new LinearLayout.LayoutParams(-1, dp(50)); ip2.setMargins(0, dp(12), 0, dp(8));
        c.addView(purchaseDate, ip2);
        c.addView(purchase, new LinearLayout.LayoutParams(-1, dp(50)));
        purchase.setOnClickListener(v -> showDailyReportPage("purchase", purchaseDate.getText().toString().trim()));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void showDailyReportPage(String type, String date) {
        content.removeAllViews();
        addHero(type.equals("sales") ? "گزارش روزانه فروش" : "گزارش روزانه خرید", "در حال آماده‌سازی گزارش واقعی روزانه از Atiran");
        addLoading(content, "در حال دریافت گزارش روزانه…");
        runDb(() -> queryDailyReport(type, date), new DbCallback() {
            @Override public void ok(String body) {
                try { renderDailyReport(new JSONObject(body)); }
                catch (Exception e) { showPageError("گزارش روزانه", e, () -> showDailyReportPage(type, date)); }
            }
            @Override public void fail(Exception e) { showPageError("گزارش روزانه", e, () -> showDailyReportPage(type, date)); }
        });
    }

    private void renderDailyReport(JSONObject r) {
        content.removeAllViews();
        String type = r.optString("type", "sales");
        addHero(type.equals("sales") ? "گزارش روزانه فروش" : "گزارش روزانه خرید", "تاریخ: " + r.optString("date", "—"));
        Button back = secondaryButton("بازگشت به گزارش‌های مدیریتی");
        back.setOnClickListener(v -> showApp("reports"));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(12));
        content.addView(back, bp);

        JSONArray metrics = r.optJSONArray("metrics");
        if (metrics != null) {
            LinearLayout row = null;
            for (int i = 0; i < metrics.length(); i++) {
                if (i % 2 == 0) { row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); content.addView(row, new LinearLayout.LayoutParams(-1, -2)); }
                JSONObject m = metrics.optJSONObject(i);
                LinearLayout mc = metric(m.optString("label"), m.optString("value"));
                LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(0, -2, 1f); mlp.setMargins(dp(4), dp(4), dp(4), dp(8));
                if (row != null) row.addView(mc, mlp);
            }
        }

        addDailyRows("فهرست اسناد", r.optJSONArray("documents"), true);
        addDailyRows("سرجمع اقلام", r.optJSONArray("items"), false);
    }

    private void addDailyRows(String title, JSONArray rows, boolean documents) {
        LinearLayout c = card();
        c.addView(text(title, 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        if (rows == null || rows.length() == 0) {
            TextView empty = text("رکوردی برای نمایش وجود ندارد.", 12, MUTED, Typeface.NORMAL);
            empty.setGravity(Gravity.CENTER);
            c.addView(empty, new LinearLayout.LayoutParams(-1, dp(70)));
        } else {
            for (int i = 0; i < Math.min(80, rows.length()); i++) {
                JSONObject row = rows.optJSONObject(i);
                LinearLayout item = new LinearLayout(this);
                item.setOrientation(LinearLayout.VERTICAL);
                item.setPadding(dp(10), dp(10), dp(10), dp(10));
                item.setBackground(roundedStroke(SURFACE_2, 15, alpha(Color.WHITE, 22)));
                if (documents) {
                    item.addView(text("شماره سند: " + row.optString("number", "—") + "   |   " + row.optString("party", "بدون نام"), 12.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
                    item.addView(text("مبلغ: " + money(row.opt("amount")) + "   |   اقلام: " + formatNumber(row.opt("items")), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
                    if (!row.optString("description", "").isEmpty()) item.addView(text("توضیحات: " + row.optString("description"), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
                } else {
                    item.addView(text(row.optString("item", "بدون نام"), 12.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
                    item.addView(text("مقدار: " + formatNumber(row.opt("quantity")) + "   |   مبلغ: " + money(row.opt("amount")), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
                }
                LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(-1, -2); ilp.setMargins(0, dp(8), 0, 0);
                c.addView(item, ilp);
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private String queryAnalytics() throws Exception {
        try (Connection c = openConnection()) {
            JSONObject a = new JSONObject();
            JSONArray monthly = loadMonthlyPurchaseSales(c);
            JSONArray profit = loadMonthlyProfit(c);
            a.put("weeklySales", loadWeeklySales(c));
            a.put("monthlyPurchaseSales", monthly);
            a.put("checkStatuses", loadCheckStatuses(c));
            a.put("topCustomers", loadTopCustomers(c));
            a.put("debtAging", loadDebtAging(c));
            a.put("monthlyProfit", profit);
            a.put("banks", loadBanks(c));
            a.put("categoryShare", loadCategoryShare(c));
            a.put("customerGrowth", loadCustomerGrowth(c));
            a.put("netMargin", loadNetMargin(profit, monthly));
            a.put("latestSalesDate", latestDate(c, "sailfact", "date"));
            a.put("latestPurchaseDate", latestDate(c, "buyfact", "DATE"));
            return a.toString();
        }
    }

    private JSONArray loadWeeklySales(Connection c) throws Exception {
        Set<String> cols = columns(c, "sailfact");
        if (!hasCol(cols, "date") || !hasCol(cols, "all")) return new JSONArray();
        String where = activeWhere(cols, "s");
        List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && hasCol(cols, "vis_rdf")) { where = appendWhere(where, "TRY_CONVERT(int,s.[vis_rdf])=?"); params.add(session.visitorId); }
        if (hasFunction(c, "dif_date_alan")) {
            String sql = "WITH x AS (SELECT TRY_CONVERT(int,-dbo.dif_date_alan(s.[date])) age_days, TRY_CONVERT(decimal(19,2),s.[all]) amount FROM dbo.sailfact s " + where + "), " +
                    "b AS (SELECT (age_days/7) week_index, SUM(amount) total FROM x WHERE age_days BETWEEN 0 AND 55 GROUP BY (age_days/7)) " +
                    "SELECT TOP (8) CASE WHEN week_index=0 THEN N'هفته جاری' ELSE N'هفته ' + CONVERT(nvarchar(10),week_index+1) END, ISNULL(total,0) FROM b ORDER BY week_index DESC";
            return reverse(readPoints(c, sql, params));
        }
        return loadMonthlyMoney(c, "sailfact", "date", "all", true);
    }

    private JSONArray loadMonthlyPurchaseSales(Connection c) throws Exception {
        JSONArray sales = loadMonthlyMoney(c, "sailfact", "date", "all", true);
        JSONArray purchases = loadMonthlyMoney(c, "buyfact", "DATE", "all", false);
        Map<String, Double> sMap = pointsMap(sales, "value");
        Map<String, Double> pMap = pointsMap(purchases, "value");
        TreeSet<String> labels = new TreeSet<>(); labels.addAll(sMap.keySet()); labels.addAll(pMap.keySet());
        while (labels.size() > 12) labels.pollFirst();
        JSONArray out = new JSONArray();
        for (String label : labels) {
            JSONObject o = new JSONObject(); o.put("label", label); o.put("value", sMap.containsKey(label) ? sMap.get(label) : 0); o.put("secondaryValue", pMap.containsKey(label) ? pMap.get(label) : 0); out.put(o);
        }
        return out;
    }

    private JSONArray loadMonthlyMoney(Connection c, String table, String dateColumn, String amountColumn, boolean visitorAware) throws Exception {
        Set<String> cols = columns(c, table);
        String d = resolve(cols, dateColumn);
        String amount = resolve(cols, amountColumn);
        if (d == null || amount == null) return new JSONArray();
        String alias = "x";
        String where = activeWhere(cols, alias);
        List<Object> params = new ArrayList<>();
        if (visitorAware && session != null && session.visitorId != null && hasCol(cols, "vis_rdf")) { where = appendWhere(where, "TRY_CONVERT(int," + alias + ".[vis_rdf])=?"); params.add(session.visitorId); }
        String sql = "SELECT TOP (12) LEFT(" + alias + ".[" + d + "],7), ISNULL(SUM(TRY_CONVERT(decimal(19,2)," + alias + ".[" + amount + "])),0) FROM dbo.[" + table + "] " + alias + " " + where + " GROUP BY LEFT(" + alias + ".[" + d + "],7) ORDER BY LEFT(" + alias + ".[" + d + "],7) DESC";
        return reverse(readPoints(c, sql, params));
    }

    private JSONArray loadCheckStatuses(Connection c) throws Exception {
        JSONArray out = new JSONArray();
        Set<String> getCols = columns(c, "getchk");
        Set<String> putCols = columns(c, "putchk");
        Set<String> typeCols = columns(c, "CheckTypes");
        if (hasCol(getCols, "chk_satus") && hasCol(getCols, "getchkmab")) {
            String typeJoin = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? " LEFT JOIN dbo.CheckTypes t ON t.ID=g.chk_satus " : "";
            String label = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? "N'دریافتی • ' + COALESCE(TRY_CONVERT(nvarchar(100),t.Desciption),N'وضعیت '+CONVERT(nvarchar(20),g.chk_satus))" : "N'دریافتی • وضعیت ' + CONVERT(nvarchar(20),g.chk_satus)";
            String where = ""; List<Object> params = new ArrayList<>();
            if (session != null && session.visitorId != null && hasCol(getCols, "vis_rdf")) { where = " WHERE TRY_CONVERT(int,g.vis_rdf)=?"; params.add(session.visitorId); }
            String sql = "SELECT " + label + ", COUNT_BIG(1), ISNULL(SUM(TRY_CONVERT(decimal(19,2),g.[getchkmab])),0) FROM dbo.getchk g " + typeJoin + where + " GROUP BY " + label;
            appendCheckPoints(out, c, sql, params);
        }
        if (hasCol(putCols, "putchk_status") && hasCol(putCols, "putchkmab")) {
            String typeJoin = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? " LEFT JOIN dbo.CheckTypes t ON t.ID=p.putchk_status " : "";
            String label = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? "N'پرداختی • ' + COALESCE(TRY_CONVERT(nvarchar(100),t.Desciption),N'وضعیت '+CONVERT(nvarchar(20),p.putchk_status))" : "N'پرداختی • وضعیت ' + CONVERT(nvarchar(20),p.putchk_status)";
            String sql = "SELECT " + label + ", COUNT_BIG(1), ISNULL(SUM(TRY_CONVERT(decimal(19,2),p.[putchkmab])),0) FROM dbo.putchk p " + typeJoin + " GROUP BY " + label;
            appendCheckPoints(out, c, sql, new ArrayList<>());
        }
        return out;
    }

    private JSONArray loadTopCustomers(Connection c) throws Exception {
        Set<String> sail = columns(c, "sailfact");
        Set<String> cust = columns(c, "CUSTOMERS");
        if (!hasCol(sail, "shmo") || !hasCol(sail, "all") || !hasCol(cust, "SHMO")) return new JSONArray();
        String nameExpr = hasCol(cust, "MONAME") ? "TRY_CONVERT(nvarchar(250),c.MONAME)" : "TRY_CONVERT(nvarchar(100),c.SHMO)";
        String where = activeWhere(sail, "s");
        List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && hasCol(sail, "vis_rdf")) { where = appendWhere(where, "TRY_CONVERT(int,s.vis_rdf)=?"); params.add(session.visitorId); }
        String sql = "SELECT TOP (10) " + nameExpr + " AS label, ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[all])),0) AS value FROM dbo.CUSTOMERS c JOIN dbo.sailfact s ON s.shmo=c.SHMO " + where + " GROUP BY c.SHMO," + nameExpr + " ORDER BY value DESC";
        return readPoints(c, sql, params);
    }

    private JSONArray loadDebtAging(Connection c) throws Exception {
        Set<String> cols = columns(c, "sailfact");
        if (!hasCol(cols, "t_date") || !hasCol(cols, "all") || !hasCol(cols, "tasvieh") || !hasFunction(c, "dif_date_alan")) return new JSONArray();
        String amount = hasCol(cols, "all_fel") ? "TRY_CONVERT(decimal(19,2),[all_fel])" : "TRY_CONVERT(decimal(19,2),[all])";
        String remain = amount;
        if (hasCol(cols, "MabDaryaftFactor")) remain += " - ISNULL(TRY_CONVERT(decimal(19,2),[MabDaryaftFactor]),0)";
        if (hasCol(cols, "tdf")) remain += " - ISNULL(TRY_CONVERT(decimal(19,2),[tdf]),0)";
        String where = "WHERE [tasvieh]='f' AND NULLIF([t_date],'') IS NOT NULL";
        if (hasCol(cols, "active")) where += " AND [active]='t'";
        List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && hasCol(cols, "vis_rdf")) { where += " AND TRY_CONVERT(int,[vis_rdf])=?"; params.add(session.visitorId); }
        String bucket = "CASE WHEN dbo.dif_date_alan([t_date]) >= 0 THEN N'۰ / جاری' WHEN -dbo.dif_date_alan([t_date]) <= 30 THEN N'۱ تا ۳۰ روز' WHEN -dbo.dif_date_alan([t_date]) <= 60 THEN N'۳۱ تا ۶۰ روز' WHEN -dbo.dif_date_alan([t_date]) <= 90 THEN N'۶۱ تا ۹۰ روز' WHEN -dbo.dif_date_alan([t_date]) <= 180 THEN N'۹۱ تا ۱۸۰ روز' ELSE N'۱۸۰+ روز' END";
        String sql = "WITH x AS (SELECT " + bucket + " bucket, (" + remain + ") amount FROM dbo.sailfact " + where + ") SELECT bucket, SUM(CASE WHEN amount>0 THEN amount ELSE 0 END) FROM x GROUP BY bucket";
        return readPoints(c, sql, params);
    }

    private JSONArray loadMonthlyProfit(Connection c) throws Exception {
        Set<String> sail = columns(c, "sailfact");
        Set<String> detail = columns(c, "subsailfact");
        Set<String> inv = columns(c, "inventory");
        String cost = resolve(inv, "pure_buy_price", "BuyPrice", "buy_price", "LastBuyPrice");
        if (!hasCol(sail, "date") || !hasCol(sail, "shfacfo") || !hasCol(detail, "shfacfo") || !hasCol(detail, "SHKA") || !hasCol(detail, "LINESUM") || !hasCol(inv, "shka") || cost == null) return new JSONArray();
        if (!hasCol(detail, "TEDVAH") && !hasCol(detail, "TEDJOZ")) return new JSONArray();
        String tedvah = hasCol(detail, "TEDVAH") ? "ISNULL(TRY_CONVERT(decimal(19,4),d.TEDVAH),0)" : "0";
        String tedjoz = hasCol(detail, "TEDJOZ") ? "ISNULL(TRY_CONVERT(decimal(19,4),d.TEDJOZ),0)" : "0";
        String qty = hasCol(inv, "mohvah") ? "(" + tedvah + "*NULLIF(TRY_CONVERT(decimal(19,4),i.mohvah),0)+" + tedjoz + ")" : "(" + tedvah + "+" + tedjoz + ")";
        String where = activeWhere(sail, "s");
        if (hasCol(detail, "active")) where = appendWhere(where, "d.active='t'");
        List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && hasCol(sail, "vis_rdf")) { where = appendWhere(where, "TRY_CONVERT(int,s.vis_rdf)=?"); params.add(session.visitorId); }
        String sql = "SELECT TOP (12) LEFT(s.[date],7), ISNULL(SUM(TRY_CONVERT(decimal(19,2),d.[LINESUM]) - (" + qty + " * ISNULL(TRY_CONVERT(decimal(19,2),i.[" + cost + "]),0))),0) FROM dbo.sailfact s JOIN dbo.subsailfact d ON d.shfacfo=s.shfacfo JOIN dbo.inventory i ON i.shka=d.SHKA " + where + " GROUP BY LEFT(s.[date],7) ORDER BY LEFT(s.[date],7) DESC";
        return reverse(readPoints(c, sql, params));
    }

    private JSONArray loadBanks(Connection c) throws Exception {
        JSONArray out = new JSONArray();
        Set<String> bank = columns(c, "BANK");
        Set<String> get = columns(c, "getchk");
        Set<String> put = columns(c, "putchk");
        if (!hasCol(bank, "RDF") || !hasCol(bank, "BANKNAME") || !hasCol(bank, "MAN")) return out;
        String inflow = hasCol(get, "our_bankrdf") && hasCol(get, "getchkmab") ? "ISNULL((SELECT SUM(TRY_CONVERT(decimal(19,2),g.getchkmab)) FROM dbo.getchk g WHERE g.our_bankrdf=b.RDF),0)" : "CAST(0 AS decimal(19,2))";
        String outflow = hasCol(put, "bankrdf") && hasCol(put, "putchkmab") ? "ISNULL((SELECT SUM(TRY_CONVERT(decimal(19,2),p.putchkmab)) FROM dbo.putchk p WHERE p.bankrdf=b.RDF),0)" : "CAST(0 AS decimal(19,2))";
        String where = hasCol(bank, "Active") ? " WHERE ISNULL(b.Active,1)=1" : "";
        String sql = "SELECT TOP (20) TRY_CONVERT(nvarchar(250),b.BANKNAME), ISNULL(TRY_CONVERT(decimal(19,2),b.MAN),0), " + inflow + ", " + outflow + " FROM dbo.BANK b " + where + " ORDER BY ISNULL(TRY_CONVERT(decimal(19,2),b.MAN),0) DESC";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) { JSONObject o = new JSONObject(); o.put("label", stringOr(r.getString(1), "بدون نام")); o.put("balance", r.getDouble(2)); o.put("inflow", r.getDouble(3)); o.put("outflow", r.getDouble(4)); o.put("value", r.getDouble(2)); out.put(o); }
        }
        return out;
    }

    private JSONArray loadCategoryShare(Connection c) throws Exception {
        Set<String> sail = columns(c, "sailfact");
        Set<String> detail = columns(c, "subsailfact");
        Set<String> inv = columns(c, "inventory");
        Set<String> grp = columns(c, "kagroup");
        String invGroup = resolve(inv, "group_rdf", "GroupID", "VarietyID", "variety_rdf");
        String groupKey = resolve(grp, "group_rdf", "ID", "GroupID", "rdf", "code");
        String groupName = resolve(grp, "group_name", "name", "Name", "GroupName", "nagr", "gname");
        if (hasCol(sail, "shfacfo") && hasCol(detail, "shfacfo") && hasCol(detail, "SHKA") && hasCol(detail, "LINESUM") && hasCol(inv, "shka") && invGroup != null && groupKey != null && groupName != null) {
            String where = activeWhere(sail, "s");
            if (hasCol(detail, "active")) where = appendWhere(where, "d.active='t'");
            List<Object> params = new ArrayList<>();
            if (session != null && session.visitorId != null && hasCol(sail, "vis_rdf")) { where = appendWhere(where, "TRY_CONVERT(int,s.vis_rdf)=?"); params.add(session.visitorId); }
            String sql = "SELECT TOP (8) COALESCE(TRY_CONVERT(nvarchar(250),g.[" + groupName + "]),N'بدون گروه'), ISNULL(SUM(TRY_CONVERT(decimal(19,2),d.LINESUM)),0) FROM dbo.sailfact s JOIN dbo.subsailfact d ON d.shfacfo=s.shfacfo JOIN dbo.inventory i ON i.shka=d.SHKA LEFT JOIN dbo.kagroup g ON TRY_CONVERT(nvarchar(100),g.[" + groupKey + "])=TRY_CONVERT(nvarchar(100),i.[" + invGroup + "]) " + where + " GROUP BY g.[" + groupName + "] ORDER BY 2 DESC";
            return readPoints(c, sql, params);
        }
        return new JSONArray();
    }

    private JSONArray loadCustomerGrowth(Connection c) throws Exception {
        Set<String> cols = columns(c, "sailfact");
        if (!hasCol(cols, "date") || !hasCol(cols, "shmo")) return new JSONArray();
        String where = activeWhere(cols, "s"); List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && hasCol(cols, "vis_rdf")) { where = appendWhere(where, "TRY_CONVERT(int,s.vis_rdf)=?"); params.add(session.visitorId); }
        String sql = "SELECT TOP (12) LEFT(s.[date],7), COUNT(DISTINCT s.[shmo]) FROM dbo.sailfact s " + where + " GROUP BY LEFT(s.[date],7) ORDER BY LEFT(s.[date],7) DESC";
        return reverse(readPoints(c, sql, params));
    }

    private JSONArray loadNetMargin(JSONArray profit, JSONArray monthly) throws Exception {
        Map<String, Double> pMap = pointsMap(profit, "value");
        Map<String, Double> sMap = pointsMap(monthly, "value");
        TreeSet<String> labels = new TreeSet<>(); labels.addAll(pMap.keySet());
        JSONArray out = new JSONArray();
        for (String label : labels) { double sales = sMap.containsKey(label) ? sMap.get(label) : 0; JSONObject o = new JSONObject(); o.put("label", label); o.put("value", sales == 0 ? 0 : (pMap.get(label) / sales) * 100.0); out.put(o); }
        return out;
    }

    private String queryDailyReport(String type, String requestedDate) throws Exception {
        try (Connection c = openConnection()) {
            boolean sales = type.equals("sales");
            String header = sales ? "sailfact" : "buyfact";
            String detailTable = sales ? "subsailfact" : "subbuyfact";
            Set<String> h = columns(c, header); Set<String> d = columns(c, detailTable); Set<String> cust = columns(c, "CUSTOMERS"); Set<String> inv = columns(c, "inventory");
            String dateCol = sales ? resolve(h, "date") : resolve(h, "DATE", "date");
            String numberCol = sales ? resolve(h, "shfacfo") : resolve(h, "shfackh");
            String detailNumber = sales ? resolve(d, "shfacfo") : resolve(d, "shfackh");
            String amountCol = resolve(h, "all");
            String actualDate = requestedDate == null || requestedDate.trim().isEmpty() ? latestDate(c, header, dateCol) : requestedDate.trim();
            JSONObject out = new JSONObject(); out.put("type", type); out.put("date", actualDate == null ? "" : actualDate);
            JSONArray docs = new JSONArray(); JSONArray items = new JSONArray(); double total = 0; long docCount = 0; long partyCount = 0; long itemCount = 0;
            if (actualDate != null && dateCol != null && numberCol != null && amountCol != null) {
                String headerParty = hasCol(h, "moname") ? "TRY_CONVERT(nvarchar(250),h.moname)" : "N'بدون نام'";
                boolean canJoinCustomer = hasCol(cust, "SHMO") && hasCol(h, "shmo");
                String partyExpr = canJoinCustomer && hasCol(cust, "MONAME") ? "COALESCE(TRY_CONVERT(nvarchar(250),c.MONAME)," + headerParty + ",N'بدون نام')" : "COALESCE(" + headerParty + ",N'بدون نام')";
                String descExpr = sales ? (hasCol(h, "description") ? "TRY_CONVERT(nvarchar(500),h.description)" : "CAST(NULL AS nvarchar(500))") : (hasCol(h, "Explain") ? "TRY_CONVERT(nvarchar(500),h.[Explain])" : "CAST(NULL AS nvarchar(500))");
                String itemApply = detailNumber == null ? "" : " OUTER APPLY (SELECT COUNT_BIG(1) item_count FROM dbo.[" + detailTable + "] dd WHERE dd.[" + detailNumber + "]=h.[" + numberCol + "]" + (hasCol(d, "active") ? " AND dd.active='t'" : "") + ") ic ";
                String itemCountExpr = detailNumber == null ? "CAST(0 AS bigint)" : "ISNULL(ic.item_count,0)";
                String where = "WHERE h.[" + dateCol + "]=?"; if (hasCol(h, "active")) where += " AND h.active='t'"; List<Object> params = new ArrayList<>(); params.add(actualDate);
                if (sales && session != null && session.visitorId != null && hasCol(h, "vis_rdf")) { where += " AND TRY_CONVERT(int,h.vis_rdf)=?"; params.add(session.visitorId); }
                String sql = "SELECT TOP (150) TRY_CONVERT(bigint,h.[" + numberCol + "]), " + partyExpr + ", TRY_CONVERT(decimal(19,2),h.[" + amountCol + "]), " + itemCountExpr + ", " + descExpr + " FROM dbo.[" + header + "] h " + (canJoinCustomer ? "LEFT JOIN dbo.CUSTOMERS c ON c.SHMO=h.shmo " : "") + itemApply + where + " ORDER BY h.[" + numberCol + "]";
                try (PreparedStatement ps = c.prepareStatement(sql)) { setParams(ps, params); try (ResultSet r = ps.executeQuery()) { Set<String> parties = new HashSet<>(); while (r.next()) { JSONObject o = new JSONObject(); o.put("number", r.getLong(1)); String party = stringOr(r.getString(2), "بدون نام"); o.put("party", party); o.put("amount", r.getDouble(3)); o.put("items", r.getLong(4)); o.put("description", stringOr(r.getString(5), "")); docs.put(o); total += r.getDouble(3); docCount++; parties.add(party); itemCount += r.getLong(4); } partyCount = parties.size(); } }
                if (detailNumber != null && hasCol(d, sales ? "SHKA" : "shka")) {
                    String key = sales ? resolve(d, "SHKA") : resolve(d, "shka", "SHKA");
                    String lineAmount = sales ? resolve(d, "LINESUM") : resolve(d, "tamam_joz", "LINESUM");
                    String detailName = sales ? resolve(d, "naka", "name", "Desc_Naka") : resolve(d, "Desc_Naka", "naka", "name");
                    String invName = resolve(inv, "naka", "Name", "KalaName");
                    String itemName = detailName != null && invName != null ? "COALESCE(NULLIF(TRY_CONVERT(nvarchar(500),dd.[" + detailName + "]),N''),TRY_CONVERT(nvarchar(500),i.[" + invName + "]),N'بدون نام')" : (detailName != null ? "COALESCE(NULLIF(TRY_CONVERT(nvarchar(500),dd.[" + detailName + "]),N''),N'بدون نام')" : (invName != null ? "COALESCE(TRY_CONVERT(nvarchar(500),i.[" + invName + "]),N'بدون نام')" : "N'بدون نام'"));
                    if (key != null && lineAmount != null) {
                        String tedvah = hasCol(d, "TEDVAH") ? "ISNULL(TRY_CONVERT(decimal(19,4),dd.TEDVAH),0)" : "0";
                        String tedjoz = hasCol(d, "TEDJOZ") ? "ISNULL(TRY_CONVERT(decimal(19,4),dd.TEDJOZ),0)" : "0";
                        String qty = hasCol(inv, "mohvah") ? "ISNULL(SUM(" + tedvah + "*ISNULL(TRY_CONVERT(decimal(19,4),i.mohvah),1)+" + tedjoz + "),0)" : "ISNULL(SUM(" + tedvah + "+" + tedjoz + "),0)";
                        String itemSql = "SELECT TOP (120) " + itemName + ", " + qty + ", ISNULL(SUM(TRY_CONVERT(decimal(19,2),dd.[" + lineAmount + "])),0) FROM dbo.[" + detailTable + "] dd JOIN dbo.[" + header + "] h ON h.[" + numberCol + "]=dd.[" + detailNumber + "] LEFT JOIN dbo.inventory i ON i.shka=dd.[" + key + "] " + where + (hasCol(d, "active") ? " AND dd.active='t'" : "") + " GROUP BY " + itemName + " ORDER BY 3 DESC";
                        try (PreparedStatement ps = c.prepareStatement(itemSql)) { setParams(ps, params); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("item", stringOr(r.getString(1), "بدون نام")); o.put("quantity", r.getDouble(2)); o.put("amount", r.getDouble(3)); items.put(o); } } }
                    }
                }
            }
            JSONObject finance = dailyFinance(c, header, h, dateCol, actualDate, sales);
            JSONArray metrics = new JSONArray();
            addMetric(metrics, sales ? "مبلغ کل فروش" : "مبلغ کل خرید", money(total));
            addMetric(metrics, "تعداد اسناد", formatNumber(docCount));
            addMetric(metrics, sales ? "تعداد مشتریان" : "تعداد طرف‌حساب‌ها", formatNumber(partyCount));
            addMetric(metrics, "سرجمع اقلام", formatNumber(itemCount));
            addMetric(metrics, "تخفیف روز", money(finance.opt("discount")));
            addMetric(metrics, "مالیات روز", money(finance.opt("tax")));
            addMetric(metrics, sales ? "دریافت ثبت‌شده" : "پرداخت ثبت‌شده", money(finance.opt("paid")));
            out.put("metrics", metrics); out.put("documents", docs); out.put("items", items); return out.toString();
        }
    }
    private void appendCheckPoints(JSONArray out, Connection c, String sql, List<Object> params) throws Exception {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    JSONObject o = new JSONObject();
                    o.put("label", stringOr(r.getString(1), "وضعیت نامشخص"));
                    o.put("count", r.getLong(2));
                    o.put("amount", r.getDouble(3));
                    o.put("value", r.getDouble(3));
                    o.put("secondaryValue", r.getLong(2));
                    out.put(o);
                }
            }
        }
    }

    private Map<String, Double> pointsMap(JSONArray points, String field) {
        Map<String, Double> map = new HashMap<>();
        if (points == null) return map;
        for (int i = 0; i < points.length(); i++) {
            JSONObject o = points.optJSONObject(i);
            if (o != null) map.put(o.optString("label", ""), o.optDouble(field, 0));
        }
        return map;
    }

    private boolean hasFunction(Connection c, String name) {
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(1) FROM sys.objects WHERE type IN ('FN','IF','TF') AND name=?")) {
            ps.setString(1, name);
            try (ResultSet r = ps.executeQuery()) { return r.next() && r.getLong(1) > 0; }
        } catch (Exception ignored) { return false; }
    }

    private String latestDate(Connection c, String table, String preferredColumn) {
        try {
            Set<String> cols = columns(c, table);
            String dateCol = preferredColumn == null ? null : resolve(cols, preferredColumn);
            if (dateCol == null) dateCol = resolve(cols, "date", "DATE", "tarikh", "Date");
            if (dateCol == null) return "";
            try (PreparedStatement ps = c.prepareStatement("SELECT MAX(NULLIF(CONVERT(nvarchar(20),[" + dateCol + "]),N'')) FROM dbo.[" + table + "]")) {
                try (ResultSet r = ps.executeQuery()) { return r.next() && r.getString(1) != null ? r.getString(1) : ""; }
            }
        } catch (Exception ignored) { return ""; }
    }

    private JSONObject dailyFinance(Connection c, String table, Set<String> cols, String dateCol, String date, boolean sales) throws Exception {
        JSONObject o = new JSONObject();
        o.put("discount", 0); o.put("tax", 0); o.put("paid", 0);
        if (date == null || date.isEmpty() || dateCol == null) return o;
        String discount = resolve(cols, "tafif", "takhfif", "discount");
        String tax = resolve(cols, "tax", "Tax", "maliat");
        String paid = sales ? resolve(cols, "MabDaryaftFactor", "received", "Daryaft") : resolve(cols, "MablaghPardakht", "paid", "Pardakht");
        String sd = discount == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + discount + "])),0)";
        String st = tax == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + tax + "])),0)";
        String sp = paid == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + paid + "])),0)";
        String where = "WHERE [" + dateCol + "]=?";
        if (hasCol(cols, "active")) where += " AND [active]='t'";
        List<Object> params = new ArrayList<>(); params.add(date);
        if (sales && session != null && session.visitorId != null && hasCol(cols, "vis_rdf")) { where += " AND TRY_CONVERT(int,[vis_rdf])=?"; params.add(session.visitorId); }
        try (PreparedStatement ps = c.prepareStatement("SELECT " + sd + "," + st + "," + sp + " FROM dbo.[" + table + "] " + where)) {
            setParams(ps, params);
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) { o.put("discount", r.getDouble(1)); o.put("tax", r.getDouble(2)); o.put("paid", r.getDouble(3)); }
            }
        }
        return o;
    }

    private void addMetric(JSONArray metrics, String label, String value) throws Exception {
        JSONObject o = new JSONObject(); o.put("label", label); o.put("value", value); metrics.put(o);
    }

    private JSONArray readPoints(Connection c, String sql, List<Object> params) throws Exception {
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    JSONObject p = new JSONObject();
                    p.put("label", stringOr(r.getString(1), "-"));
                    p.put("value", r.getDouble(2));
                    arr.put(p);
                }
            }
        }
        return arr;
    }

    private JSONArray reverse(JSONArray in) throws Exception {
        JSONArray out = new JSONArray();
        for (int i = in.length() - 1; i >= 0; i--) out.put(in.get(i));
        return out;
    }

    private void addChartCard(String title, String sub, View chart) {
        LinearLayout c = card();
        c.addView(text(title, 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text(sub, 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, dp(230));
        cp.setMargins(0, dp(12), 0, 0);
        c.addView(chart, cp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void renderSettings() {
        content.removeAllViews();
        addHero("تنظیمات", "مدیریت اتصال و خروج امن");
        LinearLayout connection = card();
        connection.addView(text("وضعیت", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        connection.addView(text("اتصال مستقیم آماده است.", 12, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        connection.addView(text("کاربر: " + (session == null ? "-" : session.userName), 12, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        Button retry = secondaryButton("تست اتصال مجدد");
        retry.setOnClickListener(v -> refreshActivePage());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, dp(50));
        rp.setMargins(0, dp(14), 0, 0);
        connection.addView(retry, rp);
        Button logout = primaryButton("خروج");
        logout.setOnClickListener(v -> showLogin("برای ورود مجدد اطلاعات Atiran را وارد کنید."));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(50));
        lp.setMargins(0, dp(10), 0, 0);
        connection.addView(logout, lp);
        content.addView(connection, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout about = card();
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2);
        ap.setMargins(0, dp(12), 0, 0);
        about.addView(text("درباره نسخه", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView desc = text("MEELANO Android Direct SQL v3.0.0\nاین نسخه برای تست شخصی با اتصال مستقیم به SQL Server ساخته شده است. جزئیات اتصال در UI نمایش داده نمی‌شود و کاربر فقط با حساب Atiran وارد می‌شود.", 12, MUTED, Typeface.NORMAL);
        desc.setLineSpacing(dp(3), 1.05f);
        about.addView(desc, new LinearLayout.LayoutParams(-1, -2));
        content.addView(about, ap);
    }

    private Set<String> columns(Connection c, String table) throws Exception {
        Set<String> set = new HashSet<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT c.name FROM sys.columns c JOIN sys.tables t ON t.object_id=c.object_id JOIN sys.schemas s ON s.schema_id=t.schema_id WHERE s.name=N'dbo' AND t.name=? ORDER BY c.column_id")) {
            ps.setString(1, table);
            try (ResultSet r = ps.executeQuery()) { while (r.next()) set.add(r.getString(1)); }
        }
        return set;
    }

    private boolean hasCol(Set<String> cols, String candidate) {
        if (cols == null || candidate == null) return false;
        for (String c : cols) if (c != null && c.equalsIgnoreCase(candidate)) return true;
        return false;
    }

    private String resolve(Set<String> cols, String... candidates) {
        if (cols == null || candidates == null) return null;
        for (String candidate : candidates) {
            if (candidate == null) continue;
            for (String col : cols) if (col != null && col.equalsIgnoreCase(candidate)) return col;
        }
        return null;
    }

    private String activeWhere(Set<String> cols, String alias) {
        String active = resolve(cols, "active", "Active");
        if (active != null) return "WHERE (" + alias + ".[" + active + "]='t' OR " + alias + ".[" + active + "]='1' OR " + alias + ".[" + active + "]='Y' OR " + alias + ".[" + active + "]=1)";
        return "";
    }

    private String appendWhere(String where, String clause) {
        if (where == null || where.trim().isEmpty()) return "WHERE " + clause;
        return where + " AND " + clause;
    }

    private JSONArray rowsToJson(ResultSet r) throws Exception {
        JSONArray arr = new JSONArray();
        ResultSetMetaData md = r.getMetaData();
        int n = md.getColumnCount();
        while (r.next()) {
            JSONObject o = new JSONObject();
            for (int i = 1; i <= n; i++) {
                Object v = r.getObject(i);
                o.put(md.getColumnLabel(i), v == null ? JSONObject.NULL : String.valueOf(v));
            }
            arr.put(o);
        }
        return arr;
    }

    private void setParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
    }

    private boolean isSensitiveColumn(String col) {
        String s = col == null ? "" : col.toLowerCase(Locale.US);
        return s.contains("password") || s.contains("pass") || s.contains("token") || s.contains("secret") || s.contains("hash");
    }

    private String join(List<String> list, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(sep);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private String stringOr(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
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

    private String readableError(Exception e) {
        if (e instanceof DbException) return e.getMessage();
        String m = e == null ? "" : e.getMessage();
        if (m == null || m.trim().isEmpty()) return "اتصال برقرار نشد.";
        String lower = m.toLowerCase(Locale.US);
        if (lower.contains("network") || lower.contains("timed out") || lower.contains("connect") || lower.contains("login failed") || lower.contains("unknownhost")) {
            return "اتصال به سرور برقرار نشد. اینترنت، VPN، دسترسی شبکه یا روشن بودن سرور را بررسی کنید.";
        }
        return m;
    }

    private void addEmptyTo(LinearLayout parent, String message) {
        LinearLayout c = card();
        TextView t = text(message, 12.5f, MUTED, Typeface.NORMAL);
        t.setGravity(Gravity.CENTER);
        c.addView(t, new LinearLayout.LayoutParams(-1, -2));
        parent.addView(c, new LinearLayout.LayoutParams(-1, -2));
    }

    private static class UserSession {
        final Integer userId;
        final Integer visitorId;
        final String userName;
        UserSession(Integer userId, Integer visitorId, String userName) {
            this.userId = userId;
            this.visitorId = visitorId;
            this.userName = userName;
        }
    }

    private static class DbException extends Exception {
        DbException(String message) { super(message); }
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
                drawCentered(canvas, w, h, "داده‌ای برای نمودار وجود ندارد.");
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
                if (i == 0) { line.moveTo(x, y); area.moveTo(x, h - bottom); area.lineTo(x, y); }
                else { line.lineTo(x, y); area.lineTo(x, y); }
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
            canvas.drawText(data.optJSONObject(0).optString("label", ""), left + dp(22), h - dp(12), paint);
            canvas.drawText(data.optJSONObject(n - 1).optString("label", ""), w - right - dp(28), h - dp(12), paint);
        }
    }

    private class MultiBarChartView extends View {
        private final JSONArray data;
        private final int primary;
        private final int secondary;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        MultiBarChartView(Context context, JSONArray data, int primary, int secondary) {
            super(context);
            this.data = data;
            this.primary = primary;
            this.secondary = secondary;
            setBackground(roundedStroke(SURFACE_2, 18, alpha(Color.WHITE, 25)));
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();
            int left = dp(18), right = dp(18), top = dp(22), bottom = dp(44);
            if (data == null || data.length() == 0) {
                drawCentered(canvas, w, h, "داده‌ای برای نمودار وجود ندارد.");
                return;
            }
            int n = Math.min(data.length(), 12);
            double max = 1;
            for (int i = 0; i < n; i++) {
                JSONObject item = data.optJSONObject(i);
                max = Math.max(max, Math.abs(item == null ? 0 : item.optDouble("value", 0)));
                max = Math.max(max, Math.abs(item == null ? 0 : item.optDouble("secondaryValue", 0)));
            }
            paint.setStyle(Paint.Style.FILL);
            float gap = dp(7);
            float groupW = Math.max(dp(22), (w - left - right - gap * (n - 1)) / n);
            float barW = Math.max(dp(7), groupW / 2.5f);
            for (int i = 0; i < n; i++) {
                JSONObject item = data.optJSONObject(i);
                double v1 = item == null ? 0 : item.optDouble("value", 0);
                double v2 = item == null ? 0 : item.optDouble("secondaryValue", 0);
                float x = left + i * (groupW + gap);
                float h1 = (float) ((v1 / max) * (h - top - bottom));
                float h2 = (float) ((v2 / max) * (h - top - bottom));
                paint.setColor(alpha(primary, 225));
                canvas.drawRoundRect(new RectF(x, h - bottom - h1, x + barW, h - bottom), dp(8), dp(8), paint);
                paint.setColor(alpha(secondary, 225));
                canvas.drawRoundRect(new RectF(x + barW + dp(3), h - bottom - h2, x + barW * 2 + dp(3), h - bottom), dp(8), dp(8), paint);
                paint.setColor(MUTED);
                paint.setTextSize(dp(8.5f));
                paint.setTextAlign(Paint.Align.CENTER);
                String label = item == null ? "" : item.optString("label", "");
                if (label.length() > 7) label = label.substring(0, 7);
                canvas.drawText(label, x + groupW / 2, h - dp(14), paint);
            }
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(dp(10));
            paint.setColor(primary); canvas.drawCircle(dp(18), dp(15), dp(4), paint); paint.setColor(MUTED); canvas.drawText("فروش", dp(27), dp(18), paint);
            paint.setColor(secondary); canvas.drawCircle(dp(78), dp(15), dp(4), paint); paint.setColor(MUTED); canvas.drawText("خرید", dp(87), dp(18), paint);
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
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();
            int left = dp(18), right = dp(18), top = dp(20), bottom = dp(40);
            if (data == null || data.length() == 0) {
                drawCentered(canvas, w, h, "داده‌ای برای نمودار وجود ندارد.");
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
        if (session != null && !"dashboard".equals(activePage)) {
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
