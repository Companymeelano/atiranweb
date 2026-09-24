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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
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

        View.OnClickListener doLogin = v -> {
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
                        showLoginError(readableError(ex), () -> doLogin.onClick(login));
                    });
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
        try (Connection c = openConnection()) {
            String visitorSql = "SELECT TOP (1) v.vis_rdf, v.vis_name, v.UserID FROM dbo.visitors AS v " +
                    "WHERE v.Username=? AND v.Password=? AND (v.active='1' OR v.active='Y' OR v.active='y') ORDER BY v.vis_rdf";
            try (PreparedStatement ps = c.prepareStatement(visitorSql)) {
                ps.setString(1, atiranUser);
                ps.setString(2, atiranPassword);
                try (ResultSet r = ps.executeQuery()) {
                    if (r.next()) {
                        Integer uid = r.getObject(3) == null ? null : r.getInt(3);
                        return new UserSession(uid, r.getInt(1), stringOr(r.getString(2), atiranUser));
                    }
                }
            }

            String userSql = "SELECT TOP (1) u.user_id, u.user_name, sv.shvis FROM dbo.sys_users AS u " +
                    "LEFT JOIN dbo.sys_vis AS sv ON sv.UserID=u.user_id " +
                    "LEFT JOIN dbo.visitors AS v ON v.vis_rdf=sv.shvis " +
                    "WHERE u.user_name=? AND CONVERT(varchar(100),u.user_password)=? " +
                    "AND ISNULL(u.active,1)=1 AND ISNULL(u.IsLocked,0)=0 " +
                    "AND (v.active IS NULL OR v.active IN ('1','Y','y')) " +
                    "ORDER BY CASE WHEN sv.shvis IS NULL THEN 1 ELSE 0 END, sv.SysID";
            try (PreparedStatement ps = c.prepareStatement(userSql)) {
                ps.setString(1, atiranUser);
                ps.setString(2, atiranPassword);
                try (ResultSet r = ps.executeQuery()) {
                    if (r.next()) {
                        Integer visitor = r.getObject(3) == null ? null : r.getInt(3);
                        return new UserSession(r.getInt(1), visitor, stringOr(r.getString(2), atiranUser));
                    }
                }
            }
        }
        throw new DbException("نام کاربری یا رمز عبور Atiran معتبر نیست.");
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
            a.put("weeklySales", loadMonthlySales(c));
            a.put("topCustomers", loadTopCustomers(c));
            a.put("debtAging", loadDebtAging(c));
            a.put("categoryShare", loadCategoryShare(c));
            a.put("monthlyProfit", loadMonthlySales(c));
            out.put("kpis", kpis);
            out.put("analytics", a);
            return out.toString();
        }
    }

    private long countTable(Connection c, String table) throws Exception {
        if (!SAFE_TABLES.contains(table)) throw new DbException("جدول مجاز نیست.");
        Set<String> cols = columns(c, table);
        String where = "";
        if (session != null && session.visitorId != null && cols.contains("vis_rdf") &&
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
        addHero("گزارش‌های مدیریتی", "نمودارهای مستقیم از SQL Server");
        addLoading(content, "در حال دریافت گزارش‌ها…");
        runDb(this::queryAnalytics, new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONObject a = new JSONObject(body);
                    content.removeAllViews();
                    addHero("گزارش‌های مدیریتی", "نمودارهای مستقیم از SQL Server");
                    addChartCard("فروش", "روند فروش", new LineChartView(MainActivity.this, a.optJSONArray("weeklySales"), GOLD));
                    addChartCard("مشتریان برتر", "خریداران اصلی", new BarChartView(MainActivity.this, a.optJSONArray("topCustomers"), INFO));
                    addChartCard("سهم دسته‌های کالا", "گروه‌های پرفروش", new BarChartView(MainActivity.this, a.optJSONArray("categoryShare"), GOLD));
                    addChartCard("مانده مشتریان", "تحلیل مانده", new BarChartView(MainActivity.this, a.optJSONArray("debtAging"), WARNING));
                } catch (Exception e) { showPageError("گزارش‌ها", e, () -> showApp("reports")); }
            }
            @Override public void fail(Exception e) { showPageError("گزارش‌ها", e, () -> showApp("reports")); }
        });
    }

    private String queryAnalytics() throws Exception {
        try (Connection c = openConnection()) {
            JSONObject a = new JSONObject();
            a.put("weeklySales", loadMonthlySales(c));
            a.put("topCustomers", loadTopCustomers(c));
            a.put("categoryShare", loadCategoryShare(c));
            a.put("debtAging", loadDebtAging(c));
            return a.toString();
        }
    }

    private JSONArray loadMonthlySales(Connection c) throws Exception {
        Set<String> cols = columns(c, "sailfact");
        if (!cols.contains("date") || !cols.contains("all")) return new JSONArray();
        String where = activeWhere(cols, "s");
        List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && cols.contains("vis_rdf")) {
            where = appendWhere(where, "TRY_CONVERT(int,s.[vis_rdf])=?");
            params.add(session.visitorId);
        }
        String sql = "SELECT TOP (12) LEFT(s.[date],7) AS label, ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[all])),0) AS value " +
                "FROM dbo.sailfact s " + where + " GROUP BY LEFT(s.[date],7) ORDER BY LEFT(s.[date],7) DESC";
        return reverse(readPoints(c, sql, params));
    }

    private JSONArray loadTopCustomers(Connection c) throws Exception {
        Set<String> sail = columns(c, "sailfact");
        Set<String> cust = columns(c, "CUSTOMERS");
        if (!sail.contains("shmo") || !sail.contains("all") || !cust.contains("SHMO")) return new JSONArray();
        String nameExpr = cust.contains("MONAME") ? "TRY_CONVERT(nvarchar(250),c.MONAME)" : "TRY_CONVERT(nvarchar(100),c.SHMO)";
        String where = activeWhere(sail, "s");
        List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && sail.contains("vis_rdf")) {
            where = appendWhere(where, "TRY_CONVERT(int,s.[vis_rdf])=?");
            params.add(session.visitorId);
        }
        String sql = "SELECT TOP (10) " + nameExpr + " AS label, ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[all])),0) AS value " +
                "FROM dbo.CUSTOMERS c JOIN dbo.sailfact s ON s.shmo=c.SHMO " + where +
                " GROUP BY c.SHMO," + nameExpr + " ORDER BY value DESC";
        return readPoints(c, sql, params);
    }

    private JSONArray loadDebtAging(Connection c) throws Exception {
        Set<String> cols = columns(c, "CUSTOMERS");
        String balance = resolve(cols, "man", "Balance", "Mandeh");
        if (balance == null) return new JSONArray();
        String vis = resolve(cols, "vis_rdf", "VisitorID", "visid");
        String where = "WHERE TRY_CONVERT(decimal(19,2),[" + balance + "]) > 0";
        List<Object> params = new ArrayList<>();
        if (session != null && session.visitorId != null && vis != null) {
            where += " AND TRY_CONVERT(int,[" + vis + "])=?";
            params.add(session.visitorId);
        }
        String amount = "TRY_CONVERT(decimal(19,2),[" + balance + "])";
        String bucket = "CASE WHEN " + amount + " < 10000000 THEN N'کمتر از ۱۰M' " +
                "WHEN " + amount + " < 50000000 THEN N'۱۰ تا ۵۰M' " +
                "WHEN " + amount + " < 200000000 THEN N'۵۰ تا ۲۰۰M' ELSE N'بیش از ۲۰۰M' END";
        String sql = "SELECT " + bucket + " AS label, SUM(" + amount + ") AS value FROM dbo.CUSTOMERS " + where + " GROUP BY " + bucket;
        return readPoints(c, sql, params);
    }

    private JSONArray loadCategoryShare(Connection c) throws Exception {
        Set<String> inv = columns(c, "inventory");
        Set<String> grp = columns(c, "kagroup");
        String groupId = resolve(inv, "GroupID", "group_rdf", "VarietyID", "variety_rdf");
        String productName = resolve(inv, "naka", "Name", "KalaName");
        if (groupId == null) return new JSONArray();
        String groupName = resolve(grp, "name", "Name", "GroupName", "nagr", "gname");
        String groupKey = resolve(grp, "ID", "GroupID", "rdf", "code");
        if (groupName != null && groupKey != null) {
            String sql = "SELECT TOP (10) TRY_CONVERT(nvarchar(250),g.[" + groupName + "]) AS label, COUNT_BIG(1) AS value " +
                    "FROM dbo.inventory i LEFT JOIN dbo.kagroup g ON TRY_CONVERT(nvarchar(100),g.[" + groupKey + "])=TRY_CONVERT(nvarchar(100),i.[" + groupId + "]) " +
                    "GROUP BY TRY_CONVERT(nvarchar(250),g.[" + groupName + "]) ORDER BY value DESC";
            return readPoints(c, sql, new ArrayList<>());
        }
        String label = productName == null ? "N'کالا'" : "TRY_CONVERT(nvarchar(250),[" + productName + "])";
        String sql = "SELECT TOP (10) " + label + " AS label, COUNT_BIG(1) AS value FROM dbo.inventory GROUP BY " + label + " ORDER BY value DESC";
        return readPoints(c, sql, new ArrayList<>());
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

    private String resolve(Set<String> cols, String... candidates) {
        for (String c : candidates) if (cols.contains(c)) return c;
        return null;
    }

    private String activeWhere(Set<String> cols, String alias) {
        if (cols.contains("active")) return "WHERE (" + alias + ".[active]='t' OR " + alias + ".[active]='1' OR " + alias + ".[active]='Y')";
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
