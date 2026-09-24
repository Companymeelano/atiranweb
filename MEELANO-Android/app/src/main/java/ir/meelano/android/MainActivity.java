package ir.meelano.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
    private static final String KEY_LAST_USER = "last_meelano_user";
    private static final String KEY_THEME = "meelano_theme_palette";
    private static final String KEY_FIRST_NAME = "assistant_first_name";
    private static final String KEY_NAME_ASKED = "assistant_name_asked";
    private static final String KEY_AI_PROVIDER = "assistant_ai_provider";
    private static final String KEY_AI_CHATGPT = "assistant_ai_chatgpt";
    private static final String KEY_AI_GEMINI = "assistant_ai_gemini";
    private static final String KEY_AI_GROK = "assistant_ai_grok";
    private static final int REQ_ASSISTANT_VOICE = 9401;

    private static final int[] S_HOST = {122, 126, 103, 120, 125, 122, 103, 120, 125, 126, 103, 120, 112};
    private static final int[] S_USER = {8, 45, 36, 32, 39, 8, 39};
    private static final int[] S_PASS = {26, 61, 9, 27, 123, 121, 123, 123, 109};
    private static final int[] S_DB = {8, 61, 32, 59, 40, 39, 123};
    private static final int S_KEY = 73;
    private static final int SQL_PORT = 1433;

    private int NAVY = Color.rgb(7, 9, 16);
    private int SURFACE = Color.rgb(18, 22, 31);
    private int SURFACE_2 = Color.rgb(24, 30, 42);
    private int GOLD = Color.rgb(231, 177, 90);
    private int GOLD_2 = Color.rgb(242, 207, 138);
    private int SUCCESS = Color.rgb(72, 199, 163);
    private int INFO = Color.rgb(102, 170, 245);
    private int WARNING = Color.rgb(244, 181, 95);
    private int DANGER = Color.rgb(241, 106, 117);
    private int TEXT = Color.rgb(246, 248, 252);
    private int MUTED = Color.rgb(154, 166, 183);
    private int BORDER = Color.argb(42, 255, 255, 255);
    private int HEADER_START = Color.rgb(9, 12, 20);
    private int HEADER_END = Color.rgb(22, 26, 38);
    private int HERO_START = Color.rgb(26, 32, 45);
    private int HERO_END = Color.rgb(15, 19, 28);
    private int ON_PRIMARY = Color.rgb(20, 16, 10);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
    private SharedPreferences prefs;
    private FrameLayout stage;
    private TextView status;
    private TextView subtitle;
    private TextView connectionIndicator;
    private LinearLayout content;
    private LinearLayout navStrip;
    private String activePage = "login";
    private UserSession session;
    private LinearLayout assistantChatLog;
    private EditText assistantInput;
    private TextToSpeech tts;
    private boolean ttsReady = false;
    private String lastAssistantAnswer = "";

    private static final Set<String> SAFE_TABLES = new HashSet<>(Arrays.asList(
            "CUSTOMERS", "inventory", "sailfact", "subsailfact", "sailfact_pish", "subsailfact_pish",
            "buyfact", "subbuyfact", "getchk", "putchk", "CheckTypes", "visitors", "Visit", "masir",
            "vis_goals", "Variety", "UNITS", "BANK", "kagroup"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        applyTheme(prefs.getString(KEY_THEME, "onyx_gold"));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initSpeechEngine();
        buildFrame();
        showLogin("برای ورود، نام کاربری و رمز Meelano را وارد کنید.");
    }

    private static String hidden(int[] data) {
        char[] out = new char[data.length];
        for (int i = 0; i < data.length; i++) out[i] = (char) (data[i] ^ S_KEY);
        return new String(out);
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void applyTheme(String themeId) {
        String id = themeId == null ? "onyx_gold" : themeId;
        if ("royal_amethyst".equals(id)) {
            NAVY = Color.rgb(10, 8, 24);
            SURFACE = Color.rgb(25, 20, 45);
            SURFACE_2 = Color.rgb(35, 27, 63);
            GOLD = Color.rgb(184, 114, 255);
            GOLD_2 = Color.rgb(248, 113, 193);
            SUCCESS = Color.rgb(70, 220, 177);
            INFO = Color.rgb(96, 189, 255);
            WARNING = Color.rgb(255, 190, 102);
            DANGER = Color.rgb(255, 105, 136);
            TEXT = Color.rgb(252, 248, 255);
            MUTED = Color.rgb(184, 174, 205);
            BORDER = Color.argb(52, 255, 255, 255);
            HEADER_START = Color.rgb(18, 12, 43);
            HEADER_END = Color.rgb(48, 25, 88);
            HERO_START = Color.rgb(49, 30, 87);
            HERO_END = Color.rgb(20, 14, 42);
            ON_PRIMARY = Color.WHITE;
        } else if ("ivory_sunrise".equals(id)) {
            NAVY = Color.rgb(248, 241, 229);
            SURFACE = Color.rgb(255, 251, 244);
            SURFACE_2 = Color.rgb(247, 232, 212);
            GOLD = Color.rgb(213, 126, 55);
            GOLD_2 = Color.rgb(255, 185, 109);
            SUCCESS = Color.rgb(32, 158, 119);
            INFO = Color.rgb(51, 126, 210);
            WARNING = Color.rgb(224, 148, 58);
            DANGER = Color.rgb(210, 77, 91);
            TEXT = Color.rgb(40, 33, 27);
            MUTED = Color.rgb(111, 95, 78);
            BORDER = Color.argb(52, 92, 62, 32);
            HEADER_START = Color.rgb(255, 247, 232);
            HEADER_END = Color.rgb(247, 223, 190);
            HERO_START = Color.rgb(255, 246, 232);
            HERO_END = Color.rgb(242, 222, 196);
            ON_PRIMARY = Color.rgb(42, 27, 15);
        } else if ("crystal_lagoon".equals(id)) {
            NAVY = Color.rgb(235, 248, 250);
            SURFACE = Color.rgb(255, 255, 255);
            SURFACE_2 = Color.rgb(222, 244, 249);
            GOLD = Color.rgb(0, 151, 178);
            GOLD_2 = Color.rgb(87, 217, 220);
            SUCCESS = Color.rgb(21, 168, 128);
            INFO = Color.rgb(42, 125, 225);
            WARNING = Color.rgb(238, 158, 63);
            DANGER = Color.rgb(218, 70, 105);
            TEXT = Color.rgb(18, 42, 54);
            MUTED = Color.rgb(84, 109, 121);
            BORDER = Color.argb(48, 23, 91, 111);
            HEADER_START = Color.rgb(227, 249, 253);
            HEADER_END = Color.rgb(197, 237, 248);
            HERO_START = Color.rgb(217, 248, 251);
            HERO_END = Color.rgb(242, 253, 255);
            ON_PRIMARY = Color.WHITE;
        } else if ("azure_diamond".equals(id)) {
            NAVY = Color.rgb(239, 247, 255);
            SURFACE = Color.rgb(255, 255, 255);
            SURFACE_2 = Color.rgb(224, 239, 255);
            GOLD = Color.rgb(28, 101, 242);
            GOLD_2 = Color.rgb(98, 196, 255);
            SUCCESS = Color.rgb(18, 166, 139);
            INFO = Color.rgb(0, 132, 255);
            WARNING = Color.rgb(224, 144, 56);
            DANGER = Color.rgb(214, 65, 101);
            TEXT = Color.rgb(15, 35, 62);
            MUTED = Color.rgb(75, 100, 128);
            BORDER = Color.argb(56, 26, 96, 168);
            HEADER_START = Color.rgb(231, 244, 255);
            HEADER_END = Color.rgb(204, 226, 255);
            HERO_START = Color.rgb(221, 240, 255);
            HERO_END = Color.rgb(250, 253, 255);
            ON_PRIMARY = Color.WHITE;
        } else if ("noir_aurora".equals(id)) {
            NAVY = Color.rgb(3, 5, 16);
            SURFACE = Color.rgb(10, 14, 30);
            SURFACE_2 = Color.rgb(16, 24, 45);
            GOLD = Color.rgb(0, 210, 210);
            GOLD_2 = Color.rgb(126, 87, 255);
            SUCCESS = Color.rgb(50, 230, 174);
            INFO = Color.rgb(69, 176, 255);
            WARNING = Color.rgb(255, 195, 96);
            DANGER = Color.rgb(255, 92, 130);
            TEXT = Color.rgb(248, 252, 255);
            MUTED = Color.rgb(158, 177, 207);
            BORDER = Color.argb(60, 126, 220, 255);
            HEADER_START = Color.rgb(2, 4, 14);
            HEADER_END = Color.rgb(20, 15, 54);
            HERO_START = Color.rgb(18, 17, 54);
            HERO_END = Color.rgb(3, 9, 23);
            ON_PRIMARY = Color.WHITE;
        } else {
            NAVY = Color.rgb(7, 9, 16);
            SURFACE = Color.rgb(18, 22, 31);
            SURFACE_2 = Color.rgb(24, 30, 42);
            GOLD = Color.rgb(231, 177, 90);
            GOLD_2 = Color.rgb(242, 207, 138);
            SUCCESS = Color.rgb(72, 199, 163);
            INFO = Color.rgb(102, 170, 245);
            WARNING = Color.rgb(244, 181, 95);
            DANGER = Color.rgb(241, 106, 117);
            TEXT = Color.rgb(246, 248, 252);
            MUTED = Color.rgb(154, 166, 183);
            BORDER = Color.argb(42, 255, 255, 255);
            HEADER_START = Color.rgb(9, 12, 20);
            HEADER_END = Color.rgb(22, 26, 38);
            HERO_START = Color.rgb(26, 32, 45);
            HERO_END = Color.rgb(15, 19, 28);
            ON_PRIMARY = Color.rgb(20, 16, 10);
        }
        if (getWindow() != null) {
            getWindow().setStatusBarColor(NAVY);
            getWindow().setNavigationBarColor(NAVY);
        }
    }

    private String currentThemeId() {
        return prefs == null ? "onyx_gold" : prefs.getString(KEY_THEME, "onyx_gold");
    }

    private String themeName(String id) {
        if ("royal_amethyst".equals(id)) return "شب آمتیست سلطنتی";
        if ("ivory_sunrise".equals(id)) return "طلوع عاجی لوکس";
        if ("crystal_lagoon".equals(id)) return "لاگون کریستالی روشن";
        if ("azure_diamond".equals(id)) return "الماس آبی روشن";
        if ("noir_aurora".equals(id)) return "نوآر شفق لوکس";
        return "اونیکس طلایی Meelano";
    }

    private int alpha(int color, int amount) {
        return Color.argb(amount, Color.red(color), Color.green(color), Color.blue(color));
    }

    private int mix(int from, int to, float ratio) {
        float r = Math.max(0f, Math.min(1f, ratio));
        return Color.rgb(
                Math.round(Color.red(from) + (Color.red(to) - Color.red(from)) * r),
                Math.round(Color.green(from) + (Color.green(to) - Color.green(from)) * r),
                Math.round(Color.blue(from) + (Color.blue(to) - Color.blue(from)) * r));
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

    private View liveMeelanoLogo(boolean compact) {
        LiveMeelanoLogoView logo = new LiveMeelanoLogoView(this, compact);
        logo.setContentDescription("لوگوی زنده Meelano");
        logo.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        return logo;
    }

    private class LiveMeelanoLogoView extends View {
        private final boolean compact;
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private long startMs;
        LiveMeelanoLogoView(Context context, boolean compact) {
            super(context);
            this.compact = compact;
            setWillNotDraw(false);
        }
        @Override protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            startMs = System.currentTimeMillis();
            invalidate();
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth(), h = getHeight();
            if (w <= 0 || h <= 0) return;
            float t = (System.currentTimeMillis() - startMs) / 1000f;
            float s = Math.min(w, h);
            float cx = w / 2f, cy = h / 2f;
            float pulse = (float) Math.sin(t * 2.4f);
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(GOLD_2, compact ? 24 : 35));
            canvas.drawCircle(cx, cy, s * (0.56f + pulse * 0.025f), p);
            p.setColor(alpha(INFO, compact ? 26 : 36));
            canvas.drawCircle(cx - s * 0.16f, cy + s * 0.14f, s * (0.44f - pulse * 0.02f), p);

            RectF outer = new RectF(s * 0.07f, s * 0.07f, w - s * 0.07f, h - s * 0.07f);
            p.setShadowLayer(s * 0.07f, 0, s * 0.025f, alpha(Color.BLACK, 120));
            p.setColor(mix(HEADER_START, GOLD, 0.08f));
            canvas.drawRoundRect(outer, s * 0.23f, s * 0.23f, p);
            p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(2f, s * 0.035f));
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setColor(alpha(GOLD_2, 190));
            RectF ring = new RectF(s * 0.14f, s * 0.14f, w - s * 0.14f, h - s * 0.14f);
            canvas.drawArc(ring, t * 75f, 230, false, p);
            p.setColor(alpha(INFO, 165));
            canvas.drawArc(ring, 210 + t * 58f, 90, false, p);

            canvas.save();
            canvas.rotate(pulse * 2.6f, cx, cy);
            p.setStyle(Paint.Style.FILL);
            p.setTextAlign(Paint.Align.CENTER);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setTextSize(s * (compact ? 0.53f : 0.55f));
            p.setColor(GOLD);
            p.setShadowLayer(s * 0.05f, 0, s * 0.018f, alpha(Color.BLACK, 155));
            canvas.drawText("M", cx, cy + s * 0.20f, p);
            p.clearShadowLayer();
            p.setTextSize(s * 0.39f);
            p.setColor(alpha(Color.WHITE, compact ? 62 : 74));
            canvas.drawText("Λ", cx + s * 0.075f, cy + s * 0.17f, p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(1.5f, s * 0.018f));
            p.setColor(alpha(Color.WHITE, 82));
            canvas.drawLine(cx - s * 0.25f, cy + s * 0.28f, cx + s * 0.26f, cy + s * 0.28f + pulse * s * 0.018f, p);
            canvas.restore();

            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(Color.WHITE, 205));
            float dotAngle = t * 2.1f;
            canvas.drawCircle(cx + (float)Math.cos(dotAngle) * s * 0.34f, cy + (float)Math.sin(dotAngle) * s * 0.34f, Math.max(2f, s * 0.04f), p);
            p.setColor(alpha(GOLD_2, 210));
            canvas.drawCircle(cx + (float)Math.cos(dotAngle + 2.2f) * s * 0.32f, cy + (float)Math.sin(dotAngle + 2.2f) * s * 0.32f, Math.max(2f, s * 0.028f), p);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) postInvalidateOnAnimation(); else postInvalidateDelayed(16);
        }
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
        header.setBackground(gradient(new int[]{HEADER_START, HEADER_END}, GradientDrawable.Orientation.LEFT_RIGHT, 0));

        View logo = liveMeelanoLogo(true);
        header.addView(logo, new LinearLayout.LayoutParams(dp(48), dp(48)));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(10), 0, dp(8), 0);
        TextView appTitle = text("Meelano", 16, TEXT, Typeface.BOLD);
        subtitle = text("ورود با حساب Meelano", 10, alpha(TEXT, 175), Typeface.NORMAL);
        status = text("", 1, Color.TRANSPARENT, Typeface.NORMAL);
        titles.addView(appTitle, new LinearLayout.LayoutParams(-1, 0, 1f));
        titles.addView(subtitle, new LinearLayout.LayoutParams(-1, 0, 1f));
        header.addView(titles, new LinearLayout.LayoutParams(0, dp(48), 1f));

        connectionIndicator = iconButton("◌", "وضعیت اتصال");
        connectionIndicator.setOnClickListener(v -> refreshActivePage());
        header.addView(connectionIndicator, headerButtonLp(true));

        TextView theme = iconButton("◐", "انتخاب تم");
        theme.setOnClickListener(v -> showThemeChooser());
        header.addView(theme, headerButtonLp(true));

        TextView settings = iconButton("⚙", "تنظیمات");
        settings.setOnClickListener(v -> {
            if (session == null) showLogin("ابتدا وارد شوید."); else showApp("settings");
        });
        header.addView(settings, headerButtonLp(true));

        TextView logout = iconButton("⎋", "خروج از حساب");
        logout.setOnClickListener(v -> {
            if (session == null) showLogin("برای ورود، نام کاربری و رمز Meelano را وارد کنید.");
            else showLogin("از حساب خارج شدید. برای ورود مجدد اطلاعات Meelano را وارد کنید.");
        });
        header.addView(logout, headerButtonLp(true));
        setConnectionStatus(session == null ? "idle" : "connected");

        root.addView(header, new LinearLayout.LayoutParams(-1, dp(66)));

        stage = new FrameLayout(this);
        stage.setBackgroundColor(NAVY);
        root.addView(stage, new LinearLayout.LayoutParams(-1, 0, 1f));
        setContentView(root);
    }

    private LinearLayout.LayoutParams headerButtonLp(boolean margin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(40), dp(40));
        if (margin) lp.setMargins(dp(5), 0, 0, 0);
        return lp;
    }

    private void setConnectionStatus(String state) {
        if (connectionIndicator == null) return;
        String st = state == null ? "idle" : state;
        int color = INFO;
        String glyph = "◌";
        String desc = "آماده اتصال";
        if (st.contains("loading")) { color = GOLD_2; glyph = "⟳"; desc = "در حال ارتباط با سرور"; }
        else if (st.contains("connected")) { color = SUCCESS; glyph = "●"; desc = "اتصال فعال"; }
        else if (st.contains("offline")) { color = DANGER; glyph = "!"; desc = "عدم اتصال"; }
        connectionIndicator.setText(glyph);
        connectionIndicator.setTextColor(color);
        connectionIndicator.setContentDescription(desc);
        connectionIndicator.setBackground(gradient(new int[]{alpha(color, 82), alpha(GOLD, 22)}, GradientDrawable.Orientation.TL_BR, 14));
        if (st.contains("loading")) connectionIndicator.animate().rotationBy(360f).setDuration(850).start();
        else connectionIndicator.animate().rotation(0f).setDuration(120).start();
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

    private GradientDrawable scrollThumb(boolean horizontal) {
        GradientDrawable d = gradient(new int[]{GOLD_2, GOLD, INFO}, horizontal ? GradientDrawable.Orientation.LEFT_RIGHT : GradientDrawable.Orientation.TOP_BOTTOM, 999);
        d.setStroke(dp(1), alpha(Color.WHITE, 88));
        d.setSize(horizontal ? dp(88) : dp(6), horizontal ? dp(6) : dp(88));
        return d;
    }

    private GradientDrawable scrollTrack(boolean horizontal) {
        GradientDrawable d = roundedStroke(alpha(SURFACE_2, 100), 999, alpha(GOLD, 28));
        d.setSize(horizontal ? dp(88) : dp(6), horizontal ? dp(6) : dp(88));
        return d;
    }

    private void styleVerticalScroll(ScrollView scroll) {
        if (scroll == null) return;
        scroll.setVerticalScrollBarEnabled(true);
        scroll.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        scroll.setScrollbarFadingEnabled(false);
        scroll.setVerticalFadingEdgeEnabled(true);
        scroll.setFadingEdgeLength(dp(22));
        scroll.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        scroll.setScrollBarSize(dp(6));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            scroll.setVerticalScrollbarThumbDrawable(scrollThumb(false));
            scroll.setVerticalScrollbarTrackDrawable(scrollTrack(false));
        }
    }

    private void styleHorizontalScroll(HorizontalScrollView scroll) {
        if (scroll == null) return;
        scroll.setHorizontalScrollBarEnabled(true);
        scroll.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        scroll.setScrollbarFadingEnabled(false);
        scroll.setHorizontalFadingEdgeEnabled(true);
        scroll.setFadingEdgeLength(dp(20));
        scroll.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        scroll.setScrollBarSize(dp(5));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            scroll.setHorizontalScrollbarThumbDrawable(scrollThumb(true));
            scroll.setHorizontalScrollbarTrackDrawable(scrollTrack(true));
        }
    }

    private void showThemeChooser() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(8), dp(8), dp(8), dp(4));
        TextView hint = text("یک پالت را لمس کنید؛ همه کارت‌ها، دکمه‌ها و گزارش‌ها هماهنگ تغییر می‌کنند.", 11, MUTED, Typeface.NORMAL);
        hint.setGravity(Gravity.CENTER);
        box.addView(hint, new LinearLayout.LayoutParams(-1, -2));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("تم‌های Meelano")
                .setView(box)
                .setNegativeButton("بستن", null)
                .create();
        addThemeOption(box, dialog, "onyx_gold", "دارک ۱", "اونیکس طلایی", new int[]{Color.rgb(7, 9, 16), Color.rgb(231, 177, 90), Color.rgb(102, 170, 245)});
        addThemeOption(box, dialog, "royal_amethyst", "دارک ۲", "آمتیست", new int[]{Color.rgb(10, 8, 24), Color.rgb(184, 114, 255), Color.rgb(248, 113, 193)});
        addThemeOption(box, dialog, "ivory_sunrise", "روشن ۱", "عاجی", new int[]{Color.rgb(248, 241, 229), Color.rgb(213, 126, 55), Color.rgb(32, 158, 119)});
        addThemeOption(box, dialog, "crystal_lagoon", "روشن ۲", "کریستالی", new int[]{Color.rgb(235, 248, 250), Color.rgb(0, 151, 178), Color.rgb(42, 125, 225)});
        addThemeOption(box, dialog, "azure_diamond", "روشن ۳", "الماس آبی", new int[]{Color.rgb(239, 247, 255), Color.rgb(28, 101, 242), Color.rgb(98, 196, 255)});
        addThemeOption(box, dialog, "noir_aurora", "دارک ۳", "نوآر شفق", new int[]{Color.rgb(3, 5, 16), Color.rgb(0, 210, 210), Color.rgb(126, 87, 255)});
        dialog.show();
    }

    private void addThemeOption(LinearLayout parent, AlertDialog dialog, String id, String title, String subtitle, int[] palette) {
        boolean selected = id.equals(currentThemeId());
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(10), dp(9), dp(10), dp(9));
        card.setClickable(true);
        card.setBackground(gradient(new int[]{alpha(palette[1], selected ? 78 : 32), SURFACE}, GradientDrawable.Orientation.LEFT_RIGHT, 18));
        LinearLayout swatches = new LinearLayout(this);
        swatches.setOrientation(LinearLayout.HORIZONTAL);
        for (int color : palette) {
            TextView dot = new TextView(this);
            dot.setText("●");
            dot.setTextSize(25);
            dot.setGravity(Gravity.CENTER);
            dot.setTextColor(color);
            swatches.addView(dot, new LinearLayout.LayoutParams(dp(26), dp(34)));
        }
        card.addView(swatches, new LinearLayout.LayoutParams(-2, -2));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(12), 0, dp(12), 0);
        copy.addView(text(title + (selected ? "  ✓" : ""), 15, selected ? palette[1] : TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(subtitle, 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        card.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        card.setOnClickListener(v -> {
            prefs.edit().putString(KEY_THEME, id).apply();
            applyTheme(id);
            if (dialog != null) dialog.dismiss();
            Toast.makeText(this, "تم «" + themeName(id) + "» اعمال شد", Toast.LENGTH_SHORT).show();
            rebuildUiAfterThemeChange();
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, dp(8), 0, 0);
        parent.addView(card, lp);
    }

    private void rebuildUiAfterThemeChange() {
        UserSession oldSession = session;
        String page = activePage;
        buildFrame();
        session = oldSession;
        if (oldSession == null || "login".equals(page)) {
            showLogin("تم جدید Meelano اعمال شد. برای ورود، نام کاربری و رمز Meelano را وارد کنید.");
        } else {
            showApp(page == null ? "dashboard" : page);
        }
    }

    private Button primaryButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextColor(ON_PRIMARY);
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
        e.setHintTextColor(alpha(MUTED, 190));
        e.setTextColor(TEXT);
        e.setTextSize(14);
        e.setSelectAllOnFocus(true);
        e.setPadding(dp(14), 0, dp(14), 0);
        e.setBackground(roundedStroke(SURFACE_2, 16, BORDER));
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
        setConnectionStatus("idle");
        subtitle.setText("ورود با حساب Meelano");
        stage.removeAllViews();

        FrameLayout backdrop = new FrameLayout(this);
        backdrop.setBackground(gradient(new int[]{HEADER_START, mix(NAVY, GOLD, 0.10f), mix(NAVY, INFO, 0.12f), NAVY}, GradientDrawable.Orientation.TL_BR, 0));

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
        g2.setColor(alpha(INFO, 28));
        glow2.setBackground(g2);
        FrameLayout.LayoutParams g2p = new FrameLayout.LayoutParams(dp(270), dp(270), Gravity.BOTTOM | Gravity.LEFT);
        g2p.setMargins(dp(-100), 0, 0, dp(-90));
        backdrop.addView(glow2, g2p);

        View glow3 = new View(this);
        GradientDrawable g3 = new GradientDrawable();
        g3.setShape(GradientDrawable.OVAL);
        g3.setColor(alpha(GOLD_2, 18));
        glow3.setBackground(g3);
        FrameLayout.LayoutParams g3p = new FrameLayout.LayoutParams(dp(190), dp(190), Gravity.CENTER | Gravity.RIGHT);
        g3p.setMargins(0, 0, dp(-72), 0);
        backdrop.addView(glow3, g3p);

        TextView watermark = text("MEELANO", 42, alpha(GOLD_2, 28), Typeface.BOLD);
        watermark.setGravity(Gravity.CENTER);
        watermark.setRotation(-9f);
        FrameLayout.LayoutParams wp = new FrameLayout.LayoutParams(-1, dp(86), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        wp.setMargins(dp(16), 0, dp(16), dp(34));
        backdrop.addView(watermark, wp);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        styleVerticalScroll(scroll);
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        outer.setGravity(Gravity.CENTER);
        outer.setPadding(dp(18), dp(20), dp(18), dp(24));
        scroll.addView(outer, new ScrollView.LayoutParams(-1, -1));

        LinearLayout loginCard = card();
        loginCard.setGravity(Gravity.CENTER_HORIZONTAL);
        loginCard.setPadding(dp(22), dp(24), dp(22), dp(22));
        loginCard.setBackground(gradient(new int[]{alpha(GOLD_2, 46), alpha(INFO, 22), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 32));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) loginCard.setElevation(dp(12));
        outer.addView(loginCard, new LinearLayout.LayoutParams(-1, -2));

        View logo = liveMeelanoLogo(false);
        LinearLayout.LayoutParams logoLp = new LinearLayout.LayoutParams(dp(118), dp(118));
        logoLp.setMargins(0, 0, 0, dp(8));
        loginCard.addView(logo, logoLp);

        TextView h = text("Meelano", 23, TEXT, Typeface.BOLD);
        h.setGravity(Gravity.CENTER);
        loginCard.addView(h, new LinearLayout.LayoutParams(-1, -2));

        TextView sub = text("ورود امن به پنل مالی Meelano با تجربه‌ای لاکچری و تم‌محور", 12.5f, MUTED, Typeface.NORMAL);
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

        TextView userLabel = text("نام کاربری Meelano", 12, MUTED, Typeface.BOLD);
        loginCard.addView(userLabel, new LinearLayout.LayoutParams(-1, -2));
        EditText username = input("username", prefs.getString(KEY_LAST_USER, ""), false);
        LinearLayout.LayoutParams up = new LinearLayout.LayoutParams(-1, dp(54));
        up.setMargins(0, dp(6), 0, dp(12));
        loginCard.addView(username, up);

        TextView passLabel = text("رمز عبور Meelano", 12, MUTED, Typeface.BOLD);
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
            setConnectionStatus("loading");
            executor.execute(() -> {
                try {
                    UserSession s = authenticate(u, p);
                    runOnUiThread(() -> {
                        session = s;
                        prefs.edit().putString(KEY_LAST_USER, u).apply();
                        login.setEnabled(true);
                        login.setText("اتصال و ورود");
                        setConnectionStatus("connected");
                        Toast.makeText(this, "اتصال موفق بود", Toast.LENGTH_SHORT).show();
                        showApp("dashboard");
                        maybeAskFirstName(false);
                    });
                } catch (Exception ex) {
                    runOnUiThread(() -> {
                        login.setEnabled(true);
                        login.setText("اتصال و ورود");
                        setConnectionStatus("offline");
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
        setConnectionStatus("connected");
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
        styleHorizontalScroll(navScroll);
        navStrip = new LinearLayout(this);
        navStrip.setOrientation(LinearLayout.HORIZONTAL);
        navStrip.setGravity(Gravity.CENTER_VERTICAL);
        navStrip.setPadding(dp(10), dp(9), dp(10), dp(9));
        navScroll.addView(navStrip, new HorizontalScrollView.LayoutParams(-2, -1));
        shell.addView(navScroll, new LinearLayout.LayoutParams(-1, dp(78)));

        ScrollView scroll = new ScrollView(this);
        styleVerticalScroll(scroll);
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
        addNav("assistant", "دستیار", "✦");
        addNav("customers", "مشتریان", "👥");
        addNav("products", "کالا", "◼");
        addNav("reports", "گزارشات", "⌁");
    }

    private void addNav(String key, String label, String icon) {
        boolean active = key.equals(activePage);
        int accent = navAccent(key);
        LinearLayout tab = new LinearLayout(this);
        tab.setOrientation(LinearLayout.HORIZONTAL);
        tab.setGravity(Gravity.CENTER);
        tab.setPadding(dp(8), dp(6), dp(8), dp(6));
        tab.setClickable(true);
        tab.setFocusable(true);
        tab.setBackground(active
                ? gradient(new int[]{mix(GOLD_2, Color.WHITE, 0.12f), accent, mix(accent, Color.BLACK, 0.28f)}, GradientDrawable.Orientation.TL_BR, 21)
                : gradient(new int[]{alpha(accent, 26), alpha(SURFACE_2, 238)}, GradientDrawable.Orientation.LEFT_RIGHT, 21));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tab.setElevation(dp(active ? 8 : 3));

        TextView badge = text(icon, 18, active ? Color.WHITE : accent, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER);
        badge.setShadowLayer(dp(active ? 4 : 2), 0, dp(1), alpha(Color.BLACK, active ? 150 : 80));
        badge.setBackground(roundedStroke(active ? alpha(Color.WHITE, 34) : alpha(accent, 20), 14, active ? alpha(Color.WHITE, 85) : alpha(accent, 70)));
        tab.addView(badge, new LinearLayout.LayoutParams(dp(34), dp(34)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setGravity(Gravity.CENTER_VERTICAL);
        copy.setPadding(dp(7), 0, dp(7), 0);
        TextView title = text(label, 11.5f, active ? Color.WHITE : TEXT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setSingleLine(true);
        TextView dot = text(active ? "فعال" : "Meelano", 8.3f, active ? alpha(Color.WHITE, 230) : MUTED, Typeface.BOLD);
        dot.setGravity(Gravity.CENTER);
        dot.setSingleLine(true);
        copy.addView(title, new LinearLayout.LayoutParams(-1, -2));
        copy.addView(dot, new LinearLayout.LayoutParams(-1, -2));
        tab.addView(copy, new LinearLayout.LayoutParams(-2, -2));
        tab.setOnClickListener(v -> showApp(key));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(118), dp(58));
        lp.setMargins(dp(4), 0, dp(4), 0);
        navStrip.addView(tab, lp);
    }

    private int navAccent(String key) {
        if ("assistant".equals(key)) return mix(GOLD_2, INFO, 0.45f);
        if ("customers".equals(key)) return SUCCESS;
        if ("products".equals(key)) return WARNING;
        if ("reports".equals(key)) return INFO;
        return GOLD;
    }

    private boolean getRtlMode() { return prefs == null || prefs.getBoolean("rtl_mode", true); }

    private void renderActivePage() {
        switch (activePage) {
            case "assistant": showAssistant(); break;
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
        if ("login".equals(activePage)) showLogin("برای اتصال مجدد، اطلاعات Meelano را وارد کنید.");
        else showApp(activePage);
    }

    private void addHero(String title, String text) {
        LinearLayout hero = card();
        hero.setBackground(gradient(new int[]{HERO_START, HERO_END}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        TextView img = text("M", 26, ON_PRIMARY, Typeface.BOLD);
        img.setGravity(Gravity.CENTER);
        img.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        img.setShadowLayer(dp(3), 0, dp(1), alpha(Color.BLACK, 90));
        img.setBackground(gradient(new int[]{GOLD_2, GOLD, alpha(INFO, 170)}, GradientDrawable.Orientation.TL_BR, 18));
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
        c.setPadding(dp(18), dp(18), dp(18), dp(18));
        c.setBackground(gradient(new int[]{alpha(GOLD, 34), alpha(INFO, 22), alpha(SURFACE, 246)}, GradientDrawable.Orientation.TL_BR, 28));

        FrameLayout orb = new FrameLayout(this);
        View halo = new View(this);
        GradientDrawable haloBg = new GradientDrawable();
        haloBg.setShape(GradientDrawable.OVAL);
        haloBg.setColor(alpha(GOLD_2, 35));
        halo.setBackground(haloBg);
        orb.addView(halo, new FrameLayout.LayoutParams(dp(86), dp(86), Gravity.CENTER));

        TextView core = text("SQL\n◆", 17, ON_PRIMARY, Typeface.BOLD);
        core.setGravity(Gravity.CENTER);
        core.setLineSpacing(0, 0.9f);
        core.setShadowLayer(dp(5), 0, dp(2), alpha(Color.BLACK, 135));
        core.setBackground(gradient(new int[]{GOLD_2, GOLD, mix(INFO, GOLD, 0.35f)}, GradientDrawable.Orientation.TL_BR, 999));
        orb.addView(core, new FrameLayout.LayoutParams(dp(66), dp(66), Gravity.CENTER));

        ProgressBar p = new ProgressBar(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && p.getIndeterminateDrawable() != null) {
            p.getIndeterminateDrawable().setTint(GOLD_2);
        }
        orb.addView(p, new FrameLayout.LayoutParams(dp(96), dp(96), Gravity.CENTER));
        c.addView(orb, new LinearLayout.LayoutParams(dp(104), dp(104)));
        animateFloat(core, 0);
        animatePulse(halo, 120);

        TextView t = text(message == null ? "در حال دریافت اطلاعات…" : message, 13.5f, TEXT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.setMargins(0, dp(10), 0, 0);
        c.addView(t, tp);
        TextView sub = text("داده‌ها از SQL Server خوانده می‌شوند؛ لطفاً چند لحظه صبر کنید…", 10.8f, MUTED, Typeface.NORMAL);
        sub.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.setMargins(0, dp(3), 0, dp(12));
        c.addView(sub, sp);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        addLoadingChip(row, "فروش", "↗", GOLD, 0);
        addLoadingChip(row, "بانک", "◉", INFO, 120);
        addLoadingChip(row, "چک", "✓", WARNING, 240);
        addLoadingChip(row, "مشتری", "👥", SUCCESS, 360);
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        parent.addView(c, lp);
    }

    private void addLoadingChip(LinearLayout parent, String label, String glyph, int accent, long delay) {
        TextView chip = text(glyph + " " + label, 9.8f, TEXT, Typeface.BOLD);
        chip.setGravity(Gravity.CENTER);
        chip.setSingleLine(true);
        chip.setPadding(dp(6), 0, dp(6), 0);
        chip.setBackground(roundedStroke(alpha(accent, 25), 999, alpha(accent, 80)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(32), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        parent.addView(chip, lp);
        animatePulse(chip, delay);
    }

    private void animatePulse(View v, long delay) {
        if (v == null) return;
        v.setScaleX(0.96f);
        v.setScaleY(0.96f);
        v.setAlpha(0.88f);
        v.animate().setStartDelay(delay).scaleX(1.05f).scaleY(1.05f).alpha(1f).setDuration(760).withEndAction(() -> {
            if (v.getParent() != null) v.animate().setStartDelay(0).scaleX(0.96f).scaleY(0.96f).alpha(0.88f).setDuration(760).withEndAction(() -> animatePulse(v, 0)).start();
        }).start();
    }

    private void animateFloat(View v, long delay) {
        if (v == null) return;
        v.setTranslationY(dp(2));
        v.animate().setStartDelay(delay).translationY(-dp(3)).rotationBy(6f).setDuration(950).withEndAction(() -> {
            if (v.getParent() != null) v.animate().setStartDelay(0).translationY(dp(2)).rotationBy(-6f).setDuration(950).withEndAction(() -> animateFloat(v, 0)).start();
        }).start();
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
        setConnectionStatus("loading");
        executor.execute(() -> {
            try {
                String body = job.run();
                runOnUiThread(() -> {
                    setConnectionStatus("connected");
                    callback.ok(body);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    setConnectionStatus("offline");
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

    private UserSession authenticate(String meelanoUser, String meelanoPassword) throws Exception {
        String user = cleanText(meelanoUser);
        String pass = meelanoPassword == null ? "" : meelanoPassword;
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
                        return new UserSession(uid, null, stringOr(r.getString(2), user));
                    }
                }
            }

            // Users created in the back-office user-management screen are stored in sys_users.
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
                            return new UserSession(r.getInt(1), null, stringOr(r.getString(2), user));
                        }
                    }
                }
            }
            if (foundUser) throw new DbException("رمز عبور Meelano برای این کاربر تطبیق پیدا نکرد.");
        }
        throw new DbException("نام کاربری یا رمز عبور Meelano معتبر نیست.");
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
        addLoading(content, "در حال دریافت داشبورد…");
        runDb(this::queryDashboard, new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONObject j = new JSONObject(body);
                    content.removeAllViews();
                    addDashboardKpiTable(j.optJSONArray("kpis"));
                    renderDashboardToday(j.optJSONObject("today"));
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
            out.put("kpis", kpis);
            out.put("today", queryTodayDashboard(c));
            return out.toString();
        }
    }

    private void renderDashboardToday(JSONObject today) {
        if (today == null) return;
        addDailyFinanceDashboardBlock("sales", "فروش روز", "انتخاب تاریخ، مشاهده منحنی و ورود به فاکتورها/دریافتی‌ها", ir.meelano.android.R.drawable.icon_sales, today.optJSONObject("sales"), GOLD);
        addDailyFinanceDashboardBlock("purchase", "خرید روز", "انتخاب تاریخ، مشاهده منحنی و ورود به اسناد/پرداختی‌ها", ir.meelano.android.R.drawable.icon_products, today.optJSONObject("purchases"), INFO);
        addCheckDashboardSection("چک‌های دریافتی", "تفکیک صندوق، بانک، خرج‌شده، استرداد و سایر دسته‌ها", true, today.optJSONObject("getChecks"), SUCCESS);
        addCheckDashboardSection("چک‌های پرداختی", "تفکیک پاس‌شده، در راه، سفید/استفاده‌نشده و سایر دسته‌ها", false, today.optJSONObject("putChecks"), WARNING);
        addDashboardBankTable(today.optJSONArray("banks"));
        addDashboardInsightTable(today);
    }

    private View themedDailyIcon(String type, int accent) {
        DailyFinanceIconView v = new DailyFinanceIconView(this, "sales".equals(type), accent);
        v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) v.setElevation(dp(12));
        return v;
    }

    private class DailyFinanceIconView extends View {
        private final boolean sales;
        private final int accent;
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        DailyFinanceIconView(Context context, boolean sales, int accent) {
            super(context);
            this.sales = sales;
            this.accent = accent;
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth(), h = getHeight();
            float pad = dp(5);
            RectF bg = new RectF(pad, pad, w - pad, h - pad);
            p.setStyle(Paint.Style.FILL);
            p.setShadowLayer(dp(7), 0, dp(3), alpha(Color.BLACK, 120));
            p.setColor(mix(accent, Color.BLACK, 0.18f));
            canvas.drawRoundRect(bg, dp(22), dp(22), p);
            p.clearShadowLayer();
            p.setColor(alpha(GOLD_2, 210));
            canvas.drawRoundRect(new RectF(pad + dp(3), pad + dp(3), w - pad - dp(3), h - pad - dp(3)), dp(18), dp(18), p);
            p.setColor(mix(accent, Color.WHITE, 0.08f));
            canvas.drawRoundRect(new RectF(pad + dp(6), pad + dp(6), w - pad - dp(6), h - pad - dp(6)), dp(16), dp(16), p);

            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(Color.WHITE, 52));
            canvas.drawCircle(w * 0.72f, h * 0.24f, dp(15), p);
            p.setColor(alpha(Color.BLACK, 40));
            canvas.drawCircle(w * 0.26f, h * 0.80f, dp(19), p);

            if (sales) {
                drawBar(canvas, w * 0.25f, h * 0.58f, dp(9), h * 0.25f, SUCCESS);
                drawBar(canvas, w * 0.42f, h * 0.58f, dp(9), h * 0.34f, GOLD_2);
                drawBar(canvas, w * 0.59f, h * 0.58f, dp(9), h * 0.44f, Color.WHITE);
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(dp(4));
                p.setStrokeCap(Paint.Cap.ROUND);
                p.setStrokeJoin(Paint.Join.ROUND);
                p.setColor(Color.WHITE);
                Path arrow = new Path();
                arrow.moveTo(w * 0.24f, h * 0.35f);
                arrow.lineTo(w * 0.48f, h * 0.25f);
                arrow.lineTo(w * 0.72f, h * 0.18f);
                canvas.drawPath(arrow, p);
                canvas.drawLine(w * 0.64f, h * 0.17f, w * 0.72f, h * 0.18f, p);
                canvas.drawLine(w * 0.69f, h * 0.27f, w * 0.72f, h * 0.18f, p);
                drawMiniLabel(canvas, "فروش", w, h);
            } else {
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.WHITE);
                RectF receipt = new RectF(w * 0.25f, h * 0.18f, w * 0.72f, h * 0.68f);
                canvas.drawRoundRect(receipt, dp(8), dp(8), p);
                p.setColor(alpha(accent, 225));
                canvas.drawRect(w * 0.31f, h * 0.31f, w * 0.66f, h * 0.35f, p);
                canvas.drawRect(w * 0.31f, h * 0.43f, w * 0.58f, h * 0.47f, p);
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(dp(5));
                p.setStrokeCap(Paint.Cap.ROUND);
                p.setColor(mix(INFO, GOLD_2, 0.35f));
                Path bag = new Path();
                bag.moveTo(w * 0.23f, h * 0.62f);
                bag.lineTo(w * 0.36f, h * 0.78f);
                bag.lineTo(w * 0.78f, h * 0.68f);
                bag.lineTo(w * 0.62f, h * 0.52f);
                canvas.drawPath(bag, p);
                p.setStyle(Paint.Style.FILL);
                p.setColor(GOLD_2);
                canvas.drawCircle(w * 0.75f, h * 0.24f, dp(10), p);
                drawMiniLabel(canvas, "خرید", w, h);
            }
        }
        private void drawBar(Canvas canvas, float x, float base, float width, float top, int color) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(color, 235));
            canvas.drawRoundRect(new RectF(x, top, x + width, base), dp(6), dp(6), p);
        }
        private void drawMiniLabel(Canvas canvas, String label, int w, int h) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(Color.BLACK, 78));
            canvas.drawRoundRect(new RectF(w * 0.22f, h * 0.72f, w * 0.78f, h * 0.91f), dp(10), dp(10), p);
            p.setColor(Color.WHITE);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(dp(10.5f));
            canvas.drawText(label, w / 2f, h * 0.85f, p);
        }
    }

    private void addDailyFinanceDashboardBlock(String type, String title, String sub, int iconRes, JSONObject data, int accent) {
        if (data == null) return;
        String date = data.optString("date", "");
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(accent, 42), SURFACE}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        View icon = themedDailyIcon(type, accent);
        LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(dp(78), dp(78));
        iconLp.setMargins(0, 0, dp(2), 0);
        head.addView(icon, iconLp);
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(10), 0);
        copy.addView(text(title, 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(sub + " • " + (date == null || date.isEmpty() ? "—" : date), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));

        JSONArray metrics = data.optJSONArray("metrics");
        if (metrics != null) {
            LinearLayout row = null;
            for (int i = 0; i < Math.min(metrics.length(), 6); i++) {
                if (i % 3 == 0) { row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); c.addView(row, new LinearLayout.LayoutParams(-1, -2)); }
                JSONObject m = metrics.optJSONObject(i);
                LinearLayout mm = metric(m.optString("label"), m.optString("value"));
                LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(0, -2, 1f);
                mlp.setMargins(dp(3), dp(8), dp(3), 0);
                if (row != null) row.addView(mm, mlp);
            }
        }
        JSONArray chartData = data.optJSONArray("chart");
        if (chartData != null && chartData.length() > 0) {
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, dp(172));
            cp.setMargins(0, dp(12), 0, 0);
            c.addView(new LineChartView(this, chartData, accent), cp);
        }

        EditText dateInput = input(date == null || date.isEmpty() ? "مثلا 1403/01/01" : date, date == null ? "" : date, false);
        LinearLayout.LayoutParams dip = new LinearLayout.LayoutParams(-1, dp(46));
        dip.setMargins(0, dp(12), 0, dp(8));
        c.addView(dateInput, dip);

        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        Button prev = secondaryButton("روز قبل");
        Button show = primaryButton("نمایش تاریخ");
        Button next = secondaryButton("روز بعد");
        prev.setTextSize(10.5f); show.setTextSize(10.5f); next.setTextSize(10.5f);
        prev.setOnClickListener(v -> showDashboardDailyView(type, dateInput.getText().toString().trim(), -1));
        show.setOnClickListener(v -> showDashboardDailyView(type, dateInput.getText().toString().trim(), 0));
        next.setOnClickListener(v -> showDashboardDailyView(type, dateInput.getText().toString().trim(), 1));
        LinearLayout.LayoutParams bp1 = new LinearLayout.LayoutParams(0, dp(42), 1f); bp1.setMargins(dp(3), 0, dp(3), 0);
        LinearLayout.LayoutParams bp2 = new LinearLayout.LayoutParams(0, dp(42), 1f); bp2.setMargins(dp(3), 0, dp(3), 0);
        LinearLayout.LayoutParams bp3 = new LinearLayout.LayoutParams(0, dp(42), 1f); bp3.setMargins(dp(3), 0, dp(3), 0);
        nav.addView(prev, bp1); nav.addView(show, bp2); nav.addView(next, bp3);
        c.addView(nav, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        Button docs = secondaryButton(type.equals("sales") ? "فاکتورها" : "اسناد خرید");
        Button cash = secondaryButton(type.equals("sales") ? "دریافتی‌ها" : "پرداختی‌ها");
        Button items = secondaryButton("اقلام روز");
        docs.setTextSize(10.5f); cash.setTextSize(10.5f); items.setTextSize(10.5f);
        docs.setOnClickListener(v -> showDashboardDailyList(type, dateInput.getText().toString().trim(), "documents"));
        cash.setOnClickListener(v -> showDashboardDailyList(type, dateInput.getText().toString().trim(), "payments"));
        items.setOnClickListener(v -> showDashboardDailyList(type, dateInput.getText().toString().trim(), "items"));
        LinearLayout.LayoutParams ap1 = new LinearLayout.LayoutParams(0, dp(40), 1f); ap1.setMargins(dp(3), dp(8), dp(3), 0);
        LinearLayout.LayoutParams ap2 = new LinearLayout.LayoutParams(0, dp(40), 1f); ap2.setMargins(dp(3), dp(8), dp(3), 0);
        LinearLayout.LayoutParams ap3 = new LinearLayout.LayoutParams(0, dp(40), 1f); ap3.setMargins(dp(3), dp(8), dp(3), 0);
        actions.addView(docs, ap1); actions.addView(cash, ap2); actions.addView(items, ap3);
        c.addView(actions, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void addCheckDashboardSection(String title, String sub, boolean incoming, JSONObject data, int accent) {
        if (data == null) return;
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(accent, 34), SURFACE}, GradientDrawable.Orientation.LEFT_RIGHT, 22));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        ImageView icon = new ImageView(this);
        icon.setImageResource(ir.meelano.android.R.drawable.icon_checks);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setPadding(dp(5), dp(5), dp(5), dp(5));
        icon.setBackground(roundedStroke(alpha(accent, 42), 16, alpha(accent, 100)));
        head.addView(icon, new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(10), 0);
        copy.addView(text(title, 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(sub + " • " + data.optString("date", "—"), 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        JSONArray metrics = data.optJSONArray("metrics");
        if (metrics != null) {
            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0; i < Math.min(3, metrics.length()); i++) {
                JSONObject m = metrics.optJSONObject(i);
                LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(0, -2, 1f); mp.setMargins(dp(3), dp(8), dp(3), 0);
                row.addView(metric(m.optString("label"), m.optString("value")), mp);
            }
            c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        }
        JSONArray rows = data.optJSONArray("breakdown");
        if (rows == null || rows.length() == 0) rows = data.optJSONArray("chart");
        if (rows != null) {
            for (int i = 0; i < Math.min(rows.length(), 7); i++) {
                JSONObject r = rows.optJSONObject(i);
                String status = r.optString("status", "");
                String label = r.optString("label", "دسته چک");
                LinearLayout line = checkDashboardRow(label, r.opt("amount"), r.opt("count"), accent);
                line.setOnClickListener(v -> showCheckList(incoming, status, label));
                c.addView(line, compactRowLp());
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private LinearLayout checkDashboardRow(String label, Object amount, Object count, int accent) {
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);
        line.setPadding(dp(9), dp(8), dp(9), dp(8));
        line.setClickable(true);
        line.setBackground(roundedStroke(alpha(accent, 18), 16, alpha(accent, 78)));
        TextView bullet = text("●", 18, accent, Typeface.BOLD);
        bullet.setGravity(Gravity.CENTER);
        line.addView(bullet, new LinearLayout.LayoutParams(dp(26), dp(42)));
        LinearLayout titleBox = new LinearLayout(this);
        titleBox.setOrientation(LinearLayout.VERTICAL);
        titleBox.setPadding(dp(6), 0, dp(8), 0);
        titleBox.addView(text(label, 12, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        titleBox.addView(text(formatNumber(count) + " فقره", 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        line.addView(titleBox, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView money = text(compactMoney(amount), 11.3f, TEXT, Typeface.BOLD);
        money.setGravity(Gravity.CENTER);
        money.setPadding(dp(9), dp(5), dp(9), dp(5));
        money.setBackground(roundedStroke(SURFACE_2, 999, alpha(accent, 92)));
        line.addView(money, new LinearLayout.LayoutParams(-2, -2));
        return line;
    }

    private void addDashboardInsightTable(JSONObject today) {
        addSmartCustomerSignalSection("مشتریان بدهکار", "۵ مشتری اول برای پیگیری سریع", today.optJSONArray("topDebtors"), "party", "amount", DANGER, ir.meelano.android.R.drawable.icon_customers);
        addSmartCustomerSignalSection("تسویه‌های گذشته", "سررسید، ویزیتور و مبلغ معوق بدون شلوغی", today.optJSONArray("overdueInvoices"), "party", "amount", WARNING, ir.meelano.android.R.drawable.icon_sales);
        addSmartCustomerSignalSection("مشتریان بدون خرید", "۵ مشتری اول که خرید ثبت‌شده ندارند", today.optJSONArray("inactiveCustomers"), "party", "hint", INFO, ir.meelano.android.R.drawable.icon_visitors);
        addSmartProductSignalSection("کالاهای فروخته‌شده روز", "۵ قلم اول با گروه، مقدار و مبلغ", today.optJSONArray("todayItems"), GOLD, ir.meelano.android.R.drawable.icon_products);
    }

    private void addSmartCustomerSignalSection(String title, String sub, JSONArray rows, String labelKey, String valueKey, int accent, int iconRes) {
        if (rows == null || rows.length() == 0) return;
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(accent, 34), SURFACE}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        icon.setPadding(dp(5), dp(5), dp(5), dp(5));
        icon.setBackground(roundedStroke(alpha(accent, 42), 16, alpha(accent, 90)));
        head.addView(icon, new LinearLayout.LayoutParams(dp(46), dp(46)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(10), 0);
        copy.addView(text(title, 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(sub, 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        JSONArray chart = new JSONArray();
        for (int i = 0; i < Math.min(rows.length(), 5); i++) {
            JSONObject r = rows.optJSONObject(i);
            JSONObject p = new JSONObject();
            try { p.put("label", String.valueOf(i + 1)); p.put("value", valueKey.equals("hint") ? 1 : Math.max(0, r.optDouble(valueKey, r.optDouble("amount", 0)))); chart.put(p); } catch (Exception ignored) { }
        }
        if (chart.length() > 0 && !valueKey.equals("hint")) {
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, dp(86)); cp.setMargins(0, dp(8), 0, 0);
            c.addView(new BarChartView(this, chart, accent), cp);
        }
        for (int i = 0; i < Math.min(rows.length(), 5); i++) {
            JSONObject r = rows.optJSONObject(i);
            String label = r.optString(labelKey, r.optString("party", "—"));
            String main = valueKey.equals("hint") ? r.optString("hint", "بدون خرید") : money(r.opt(valueKey));
            String meta = "";
            if (!r.optString("visitor", "").isEmpty()) meta = "ویزیتور: " + r.optString("visitor");
            if (!r.optString("dueDate", "").isEmpty()) meta = (meta.isEmpty() ? "" : meta + " • ") + "سررسید: " + r.optString("dueDate");
            if (!r.optString("hint", "").isEmpty() && !valueKey.equals("hint")) meta = (meta.isEmpty() ? "" : meta + " • ") + r.optString("hint");
            LinearLayout line = signalRow(i + 1, label, main, meta, accent);
            line.setOnClickListener(v -> openCustomerFromDashboard(r));
            c.addView(line, compactRowLp());
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void addSmartProductSignalSection(String title, String sub, JSONArray rows, int accent, int iconRes) {
        if (rows == null || rows.length() == 0) return;
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(accent, 34), SURFACE}, GradientDrawable.Orientation.LEFT_RIGHT, 22));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        ImageView icon = new ImageView(this); icon.setImageResource(iconRes); icon.setPadding(dp(5), dp(5), dp(5), dp(5)); icon.setBackground(roundedStroke(alpha(accent, 42), 16, alpha(accent, 90)));
        head.addView(icon, new LinearLayout.LayoutParams(dp(46), dp(46)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(10), 0);
        copy.addView(text(title, 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(sub, 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f)); c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        for (int i = 0; i < Math.min(rows.length(), 5); i++) {
            JSONObject r = rows.optJSONObject(i);
            String meta = r.optString("hint", "");
            LinearLayout line = signalRow(i + 1, r.optString("item", "کالا"), money(r.opt("amount")), meta, accent);
            line.setOnClickListener(v -> openProductFromDashboard(r));
            c.addView(line, compactRowLp());
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private LinearLayout signalRow(int index, String label, String value, String meta, int accent) {
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);
        line.setPadding(dp(9), dp(8), dp(9), dp(8));
        line.setClickable(true);
        line.setBackground(roundedStroke(alpha(accent, 18), 16, alpha(accent, 70)));
        TextView num = text(String.valueOf(index), 12, TEXT, Typeface.BOLD);
        num.setGravity(Gravity.CENTER);
        num.setBackground(gradient(new int[]{alpha(accent, 150), alpha(GOLD_2, 72)}, GradientDrawable.Orientation.TL_BR, 999));
        line.addView(num, new LinearLayout.LayoutParams(dp(34), dp(34)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(9), 0, dp(9), 0);
        copy.addView(text(label, 11.7f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        if (meta != null && !meta.isEmpty()) copy.addView(text(meta, 9.6f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        line.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView val = text(value, 10.8f, TEXT, Typeface.BOLD);
        val.setGravity(Gravity.CENTER);
        val.setPadding(dp(8), dp(5), dp(8), dp(5));
        val.setBackground(roundedStroke(SURFACE_2, 999, alpha(accent, 82)));
        line.addView(val, new LinearLayout.LayoutParams(-2, -2));
        return line;
    }

    private void addDashboardBankTable(JSONArray banks) {
        if (banks == null || banks.length() == 0) return;
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(INFO, 38), alpha(GOLD, 20), SURFACE}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("موجودی بانک‌ها", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("آیکن سه‌بعدی هر بانک همراه با موجودی، ورودی و خروجی؛ هماهنگ با تم فعال", 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        for (int i = 0; i < Math.min(6, banks.length()); i++) {
            JSONObject b = banks.optJSONObject(i);
            LinearLayout row = bankIconRow(i, b, INFO);
            c.addView(row, compactRowLp());
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private LinearLayout bankIconRow(int index, JSONObject b, int accent) {
        String bankName = b == null ? "بانک" : b.optString("label", "بانک");
        String branch = b == null ? "" : b.optString("branch", "");
        boolean duplicate = b != null && b.optBoolean("duplicate", false);
        int brand = bankBrandColor(bankName, accent);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(9), dp(8), dp(9), dp(8));
        row.setBackground(roundedStroke(alpha(brand, 18), 16, alpha(brand, 82)));
        row.setContentDescription(bankName);
        TextView icon = text(bankIconText(bankName) + (duplicate && !branch.isEmpty() ? "\n" + branch : ""), duplicate && !branch.isEmpty() ? 10.2f : 12.5f, Color.WHITE, Typeface.BOLD);
        icon.setGravity(Gravity.CENTER);
        icon.setMaxLines(2);
        icon.setShadowLayer(dp(2), 0, dp(1), alpha(Color.BLACK, 120));
        icon.setBackground(gradient(new int[]{alpha(brand, 235), alpha(GOLD_2, 125)}, GradientDrawable.Orientation.TL_BR, 18));
        row.addView(icon, new LinearLayout.LayoutParams(dp(54), dp(54)));
        LinearLayout values = new LinearLayout(this);
        values.setOrientation(LinearLayout.VERTICAL);
        values.setPadding(dp(9), 0, dp(9), 0);
        if (duplicate && !branch.isEmpty()) values.addView(bankValueLine("شعبه", branch, brand), new LinearLayout.LayoutParams(-1, -2));
        values.addView(bankValueLine("موجودی", money(b == null ? 0 : b.opt("balance")), SUCCESS), new LinearLayout.LayoutParams(-1, -2));
        values.addView(bankValueLine("ورودی", money(b == null ? 0 : b.opt("inflow")), INFO), new LinearLayout.LayoutParams(-1, -2));
        values.addView(bankValueLine("خروجی", money(b == null ? 0 : b.opt("outflow")), WARNING), new LinearLayout.LayoutParams(-1, -2));
        row.addView(values, new LinearLayout.LayoutParams(0, -2, 1f));
        return row;
    }

    private String bankIconText(String bankName) {
        String n = bankName == null ? "" : bankName.replace("بانک", "").trim();
        if (n.contains("ملی")) return "ملی";
        if (n.contains("ملت")) return "ملت";
        if (n.contains("صادرات")) return "صاد";
        if (n.contains("تجارت")) return "تج";
        if (n.contains("سپه")) return "سپه";
        if (n.contains("پاسارگاد")) return "پاس";
        if (n.contains("پارسیان")) return "پار";
        if (n.contains("سامان")) return "سام";
        if (n.contains("شهر")) return "شهر";
        if (n.contains("رفاه")) return "رفاه";
        if (n.contains("کشاورزی")) return "کش";
        if (n.contains("مسکن")) return "مسکن";
        if (n.contains("اقتصاد")) return "اقتصاد";
        if (n.contains("رسالت")) return "رسالت";
        if (n.contains("آینده") || n.contains("اينده")) return "آینده";
        if (n.contains("دی")) return "دی";
        if (n.length() <= 4 && !n.isEmpty()) return n;
        return n.isEmpty() ? "بانک" : n.substring(0, Math.min(3, n.length()));
    }

    private int bankBrandColor(String bankName, int fallback) {
        String n = bankName == null ? "" : bankName;
        if (n.contains("ملی")) return Color.rgb(210, 36, 48);
        if (n.contains("ملت")) return Color.rgb(196, 32, 45);
        if (n.contains("صادرات")) return Color.rgb(42, 79, 170);
        if (n.contains("تجارت")) return Color.rgb(38, 99, 183);
        if (n.contains("سپه")) return Color.rgb(28, 111, 78);
        if (n.contains("پاسارگاد")) return Color.rgb(193, 143, 48);
        if (n.contains("پارسیان")) return Color.rgb(199, 132, 35);
        if (n.contains("سامان")) return Color.rgb(0, 130, 198);
        if (n.contains("شهر")) return Color.rgb(118, 64, 170);
        if (n.contains("رفاه")) return Color.rgb(31, 154, 92);
        if (n.contains("کشاورزی")) return Color.rgb(33, 144, 74);
        if (n.contains("مسکن")) return Color.rgb(236, 104, 38);
        return fallback;
    }

    private LinearLayout bankValueLine(String label, String value, int accent) {
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);
        TextView l = text(label, 9.8f, MUTED, Typeface.BOLD);
        TextView v = text(value, 10.8f, TEXT, Typeface.BOLD);
        v.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        v.setPadding(dp(7), dp(3), dp(7), dp(3));
        v.setBackground(roundedStroke(SURFACE_2, 999, alpha(accent, 70)));
        line.addView(l, new LinearLayout.LayoutParams(dp(58), -2));
        line.addView(v, new LinearLayout.LayoutParams(0, -2, 1f));
        return line;
    }

    private LinearLayout compactActionRow(String title, String value, int accent) {
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);
        line.setPadding(dp(9), dp(8), dp(9), dp(8));
        line.setClickable(true);
        line.setBackground(roundedStroke(alpha(accent, 24), 14, alpha(accent, 82)));
        TextView name = text(title, 11.2f, TEXT, Typeface.BOLD);
        TextView val = text(value, 10.2f, accent, Typeface.BOLD);
        val.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        val.setMaxLines(2);
        line.addView(name, new LinearLayout.LayoutParams(0, -2, 1f));
        line.addView(val, new LinearLayout.LayoutParams(-2, -2));
        return line;
    }

    private LinearLayout.LayoutParams compactRowLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, dp(7), 0, 0);
        return lp;
    }

    private void openCustomerFromDashboard(JSONObject r) {
        JSONObject c = new JSONObject();
        try {
            c.put("کد", r.optString("code", r.optString("customerCode", "")));
            c.put("نام", r.optString("party", r.optString("label", "مشتری")));
            c.put("مانده", r.optDouble("amount", 0));
            c.put("_back", "dashboard");
        } catch (Exception ignored) { }
        showCustomerDetail(c, "all");
    }

    private void openProductFromDashboard(JSONObject r) {
        JSONObject p = new JSONObject();
        try {
            p.put("کد", r.optString("code", r.optString("productCode", "")));
            p.put("نام", r.optString("item", r.optString("label", "کالا")));
            p.put("مبلغ_فروش", r.optDouble("amount", 0));
            p.put("گروه", r.optString("hint", ""));
        } catch (Exception ignored) { }
        showProductDialog(p);
    }

    private void showDashboardDailyView(String type, String date, int step) {
        content.removeAllViews();
        addHero(type.equals("sales") ? "جزئیات فروش روز" : "جزئیات خرید روز", "انتخاب تاریخ و مشاهده منحنی همان روز");
        Button back = secondaryButton("بازگشت به داشبورد");
        back.setOnClickListener(v -> showApp("dashboard"));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(12)); content.addView(back, bp);
        addLoading(content, "در حال دریافت اطلاعات روز…");
        runDb(() -> queryDashboardDailyDetail(type, date, step), new DbCallback() {
            @Override public void ok(String body) {
                try { renderDashboardDailyDetail(new JSONObject(body)); }
                catch (Exception e) { showPageError("جزئیات روز", e, () -> showDashboardDailyView(type, date, step)); }
            }
            @Override public void fail(Exception e) { showPageError("جزئیات روز", e, () -> showDashboardDailyView(type, date, step)); }
        });
    }

    private void renderDashboardDailyDetail(JSONObject r) {
        content.removeAllViews();
        String type = r.optString("type", "sales");
        int accent = type.equals("sales") ? GOLD : INFO;
        addHero(type.equals("sales") ? "جزئیات فروش روز" : "جزئیات خرید روز", "تاریخ انتخابی: " + r.optString("date", "—"));
        Button back = secondaryButton("بازگشت به داشبورد");
        back.setOnClickListener(v -> showApp("dashboard"));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(12)); content.addView(back, bp);
        JSONObject block = new JSONObject();
        try { block.put("date", r.optString("date")); block.put("metrics", r.optJSONArray("metrics")); block.put("chart", r.optJSONArray("chart")); } catch (Exception ignored) { }
        addDailyFinanceDashboardBlock(type, type.equals("sales") ? "فروش روز" : "خرید روز", "نمای جزئیات تاریخ انتخابی و دسترسی سریع به لیست‌ها", type.equals("sales") ? ir.meelano.android.R.drawable.icon_sales : ir.meelano.android.R.drawable.icon_products, block, accent);
    }

    private void showDashboardDailyList(String type, String date, String kind) {
        content.removeAllViews();
        addHero(type.equals("sales") ? "لیست‌های فروش روز" : "لیست‌های خرید روز", "در حال آماده‌سازی لیست کامل با جزئیات");
        addLoading(content, "در حال دریافت لیست…");
        runDb(() -> queryDashboardDailyDetail(type, date, 0), new DbCallback() {
            @Override public void ok(String body) {
                try { renderDashboardDailyList(new JSONObject(body), kind); }
                catch (Exception e) { showPageError("لیست روز", e, () -> showDashboardDailyList(type, date, kind)); }
            }
            @Override public void fail(Exception e) { showPageError("لیست روز", e, () -> showDashboardDailyList(type, date, kind)); }
        });
    }

    private void renderDashboardDailyList(JSONObject r, String kind) {
        content.removeAllViews();
        String type = r.optString("type", "sales");
        String date = r.optString("date", "");
        String title = "payments".equals(kind) ? (type.equals("sales") ? "دریافتی‌های روز" : "پرداختی‌های روز") : ("items".equals(kind) ? "اقلام روز" : (type.equals("sales") ? "فاکتورهای فروش روز" : "اسناد خرید روز"));
        addHero(title, "تاریخ: " + date);
        Button back = secondaryButton("بازگشت به جزئیات روز");
        back.setOnClickListener(v -> showDashboardDailyView(type, date, 0));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(12)); content.addView(back, bp);
        if ("payments".equals(kind)) addPaymentRows(title, r.optJSONArray("payments"));
        else if ("items".equals(kind)) addDailyRows(title, r.optJSONArray("items"), false);
        else addDailyRows(title, r.optJSONArray("documents"), true);
    }

    private String queryDashboardDailyDetail(String type, String requestedDate, int step) throws Exception {
        boolean sales = "sales".equals(type);
        String table = sales ? "sailfact" : "buyfact";
        String dateCol;
        String actualDate;
        try (Connection c = openConnection()) {
            Set<String> cols = columns(c, table);
            dateCol = sales ? resolve(cols, "date") : resolve(cols, "DATE", "date");
            actualDate = requestedDate == null || requestedDate.trim().isEmpty() ? latestDate(c, table, dateCol) : requestedDate.trim();
            if (step != 0) actualDate = adjacentDate(c, table, dateCol, actualDate, step);
        }
        JSONObject r = new JSONObject(queryDailyReport(type, actualDate));
        try (Connection c = openConnection()) {
            r.put("chart", queryDailyTrend(c, type, r.optString("date", actualDate)));
            r.put("payments", queryDailyPayments(c, type, r.optString("date", actualDate)));
        }
        return r.toString();
    }

    private String adjacentDate(Connection c, String table, String dateCol, String date, int step) {
        if (dateCol == null || date == null || date.trim().isEmpty()) return date;
        String op = step < 0 ? "<" : ">";
        String ord = step < 0 ? "DESC" : "ASC";
        try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) [" + dateCol + "] FROM dbo.[" + table + "] WHERE [" + dateCol + "] " + op + " ? GROUP BY [" + dateCol + "] ORDER BY [" + dateCol + "] " + ord)) {
            ps.setString(1, date);
            try (ResultSet r = ps.executeQuery()) { if (r.next() && r.getString(1) != null) return r.getString(1); }
        } catch (Exception ignored) { }
        return date;
    }

    private JSONArray queryDailyTrend(Connection c, String type, String date) throws Exception {
        boolean sales = "sales".equals(type);
        String table = sales ? "sailfact" : "buyfact";
        Set<String> cols = columns(c, table);
        String dateCol = sales ? resolve(cols, "date") : resolve(cols, "DATE", "date");
        String amountCol = resolve(cols, "all");
        if (dateCol == null || amountCol == null || date == null || date.isEmpty()) return new JSONArray();
        String where = "WHERE [" + dateCol + "]<=?" + activeAnd(cols, "");
        List<Object> params = new ArrayList<>(); params.add(date);
        if (sales && session != null && session.visitorId != null && hasCol(cols, "vis_rdf")) { where += " AND TRY_CONVERT(int,[vis_rdf])=?"; params.add(session.visitorId); }
        String sql = "SELECT TOP (7) [" + dateCol + "], ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + amountCol + "])),0) FROM dbo.[" + table + "] " + where + " GROUP BY [" + dateCol + "] ORDER BY [" + dateCol + "] DESC";
        return reverse(readPoints(c, sql, params));
    }

    private JSONArray queryDailyPayments(Connection c, String type, String date) throws Exception {
        boolean sales = "sales".equals(type);
        String table = sales ? "sailfact" : "buyfact";
        Set<String> h = columns(c, table); Set<String> cust = columns(c, "CUSTOMERS");
        String dateCol = sales ? resolve(h, "date") : resolve(h, "DATE", "date");
        String numberCol = sales ? resolve(h, "shfacfo") : resolve(h, "shfackh");
        String amountCol = sales ? resolve(h, "MabDaryaftFactor", "Daryaft", "received") : resolve(h, "MablaghPardakht", "Pardakht", "paid");
        if (dateCol == null || numberCol == null || amountCol == null || date == null || date.isEmpty()) return new JSONArray();
        String headerParty = hasCol(h, "moname") ? "TRY_CONVERT(nvarchar(250),h.moname)" : "N'بدون نام'";
        boolean canJoinCustomer = hasCol(cust, "SHMO") && hasCol(h, "shmo") && hasCol(cust, "MONAME");
        String partyExpr = canJoinCustomer ? "COALESCE(TRY_CONVERT(nvarchar(250),c.MONAME)," + headerParty + ",N'بدون نام')" : "COALESCE(" + headerParty + ",N'بدون نام')";
        String sql = "SELECT TOP (150) TRY_CONVERT(nvarchar(80),h.[" + numberCol + "]), " + partyExpr + ", TRY_CONVERT(decimal(19,2),h.[" + amountCol + "]), TRY_CONVERT(nvarchar(500)," + (hasCol(h, "description") ? "h.description" : (hasCol(h, "Explain") ? "h.[Explain]" : "NULL")) + ") FROM dbo.[" + table + "] h " + (canJoinCustomer ? "LEFT JOIN dbo.CUSTOMERS c ON c.SHMO=h.shmo " : "") + " WHERE h.[" + dateCol + "]=? AND ISNULL(TRY_CONVERT(decimal(19,2),h.[" + amountCol + "]),0)>0" + activeAnd(h, "h") + " ORDER BY h.[" + numberCol + "]";
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1, date); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("number", stringOr(r.getString(1), "—")); o.put("party", stringOr(r.getString(2), "بدون نام")); o.put("amount", r.getDouble(3)); o.put("description", stringOr(r.getString(4), "")); arr.put(o); } } }
        return arr;
    }

    private void addPaymentRows(String title, JSONArray rows) {
        LinearLayout c = card();
        c.addView(text(title, 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        if (rows == null || rows.length() == 0) { TextView empty = text("دریافت/پرداختی برای این تاریخ ثبت نشده است.", 12, MUTED, Typeface.NORMAL); empty.setGravity(Gravity.CENTER); c.addView(empty, new LinearLayout.LayoutParams(-1, dp(70))); }
        else {
            for (int i = 0; i < Math.min(150, rows.length()); i++) {
                JSONObject row = rows.optJSONObject(i);
                LinearLayout item = new LinearLayout(this); item.setOrientation(LinearLayout.VERTICAL); item.setPadding(dp(10), dp(10), dp(10), dp(10)); item.setBackground(roundedStroke(SURFACE_2, 15, BORDER));
                item.addView(text("سند: " + row.optString("number", "—") + "   |   " + row.optString("party", "بدون نام"), 12.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
                item.addView(text("مبلغ: " + money(row.opt("amount")), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
                if (!row.optString("description", "").isEmpty()) item.addView(text("توضیحات: " + row.optString("description"), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
                LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(-1, -2); ilp.setMargins(0, dp(8), 0, 0); c.addView(item, ilp);
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void showCheckList(boolean incoming, String statusValue, String label) {
        content.removeAllViews();
        addHero(incoming ? "جزئیات چک‌های دریافتی" : "جزئیات چک‌های پرداختی", label == null ? "" : label);
        Button back = secondaryButton("بازگشت به داشبورد"); back.setOnClickListener(v -> showApp("dashboard"));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(12)); content.addView(back, bp);
        addLoading(content, "در حال دریافت چک‌ها…");
        runDb(() -> queryCheckList(incoming, statusValue), new DbCallback() {
            @Override public void ok(String body) { try { renderCheckList(incoming, label, new JSONArray(body)); } catch (Exception e) { showPageError("چک‌ها", e, () -> showCheckList(incoming, statusValue, label)); } }
            @Override public void fail(Exception e) { showPageError("چک‌ها", e, () -> showCheckList(incoming, statusValue, label)); }
        });
    }

    private void renderCheckList(boolean incoming, String label, JSONArray rows) {
        content.removeAllViews();
        addHero(incoming ? "جزئیات چک‌های دریافتی" : "جزئیات چک‌های پرداختی", label == null ? "" : label);
        Button back = secondaryButton("بازگشت به داشبورد"); back.setOnClickListener(v -> showApp("dashboard"));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(12)); content.addView(back, bp);
        if (rows == null || rows.length() == 0) { addEmptyTo(content, "چکی برای این دسته پیدا نشد."); return; }
        for (int i = 0; i < rows.length(); i++) {
            JSONObject r = rows.optJSONObject(i);
            LinearLayout item = card(); item.setBackground(roundedStroke(SURFACE, 18, alpha(incoming ? SUCCESS : WARNING, 70)));
            item.addView(text("شماره چک: " + r.optString("number", "—") + "   |   " + r.optString("date", "—"), 13, incoming ? SUCCESS : WARNING, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            item.addView(text("طرف حساب: " + r.optString("party", "—") + "   |   بانک: " + r.optString("bank", "—"), 11.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            item.addView(text("مبلغ: " + money(r.opt("amount")) + "   |   دسته چک: " + r.optString("statusLabel", "—"), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
            if (!r.optString("description", "").isEmpty()) item.addView(text("توضیحات: " + r.optString("description"), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); content.addView(item, lp);
        }
    }

    private String queryCheckList(boolean incoming, String statusValue) throws Exception {
        JSONArray arr = new JSONArray();
        try (Connection c = openConnection()) {
            String table = incoming ? "getchk" : "putchk";
            Set<String> cols = columns(c, table); Set<String> typeCols = columns(c, "CheckTypes"); Set<String> bankCols = columns(c, "BANK"); Set<String> cust = columns(c, "CUSTOMERS");
            String amount = incoming ? resolve(cols, "getchkmab", "mablagh", "amount") : resolve(cols, "putchkmab", "mablagh", "amount");
            String status = incoming ? resolve(cols, "chk_satus", "status") : resolve(cols, "putchk_status", "status");
            if (amount == null) return arr.toString();
            String date = incoming ? resolve(cols, "getchkdate", "chkdate", "date", "sarresid", "t_date") : resolve(cols, "putchkdate", "chkdate", "date", "sarresid", "t_date");
            String number = incoming ? resolve(cols, "getchknum", "chknum", "number", "serial") : resolve(cols, "putchknum", "chknum", "number", "serial");
            String bankRef = incoming ? resolve(cols, "our_bankrdf", "bankrdf", "BankRDF") : resolve(cols, "bankrdf", "our_bankrdf", "BankRDF");
            String shmo = resolve(cols, "shmo", "SHMO");
            String desc = resolve(cols, "description", "Explain", "tozihat");
            String raw = status == null ? "CAST(NULL AS nvarchar(50))" : "TRY_CONVERT(nvarchar(50),x.[" + status + "])";
            String statusExpr = status == null ? "N'نامشخص'" : (hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? "COALESCE(TRY_CONVERT(nvarchar(120),t.Desciption),N'دسته '+" + raw + ")" : "N'دسته '+" + raw);
            String join = status != null && hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? " LEFT JOIN dbo.CheckTypes t ON TRY_CONVERT(nvarchar(50),t.ID)=" + raw : "";
            String bankExpr = "N'—'";
            if (bankRef != null && hasCol(bankCols, "RDF") && hasCol(bankCols, "BANKNAME")) { join += " LEFT JOIN dbo.BANK b ON TRY_CONVERT(nvarchar(100),b.RDF)=TRY_CONVERT(nvarchar(100),x.[" + bankRef + "])"; bankExpr = "COALESCE(TRY_CONVERT(nvarchar(200),b.BANKNAME),N'—')"; }
            String partyExpr = "N'—'";
            if (shmo != null && hasCol(cust, "SHMO") && hasCol(cust, "MONAME")) { join += " LEFT JOIN dbo.CUSTOMERS c ON TRY_CONVERT(nvarchar(100),c.SHMO)=TRY_CONVERT(nvarchar(100),x.[" + shmo + "])"; partyExpr = "COALESCE(TRY_CONVERT(nvarchar(250),c.MONAME),TRY_CONVERT(nvarchar(100),x.[" + shmo + "]),N'—')"; }
            String where = ""; List<Object> params = new ArrayList<>();
            if (status != null && statusValue != null && !statusValue.trim().isEmpty()) {
                if ("__blank__".equals(statusValue.trim())) where = " WHERE (" + raw + " IN (N'4',N'سفید',N'blank',N'unused')" + (hasCol(typeCols, "Desciption") ? " OR TRY_CONVERT(nvarchar(120),t.Desciption) LIKE N'%سفید%' OR TRY_CONVERT(nvarchar(120),t.Desciption) LIKE N'%استفاده%'" : "") + ")";
                else { where = " WHERE " + raw + "=?"; params.add(statusValue.trim()); }
            }
            String sql = "SELECT TOP (200) " + (date == null ? "CAST(NULL AS nvarchar(30))" : "TRY_CONVERT(nvarchar(30),x.[" + date + "])") + ", " + (number == null ? "CAST(NULL AS nvarchar(80))" : "TRY_CONVERT(nvarchar(80),x.[" + number + "])") + ", TRY_CONVERT(decimal(19,2),x.[" + amount + "]), " + raw + ", " + statusExpr + ", " + bankExpr + ", " + partyExpr + ", " + (desc == null ? "CAST(NULL AS nvarchar(500))" : "TRY_CONVERT(nvarchar(500),x.[" + desc + "])") + " FROM dbo.[" + table + "] x" + join + where + " ORDER BY 1 DESC";
            try (PreparedStatement ps = c.prepareStatement(sql)) { setParams(ps, params); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("date", stringOr(r.getString(1), "—")); o.put("number", stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("status", stringOr(r.getString(4), "")); o.put("statusLabel", friendlyCheckStatus(incoming, r.getString(4), r.getString(5))); o.put("bank", stringOr(r.getString(6), "—")); o.put("party", stringOr(r.getString(7), "—")); o.put("description", stringOr(r.getString(8), "")); arr.put(o); } } }
        }
        return arr.toString();
    }

    private JSONObject queryTodayDashboard(Connection c) throws Exception {
        JSONObject out = new JSONObject();
        String salesDate = latestDate(c, "sailfact", "date");
        String purchaseDate = latestDate(c, "buyfact", "DATE");
        out.put("sales", queryDailyDashboardBlock(c, true, salesDate));
        out.put("purchases", queryDailyDashboardBlock(c, false, purchaseDate));
        out.put("getChecks", queryCheckDashboardBlock(c, true));
        out.put("putChecks", queryCheckDashboardBlock(c, false));
        out.put("topDebtors", queryTopDebtors(c));
        out.put("overdueInvoices", queryOverdueInvoices(c));
        out.put("inactiveCustomers", queryInactiveCustomers(c));
        out.put("todayItems", queryTodaySoldItems(c, salesDate));
        out.put("banks", loadBanks(c));
        return out;
    }

    private JSONObject queryDailyDashboardBlock(Connection c, boolean sales, String date) throws Exception {
        JSONObject out = new JSONObject();
        out.put("date", date == null || date.isEmpty() ? "—" : date);
        JSONArray metrics = new JSONArray();
        JSONArray chart = new JSONArray();
        String table = sales ? "sailfact" : "buyfact";
        Set<String> cols = columns(c, table);
        String dateCol = sales ? resolve(cols, "date") : resolve(cols, "DATE", "date");
        String amountCol = resolve(cols, "all");
        String numberCol = sales ? resolve(cols, "shfacfo") : resolve(cols, "shfackh");
        String partyCol = resolve(cols, "shmo");
        String paidCol = sales ? resolve(cols, "MabDaryaftFactor", "Daryaft", "received") : resolve(cols, "MablaghPardakht", "Pardakht", "paid");
        String discountCol = resolve(cols, "tafif", "takhfif", "discount");
        String taxCol = resolve(cols, "tax", "Tax", "maliat");
        double total = 0, paid = 0, discount = 0, tax = 0; long docs = 0, parties = 0;
        if (dateCol != null && amountCol != null && date != null && !date.isEmpty()) {
            String where = "WHERE [" + dateCol + "]=?" + activeAnd(cols, "");
            String sql = "SELECT ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + amountCol + "])),0), COUNT_BIG(" + (numberCol == null ? "1" : "[" + numberCol + "]") + "), " +
                    (partyCol == null ? "CAST(0 AS bigint)" : "COUNT(DISTINCT [" + partyCol + "])") + ", " +
                    (paidCol == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + paidCol + "])),0)") + ", " +
                    (discountCol == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + discountCol + "])),0)") + ", " +
                    (taxCol == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + taxCol + "])),0)") + " FROM dbo.[" + table + "] " + where;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, date);
                try (ResultSet r = ps.executeQuery()) {
                    if (r.next()) { total = r.getDouble(1); docs = r.getLong(2); parties = r.getLong(3); paid = r.getDouble(4); discount = r.getDouble(5); tax = r.getDouble(6); }
                }
            }
            String trendSql = "SELECT TOP (7) [" + dateCol + "], ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + amountCol + "])),0) FROM dbo.[" + table + "] " + (activeWhere(cols, "").isEmpty() ? "" : activeWhere(cols, "")) + " GROUP BY [" + dateCol + "] ORDER BY [" + dateCol + "] DESC";
            chart = reverse(readPoints(c, trendSql, new ArrayList<>()));
        }
        addMetric(metrics, sales ? "جمع فروش" : "جمع خرید", money(total));
        addMetric(metrics, "تعداد اسناد", formatNumber(docs));
        addMetric(metrics, sales ? "تعداد مشتری" : "طرف‌حساب", formatNumber(parties));
        addMetric(metrics, sales ? "دریافتی" : "پرداختی", money(paid));
        addMetric(metrics, "تخفیف", money(discount));
        addMetric(metrics, "مالیات", money(tax));
        out.put("metrics", metrics); out.put("chart", chart); return out;
    }

    private JSONObject queryCheckDashboardBlock(Connection c, boolean incoming) throws Exception {
        JSONObject out = new JSONObject();
        String table = incoming ? "getchk" : "putchk";
        Set<String> cols = columns(c, table);
        String amount = incoming ? resolve(cols, "getchkmab", "mablagh", "amount") : resolve(cols, "putchkmab", "mablagh", "amount");
        String statusCol = incoming ? resolve(cols, "chk_satus", "status") : resolve(cols, "putchk_status", "status");
        String dateCol = incoming ? resolve(cols, "getchkdate", "chkdate", "date", "sarresid", "t_date") : resolve(cols, "putchkdate", "chkdate", "date", "sarresid", "t_date");
        String date = latestDate(c, table, dateCol);
        out.put("date", date == null || date.isEmpty() ? "—" : date);
        JSONArray metrics = new JSONArray(); JSONArray chart = new JSONArray(); JSONArray breakdown = new JSONArray(); double total = 0; long count = 0;
        String where = dateCol == null || date == null || date.isEmpty() ? "" : " WHERE [" + dateCol + "]=?";
        if (amount != null) {
            try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(1), ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + amount + "])),0) FROM dbo.[" + table + "]" + where)) {
                if (!where.isEmpty()) ps.setString(1, date);
                try (ResultSet r = ps.executeQuery()) { if (r.next()) { count = r.getLong(1); total = r.getDouble(2); } }
            }
        }
        if (amount != null) {
            if (statusCol != null) {
                Set<String> typeCols = columns(c, "CheckTypes");
                String raw = "TRY_CONVERT(nvarchar(50),x.[" + statusCol + "])";
                String label = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? "COALESCE(TRY_CONVERT(nvarchar(120),t.Desciption),N'دسته '+" + raw + ")" : "N'دسته '+" + raw;
                String join = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? " LEFT JOIN dbo.CheckTypes t ON TRY_CONVERT(nvarchar(50),t.ID)=" + raw : "";
                String sql = "SELECT TOP (8) " + raw + ", " + label + ", COUNT_BIG(1), ISNULL(SUM(TRY_CONVERT(decimal(19,2),x.[" + amount + "])),0) FROM dbo.[" + table + "] x" + join + " GROUP BY " + raw + ", " + label + " ORDER BY 4 DESC";
                try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
                    while (r.next()) {
                        String status = stringOr(r.getString(1), "");
                        String nice = friendlyCheckStatus(incoming, status, r.getString(2));
                        JSONObject o = new JSONObject();
                        o.put("status", status);
                        o.put("label", nice);
                        o.put("count", r.getLong(3));
                        o.put("amount", r.getDouble(4));
                        o.put("value", r.getDouble(4));
                        breakdown.put(o); chart.put(o);
                    }
                }
            } else {
                JSONObject o = new JSONObject(); o.put("status", ""); o.put("label", incoming ? "همه چک‌های دریافتی" : "همه چک‌های پرداختی"); o.put("count", count); o.put("amount", total); o.put("value", total); breakdown.put(o); chart.put(o);
            }
        }
        if (!incoming) ensureBlankCheckCategory(breakdown, chart);
        addMetric(metrics, "تعداد چک", formatNumber(count));
        addMetric(metrics, "جمع مبلغ", compactMoney(total));
        addMetric(metrics, incoming ? "نوع" : "نوع", incoming ? "دریافتی" : "پرداختی");
        out.put("metrics", metrics); out.put("chart", chart); out.put("breakdown", breakdown); return out;
    }

    private String friendlyCheckStatus(boolean incoming, String status, String label) {
        String l = label == null ? "" : label.toLowerCase(Locale.US);
        String s = status == null ? "" : status.trim();
        if (incoming) {
            if (l.contains("صندوق") || l.contains("sandogh") || s.equals("0")) return "موجود در صندوق";
            if (l.contains("بانک") || l.contains("bank")) return "نزد بانک";
            if (l.contains("خرج") || l.contains("انتقال") || s.equals("3")) return "خرج‌شده";
            if (l.contains("استرد") || l.contains("برگشت") || l.contains("return") || s.equals("2")) return "استرداد / برگشتی";
            if (l.contains("وصول") || l.contains("پاس") || s.equals("1")) return "وصول / پاس‌شده";
            return stringOr(label, translateStatus(s));
        } else {
            if (l.contains("سفید") || l.contains("استفاده") || l.contains("blank") || l.contains("unused") || s.equals("4")) return "سفید / استفاده‌نشده";
            if (l.contains("پاس") || l.contains("وصول") || s.equals("1")) return "پاس‌شده";
            if (l.contains("راه") || l.contains("جاری") || l.contains("ثبت") || s.equals("0")) return "در راه / جاری";
            if (l.contains("برگشت") || l.contains("رد") || s.equals("2")) return "برگشتی / رد شده";
            if (l.contains("خرج") || l.contains("انتقال") || s.equals("3")) return "انتقال‌یافته";
            return stringOr(label, translateStatus(s));
        }
    }

    private void ensureBlankCheckCategory(JSONArray breakdown, JSONArray chart) throws Exception {
        if (breakdown == null) return;
        for (int i = 0; i < breakdown.length(); i++) {
            JSONObject o = breakdown.optJSONObject(i);
            String l = o == null ? "" : o.optString("label", "");
            if (l.contains("سفید") || l.contains("استفاده‌نشده")) return;
        }
        JSONObject blank = new JSONObject();
        blank.put("status", "__blank__");
        blank.put("label", "سفید / استفاده‌نشده");
        blank.put("count", 0);
        blank.put("amount", 0);
        blank.put("value", 0);
        breakdown.put(blank);
        if (chart != null) chart.put(blank);
    }

    private JSONArray queryTopDebtors(Connection c) throws Exception {
        Set<String> cols = columns(c, "CUSTOMERS");
        String shmo = resolve(cols, "SHMO", "shmo"); String name = resolve(cols, "MONAME", "Name", "CusName"); String balance = resolve(cols, "man", "Balance", "Mandeh");
        if (balance == null || shmo == null) return new JSONArray();
        String label = name == null ? "TRY_CONVERT(nvarchar(120),c.[" + shmo + "])" : "TRY_CONVERT(nvarchar(250),c.[" + name + "])";
        String sql = "SELECT TOP (8) TRY_CONVERT(nvarchar(100),c.[" + shmo + "]), " + label + ", TRY_CONVERT(decimal(19,2),c.[" + balance + "]) FROM dbo.CUSTOMERS c WHERE TRY_CONVERT(decimal(19,2),c.[" + balance + "])>0 ORDER BY 3 DESC";
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("party", stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("hint", "مانده بدهی"); o.put("value", r.getDouble(3)); arr.put(o); }
        }
        return arr;
    }

    private JSONArray queryOverdueInvoices(Connection c) throws Exception {
        Set<String> sail = columns(c, "sailfact"); Set<String> cust = columns(c, "CUSTOMERS"); Set<String> vis = columns(c, "visitors");
        if (!hasCol(sail, "t_date") || !hasCol(sail, "tasvieh") || !hasCol(sail, "all") || !hasFunction(c, "dif_date_alan")) return new JSONArray();
        String shmo = resolve(sail, "shmo"); String custCode = resolve(cust, "SHMO", "shmo"); String custName = resolve(cust, "MONAME", "Name", "CusName");
        String visitorId = resolve(sail, "vis_rdf", "VisitorID", "visitor"); String visKey = resolve(vis, "rdf", "RDF", "id", "ID"); String visName = resolve(vis, "name", "Name", "vis_name", "VisitorName", "moname");
        String nameExpr = custName != null && shmo != null && custCode != null ? "COALESCE(TRY_CONVERT(nvarchar(250),c.[" + custName + "]),N'بدون نام')" : "N'بدون نام'";
        String codeExpr = shmo == null ? "CAST(NULL AS nvarchar(100))" : "TRY_CONVERT(nvarchar(100),s.[" + shmo + "])";
        String join = custName != null && shmo != null && custCode != null ? " LEFT JOIN dbo.CUSTOMERS c ON TRY_CONVERT(nvarchar(100),c.[" + custCode + "])=TRY_CONVERT(nvarchar(100),s.[" + shmo + "])" : "";
        String visitorExpr = visitorId != null && visKey != null && visName != null ? "COALESCE(TRY_CONVERT(nvarchar(150),v.[" + visName + "]),N'بدون ویزیتور')" : "N'بدون ویزیتور'";
        if (visitorId != null && visKey != null && visName != null) join += " LEFT JOIN dbo.visitors v ON TRY_CONVERT(nvarchar(100),v.[" + visKey + "])=TRY_CONVERT(nvarchar(100),s.[" + visitorId + "])";
        String number = hasCol(sail, "shfacfo") ? "TRY_CONVERT(nvarchar(80),s.shfacfo)" : "CAST(NULL AS nvarchar(80))";
        String sql = "SELECT TOP (8) " + codeExpr + ", " + nameExpr + ", TRY_CONVERT(decimal(19,2),s.[all]), s.t_date, -dbo.dif_date_alan(s.t_date), " + visitorExpr + ", " + number + " FROM dbo.sailfact s" + join + " WHERE s.tasvieh='f' AND NULLIF(s.t_date,'') IS NOT NULL AND dbo.dif_date_alan(s.t_date)<0" + activeAnd(sail, "s") + " ORDER BY -dbo.dif_date_alan(s.t_date) DESC";
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("party", stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("dueDate", stringOr(r.getString(4), "")); o.put("days", r.getLong(5)); o.put("visitor", stringOr(r.getString(6), "بدون ویزیتور")); o.put("invoice", stringOr(r.getString(7), "")); o.put("hint", "تاخیر " + formatNumber(r.getLong(5)) + " روز"); o.put("value", r.getDouble(3)); arr.put(o); }
        }
        return arr;
    }

    private JSONArray queryInactiveCustomers(Connection c) throws Exception {
        Set<String> cols = columns(c, "CUSTOMERS");
        Set<String> sail = columns(c, "sailfact");
        String shmo = resolve(cols, "SHMO", "shmo"); String name = resolve(cols, "MONAME", "Name", "CusName"); String sailShmo = resolve(sail, "shmo", "SHMO");
        if (shmo == null || sailShmo == null) return new JSONArray();
        String label = name == null ? "TRY_CONVERT(nvarchar(120),c.[" + shmo + "])" : "TRY_CONVERT(nvarchar(250),c.[" + name + "])";
        String sql = "SELECT TOP (8) TRY_CONVERT(nvarchar(100),c.[" + shmo + "]), " + label + ", CAST(0 AS decimal(19,2)), N'بدون خرید ثبت‌شده' FROM dbo.CUSTOMERS c WHERE NOT EXISTS (SELECT 1 FROM dbo.sailfact s WHERE TRY_CONVERT(nvarchar(100),s.[" + sailShmo + "])=TRY_CONVERT(nvarchar(100),c.[" + shmo + "])) ORDER BY " + label;
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("party", stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("hint", stringOr(r.getString(4), "بدون خرید")); o.put("value", r.getDouble(3)); arr.put(o); }
        }
        return arr;
    }

    private JSONArray queryTodaySoldItems(Connection c, String date) throws Exception {
        Set<String> sail = columns(c, "sailfact"); Set<String> detail = columns(c, "subsailfact"); Set<String> inv = columns(c, "inventory"); Set<String> grp = columns(c, "kagroup");
        if (date == null || date.isEmpty() || !hasCol(sail, "date") || !hasCol(sail, "shfacfo") || !hasCol(detail, "shfacfo") || !hasCol(detail, "SHKA")) return new JSONArray();
        String lineAmount = resolve(detail, "LINESUM", "all", "amount"); if (lineAmount == null) return new JSONArray();
        String invName = resolve(inv, "naka", "Name", "KalaName"); String invKey = resolve(inv, "shka", "SHKA");
        String groupId = resolve(inv, "group_rdf", "GroupID", "VarietyID", "variety_rdf"); String groupKey = resolve(grp, "group_rdf", "ID", "GroupID", "rdf"); String groupName = resolve(grp, "group_name", "name", "Name", "GroupName");
        String itemName = invName == null ? "N'کالا'" : "TRY_CONVERT(nvarchar(250),i.[" + invName + "])";
        String productCode = invKey == null ? "TRY_CONVERT(nvarchar(100),d.SHKA)" : "TRY_CONVERT(nvarchar(100),i.[" + invKey + "])";
        String groupExpr = groupId != null && groupKey != null && groupName != null ? "COALESCE(TRY_CONVERT(nvarchar(150),g.[" + groupName + "]),N'بدون گروه')" : "N'بدون گروه'";
        String qty = hasCol(detail, "TEDVAH") || hasCol(detail, "TEDJOZ") ? "ISNULL(SUM(" + (hasCol(detail, "TEDVAH") ? "ISNULL(TRY_CONVERT(decimal(19,3),d.TEDVAH),0)" : "0") + "+" + (hasCol(detail, "TEDJOZ") ? "ISNULL(TRY_CONVERT(decimal(19,3),d.TEDJOZ),0)" : "0") + "),0)" : "CAST(0 AS decimal(19,3))";
        String joinGroup = groupId != null && groupKey != null && groupName != null ? " LEFT JOIN dbo.kagroup g ON TRY_CONVERT(nvarchar(100),g.[" + groupKey + "])=TRY_CONVERT(nvarchar(100),i.[" + groupId + "])" : "";
        String sql = "SELECT TOP (8) " + productCode + ", " + itemName + ", ISNULL(SUM(TRY_CONVERT(decimal(19,2),d.[" + lineAmount + "])),0), " + groupExpr + ", " + qty + " FROM dbo.sailfact s JOIN dbo.subsailfact d ON d.shfacfo=s.shfacfo LEFT JOIN dbo.inventory i ON i.shka=d.SHKA" + joinGroup + " WHERE s.[date]=?" + activeAnd(sail, "s") + activeAnd(detail, "d") + " GROUP BY " + productCode + "," + itemName + "," + groupExpr + " ORDER BY 3 DESC";
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1, date); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("item", stringOr(r.getString(2), "کالا")); o.put("amount", r.getDouble(3)); o.put("group", stringOr(r.getString(4), "")); o.put("qty", r.getDouble(5)); o.put("hint", stringOr(r.getString(4), "") + " • مقدار " + formatNumber(r.getDouble(5))); o.put("value", r.getDouble(3)); arr.put(o); } } }
        return arr;
    }

    private JSONArray readDashboardRows(Connection c, String sql, String labelKey, String valueKey, String kind) throws Exception {
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) { JSONObject o = new JSONObject(); o.put(labelKey, stringOr(r.getString(1), "—")); o.put(valueKey, r.getDouble(2)); o.put("hint", r.getMetaData().getColumnCount() >= 3 ? stringOr(r.getString(3), kind) : kind); o.put("value", r.getDouble(2)); arr.put(o); }
        }
        return arr;
    }
    private long countTable(Connection c, String table) throws Exception {
        if (!SAFE_TABLES.contains(table)) throw new DbException("جدول مجاز نیست.");
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(1) FROM dbo.[" + table + "]")) {
            try (ResultSet r = ps.executeQuery()) { return r.next() ? r.getLong(1) : 0; }
        }
    }

    private int kpiIconResource(String title, int index) {
        String t = title == null ? "" : title;
        if (t.contains("مشتری")) return ir.meelano.android.R.drawable.icon_customers;
        if (t.contains("کالا")) return ir.meelano.android.R.drawable.icon_products;
        if (t.contains("فروش") || t.contains("فاکتور")) return ir.meelano.android.R.drawable.icon_sales;
        if (t.contains("چک")) return ir.meelano.android.R.drawable.icon_checks;
        if (t.contains("ویزیت")) return ir.meelano.android.R.drawable.icon_visitors;
        if (t.contains("هدف")) return ir.meelano.android.R.drawable.icon_goals;
        if (index == 1) return ir.meelano.android.R.drawable.icon_products;
        if (index == 2) return ir.meelano.android.R.drawable.icon_sales;
        if (index == 4) return ir.meelano.android.R.drawable.icon_checks;
        return ir.meelano.android.R.drawable.icon_dashboard;
    }

    private String kpiGlyph(String title, int index) {
        String t = title == null ? "" : title;
        if (t.contains("مشتری")) return "👥";
        if (t.contains("کالا")) return "◼";
        if (t.contains("فروش") || t.contains("فاکتور")) return "₿";
        if (t.contains("چک")) return "✓";
        if (t.contains("ویزیت")) return "⌾";
        if (t.contains("هدف")) return "◎";
        return "◆";
    }

    private void addDashboardKpiTable(JSONArray kpis) {
        if (kpis == null || kpis.length() == 0) return;
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(GOLD, 34), SURFACE}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("آمار کلیدی Meelano", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("مشتریان، کالاها، فاکتورها و چک‌ها در یک جدول فشرده", 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = null;
        for (int i = 0; i < kpis.length(); i++) {
            if (i % 2 == 0) { row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); c.addView(row, new LinearLayout.LayoutParams(-1, -2)); }
            JSONObject item = kpis.optJSONObject(i);
            int accent = i % 4 == 0 ? GOLD : (i % 4 == 1 ? INFO : (i % 4 == 2 ? SUCCESS : WARNING));
            LinearLayout cell = new LinearLayout(this);
            cell.setOrientation(LinearLayout.HORIZONTAL);
            cell.setGravity(Gravity.CENTER_VERTICAL);
            cell.setPadding(dp(8), dp(8), dp(8), dp(8));
            cell.setBackground(roundedStroke(alpha(accent, 20), 15, alpha(accent, 64)));
            TextView icon = text(kpiGlyph(item == null ? "" : item.optString("title"), i), 18, TEXT, Typeface.BOLD);
            icon.setGravity(Gravity.CENTER);
            icon.setBackground(gradient(new int[]{alpha(accent, 130), alpha(GOLD_2, 58)}, GradientDrawable.Orientation.TL_BR, 14));
            cell.addView(icon, new LinearLayout.LayoutParams(dp(38), dp(38)));
            LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(8), 0, dp(8), 0);
            copy.addView(text(item == null ? "شاخص" : item.optString("title", "شاخص"), 10.5f, MUTED, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            copy.addView(text(formatNumber(item == null ? 0 : item.opt("value")), 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            cell.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, -2, 1f); cp.setMargins(dp(3), dp(8), dp(3), 0);
            if (row != null) row.addView(cell, cp);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
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
            String titleText = item == null ? "شاخص" : item.optString("title", "شاخص");
            LinearLayout c = card();
            c.setBackground(gradient(new int[]{SURFACE, SURFACE_2}, GradientDrawable.Orientation.TOP_BOTTOM, 20));
            ImageView icon = new ImageView(this);
            icon.setImageResource(kpiIconResource(titleText, i));
            icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            icon.setPadding(0, 0, 0, dp(2));
            TextView title = text(titleText, 11.5f, MUTED, Typeface.NORMAL);
            TextView value = text(formatNumber(item == null ? 0 : item.opt("value")), 21, TEXT, Typeface.BOLD);
            TextView live = text("داده مستقیم", 10, SUCCESS, Typeface.NORMAL);
            c.addView(icon, new LinearLayout.LayoutParams(-1, dp(46)));
            c.addView(title, new LinearLayout.LayoutParams(-1, -2));
            c.addView(value, new LinearLayout.LayoutParams(-1, -2));
            c.addView(live, new LinearLayout.LayoutParams(-1, -2));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(142), 1f);
            cp.setMargins(dp(4), 0, dp(4), 0);
            if (row != null) row.addView(c, cp);
        }
    }

    private void loadCustomers(String query) {
        loadCustomers(query, "all");
    }

    private void loadCustomers(String query, String filter) {
        content.removeAllViews();
        addHero("مشتریان", "فیلتر هوشمند بدهکاران، بستانکاران، بدون خرید و پرخریدها");
        addSearchBox("جستجوی مشتری…", query, q -> loadCustomers(q, filter));
        addCustomerFilterChips(query, filter);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت مشتریان…");
        runDb(() -> queryCustomers(query, filter), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONArray rows = new JSONArray(body);
                    list.removeAllViews();
                    if (rows.length() == 0) { addEmptyTo(list, "مشتری مطابق فیلتر پیدا نشد."); return; }
                    for (int i = 0; i < rows.length(); i++) addCustomerCard(list, rows.optJSONObject(i));
                } catch (Exception e) { showPageError("مشتریان", e, () -> loadCustomers(query, filter)); }
            }
            @Override public void fail(Exception e) { showPageError("مشتریان", e, () -> loadCustomers(query, filter)); }
        });
    }

    private void addCustomerFilterChips(String query, String activeFilter) {
        LinearLayout panel = card();
        panel.setPadding(dp(13), dp(13), dp(13), dp(13));
        panel.setBackground(gradient(new int[]{alpha(GOLD, 22), alpha(INFO, 12), alpha(SURFACE, 245)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        LinearLayout titleRow = new LinearLayout(this);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setGravity(Gravity.CENTER_VERTICAL);
        TextView badge = text("⌁", 18, GOLD_2, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER);
        badge.setBackground(roundedStroke(alpha(GOLD, 30), 13, alpha(GOLD, 70)));
        titleRow.addView(badge, new LinearLayout.LayoutParams(dp(38), dp(38)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(10), 0);
        copy.addView(text("فیلترهای هوشمند مشتری", 13.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("جستجو بالا مستقل است؛ این بخش فقط نوع مشتری را مرتب می‌کند.", 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        titleRow.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        panel.addView(titleRow, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setGravity(Gravity.CENTER_VERTICAL);
        String[][] filters = {{"all","همه"},{"debt","بدهکار"},{"credit","بستانکار"},{"no_buy","بدون خرید"},{"top","پرخرید"}};
        for (String[] f : filters) {
            Button b = activeFilter.equals(f[0]) ? primaryButton(f[1]) : secondaryButton(f[1]);
            b.setTextSize(10.2f);
            b.setOnClickListener(v -> loadCustomers(query, f[0]));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(42), 1f);
            lp.setMargins(dp(3), 0, dp(3), 0);
            box.addView(b, lp);
        }
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2);
        bp.setMargins(0, dp(14), 0, 0);
        panel.addView(box, bp);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, -2);
        pp.setMargins(0, dp(10), 0, dp(16));
        content.addView(panel, pp);
    }

    private String queryCustomers(String search, String filter) throws Exception {
        try (Connection c = openConnection()) {
            Set<String> cols = columns(c, "CUSTOMERS");
            Set<String> saleCols = columns(c, "sailfact");
            Set<String> checkCols = columns(c, "getchk");
            String shmo = resolve(cols, "SHMO", "shmo", "CustomerCode");
            if (shmo == null) throw new DbException("ستون مشتری یافت نشد.");
            String name = resolve(cols, "MONAME", "Name", "CusName", "CustomerName");
            String phone = resolve(cols, "cell", "mobile", "Mobile", "tell1", "tel1");
            String phone2 = resolve(cols, "tell1", "tell2", "phone", "Phone");
            String phone3 = resolve(cols, "tell2", "tell3");
            String address = resolve(cols, "address", "Address", "adr", "addr", "manzel");
            String balance = resolve(cols, "man", "Balance", "Mandeh", "mande");
            String credit = resolve(cols, "etebar", "credit", "Credit");
            String vis = resolve(cols, "vis_rdf", "VisitorID", "visid");
            String economic = resolve(cols, "eghtesadi", "EconomicCode", "codeeghtesadi");
            String national = resolve(cols, "meli", "NationalCode", "codemeli");

            List<String> select = new ArrayList<>();
            select.add("c.[" + shmo + "] AS کد");
            select.add(name == null ? "CAST(NULL AS nvarchar(250)) AS نام" : "TRY_CONVERT(nvarchar(250),c.[" + name + "]) AS نام");
            select.add(phone == null ? "CAST(NULL AS nvarchar(100)) AS همراه" : "TRY_CONVERT(nvarchar(100),c.[" + phone + "]) AS همراه");
            select.add(phone2 == null ? "CAST(NULL AS nvarchar(100)) AS تلفن" : "TRY_CONVERT(nvarchar(100),c.[" + phone2 + "]) AS تلفن");
            select.add(phone3 == null ? "CAST(NULL AS nvarchar(100)) AS تلفن۲" : "TRY_CONVERT(nvarchar(100),c.[" + phone3 + "]) AS تلفن۲");
            select.add(address == null ? "CAST(NULL AS nvarchar(500)) AS نشانی" : "TRY_CONVERT(nvarchar(500),c.[" + address + "]) AS نشانی");
            select.add(balance == null ? "CAST(0 AS decimal(19,2)) AS مانده" : "TRY_CONVERT(decimal(19,2),c.[" + balance + "]) AS مانده");
            select.add(credit == null ? "CAST(NULL AS decimal(19,2)) AS اعتبار" : "TRY_CONVERT(decimal(19,2),c.[" + credit + "]) AS اعتبار");
            select.add(economic == null ? "CAST(NULL AS nvarchar(100)) AS اقتصادی" : "TRY_CONVERT(nvarchar(100),c.[" + economic + "]) AS اقتصادی");
            select.add(national == null ? "CAST(NULL AS nvarchar(100)) AS ملی" : "TRY_CONVERT(nvarchar(100),c.[" + national + "]) AS ملی");
            select.add(vis == null ? "CAST(NULL AS int) AS کد_ویزیتور" : "TRY_CONVERT(int,c.[" + vis + "]) AS کد_ویزیتور");
            select.add("ISNULL(sf.sales_total,0) AS جمع_فروش");
            select.add("ISNULL(sf.sales_count,0) AS تعداد_فاکتور");
            select.add("ISNULL(ch.check_total,0) AS جمع_چک");

            List<String> where = new ArrayList<>();
            List<Object> params = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                List<String> parts = new ArrayList<>();
                for (String col : new String[]{name, phone, phone2, phone3, address, shmo, national, economic}) {
                    if (col != null) { parts.add("TRY_CONVERT(nvarchar(500),c.[" + col + "]) LIKE N'%' + ? + N'%'"); params.add(search.trim()); }
                }
                where.add("(" + join(parts, " OR ") + ")");
            }
            boolean canSales = hasCol(saleCols, "shmo") && hasCol(saleCols, "all");
            boolean canChecks = hasCol(checkCols, "shmo") && hasCol(checkCols, "getchkmab");
            String saleApply = canSales ? "OUTER APPLY (SELECT COUNT_BIG(1) sales_count, ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[" + resolve(saleCols, "all") + "])),0) sales_total FROM dbo.sailfact s WHERE s.[" + resolve(saleCols, "shmo") + "]=c.[" + shmo + "]) sf " : "OUTER APPLY (SELECT CAST(0 AS bigint) sales_count, CAST(0 AS decimal(19,2)) sales_total) sf ";
            String checkApply = canChecks ? "OUTER APPLY (SELECT ISNULL(SUM(TRY_CONVERT(decimal(19,2),g.[" + resolve(checkCols, "getchkmab") + "])),0) check_total FROM dbo.getchk g WHERE g.[" + resolve(checkCols, "shmo") + "]=c.[" + shmo + "]) ch " : "OUTER APPLY (SELECT CAST(0 AS decimal(19,2)) check_total) ch ";
            if ("debt".equals(filter) && balance != null) where.add("TRY_CONVERT(decimal(19,2),c.[" + balance + "])>0");
            if ("credit".equals(filter) && balance != null) where.add("TRY_CONVERT(decimal(19,2),c.[" + balance + "])<0");
            if ("no_buy".equals(filter) && canSales) where.add("ISNULL(sf.sales_count,0)=0");
            String order = "top".equals(filter) ? " ORDER BY جمع_فروش DESC, نام" : ("debt".equals(filter) ? " ORDER BY مانده DESC, نام" : " ORDER BY نام, کد");
            String sql = "SELECT TOP (200) " + join(select, ",") + " FROM dbo.[CUSTOMERS] c " + saleApply + checkApply +
                    (where.isEmpty() ? "" : " WHERE " + join(where, " AND ")) + order;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                setParams(ps, params);
                try (ResultSet r = ps.executeQuery()) { return rowsToJson(r).toString(); }
            }
        }
    }

    private void addCustomerCard(LinearLayout parent, JSONObject r) {
        if (r == null) return;
        double balance = r.optDouble("مانده", 0);
        int accent = balance > 0 ? DANGER : (balance < 0 ? SUCCESS : INFO);
        String statusText = balance > 0 ? "بدهکار" : (balance < 0 ? "بستانکار" : "تسویه");
        LinearLayout c = card();
        c.setClickable(true);
        c.setBackground(gradient(new int[]{alpha(accent, 44), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.setOnClickListener(v -> showCustomerDetail(r, "all"));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        TextView avatar = text(initials(r.optString("نام", "م")), 17, Color.WHITE, Typeface.BOLD);
        avatar.setGravity(Gravity.CENTER);
        avatar.setShadowLayer(dp(2), 0, dp(1), alpha(Color.BLACK, 100));
        avatar.setBackground(gradient(new int[]{alpha(accent, 210), alpha(GOLD_2, 110)}, GradientDrawable.Orientation.TL_BR, 18));
        head.addView(avatar, new LinearLayout.LayoutParams(dp(52), dp(52)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(10), 0);
        String name = r.optString("نام", "بدون نام");
        copy.addView(text(name, 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("کد " + r.optString("کد", "-") + " • " + r.optString("همراه", "-"), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView chip = text(statusText, 10.5f, TEXT, Typeface.BOLD);
        chip.setGravity(Gravity.CENTER);
        chip.setPadding(dp(10), dp(5), dp(10), dp(5));
        chip.setBackground(roundedStroke(alpha(accent, 70), 999, alpha(accent, 130)));
        head.addView(chip, new LinearLayout.LayoutParams(-2, -2));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.addView(customerMiniMetric("مانده", money(r.opt("مانده")), accent), weightedMiniLp());
        row1.addView(customerMiniMetric("فروش", money(r.opt("جمع_فروش")), GOLD), weightedMiniLp());
        row1.addView(customerMiniMetric("فاکتور", formatNumber(r.opt("تعداد_فاکتور")), INFO), weightedMiniLp());
        LinearLayout.LayoutParams r1p = new LinearLayout.LayoutParams(-1, -2); r1p.setMargins(0, dp(10), 0, 0); c.addView(row1, r1p);

        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.addView(customerMiniMetric("چک", money(r.opt("جمع_چک")), WARNING), weightedMiniLp());
        row2.addView(customerMiniMetric("اعتبار", money(r.opt("اعتبار")), SUCCESS), weightedMiniLp());
        row2.addView(customerMiniMetric("تلفن", r.optString("تلفن", "-"), INFO), weightedMiniLp());
        LinearLayout.LayoutParams r2p = new LinearLayout.LayoutParams(-1, -2); r2p.setMargins(0, dp(7), 0, 0); c.addView(row2, r2p);

        TextView address = text("نشانی: " + r.optString("نشانی", "-"), 10.5f, alpha(TEXT, 190), Typeface.NORMAL);
        address.setMaxLines(2);
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2); ap.setMargins(0, dp(8), 0, 0); c.addView(address, ap);
        TextView action = text("مشاهده گردش حساب ←", 10.5f, accent, Typeface.BOLD);
        action.setGravity(Gravity.CENTER);
        action.setPadding(dp(8), dp(7), dp(8), dp(7));
        action.setBackground(roundedStroke(alpha(accent, 24), 999, alpha(accent, 70)));
        LinearLayout.LayoutParams alp = new LinearLayout.LayoutParams(-1, -2); alp.setMargins(0, dp(8), 0, 0); c.addView(action, alp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        parent.addView(c, lp);
    }

    private LinearLayout.LayoutParams weightedMiniLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        return lp;
    }

    private LinearLayout customerMiniMetric(String label, String value, int accent) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(dp(5), dp(7), dp(5), dp(7));
        box.setBackground(roundedStroke(alpha(accent, 18), 14, alpha(accent, 62)));
        TextView l = text(label, 9.2f, MUTED, Typeface.BOLD); l.setGravity(Gravity.CENTER);
        TextView v = text(value, 10.3f, TEXT, Typeface.BOLD); v.setGravity(Gravity.CENTER); v.setSingleLine(false); v.setMaxLines(2);
        box.addView(l, new LinearLayout.LayoutParams(-1, -2));
        box.addView(v, new LinearLayout.LayoutParams(-1, -2));
        return box;
    }

    private void showCustomerDialog(JSONObject r) {
        String message = "کد مشتری: " + r.optString("کد", "-") + "\n"
                + "همراه: " + r.optString("همراه", "-") + "\n"
                + "تلفن: " + r.optString("تلفن", "-") + "\n"
                + "تلفن دوم: " + r.optString("تلفن۲", "-") + "\n"
                + "مانده: " + money(r.opt("مانده")) + "\n"
                + "اعتبار: " + money(r.opt("اعتبار")) + "\n"
                + "جمع فروش: " + money(r.opt("جمع_فروش")) + "\n"
                + "تعداد فاکتور: " + formatNumber(r.opt("تعداد_فاکتور")) + "\n"
                + "جمع چک‌ها: " + money(r.opt("جمع_چک")) + "\n"
                + "کد اقتصادی: " + r.optString("اقتصادی", "-") + "\n"
                + "کد ملی: " + r.optString("ملی", "-") + "\n"
                + "کد ویزیتور: " + r.optString("کد_ویزیتور", "-") + "\n\n"
                + "نشانی: " + r.optString("نشانی", "-");
        new AlertDialog.Builder(this).setTitle(r.optString("نام", "Customer 360")).setMessage(message).setPositiveButton("بستن", null).show();
    }

    private void showCustomerDetail(JSONObject customer, String filter) {
        String code = customer.optString("کد", "");
        String name = customer.optString("نام", "Customer 360");
        content.removeAllViews();
        addHero("گردش حساب مشتری", name + " • کد " + code);
        String backTarget = customer.optString("_back", "customers");
        Button back = secondaryButton("dashboard".equals(backTarget) ? "بازگشت به داشبورد" : "بازگشت به مشتریان");
        back.setOnClickListener(v -> showApp(backTarget));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(10));
        content.addView(back, bp);
        addCustomerLedgerFilters(customer, filter);
        addLoading(content, "در حال دریافت ریز گردش حساب مشتری…");
        runDb(() -> queryCustomerLedger(code, filter), new DbCallback() {
            @Override public void ok(String body) {
                try { renderCustomerLedger(customer, new JSONArray(body), filter); }
                catch (Exception e) { showPageError("گردش مشتری", e, () -> showCustomerDetail(customer, filter)); }
            }
            @Override public void fail(Exception e) { showPageError("گردش مشتری", e, () -> showCustomerDetail(customer, filter)); }
        });
    }

    private void addCustomerLedgerFilters(JSONObject customer, String activeFilter) {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.HORIZONTAL);
        String[][] filters = {{"all","همه"},{"sales","فاکتور"},{"checks","چک"},{"paid","تسویه/دریافت"}};
        for (String[] f : filters) {
            Button b = activeFilter.equals(f[0]) ? primaryButton(f[1]) : secondaryButton(f[1]);
            b.setTextSize(10.5f);
            b.setOnClickListener(v -> showCustomerDetail(customer, f[0]));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(42), 1f); lp.setMargins(dp(3), 0, dp(3), dp(10));
            box.addView(b, lp);
        }
        content.addView(box, new LinearLayout.LayoutParams(-1, -2));
    }

    private void renderCustomerLedger(JSONObject customer, JSONArray rows, String filter) {
        content.removeAllViews();
        addHero("گردش حساب مشتری", customer.optString("نام", "Customer 360") + " • مانده " + money(customer.opt("مانده")));
        String backTarget = customer.optString("_back", "customers");
        Button back = secondaryButton("dashboard".equals(backTarget) ? "بازگشت به داشبورد" : "بازگشت به مشتریان"); back.setOnClickListener(v -> showApp(backTarget));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(48)); bp.setMargins(0, 0, 0, dp(10)); content.addView(back, bp);
        addCustomerLedgerFilters(customer, filter);
        if (rows == null || rows.length() == 0) { addEmptyTo(content, "گردشی برای این فیلتر پیدا نشد."); return; }
        for (int i = 0; i < rows.length(); i++) {
            JSONObject r = rows.optJSONObject(i);
            LinearLayout c = card();
            int accent = "چک دریافتی".equals(r.optString("type")) ? SUCCESS : ("چک پرداختی".equals(r.optString("type")) ? WARNING : GOLD);
            c.setBackground(roundedStroke(SURFACE, 18, alpha(accent, 60)));
            c.addView(text(r.optString("type", "گردش") + "   |   " + r.optString("date", "—"), 13, accent, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            c.addView(text(r.optString("title", "—"), 12, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            c.addView(text("مبلغ: " + money(r.opt("amount")) + "   |   وضعیت: " + r.optString("status", "—"), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
            if (!r.optString("description", "").isEmpty()) c.addView(text("توضیحات: " + r.optString("description"), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); content.addView(c, lp);
        }
    }

    private String queryCustomerLedger(String customerCode, String filter) throws Exception {
        JSONArray rows = new JSONArray();
        try (Connection c = openConnection()) {
            if (customerCode == null || customerCode.trim().isEmpty()) return rows.toString();
            if ("all".equals(filter) || "sales".equals(filter) || "paid".equals(filter)) appendCustomerSalesLedger(c, rows, customerCode);
            if ("all".equals(filter) || "checks".equals(filter) || "paid".equals(filter)) appendCustomerChecksLedger(c, rows, customerCode, true);
            if ("all".equals(filter) || "checks".equals(filter)) appendCustomerChecksLedger(c, rows, customerCode, false);
        }
        return sortLedgerRows(rows).toString();
    }

    private JSONArray sortLedgerRows(JSONArray rows) throws Exception {
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) list.add(rows.optJSONObject(i));
        java.util.Collections.sort(list, (a, b) -> b.optString("date", "").compareTo(a.optString("date", "")));
        JSONArray out = new JSONArray();
        for (JSONObject o : list) if (o != null) out.put(o);
        return out;
    }

    private void appendCustomerSalesLedger(Connection c, JSONArray rows, String code) throws Exception {
        Set<String> cols = columns(c, "sailfact");
        String shmo = resolve(cols, "shmo"); String date = resolve(cols, "date"); String amount = resolve(cols, "all"); String number = resolve(cols, "shfacfo");
        if (shmo == null || date == null || amount == null) return;
        String tasvieh = resolve(cols, "tasvieh"); String paid = resolve(cols, "MabDaryaftFactor", "Daryaft"); String desc = resolve(cols, "description", "Explain");
        String sql = "SELECT TOP (100) [" + date + "], " + (number == null ? "CAST(NULL AS nvarchar(50))" : "TRY_CONVERT(nvarchar(50),[" + number + "])") + ", TRY_CONVERT(decimal(19,2),[" + amount + "]), " + (tasvieh == null ? "N'نامشخص'" : "TRY_CONVERT(nvarchar(50),[" + tasvieh + "])") + ", " + (paid == null ? "CAST(0 AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),[" + paid + "])") + ", " + (desc == null ? "CAST(NULL AS nvarchar(500))" : "TRY_CONVERT(nvarchar(500),[" + desc + "])") + " FROM dbo.sailfact WHERE TRY_CONVERT(nvarchar(100),[" + shmo + "])=? ORDER BY [" + date + "] DESC";
        try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1, code); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("type", "فاکتور فروش"); o.put("date", r.getString(1)); o.put("title", "شماره فاکتور " + stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("status", "تسویه: " + translateStatus(stringOr(r.getString(4), "—")) + " / دریافتی: " + money(r.getObject(5))); o.put("description", stringOr(r.getString(6), "")); rows.put(o); } } }
    }

    private void appendCustomerChecksLedger(Connection c, JSONArray rows, String code, boolean incoming) throws Exception {
        String table = incoming ? "getchk" : "putchk";
        Set<String> cols = columns(c, table); Set<String> typeCols = columns(c, "CheckTypes");
        String shmo = resolve(cols, "shmo"); String amount = incoming ? resolve(cols, "getchkmab", "mablagh") : resolve(cols, "putchkmab", "mablagh");
        if (shmo == null || amount == null) return;
        String date = incoming ? resolve(cols, "getchkdate", "chkdate", "date", "sarresid") : resolve(cols, "putchkdate", "chkdate", "date", "sarresid");
        String number = incoming ? resolve(cols, "getchknum", "chknum", "number", "serial") : resolve(cols, "putchknum", "chknum", "number", "serial");
        String status = incoming ? resolve(cols, "chk_satus", "status") : resolve(cols, "putchk_status", "status");
        String desc = resolve(cols, "description", "Explain", "tozihat");
        String statusExpr = status == null ? "N'نامشخص'" : (hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? "COALESCE(TRY_CONVERT(nvarchar(120),t.Desciption),TRY_CONVERT(nvarchar(50),x.[" + status + "]))" : "TRY_CONVERT(nvarchar(50),x.[" + status + "])");
        String join = status != null && hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? " LEFT JOIN dbo.CheckTypes t ON t.ID=x.[" + status + "]" : "";
        String sql = "SELECT TOP (100) " + (date == null ? "CAST(NULL AS nvarchar(20))" : "TRY_CONVERT(nvarchar(20),x.[" + date + "])") + ", " + (number == null ? "CAST(NULL AS nvarchar(50))" : "TRY_CONVERT(nvarchar(50),x.[" + number + "])") + ", TRY_CONVERT(decimal(19,2),x.[" + amount + "]), " + statusExpr + ", " + (desc == null ? "CAST(NULL AS nvarchar(500))" : "TRY_CONVERT(nvarchar(500),x.[" + desc + "])") + " FROM dbo.[" + table + "] x" + join + " WHERE TRY_CONVERT(nvarchar(100),x.[" + shmo + "])=? ORDER BY 1 DESC";
        try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1, code); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("type", incoming ? "چک دریافتی" : "چک پرداختی"); o.put("date", stringOr(r.getString(1), "—")); o.put("title", "شماره چک " + stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("status", stringOr(r.getString(4), "—")); o.put("description", stringOr(r.getString(5), "")); rows.put(o); } } }
    }

    private void loadProducts(String query) {
        loadProducts(query, "all");
    }

    private void loadProducts(String query, String filter) {
        content.removeAllViews();
        addHero("کالا و انبار", "فیلتر موجودی، گردش خرید/فروش و کارت‌های محصول با تم MEELANO");
        addSearchBox("جستجوی کالا…", query, q -> loadProducts(q, filter));
        addProductFilterChips(query, filter);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت کالاها…");
        runDb(() -> queryProducts(query, filter), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONArray rows = new JSONArray(body);
                    list.removeAllViews();
                    if (rows.length() == 0) { addEmptyTo(list, "کالایی مطابق فیلتر پیدا نشد."); return; }
                    for (int i = 0; i < rows.length(); i++) addProductCard(list, rows.optJSONObject(i));
                } catch (Exception e) { showPageError("کالا", e, () -> loadProducts(query, filter)); }
            }
            @Override public void fail(Exception e) { showPageError("کالا", e, () -> loadProducts(query, filter)); }
        });
    }

    private void addProductFilterChips(String query, String activeFilter) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.HORIZONTAL);
        String[][] filters = {{"all","همه"},{"stock","موجود"},{"low","کم/منفی"},{"top","پرفروش"},{"idle","بدون گردش"}};
        for (String[] f : filters) {
            Button b = activeFilter.equals(f[0]) ? primaryButton(f[1]) : secondaryButton(f[1]);
            b.setTextSize(10.5f);
            b.setOnClickListener(v -> loadProducts(query, f[0]));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(42), 1f);
            lp.setMargins(dp(3), 0, dp(3), dp(12));
            box.addView(b, lp);
        }
        content.addView(box, new LinearLayout.LayoutParams(-1, -2));
    }

    private String queryProducts(String search, String filter) throws Exception {
        try (Connection c = openConnection()) {
            Set<String> cols = columns(c, "inventory");
            Map<String, String> invTypes = columnTypes(c, "inventory");
            Set<String> saleCols = columns(c, "subsailfact");
            Set<String> buyCols = columns(c, "subbuyfact");
            Set<String> groupCols = columns(c, "kagroup");
            String shka = resolve(cols, "shka", "SHKA");
            if (shka == null) throw new DbException("ستون کالا یافت نشد.");
            String name = resolve(cols, "naka", "Name", "KalaName");
            String code = resolve(cols, "StuffCode", "Code", "Barcode", "KalaCode");
            String price = resolve(cols, "FinalSalePrice", "SalePrice", "Price");
            String buyPrice = resolve(cols, "pure_buy_price", "BuyPrice", "buy_price", "LastBuyPrice");
            String stock = resolve(cols, "Mojoodi", "mojoodi", "Stock", "Qty", "tedad", "Tedad", "inventorycount");
            String unit = resolve(cols, "unit", "Unit", "vahed", "UnitName");
            String imageCol = resolve(cols, "pic", "aks", "image", "Image", "picture", "Picture", "photo", "Photo", "tasvir", "file", "FileName", "img");
            boolean imageText = isTextColumn(invTypes, imageCol);
            String groupId = resolve(cols, "group_rdf", "GroupID", "VarietyID", "variety_rdf");
            String groupKey = resolve(groupCols, "group_rdf", "ID", "GroupID", "rdf", "code");
            String groupName = resolve(groupCols, "group_name", "name", "Name", "GroupName", "nagr", "gname");
            String saleKey = resolve(saleCols, "SHKA", "shka", "StuffCode", "KalaID", "ProductID");
            String saleAmount = resolve(saleCols, "LINESUM", "all", "mablagh", "Total", "TotalPrice", "amount");
            String saleQty = resolve(saleCols, "tedad", "Tedad", "TEDVAH", "qty", "quantity");
            String buyKey = resolve(buyCols, "shka", "SHKA", "StuffCode", "KalaID", "ProductID");
            String buyAmount = resolve(buyCols, "tamam_joz", "LINESUM", "all", "mablagh", "Total", "amount");
            String buyQty = resolve(buyCols, "tedad", "Tedad", "TEDVAH", "qty", "quantity");

            List<String> select = new ArrayList<>();
            select.add("i.[" + shka + "] AS کد");
            select.add(name == null ? "CAST(NULL AS nvarchar(250)) AS نام" : "TRY_CONVERT(nvarchar(250),i.[" + name + "]) AS نام");
            select.add(code == null ? "CAST(NULL AS nvarchar(100)) AS بارکد" : "TRY_CONVERT(nvarchar(100),i.[" + code + "]) AS بارکد");
            select.add(price == null ? "CAST(0 AS decimal(19,2)) AS قیمت_فروش" : "TRY_CONVERT(decimal(19,2),i.[" + price + "]) AS قیمت_فروش");
            select.add(buyPrice == null ? "CAST(0 AS decimal(19,2)) AS بهای_خرید" : "TRY_CONVERT(decimal(19,2),i.[" + buyPrice + "]) AS بهای_خرید");
            select.add(stock == null ? "CAST(0 AS decimal(19,3)) AS موجودی" : "TRY_CONVERT(decimal(19,3),i.[" + stock + "]) AS موجودی");
            select.add(unit == null ? "CAST(NULL AS nvarchar(80)) AS واحد" : "TRY_CONVERT(nvarchar(80),i.[" + unit + "]) AS واحد");
            select.add(!imageText ? "CAST(NULL AS nvarchar(max)) AS تصویر" : "TRY_CONVERT(nvarchar(max),i.[" + imageCol + "]) AS تصویر");
            select.add(groupName != null && groupKey != null && groupId != null ? "TRY_CONVERT(nvarchar(250),g.[" + groupName + "]) AS گروه" : "CAST(NULL AS nvarchar(250)) AS گروه");
            select.add("ISNULL(sa.sale_qty,0) AS تعداد_فروش");
            select.add("ISNULL(sa.sale_amount,0) AS مبلغ_فروش");
            select.add("ISNULL(ba.buy_qty,0) AS تعداد_خرید");
            select.add("ISNULL(ba.buy_amount,0) AS مبلغ_خرید");
            select.add("(ISNULL(ba.buy_qty,0)-ISNULL(sa.sale_qty,0)) AS گردش_خالص");

            String saleApply = saleKey != null ? "OUTER APPLY (SELECT " +
                    (saleQty == null ? "CAST(0 AS decimal(19,3))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,3),s.[" + saleQty + "])),0)") + " sale_qty, " +
                    (saleAmount == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[" + saleAmount + "])),0)") + " sale_amount FROM dbo.subsailfact s WHERE TRY_CONVERT(nvarchar(100),s.[" + saleKey + "])=TRY_CONVERT(nvarchar(100),i.[" + shka + "])" + activeAnd(saleCols, "s") + ") sa " : "OUTER APPLY (SELECT CAST(0 AS decimal(19,3)) sale_qty, CAST(0 AS decimal(19,2)) sale_amount) sa ";
            String buyApply = buyKey != null ? "OUTER APPLY (SELECT " +
                    (buyQty == null ? "CAST(0 AS decimal(19,3))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,3),b.[" + buyQty + "])),0)") + " buy_qty, " +
                    (buyAmount == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),b.[" + buyAmount + "])),0)") + " buy_amount FROM dbo.subbuyfact b WHERE TRY_CONVERT(nvarchar(100),b.[" + buyKey + "])=TRY_CONVERT(nvarchar(100),i.[" + shka + "])" + activeAnd(buyCols, "b") + ") ba " : "OUTER APPLY (SELECT CAST(0 AS decimal(19,3)) buy_qty, CAST(0 AS decimal(19,2)) buy_amount) ba ";
            String groupJoin = groupName != null && groupKey != null && groupId != null ? "LEFT JOIN dbo.kagroup g ON TRY_CONVERT(nvarchar(100),g.[" + groupKey + "])=TRY_CONVERT(nvarchar(100),i.[" + groupId + "]) " : "";

            List<String> where = new ArrayList<>();
            List<Object> params = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                List<String> parts = new ArrayList<>();
                for (String col : new String[]{name, code, shka}) {
                    if (col != null) { parts.add("TRY_CONVERT(nvarchar(500),i.[" + col + "]) LIKE N'%' + ? + N'%'"); params.add(search.trim()); }
                }
                where.add("(" + join(parts, " OR ") + ")");
            }
            if ("stock".equals(filter)) where.add("ISNULL(TRY_CONVERT(decimal(19,3),i.[" + (stock == null ? shka : stock) + "]),0)>0");
            if ("low".equals(filter) && stock != null) where.add("ISNULL(TRY_CONVERT(decimal(19,3),i.[" + stock + "]),0)<=0");
            if ("idle".equals(filter)) where.add("ISNULL(sa.sale_qty,0)=0 AND ISNULL(ba.buy_qty,0)=0");
            String order = "top".equals(filter) ? " ORDER BY مبلغ_فروش DESC, نام" : ("low".equals(filter) && stock != null ? " ORDER BY موجودی ASC, نام" : " ORDER BY نام, کد");
            String sql = "SELECT TOP (160) " + join(select, ",") + " FROM dbo.[inventory] i " + groupJoin + saleApply + buyApply +
                    (where.isEmpty() ? "" : " WHERE " + join(where, " AND ")) + order;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                setParams(ps, params);
                try (ResultSet r = ps.executeQuery()) { return rowsToJson(r).toString(); }
            }
        }
    }

    private void addProductCard(LinearLayout parent, JSONObject r) {
        if (r == null) return;
        double stock = r.optDouble("موجودی", 0);
        int accent = stock > 0 ? SUCCESS : WARNING;
        LinearLayout c = card();
        c.setClickable(true);
        c.setBackground(gradient(new int[]{SURFACE, alpha(accent, 32)}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
        c.setOnClickListener(v -> showProductDialog(r));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        ImageView thumb = new ImageView(this);
        thumb.setPadding(dp(8), dp(8), dp(8), dp(8));
        thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        thumb.setBackground(roundedStroke(alpha(accent, 45), 18, alpha(accent, 110)));
        applyProductImage(thumb, r);
        head.addView(thumb, new LinearLayout.LayoutParams(dp(66), dp(66)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(10), 0);
        copy.addView(text(r.optString("نام", "بدون نام"), 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("کد: " + r.optString("کد", "-") + "   |   بارکد: " + r.optString("بارکد", "-"), 11.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("گروه: " + r.optString("گروه", "-") + "   |   " + (hasProductImage(r) ? "تصویر محصول متصل" : "تصویر پیش‌فرض"), 10.8f, accent, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout metrics = new LinearLayout(this);
        metrics.setOrientation(LinearLayout.HORIZONTAL);
        metrics.addView(metric("موجودی", formatNumber(r.opt("موجودی")) + " " + r.optString("واحد", "")), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics.addView(metric("فروش", money(r.opt("مبلغ_فروش"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics.addView(metric("خرید", money(r.opt("مبلغ_خرید"))), new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2);
        mp.setMargins(0, dp(10), 0, 0);
        c.addView(metrics, mp);
        LinearLayout metrics2 = new LinearLayout(this);
        metrics2.setOrientation(LinearLayout.HORIZONTAL);
        metrics2.addView(metric("قیمت فروش", money(r.opt("قیمت_فروش"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics2.addView(metric("بهای خرید", money(r.opt("بهای_خرید"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics2.addView(metric("گردش خالص", formatNumber(r.opt("گردش_خالص"))), new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams mp2 = new LinearLayout.LayoutParams(-1, -2);
        mp2.setMargins(0, dp(8), 0, 0);
        c.addView(metrics2, mp2);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        parent.addView(c, lp);
    }

    private boolean hasProductImage(JSONObject r) {
        String raw = r == null ? "" : r.optString("تصویر", "");
        return raw != null && !raw.trim().isEmpty() && !"null".equalsIgnoreCase(raw.trim());
    }

    private void applyProductImage(ImageView img, JSONObject r) {
        if (img == null) return;
        img.setImageResource(ir.meelano.android.R.drawable.icon_products);
        if (!hasProductImage(r)) return;
        String raw = r.optString("تصویر", "").trim();
        int comma = raw.indexOf(',');
        if (raw.startsWith("data:image") && comma > 0) raw = raw.substring(comma + 1);
        if (raw.length() < 80 || raw.contains("\\") || raw.contains("/")) return;
        try {
            byte[] bytes = Base64.decode(raw, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bmp != null) img.setImageBitmap(bmp);
        } catch (Exception ignored) { }
    }

    private void showProductDialog(JSONObject r) {
        String message = "کد کالا: " + r.optString("کد", "-") + "\n"
                + "بارکد/کد جانبی: " + r.optString("بارکد", "-") + "\n"
                + "گروه: " + r.optString("گروه", "-") + "\n"
                + "واحد: " + r.optString("واحد", "-") + "\n"
                + "موجودی فعلی: " + formatNumber(r.opt("موجودی")) + "\n"
                + "قیمت فروش: " + money(r.opt("قیمت_فروش")) + "\n"
                + "بهای خرید: " + money(r.opt("بهای_خرید")) + "\n"
                + "تعداد فروش: " + formatNumber(r.opt("تعداد_فروش")) + "\n"
                + "مبلغ فروش: " + money(r.opt("مبلغ_فروش")) + "\n"
                + "تعداد خرید: " + formatNumber(r.opt("تعداد_خرید")) + "\n"
                + "مبلغ خرید: " + money(r.opt("مبلغ_خرید")) + "\n"
                + "گردش خالص: " + formatNumber(r.opt("گردش_خالص"));
        new AlertDialog.Builder(this).setTitle(r.optString("نام", "جزئیات کالا")).setMessage(message).setPositiveButton("بستن", null).show();
    }


    private String initials(String name) {
        if (name == null) return "م";
        String t = name.trim();
        if (t.isEmpty()) return "م";
        String[] parts = t.split("\\s+");
        if (parts.length >= 2 && parts[0].length() > 0 && parts[1].length() > 0) return parts[0].substring(0, 1) + parts[1].substring(0, 1);
        return t.substring(0, Math.min(2, t.length()));
    }

    private String prettyColumnName(String col) {
        if (col == null) return "فیلد";
        String l = col.toLowerCase(Locale.US);
        if (l.equals("shmo") || l.contains("customer") || l.equals("کد")) return "کد مشتری";
        if (l.contains("moname") || l.equals("name") || l.contains("naka")) return "نام";
        if (l.contains("shfac") || l.contains("factor")) return "شماره سند";
        if (l.contains("date") || l.contains("tarikh")) return "تاریخ";
        if (l.equals("all") || l.contains("mab") || l.contains("amount") || l.contains("price")) return "مبلغ";
        if (l.contains("status") || l.contains("satus") || l.contains("tasvieh")) return "وضعیت/تسویه";
        if (l.contains("description") || l.contains("explain")) return "توضیحات";
        if (l.contains("chk")) return "چک";
        if (l.contains("bank")) return "بانک";
        if (l.contains("tedad") || l.contains("qty")) return "تعداد";
        return col;
    }

    private String prettyValue(String col, Object value) {
        if (value == null || JSONObject.NULL.equals(value)) return "-";
        String l = col == null ? "" : col.toLowerCase(Locale.US);
        String v = String.valueOf(value);
        if (l.equals("all") || l.contains("mab") || l.contains("amount") || l.contains("price") || l.contains("man")) return money(v);
        if (l.contains("status") || l.contains("satus") || l.contains("tasvieh")) return translateStatus(v);
        return v;
    }

    private String translateStatus(String value) {
        if (value == null) return "نامشخص";
        String v = value.trim();
        if (v.equals("0")) return "ثبت‌شده / در جریان";
        if (v.equals("1")) return "وصول / تایید شده";
        if (v.equals("2")) return "برگشتی / رد شده";
        if (v.equals("3")) return "خرج‌شده / انتقال‌یافته";
        if (v.equalsIgnoreCase("t")) return "فعال / بله";
        if (v.equalsIgnoreCase("f")) return "غیرفعال / خیر";
        return v;
    }

    private LinearLayout metric(String label, String value) {
        LinearLayout m = new LinearLayout(this);
        m.setOrientation(LinearLayout.VERTICAL);
        m.setPadding(dp(6), dp(8), dp(6), dp(8));
        m.setBackground(roundedStroke(SURFACE_2, 14, BORDER));
        TextView l = text(label, 9.5f, MUTED, Typeface.NORMAL);
        l.setGravity(Gravity.CENTER);
        float valueSize = value != null && value.length() > 18 ? 9.2f : (value != null && value.length() > 13 ? 10f : 11f);
        TextView v = text(value, valueSize, TEXT, Typeface.BOLD);
        v.setGravity(Gravity.CENTER);
        v.setMaxLines(2);
        m.addView(l, new LinearLayout.LayoutParams(-1, -2));
        m.addView(v, new LinearLayout.LayoutParams(-1, -2));
        return m;
    }

    private interface SearchAction { void run(String query); }

    private void addSearchBox(String hint, String query, SearchAction action) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setGravity(Gravity.CENTER_VERTICAL);
        box.setPadding(dp(8), dp(8), dp(8), dp(8));
        box.setBackground(roundedStroke(alpha(INFO, 14), 18, alpha(INFO, 44)));
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(14));
        content.addView(box, lp);
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
                TextView cell = text(prettyColumnName(col) + ": " + prettyValue(col, val), shown == 0 ? 13 : 11.5f, shown == 0 ? TEXT : MUTED, shown == 0 ? Typeface.BOLD : Typeface.NORMAL);
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
        addHero("گزارشات کاربردی مدیریت", "خلاصه‌های عملیاتی و دسته‌بندی‌شده مستقیم از SQL Server");
        addLoading(content, "در حال آماده‌سازی گزارشات مدیریتی…");
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
        addHero("اتاق فرمان زنده گزارشات", "گزارشات کامل‌تر، دسته‌بندی‌شده و بدون نمودار؛ مخصوص تصمیم مدیریت");
        addReportCommandCenter(a);
        addReportHealthGrid(a);
        addReportActionPlan(a);

        addReportCategory("۱) درآمد، فروش و جریان سفارش", "اول بفهمیم فروش کجا رشد کرده، کجا افت کرده و خرید چقدر با فروش هم‌خوان است", "↗", GOLD);
        addReportRowsSection("فروش هفتگی", "ردیابی بازه‌های قوی/ضعیف فروش برای واکنش سریع", "weeklySales", a.optJSONArray("weeklySales"), GOLD, 6, 0);
        addDualReportRowsSection("تعادل خرید و فروش", "اگر خرید از فروش جلو بزند، موجودی و نقدینگی زودتر باید کنترل شود", a.optJSONArray("monthlyPurchaseSales"));
        addReportRowsSection("مشتریان درآمدساز", "۵ مشتری که بیشترین اثر را روی فروش دارند", "topCustomers", a.optJSONArray("topCustomers"), SUCCESS, 5, 0);

        addReportCategory("۲) سودآوری، قیمت‌گذاری و کالا", "تمرکز روی سود واقعی، حاشیه سود و گروه‌هایی که باید برایشان تصمیم قیمت/موجودی گرفت", "◆", GOLD_2);
        addReportRowsSection("سود ماهانه", "ماه‌های سودساز برای بررسی تخفیف، بهای تمام‌شده و سیاست فروش", "monthlyProfit", a.optJSONArray("monthlyProfit"), SUCCESS, 6, 0);
        addReportRowsSection("حاشیه سود", "درصد سود به فروش؛ افت آن هشدار تخفیف یا قیمت‌گذاری اشتباه است", "netMargin", a.optJSONArray("netMargin"), GOLD_2, 6, 2);
        addReportRowsSection("گروه‌های کالایی پرفروش", "گروه‌های اثرگذار برای تامین، تبلیغ و افزایش/اصلاح قیمت", "categoryShare", a.optJSONArray("categoryShare"), INFO, 6, 0);

        addReportCategory("۳) وصول، مطالبات و کنترل چک", "نقدینگی واقعی از همین بخش می‌آید؛ بدهی سن‌دار و چک باید اولویت‌بندی شود", "✓", WARNING);
        addReportRowsSection("مطالبات سن‌دار", "باکت‌های سنی بدهی برای برنامه تماس و وصول", "debtAging", a.optJSONArray("debtAging"), WARNING, 6, 0);
        addReportRowsSection("فاکتورهای معوق", "اسناد معوق با نام مشتری، سررسید و ویزیتور برای پیگیری مستقیم", "overdueInvoices", a.optJSONArray("overdueInvoices"), DANGER, 5, 0);
        addReportRowsSection("وضعیت چک‌ها", "تفکیک چک دریافتی/پرداختی بر اساس دسته‌های واقعی", "checkStatuses", a.optJSONArray("checkStatuses"), WARNING, 7, 4);
        addBankNamesOnlySection(a.optJSONArray("banks"));

        addReportCategory("۴) مشتری، وفاداری و بازار", "مشتریان فعال، بدهکاران مهم و مشتریان خاموش را جدا ببینید", "👥", SUCCESS);
        addReportRowsSection("رشد مشتریان فعال", "تعداد مشتریان خریدکننده در دوره‌های اخیر", "customerGrowth", a.optJSONArray("customerGrowth"), INFO, 6, 1);
        addReportRowsSection("بدهکاران اولویت‌دار", "اولویت تماس و مذاکره وصول بر اساس مانده", "topDebtors", a.optJSONArray("topDebtors"), DANGER, 5, 0);
        addReportRowsSection("مشتریان بدون خرید", "لیست بازفعال‌سازی؛ تماس کوتاه، پیشنهاد هوشمند، پیگیری سریع", "inactiveCustomers", a.optJSONArray("inactiveCustomers"), INFO, 5, 3);

        addReportCategory("۵) عملیات روزانه و کنترل اسناد", "ورود سریع به گزارش‌های روزانه فروش و خرید با تاریخ دلخواه", "⇄", GOLD_2);
        addDailyReportLaunchers(a.optString("latestSalesDate", ""), a.optString("latestPurchaseDate", ""));
    }

    private void addReportHealthGrid(JSONObject a) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(INFO, 28), alpha(GOLD, 20), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("تابلوی سلامت مدیریت", 16.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text("شش شاخص سریع برای اینکه قبل از ورود به جزئیات، وضعیت کسب‌وکار را حس کنید.", 10.8f, MUTED, Typeface.NORMAL);
        sub.setLineSpacing(dp(2), 1.05f);
        c.addView(sub, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout row = null;
        String salesTrend = trendSummary(a.optJSONArray("weeklySales"), false);
        String profitTrend = trendSummary(a.optJSONArray("monthlyProfit"), false);
        String margin = latestValueText(a.optJSONArray("netMargin"), true);
        String customerGrowth = latestValueText(a.optJSONArray("customerGrowth"), false);
        String debt = moneyValue(strongestPoint(a.optJSONArray("debtAging")), "value");
        String checks = checkStatusSummary(a.optJSONArray("checkStatuses"));
        String[][] cells = {
                {"فروش", salesTrend, "↗"}, {"سود", profitTrend, "◆"}, {"حاشیه سود", margin, "%"},
                {"مشتری فعال", customerGrowth, "👥"}, {"ریسک مطالبات", debt, "!"}, {"چک‌ها", checks, "✓"}
        };
        int[] colors = {GOLD, SUCCESS, GOLD_2, INFO, DANGER, WARNING};
        for (int i = 0; i < cells.length; i++) {
            if (i % 2 == 0) { row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(9), 0, 0); c.addView(row, rp); }
            addHealthCell(row, cells[i][0], cells[i][1], cells[i][2], colors[i]);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addHealthCell(LinearLayout row, String title, String value, String glyph, int accent) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setGravity(Gravity.CENTER_VERTICAL);
        box.setPadding(dp(8), dp(8), dp(8), dp(8));
        box.setBackground(roundedStroke(alpha(accent, 18), 18, alpha(accent, 62)));
        TextView icon = report3dIcon(glyph, accent);
        box.addView(icon, new LinearLayout.LayoutParams(dp(38), dp(38)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(8), 0, dp(8), 0);
        copy.addView(text(title, 10.5f, MUTED, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView v = text(value, 11.3f, TEXT, Typeface.BOLD);
        v.setMaxLines(2);
        copy.addView(v, new LinearLayout.LayoutParams(-1, -2));
        box.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1f); lp.setMargins(dp(3), 0, dp(3), 0);
        if (row != null) row.addView(box, lp);
    }

    private void addReportActionPlan(JSONObject a) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(WARNING, 28), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 24));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("!", WARNING), new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("اقدامات پیشنهادی امروز", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("این بخش عددها را به کار عملی تبدیل می‌کند؛ گزارش فقط برای تماشا نیست.", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        addActionItem(c, "وصول", "اول با " + labelOf(strongestPoint(a.optJSONArray("topDebtors")), "party", labelOf(strongestPoint(a.optJSONArray("debtAging")), "label", "بدهکاران مهم")) + " تماس بگیر و وضعیت تسویه را روشن کن.", DANGER);
        addActionItem(c, "قیمت‌گذاری", "حاشیه سود آخرین دوره: " + latestValueText(a.optJSONArray("netMargin"), true) + "; تخفیف و بهای تمام‌شده را بازبینی کن.", GOLD_2);
        addActionItem(c, "موجودی", "گروه داغ: " + labelOf(strongestPoint(a.optJSONArray("categoryShare")), "label", "نامشخص") + "; تامین و قیمت این گروه را جلوتر کنترل کن.", INFO);
        addActionItem(c, "چک", "وضعیت‌های پرتکرار چک را از کارت «وضعیت چک‌ها» پیگیری کن؛ تاریخ سررسید را عقب نینداز.", WARNING);
        addActionItem(c, "وفاداری", "برای " + labelOf(strongestPoint(a.optJSONArray("topCustomers")), "label", "مشتری اول") + " پیشنهاد نگهداشت بده؛ وابستگی فروش به یک مشتری هم ریسک است.", SUCCESS);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addActionItem(LinearLayout parent, String tag, String body, int accent) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setPadding(dp(10), dp(9), dp(10), dp(9));
        item.setBackground(roundedStroke(alpha(accent, 15), 16, alpha(accent, 58)));
        TextView badge = text(tag, 10.4f, accent, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER);
        badge.setSingleLine(true);
        badge.setBackground(roundedStroke(alpha(accent, 28), 999, alpha(accent, 82)));
        item.addView(badge, new LinearLayout.LayoutParams(dp(72), dp(34)));
        TextView b = text(body, 10.9f, TEXT, Typeface.NORMAL);
        b.setLineSpacing(dp(2), 1.05f);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(0, -2, 1f); bp.setMargins(dp(8), 0, dp(8), 0);
        item.addView(b, bp);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1, -2); ip.setMargins(0, dp(8), 0, 0); parent.addView(item, ip);
    }

    private String trendSummary(JSONArray rows, boolean percent) {
        if (rows == null || rows.length() == 0) return "داده کافی نیست";
        JSONObject last = rows.optJSONObject(rows.length() - 1);
        JSONObject prev = rows.length() > 1 ? rows.optJSONObject(rows.length() - 2) : null;
        double v = valueOf(last), p0 = valueOf(prev);
        String base = percent ? formatNumber(v) + "٪" : compactMoney(v);
        if (prev == null || Math.abs(p0) < 0.0001) return base;
        double change = ((v - p0) / Math.abs(p0)) * 100.0;
        return base + " • " + (change >= 0 ? "+" : "") + formatNumber(change) + "٪";
    }

    private String latestValueText(JSONArray rows, boolean percent) {
        if (rows == null || rows.length() == 0) return "داده کافی نیست";
        JSONObject last = rows.optJSONObject(rows.length() - 1);
        double v = valueOf(last);
        return percent ? formatNumber(v) + "٪" : formatNumber(v);
    }

    private String checkStatusSummary(JSONArray rows) {
        if (rows == null || rows.length() == 0) return "داده کافی نیست";
        long count = 0;
        for (int i = 0; i < rows.length(); i++) count += rows.optJSONObject(i) == null ? 0 : rows.optJSONObject(i).optLong("count", 0);
        return formatNumber(count) + " فقره";
    }

    private void addReportRowsSection(String title, String sub, String key, JSONArray rows, int accent, int limit, int valueMode) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(accent, 24), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon(reportGlyph(title), accent), new LinearLayout.LayoutParams(dp(46), dp(46)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text(title, 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView st = text(sub, 10.8f, MUTED, Typeface.NORMAL);
        st.setLineSpacing(dp(2), 1.05f);
        copy.addView(st, new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        if (rows == null || rows.length() == 0) {
            TextView empty = text("داده کافی برای این گزارش وجود ندارد.", 11.2f, MUTED, Typeface.NORMAL);
            empty.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(-1, dp(58)); ep.setMargins(0, dp(10), 0, 0);
            c.addView(empty, ep);
        } else {
            for (int i = 0; i < Math.min(limit, rows.length()); i++) addReportDataRow(c, rows.optJSONObject(i), i + 1, accent, valueMode, key);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addReportDataRow(LinearLayout parent, JSONObject row, int index, int accent, int valueMode, String key) {
        if (row == null) return;
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setPadding(dp(9), dp(8), dp(9), dp(8));
        item.setBackground(roundedStroke(alpha(accent, 15), 15, alpha(accent, 45)));
        TextView rank = text(String.valueOf(index), 12, accent, Typeface.BOLD);
        rank.setGravity(Gravity.CENTER);
        rank.setBackground(roundedStroke(alpha(accent, 32), 999, alpha(accent, 75)));
        item.addView(rank, new LinearLayout.LayoutParams(dp(34), dp(34)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(9), 0, dp(9), 0);
        String label = labelOf(row, "label", labelOf(row, "party", labelOf(row, "item", "—")));
        copy.addView(text(label, 11.6f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        String hint = reportHint(key, row, valueMode);
        copy.addView(text(hint, 9.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        item.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView value = text(reportValue(row, valueMode), 10.4f, accent, Typeface.BOLD);
        value.setGravity(Gravity.CENTER);
        value.setMaxLines(2);
        item.addView(value, new LinearLayout.LayoutParams(dp(valueMode == 4 ? 104 : 92), -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(8), 0, 0); parent.addView(item, lp);
    }

    private String reportValue(JSONObject row, int mode) {
        if (row == null) return "—";
        if (mode == 1) return formatNumber(row.opt("value"));
        if (mode == 2) return formatNumber(row.opt("value")) + "٪";
        if (mode == 3) return "پیگیری";
        if (mode == 4) return "تعداد " + formatNumber(row.opt("count")) + "\n" + compactMoney(row.opt("amount"));
        return compactMoney(row.opt("value"));
    }

    private String reportHint(String key, JSONObject row, int mode) {
        String hint = row == null ? "" : row.optString("hint", "");
        if (hint != null && !hint.trim().isEmpty()) return hint;
        String k = key == null ? "" : key;
        if ("weeklySales".equals(k)) return "فروش ثبت‌شده این بازه";
        if ("monthlyPurchaseSales".equals(k)) return "مقایسه جریان خرید و فروش";
        if ("monthlyProfit".equals(k)) return "سود تخمینی بر اساس بهای ثبت‌شده";
        if ("netMargin".equals(k)) return "درصد سود به فروش";
        if ("checkStatuses".equals(k)) return "نیازمند کنترل تاریخ و وضعیت";
        if ("topCustomers".equals(k)) return "حفظ رابطه و کنترل سقف اعتبار";
        if ("topDebtors".equals(k)) return "اولویت تماس وصول";
        if ("overdueInvoices".equals(k)) return "فاکتور معوق؛ پیگیری با ویزیتور";
        if ("inactiveCustomers".equals(k)) return "بازفعال‌سازی با پیشنهاد هدفمند";
        if ("customerGrowth".equals(k)) return "تعداد مشتریان فعال";
        if ("categoryShare".equals(k)) return "تصمیم تامین و قیمت‌گذاری";
        if ("debtAging".equals(k)) return "اولویت‌بندی وصول";
        return mode == 2 ? "درصد" : "گزارش مدیریتی";
    }

    private void addDualReportRowsSection(String title, String sub, JSONArray rows) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(INFO, 22), alpha(GOLD, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.LEFT_RIGHT, 22));
        c.addView(text(title, 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text(sub, 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        if (rows == null || rows.length() == 0) {
            TextView empty = text("داده‌ای برای مقایسه خرید و فروش نیست.", 11.2f, MUTED, Typeface.NORMAL);
            empty.setGravity(Gravity.CENTER);
            c.addView(empty, new LinearLayout.LayoutParams(-1, dp(58)));
        } else {
            for (int i = 0; i < Math.min(5, rows.length()); i++) {
                JSONObject row = rows.optJSONObject(i);
                LinearLayout item = new LinearLayout(this);
                item.setOrientation(LinearLayout.VERTICAL);
                item.setPadding(dp(10), dp(9), dp(10), dp(9));
                item.setBackground(roundedStroke(alpha(INFO, 14), 15, alpha(INFO, 50)));
                item.addView(text(labelOf(row, "label", "—"), 11.7f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
                item.addView(text("فروش: " + compactMoney(row == null ? null : row.opt("value")) + "  •  خرید: " + compactMoney(row == null ? null : row.opt("secondaryValue")), 10.3f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
                LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1, -2); ip.setMargins(0, dp(8), 0, 0); c.addView(item, ip);
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addBankNamesOnlySection(JSONArray banks) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(INFO, 20), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("◉", INFO), new LinearLayout.LayoutParams(dp(46), dp(46)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("بانک‌های قابل پایش", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("برای محرمانگی در گزارشات مدیریتی، مبلغ بانک‌ها نمایش داده نمی‌شود.", 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        if (banks == null || banks.length() == 0) {
            TextView empty = text("بانکی برای نمایش وجود ندارد.", 11.2f, MUTED, Typeface.NORMAL);
            empty.setGravity(Gravity.CENTER);
            c.addView(empty, new LinearLayout.LayoutParams(-1, dp(56)));
        } else {
            LinearLayout line = null;
            for (int i = 0; i < Math.min(8, banks.length()); i++) {
                if (i % 2 == 0) { line = new LinearLayout(this); line.setOrientation(LinearLayout.HORIZONTAL); LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(8), 0, 0); c.addView(line, rp); }
                JSONObject bank = banks.optJSONObject(i);
                String branch = bank == null ? "" : bank.optString("branch", "");
                String bankLabel = labelOf(bank, "label", "بانک");
                if (bank != null && bank.optBoolean("duplicate", false) && branch != null && !branch.isEmpty()) bankLabel += " • شعبه " + branch;
                TextView chip = text(bankLabel, 10.2f, TEXT, Typeface.BOLD);
                chip.setGravity(Gravity.CENTER);
                chip.setSingleLine(true);
                chip.setPadding(dp(8), 0, dp(8), 0);
                chip.setBackground(roundedStroke(alpha(INFO, 16), 999, alpha(INFO, 55)));
                LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(38), 1f); cp.setMargins(dp(3), 0, dp(3), 0);
                if (line != null) line.addView(chip, cp);
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addReportCommandCenter(JSONObject a) {
        LinearLayout panel = card();
        panel.setBackground(gradient(new int[]{alpha(GOLD, 38), alpha(INFO, 20), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 28));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) panel.setElevation(dp(9));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        TextView icon = report3dIcon("◆", GOLD);
        head.addView(icon, new LinearLayout.LayoutParams(dp(62), dp(62)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(11), 0, dp(8), 0);
        copy.addView(text("خلاصه حیاتی مدیریت", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text("سیگنال‌های فروش، سود، وصول، مشتری و کالا در چند کارت کاربردی.", 11, MUTED, Typeface.NORMAL);
        sub.setLineSpacing(dp(2), 1.05f);
        copy.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        panel.addView(head, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        JSONObject weekly = strongestPoint(a.optJSONArray("weeklySales"));
        JSONObject customer = strongestPoint(a.optJSONArray("topCustomers"));
        addReportInsight(row1, "فروش قوی", labelOf(weekly, "label", "نامشخص"), moneyValue(weekly, "value"), "↗", GOLD);
        addReportInsight(row1, "مشتری کلیدی", labelOf(customer, "label", "نامشخص"), moneyValue(customer, "value"), "👥", SUCCESS);
        LinearLayout.LayoutParams r1p = new LinearLayout.LayoutParams(-1, -2); r1p.setMargins(0, dp(12), 0, 0); panel.addView(row1, r1p);

        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        JSONObject debt = strongestPoint(a.optJSONArray("debtAging"));
        JSONObject cat = strongestPoint(a.optJSONArray("categoryShare"));
        addReportInsight(row2, "ریسک مطالبات", labelOf(debt, "label", "بدون هشدار"), moneyValue(debt, "value"), "!", WARNING);
        addReportInsight(row2, "گروه پرفروش", labelOf(cat, "label", "نامشخص"), moneyValue(cat, "value"), "◼", INFO);
        LinearLayout.LayoutParams r2p = new LinearLayout.LayoutParams(-1, -2); r2p.setMargins(0, dp(8), 0, 0); panel.addView(row2, r2p);

        TextView advice = text("پیشنهاد: فروش، سود و وصول را همزمان ببینید؛ عددهای قشنگ بدون نقدینگی فقط ویترین‌اند.", 10.8f, alpha(TEXT, 210), Typeface.BOLD);
        advice.setGravity(Gravity.CENTER);
        advice.setPadding(dp(10), dp(9), dp(10), dp(9));
        advice.setBackground(roundedStroke(alpha(GOLD, 20), 16, alpha(GOLD, 62)));
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2); ap.setMargins(0, dp(12), 0, 0); panel.addView(advice, ap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(panel, lp);
    }

    private void addReportInsight(LinearLayout row, String title, String label, String value, String glyph, int accent) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setGravity(Gravity.CENTER_VERTICAL);
        box.setPadding(dp(8), dp(8), dp(8), dp(8));
        box.setBackground(gradient(new int[]{alpha(accent, 33), alpha(SURFACE_2, 235)}, GradientDrawable.Orientation.RIGHT_LEFT, 18));
        TextView icon = report3dIcon(glyph, accent);
        box.addView(icon, new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(8), 0, dp(8), 0);
        copy.addView(text(title, 10.8f, MUTED, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(label, 11.2f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(value, 9.8f, accent, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        box.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        row.addView(box, lp);
    }

    private void addReportCategory(String title, String sub, String glyph, int accent) {
        LinearLayout c = card();
        c.setPadding(dp(13), dp(13), dp(13), dp(13));
        c.setBackground(gradient(new int[]{alpha(accent, 40), alpha(SURFACE, 246)}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        TextView icon = report3dIcon(glyph, accent);
        row.addView(icon, new LinearLayout.LayoutParams(dp(54), dp(54)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text(title, 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView st = text(sub, 10.8f, MUTED, Typeface.NORMAL);
        st.setLineSpacing(dp(2), 1.05f);
        copy.addView(st, new LinearLayout.LayoutParams(-1, -2));
        row.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, dp(4), 0, dp(10));
        content.addView(c, lp);
    }

    private TextView report3dIcon(String glyph, int accent) {
        TextView icon = text(glyph, 20, Color.WHITE, Typeface.BOLD);
        icon.setGravity(Gravity.CENTER);
        icon.setShadowLayer(dp(4), 0, dp(2), alpha(Color.BLACK, 150));
        icon.setBackground(gradient(new int[]{mix(accent, Color.WHITE, 0.24f), accent, mix(accent, Color.BLACK, 0.33f)}, GradientDrawable.Orientation.TL_BR, 17));
        return icon;
    }

    private JSONObject strongestPoint(JSONArray arr) {
        if (arr == null || arr.length() == 0) return null;
        JSONObject best = null;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) continue;
            double v = Math.abs(valueOf(o));
            if (best == null || v > max) { best = o; max = v; }
        }
        return best;
    }

    private void addDailyReportLaunchers(String latestSales, String latestPurchase) {
        LinearLayout c = card();
        c.addView(text("گزارش روزانه فروش و خرید", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("تاریخ را وارد کنید؛ اگر خالی باشد آخرین روز ثبت‌شده واقعی در Meelano استفاده می‌شود.", 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));

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
        addHero(type.equals("sales") ? "گزارش روزانه فروش" : "گزارش روزانه خرید", "در حال آماده‌سازی گزارش واقعی روزانه از Meelano");
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
                item.setBackground(roundedStroke(SURFACE_2, 15, BORDER));
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
            a.put("topDebtors", queryTopDebtors(c));
            a.put("overdueInvoices", queryOverdueInvoices(c));
            a.put("inactiveCustomers", queryInactiveCustomers(c));
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
            String label = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? "N'دریافتی • ' + COALESCE(TRY_CONVERT(nvarchar(100),t.Desciption),N'دسته '+CONVERT(nvarchar(20),g.chk_satus))" : "N'دریافتی • دسته ' + CONVERT(nvarchar(20),g.chk_satus)";
            String where = ""; List<Object> params = new ArrayList<>();
            if (session != null && session.visitorId != null && hasCol(getCols, "vis_rdf")) { where = " WHERE TRY_CONVERT(int,g.vis_rdf)=?"; params.add(session.visitorId); }
            String sql = "SELECT " + label + ", COUNT_BIG(1), ISNULL(SUM(TRY_CONVERT(decimal(19,2),g.[getchkmab])),0) FROM dbo.getchk g " + typeJoin + where + " GROUP BY " + label;
            appendCheckPoints(out, c, sql, params);
        }
        if (hasCol(putCols, "putchk_status") && hasCol(putCols, "putchkmab")) {
            String typeJoin = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? " LEFT JOIN dbo.CheckTypes t ON t.ID=p.putchk_status " : "";
            String label = hasCol(typeCols, "ID") && hasCol(typeCols, "Desciption") ? "N'پرداختی • ' + COALESCE(TRY_CONVERT(nvarchar(100),t.Desciption),N'دسته '+CONVERT(nvarchar(20),p.putchk_status))" : "N'پرداختی • دسته ' + CONVERT(nvarchar(20),p.putchk_status)";
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
        where += activeAnd(cols, "");
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
        if (!activeCondition(detail, "d").isEmpty()) where = appendWhere(where, activeCondition(detail, "d"));
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
        String branch = resolve(bank, "BRANCHCODE", "BranchCode", "branch_code", "SHOBE", "shobe", "shobeh", "Branch", "code", "Code", "BANKCODE", "RDF");
        String branchExpr = branch == null ? "CAST(NULL AS nvarchar(80))" : "TRY_CONVERT(nvarchar(80),b.[" + branch + "])";
        String inflow = hasCol(get, "our_bankrdf") && hasCol(get, "getchkmab") ? "ISNULL((SELECT SUM(TRY_CONVERT(decimal(19,2),g.getchkmab)) FROM dbo.getchk g WHERE g.our_bankrdf=b.RDF),0)" : "CAST(0 AS decimal(19,2))";
        String outflow = hasCol(put, "bankrdf") && hasCol(put, "putchkmab") ? "ISNULL((SELECT SUM(TRY_CONVERT(decimal(19,2),p.putchkmab)) FROM dbo.putchk p WHERE p.bankrdf=b.RDF),0)" : "CAST(0 AS decimal(19,2))";
        String where = hasCol(bank, "Active") ? " WHERE ISNULL(b.Active,1)=1" : "";
        String sql = "SELECT TOP (20) TRY_CONVERT(nvarchar(250),b.BANKNAME), ISNULL(TRY_CONVERT(decimal(19,2),b.MAN),0), " + inflow + ", " + outflow + ", " + branchExpr + ", COUNT(1) OVER (PARTITION BY TRY_CONVERT(nvarchar(250),b.BANKNAME)) FROM dbo.BANK b " + where + " ORDER BY ISNULL(TRY_CONVERT(decimal(19,2),b.MAN),0) DESC";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) { JSONObject o = new JSONObject(); o.put("label", stringOr(r.getString(1), "بدون نام")); o.put("balance", r.getDouble(2)); o.put("inflow", r.getDouble(3)); o.put("outflow", r.getDouble(4)); o.put("branch", stringOr(r.getString(5), "")); o.put("duplicate", r.getLong(6) > 1); o.put("value", r.getDouble(2)); out.put(o); }
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
            if (!activeCondition(detail, "d").isEmpty()) where = appendWhere(where, activeCondition(detail, "d"));
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
                String itemApply = detailNumber == null ? "" : " OUTER APPLY (SELECT COUNT_BIG(1) item_count FROM dbo.[" + detailTable + "] dd WHERE dd.[" + detailNumber + "]=h.[" + numberCol + "]" + activeAnd(d, "dd") + ") ic ";
                String itemCountExpr = detailNumber == null ? "CAST(0 AS bigint)" : "ISNULL(ic.item_count,0)";
                String where = "WHERE h.[" + dateCol + "]=?"; where += activeAnd(h, "h"); List<Object> params = new ArrayList<>(); params.add(actualDate);
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
                        String itemSql = "SELECT TOP (120) " + itemName + ", " + qty + ", ISNULL(SUM(TRY_CONVERT(decimal(19,2),dd.[" + lineAmount + "])),0) FROM dbo.[" + detailTable + "] dd JOIN dbo.[" + header + "] h ON h.[" + numberCol + "]=dd.[" + detailNumber + "] LEFT JOIN dbo.inventory i ON i.shka=dd.[" + key + "] " + where + activeAnd(d, "dd") + " GROUP BY " + itemName + " ORDER BY 3 DESC";
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
                    o.put("label", stringOr(r.getString(1), "دسته نامشخص"));
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
        where += activeAnd(cols, "");
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

    private String reportGlyph(String title) {
        String t = title == null ? "" : title;
        if (t.contains("چک")) return "✓";
        if (t.contains("مشتری")) return "👥";
        if (t.contains("مطالبات")) return "!";
        if (t.contains("بانک")) return "◉";
        if (t.contains("خرید")) return "↙";
        if (t.contains("سود") || t.contains("حاشیه")) return "◆";
        if (t.contains("کالا") || t.contains("دسته")) return "◼";
        return "↗";
    }

    private void initSpeechEngine() {
        try {
            tts = new TextToSpeech(getApplicationContext(), statusCode -> {
                if (statusCode == TextToSpeech.SUCCESS && tts != null) {
                    int result = tts.setLanguage(new Locale("fa", "IR"));
                    ttsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED;
                }
            });
        } catch (Exception ignored) {
            ttsReady = false;
        }
    }

    private String assistantFirstName() {
        return prefs == null ? "" : prefs.getString(KEY_FIRST_NAME, "").trim();
    }

    private String displayFirstName() {
        String n = assistantFirstName();
        return n.isEmpty() ? "دوست خوبم" : n;
    }

    private String extractFirstName(String raw) {
        String n = raw == null ? "" : raw.trim();
        if (n.isEmpty() && session != null) n = session.userName == null ? "" : session.userName.trim();
        if (n.isEmpty()) return "کاربر";
        String[] parts = n.split("\\s+");
        return parts.length == 0 ? n : parts[0];
    }

    private void maybeAskFirstName(boolean force) {
        if (prefs == null) return;
        if (!force && prefs.getBoolean(KEY_NAME_ASKED, false)) return;
        LinearLayout box = card();
        box.setPadding(dp(18), dp(18), dp(18), dp(16));
        box.setBackground(gradient(new int[]{alpha(GOLD_2, 48), alpha(INFO, 22), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 28));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) box.setElevation(dp(10));

        FrameLayout portrait = miloPortrait(dp(156));
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(156));
        pp.setMargins(0, 0, 0, dp(10));
        box.addView(portrait, pp);
        TextView title = text("میلو چطور صدایت کند؟", 18, TEXT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        box.addView(title, new LinearLayout.LayoutParams(-1, -2));
        TextView hint = text("نام کوچک را بنویس تا گزارش‌ها شخصی‌تر شوند.", 11.2f, MUTED, Typeface.NORMAL);
        hint.setGravity(Gravity.CENTER);
        hint.setLineSpacing(dp(2), 1.05f);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.setMargins(0, dp(5), 0, dp(12));
        box.addView(hint, hp);

        EditText name = input("مثلاً میلاد", assistantFirstName(), false);
        name.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) name.setTextDirection(View.TEXT_DIRECTION_RTL);
        box.addView(name, new LinearLayout.LayoutParams(-1, dp(54)));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(box)
                .setPositiveButton("ثبت و ادامه", (d, w) -> {
                    String first = extractFirstName(name.getText().toString());
                    prefs.edit().putString(KEY_FIRST_NAME, first).putBoolean(KEY_NAME_ASKED, true).apply();
                    Toast.makeText(this, "خوش آمدی " + first + " عزیز", Toast.LENGTH_SHORT).show();
                    if ("assistant".equals(activePage)) showAssistant();
                })
                .create();
        dialog.setOnShowListener(d -> {
            name.requestFocus();
            if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(roundedStroke(alpha(SURFACE, 245), 28, alpha(GOLD, 90)));
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positive != null) {
                positive.setTextColor(GOLD);
                positive.setTextSize(14f);
                positive.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private FrameLayout miloPortrait(int heightPx) {
        FrameLayout frame = new FrameLayout(this);
        frame.setPadding(dp(6), dp(6), dp(6), dp(6));
        frame.setBackground(gradient(new int[]{alpha(GOLD, 34), alpha(INFO, 22), alpha(SURFACE_2, 232)}, GradientDrawable.Orientation.TL_BR, 24));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) frame.setElevation(dp(10));
        LivingMiloView live = new LivingMiloView(this);
        live.setContentDescription("میلو، موجود زنده و متحرک دستیار هوشمند Meelano");
        live.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        frame.addView(live, new FrameLayout.LayoutParams(-1, heightPx <= 0 ? dp(230) : heightPx, Gravity.CENTER));
        View glass = new View(this);
        glass.setBackground(roundedStroke(alpha(Color.WHITE, 10), 22, alpha(GOLD_2, 50)));
        frame.addView(glass, new FrameLayout.LayoutParams(-1, -1, Gravity.CENTER));
        return frame;
    }

    private class LivingMiloView extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private long startMs;
        LivingMiloView(Context context) {
            super(context);
            setWillNotDraw(false);
        }
        @Override protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            startMs = System.currentTimeMillis();
            invalidate();
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth(), h = getHeight();
            if (w <= 0 || h <= 0) return;
            float t = (System.currentTimeMillis() - startMs) / 1000f;
            float cx = w / 2f;
            float ground = h * 0.82f;
            float sc = Math.min(w / 360f, h / 310f);
            float breath = (float)Math.sin(t * 2.2f);
            float hover = (float)Math.sin(t * 1.35f) * dp(4);
            drawMiloAura(canvas, w, h, t, sc);

            canvas.save();
            canvas.translate(0, hover);
            canvas.rotate((float)Math.sin(t * 1.15f) * 1.8f, cx, h * 0.46f);
            drawMiloBody(canvas, cx, ground, sc, breath, t);
            drawMiloHead(canvas, cx, h * 0.34f, sc, breath, t);
            drawMiloArms(canvas, cx, h * 0.56f, sc, t);
            canvas.restore();
            drawMiloHolograms(canvas, w, h, t, sc);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) postInvalidateOnAnimation(); else postInvalidateDelayed(16);
        }
        private void drawMiloAura(Canvas c, int w, int h, float t, float sc) {
            float cx = w / 2f, cy = h * 0.48f;
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(GOLD_2, 28));
            c.drawCircle(cx, cy, dp(118) * sc + (float)Math.sin(t * 2.1f) * dp(5), p);
            p.setColor(alpha(INFO, 30));
            c.drawCircle(cx - dp(42) * sc, cy + dp(25) * sc, dp(92) * sc, p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(dp(2.2f) * sc);
            RectF orbit = new RectF(cx - dp(132) * sc, cy - dp(88) * sc, cx + dp(132) * sc, cy + dp(88) * sc);
            p.setColor(alpha(GOLD, 155));
            c.drawArc(orbit, t * 62f, 190, false, p);
            p.setColor(alpha(INFO, 150));
            c.drawArc(orbit, 180 + t * 74f, 112, false, p);
            p.setStyle(Paint.Style.FILL);
            for (int i = 0; i < 7; i++) {
                float a = t * (0.8f + i * 0.07f) + i * 0.92f;
                float rx = dp(130 - i * 5) * sc;
                float ry = dp(82 + (i % 2) * 18) * sc;
                p.setColor(alpha(i % 2 == 0 ? GOLD_2 : INFO, 118 + i * 12));
                c.drawCircle(cx + (float)Math.cos(a) * rx, cy + (float)Math.sin(a) * ry, dp(3.2f + (i % 3)) * sc, p);
            }
        }
        private void drawMiloBody(Canvas c, float cx, float ground, float sc, float breath, float t) {
            p.setStyle(Paint.Style.FILL);
            p.setShadowLayer(dp(10) * sc, 0, dp(4) * sc, alpha(Color.BLACK, 130));
            p.setColor(alpha(Color.BLACK, 55));
            c.drawOval(new RectF(cx - dp(78) * sc, ground - dp(17) * sc, cx + dp(78) * sc, ground + dp(16) * sc), p);
            p.clearShadowLayer();
            RectF body = new RectF(cx - dp(62) * sc, ground - dp(142) * sc - breath * dp(2), cx + dp(62) * sc, ground - dp(24) * sc + breath * dp(2));
            p.setColor(mix(INFO, HEADER_START, 0.42f));
            c.drawRoundRect(body, dp(34) * sc, dp(34) * sc, p);
            p.setColor(alpha(GOLD_2, 230));
            c.drawRoundRect(new RectF(body.left + dp(14) * sc, body.top + dp(18) * sc, body.right - dp(14) * sc, body.top + dp(58) * sc), dp(18) * sc, dp(18) * sc, p);
            p.setColor(alpha(Color.WHITE, 185));
            p.setTextAlign(Paint.Align.CENTER);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setTextSize(dp(17) * sc);
            c.drawText("Milo", cx, body.top + dp(45) * sc, p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(3) * sc);
            p.setColor(alpha(Color.WHITE, 75));
            c.drawLine(cx - dp(38) * sc, body.top + dp(75) * sc, cx + dp(38) * sc, body.top + dp(75) * sc, p);
            p.setStyle(Paint.Style.FILL);
            for (int i = 0; i < 3; i++) {
                p.setColor(alpha(i == 1 ? SUCCESS : GOLD, 190));
                c.drawCircle(cx + (i - 1) * dp(24) * sc, body.top + dp(96) * sc + (float)Math.sin(t * 4 + i) * dp(2) * sc, dp(5) * sc, p);
            }
        }
        private void drawMiloHead(Canvas c, float cx, float cy, float sc, float breath, float t) {
            float blinkPhase = t % 4.2f;
            float blink = blinkPhase > 3.93f ? 0.12f : 1f;
            RectF head = new RectF(cx - dp(76) * sc, cy - dp(70) * sc, cx + dp(76) * sc, cy + dp(70) * sc);
            p.setStyle(Paint.Style.FILL);
            p.setShadowLayer(dp(9) * sc, 0, dp(3) * sc, alpha(Color.BLACK, 135));
            p.setColor(mix(HEADER_START, Color.WHITE, 0.10f));
            c.drawRoundRect(head, dp(42) * sc, dp(42) * sc, p);
            p.clearShadowLayer();
            p.setColor(alpha(GOLD, 150));
            c.drawRoundRect(new RectF(head.left + dp(6) * sc, head.top + dp(6) * sc, head.right - dp(6) * sc, head.bottom - dp(6) * sc), dp(36) * sc, dp(36) * sc, p);
            p.setColor(mix(INFO, NAVY, 0.28f));
            RectF face = new RectF(head.left + dp(16) * sc, head.top + dp(20) * sc, head.right - dp(16) * sc, head.bottom - dp(18) * sc);
            c.drawRoundRect(face, dp(28) * sc, dp(28) * sc, p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(2.2f) * sc);
            p.setColor(alpha(Color.WHITE, 75));
            c.drawRoundRect(face, dp(28) * sc, dp(28) * sc, p);

            p.setStyle(Paint.Style.FILL);
            drawEye(c, cx - dp(28) * sc, cy - dp(8) * sc, dp(13) * sc, blink, t, sc);
            drawEye(c, cx + dp(28) * sc, cy - dp(8) * sc, dp(13) * sc, blink, t + 0.2f, sc);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(4) * sc);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setColor(alpha(GOLD_2, 215));
            RectF smile = new RectF(cx - dp(26) * sc, cy + dp(15) * sc, cx + dp(26) * sc, cy + dp(42) * sc);
            c.drawArc(smile, 18, 144, false, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(SUCCESS, 190));
            c.drawCircle(cx + (float)Math.sin(t * 2.8f) * dp(20) * sc, cy + dp(43) * sc, dp(4) * sc, p);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(4) * sc);
            p.setColor(alpha(GOLD, 210));
            c.drawLine(cx, head.top - dp(2) * sc, cx + (float)Math.sin(t * 2f) * dp(10) * sc, head.top - dp(28) * sc, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(GOLD_2, 235));
            c.drawCircle(cx + (float)Math.sin(t * 2f) * dp(10) * sc, head.top - dp(31) * sc, dp(8) * sc + breath * dp(1.2f) * sc, p);
        }
        private void drawEye(Canvas c, float x, float y, float r, float blink, float t, float sc) {
            p.setColor(alpha(Color.WHITE, 235));
            RectF eye = new RectF(x - r, y - r * blink, x + r, y + r * blink);
            c.drawOval(eye, p);
            p.setColor(mix(INFO, GOLD, 0.35f));
            c.drawCircle(x + (float)Math.sin(t * 1.6f) * r * 0.18f, y, Math.max(dp(2) * sc, r * 0.40f * blink), p);
            p.setColor(alpha(Color.BLACK, 205));
            c.drawCircle(x + (float)Math.sin(t * 1.6f) * r * 0.18f, y, Math.max(dp(1.2f) * sc, r * 0.17f * blink), p);
        }
        private void drawMiloArms(Canvas c, float cx, float y, float sc, float t) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(dp(11) * sc);
            p.setColor(mix(INFO, HEADER_START, 0.34f));
            float wave = (float)Math.sin(t * 4.4f);
            c.drawLine(cx - dp(58) * sc, y - dp(18) * sc, cx - dp(100) * sc, y + dp(8) * sc + wave * dp(5) * sc, p);
            c.drawLine(cx + dp(58) * sc, y - dp(18) * sc, cx + dp(100) * sc, y - dp(2) * sc - wave * dp(10) * sc, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(GOLD_2);
            c.drawCircle(cx - dp(104) * sc, y + dp(9) * sc + wave * dp(5) * sc, dp(12) * sc, p);
            p.setColor(GOLD);
            c.drawCircle(cx + dp(104) * sc, y - dp(2) * sc - wave * dp(10) * sc, dp(12) * sc, p);
        }
        private void drawMiloHolograms(Canvas c, int w, int h, float t, float sc) {
            p.setStyle(Paint.Style.FILL);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setTextAlign(Paint.Align.CENTER);
            String[] labels = {"SQL", "فروش", "ریسک"};
            int[] colors = {INFO, GOLD, WARNING};
            for (int i = 0; i < labels.length; i++) {
                float x = w * (0.18f + i * 0.32f) + (float)Math.sin(t * 1.7f + i) * dp(5) * sc;
                float y = h * (0.16f + (i % 2) * 0.60f) + (float)Math.cos(t * 1.4f + i) * dp(6) * sc;
                RectF panel = new RectF(x - dp(34) * sc, y - dp(16) * sc, x + dp(34) * sc, y + dp(16) * sc);
                p.setColor(alpha(colors[i], 44));
                c.drawRoundRect(panel, dp(12) * sc, dp(12) * sc, p);
                p.setColor(alpha(colors[i], 220));
                p.setTextSize(dp(10.5f) * sc);
                c.drawText(labels[i], x, y + dp(4) * sc, p);
            }
        }
    }

    private void addAssistantBusinessElements(LinearLayout parent) {
        TextView title = text("حوزه‌هایی که میلو هوشمندانه پایش می‌کند", 10.8f, MUTED, Typeface.BOLD);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.setMargins(0, dp(12), 0, dp(6));
        parent.addView(title, tp);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        addAssistantElement(row, "فروش", "↗", GOLD, 0);
        addAssistantElement(row, "کالا", "◼", WARNING, 120);
        addAssistantElement(row, "مشتری", "👥", SUCCESS, 240);
        addAssistantElement(row, "چک", "✓", INFO, 360);
        addAssistantElement(row, "بانک", "◉", GOLD_2, 480);
        parent.addView(row, new LinearLayout.LayoutParams(-1, -2));
    }

    private void addAssistantElement(LinearLayout parent, String label, String glyph, int accent, long delay) {
        LinearLayout chip = new LinearLayout(this);
        chip.setOrientation(LinearLayout.VERTICAL);
        chip.setGravity(Gravity.CENTER);
        chip.setPadding(dp(3), dp(5), dp(3), dp(5));
        chip.setBackground(gradient(new int[]{alpha(accent, 44), alpha(SURFACE_2, 218)}, GradientDrawable.Orientation.TL_BR, 16));
        TextView g = text(glyph, 17, accent, Typeface.BOLD);
        g.setGravity(Gravity.CENTER);
        g.setShadowLayer(dp(2), 0, dp(1), alpha(Color.BLACK, 90));
        TextView l = text(label, 8.7f, TEXT, Typeface.BOLD);
        l.setGravity(Gravity.CENTER);
        l.setSingleLine(true);
        chip.addView(g, new LinearLayout.LayoutParams(-1, -2));
        chip.addView(l, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(52), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        parent.addView(chip, lp);
        animatePulse(chip, delay);
    }

    private void showAssistant() {
        content.removeAllViews();
        addHero("میلو، دستیار زنده هوشمند", "تحلیل کوتاه فروش، مشتری، کالا، چک و ریسک");
        if (!prefs.getBoolean(KEY_NAME_ASKED, false)) maybeAskFirstName(false);

        LinearLayout intro = card();
        intro.setPadding(dp(14), dp(14), dp(14), dp(14));
        intro.setBackground(gradient(new int[]{alpha(INFO, 30), alpha(GOLD, 24), alpha(SURFACE, 248)}, GradientDrawable.Orientation.LEFT_RIGHT, 26));
        FrameLayout portrait = miloPortrait(dp(230));
        intro.addView(portrait, new LinearLayout.LayoutParams(-1, dp(230)));
        TextView title = text("میلو زنده است و آماده تحلیل", 16.5f, TEXT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.setMargins(0, dp(10), 0, 0);
        intro.addView(title, tp);
        TextView body = text("یک سؤال کوتاه بپرس؛ جواب مدیریتی، دقیق و سریع می‌دهم.", 11.2f, MUTED, Typeface.NORMAL);
        body.setGravity(Gravity.CENTER);
        intro.addView(body, new LinearLayout.LayoutParams(-1, -2));
        addAssistantBusinessElements(intro);

        LinearLayout chips = new LinearLayout(this);
        chips.setOrientation(LinearLayout.HORIZONTAL);
        addAssistantQuickChip(chips, "گزارش", "یک گزارش مدیریتی کوتاه از فروش، خرید، چک، مشتری و کالا بده.");
        addAssistantQuickChip(chips, "ریسک", "ریسک‌های وصول، زیان و مشتریان خطرناک را کوتاه بگو.");
        addAssistantQuickChip(chips, "قیمت", "برای قیمت‌گذاری و بازار فروش پیشنهاد عملی بده.");
        addAssistantQuickChip(chips, "پورسانت", "برای پورسانت ویزیتورها پیشنهاد بده.");
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.setMargins(0, dp(12), 0, 0);
        intro.addView(chips, cp);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1, -2);
        ip.setMargins(0, 0, 0, dp(12));
        content.addView(intro, ip);

        assistantChatLog = card();
        assistantChatLog.setPadding(dp(10), dp(10), dp(10), dp(10));
        assistantChatLog.setBackground(roundedStroke(alpha(SURFACE_2, 230), 22, alpha(GOLD, 42)));
        content.addView(assistantChatLog, new LinearLayout.LayoutParams(-1, -2));
        addAssistantBubble(false, "سلام " + displayFirstName() + "! من میلو هستم؛ کوتاه بپرس، دقیق جواب می‌دهم.");

        LinearLayout inputCard = card();
        inputCard.setPadding(dp(12), dp(12), dp(12), dp(12));
        LinearLayout inputRow = new LinearLayout(this);
        inputRow.setOrientation(LinearLayout.HORIZONTAL);
        inputRow.setGravity(Gravity.CENTER_VERTICAL);
        assistantInput = input("از میلو بپرس…", "", false);
        assistantInput.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) assistantInput.setTextDirection(View.TEXT_DIRECTION_RTL);
        assistantInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        Button send = primaryButton("ارسال");
        inputRow.addView(assistantInput, new LinearLayout.LayoutParams(0, dp(52), 1f));
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(dp(86), dp(52)); sp.setMargins(dp(8), 0, 0, 0);
        inputRow.addView(send, sp);
        inputCard.addView(inputRow, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout voiceRow = new LinearLayout(this);
        voiceRow.setOrientation(LinearLayout.HORIZONTAL);
        Button voice = secondaryButton("🎙 پرسش صوتی");
        Button speak = secondaryButton("🔊 خواندن پاسخ");
        Button settings = secondaryButton("⚙ کلیدهای AI");
        voice.setTextSize(10.5f); speak.setTextSize(10.5f); settings.setTextSize(10.5f);
        voice.setOnClickListener(v -> startAssistantVoiceInput());
        speak.setOnClickListener(v -> speakAssistantText(lastAssistantAnswer.isEmpty() ? "هنوز پاسخی ندارم؛ اول یک سوال بپرس." : lastAssistantAnswer));
        settings.setOnClickListener(v -> showApp("settings"));
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(0, dp(42), 1f); vp.setMargins(dp(3), dp(10), dp(3), 0);
        voiceRow.addView(voice, vp);
        LinearLayout.LayoutParams vp2 = new LinearLayout.LayoutParams(0, dp(42), 1f); vp2.setMargins(dp(3), dp(10), dp(3), 0);
        voiceRow.addView(speak, vp2);
        LinearLayout.LayoutParams vp3 = new LinearLayout.LayoutParams(0, dp(42), 1f); vp3.setMargins(dp(3), dp(10), dp(3), 0);
        voiceRow.addView(settings, vp3);
        inputCard.addView(voiceRow, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams icp = new LinearLayout.LayoutParams(-1, -2); icp.setMargins(0, dp(12), 0, 0);
        content.addView(inputCard, icp);

        View.OnClickListener submitter = v -> submitAssistantQuestion(assistantInput.getText().toString().trim());
        send.setOnClickListener(submitter);
        assistantInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) { submitter.onClick(send); return true; }
            return false;
        });
    }

    private void addAssistantQuickChip(LinearLayout parent, String label, String prompt) {
        Button b = secondaryButton(label);
        b.setTextSize(9.8f);
        b.setOnClickListener(v -> submitAssistantQuestion(prompt));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(40), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        parent.addView(b, lp);
    }

    private TextView addAssistantBubble(boolean user, String message) {
        if (assistantChatLog == null) return null;
        int accent = user ? INFO : GOLD;
        LinearLayout bubble = new LinearLayout(this);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setPadding(dp(11), dp(9), dp(11), dp(9));
        bubble.setBackground(roundedStroke(alpha(accent, user ? 28 : 34), 17, alpha(accent, 80)));
        TextView who = text(user ? displayFirstName() : "میلو • AI", 10.5f, accent, Typeface.BOLD);
        TextView body = text(message, 12.2f, TEXT, Typeface.NORMAL);
        body.setLineSpacing(dp(3), 1.06f);
        bubble.addView(who, new LinearLayout.LayoutParams(-1, -2));
        bubble.addView(body, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(user ? dp(36) : 0, dp(8), user ? 0 : dp(36), 0);
        assistantChatLog.addView(bubble, lp);
        return body;
    }

    private void submitAssistantQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            Toast.makeText(this, "یک سوال کوتاه بنویس؛ ذهن‌خوانی هنوز در نسخه بتاست!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (assistantInput != null) assistantInput.setText("");
        addAssistantBubble(true, question);
        TextView pending = addAssistantBubble(false, "دارم داده‌ها را تحلیل می‌کنم… یک لحظه، قهوه مجازی‌ام داغ است.");
        setConnectionStatus("loading");
        executor.execute(() -> {
            String answer;
            try {
                if (asksCreator(question)) {
                    answer = creatorAnswer();
                } else {
                    String snapshot = queryAssistantSnapshot();
                    String provider = activeAiProvider();
                    String key = storedAiKey(provider);
                    if (provider.isEmpty() || key.isEmpty()) {
                        answer = localAssistantAnswer(question, snapshot);
                    } else {
                        try {
                            answer = callAiProvider(provider, key, question, snapshot);
                            if (answer == null || answer.trim().isEmpty()) answer = localAssistantAnswer(question, snapshot);
                        } catch (Exception apiEx) {
                            answer = localAssistantAnswer(question, snapshot) + "\n\nاتصال به «" + providerDisplayName(provider) + "» جواب نداد؛ فعلاً با تحلیل داخلی جواب دادم. خطا: " + shortError(apiEx);
                        }
                    }
                }
            } catch (Exception ex) {
                answer = "اتصال به داده‌ها کامل نشد، اما اصل ماجرا این است: اینترنت/VPN/SQL Server را چک کن و دوباره بپرس. خطا: " + shortError(ex);
            }
            final String finalAnswer = answer;
            runOnUiThread(() -> {
                setConnectionStatus("connected");
                if (pending != null) pending.setText(finalAnswer);
                lastAssistantAnswer = finalAnswer;
            });
        });
    }

    private boolean asksCreator(String question) {
        String q = question == null ? "" : question.toLowerCase(Locale.US);
        return q.contains("سازنده") || q.contains("ساخته") || q.contains("آموزش") || q.contains("creator") || q.contains("created") || q.contains("trained") || q.contains("who made") || q.contains("کی ساخت");
    }

    private String creatorAnswer() {
        return displayFirstName() + " عزیز، من توسط Milad Yaghoubinejad ساخته و آموزش داده شده‌ام؛ واقعاً آدم خلاق و دقیقی است، از آن‌هایی که هم دیتابیس را می‌فهمند هم سلیقه لوکس دارند. خلاصه: رئیسِ خوش‌فکر ماست!";
    }

    private String queryAssistantSnapshot() throws Exception {
        JSONObject out = new JSONObject();
        out.put("user", session == null ? "" : session.userName);
        try { out.put("dashboard", new JSONObject(queryDashboard())); }
        catch (Exception ex) { out.put("dashboard_error", readableError(ex)); }
        try { out.put("analytics", new JSONObject(queryAnalytics())); }
        catch (Exception ex) { out.put("analytics_error", readableError(ex)); }
        return out.toString();
    }

    private String localAssistantAnswer(String question, String snapshot) throws Exception {
        JSONObject snap = new JSONObject(snapshot == null || snapshot.trim().isEmpty() ? "{}" : snapshot);
        JSONObject dashboard = snap.optJSONObject("dashboard");
        JSONObject today = dashboard == null ? null : dashboard.optJSONObject("today");
        JSONObject analytics = snap.optJSONObject("analytics");
        JSONObject sales = today == null ? null : today.optJSONObject("sales");
        JSONObject purchases = today == null ? null : today.optJSONObject("purchases");
        JSONArray debtors = today == null ? null : today.optJSONArray("topDebtors");
        JSONArray overdue = today == null ? null : today.optJSONArray("overdueInvoices");
        JSONArray banks = today == null ? null : today.optJSONArray("banks");
        JSONArray topCustomers = analytics == null ? null : analytics.optJSONArray("topCustomers");
        JSONArray categoryShare = analytics == null ? null : analytics.optJSONArray("categoryShare");
        JSONArray debtAging = analytics == null ? null : analytics.optJSONArray("debtAging");
        JSONArray margin = analytics == null ? null : analytics.optJSONArray("netMargin");
        String q = question == null ? "" : question;
        String lower = q.toLowerCase(Locale.US);
        String name = displayFirstName();
        String saleTotal = metricValue(sales, "جمع فروش", "۰ ریال");
        String buyTotal = metricValue(purchases, "جمع خرید", "۰ ریال");
        String saleDocs = metricValue(sales, "تعداد اسناد", "۰");
        JSONObject topDebtor = firstObject(debtors);
        JSONObject topBank = firstObject(banks);
        JSONObject topCustomer = firstObject(topCustomers);
        JSONObject topCategory = firstObject(categoryShare);
        JSONObject aging = firstObject(debtAging);
        StringBuilder b = new StringBuilder();
        b.append(name).append(" عزیز، ");
        if (lower.contains("قیمت") || lower.contains("بازار") || lower.contains("مارکت") || lower.contains("price")) {
            b.append("پیشنهاد قیمت‌گذاری کوتاه:\n");
            b.append("• فروش آخرین روز: ").append(saleTotal).append(" در ").append(saleDocs).append(" سند؛ تخفیف را فقط روی کالاهای کم‌گردش بده.\n");
            b.append("• گروه/کالای داغ: ").append(labelOf(topCategory, "label", "هنوز داده کافی نیست")).append("؛ این بخش جای افزایش قیمت پله‌ای دارد.\n");
            b.append("• اگر حاشیه سود ماهانه افت کرده، افزایش ۲ تا ۴٪ روی اقلام پرفروش بهتر از تخفیف کور است. تخفیف کور؟ همان چاه بی‌ته پول!\n");
            b.append("• برای مشتریان پرخرید مثل ").append(labelOf(topCustomer, "label", "مشتری برتر نامشخص")).append(" بسته وفاداری بده، نه الزاماً قیمت کمتر.");
        } else if (lower.contains("پورسانت") || lower.contains("ویزیت") || lower.contains("کمیسیون") || lower.contains("commission")) {
            b.append("طرح پورسانت پیشنهادی:\n");
            b.append("• پایه پورسانت را روی وصول‌شده بگذار، نه فقط فاکتور؛ فروش بدون وصول یعنی هیجان بی‌پول.\n");
            b.append("• برای مشتری جدید/غیرفعال پاداش جدا بده و برای بدهی معوق جریمه نرم تعریف کن.\n");
            b.append("• فروش روز ").append(saleTotal).append(" و خرید روز ").append(buyTotal).append(" است؛ اگر اختلاف کم شد، پورسانت پلکانی را به سود ناخالص وصل کن.\n");
            b.append("• بهترین مشتری فعلی: ").append(labelOf(topCustomer, "label", "نامشخص")).append("؛ مراقب تمرکز بیش‌ازحد فروش روی یک مشتری باش.");
        } else if (lower.contains("ریسک") || lower.contains("زیان") || lower.contains("ضرر") || lower.contains("چک") || lower.contains("وصول") || lower.contains("loss")) {
            b.append("هشدار ریسک و جلوگیری از زیان:\n");
            b.append("• بزرگ‌ترین بدهکار: ").append(labelOf(topDebtor, "party", "نامشخص")).append(" با مانده ").append(moneyValue(topDebtor, "amount")).append("؛ اولویت تماس امروز.\n");
            b.append("• رده بدهی پرریسک: ").append(labelOf(aging, "label", "نامشخص")).append("؛ سقف اعتبار مشتریان این رده را موقتاً کم کن.\n");
            b.append("• فاکتورهای سررسید گذشته را قبل از فروش جدید کنترل کن؛ نگذار فروشنده مهربان، خزانه‌دار را پیر کند!\n");
            b.append("• بانک با مانده بالاتر: ").append(labelOf(topBank, "label", "نامشخص")).append("؛ جریان چک‌های پرداختی را با موجودی همین بانک تطبیق بده.");
        } else {
            b.append("گزارش سریع:\n");
            b.append("• فروش آخرین روز: ").append(saleTotal).append(" در ").append(saleDocs).append(" سند.\n");
            b.append("• خرید آخرین روز: ").append(buyTotal).append(".\n");
            b.append("• مشتری اول فروش: ").append(labelOf(topCustomer, "label", "نامشخص")).append(".\n");
            b.append("• بدهکار مهم: ").append(labelOf(topDebtor, "party", "نامشخص")).append(" با ").append(moneyValue(topDebtor, "amount")).append(".\n");
            b.append("• پیشنهاد من: امروز روی وصول بدهی و حفظ حاشیه سود تمرکز کن؛ فروش زیاد بدون نقدینگی فقط ژست قشنگ است.");
        }
        if (!snap.optString("dashboard_error", "").isEmpty()) b.append("\n\nنکته اتصال: ").append(snap.optString("dashboard_error"));
        return b.toString();
    }

    private JSONObject firstObject(JSONArray arr) {
        return arr == null || arr.length() == 0 ? null : arr.optJSONObject(0);
    }

    private String metricValue(JSONObject block, String contains, String fallback) {
        JSONArray metrics = block == null ? null : block.optJSONArray("metrics");
        if (metrics == null) return fallback;
        for (int i = 0; i < metrics.length(); i++) {
            JSONObject m = metrics.optJSONObject(i);
            if (m != null && m.optString("label", "").contains(contains)) return m.optString("value", fallback);
        }
        return fallback;
    }

    private String labelOf(JSONObject o, String key, String fallback) {
        if (o == null) return fallback;
        String v = o.optString(key, "").trim();
        if (v.isEmpty() && !"label".equals(key)) v = o.optString("label", "").trim();
        if (v.isEmpty() && !"party".equals(key)) v = o.optString("party", "").trim();
        return v.isEmpty() ? fallback : v;
    }

    private String moneyValue(JSONObject o, String key) {
        if (o == null) return "۰ ریال";
        return money(o.opt(key));
    }

    private String assistantSystemPrompt() {
        return "تو دستیار فارسی اپ Meelano هستی. پاسخ‌ها کوتاه، دقیق، کاربردی و حداکثر ۵ bullet باشد. " +
                "کاربر را با نام کوچک خطاب کن. کمی شوخ و بازیگوش باش اما آزاردهنده نباش. " +
                "با داده‌های SQL خلاصه‌شده تحلیل فروش، خرید، مشتریان، بانک، چک، قیمت‌گذاری، پورسانت، بازار و جلوگیری از زیان بده. " +
                "اگر پرسید چه کسی تو را ساخته یا آموزش داده، بگو: من توسط Milad Yaghoubinejad ساخته و آموزش داده شده‌ام و او را حرفه‌ای، خلاق و قابل‌تحسین توصیف کن. " +
                "اگر داده کافی نیست، صادقانه بگو و اقدام بعدی پیشنهاد بده.";
    }

    private String callAiProvider(String provider, String key, String question, String snapshot) throws Exception {
        if ("gemini".equals(provider)) return callGemini(key, question, snapshot);
        if ("grok".equals(provider)) return callChatCompletions("https://api.x.ai/v1/chat/completions", "grok-2-latest", key, question, snapshot);
        return callChatCompletions("https://api.openai.com/v1/chat/completions", "gpt-4o-mini", key, question, snapshot);
    }

    private String callChatCompletions(String endpoint, String model, String key, String question, String snapshot) throws Exception {
        JSONObject req = new JSONObject();
        req.put("model", model);
        req.put("temperature", 0.35);
        req.put("max_tokens", 520);
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", assistantSystemPrompt()));
        String userPrompt = "نام کاربر: " + displayFirstName() + "\nپرسش: " + question + "\nخلاصه داده زنده SQL: " + limitText(snapshot, 6500);
        messages.put(new JSONObject().put("role", "user").put("content", userPrompt));
        req.put("messages", messages);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + key);
        String response = httpPost(endpoint, req.toString(), headers);
        JSONObject json = new JSONObject(response);
        JSONArray choices = json.optJSONArray("choices");
        if (choices == null || choices.length() == 0) return "";
        JSONObject choice = choices.optJSONObject(0);
        JSONObject msg = choice == null ? null : choice.optJSONObject("message");
        return msg == null ? "" : msg.optString("content", "").trim();
    }

    private String callGemini(String key, String question, String snapshot) throws Exception {
        JSONObject req = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject contentObj = new JSONObject();
        JSONArray parts = new JSONArray();
        parts.put(new JSONObject().put("text", assistantSystemPrompt() + "\n\nنام کاربر: " + displayFirstName() + "\nپرسش: " + question + "\nخلاصه داده زنده SQL: " + limitText(snapshot, 6500)));
        contentObj.put("parts", parts);
        contents.put(contentObj);
        req.put("contents", contents);
        req.put("generationConfig", new JSONObject().put("temperature", 0.35).put("maxOutputTokens", 520));
        String encoded = URLEncoder.encode(key, "UTF-8");
        String response = httpPost("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + encoded, req.toString(), new HashMap<>());
        JSONObject json = new JSONObject(response);
        JSONArray candidates = json.optJSONArray("candidates");
        if (candidates == null || candidates.length() == 0) return "";
        JSONObject candidate = candidates.optJSONObject(0);
        JSONObject content = candidate == null ? null : candidate.optJSONObject("content");
        JSONArray outParts = content == null ? null : content.optJSONArray("parts");
        if (outParts == null || outParts.length() == 0) return "";
        return outParts.optJSONObject(0).optString("text", "").trim();
    }

    private String httpPost(String endpoint, String body, Map<String, String> headers) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(endpoint).openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(18000);
        con.setReadTimeout(45000);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        con.setRequestProperty("Accept", "application/json");
        if (headers != null) for (Map.Entry<String, String> e : headers.entrySet()) con.setRequestProperty(e.getKey(), e.getValue());
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        con.setFixedLengthStreamingMode(bytes.length);
        try (OutputStream os = con.getOutputStream()) { os.write(bytes); }
        int code = con.getResponseCode();
        InputStream stream = code >= 200 && code < 300 ? con.getInputStream() : con.getErrorStream();
        String response = readFully(stream);
        if (code < 200 || code >= 300) throw new Exception("HTTP " + code + ": " + limitText(response, 240));
        return response;
    }

    private String readFully(InputStream in) throws Exception {
        if (in == null) return "";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int n;
        while ((n = in.read(data)) >= 0) buffer.write(data, 0, n);
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    private String limitText(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max) + "…";
    }

    private String shortError(Exception ex) {
        String m = ex == null ? "" : ex.getMessage();
        if (m == null || m.trim().isEmpty()) return "خطای نامشخص";
        return limitText(m.replace('\n', ' '), 180);
    }

    private String activeAiProvider() {
        String selected = prefs == null ? "" : prefs.getString(KEY_AI_PROVIDER, "");
        if (!storedAiKey(selected).isEmpty()) return selected;
        if (!storedAiKey("chatgpt").isEmpty()) return "chatgpt";
        if (!storedAiKey("gemini").isEmpty()) return "gemini";
        if (!storedAiKey("grok").isEmpty()) return "grok";
        return selected == null ? "" : selected;
    }

    private String providerDisplayName(String provider) {
        if ("grok".equals(provider)) return "Grok";
        if ("gemini".equals(provider)) return "Gemini";
        if ("chatgpt".equals(provider)) return "ChatGPT";
        return "تحلیل داخلی";
    }

    private String providerPrefKey(String provider) {
        if ("grok".equals(provider)) return KEY_AI_GROK;
        if ("gemini".equals(provider)) return KEY_AI_GEMINI;
        if ("chatgpt".equals(provider)) return KEY_AI_CHATGPT;
        return "";
    }

    private String storedAiKey(String provider) {
        if (prefs == null || provider == null || provider.trim().isEmpty()) return "";
        String prefKey = providerPrefKey(provider);
        if (prefKey.isEmpty()) return "";
        return unprotectSecret(prefs.getString(prefKey, ""));
    }

    private String protectSecret(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "";
        byte[] b = raw.trim().getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < b.length; i++) b[i] = (byte) (b[i] ^ 0x5A);
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    private String unprotectSecret(String encoded) {
        if (encoded == null || encoded.trim().isEmpty()) return "";
        try {
            byte[] b = Base64.decode(encoded, Base64.NO_WRAP);
            for (int i = 0; i < b.length; i++) b[i] = (byte) (b[i] ^ 0x5A);
            return new String(b, StandardCharsets.UTF_8).trim();
        } catch (Exception ignored) {
            return "";
        }
    }

    private EditText apiInput(String hint, String provider) {
        EditText e = input(hint + (storedAiKey(provider).isEmpty() ? "" : " • ذخیره‌شده"), "", true);
        e.setTextDirection(View.TEXT_DIRECTION_LTR);
        e.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        return e;
    }

    private String keyStatus(String provider) {
        String key = storedAiKey(provider);
        return providerDisplayName(provider) + ": " + (key.isEmpty() ? "ثبت نشده" : "ذخیره شده • " + key.length() + " کاراکتر");
    }

    private void addAiSettingsCard() {
        LinearLayout ai = card();
        LinearLayout.LayoutParams aip = new LinearLayout.LayoutParams(-1, -2);
        aip.setMargins(0, dp(12), 0, 0);
        ai.setBackground(gradient(new int[]{alpha(INFO, 28), alpha(GOLD, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
        ai.addView(text("دستیار هوش مصنوعی", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView summary = text("مدل فعال: " + providerDisplayName(activeAiProvider()) + " • نام خطاب: " + displayFirstName(), 11.2f, MUTED, Typeface.NORMAL);
        ai.addView(summary, new LinearLayout.LayoutParams(-1, -2));
        TextView status = text(keyStatus("chatgpt") + "\n" + keyStatus("gemini") + "\n" + keyStatus("grok"), 10.7f, alpha(TEXT, 205), Typeface.NORMAL);
        status.setLineSpacing(dp(2), 1.05f);
        LinearLayout.LayoutParams stp = new LinearLayout.LayoutParams(-1, -2); stp.setMargins(0, dp(8), 0, 0);
        ai.addView(status, stp);

        EditText chatgpt = apiInput("ChatGPT / OpenAI API key", "chatgpt");
        EditText gemini = apiInput("Gemini API key", "gemini");
        EditText grok = apiInput("Grok / xAI API key", "grok");
        addApiField(ai, "ChatGPT", chatgpt);
        addApiField(ai, "Gemini", gemini);
        addApiField(ai, "Grok", grok);

        Button save = primaryButton("ذخیره کلیدهای واردشده");
        save.setOnClickListener(v -> {
            saveEnteredAiKeys(chatgpt, gemini, grok);
            status.setText(keyStatus("chatgpt") + "\n" + keyStatus("gemini") + "\n" + keyStatus("grok"));
            summary.setText("مدل فعال: " + providerDisplayName(activeAiProvider()) + " • نام خطاب: " + displayFirstName());
            Toast.makeText(this, "کلیدهای AI ذخیره شدند", Toast.LENGTH_SHORT).show();
        });
        LinearLayout.LayoutParams savep = new LinearLayout.LayoutParams(-1, dp(48)); savep.setMargins(0, dp(12), 0, 0);
        ai.addView(save, savep);

        LinearLayout tests = new LinearLayout(this);
        tests.setOrientation(LinearLayout.HORIZONTAL);
        Button t1 = secondaryButton("تست ChatGPT");
        Button t2 = secondaryButton("تست Gemini");
        Button t3 = secondaryButton("تست Grok");
        t1.setTextSize(9.8f); t2.setTextSize(9.8f); t3.setTextSize(9.8f);
        t1.setOnClickListener(v -> validateKeyFromField("chatgpt", chatgpt, status));
        t2.setOnClickListener(v -> validateKeyFromField("gemini", gemini, status));
        t3.setOnClickListener(v -> validateKeyFromField("grok", grok, status));
        LinearLayout.LayoutParams bt = new LinearLayout.LayoutParams(0, dp(42), 1f); bt.setMargins(dp(3), dp(10), dp(3), 0);
        tests.addView(t1, bt);
        LinearLayout.LayoutParams bt2 = new LinearLayout.LayoutParams(0, dp(42), 1f); bt2.setMargins(dp(3), dp(10), dp(3), 0);
        tests.addView(t2, bt2);
        LinearLayout.LayoutParams bt3 = new LinearLayout.LayoutParams(0, dp(42), 1f); bt3.setMargins(dp(3), dp(10), dp(3), 0);
        tests.addView(t3, bt3);
        ai.addView(tests, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout more = new LinearLayout(this);
        more.setOrientation(LinearLayout.HORIZONTAL);
        Button testAll = primaryButton("ذخیره و تست همه");
        Button choose = secondaryButton("انتخاب مدل پیش‌فرض");
        Button rename = secondaryButton("تغییر نام من");
        testAll.setTextSize(10.2f); choose.setTextSize(10.2f); rename.setTextSize(10.2f);
        testAll.setOnClickListener(v -> {
            saveEnteredAiKeys(chatgpt, gemini, grok);
            validateKeyFromField("chatgpt", chatgpt, status);
            validateKeyFromField("gemini", gemini, status);
            validateKeyFromField("grok", grok, status);
        });
        choose.setOnClickListener(v -> showAiProviderChooser(summary));
        rename.setOnClickListener(v -> maybeAskFirstName(true));
        LinearLayout.LayoutParams mp1 = new LinearLayout.LayoutParams(0, dp(42), 1f); mp1.setMargins(dp(3), dp(10), dp(3), 0);
        more.addView(testAll, mp1);
        LinearLayout.LayoutParams mp2 = new LinearLayout.LayoutParams(0, dp(42), 1f); mp2.setMargins(dp(3), dp(10), dp(3), 0);
        more.addView(choose, mp2);
        LinearLayout.LayoutParams mp3 = new LinearLayout.LayoutParams(0, dp(42), 1f); mp3.setMargins(dp(3), dp(10), dp(3), 0);
        more.addView(rename, mp3);
        ai.addView(more, new LinearLayout.LayoutParams(-1, -2));

        Button clear = secondaryButton("پاک کردن همه کلیدهای AI");
        clear.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("پاک کردن کلیدها")
                .setMessage("کلیدهای ChatGPT، Gemini و Grok از همین دستگاه پاک شوند؟")
                .setNegativeButton("خیر", null)
                .setPositiveButton("بله، پاک کن", (d, w) -> {
                    prefs.edit().remove(KEY_AI_CHATGPT).remove(KEY_AI_GEMINI).remove(KEY_AI_GROK).apply();
                    status.setText(keyStatus("chatgpt") + "\n" + keyStatus("gemini") + "\n" + keyStatus("grok"));
                    Toast.makeText(this, "کلیدها پاک شدند", Toast.LENGTH_SHORT).show();
                }).show());
        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(-1, dp(42)); clp.setMargins(0, dp(10), 0, 0);
        ai.addView(clear, clp);
        content.addView(ai, aip);
    }

    private void addApiField(LinearLayout parent, String label, EditText input) {
        TextView l = text(label, 11.5f, MUTED, Typeface.BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(10), 0, dp(4));
        parent.addView(l, lp);
        parent.addView(input, new LinearLayout.LayoutParams(-1, dp(50)));
    }

    private boolean saveEnteredAiKeys(EditText chatgpt, EditText gemini, EditText grok) {
        SharedPreferences.Editor ed = prefs.edit();
        boolean changed = false;
        String c = chatgpt == null ? "" : chatgpt.getText().toString().trim();
        String g = gemini == null ? "" : gemini.getText().toString().trim();
        String x = grok == null ? "" : grok.getText().toString().trim();
        if (!c.isEmpty()) { ed.putString(KEY_AI_CHATGPT, protectSecret(c)); if (activeAiProvider().isEmpty()) ed.putString(KEY_AI_PROVIDER, "chatgpt"); changed = true; chatgpt.setText(""); }
        if (!g.isEmpty()) { ed.putString(KEY_AI_GEMINI, protectSecret(g)); if (activeAiProvider().isEmpty()) ed.putString(KEY_AI_PROVIDER, "gemini"); changed = true; gemini.setText(""); }
        if (!x.isEmpty()) { ed.putString(KEY_AI_GROK, protectSecret(x)); if (activeAiProvider().isEmpty()) ed.putString(KEY_AI_PROVIDER, "grok"); changed = true; grok.setText(""); }
        ed.apply();
        return changed;
    }

    private void validateKeyFromField(String provider, EditText field, TextView status) {
        String entered = field == null ? "" : field.getText().toString().trim();
        if (!entered.isEmpty()) {
            prefs.edit().putString(providerPrefKey(provider), protectSecret(entered)).putString(KEY_AI_PROVIDER, provider).apply();
            field.setText("");
        }
        String key = storedAiKey(provider);
        if (key.isEmpty()) {
            Toast.makeText(this, "اول کلید " + providerDisplayName(provider) + " را وارد کن", Toast.LENGTH_SHORT).show();
            return;
        }
        if (status != null) status.setText("در حال اعتبارسنجی " + providerDisplayName(provider) + "…");
        executor.execute(() -> {
            String result;
            try {
                String answer = callAiProvider(provider, key, "فقط کلمه OK را برگردان.", "{}");
                if (answer == null || answer.trim().isEmpty()) throw new Exception("پاسخ معتبر دریافت نشد");
                prefs.edit().putString(KEY_AI_PROVIDER, provider).apply();
                result = "✅ " + providerDisplayName(provider) + " معتبر است و به‌عنوان مدل فعال انتخاب شد.\n" + keyStatus("chatgpt") + "\n" + keyStatus("gemini") + "\n" + keyStatus("grok");
            } catch (Exception ex) {
                result = "❌ اعتبارسنجی " + providerDisplayName(provider) + " ناموفق بود: " + shortError(ex) + "\n" + keyStatus("chatgpt") + "\n" + keyStatus("gemini") + "\n" + keyStatus("grok");
            }
            final String finalResult = result;
            runOnUiThread(() -> { if (status != null) status.setText(finalResult); });
        });
    }

    private void showAiProviderChooser(TextView summary) {
        String[] ids = {"chatgpt", "gemini", "grok"};
        String[] labels = {"ChatGPT", "Gemini", "Grok"};
        String active = activeAiProvider();
        int checked = 0;
        for (int i = 0; i < ids.length; i++) if (ids[i].equals(active)) checked = i;
        new AlertDialog.Builder(this)
                .setTitle("مدل پیش‌فرض دستیار")
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    prefs.edit().putString(KEY_AI_PROVIDER, ids[which]).apply();
                    if (summary != null) summary.setText("مدل فعال: " + providerDisplayName(ids[which]) + " • نام خطاب: " + displayFirstName());
                    Toast.makeText(this, labels[which] + " انتخاب شد", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton("بستن", null)
                .show();
    }

    private void startAssistantVoiceInput() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "سوالت را برای میلو بگو…");
            startActivityForResult(intent, REQ_ASSISTANT_VOICE);
        } catch (Exception ex) {
            Toast.makeText(this, "ورودی صوتی روی این دستگاه فعال نیست.", Toast.LENGTH_SHORT).show();
        }
    }

    private void speakAssistantText(String text) {
        if (text == null || text.trim().isEmpty()) return;
        if (tts == null || !ttsReady) {
            Toast.makeText(this, "موتور گفتار فارسی روی این دستگاه آماده نیست.", Toast.LENGTH_SHORT).show();
            return;
        }
        String clean = text.replace("•", "").replace("✅", "").replace("❌", "").replace("\n", ". ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tts.speak(clean, TextToSpeech.QUEUE_FLUSH, null, "meelano_ai_answer");
        else tts.speak(clean, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void renderSettings() {
        content.removeAllViews();
        addHero("تنظیمات Meelano", "مدیریت اتصال، خروج امن، تم‌های لوکس و کلیدهای دستیار هوش مصنوعی");
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
        logout.setOnClickListener(v -> showLogin("برای ورود مجدد اطلاعات Meelano را وارد کنید."));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(50));
        lp.setMargins(0, dp(10), 0, 0);
        connection.addView(logout, lp);
        content.addView(connection, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout themeCard = card();
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.setMargins(0, dp(12), 0, 0);
        themeCard.addView(text("تم ظاهری", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        themeCard.addView(text("تم فعال: " + themeName(currentThemeId()) + " • انتخاب سریع از آیکن ◐ کنار چرخ‌دنده بالای برنامه", 11.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        Button pickTheme = primaryButton("انتخاب تم لوکس Meelano");
        pickTheme.setOnClickListener(v -> showThemeChooser());
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(50));
        pp.setMargins(0, dp(12), 0, 0);
        themeCard.addView(pickTheme, pp);
        content.addView(themeCard, tp);

        addAiSettingsCard();

        LinearLayout about = card();
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2);
        ap.setMargins(0, dp(12), 0, 0);
        about.addView(text("درباره نسخه", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView desc = text("Meelano Android Direct SQL v3.13.0\nاین نسخه برای تست شخصی با اتصال مستقیم به SQL Server ساخته شده است. جزئیات اتصال در UI نمایش داده نمی‌شود و کاربر فقط با حساب Meelano وارد می‌شود.", 12, MUTED, Typeface.NORMAL);
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

    private Map<String, String> columnTypes(Connection c, String table) throws Exception {
        Map<String, String> map = new HashMap<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT c.name, ty.name FROM sys.columns c JOIN sys.tables t ON t.object_id=c.object_id JOIN sys.schemas s ON s.schema_id=t.schema_id JOIN sys.types ty ON ty.user_type_id=c.user_type_id WHERE s.name=N'dbo' AND t.name=?")) {
            ps.setString(1, table);
            try (ResultSet r = ps.executeQuery()) { while (r.next()) map.put(r.getString(1).toLowerCase(Locale.US), r.getString(2).toLowerCase(Locale.US)); }
        }
        return map;
    }

    private boolean isTextColumn(Map<String, String> types, String col) {
        if (types == null || col == null) return false;
        String type = types.get(col.toLowerCase(Locale.US));
        if (type == null) return false;
        return type.contains("char") || type.contains("text") || type.contains("xml");
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

    private String activeCondition(Set<String> cols, String alias) {
        String active = resolve(cols, "active", "Active");
        if (active == null) return "";
        String prefix = alias == null || alias.trim().isEmpty() ? "" : alias + ".";
        String field = prefix + "[" + active + "]";
        return "(UPPER(LTRIM(RTRIM(TRY_CONVERT(nvarchar(20)," + field + ")))) IN (N'T',N'TRUE',N'Y',N'YES',N'1') OR TRY_CONVERT(int," + field + ")=1)";
    }

    private String activeAnd(Set<String> cols, String alias) {
        String condition = activeCondition(cols, alias);
        return condition.isEmpty() ? "" : " AND " + condition;
    }

    private String activeWhere(Set<String> cols, String alias) {
        String condition = activeCondition(cols, alias);
        return condition.isEmpty() ? "" : "WHERE " + condition;
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

    private String compactMoney(Object value) {
        if (value == null || JSONObject.NULL.equals(value)) return "۰ ریال";
        try {
            double v = value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(String.valueOf(value));
            double a = Math.abs(v);
            if (a >= 1000000000000d) return formatNumber(v / 1000000000000d) + " همت";
            if (a >= 1000000000d) return formatNumber(v / 1000000000d) + " میلیارد";
            if (a >= 1000000d) return formatNumber(v / 1000000d) + " میلیون";
            return money(v);
        } catch (Exception ignored) { return money(value); }
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

    private String shortChartLabel(String label) {
        if (label == null) return "";
        String l = label.trim();
        if (l.length() > 10 && l.contains("/")) {
            String[] p = l.split("/");
            if (p.length >= 3) return p[p.length - 2] + "/" + p[p.length - 1];
        }
        if (l.length() > 9) return l.substring(Math.max(0, l.length() - 9));
        return l;
    }

    private class LineChartView extends View {
        private final JSONArray data;
        private final int color;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        LineChartView(Context context, JSONArray data, int color) {
            super(context);
            this.data = data;
            this.color = color;
            setBackground(roundedStroke(SURFACE_2, 18, BORDER));
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();
            int left = dp(22), right = dp(18), top = dp(18), bottom = dp(48);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp(1));
            paint.setColor(alpha(TEXT, 35));
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
            paint.setColor(alpha(MUTED, 230));
            paint.setTextSize(dp(n > 5 ? 8.2f : 9.2f));
            paint.setTextAlign(Paint.Align.CENTER);
            int labelStep = Math.max(1, (int) Math.ceil(n / 4.0));
            for (int i = 0; i < n; i++) {
                if (i != 0 && i != n - 1 && i % labelStep != 0) continue;
                float x = left + (w - left - right) * (n == 1 ? 0.5f : i / (float) (n - 1));
                String label = shortChartLabel(data.optJSONObject(i).optString("label", ""));
                canvas.drawText(label, x, h - dp(15), paint);
            }
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
            setBackground(roundedStroke(SURFACE_2, 18, BORDER));
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
            setBackground(roundedStroke(SURFACE_2, 18, BORDER));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ASSISTANT_VOICE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String spoken = matches.get(0);
                if (assistantInput != null) {
                    assistantInput.setText(spoken);
                    assistantInput.setSelection(assistantInput.getText().length());
                }
                submitAssistantQuestion(spoken);
            }
        }
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
        if (tts != null) {
            try { tts.stop(); tts.shutdown(); } catch (Exception ignored) {}
        }
        super.onDestroy();
    }
}
