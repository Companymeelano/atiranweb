package ir.meelano.android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.drawable.GradientDrawable;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final String PREFS = "meelano_android_direct_sql";
    private static final String DEFAULT_THEME = "azure_diamond";
    private static final String KEY_LAST_USER = "last_meelano_user";
    private static final String KEY_THEME = "meelano_theme_palette";
    private static final String KEY_FIRST_NAME = "assistant_first_name";
    private static final String KEY_NAME_ASKED = "assistant_name_asked";
    private static final String KEY_AI_PROVIDER = "assistant_ai_provider";
    private static final String KEY_AI_CHATGPT = "assistant_ai_chatgpt";
    private static final String KEY_AI_GEMINI = "assistant_ai_gemini";
    private static final String KEY_AI_GROK = "assistant_ai_grok";
    private static final String KEY_AI_GROQ = "assistant_ai_groq";
    private static final String KEY_AI_GAPGPT = "assistant_ai_gapgpt";
    private static final String KEY_AI_GAPGPT_BASE = "assistant_ai_gapgpt_base";
    private static final String KEY_COMPACT_UI = "meelano_compact_ui";
    private static final String KEY_REDUCED_MOTION = "meelano_reduced_motion";
    private static final String KEY_CACHE_DASHBOARD = "cache_dashboard_json";
    private static final String KEY_CACHE_REPORTS = "cache_reports_json";
    private static final String KEY_LAST_ALERT_DAY = "last_dashboard_alert_day";
    private static final String KEY_QUICK_LOGIN_ENABLED = "quick_login_enabled";
    private static final String KEY_QUICK_PIN = "quick_login_pin";
    private static final String KEY_QUICK_USER_ID = "quick_user_id";
    private static final String KEY_QUICK_VISITOR_ID = "quick_visitor_id";
    private static final String KEY_QUICK_USER_NAME = "quick_user_name";
    private static final String KEY_QUICK_ROLE = "quick_access_role";
    private static final String KEY_QUICK_PERMISSIONS = "quick_access_permissions";
    private static final String KEY_CART_DRAFT = "visitor_cart_draft_json";
    private static final String KEY_CART_DRAFTS = "visitor_cart_drafts_json";
    private static final String KEY_OFFLINE_PREF_QUEUE = "visitor_prefactor_offline_queue_json";
    private static final String KEY_VISIT_RESULTS_LOCAL = "visitor_visit_results_local_json";
    private static final String KEY_WIDGET_SUMMARY = "widget_summary";
    private static final String KEY_LAST_CONNECTION_OK = "last_connection_ok";
    private static final String KEY_LAST_CONNECTION_ERROR = "last_connection_error";
    private static final String KEY_TAX_BASE_URL = "taxpayer_base_url";
    private static final String KEY_TAX_MEMORY_ID = "taxpayer_memory_id";
    private static final String KEY_TAX_ECONOMIC_ID = "taxpayer_economic_id";
    private static final String KEY_TAX_CLIENT_ID = "taxpayer_client_id";
    private static final String KEY_TAX_AUTH_KEY = "taxpayer_auth_key";
    private static final String KEY_TAX_PRIVATE_KEY = "taxpayer_private_key";
    private static final String KEY_TAX_SENT = "taxpayer_sent_invoice_keys";
    private static final String KEY_TAX_EXCLUDED = "taxpayer_excluded_invoice_keys";
    private static final String KEY_TAX_FAILED = "taxpayer_failed_invoice_keys";
    private static final String KEY_TAX_LAST_REPORT = "taxpayer_last_report";
    private static final String KEY_CAMERA_HOST = "camera_dvr_host";
    private static final String KEY_CAMERA_PORT = "camera_dvr_port";
    private static final String KEY_CAMERA_USER = "camera_dvr_user";
    private static final String KEY_CAMERA_PASS = "camera_dvr_pass";
    private static final String KEY_CAMERA_CHANNELS = "camera_dvr_channels";
    private static final String KEY_CAMERA_TEMPLATE = "camera_stream_template";
    private static final String KEY_CAMERA_REPLAY_TEMPLATE = "camera_replay_template";
    private static final String KEY_CAMERA_LAYOUT = "camera_grid_layout";
    private static final String KEY_ALARM_LOG = "alarm_last_log";
    private static final String KEY_REMIND_CHECKS = "remind_checks";
    private static final String KEY_REMIND_DEBTORS = "remind_debtors";
    private static final String KEY_REMIND_INACTIVE = "remind_inactive";
    private static final String KEY_REMIND_DAILY = "remind_daily";
    private static final String KEY_REMIND_HOUR = "remind_hour";
    private static final String NOTIFY_CHANNEL = "meelano_management_alerts";
    private static final int REQ_ASSISTANT_VOICE = 9401;
    private static final int REQ_BARCODE_SCAN = 9402;
    private static final int REQ_TTS_CHECK = 9403;
    private static final int REQ_CHAT_ATTACHMENT = 9404;
    private static final int REQ_WIFI_PERMISSION = 9405;

    private static final int[] S_HOST = {122, 126, 103, 120, 125, 122, 103, 120, 125, 126, 103, 120, 112};
    private static final int[] S_USER = {8, 45, 36, 32, 39, 8, 39};
    private static final int[] S_PASS = {26, 61, 9, 27, 123, 121, 123, 123, 109};
    private static final int[] S_DB = {8, 61, 32, 59, 40, 39, 123};
    private static final int S_KEY = 73;
    private static final int SQL_PORT = 1433;
    private static final String LOCAL_KEY_ALIAS = "meelano_local_secret_v325";

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
    private String pendingTtsText = "";
    private String lastAssistantAnswer = "";
    private String lastReportSummary = "";
    private String lastReportJson = "";
    private String miloMood = "happy";
    private boolean miloThinking = false;
    private boolean miloSpeaking = false;
    private String dashboardCacheJson = "";
    private String reportsCacheJson = "";
    private String customersCacheJson = "";
    private String customersCacheQuery = "";
    private String customersCacheFilter = "all";
    private boolean customersCacheAllRows = false;
    private String customersSortOrder = "smart";
    private String productsCacheJson = "";
    private String productsCacheQuery = "";
    private String productsCacheFilter = "all";
    private String pendingChatAttachmentKind = "file";
    private String chatSearchQuery = "";
    private String taxPeriod = "day";
    private String taxDocType = "all";
    private JSONArray taxCurrentInvoices = new JSONArray();
    private final Set<String> taxSelectedKeys = new HashSet<>();
    private int alarmActiveDevice = 1;
    private JSONArray visitorCartItems = new JSONArray();
    private JSONObject visitorCartCustomer = null;
    private String visitorCartNotes = "";
    private String visitorCartSignature = "";
    private String visitorCartDraftId = "";
    private String visitorCartSettlement = "اعتباری";
    private String visitorCartDeliveryDate = "";
    private String visitorCartAddress = "";
    private String visitorCartPaymentRef = "";
    private double visitorCartGlobalDiscount = 0;
    private double visitorCartTaxPercent = 0;
    private EditText cartNotesInput;
    private EditText cartDiscountInput;
    private EditText cartTaxInput;
    private EditText cartDeliveryInput;
    private EditText cartAddressInput;
    private EditText cartPaymentRefInput;
    private volatile boolean lastSqlVpnDetected = false;
    private volatile boolean lastSqlVpnBypassed = false;
    private volatile String lastSqlNetworkNote = "";
    private final Map<String, String> customerLedgerCache = new HashMap<>();

    private static final Set<String> SAFE_TABLES = new HashSet<>(Arrays.asList(
            "CUSTOMERS", "inventory", "sailfact", "subsailfact", "sailfact_pish", "subsailfact_pish",
            "buyfact", "subbuyfact", "getchk", "putchk", "CheckTypes", "visitors", "Visit", "masir",
            "vis_goals", "Variety", "UNITS", "BANK", "kagroup"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        applyTheme(prefs.getString(KEY_THEME, DEFAULT_THEME));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initSpeechEngine();
        initNotificationChannel();
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

    private boolean compactUi() {
        if (prefs != null && prefs.getBoolean(KEY_COMPACT_UI, false)) return true;
        try {
            float widthDp = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;
            return widthDp > 0 && widthDp < 370;
        } catch (Exception ignored) { return false; }
    }

    private boolean isLightTheme() {
        return Color.red(NAVY) + Color.green(NAVY) + Color.blue(NAVY) > 520;
    }

    private boolean motionAllowed() {
        if (prefs != null && prefs.getBoolean(KEY_REDUCED_MOTION, false)) return false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                if (pm != null && pm.isPowerSaveMode()) return false;
            }
        } catch (Exception ignored) { }
        return true;
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationChannel ch = new NotificationChannel(NOTIFY_CHANNEL, "هشدارهای مدیریتی Meelano", NotificationManager.IMPORTANCE_DEFAULT);
                ch.setDescription("یادآوری چک‌ها، مطالبات و هشدارهای مهم داشبورد");
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (nm != null) nm.createNotificationChannel(ch);
            } catch (Exception ignored) { }
        }
    }

    private void requestNotificationPermissionIfNeeded() {
        try {
            if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 9134);
            }
        } catch (Exception ignored) { }
    }

    private String normalizeThemeId(String themeId) {
        String id = themeId == null ? DEFAULT_THEME : themeId.trim();
        if (id.isEmpty()) return DEFAULT_THEME;
        if ("royal_amethyst".equals(id) || "ivory_sunrise".equals(id) || "crystal_lagoon".equals(id) || "azure_diamond".equals(id) || "noir_aurora".equals(id) || "onyx_gold".equals(id)) return id;
        return DEFAULT_THEME;
    }

    private void applyTheme(String themeId) {
        String id = normalizeThemeId(themeId);
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
        } else if ("onyx_gold".equals(id)) {
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
        return normalizeThemeId(prefs == null ? DEFAULT_THEME : prefs.getString(KEY_THEME, DEFAULT_THEME));
    }

    private String themeName(String id) {
        if ("royal_amethyst".equals(id)) return "شب آمتیست سلطنتی";
        if ("ivory_sunrise".equals(id)) return "طلوع عاجی لوکس";
        if ("crystal_lagoon".equals(id)) return "لاگون کریستالی روشن";
        if ("azure_diamond".equals(id)) return "الماس آبی روشن";
        if ("noir_aurora".equals(id)) return "نوآر شفق لوکس";
        if ("onyx_gold".equals(id)) return "اونیکس طلایی Meelano";
        return "الماس آبی روشن";
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

    private int onColorFor(int color) {
        int score = Color.red(color) * 299 + Color.green(color) * 587 + Color.blue(color) * 114;
        return score > 156000 ? Color.rgb(18, 23, 34) : Color.WHITE;
    }

    private int themeAccent(String key) {
        String k = key == null ? "" : key.toLowerCase(Locale.US);
        if (k.contains("debt") || k.contains("danger") || k.contains("risk") || k.contains("مطالب")) return DANGER;
        if (k.contains("warning") || k.contains("check") || k.contains("چک")) return WARNING;
        if (k.contains("success") || k.contains("customer") || k.contains("مشتری")) return SUCCESS;
        if (k.contains("tax") || k.contains("مودیان") || k.contains("مالیات")) return mix(GOLD, INFO, 0.24f);
        if (k.contains("camera") || k.contains("دوربین") || k.contains("dvr")) return mix(INFO, SUCCESS, 0.28f);
        if (k.contains("alarm") || k.contains("دزدگیر") || k.contains("امنیت")) return mix(DANGER, GOLD, 0.32f);
        if (k.contains("attendance") || k.contains("حضور")) return mix(GOLD, SUCCESS, 0.32f);
        if (k.contains("chat") || k.contains("گفتگو")) return mix(GOLD_2, INFO, 0.62f);
        if (k.contains("personnel") || k.contains("پرسنل")) return mix(SUCCESS, INFO, 0.38f);
        if (k.contains("report") || k.contains("گزارش")) return INFO;
        if (k.contains("product") || k.contains("کالا")) return WARNING;
        if (k.contains("theme") || k.contains("تم")) return GOLD_2;
        return GOLD;
    }

    private GradientDrawable premiumPanel(int accent, float radius) {
        int glow = mix(accent, Color.WHITE, isLightTheme() ? 0.42f : 0.18f);
        int deep = mix(SURFACE, accent, isLightTheme() ? 0.06f : 0.18f);
        GradientDrawable d = gradient(new int[]{alpha(Color.WHITE, isLightTheme() ? 92 : 20), alpha(glow, isLightTheme() ? 42 : 34), alpha(deep, 248)}, GradientDrawable.Orientation.TL_BR, radius);
        d.setStroke(dp(1), alpha(mix(accent, Color.WHITE, 0.30f), isLightTheme() ? 96 : 82));
        return d;
    }

    private GradientDrawable luxuryButtonBg(int accent, boolean primary, float radius) {
        GradientDrawable d;
        if (primary) {
            d = gradient(new int[]{mix(accent, Color.WHITE, isLightTheme() ? 0.34f : 0.20f), accent, mix(GOLD, accent, 0.36f), mix(accent, Color.BLACK, isLightTheme() ? 0.08f : 0.28f)}, GradientDrawable.Orientation.LEFT_RIGHT, radius);
            d.setStroke(dp(1), alpha(mix(accent, Color.WHITE, 0.52f), 150));
        } else {
            d = gradient(new int[]{alpha(Color.WHITE, isLightTheme() ? 95 : 18), alpha(accent, isLightTheme() ? 22 : 30), alpha(SURFACE_2, 235)}, GradientDrawable.Orientation.TL_BR, radius);
            d.setStroke(dp(1), alpha(mix(accent, Color.WHITE, 0.22f), 92));
        }
        return d;
    }

    private void applyTouchFeedback(View v) {
        if (v == null) return;
        v.setOnTouchListener((view, event) -> {
            if (!motionAllowed() || event == null) return false;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                view.animate().scaleX(0.975f).scaleY(0.975f).alpha(0.94f).setDuration(90).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                view.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(130).start();
            }
            return false;
        });
    }

    private String navGlyph(String key) {
        if ("dashboard".equals(key)) return "⌂";
        if ("customers".equals(key)) return "♙";
        if ("products".equals(key)) return "◍";
        if ("reports".equals(key)) return "≡";
        if ("command".equals(key)) return "⌘";
        if ("assistant".equals(key)) return "✦";
        if ("chat".equals(key)) return "✉";
        if ("personnel".equals(key)) return "ID";
        if ("attendance".equals(key)) return "⏱";
        if ("taxpayers".equals(key)) return "٪";
        if ("cameras".equals(key)) return "▣";
        if ("alarm".equals(key)) return "◬";
        return "◆";
    }

    private String semanticGlyph(String label) {
        String t = label == null ? "" : label;
        if (t.contains("تماس")) return "☎";
        if (t.contains("پیام") || t.contains("گفتگو")) return "✉";
        if (t.contains("گردش") || t.contains("حساب")) return "☷";
        if (t.contains("CSV")) return "CSV";
        if (t.contains("PDF")) return "PDF";
        if (t.contains("تازه") || t.contains("بروزرسان")) return "⟳";
        if (t.contains("تم")) return "✺";
        if (t.contains("مودیان") || t.contains("مالیات")) return "٪";
        if (t.contains("دوربین") || t.contains("DVR")) return "▣";
        if (t.contains("دزدگیر") || t.contains("امنیت")) return "◬";
        if (t.contains("مرخصی")) return "☘";
        if (t.contains("ورود")) return "↘";
        if (t.contains("خروج")) return "↗";
        if (t.contains("جستجو")) return "⌕";
        if (t.contains("فروش")) return "↗";
        if (t.contains("خرید")) return "↙";
        if (t.contains("چک")) return "✓";
        if (t.contains("کالا")) return "◍";
        if (t.contains("مشتری")) return "♙";
        return "◆";
    }

    private String withIcon(String glyph, String label) {
        String g = glyph == null ? "" : glyph.trim();
        String l = label == null ? "" : label.trim();
        return g.isEmpty() ? l : g + "  " + l;
    }

    private TextView pill(String label, int accent, boolean filled) {
        TextView chip = text(label, 9.6f, filled ? onColorFor(accent) : accent, Typeface.BOLD);
        chip.setGravity(Gravity.CENTER);
        chip.setSingleLine(true);
        chip.setPadding(dp(8), dp(4), dp(8), dp(4));
        chip.setBackground(filled ? luxuryButtonBg(accent, true, 999) : roundedStroke(alpha(accent, isLightTheme() ? 18 : 28), 999, alpha(accent, 86)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) chip.setElevation(dp(filled ? 3 : 1));
        return chip;
    }

    private void styleMeelanoDialog(AlertDialog dialog, int accent) {
        if (dialog == null) return;
        dialog.setOnShowListener(d -> {
            try {
                if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(premiumPanel(accent, 30));
                Button pos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button neg = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                Button neu = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                if (pos != null) { pos.setTextColor(accent); pos.setAllCaps(false); pos.setTypeface(Typeface.DEFAULT, Typeface.BOLD); applyTouchFeedback(pos); }
                if (neg != null) { neg.setTextColor(MUTED); neg.setAllCaps(false); applyTouchFeedback(neg); }
                if (neu != null) { neu.setTextColor(INFO); neu.setAllCaps(false); applyTouchFeedback(neu); }
            } catch (Exception ignored) { }
        });
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

    private GradientDrawable glassGradient(int accent, float radius) {
        GradientDrawable d = gradient(new int[]{alpha(Color.WHITE, 30), alpha(accent, 22), alpha(SURFACE, 246)}, GradientDrawable.Orientation.TL_BR, radius);
        d.setStroke(dp(1), alpha(mix(accent, Color.WHITE, 0.24f), 72));
        return d;
    }

    private GradientDrawable diamondStroke(int base, int accent, float radius) {
        GradientDrawable d = gradient(new int[]{mix(base, Color.WHITE, 0.05f), alpha(accent, 20), base}, GradientDrawable.Orientation.TL_BR, radius);
        d.setStroke(dp(1), alpha(accent, 58));
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
            boolean moving = motionAllowed();
            float t = moving ? (System.currentTimeMillis() - startMs) / 1000f : 0f;
            float s = Math.min(w, h);
            float pulse = (float) Math.sin(t * 2.2f);
            float drift = (float) Math.sin(t * 1.35f);
            int skyA = mix(HEADER_START, INFO, isLightTheme() ? 0.18f : 0.08f);
            int skyB = mix(NAVY, themeAccent(activePage), isLightTheme() ? 0.20f : 0.13f);
            int moonColor = mix(GOLD_2, Color.WHITE, isLightTheme() ? 0.45f : 0.28f);
            int mColor = mix(GOLD, themeAccent(activePage), isLightTheme() ? 0.36f : 0.22f);
            RectF outer = new RectF(s * 0.06f, s * 0.06f, w - s * 0.06f, h - s * 0.06f);
            p.setStyle(Paint.Style.FILL);
            p.setShadowLayer(s * 0.08f, 0, s * 0.028f, alpha(Color.BLACK, 145));
            p.setColor(skyA);
            canvas.drawRoundRect(outer, s * 0.24f, s * 0.24f, p);
            p.clearShadowLayer();
            p.setColor(alpha(skyB, 205));
            canvas.drawCircle(s * 0.24f, s * 0.88f, s * 0.72f, p);
            p.setColor(alpha(INFO, isLightTheme() ? 44 : 34));
            canvas.drawCircle(s * 0.82f, s * 0.15f, s * 0.45f, p);

            float moonX = w - s * 0.24f;
            float moonY = s * 0.23f;
            p.setColor(alpha(moonColor, 92));
            canvas.drawCircle(moonX, moonY, s * (0.245f + 0.016f * pulse), p);
            p.setColor(alpha(mix(moonColor, GOLD, 0.20f), 225));
            canvas.drawCircle(moonX, moonY, s * 0.165f, p);
            p.setColor(alpha(Color.WHITE, 120));
            canvas.drawCircle(moonX - s * 0.045f, moonY - s * 0.04f, s * 0.045f, p);
            p.setColor(alpha(GOLD_2, 120));
            canvas.drawCircle(moonX + s * 0.052f, moonY + s * 0.038f, s * 0.030f, p);
            p.setColor(alpha(Color.WHITE, isLightTheme() ? 165 : 132));
            float[][] stars = {{.22f,.22f,.018f},{.38f,.15f,.012f},{.18f,.43f,.010f},{.68f,.40f,.013f},{.48f,.31f,.009f}};
            for (int i = 0; i < stars.length; i++) {
                float twinkle = 0.65f + 0.35f * (float)Math.sin(t * 2.0f + i * 1.7f);
                canvas.drawCircle(stars[i][0] * w, stars[i][1] * h, Math.max(1.2f, s * stars[i][2] * twinkle), p);
            }

            float mx = s * (0.48f + drift * 0.035f);
            float my = s * (0.73f - (0.05f + 0.035f * pulse));
            p.setTextAlign(Paint.Align.CENTER);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setTextSize(s * (compact ? 0.60f : 0.62f));
            p.setStyle(Paint.Style.FILL);
            p.setShadowLayer(s * 0.07f, 0, s * 0.025f, alpha(Color.BLACK, 170));
            for (int i = 5; i >= 1; i--) {
                p.setColor(alpha(mix(mColor, Color.BLACK, 0.42f), 88 + i * 18));
                canvas.drawText("M", mx + i * s * 0.012f, my + i * s * 0.014f, p);
            }
            p.setColor(mix(INFO, mColor, 0.35f));
            canvas.drawText("M", mx - s * 0.018f, my - s * 0.010f, p);
            p.setColor(mix(DANGER, INFO, 0.28f));
            canvas.drawText("M", mx + s * 0.010f, my + s * 0.006f, p);
            p.setColor(mix(mColor, moonColor, 0.36f));
            canvas.drawText("M", mx, my, p);
            p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(1.1f, s * 0.019f));
            p.setColor(alpha(Color.WHITE, isLightTheme() ? 170 : 132));
            canvas.drawText("M", mx, my, p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(1.0f, s * 0.012f));
            p.setColor(alpha(moonColor, 115));
            p.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(mx + s * 0.18f, my - s * 0.16f, moonX - s * 0.10f, moonY + s * 0.06f, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(moonColor, 190));
            canvas.drawCircle(mx + s * 0.20f, my - s * 0.17f, Math.max(1.2f, s * 0.022f), p);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(1.2f, s * 0.018f));
            p.setColor(alpha(mix(mColor, moonColor, 0.45f), 150));
            canvas.drawRoundRect(outer, s * 0.24f, s * 0.24f, p);
            if (moving) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) postInvalidateOnAnimation(); else postInvalidateDelayed(40);
            }
        }
    }

    private void buildFrame() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(NAVY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        LinearLayout headerWrap = new LinearLayout(this);
        headerWrap.setOrientation(LinearLayout.VERTICAL);
        headerWrap.setPadding(dp(8), dp(6), dp(8), dp(6));
        headerWrap.setBackground(gradient(new int[]{mix(HEADER_START, INFO, 0.10f), mix(HEADER_END, GOLD_2, 0.08f), HEADER_END}, GradientDrawable.Orientation.LEFT_RIGHT, 0));

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(2), 0, dp(2), 0);

        View logo = liveMeelanoLogo(true);
        header.addView(logo, new LinearLayout.LayoutParams(dp(50), dp(50)));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setGravity(Gravity.CENTER_VERTICAL);
        titles.setPadding(dp(10), 0, dp(8), 0);
        TextView appTitle = text("Meelano", 17.5f, TEXT, Typeface.BOLD);
        appTitle.setSingleLine(true);
        appTitle.setShadowLayer(dp(2), 0, dp(1), alpha(Color.WHITE, isLightTheme() ? 90 : 20));
        subtitle = text("ورود با حساب Meelano", 10.2f, alpha(TEXT, 205), Typeface.NORMAL);
        subtitle.setSingleLine(true);
        status = text("", 1, Color.TRANSPARENT, Typeface.NORMAL);
        titles.addView(appTitle, new LinearLayout.LayoutParams(-1, -2));
        titles.addView(subtitle, new LinearLayout.LayoutParams(-1, -2));
        header.addView(titles, new LinearLayout.LayoutParams(0, dp(50), 1f));

        connectionIndicator = null;

        LinearLayout tools = new LinearLayout(this);
        tools.setOrientation(LinearLayout.HORIZONTAL);
        tools.setGravity(Gravity.CENTER_VERTICAL);
        tools.setPadding(dp(2), 0, dp(2), 0);
        addHeaderTool(tools, "⌕", "جستجوی سراسری", INFO, v -> showGlobalSearchDialog());
        addHeaderTool(tools, "♛", "مدیریت دسترسی کاربران", mix(INFO, GOLD, 0.34f), v -> { if (session == null) showLogin("ابتدا وارد شوید."); else showApp("management"); });
        addHeaderTool(tools, "✺", "انتخاب تم", GOLD_2, v -> showThemeChooser());
        addHeaderTool(tools, "⚙", "تنظیمات", SUCCESS, v -> { if (session == null) showLogin("ابتدا وارد شوید."); else showApp("settings"); });
        addHeaderTool(tools, "⎋", "خروج", DANGER, v -> { if (session == null) showLogin("برای ورود، نام کاربری و رمز Meelano را وارد کنید."); else showLogin("از حساب خارج شدید. برای ورود مجدد اطلاعات Meelano را وارد کنید."); });
        header.addView(tools, new LinearLayout.LayoutParams(-2, dp(38)));
        headerWrap.addView(header, new LinearLayout.LayoutParams(-1, dp(54)));
        setConnectionStatus(session == null ? "idle" : "connected");

        root.addView(headerWrap, new LinearLayout.LayoutParams(-1, dp(64)));

        stage = new FrameLayout(this);
        stage.setBackgroundColor(NAVY);
        root.addView(stage, new LinearLayout.LayoutParams(-1, 0, 1f));
        setContentView(root);
    }

    private void addHeaderTool(LinearLayout parent, String glyph, String label, int accent, View.OnClickListener listener) {
        TextView b = new TextView(this);
        b.setText(glyph == null ? "" : glyph);
        b.setTextSize(glyph != null && glyph.length() > 1 ? 11.2f : 14.8f);
        b.setGravity(Gravity.CENTER);
        b.setSingleLine(true);
        b.setTextColor(onColorFor(accent));
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setPadding(0, 0, 0, dp(1));
        b.setShadowLayer(dp(3), 0, dp(1), alpha(Color.BLACK, 150));
        int baseAccent = mix(accent, GOLD_2, isLightTheme() ? 0.12f : 0.20f);
        GradientDrawable bg = gradient(new int[]{mix(baseAccent, Color.WHITE, isLightTheme() ? 0.34f : 0.18f), baseAccent, mix(baseAccent, HEADER_START, 0.40f)}, GradientDrawable.Orientation.TL_BR, 999);
        bg.setStroke(dp(1), alpha(mix(baseAccent, Color.WHITE, 0.45f), 155));
        b.setBackground(bg);
        b.setContentDescription(label);
        b.setClickable(true);
        b.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) b.setElevation(dp(7));
        applyTouchFeedback(b);
        b.setOnClickListener(listener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(31), dp(31));
        lp.setMargins(dp(2), 0, dp(2), 0);
        parent.addView(b, lp);
    }

    private LinearLayout.LayoutParams headerButtonLp(boolean margin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(36), dp(40));
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
        addThemeOption(box, dialog, "azure_diamond", "روشن ۱", "الماس آبی", new int[]{Color.rgb(239, 247, 255), Color.rgb(28, 101, 242), Color.rgb(98, 196, 255)});
        addThemeOption(box, dialog, "crystal_lagoon", "روشن ۲", "کریستالی", new int[]{Color.rgb(235, 248, 250), Color.rgb(0, 151, 178), Color.rgb(42, 125, 225)});
        addThemeOption(box, dialog, "ivory_sunrise", "روشن ۳", "عاجی", new int[]{Color.rgb(248, 241, 229), Color.rgb(213, 126, 55), Color.rgb(32, 158, 119)});
        addThemeOption(box, dialog, "onyx_gold", "دارک ۱", "اونیکس طلایی", new int[]{Color.rgb(7, 9, 16), Color.rgb(231, 177, 90), Color.rgb(102, 170, 245)});
        addThemeOption(box, dialog, "royal_amethyst", "دارک ۲", "آمتیست", new int[]{Color.rgb(10, 8, 24), Color.rgb(184, 114, 255), Color.rgb(248, 113, 193)});
        addThemeOption(box, dialog, "noir_aurora", "دارک ۳", "نوآر شفق", new int[]{Color.rgb(3, 5, 16), Color.rgb(0, 210, 210), Color.rgb(126, 87, 255)});
        styleMeelanoDialog(dialog, GOLD_2);
        dialog.show();
    }

    private void addThemeOption(LinearLayout parent, AlertDialog dialog, String id, String title, String subtitle, int[] palette) {
        boolean selected = id.equals(currentThemeId());
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(10), dp(9), dp(10), dp(9));
        card.setClickable(true);
        card.setBackground(gradient(new int[]{alpha(palette[1], selected ? 78 : 32), alpha(palette[2], 20), SURFACE}, GradientDrawable.Orientation.LEFT_RIGHT, 18));
        LinearLayout swatches = new LinearLayout(this);
        swatches.setOrientation(LinearLayout.HORIZONTAL);
        swatches.setGravity(Gravity.CENTER);
        for (int color : palette) {
            TextView dot = new TextView(this);
            dot.setText(" ");
            dot.setGravity(Gravity.CENTER);
            dot.setBackground(gradient(new int[]{mix(color, Color.WHITE, 0.26f), color, mix(color, Color.BLACK, 0.16f)}, GradientDrawable.Orientation.TL_BR, 999));
            LinearLayout.LayoutParams dpLp = new LinearLayout.LayoutParams(dp(24), dp(24));
            dpLp.setMargins(dp(2), 0, dp(2), 0);
            swatches.addView(dot, dpLp);
        }
        card.addView(swatches, new LinearLayout.LayoutParams(-2, dp(38)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(12), 0, dp(12), 0);
        copy.addView(text(title + (selected ? "  ✓" : ""), 15, selected ? palette[1] : TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(subtitle, 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        card.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        applyTouchFeedback(card);
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
        int accent = themeAccent(label);
        b.setTextColor(onColorFor(accent));
        b.setTextSize(compactUi() ? 12.4f : 13.3f);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setMinHeight(dp(44));
        b.setPadding(dp(10), 0, dp(10), dp(1));
        b.setShadowLayer(dp(2), 0, dp(1), alpha(Color.BLACK, isLightTheme() ? 80 : 150));
        b.setBackground(luxuryButtonBg(accent, true, 18));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { b.setElevation(dp(5)); b.setLetterSpacing(0.01f); }
        applyTouchFeedback(b);
        return b;
    }

    private Button secondaryButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        int accent = themeAccent(label);
        b.setTextColor(TEXT);
        b.setTextSize(compactUi() ? 12.0f : 12.8f);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setMinHeight(dp(42));
        b.setPadding(dp(10), 0, dp(10), dp(1));
        b.setBackground(luxuryButtonBg(accent, false, 17));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { b.setElevation(dp(2)); b.setLetterSpacing(0.005f); }
        applyTouchFeedback(b);
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
        int pad = compactUi() ? dp(11) : dp(15);
        c.setPadding(pad, pad, pad, pad);
        c.setBackground(premiumPanel(INFO, compactUi() ? 19 : 23));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) c.setElevation(dp(6));
        return c;
    }

    private class DiamondPatternView extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        DiamondPatternView(Context context) { super(context); }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth(), h = getHeight();
            if (w <= 0 || h <= 0) return;
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(1f, dp(0.7f)));
            p.setColor(alpha(GOLD_2, 30));
            int step = Math.max(dp(42), 42);
            for (int x = -w; x < w * 2; x += step) {
                canvas.drawLine(x, 0, x + h, h, p);
                canvas.drawLine(x + h, 0, x, h, p);
            }
            p.setColor(alpha(INFO, 24));
            p.setStrokeWidth(Math.max(1f, dp(1.1f)));
            for (int x = -w; x < w * 2; x += step * 3) {
                canvas.drawLine(x, 0, x + h, h, p);
            }
        }
    }


    private void showLogin(String message) {
        activePage = "login";
        session = null;
        setConnectionStatus("idle");
        subtitle.setText("ورود با حساب Meelano");
        stage.removeAllViews();

        FrameLayout backdrop = new FrameLayout(this);
        backdrop.setBackground(gradient(new int[]{HEADER_START, mix(NAVY, GOLD, 0.10f), mix(NAVY, INFO, 0.16f), NAVY}, GradientDrawable.Orientation.TL_BR, 0));
        backdrop.addView(new DiamondPatternView(this), new FrameLayout.LayoutParams(-1, -1));

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
        loginCard.setBackground(gradient(new int[]{alpha(Color.WHITE, 38), alpha(GOLD_2, 48), alpha(INFO, 28), alpha(SURFACE, 252)}, GradientDrawable.Orientation.TL_BR, 34));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) loginCard.setElevation(dp(12));
        outer.addView(loginCard, new LinearLayout.LayoutParams(-1, -2));

        ImageView logo = new ImageView(this);
        logo.setImageResource(ir.meelano.android.R.drawable.meelano_3d);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        logo.setPadding(dp(5), dp(5), dp(5), dp(5));
        logo.setBackground(roundedStroke(alpha(GOLD, 18), 28, alpha(GOLD, 72)));
        LinearLayout.LayoutParams logoLp = new LinearLayout.LayoutParams(dp(112), dp(112));
        logoLp.setMargins(0, 0, 0, dp(8));
        loginCard.addView(logo, logoLp);

        TextView h = text("Meelano Diamond Login", 23, TEXT, Typeface.BOLD);
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

        Button login = primaryButton("اتصال و ورود ✦");
        loginCard.addView(login, new LinearLayout.LayoutParams(-1, dp(54)));

        TextView note = text("در صورت عدم اتصال، فقط پیام خطای اتصال و گزینه تلاش مجدد نمایش داده می‌شود.", 10.5f, MUTED, Typeface.NORMAL);
        note.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams noteLp = new LinearLayout.LayoutParams(-1, -2);
        noteLp.setMargins(0, dp(14), 0, 0);
        loginCard.addView(note, noteLp);
        addQuickLoginPanel(loginCard);

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
                        clearUserScopedCaches();
                        prefs.edit().putString(KEY_LAST_USER, u).apply();
                        storeQuickSession(s);
                        login.setEnabled(true);
                        login.setText("اتصال و ورود ✦");
                        setConnectionStatus("connected");
                        Toast.makeText(this, "اتصال موفق بود", Toast.LENGTH_SHORT).show();
                        showApp("dashboard");
                        maybePromptQuickPinSetup();
                    });
                } catch (Exception ex) {
                    runOnUiThread(() -> {
                        login.setEnabled(true);
                        login.setText("اتصال و ورود ✦");
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

    private void addQuickLoginPanel(LinearLayout parent) {
        if (parent == null || prefs == null || !prefs.getBoolean(KEY_QUICK_LOGIN_ENABLED, false)) return;
        LinearLayout q = new LinearLayout(this);
        q.setOrientation(LinearLayout.VERTICAL);
        q.setPadding(dp(12), dp(11), dp(12), dp(12));
        q.setBackground(roundedStroke(alpha(INFO, 18), 18, alpha(INFO, 72)));
        TextView title = text("ورود سریع امن", 13.5f, TEXT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        q.addView(title, new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text("با PIN یا اثر انگشت، بدون نمایش جزئیات اتصال وارد شوید.", 10.4f, MUTED, Typeface.NORMAL);
        sub.setGravity(Gravity.CENTER);
        q.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button pin = primaryButton("PIN");
        Button bio = secondaryButton("اثر انگشت");
        pin.setOnClickListener(v -> showQuickPinDialog());
        bio.setOnClickListener(v -> startBiometricQuickLogin());
        row.addView(pin, weightedButtonLp());
        row.addView(bio, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); q.addView(row, rp);
        LinearLayout.LayoutParams qp = new LinearLayout.LayoutParams(-1, -2); qp.setMargins(0, dp(12), 0, 0); parent.addView(q, qp);
    }

    private void storeQuickSession(UserSession s) {
        if (prefs == null || s == null) return;
        SharedPreferences.Editor e = prefs.edit();
        e.putString(KEY_QUICK_USER_NAME, s.userName == null ? "" : s.userName);
        e.putString(KEY_QUICK_ROLE, s.accessRole == null ? "" : s.accessRole);
        e.putString(KEY_QUICK_PERMISSIONS, s.permissions == null ? "" : s.permissions);
        if (s.userId == null) e.remove(KEY_QUICK_USER_ID); else e.putInt(KEY_QUICK_USER_ID, s.userId);
        if (s.visitorId == null) e.remove(KEY_QUICK_VISITOR_ID); else e.putInt(KEY_QUICK_VISITOR_ID, s.visitorId);
        e.apply();
    }

    private UserSession storedQuickSession() {
        if (prefs == null) return null;
        String name = prefs.getString(KEY_QUICK_USER_NAME, "");
        if (name == null || name.trim().isEmpty()) return null;
        Integer uid = prefs.contains(KEY_QUICK_USER_ID) ? prefs.getInt(KEY_QUICK_USER_ID, 0) : null;
        Integer vid = prefs.contains(KEY_QUICK_VISITOR_ID) ? prefs.getInt(KEY_QUICK_VISITOR_ID, 0) : null;
        String role = prefs.getString(KEY_QUICK_ROLE, "");
        String permissions = prefs.getString(KEY_QUICK_PERMISSIONS, "");
        return new UserSession(uid, vid, name, role, permissions);
    }

    private void maybePromptQuickPinSetup() {
        if (prefs == null || prefs.getBoolean(KEY_QUICK_LOGIN_ENABLED, false) || !prefs.getString(KEY_QUICK_PIN, "").isEmpty()) return;
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18), dp(18), dp(18), dp(14));
        box.setBackground(gradient(new int[]{alpha(INFO, 34), alpha(GOLD_2, 26), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 30));
        TextView icon = report3dIcon("◉", INFO);
        icon.setText("⌾");
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(dp(70), dp(70));
        ip.gravity = Gravity.CENTER_HORIZONTAL;
        box.addView(icon, ip);
        TextView title = text("قفل سریع و امن Meelano", 18, TEXT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2); tp.setMargins(0, dp(8), 0, dp(4));
        box.addView(title, tp);
        TextView body = text("برای ورودهای بعدی، یک PIN کوتاه تنظیم کن؛ اگر گوشی اثر انگشت داشته باشد، همین جلسه ذخیره‌شده با تأیید اثر انگشت هم قابل ورود است.", 11.1f, MUTED, Typeface.NORMAL);
        body.setGravity(Gravity.CENTER);
        body.setLineSpacing(dp(2), 1.06f);
        box.addView(body, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout benefits = new LinearLayout(this);
        benefits.setOrientation(LinearLayout.HORIZONTAL);
        addSecurityChip(benefits, "رمزگذاری", "◆", GOLD);
        addSecurityChip(benefits, "PIN", "••", INFO);
        addSecurityChip(benefits, "اثر انگشت", "⌾", SUCCESS);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); bp.setMargins(0, dp(12), 0, dp(12));
        box.addView(benefits, bp);
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        Button later = secondaryButton("بعداً");
        Button setup = primaryButton("تنظیم قفل سریع");
        actions.addView(later, weightedButtonLp());
        actions.addView(setup, weightedButtonLp());
        box.addView(actions, new LinearLayout.LayoutParams(-1, -2));
        AlertDialog dialog = new AlertDialog.Builder(this).setView(box).create();
        later.setOnClickListener(v -> dialog.dismiss());
        setup.setOnClickListener(v -> { dialog.dismiss(); showSetQuickPinDialog(); });
        dialog.setOnShowListener(d -> { if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(roundedStroke(alpha(SURFACE, 0), 30, alpha(INFO, 0))); });
        dialog.show();
    }

    private void addSecurityChip(LinearLayout parent, String label, String glyph, int accent) {
        LinearLayout chip = new LinearLayout(this);
        chip.setOrientation(LinearLayout.VERTICAL);
        chip.setGravity(Gravity.CENTER);
        chip.setPadding(dp(4), dp(7), dp(4), dp(7));
        chip.setBackground(gradient(new int[]{alpha(accent, 42), alpha(SURFACE_2, 220)}, GradientDrawable.Orientation.TL_BR, 18));
        TextView g = text(glyph, 15, accent, Typeface.BOLD);
        g.setGravity(Gravity.CENTER);
        TextView l = text(label, 8.6f, TEXT, Typeface.BOLD);
        l.setGravity(Gravity.CENTER);
        l.setSingleLine(true);
        chip.addView(g, new LinearLayout.LayoutParams(-1, -2));
        chip.addView(l, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(56), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        parent.addView(chip, lp);
    }

    private void showSetQuickPinDialog() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18), dp(16), dp(18), dp(12));
        box.setBackground(gradient(new int[]{alpha(SUCCESS, 30), alpha(INFO, 20), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 28));
        TextView title = text("PIN امن Meelano", 17, TEXT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        box.addView(title, new LinearLayout.LayoutParams(-1, -2));
        TextView hint = text("۴ تا ۶ رقم انتخاب کن؛ اطلاعات اتصال همچنان مخفی می‌ماند.", 10.8f, MUTED, Typeface.NORMAL);
        hint.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2); hp.setMargins(0, dp(4), 0, dp(12));
        box.addView(hint, hp);
        EditText pin = input("PIN چهار تا شش رقمی", "", true);
        pin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        box.addView(pin, new LinearLayout.LayoutParams(-1, dp(54)));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(box)
                .setNegativeButton("بستن", null)
                .setPositiveButton("ذخیره", null)
                .create();
        dialog.setOnShowListener(d -> {
            if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(roundedStroke(alpha(SURFACE, 245), 28, alpha(SUCCESS, 90)));
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positive != null) {
                positive.setTextColor(SUCCESS);
                positive.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                positive.setOnClickListener(v -> {
                    String value = pin.getText().toString().trim();
                    if (value.length() < 4 || value.length() > 6) { Toast.makeText(this, "PIN باید ۴ تا ۶ رقم باشد.", Toast.LENGTH_SHORT).show(); return; }
                    prefs.edit().putString(KEY_QUICK_PIN, protectSecret(value)).putBoolean(KEY_QUICK_LOGIN_ENABLED, true).apply();
                    Toast.makeText(this, "ورود سریع فعال شد.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }
        });
        dialog.show();
    }

    private void showQuickPinDialog() {
        EditText pin = input("PIN", "", true);
        pin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        new AlertDialog.Builder(this)
                .setTitle("ورود سریع با PIN")
                .setView(pin)
                .setNegativeButton("بستن", null)
                .setPositiveButton("ورود", (d, w) -> {
                    String saved = unprotectSecret(prefs.getString(KEY_QUICK_PIN, ""));
                    if (!saved.equals(pin.getText().toString().trim())) { Toast.makeText(this, "PIN درست نیست.", Toast.LENGTH_SHORT).show(); return; }
                    restoreQuickLogin();
                })
                .show();
    }

    private void startBiometricQuickLogin() {
        if (Build.VERSION.SDK_INT < 28) { Toast.makeText(this, "اثر انگشت روی این نسخه اندروید پشتیبانی نمی‌شود؛ از PIN استفاده کنید.", Toast.LENGTH_SHORT).show(); return; }
        if (storedQuickSession() == null) { Toast.makeText(this, "ابتدا یک‌بار با حساب Meelano وارد شوید.", Toast.LENGTH_SHORT).show(); return; }
        try {
            CancellationSignal signal = new CancellationSignal();
            BiometricPrompt prompt = new BiometricPrompt.Builder(this)
                    .setTitle("ورود سریع Meelano")
                    .setSubtitle("تأیید هویت برای ورود به داشبورد")
                    .setNegativeButton("لغو", getMainExecutor(), (d, which) -> {})
                    .build();
            prompt.authenticate(signal, getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
                @Override public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) { restoreQuickLogin(); }
                @Override public void onAuthenticationError(int errorCode, CharSequence errString) { Toast.makeText(MainActivity.this, "ورود سریع لغو شد.", Toast.LENGTH_SHORT).show(); }
            });
        } catch (Exception ex) { Toast.makeText(this, "اثر انگشت در دسترس نیست؛ از PIN استفاده کنید.", Toast.LENGTH_SHORT).show(); }
    }

    private void restoreQuickLogin() {
        UserSession saved = storedQuickSession();
        if (saved == null) { Toast.makeText(this, "جلسه ذخیره‌شده پیدا نشد.", Toast.LENGTH_SHORT).show(); return; }
        setConnectionStatus("loading");
        executor.execute(() -> {
            try (Connection c = openConnection()) {
                UserSession fresh = withResolvedAccessRole(c, saved, saved.userName);
                runOnUiThread(() -> {
                    session = fresh;
                    clearUserScopedCaches();
                    storeQuickSession(fresh);
                    setConnectionStatus("connected");
                    Toast.makeText(this, "ورود سریع انجام شد و مجوزها بروزرسانی شد.", Toast.LENGTH_SHORT).show();
                    showApp("dashboard");
                });
            } catch (Exception ex) {
                runOnUiThread(() -> {
                    setConnectionStatus("offline");
                    showLoginError(readableError(ex), () -> restoreQuickLogin());
                });
            }
        });
    }

    private void showLoginError(String message, Runnable retry) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18), dp(14), dp(18), dp(8));
        TextView icon = report3dIcon("!", DANGER);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(dp(58), dp(58));
        ip.gravity = Gravity.CENTER_HORIZONTAL;
        box.addView(icon, ip);
        TextView title = text("ورود انجام نشد", 17, TEXT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        box.addView(title, new LinearLayout.LayoutParams(-1, -2));
        TextView body = text((message == null || message.trim().isEmpty() ? "ارتباط برقرار نشد. اینترنت یا دسترسی سرور را بررسی کنید." : message) + "\n\nدوباره تلاش کنید؛ جزئیات فنی اتصال نمایش داده نمی‌شود.", 12, MUTED, Typeface.NORMAL);
        body.setGravity(Gravity.CENTER);
        body.setLineSpacing(dp(3), 1.05f);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2);
        bp.setMargins(0, dp(8), 0, 0);
        box.addView(body, bp);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(box)
                .setNegativeButton("بستن", null)
                .setPositiveButton("تلاش مجدد", (d, w) -> retry.run())
                .create();
        dialog.setOnShowListener(d -> {
            if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(roundedStroke(alpha(SURFACE, 248), 28, alpha(DANGER, 75)));
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positive != null) positive.setTextColor(GOLD);
        });
        dialog.show();
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

        navStrip = new LinearLayout(this);
        navStrip.setOrientation(LinearLayout.VERTICAL);
        navStrip.setGravity(Gravity.CENTER);
        navStrip.setPadding(dp(8), dp(7), dp(8), dp(7));
        navStrip.setBackground(gradient(new int[]{alpha(HEADER_START, 238), alpha(SURFACE_2, 210)}, GradientDrawable.Orientation.LEFT_RIGHT, 0));
        shell.addView(navStrip, new LinearLayout.LayoutParams(-1, dp(262)));

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
        navStrip.setOrientation(LinearLayout.VERTICAL);
        LinearLayout row1 = navRow();
        LinearLayout row2 = navRow();
        LinearLayout row3 = navRow();
        LinearLayout row4 = navRow();
        LinearLayout row5 = navRow();
        navStrip.addView(row1, new LinearLayout.LayoutParams(-1, 0, 1f));
        navStrip.addView(row2, new LinearLayout.LayoutParams(-1, 0, 1f));
        navStrip.addView(row3, new LinearLayout.LayoutParams(-1, 0, 1f));
        navStrip.addView(row4, new LinearLayout.LayoutParams(-1, 0, 1f));
        navStrip.addView(row5, new LinearLayout.LayoutParams(-1, 0, 1f));
        addNav(row1, "dashboard", "داشبورد", navGlyph("dashboard"));
        addNav(row1, "customers", "مشتریان", navGlyph("customers"));
        addNav(row1, "products", "کالا", navGlyph("products"));
        addNav(row2, "reports", "گزارشات", navGlyph("reports"));
        addNav(row2, "command", "فرماندهی", navGlyph("command"));
        addNav(row2, "assistant", "دستیار", navGlyph("assistant"));
        addNav(row3, "chat", "گفتگو", navGlyph("chat"));
        addNav(row3, "personnel", "پرسنل", navGlyph("personnel"));
        addNav(row3, "attendance", "حضور", navGlyph("attendance"));
        addNav(row4, "taxpayers", "مودیان", navGlyph("taxpayers"));
        addNav(row4, "cameras", "دوربین", navGlyph("cameras"));
        addNav(row4, "alarm", "دزدگیر", navGlyph("alarm"));
        addNav(row5, "visitor_dashboard", "داشبورد ویزیتور", navGlyph("visitor_dashboard"));
        addNav(row5, "showcase", "ویترین", navGlyph("showcase"));
        addNav(row5, "cart", "سبد خرید", navGlyph("cart"));
    }

    private LinearLayout navRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        return row;
    }

    private void addNav(LinearLayout parent, String key, String label, String icon) {
        boolean active = key.equals(activePage);
        boolean locked = !canOpenPage(key);
        int accent = navAccent(key);
        LinearLayout tab = new LinearLayout(this);
        tab.setOrientation(LinearLayout.HORIZONTAL);
        tab.setGravity(Gravity.CENTER);
        tab.setPadding(dp(5), dp(4), dp(5), dp(4));
        tab.setClickable(true);
        tab.setFocusable(true);
        tab.setAlpha(locked && !active ? 0.64f : 1f);
        tab.setBackground(active ? luxuryButtonBg(accent, true, 19) : luxuryButtonBg(locked ? MUTED : accent, false, 19));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tab.setElevation(dp(active ? 7 : 2));
        applyTouchFeedback(tab);

        String visibleIcon = locked ? "🔒" : icon;
        TextView badge = text(visibleIcon, visibleIcon != null && visibleIcon.length() > 1 ? 12.5f : 16, active ? onColorFor(accent) : (locked ? alpha(MUTED, 190) : accent), Typeface.BOLD);
        badge.setGravity(Gravity.CENTER);
        badge.setSingleLine(true);
        badge.setShadowLayer(dp(active ? 3 : 1), 0, dp(1), alpha(Color.BLACK, active ? 145 : 55));
        badge.setBackground(roundedStroke(active ? alpha(Color.WHITE, isLightTheme() ? 58 : 38) : alpha(locked ? MUTED : accent, 18), 13, active ? alpha(Color.WHITE, 105) : alpha(locked ? MUTED : accent, 70)));
        tab.addView(badge, new LinearLayout.LayoutParams(dp(30), dp(30)));

        TextView title = text(label, 10.2f, active ? onColorFor(accent) : (locked ? alpha(MUTED, 210) : TEXT), Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setSingleLine(true);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(0, -2, 1f); tp.setMargins(dp(4), 0, dp(4), 0);
        tab.addView(title, tp);
        tab.setOnClickListener(v -> showApp(key));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(43), 1f);
        lp.setMargins(dp(3), dp(3), dp(3), dp(3));
        parent.addView(tab, lp);
    }

    private int navAccent(String key) {
        if ("command".equals(key)) return INFO;
        if ("assistant".equals(key)) return mix(GOLD_2, INFO, 0.45f);
        if ("customers".equals(key)) return SUCCESS;
        if ("products".equals(key)) return WARNING;
        if ("reports".equals(key)) return INFO;
        if ("chat".equals(key)) return mix(GOLD_2, INFO, 0.62f);
        if ("personnel".equals(key)) return mix(SUCCESS, INFO, 0.35f);
        if ("attendance".equals(key)) return mix(GOLD, SUCCESS, 0.30f);
        if ("visitor_dashboard".equals(key)) return mix(SUCCESS, GOLD, 0.26f);
        if ("showcase".equals(key)) return mix(WARNING, INFO, 0.24f);
        if ("cart".equals(key)) return mix(GOLD, SUCCESS, 0.46f);
        if ("taxpayers".equals(key)) return mix(GOLD, INFO, 0.28f);
        if ("cameras".equals(key)) return mix(INFO, SUCCESS, 0.30f);
        if ("alarm".equals(key)) return mix(DANGER, GOLD, 0.30f);
        return GOLD;
    }

    private boolean getRtlMode() { return prefs == null || prefs.getBoolean("rtl_mode", true); }

    private void renderActivePage() {
        if (!canOpenPage(activePage)) { renderLockedSection(activePage); return; }
        switch (activePage) {
            case "command": loadCommandCenter(); break;
            case "assistant": showAssistant(); break;
            case "chat": loadChatRoom(); break;
            case "personnel": loadPersonnel(); break;
            case "attendance": loadAttendance(); break;
            case "visitor_dashboard": loadVisitorDashboard(); break;
            case "showcase": loadShowcase("", "all"); break;
            case "cart": renderCartPage(); break;
            case "taxpayers": loadTaxpayers(); break;
            case "cameras": renderCamerasPage(); break;
            case "alarm": renderAlarmPage(); break;
            case "customers": loadCustomers(customersCacheQuery == null ? "" : customersCacheQuery, customersCacheFilter == null ? "all" : customersCacheFilter); break;
            case "products": loadProducts(productsCacheQuery == null ? "" : productsCacheQuery, productsCacheFilter == null ? "all" : productsCacheFilter); break;
            case "sales": loadTable("فروش و اسناد", "نمای مستقیم از جدول فروش", "sailfact", ""); break;
            case "checks": loadTable("چک‌ها و وصول", "نمای مستقیم از چک‌های دریافتی", "getchk", ""); break;
            case "reports": loadReports(); break;
            case "settings": renderSettings(); break;
            case "management": loadAccessManagement(); break;
            case "health": renderConnectionHealthPage(); break;
            case "dashboard":
            default: loadDashboard(); break;
        }
    }

    private void refreshActivePage() {
        if ("login".equals(activePage)) showLogin("برای اتصال مجدد، اطلاعات Meelano را وارد کنید.");
        else showApp(activePage);
    }

    private String refreshKey(String page) { return "last_manual_refresh_" + (page == null ? "page" : page); }

    private void markRefresh(String page) {
        if (prefs != null) prefs.edit().putString(refreshKey(page), nowText()).apply();
    }

    private String lastRefreshText(String page) {
        return prefs == null ? "ثبت نشده" : prefs.getString(refreshKey(page), "ثبت نشده");
    }

    private void addManualRefreshPanel(String page, String title, String hint, Runnable refresh) {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.HORIZONTAL);
        c.setGravity(Gravity.CENTER_VERTICAL);
        c.setPadding(dp(10), dp(8), dp(10), dp(8));
        int accent = navAccent(page);
        c.setBackground(premiumPanel(accent, 22));
        c.addView(report3dIcon("⟳", accent), new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(8), 0, dp(8), 0);
        copy.addView(text(title == null ? "بروزرسانی دستی" : title, 12.7f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text("آخرین بروزرسانی: " + lastRefreshText(page) + (hint == null || hint.isEmpty() ? "" : " • " + hint), 9.8f, MUTED, Typeface.NORMAL);
        sub.setSingleLine(false); sub.setMaxLines(2);
        copy.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        c.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView btn = new TextView(this);
        btn.setText(withIcon("⟳", "تازه‌سازی"));
        btn.setTextSize(10.2f);
        btn.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        btn.setTextColor(Color.WHITE);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(dp(6), 0, dp(6), 0);
        btn.setShadowLayer(dp(2), 0, dp(1), alpha(Color.BLACK, 120));
        GradientDrawable bg = gradient(new int[]{mix(accent, Color.WHITE, 0.22f), accent, mix(accent, Color.BLACK, 0.24f)}, GradientDrawable.Orientation.TL_BR, 999);
        bg.setStroke(dp(1), alpha(Color.WHITE, 120));
        btn.setBackground(bg);
        btn.setClickable(true);
        btn.setOnClickListener(v -> { if (refresh != null) refresh.run(); });
        applyTouchFeedback(btn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) btn.setElevation(dp(5));
        c.addView(btn, new LinearLayout.LayoutParams(dp(108), dp(40)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void loadCommandCenter() { loadCommandCenter(false); }

    private void loadCommandCenter(boolean force) {
        if (!force && dashboardCacheJson != null && !dashboardCacheJson.trim().isEmpty()) {
            try { renderCommandCenter(new JSONObject(dashboardCacheJson).optJSONObject("today"), false); return; } catch (Exception ignored) { }
        }
        content.removeAllViews();
        addHero("فرماندهی هوشمند Meelano", "پیش‌بینی نقدینگی، رادار کالا، تقویم مدیریتی، ریسک مشتری و خروجی عملیاتی در یک صفحه.");
        addManualRefreshPanel("command", "بروزرسانی دستی فرماندهی", "از داده داشبورد استفاده می‌کند", () -> loadCommandCenter(true));
        addLoading(content, "میلو در حال ساخت اتاق فرمان است…");
        runDb(this::queryDashboard, new DbCallback() {
            @Override public void ok(String body) {
                try {
                    dashboardCacheJson = body;
                    markRefresh("dashboard"); markRefresh("command");
                    if (prefs != null) prefs.edit().putString(KEY_CACHE_DASHBOARD, body).apply();
                    renderCommandCenter(new JSONObject(body).optJSONObject("today"), false);
                } catch (Exception e) { showPageError("فرماندهی", e, () -> loadCommandCenter(true)); }
            }
            @Override public void fail(Exception e) {
                try {
                    String cached = dashboardCacheJson != null && !dashboardCacheJson.trim().isEmpty() ? dashboardCacheJson : (prefs == null ? "" : prefs.getString(KEY_CACHE_DASHBOARD, ""));
                    if (cached == null || cached.trim().isEmpty()) { showPageError("فرماندهی", e, () -> loadCommandCenter(true)); return; }
                    dashboardCacheJson = cached;
                    renderCommandCenter(new JSONObject(cached).optJSONObject("today"), true);
                    addCacheBanner("فرماندهی آفلاین", "آخرین داده ذخیره‌شده نمایش داده شد. خطا: " + shortError(e));
                } catch (Exception ex) { showPageError("فرماندهی", e, () -> loadCommandCenter(true)); }
            }
        });
    }

    private void renderCommandCenter(JSONObject today, boolean cached) {
        content.removeAllViews();
        addHero("فرماندهی هوشمند Meelano", cached ? "نمای آفلاین از آخرین داده ذخیره‌شده" : "اتاق تصمیم سریع برای امروز و هفته پیش‌رو");
        addManualRefreshPanel("command", "بروزرسانی دستی فرماندهی", "اطلاعات ثابت است تا خودتان تازه‌سازی کنید", () -> loadCommandCenter(true));
        addCashForecastCard(today);
        addManagementCalendarCard(today);
        addProductRadarCard(today);
        addSmartComparisonCard(today);
        addCommandShortcutCard(today);
    }

    private void addCashForecastCard(JSONObject today) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{Color.rgb(21, 91, 181), Color.rgb(0, 184, 217), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 28));
        c.addView(text("پیش‌بینی نقدینگی ۷ روزه", 17, Color.WHITE, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        JSONObject sales = today == null ? null : today.optJSONObject("sales");
        JSONObject get = today == null ? null : today.optJSONObject("getChecks");
        JSONObject put = today == null ? null : today.optJSONObject("putChecks");
        double salesTotal = metricMoneyValue(sales == null ? null : sales.optJSONArray("metrics"), "جمع فروش");
        double inChecks = metricMoneyValue(get == null ? null : get.optJSONArray("metrics"), "جمع مبلغ");
        double outChecks = metricMoneyValue(put == null ? null : put.optJSONArray("metrics"), "جمع مبلغ");
        double debtor = valueOf(firstObject(today == null ? null : today.optJSONArray("topDebtors")));
        double pressure = outChecks - inChecks - Math.max(0, salesTotal * 0.25);
        int accent = pressure > 0 ? DANGER : SUCCESS;
        addActionItem(c, pressure > 0 ? "فشار" : "آرام", pressure > 0 ? "چک‌های پرداختی/تعهدات از ورودی جلوتر است؛ امروز وصول بدهکار اولویت اول است." : "ورودی‌های فروش/چک فشار شدید نشان نمی‌دهد؛ با این حال وصول را رها نکن.", accent);
        addActionItem(c, "ورودی", "فروش/چک دریافتی قابل اتکا: " + money(salesTotal + inChecks), SUCCESS);
        addActionItem(c, "خروجی", "چک/تعهد پرداختی قابل پایش: " + money(outChecks), WARNING);
        addActionItem(c, "وصول", "بزرگ‌ترین فرصت وصول: " + labelOf(firstObject(today == null ? null : today.optJSONArray("topDebtors")), "party", "—") + " • " + money(debtor), DANGER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addManagementCalendarCard(JSONObject today) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(Color.rgb(126, 87, 255), 46), alpha(GOLD, 22), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 26));
        c.addView(text("تقویم مدیریتی امروز", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        JSONObject overdue = firstObject(today == null ? null : today.optJSONArray("overdueInvoices"));
        JSONObject inactive = firstObject(today == null ? null : today.optJSONArray("inactiveCustomers"));
        JSONObject debtor = firstObject(today == null ? null : today.optJSONArray("topDebtors"));
        addActionItem(c, "۹:۰۰", "مرور سلامت اتصال و گزارش روزانه میلو", INFO);
        addActionItem(c, "۱۰:۳۰", "تماس وصول: " + labelOf(debtor, "party", "بدهکار مهم") + " • " + moneyValue(debtor, "amount"), DANGER);
        addActionItem(c, "۱۲:۰۰", "پیگیری فاکتور معوق: " + labelOf(overdue, "party", "موردی ثبت نشده"), WARNING);
        addActionItem(c, "۱۶:۰۰", "بازفعال‌سازی مشتری خاموش: " + labelOf(inactive, "party", "مشتری پیشنهادی ندارد"), SUCCESS);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addProductRadarCard(JSONObject today) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(WARNING, 34), alpha(Color.rgb(236, 72, 153), 22), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 26));
        c.addView(text("رادار کالاهای خطرناک و طلایی", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        JSONArray items = today == null ? null : today.optJSONArray("todayItems");
        if (items == null || items.length() == 0) {
            addActionItem(c, "داده", "برای تحلیل کالا، فروش روز باید دارای اقلام ثبت‌شده باشد.", MUTED);
        } else {
            for (int i = 0; i < Math.min(4, items.length()); i++) {
                JSONObject it = items.optJSONObject(i);
                addActionItem(c, i == 0 ? "طلایی" : "رصد", labelOf(it, "item", "کالا") + " • " + moneyValue(it, "amount") + " • " + it.optString("hint", ""), i == 0 ? GOLD : WARNING);
            }
            addActionItem(c, "تصمیم", "برای کالای پرفروش موجودی را چک کن؛ برای کالاهای کم‌گردش، تخفیف کور نده، بسته پیشنهادی بساز.", INFO);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addSmartComparisonCard(JSONObject today) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 28), alpha(INFO, 24), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 26));
        c.addView(text("مقایسه امروز با روزهای اخیر", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        JSONObject sales = today == null ? null : today.optJSONObject("sales");
        JSONObject buy = today == null ? null : today.optJSONObject("purchases");
        addActionItem(c, "فروش", trendDeltaText(sales == null ? null : sales.optJSONArray("chart")), trendDeltaAccent(sales == null ? null : sales.optJSONArray("chart")));
        addActionItem(c, "خرید", trendDeltaText(buy == null ? null : buy.optJSONArray("chart")), trendDeltaAccent(buy == null ? null : buy.optJSONArray("chart")));
        addActionItem(c, "میلو", "اگر فروش رشد کرده اما وصول نه، خوشحالی‌ات را قسطی کن؛ نقدینگی رئیس واقعی است.", GOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private String trendDeltaText(JSONArray arr) {
        if (arr == null || arr.length() < 2) return "برای مقایسه، حداقل دو روز داده لازم است.";
        double prev = valueOf(arr.optJSONObject(arr.length() - 2));
        double last = valueOf(arr.optJSONObject(arr.length() - 1));
        double diff = last - prev;
        double pct = prev == 0 ? 0 : (diff / Math.abs(prev)) * 100.0;
        return (diff >= 0 ? "رشد " : "افت ") + money(Math.abs(diff)) + (prev == 0 ? "" : " • " + formatNumber(pct) + "٪") + " نسبت به نقطه قبل";
    }

    private int trendDeltaAccent(JSONArray arr) {
        if (arr == null || arr.length() < 2) return MUTED;
        return valueOf(arr.optJSONObject(arr.length() - 1)) >= valueOf(arr.optJSONObject(arr.length() - 2)) ? SUCCESS : DANGER;
    }

    private void addCommandShortcutCard(JSONObject today) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(Color.rgb(255, 137, 66), 34), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("میانبرهای مدیریتی", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        Button voice = primaryButton("میلو گزارش را بخوان"); voice.setTextSize(10.2f); voice.setOnClickListener(v -> speakAssistantText(buildDailyVoiceSummary(today)));
        Button customers = secondaryButton("مشتریان پرریسک"); customers.setTextSize(10.2f); customers.setOnClickListener(v -> loadCustomers("", "debt"));
        row1.addView(voice, weightedButtonLp()); row1.addView(customers, weightedButtonLp());
        LinearLayout.LayoutParams r1 = new LinearLayout.LayoutParams(-1, -2); r1.setMargins(0, dp(10), 0, dp(8)); c.addView(row1, r1);
        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        Button csv = secondaryButton("CSV خلاصه"); csv.setTextSize(10.2f); csv.setOnClickListener(v -> exportTodayCsv(today));
        Button health = secondaryButton("سلامت اتصال"); health.setTextSize(10.2f); health.setOnClickListener(v -> showApp("health"));
        row2.addView(csv, weightedButtonLp()); row2.addView(health, weightedButtonLp());
        c.addView(row2, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void exportTodayCsv(JSONObject today) {
        try {
            File dir = getExternalFilesDir(null); if (dir == null) dir = getFilesDir();
            File file = new File(dir, "Meelano-Today-Command-v3.37.csv");
            StringBuilder b = new StringBuilder("section,label,value\n");
            appendCsvMetricRows(b, "sales", today == null ? null : today.optJSONObject("sales"));
            appendCsvMetricRows(b, "purchases", today == null ? null : today.optJSONObject("purchases"));
            appendCsvMetricRows(b, "get_checks", today == null ? null : today.optJSONObject("getChecks"));
            appendCsvMetricRows(b, "put_checks", today == null ? null : today.optJSONObject("putChecks"));
            try (FileOutputStream fos = new FileOutputStream(file)) { fos.write(b.toString().getBytes(StandardCharsets.UTF_8)); }
            sharePlainText("CSV خلاصه Meelano", "خروجی CSV ساخته شد:\n" + file.getAbsolutePath() + "\n\n" + b.toString(), null);
        } catch (Exception ex) { Toast.makeText(this, "ساخت CSV ممکن نشد: " + shortError(ex), Toast.LENGTH_SHORT).show(); }
    }

    private void appendCsvMetricRows(StringBuilder b, String section, JSONObject block) {
        JSONArray m = block == null ? null : block.optJSONArray("metrics");
        if (m == null) return;
        for (int i = 0; i < m.length(); i++) {
            JSONObject o = m.optJSONObject(i);
            if (o == null) continue;
            b.append(section).append(',').append(csvSafe(o.optString("label", ""))).append(',').append(csvSafe(o.optString("value", ""))).append('\n');
        }
    }

    private String csvSafe(String v) {
        String s = v == null ? "" : v.replace("\"", "\"\"");
        return "\"" + s + "\"";
    }

    private void showGlobalSearchDialog() {
        if (session == null) { showLogin("برای جستجوی سراسری ابتدا وارد شوید."); return; }
        EditText q = input("نام مشتری، شماره چک، کد کالا یا فاکتور…", "", false);
        q.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) q.setTextDirection(View.TEXT_DIRECTION_RTL);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("جستجوی هوشمند سراسری")
                .setView(q)
                .setNegativeButton("بستن", null)
                .setPositiveButton("جستجو", (d, w) -> showGlobalSearchResults(q.getText().toString().trim()))
                .create();
        styleMeelanoDialog(dialog, INFO);
        dialog.show();
    }

    private void showGlobalSearchResults(String query) {
        if (query == null || query.trim().isEmpty()) { Toast.makeText(this, "عبارت جستجو را بنویسید.", Toast.LENGTH_SHORT).show(); return; }
        activePage = "search";
        if (content == null) showApp("dashboard");
        content.removeAllViews();
        addHero("جستجوی سراسری", "نتایج برای: " + query);
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال جستجو در مشتری، کالا، چک و فاکتور…");
        runDb(() -> queryGlobalSearch(query), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    JSONArray rows = new JSONArray(body);
                    list.removeAllViews();
                    if (rows.length() == 0) { addEmptyTo(list, "نتیجه‌ای برای این جستجو پیدا نشد."); return; }
                    for (int i = 0; i < rows.length(); i++) addGlobalSearchResultCard(list, rows.optJSONObject(i));
                } catch (Exception e) { showPageError("جستجو", e, () -> showGlobalSearchResults(query)); }
            }
            @Override public void fail(Exception e) { showPageError("جستجو", e, () -> showGlobalSearchResults(query)); }
        });
    }

    private String queryGlobalSearch(String raw) throws Exception {
        JSONArray out = new JSONArray();
        String q = raw == null ? "" : raw.trim();
        if (q.isEmpty()) return out.toString();
        try (Connection c = openConnection()) {
            appendGlobalCustomerResults(c, out, q);
            appendGlobalProductResults(c, out, q);
            appendGlobalBankResults(c, out, q);
            appendGlobalCheckResults(c, out, q, true);
            appendGlobalCheckResults(c, out, q, false);
            appendGlobalInvoiceResults(c, out, q);
        }
        return out.toString();
    }

    private void appendGlobalCustomerResults(Connection c, JSONArray out, String q) {
        try {
            Set<String> cols = columns(c, "CUSTOMERS");
            String code = resolve(cols, "SHMO", "shmo");
            String name = resolve(cols, "MONAME", "Name", "CustomerName");
            String phone = resolve(cols, "cell", "mobile", "tell1", "phone");
            if (code == null) return;
            List<String> parts = new ArrayList<>();
            List<String> searchCols = new ArrayList<>();
            for (String col : new String[]{code, name, phone}) if (col != null && !searchCols.contains(col)) searchCols.add(col);
            for (String col : customerAddressColumns(cols)) if (col != null && !searchCols.contains(col)) searchCols.add(col);
            for (String col : searchCols) if (col != null) parts.add("TRY_CONVERT(nvarchar(500),[" + col + "]) LIKE N'%' + ? + N'%'");
            String sql = "SELECT TOP (5) TRY_CONVERT(nvarchar(80),[" + code + "]), " + (name == null ? "N'بدون نام'" : "TRY_CONVERT(nvarchar(250),[" + name + "])") + ", " + (phone == null ? "N''" : "TRY_CONVERT(nvarchar(100),[" + phone + "])") + ", " + customerAddressExpr(cols, "") + " FROM dbo.CUSTOMERS WHERE " + join(parts, " OR ");
            try (PreparedStatement ps = c.prepareStatement(sql)) { for (int i = 1; i <= parts.size(); i++) ps.setString(i, q); try (ResultSet r = ps.executeQuery()) { while (r.next()) addGlobalResult(out, "مشتری", stringOr(r.getString(2), "بدون نام"), "کد " + stringOr(r.getString(1), "—") + " • " + stringOr(r.getString(3), "") + (cleanCustomerAddress(r.getString(4)).isEmpty() ? "" : " • نشانی: " + cleanCustomerAddress(r.getString(4))), SUCCESS); } }
        } catch (Exception ignored) { }
    }

    private void appendGlobalProductResults(Connection c, JSONArray out, String q) {
        try {
            Set<String> cols = columns(c, "inventory");
            String code = resolve(cols, "shka", "SHKA");
            String name = resolve(cols, "naka", "Name", "KalaName");
            String barcode = resolve(cols, "StuffCode", "Code", "Barcode", "KalaCode");
            if (code == null) return;
            List<String> parts = new ArrayList<>();
            for (String col : new String[]{code, name, barcode}) if (col != null) parts.add("TRY_CONVERT(nvarchar(500),[" + col + "]) LIKE N'%' + ? + N'%'");
            String sql = "SELECT TOP (5) TRY_CONVERT(nvarchar(80),[" + code + "]), " + (name == null ? "N'بدون نام'" : "TRY_CONVERT(nvarchar(250),[" + name + "])") + ", " + (barcode == null ? "N''" : "TRY_CONVERT(nvarchar(100),[" + barcode + "])") + " FROM dbo.inventory WHERE " + join(parts, " OR ");
            try (PreparedStatement ps = c.prepareStatement(sql)) { for (int i = 1; i <= parts.size(); i++) ps.setString(i, q); try (ResultSet r = ps.executeQuery()) { while (r.next()) addGlobalResult(out, "کالا", stringOr(r.getString(2), "بدون نام"), "کد " + stringOr(r.getString(1), "—") + " • " + stringOr(r.getString(3), ""), WARNING); } }
        } catch (Exception ignored) { }
    }

    private void appendGlobalBankResults(Connection c, JSONArray out, String q) {
        try {
            Set<String> cols = columns(c, "BANK");
            String id = resolve(cols, "RDF", "ID", "BankID", "bankrdf");
            String name = resolve(cols, "BANKNAME", "BankName", "name", "Name");
            String branch = resolve(cols, "BranchCode", "branch", "code", "CODE");
            if (name == null) return;
            List<String> parts = new ArrayList<>();
            for (String col : new String[]{id, name, branch}) if (col != null) parts.add("TRY_CONVERT(nvarchar(500),[" + col + "]) LIKE N'%' + ? + N'%'");
            String sql = "SELECT TOP (5) " + (id == null ? "N''" : "TRY_CONVERT(nvarchar(80),[" + id + "])") + ", TRY_CONVERT(nvarchar(250),[" + name + "]), " + (branch == null ? "N''" : "TRY_CONVERT(nvarchar(100),[" + branch + "])") + " FROM dbo.BANK WHERE " + join(parts, " OR ");
            try (PreparedStatement ps = c.prepareStatement(sql)) { for (int i = 1; i <= parts.size(); i++) ps.setString(i, q); try (ResultSet r = ps.executeQuery()) { while (r.next()) addGlobalResult(out, "بانک", stringOr(r.getString(2), "بانک"), "کد " + stringOr(r.getString(1), "—") + " • شعبه " + stringOr(r.getString(3), "—"), INFO); } }
        } catch (Exception ignored) { }
    }

    private void appendGlobalCheckResults(Connection c, JSONArray out, String q, boolean incoming) {
        try {
            String table = incoming ? "getchk" : "putchk";
            Set<String> cols = columns(c, table);
            String num = incoming ? resolve(cols, "getchknum", "chknum", "number") : resolve(cols, "putchknum", "chknum", "number");
            String amount = incoming ? resolve(cols, "getchkmab", "amount") : resolve(cols, "putchkmab", "amount");
            String date = incoming ? resolve(cols, "getchkdate", "date", "sarresid") : resolve(cols, "putchkdate", "date", "sarresid");
            if (num == null) return;
            String sql = "SELECT TOP (5) TRY_CONVERT(nvarchar(100),[" + num + "]), " + (amount == null ? "CAST(0 AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),[" + amount + "])") + ", " + (date == null ? "N''" : "TRY_CONVERT(nvarchar(30),[" + date + "])") + " FROM dbo.[" + table + "] WHERE TRY_CONVERT(nvarchar(200),[" + num + "]) LIKE N'%' + ? + N'%'";
            try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1, q); try (ResultSet r = ps.executeQuery()) { while (r.next()) addGlobalResult(out, incoming ? "چک دریافتی" : "چک پرداختی", "شماره " + stringOr(r.getString(1), "—"), money(r.getDouble(2)) + " • " + stringOr(r.getString(3), ""), incoming ? SUCCESS : WARNING); } }
        } catch (Exception ignored) { }
    }

    private void appendGlobalInvoiceResults(Connection c, JSONArray out, String q) {
        try {
            Set<String> cols = columns(c, "sailfact");
            String no = resolve(cols, "shfacfo", "factor_no", "number", "shomare");
            String date = resolve(cols, "date", "t_date");
            String amount = resolve(cols, "all", "all_fel", "amount");
            if (no == null) return;
            String sql = "SELECT TOP (5) TRY_CONVERT(nvarchar(100),[" + no + "]), " + (amount == null ? "CAST(0 AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),[" + amount + "])") + ", " + (date == null ? "N''" : "TRY_CONVERT(nvarchar(30),[" + date + "])") + " FROM dbo.sailfact WHERE TRY_CONVERT(nvarchar(200),[" + no + "]) LIKE N'%' + ? + N'%'";
            try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1, q); try (ResultSet r = ps.executeQuery()) { while (r.next()) addGlobalResult(out, "فاکتور فروش", "فاکتور " + stringOr(r.getString(1), "—"), money(r.getDouble(2)) + " • " + stringOr(r.getString(3), ""), GOLD); } }
        } catch (Exception ignored) { }
    }

    private void addGlobalResult(JSONArray out, String type, String title, String sub, int accent) throws Exception {
        JSONObject o = new JSONObject(); o.put("type", type); o.put("title", title); o.put("sub", sub); o.put("accent", accent); out.put(o);
    }

    private void addGlobalSearchResultCard(LinearLayout parent, JSONObject r) {
        if (r == null) return;
        int accent = r.optInt("accent", GOLD);
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(accent, 26), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(report3dIcon(reportGlyph(r.optString("type", "")), accent), new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text(r.optString("type", "نتیجه") + " • " + r.optString("title", ""), 13.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(r.optString("sub", ""), 10.6f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        row.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); parent.addView(c, lp);
    }

    private void addHero(String title, String text) {
        LinearLayout hero = card();
        int accent = themeAccent(title);
        hero.setBackground(gradient(new int[]{alpha(Color.WHITE, isLightTheme() ? 36 : 18), alpha(accent, 24), HERO_START, HERO_END}, GradientDrawable.Orientation.TL_BR, 26));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        TextView img = report3dIcon(semanticGlyph(title), accent);
        row.addView(img, new LinearLayout.LayoutParams(dp(60), dp(60)));
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
        addLoadingChip(row, "مشتری", "م", SUCCESS, 360);
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        addSkeletonBars(c);
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

    private void addSkeletonBars(LinearLayout parent) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(8), dp(4), dp(8), 0);
        int[] widths = {92, 72, 86};
        for (int i = 0; i < widths.length; i++) {
            View bar = new View(this);
            bar.setBackground(gradient(new int[]{alpha(GOLD, 18), alpha(INFO, 12), alpha(SURFACE_2, 190)}, GradientDrawable.Orientation.LEFT_RIGHT, 999));
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(10));
            bp.setMargins(dp((100 - widths[i]) / 2), dp(7), dp((100 - widths[i]) / 2), 0);
            box.addView(bar, bp);
            animatePulse(bar, i * 100);
        }
        parent.addView(box, new LinearLayout.LayoutParams(-1, -2));
    }

    private void animatePulse(View v, long delay) {
        if (v == null || !motionAllowed()) return;
        v.setScaleX(0.96f);
        v.setScaleY(0.96f);
        v.setAlpha(0.88f);
        v.animate().setStartDelay(delay).scaleX(1.05f).scaleY(1.05f).alpha(1f).setDuration(760).withEndAction(() -> {
            if (v.getParent() != null) v.animate().setStartDelay(0).scaleX(0.96f).scaleY(0.96f).alpha(0.88f).setDuration(760).withEndAction(() -> animatePulse(v, 0)).start();
        }).start();
    }

    private void animateFloat(View v, long delay) {
        if (v == null || !motionAllowed()) return;
        v.setTranslationY(dp(2));
        v.animate().setStartDelay(delay).translationY(-dp(3)).rotationBy(6f).setDuration(950).withEndAction(() -> {
            if (v.getParent() != null) v.animate().setStartDelay(0).translationY(dp(2)).rotationBy(-6f).setDuration(950).withEndAction(() -> animateFloat(v, 0)).start();
        }).start();
    }

    private void showPageError(String title, Exception error, Runnable retry) {
        content.removeAllViews();
        addHero(title, "ارتباط امن با داده‌ها برقرار نشد");
        LinearLayout c = card();
        c.setGravity(Gravity.CENTER_HORIZONTAL);
        c.setBackground(gradient(new int[]{alpha(DANGER, 28), alpha(INFO, 12), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 28));
        c.addView(report3dIcon("!", DANGER), new LinearLayout.LayoutParams(dp(64), dp(64)));
        TextView h = text("فعلاً اتصال برقرار نشد", 17, TEXT, Typeface.BOLD);
        h.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.setMargins(0, dp(10), 0, 0);
        c.addView(h, hp);
        TextView m = text(readableError(error) + "\n\nجزئیات فنی اتصال پنهان می‌ماند؛ برای ادامه فقط دوباره تلاش کنید.", 12, MUTED, Typeface.NORMAL);
        m.setGravity(Gravity.CENTER);
        m.setLineSpacing(dp(3), 1.05f);
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2);
        mp.setMargins(0, dp(8), 0, dp(14));
        c.addView(m, mp);
        Button b = primaryButton("تلاش مجدد ✦");
        b.setOnClickListener(v -> retry.run());
        c.addView(b, new LinearLayout.LayoutParams(-1, dp(50)));
        content.addView(c, new LinearLayout.LayoutParams(-1, -2));
    }

    private interface DbJob { String run() throws Exception; }
    private interface DbCallback { void ok(String body); void fail(Exception e); }
    private interface JsonArrayJob { JSONArray run() throws Exception; }

    private void runDb(DbJob job, DbCallback callback) {
        setConnectionStatus("loading");
        long started = System.currentTimeMillis();
        executor.execute(() -> {
            try {
                String body = job.run();
                long elapsed = System.currentTimeMillis() - started;
                if (prefs != null) prefs.edit().putString(KEY_LAST_CONNECTION_OK, nowText() + " • " + elapsed + "ms").remove(KEY_LAST_CONNECTION_ERROR).apply();
                runOnUiThread(() -> {
                    setConnectionStatus("connected");
                    callback.ok(body);
                });
            } catch (Exception e) {
                if (prefs != null) prefs.edit().putString(KEY_LAST_CONNECTION_ERROR, nowText() + " • " + shortError(e)).apply();
                runOnUiThread(() -> {
                    setConnectionStatus("offline");
                    callback.fail(e);
                });
            }
        });
    }

    private String nowText() {
        try { return new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US).format(new Date()); }
        catch (Exception ignored) { return String.valueOf(System.currentTimeMillis()); }
    }

    private Connection openConnection() throws Exception {
        bindSqlNetworkForVpnIfNeeded();
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

    private void bindSqlNetworkForVpnIfNeeded() {
        lastSqlVpnDetected = false;
        lastSqlVpnBypassed = false;
        lastSqlNetworkNote = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (cm == null) return;
            Network active = cm.getActiveNetwork();
            NetworkCapabilities activeCaps = active == null ? null : cm.getNetworkCapabilities(active);
            lastSqlVpnDetected = activeCaps != null && activeCaps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
            if (!lastSqlVpnDetected) { cm.bindProcessToNetwork(null); lastSqlNetworkNote = "مسیر شبکه عادی"; return; }
            Network best = null;
            for (Network n : cm.getAllNetworks()) {
                NetworkCapabilities caps = cm.getNetworkCapabilities(n);
                if (caps == null) continue;
                boolean internet = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                boolean vpn = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                boolean wifi = caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                boolean cell = caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                if (internet && !vpn && (wifi || cell || best == null)) { best = n; if (wifi) break; }
            }
            if (best != null && cm.bindProcessToNetwork(best)) {
                lastSqlVpnBypassed = true;
                lastSqlNetworkNote = "VPN فعال تشخیص داده شد؛ اتصال SQL از شبکه مستقیم دستگاه عبور داده شد.";
            } else {
                lastSqlNetworkNote = "VPN فعال تشخیص داده شد اما مسیر مستقیم قابل انتخاب نبود.";
            }
        } catch (Exception ignored) { lastSqlNetworkNote = "بررسی مسیر شبکه کامل نشد."; }
    }

    private String sqlNetworkHint() {
        if (!lastSqlVpnDetected) return "";
        if (lastSqlVpnBypassed) return "\nVPN فعال بود و برنامه تلاش کرد اتصال SQL را از شبکه مستقیم دستگاه عبور دهد.";
        return "\nVPN فعال تشخیص داده شد؛ اگر اتصال برقرار نشد، در تنظیمات VPN اجازه عبور MEELANO یا Split tunneling را فعال کنید.";
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
                        return withResolvedAccessRole(c, new UserSession(uid, r.getObject(1) == null ? null : r.getInt(1), stringOr(r.getString(2), user)), user);
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
                            return withResolvedAccessRole(c, new UserSession(r.getInt(1), visitor, stringOr(r.getString(2), user)), user);
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

    private void loadDashboard() { loadDashboard(false); }

    private void loadDashboard(boolean force) {
        if (!force && dashboardCacheJson != null && !dashboardCacheJson.trim().isEmpty()) {
            try { renderDashboardJson(new JSONObject(dashboardCacheJson), false); return; } catch (Exception ignored) { }
        }
        content.removeAllViews();
        addLoading(content, "در حال دریافت داشبورد…");
        runDb(this::queryDashboard, new DbCallback() {
            @Override public void ok(String body) {
                try {
                    dashboardCacheJson = body;
                    markRefresh("dashboard");
                    if (prefs != null) prefs.edit().putString(KEY_CACHE_DASHBOARD, body).apply();
                    renderDashboardJson(new JSONObject(body), false);
                } catch (Exception e) { showPageError("داشبورد", e, () -> loadDashboard(true)); }
            }
            @Override public void fail(Exception e) {
                if (!renderCachedDashboard(e)) showPageError("داشبورد", e, () -> loadDashboard(true));
            }
        });
    }

    private void renderDashboardJson(JSONObject j, boolean cached) throws Exception {
        if (!isFullAccessUser()) { renderRoleDashboardJson(j, cached); return; }
        content.removeAllViews();
        JSONObject today = j.optJSONObject("today");
        addGoodMorningManagerCard(today, cached);
        addDashboardTeamBrief(today);
        addDashboardKpiTable(j.optJSONArray("kpis"));
        renderDashboardToday(today);
        updateHomeWidgetFromDashboard(today);
    }

    private void renderRoleDashboardJson(JSONObject j, boolean cached) throws Exception {
        content.removeAllViews();
        JSONObject today = j == null ? null : j.optJSONObject("today");
        addRoleWelcomeCard(today, cached);
        addRoleDashboardShortcuts();
        int before = content.getChildCount();
        if (today != null) addDashboardInsightTable(today);
        if (content.getChildCount() == before) {
            LinearLayout empty = new LinearLayout(this);
            empty.setOrientation(LinearLayout.VERTICAL);
            content.addView(empty, new LinearLayout.LayoutParams(-1, -2));
            addEmptyTo(empty, currentVisitorScopeId() == null ? "برای این حساب هنوز ویزیتور/محدوده مشتری مشخص نشده است؛ مدیر باید نقش یا ویزیتور را تنظیم کند." : "برای مشتریان مرتبط با شما موردی در بدهکار، تسویه‌نشده یا بدون خرید پیدا نشد.");
        }
        updateHomeWidgetFromDashboard(today);
    }

    private void addRoleWelcomeCard(JSONObject today, boolean cached) {
        LinearLayout c = card();
        c.setPadding(dp(12), dp(12), dp(12), dp(12));
        int accent = navAccent("dashboard");
        c.setBackground(gradient(new int[]{alpha(accent, 30), alpha(SUCCESS, 18), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 28));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("🔐", accent), new LinearLayout.LayoutParams(dp(46), dp(46)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(9), 0, dp(7), 0);
        copy.addView(text(timeGreetingTitle() + " " + displayFirstName(), 16.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        String scope = currentVisitorScopeId() == null ? "محدوده مشتری هنوز به ویزیتور وصل نشده" : "نمای مشتریان ویزیتور شما";
        copy.addView(text("دسترسی: " + accessRoleLabel(currentAccessRole()) + " • " + scope + (cached ? " • داده ذخیره‌شده" : ""), 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        head.addView(circularDashboardAction("⟳", "بروزرسانی داشبورد", INFO, v -> loadDashboard(true)), new LinearLayout.LayoutParams(dp(38), dp(38)));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(dashboardMiniMetric("بدهکاران", formatNumber(today == null ? 0 : today.optJSONArray("topDebtors") == null ? 0 : today.optJSONArray("topDebtors").length()), "مشتریان شما", DANGER), dashboardMiniLp());
        row.addView(dashboardMiniMetric("تسویه‌نشده", formatNumber(today == null ? 0 : today.optJSONArray("overdueInvoices") == null ? 0 : today.optJSONArray("overdueInvoices").length()), "فاکتور معوق", WARNING), dashboardMiniLp());
        row.addView(dashboardMiniMetric("بدون خرید", formatNumber(today == null ? 0 : today.optJSONArray("inactiveCustomers") == null ? 0 : today.optJSONArray("inactiveCustomers").length()), "فرصت پیگیری", INFO), dashboardMiniLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
        rp.setMargins(0, dp(10), 0, 0);
        c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private void addRoleDashboardShortcuts() {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(GOLD, 22), alpha(INFO, 14), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("دسترسی‌های فعال شما", 14.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("سایر بخش‌ها برای این نقش به‌صورت قفل دیده می‌شوند و فقط مدیر می‌تواند آن‌ها را باز کند.", 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        addRoleShortcut(row, "customers", "مشتریان");
        addRoleShortcut(row, "products", "کالاها");
        addRoleShortcut(row, "attendance", "حضور من");
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addRoleShortcut(LinearLayout row, String page, String label) {
        Button b = canOpenPage(page) ? primaryButton(label) : secondaryButton("🔒 " + label);
        b.setTextSize(9.5f);
        b.setOnClickListener(v -> showApp(page));
        row.addView(b, weightedButtonLp());
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

    private boolean renderCachedDashboard(Exception error) {
        try {
            String cached = dashboardCacheJson != null && !dashboardCacheJson.trim().isEmpty() ? dashboardCacheJson : (prefs == null ? "" : prefs.getString(KEY_CACHE_DASHBOARD, ""));
            if (cached == null || cached.trim().isEmpty()) return false;
            dashboardCacheJson = cached;
            renderDashboardJson(new JSONObject(cached), true);
            addCacheBanner("داشبورد آفلاین", "اتصال برقرار نشد؛ آخرین داده ذخیره‌شده نمایش داده می‌شود. خطا: " + shortError(error));
            return true;
        } catch (Exception ignored) { return false; }
    }

    private boolean renderCachedReports(Exception error) {
        try {
            String cached = reportsCacheJson != null && !reportsCacheJson.trim().isEmpty() ? reportsCacheJson : (prefs == null ? "" : prefs.getString(KEY_CACHE_REPORTS, ""));
            if (cached == null || cached.trim().isEmpty()) return false;
            reportsCacheJson = cached;
            renderAnalytics(new JSONObject(cached));
            addCacheBanner("گزارشات آفلاین", "اتصال برقرار نشد؛ آخرین اتاق فرمان ذخیره‌شده نمایش داده می‌شود. خطا: " + shortError(error));
            return true;
        } catch (Exception ignored) { return false; }
    }

    private void addCacheBanner(String title, String body) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(WARNING, 28), alpha(SURFACE, 246)}, GradientDrawable.Orientation.RIGHT_LEFT, 20));
        c.addView(text(title, 14.5f, WARNING, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView b = text(body, 10.8f, TEXT, Typeface.NORMAL);
        b.setLineSpacing(dp(2), 1.05f);
        c.addView(b, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10));
        if (content != null && content.getChildCount() > 0) content.addView(c, 0, lp); else content.addView(c, lp);
    }

    private void addDashboardSmartAlerts(JSONObject today, boolean cached) {
        JSONArray alerts = buildDashboardAlerts(today);
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(DANGER, alerts.length() > 0 ? 28 : 10), alpha(GOLD, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 24));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon(alerts.length() > 0 ? "!" : "✓", alerts.length() > 0 ? WARNING : SUCCESS), new LinearLayout.LayoutParams(dp(46), dp(46)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("هشدار امروز میلو", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(cached ? "بر اساس آخرین داده ذخیره‌شده" : "اولویت‌های فوری برای اقدام مدیر", 10.6f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        if (alerts.length() == 0) {
            TextView ok = text("فعلاً هشدار بحرانی دیده نمی‌شود؛ فقط مراقب باش، دیتابیس همیشه سورپرایز دارد!", 11.2f, MUTED, Typeface.NORMAL);
            ok.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams op = new LinearLayout.LayoutParams(-1, -2); op.setMargins(0, dp(10), 0, 0); c.addView(ok, op);
        } else {
            for (int i = 0; i < Math.min(5, alerts.length()); i++) {
                JSONObject a = alerts.optJSONObject(i);
                addAlertLine(c, a == null ? "هشدار" : a.optString("title", "هشدار"), a == null ? "" : a.optString("body", ""), a == null ? WARNING : a.optInt("accent", WARNING));
            }
            notifyDashboardAlerts(alerts);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private JSONArray buildDashboardAlerts(JSONObject today) {
        JSONArray out = new JSONArray();
        try {
            if (today == null) return out;
            JSONObject sales = today.optJSONObject("sales");
            JSONObject purchases = today.optJSONObject("purchases");
            double salesTotal = metricMoneyValue(sales == null ? null : sales.optJSONArray("metrics"), "جمع فروش");
            double buyTotal = metricMoneyValue(purchases == null ? null : purchases.optJSONArray("metrics"), "جمع خرید");
            if (buyTotal > 0 && salesTotal > 0 && buyTotal > salesTotal * 1.15) addAlert(out, "فشار نقدینگی", "خرید روز از فروش جلو زده؛ موجودی و پرداخت‌ها را کنترل کن.", WARNING);
            JSONArray overdue = today.optJSONArray("overdueInvoices");
            if (overdue != null && overdue.length() > 0) {
                JSONObject o = overdue.optJSONObject(0);
                addAlert(out, "فاکتور معوق", labelOf(o, "party", "مشتری نامشخص") + " • " + moneyValue(o, "amount") + " • " + o.optString("hint", "پیگیری فوری"), DANGER);
            }
            JSONArray debtors = today.optJSONArray("topDebtors");
            if (debtors != null && debtors.length() > 0) {
                JSONObject d = debtors.optJSONObject(0);
                addAlert(out, "بدهکار اولویت‌دار", labelOf(d, "party", "نامشخص") + " با مانده " + moneyValue(d, "amount"), DANGER);
            }
            JSONObject put = today.optJSONObject("putChecks");
            if (put != null) {
                JSONArray rows = put.optJSONArray("breakdown");
                JSONObject largest = strongestPoint(rows);
                if (largest != null && valueOf(largest) > 0) addAlert(out, "چک پرداختی", labelOf(largest, "label", "دسته چک") + " • " + reportValue(largest, 4), WARNING);
            }
            JSONArray inactive = today.optJSONArray("inactiveCustomers");
            if (inactive != null && inactive.length() >= 5) addAlert(out, "بازفعال‌سازی مشتری", "حداقل ۵ مشتری بدون خرید پیدا شد؛ کمپین تماس کوتاه پیشنهاد می‌شود.", INFO);
        } catch (Exception ignored) { }
        return out;
    }

    private void addAlert(JSONArray arr, String title, String body, int accent) throws Exception {
        JSONObject o = new JSONObject(); o.put("title", title); o.put("body", body); o.put("accent", accent); arr.put(o);
    }

    private void addAlertLine(LinearLayout parent, String title, String body, int accent) {
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);
        line.setPadding(dp(9), dp(8), dp(9), dp(8));
        line.setBackground(roundedStroke(alpha(accent, 16), 15, alpha(accent, 62)));
        TextView b = text("●", 18, accent, Typeface.BOLD);
        b.setGravity(Gravity.CENTER);
        line.addView(b, new LinearLayout.LayoutParams(dp(26), -1));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.addView(text(title, 11.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text(body, 10.2f, MUTED, Typeface.NORMAL);
        sub.setLineSpacing(dp(2), 1.05f);
        copy.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        line.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(8), 0, 0); parent.addView(line, lp);
    }

    private double metricMoneyValue(JSONArray metrics, String contains) {
        if (metrics == null) return 0;
        for (int i = 0; i < metrics.length(); i++) {
            JSONObject m = metrics.optJSONObject(i);
            if (m != null && m.optString("label", "").contains(contains)) return numericFromText(m.optString("value", "0"));
        }
        return 0;
    }

    private double numericFromText(String value) {
        if (value == null) return 0;
        String normalized = value.replace("ریال", "").replace(",", "").replace("٬", "").trim();
        normalized = normalized.replace('۰','0').replace('۱','1').replace('۲','2').replace('۳','3').replace('۴','4').replace('۵','5').replace('۶','6').replace('۷','7').replace('۸','8').replace('۹','9');
        try { return Double.parseDouble(normalized); } catch (Exception ignored) { return 0; }
    }

    private void addGoodMorningManagerCard(JSONObject today, boolean cached) {
        LinearLayout c = card();
        c.setPadding(dp(12), dp(12), dp(12), dp(12));
        int greetAccent = timeGreetingAccent();
        c.setBackground(gradient(new int[]{alpha(greetAccent, 34), alpha(INFO, 18), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 28));

        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon(timeGreetingIcon(), greetAccent), new LinearLayout.LayoutParams(dp(46), dp(46)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(9), 0, dp(7), 0);
        copy.addView(text(timeGreetingTitle() + " " + displayFirstName(), 16.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text(cached ? "آخرین داده ذخیره‌شده نمایش داده می‌شود؛ هر وقت خواستی با دکمه کنار کارت تازه‌سازی کن." : managerTimedLine(today), 10.2f, MUTED, Typeface.NORMAL);
        sub.setLineSpacing(dp(1), 1.03f);
        sub.setMaxLines(2);
        copy.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView refresh = circularDashboardAction("⟳", "بروزرسانی داشبورد", INFO, v -> loadDashboard(true));
        head.addView(refresh, new LinearLayout.LayoutParams(dp(38), dp(38)));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));

        JSONObject sales = today == null ? null : today.optJSONObject("sales");
        JSONObject checks = today == null ? null : today.optJSONObject("putChecks");
        JSONObject debtor = firstObject(today == null ? null : today.optJSONArray("topDebtors"));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(dashboardMiniMetric("فروش امروز", metricValue(sales, "جمع فروش", "—"), "فاکتورهای روز", GOLD), dashboardMiniLp());
        row.addView(dashboardMiniMetric("چک امروز", metricValue(checks, "جمع مبلغ", "—"), "پرداختی/سررسید", WARNING), dashboardMiniLp());
        String debtorName = labelOf(debtor, "party", "—");
        String debtorAmount = privacyMode() ? "•••• ریال" : moneyValue(debtor, "amount");
        row.addView(dashboardMiniMetric("بدهکار مهم", debtorName, debtorAmount, DANGER), dashboardMiniLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
        rp.setMargins(0, dp(10), 0, 0);
        c.addView(row, rp);

        LinearLayout tasksBox = new LinearLayout(this);
        tasksBox.setOrientation(LinearLayout.VERTICAL);
        tasksBox.setPadding(dp(8), dp(8), dp(8), dp(8));
        tasksBox.setBackground(roundedStroke(alpha(SURFACE_2, 128), 18, alpha(mix(INFO, GOLD, 0.35f), 64)));
        LinearLayout tasksHead = new LinearLayout(this);
        tasksHead.setGravity(Gravity.CENTER_VERTICAL);
        tasksHead.setOrientation(LinearLayout.HORIZONTAL);
        TextView taskTitle = text("کارهای امروز", 12.6f, TEXT, Typeface.BOLD);
        tasksHead.addView(taskTitle, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView last = text("آخرین بروزرسانی: " + lastRefreshText("dashboard"), 9.2f, MUTED, Typeface.NORMAL);
        last.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tasksHead.addView(last, new LinearLayout.LayoutParams(-2, -2));
        tasksBox.addView(tasksHead, new LinearLayout.LayoutParams(-1, -2));
        JSONArray tasks = buildTodayTasks(today);
        if (tasks.length() == 0) {
            try {
                JSONObject calm = new JSONObject();
                calm.put("id", "calm_day"); calm.put("tag", "آرام"); calm.put("title", "مرور کوتاه گزارش روزانه");
                calm.put("body", "فعلاً کار فوری دیده نمی‌شود؛ فروش و اتصال را چک کن."); calm.put("accent", SUCCESS);
                addDashboardTaskRow(tasksBox, calm);
            } catch (Exception ignored) { }
        } else {
            for (int i = 0; i < Math.min(4, tasks.length()); i++) addDashboardTaskRow(tasksBox, tasks.optJSONObject(i));
        }
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.setMargins(0, dp(10), 0, 0);
        c.addView(tasksBox, tp);

        Button listen = primaryButton("🔊 میلو بخوان");
        listen.setTextSize(10.5f);
        listen.setOnClickListener(v -> speakAssistantText(buildDailyVoiceSummary(today)));
        LinearLayout.LayoutParams lpListen = new LinearLayout.LayoutParams(-1, dp(42));
        lpListen.setMargins(0, dp(9), 0, 0);
        c.addView(listen, lpListen);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(12));
        content.addView(c, lp);
    }

    private TextView circularDashboardAction(String glyph, String description, int accent, View.OnClickListener listener) {
        TextView b = new TextView(this);
        b.setText(glyph);
        b.setTextSize(16f);
        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setTextColor(Color.WHITE);
        b.setShadowLayer(dp(3), 0, dp(1), alpha(Color.BLACK, 150));
        GradientDrawable bg = gradient(new int[]{mix(accent, Color.WHITE, 0.25f), accent, mix(accent, Color.BLACK, 0.26f)}, GradientDrawable.Orientation.TL_BR, 999);
        bg.setStroke(dp(1), alpha(Color.WHITE, 130));
        b.setBackground(bg);
        b.setClickable(true);
        b.setFocusable(true);
        b.setContentDescription(description);
        b.setOnClickListener(listener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) b.setElevation(dp(6));
        return b;
    }

    private LinearLayout.LayoutParams dashboardMiniLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(84), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        return lp;
    }

    private LinearLayout dashboardMiniMetric(String label, String value, String detail, int accent) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(dp(5), dp(6), dp(5), dp(6));
        box.setBackground(gradient(new int[]{alpha(accent, 28), alpha(SURFACE, 230)}, GradientDrawable.Orientation.TOP_BOTTOM, 16));
        TextView l = text(label, 8.9f, MUTED, Typeface.BOLD);
        l.setGravity(Gravity.CENTER);
        l.setSingleLine(true);
        TextView v = text(value == null || value.trim().isEmpty() ? "—" : value, 9.8f, TEXT, Typeface.BOLD);
        v.setGravity(Gravity.CENTER);
        v.setSingleLine(true);
        v.setEllipsize(TextUtils.TruncateAt.END);
        TextView d = text(detail == null || detail.trim().isEmpty() ? "—" : detail, 8.7f, accent, Typeface.BOLD);
        d.setGravity(Gravity.CENTER);
        d.setSingleLine(true);
        d.setEllipsize(TextUtils.TruncateAt.END);
        box.addView(l, new LinearLayout.LayoutParams(-1, -2));
        box.addView(v, new LinearLayout.LayoutParams(-1, 0, 1f));
        box.addView(d, new LinearLayout.LayoutParams(-1, -2));
        return box;
    }

    private void addDashboardTaskRow(LinearLayout parent, JSONObject task) {
        if (task == null) return;
        String id = task.optString("id", "task");
        int accent = task.optInt("accent", GOLD);
        boolean done = taskDone(id);
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setPadding(dp(6), dp(5), dp(6), dp(5));
        item.setBackground(roundedStroke(alpha(done ? SUCCESS : accent, done ? 16 : 13), 14, alpha(done ? SUCCESS : accent, 50)));
        TextView badge = text(done ? "✓" : task.optString("tag", "امروز"), 8.6f, done ? SUCCESS : accent, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER);
        badge.setSingleLine(true);
        badge.setBackground(roundedStroke(alpha(done ? SUCCESS : accent, 24), 999, alpha(done ? SUCCESS : accent, 70)));
        item.addView(badge, new LinearLayout.LayoutParams(dp(50), dp(30)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(7), 0, dp(7), 0);
        TextView title = text(task.optString("title", "کار امروز"), 10.4f, TEXT, Typeface.BOLD);
        title.setSingleLine(true);
        title.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(title, new LinearLayout.LayoutParams(-1, -2));
        TextView body = text(task.optString("body", ""), 9.2f, done ? alpha(MUTED, 145) : MUTED, Typeface.NORMAL);
        body.setSingleLine(true);
        body.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(body, new LinearLayout.LayoutParams(-1, -2));
        item.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        Button toggle = done ? secondaryButton("برگردان") : primaryButton("انجام شد");
        toggle.setTextSize(8.4f);
        toggle.setPadding(dp(2), 0, dp(2), 0);
        toggle.setOnClickListener(v -> { setTaskDone(id, !taskDone(id)); refreshActivePage(); });
        item.addView(toggle, new LinearLayout.LayoutParams(dp(70), dp(32)));
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1, -2);
        ip.setMargins(0, dp(6), 0, 0);
        parent.addView(item, ip);
    }

    private int currentHourOfDay() {
        try { return Calendar.getInstance(new Locale("fa", "IR")).get(Calendar.HOUR_OF_DAY); }
        catch (Exception ignored) { return Calendar.getInstance().get(Calendar.HOUR_OF_DAY); }
    }

    private String timeGreetingTitle() {
        int h = currentHourOfDay();
        if (h >= 5 && h < 11) return "صبح بخیر";
        if (h >= 11 && h < 15) return "ظهر بخیر";
        if (h >= 15 && h < 20) return "عصر بخیر";
        return "شب بخیر";
    }

    private String timeGreetingIcon() {
        int h = currentHourOfDay();
        if (h >= 5 && h < 11) return "☀";
        if (h >= 11 && h < 15) return "◉";
        if (h >= 15 && h < 20) return "◐";
        return "☾";
    }

    private int timeGreetingAccent() {
        int h = currentHourOfDay();
        if (h >= 5 && h < 11) return GOLD;
        if (h >= 11 && h < 15) return GOLD_2;
        if (h >= 15 && h < 20) return WARNING;
        return INFO;
    }

    private String managerTimedLine(JSONObject today) {
        String base = managerMorningLine(today);
        int h = currentHourOfDay();
        if (h >= 11 && h < 15) return "گزارش ظهر: " + base;
        if (h >= 15 && h < 20) return "جمع‌بندی عصر: " + base;
        if (h >= 20 || h < 5) return "جمع‌بندی شبانه: " + base;
        return base;
    }

    private void addDashboardTeamBrief(JSONObject today) {
        JSONObject t = today == null ? null : today.optJSONObject("teamBrief");
        if (t == null) return;
        long attendance = t.optLong("attendanceToday", 0);
        long pending = t.optLong("pendingLeaves", 0);
        long pinned = t.optLong("pinnedMessages", 0);
        long members = t.optLong("chatMembers", 0);
        if (attendance == 0 && pending == 0 && pinned == 0 && members == 0) return;
        LinearLayout c = card();
        c.setPadding(dp(11), dp(10), dp(11), dp(10));
        c.setBackground(gradient(new int[]{alpha(navAccent("attendance"), 24), alpha(navAccent("chat"), 18), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 24));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("CEO", GOLD), new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(9),0,dp(9),0);
        copy.addView(text("داشبورد مدیریتی تیم", 15.2f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1,-2));
        copy.addView(text("خلاصه سریع گفتگو، حضور و مرخصی برای تصمیم روزانه", 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1,-2));
        head.addView(copy, new LinearLayout.LayoutParams(0,-2,1f)); c.addView(head, new LinearLayout.LayoutParams(-1,-2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(dashboardMiniMetric("حاضر امروز", formatNumber(attendance), "پرسنل", SUCCESS), dashboardMiniLp());
        row.addView(dashboardMiniMetric("مرخصی منتظر", formatNumber(pending), pending > 0 ? "نیاز بررسی" : "آرام", pending > 0 ? WARNING : SUCCESS), dashboardMiniLp());
        row.addView(dashboardMiniMetric("پیام پین", formatNumber(pinned), "گفتگو", navAccent("chat")), dashboardMiniLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(9), 0, 0); c.addView(row, rp);
        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
        Button chat = secondaryButton("گفتگو"); Button attendanceBtn = secondaryButton("حضور"); Button personnelBtn = secondaryButton("پرسنل");
        chat.setTextSize(9.5f); attendanceBtn.setTextSize(9.5f); personnelBtn.setTextSize(9.5f);
        chat.setOnClickListener(v -> showApp("chat")); attendanceBtn.setOnClickListener(v -> showApp("attendance")); personnelBtn.setOnClickListener(v -> showApp("personnel"));
        actions.addView(chat, weightedButtonLp()); actions.addView(attendanceBtn, weightedButtonLp()); actions.addView(personnelBtn, weightedButtonLp());
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1,-2); ap.setMargins(0, dp(8), 0, 0); c.addView(actions, ap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private String managerMorningLine(JSONObject today) {
        if (today == null) return "امروز اول اتصال و داده‌ها را تازه کن؛ بدون داده، مدیریت هم چشم‌بسته می‌شود.";
        JSONObject debtor = firstObject(today.optJSONArray("topDebtors"));
        JSONArray overdue = today.optJSONArray("overdueInvoices");
        JSONObject inactive = firstObject(today.optJSONArray("inactiveCustomers"));
        String d = labelOf(debtor, "party", "بدهکار مهم");
        if (overdue != null && overdue.length() > 0) return "اول پیگیری وصول و چک‌ها؛ " + d + " را جدی بگیر، نقدینگی امروز ناز دارد.";
        if (inactive != null) return "فروش را با بازفعال‌سازی «" + labelOf(inactive, "party", "مشتری خاموش") + "» گرم کن؛ میلو می‌گوید فرصت خوابیده است.";
        return "وضعیت بحرانی دیده نمی‌شود؛ فروش، وصول و قیمت‌گذاری را همزمان نگه دار.";
    }

    private void addTodayTaskCenter(JSONObject today) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 18), alpha(GOLD, 16), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 26));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("✓", SUCCESS), new LinearLayout.LayoutParams(dp(50), dp(50)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("کارهای امروز", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("اولویت‌ها را انجام‌شده بزنید؛ وضعیت روی همین دستگاه ذخیره می‌شود.", 10.7f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        JSONArray tasks = buildTodayTasks(today);
        if (tasks.length() == 0) addActionItem(c, "آرام", "فعلاً کار فوری دیده نمی‌شود؛ فقط گزارش روزانه و وضعیت اتصال را چک کن.", SUCCESS);
        for (int i = 0; i < tasks.length(); i++) addTaskItem(c, tasks.optJSONObject(i));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private JSONArray buildTodayTasks(JSONObject today) {
        JSONArray arr = new JSONArray();
        try {
            if (today == null) return arr;
            JSONObject debtor = firstObject(today.optJSONArray("topDebtors"));
            if (debtor != null) arr.put(new JSONObject().put("id", "debtor_" + labelOf(debtor, "party", "x")).put("tag", "فوری").put("title", "تماس با بدهکار مهم").put("body", labelOf(debtor, "party", "مشتری") + " • " + moneyValue(debtor, "amount")).put("accent", DANGER));
            JSONObject overdue = firstObject(today.optJSONArray("overdueInvoices"));
            if (overdue != null) arr.put(new JSONObject().put("id", "overdue_" + labelOf(overdue, "party", "x")).put("tag", "امروز").put("title", "پیگیری فاکتور معوق").put("body", labelOf(overdue, "party", "طرف حساب") + " • " + moneyValue(overdue, "amount")).put("accent", WARNING));
            JSONObject inactive = firstObject(today.optJSONArray("inactiveCustomers"));
            if (inactive != null) arr.put(new JSONObject().put("id", "inactive_" + labelOf(inactive, "party", "x")).put("tag", "فروش").put("title", "بازفعال‌سازی مشتری خاموش").put("body", labelOf(inactive, "party", "مشتری") + " را با پیشنهاد کوتاه پیگیری کن.").put("accent", INFO));
            JSONObject sales = today.optJSONObject("sales");
            JSONObject purchases = today.optJSONObject("purchases");
            if (metricMoneyValue(purchases == null ? null : purchases.optJSONArray("metrics"), "جمع خرید") > metricMoneyValue(sales == null ? null : sales.optJSONArray("metrics"), "جمع فروش")) arr.put(new JSONObject().put("id", "cash_pressure").put("tag", "نقدینگی").put("title", "کنترل خرید بالاتر از فروش").put("body", "خرید از فروش جلوتر است؛ پرداخت‌ها را با بانک و چک تطبیق بده.").put("accent", WARNING));
            arr.put(new JSONObject().put("id", "daily_report").put("tag", "گزارش").put("title", "مرور گزارش روزانه").put("body", "خلاصه فروش، چک و مشتری را با میلو مرور کن.").put("accent", GOLD));
        } catch (Exception ignored) { }
        return arr;
    }

    private void addTaskItem(LinearLayout parent, JSONObject task) {
        if (task == null) return;
        String id = task.optString("id", "task");
        int accent = task.optInt("accent", GOLD);
        boolean done = taskDone(id);
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setPadding(dp(9), dp(8), dp(9), dp(8));
        item.setBackground(roundedStroke(alpha(done ? SUCCESS : accent, done ? 18 : 16), 17, alpha(done ? SUCCESS : accent, 65)));
        TextView badge = text(done ? "انجام شد" : task.optString("tag", "امروز"), 9.8f, done ? SUCCESS : accent, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER); badge.setSingleLine(true);
        badge.setBackground(roundedStroke(alpha(done ? SUCCESS : accent, 24), 999, alpha(done ? SUCCESS : accent, 72)));
        item.addView(badge, new LinearLayout.LayoutParams(dp(70), dp(34)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(8), 0, dp(8), 0);
        copy.addView(text(task.optString("title", "کار امروز"), 11.3f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView body = text(task.optString("body", ""), 10.2f, done ? alpha(MUTED, 150) : MUTED, Typeface.NORMAL); body.setMaxLines(2);
        copy.addView(body, new LinearLayout.LayoutParams(-1, -2));
        item.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        Button toggle = done ? secondaryButton("برگردان") : primaryButton("انجام شد");
        toggle.setTextSize(9.4f);
        toggle.setOnClickListener(v -> { setTaskDone(id, !taskDone(id)); refreshActivePage(); });
        item.addView(toggle, new LinearLayout.LayoutParams(dp(82), dp(38)));
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1, -2); ip.setMargins(0, dp(8), 0, 0); parent.addView(item, ip);
    }

    private String taskKey(String id) {
        long day = System.currentTimeMillis() / 86400000L;
        return "task_done_" + day + "_" + (id == null ? "task" : id.replace(' ', '_'));
    }

    private boolean taskDone(String id) { return prefs != null && prefs.getBoolean(taskKey(id), false); }
    private void setTaskDone(String id, boolean done) { if (prefs != null) prefs.edit().putBoolean(taskKey(id), done).apply(); }

    private String buildDailyVoiceSummary(JSONObject today) {
        JSONObject sales = today == null ? null : today.optJSONObject("sales");
        JSONObject purchases = today == null ? null : today.optJSONObject("purchases");
        JSONObject debtor = firstObject(today == null ? null : today.optJSONArray("topDebtors"));
        return displayFirstName() + " عزیز، گزارش سریع امروز: فروش " + metricValue(sales, "جمع فروش", "نامشخص") + ". خرید " + metricValue(purchases, "جمع خرید", "نامشخص") + ". بدهکار مهم: " + labelOf(debtor, "party", "نداریم") + ". پیشنهاد میلو: اول وصول و چک‌ها، بعد فروش جدید. مدیریتی باش، نه فقط خوش‌استایل.";
    }

    private void updateHomeWidgetFromDashboard(JSONObject today) {
        try {
            String summary = buildWidgetSummary(today);
            if (prefs != null) prefs.edit().putString(KEY_WIDGET_SUMMARY, summary).apply();
            RemoteViews views = new RemoteViews(getPackageName(), ir.meelano.android.R.layout.widget_meelano);
            views.setTextViewText(ir.meelano.android.R.id.widget_title, "Meelano امروز");
            views.setTextViewText(ir.meelano.android.R.id.widget_summary, summary);
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 1818, intent, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);
            views.setOnClickPendingIntent(ir.meelano.android.R.id.widget_root, pi);
            AppWidgetManager.getInstance(this).updateAppWidget(new ComponentName(this, MeelanoWidgetProvider.class), views);
        } catch (Exception ignored) { }
    }

    private String buildWidgetSummary(JSONObject today) {
        JSONObject sales = today == null ? null : today.optJSONObject("sales");
        JSONObject debtor = firstObject(today == null ? null : today.optJSONArray("topDebtors"));
        return "فروش: " + metricValue(sales, "جمع فروش", "—") + "\nبدهکار: " + labelOf(debtor, "party", "—") + "\nاتصال: " + (prefs == null ? "—" : prefs.getString(KEY_LAST_CONNECTION_OK, "آماده"));
    }

    private void notifyDashboardAlerts(JSONArray alerts) {
        if (alerts == null || alerts.length() == 0 || prefs == null) return;
        if (!prefs.getBoolean(KEY_REMIND_CHECKS, false) && !prefs.getBoolean(KEY_REMIND_DEBTORS, false) && !prefs.getBoolean(KEY_REMIND_INACTIVE, false) && !prefs.getBoolean(KEY_REMIND_DAILY, false)) return;
        long day = System.currentTimeMillis() / 86400000L;
        if (prefs.getLong(KEY_LAST_ALERT_DAY, -1) == day) return;
        JSONObject first = alerts.optJSONObject(0);
        showLocalNotification("هشدار Meelano", first == null ? "چند هشدار مدیریتی نیازمند بررسی است." : first.optString("title", "هشدار") + ": " + first.optString("body", ""), false);
        prefs.edit().putLong(KEY_LAST_ALERT_DAY, day).apply();
    }

    private void renderDashboardToday(JSONObject today) {
        if (today == null) return;
        JSONObject salesBlock = today.optJSONObject("sales");
        JSONObject purchaseBlock = today.optJSONObject("purchases");
        try { if (salesBlock != null && (salesBlock.optJSONArray("items") == null || salesBlock.optJSONArray("items").length() == 0)) salesBlock.put("items", today.optJSONArray("todayItems")); } catch (Exception ignored) { }
        try { if (purchaseBlock != null && (purchaseBlock.optJSONArray("items") == null || purchaseBlock.optJSONArray("items").length() == 0)) purchaseBlock.put("items", today.optJSONArray("purchaseItems")); } catch (Exception ignored) { }
        addDailyFinanceDashboardBlock("sales", "فروش روز", "انتخاب تاریخ، مشاهده منحنی و ورود به فاکتورها/دریافتی‌ها", ir.meelano.android.R.drawable.icon_sales, salesBlock, GOLD);
        addDailyFinanceDashboardBlock("purchase", "خرید روز", "انتخاب تاریخ، مشاهده منحنی و ورود به اسناد/پرداختی‌ها", ir.meelano.android.R.drawable.icon_products, purchaseBlock, INFO);
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
        addDailyItemsInline(c, type, data.optJSONArray("items"), accent);

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

    private void addDailyItemsInline(LinearLayout parent, String type, JSONArray items, int accent) {
        if (parent == null) return;
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(9), dp(9), dp(9), dp(8));
        box.setBackground(roundedStroke(alpha(accent, 18), 18, alpha(accent, 68)));
        String title = "sales".equals(type) ? "سرجمع کالاهای فروخته‌شده همین روز" : "سرجمع کالاهای خریداری‌شده همین روز";
        String sub = "بر اساس فاکتورهای همان تاریخ؛ هر قلم یک‌بار و سرجمع نمایش داده می‌شود.";
        box.addView(text(title, 13.2f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView hint = text(sub, 9.5f, MUTED, Typeface.NORMAL);
        hint.setMaxLines(2);
        box.addView(hint, new LinearLayout.LayoutParams(-1, -2));
        if (items == null || items.length() == 0) {
            TextView empty = text("برای این تاریخ اقلام قابل تفکیک پیدا نشد؛ اگر فاکتور دارای ریزاقلام است، با بروزرسانی دستی دوباره بررسی می‌شود.", 10.2f, MUTED, Typeface.NORMAL);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(dp(8), dp(10), dp(8), dp(8));
            empty.setBackground(roundedStroke(alpha(SURFACE_2, 120), 14, alpha(accent, 48)));
            LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(-1, -2); ep.setMargins(0, dp(7), 0, 0);
            box.addView(empty, ep);
        }
        for (int i = 0; items != null && i < Math.min(items.length(), 5); i++) {
            JSONObject r = items.optJSONObject(i);
            if (r == null) continue;
            LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setGravity(Gravity.CENTER_VERTICAL);
            line.setPadding(dp(7), dp(6), dp(7), dp(6));
            line.setClickable(true);
            line.setBackground(roundedStroke(alpha(SURFACE_2, 132), 14, alpha(accent, 54)));
            TextView idx = text(formatNumber(i + 1), 10, Color.WHITE, Typeface.BOLD);
            idx.setGravity(Gravity.CENTER);
            idx.setBackground(gradient(new int[]{accent, mix(accent, Color.BLACK, 0.25f)}, GradientDrawable.Orientation.TL_BR, 999));
            line.addView(idx, new LinearLayout.LayoutParams(dp(28), dp(28)));
            LinearLayout copy = new LinearLayout(this);
            copy.setOrientation(LinearLayout.VERTICAL);
            copy.setPadding(dp(7), 0, dp(7), 0);
            TextView name = text(r.optString("item", "کالا"), 10.8f, TEXT, Typeface.BOLD);
            name.setSingleLine(true);
            name.setEllipsize(TextUtils.TruncateAt.END);
            copy.addView(name, new LinearLayout.LayoutParams(-1, -2));
            String group = r.optString("group", "");
            double qv = r.optDouble("quantity", r.optDouble("qty", 0));
            TextView meta = text((group == null || group.trim().isEmpty() ? "بدون گروه" : group) + " • مقدار " + formatNumber(qv), 9.2f, MUTED, Typeface.NORMAL);
            meta.setSingleLine(true);
            meta.setEllipsize(TextUtils.TruncateAt.END);
            copy.addView(meta, new LinearLayout.LayoutParams(-1, -2));
            line.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
            TextView amount = text(compactMoney(r.opt("amount")), 10.3f, TEXT, Typeface.BOLD);
            amount.setGravity(Gravity.CENTER);
            amount.setSingleLine(true);
            amount.setBackground(roundedStroke(alpha(accent, 20), 999, alpha(accent, 76)));
            amount.setPadding(dp(7), dp(4), dp(7), dp(4));
            line.addView(amount, new LinearLayout.LayoutParams(-2, -2));
            line.setOnClickListener(v -> openProductFromDashboard(r));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, dp(6), 0, 0);
            box.addView(line, lp);
        }
        if (items != null && items.length() > 5) {
            TextView more = text("+ " + formatNumber(items.length() - 5) + " قلم دیگر در دکمه «اقلام روز»", 9.3f, accent, Typeface.BOLD);
            more.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2); mp.setMargins(0, dp(6), 0, 0);
            box.addView(more, mp);
        }
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2);
        bp.setMargins(0, dp(10), 0, 0);
        parent.addView(box, bp);
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
        try { block.put("date", r.optString("date")); block.put("metrics", r.optJSONArray("metrics")); block.put("chart", r.optJSONArray("chart")); block.put("items", r.optJSONArray("items")); } catch (Exception ignored) { }
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
            if (r.optJSONArray("items") == null || r.optJSONArray("items").length() == 0) r.put("items", queryDailyAggregatedItems(c, "sales".equals(type), r.optString("date", actualDate), 120));
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
        JSONObject salesBlock = queryDailyDashboardBlock(c, true, salesDate);
        JSONArray salesItems = queryDailyAggregatedItems(c, true, salesDate, 8);
        salesBlock.put("items", salesItems);
        JSONObject purchaseBlock = queryDailyDashboardBlock(c, false, purchaseDate);
        JSONArray purchaseItems = queryDailyAggregatedItems(c, false, purchaseDate, 8);
        purchaseBlock.put("items", purchaseItems);
        out.put("sales", salesBlock);
        out.put("purchases", purchaseBlock);
        out.put("getChecks", queryCheckDashboardBlock(c, true));
        out.put("putChecks", queryCheckDashboardBlock(c, false));
        out.put("topDebtors", queryTopDebtors(c));
        out.put("overdueInvoices", queryOverdueInvoices(c));
        out.put("inactiveCustomers", queryInactiveCustomers(c));
        out.put("todayItems", salesItems);
        out.put("purchaseItems", purchaseItems);
        out.put("banks", loadBanks(c));
        out.put("teamBrief", queryCollaborationBrief(c));
        return out;
    }

    private JSONObject queryCollaborationBrief(Connection c) {
        JSONObject o = new JSONObject();
        try {
            o.put("attendanceToday", 0); o.put("pendingLeaves", 0); o.put("pinnedMessages", 0); o.put("chatMembers", 0);
            if (c == null) return o;
            if (tableExists(c, "meelano_attendance")) {
                try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(DISTINCT username) FROM dbo.meelano_attendance WHERE CONVERT(date,event_time)=CONVERT(date,SYSDATETIME())")) { try (ResultSet r = ps.executeQuery()) { if (r.next()) o.put("attendanceToday", r.getLong(1)); } }
            }
            if (tableExists(c, "meelano_leave_requests")) {
                try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(1) FROM dbo.meelano_leave_requests WHERE status=N'pending'")) { try (ResultSet r = ps.executeQuery()) { if (r.next()) o.put("pendingLeaves", r.getLong(1)); } }
            }
            if (tableExists(c, "meelano_chat_messages")) {
                try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(1) FROM dbo.meelano_chat_messages WHERE deleted=0 AND pinned=1")) { try (ResultSet r = ps.executeQuery()) { if (r.next()) o.put("pinnedMessages", r.getLong(1)); } }
            }
            if (tableExists(c, "meelano_chat_members")) {
                try (PreparedStatement ps = c.prepareStatement("SELECT COUNT_BIG(1) FROM dbo.meelano_chat_members WHERE kicked=0")) { try (ResultSet r = ps.executeQuery()) { if (r.next()) o.put("chatMembers", r.getLong(1)); } }
            }
        } catch (Exception ignored) { }
        return o;
    }

    private boolean tableExists(Connection c, String table) {
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM sys.tables t JOIN sys.schemas s ON s.schema_id=t.schema_id WHERE s.name=N'dbo' AND t.name=?")) {
            ps.setString(1, table);
            try (ResultSet r = ps.executeQuery()) { return r.next(); }
        } catch (Exception ignored) { return false; }
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
        List<String> where = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        where.add("TRY_CONVERT(decimal(19,2),c.[" + balance + "])>0");
        String scope = customerScopeCondition(cols, "c", params);
        if (!scope.isEmpty()) where.add(scope);
        String sql = "SELECT TOP (8) TRY_CONVERT(nvarchar(100),c.[" + shmo + "]), " + label + ", TRY_CONVERT(decimal(19,2),c.[" + balance + "]) FROM dbo.CUSTOMERS c WHERE " + join(where, " AND ") + " ORDER BY 3 DESC";
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("party", stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("hint", "مانده بدهی"); o.put("value", r.getDouble(3)); arr.put(o); }
            }
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
        String joinSql = custName != null && shmo != null && custCode != null ? " LEFT JOIN dbo.CUSTOMERS c ON TRY_CONVERT(nvarchar(100),c.[" + custCode + "])=TRY_CONVERT(nvarchar(100),s.[" + shmo + "])" : "";
        String visitorExpr = visitorId != null && visKey != null && visName != null ? "COALESCE(TRY_CONVERT(nvarchar(150),v.[" + visName + "]),N'بدون ویزیتور')" : "N'بدون ویزیتور'";
        if (visitorId != null && visKey != null && visName != null) joinSql += " LEFT JOIN dbo.visitors v ON TRY_CONVERT(nvarchar(100),v.[" + visKey + "])=TRY_CONVERT(nvarchar(100),s.[" + visitorId + "])";
        String number = hasCol(sail, "shfacfo") ? "TRY_CONVERT(nvarchar(80),s.shfacfo)" : "CAST(NULL AS nvarchar(80))";
        List<String> where = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        where.add("s.tasvieh='f'");
        where.add("NULLIF(s.t_date,'') IS NOT NULL");
        where.add("dbo.dif_date_alan(s.t_date)<0");
        String scope = salesScopeCondition(sail, "s", params);
        if (!scope.isEmpty()) where.add(scope);
        String sql = "SELECT TOP (8) " + codeExpr + ", " + nameExpr + ", TRY_CONVERT(decimal(19,2),s.[all]), s.t_date, -dbo.dif_date_alan(s.t_date), " + visitorExpr + ", " + number + " FROM dbo.sailfact s" + joinSql + " WHERE " + join(where, " AND ") + activeAnd(sail, "s") + " ORDER BY -dbo.dif_date_alan(s.t_date) DESC";
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("party", stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("dueDate", stringOr(r.getString(4), "")); o.put("days", r.getLong(5)); o.put("visitor", stringOr(r.getString(6), "بدون ویزیتور")); o.put("invoice", stringOr(r.getString(7), "")); o.put("hint", "تاخیر " + formatNumber(r.getLong(5)) + " روز"); o.put("value", r.getDouble(3)); arr.put(o); }
            }
        }
        return arr;
    }

    private JSONArray queryInactiveCustomers(Connection c) throws Exception {
        Set<String> cols = columns(c, "CUSTOMERS");
        Set<String> sail = columns(c, "sailfact");
        String shmo = resolve(cols, "SHMO", "shmo"); String name = resolve(cols, "MONAME", "Name", "CusName"); String sailShmo = resolve(sail, "shmo", "SHMO");
        if (shmo == null || sailShmo == null) return new JSONArray();
        String label = name == null ? "TRY_CONVERT(nvarchar(120),c.[" + shmo + "])" : "TRY_CONVERT(nvarchar(250),c.[" + name + "])";
        List<String> where = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        where.add("NOT EXISTS (SELECT 1 FROM dbo.sailfact s WHERE TRY_CONVERT(nvarchar(100),s.[" + sailShmo + "])=TRY_CONVERT(nvarchar(100),c.[" + shmo + "]))");
        String scope = customerScopeCondition(cols, "c", params);
        if (!scope.isEmpty()) where.add(scope);
        String sql = "SELECT TOP (8) TRY_CONVERT(nvarchar(100),c.[" + shmo + "]), " + label + ", CAST(0 AS decimal(19,2)), N'بدون خرید ثبت‌شده' FROM dbo.CUSTOMERS c WHERE " + join(where, " AND ") + " ORDER BY " + label;
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("party", stringOr(r.getString(2), "—")); o.put("amount", r.getDouble(3)); o.put("hint", stringOr(r.getString(4), "بدون خرید")); o.put("value", r.getDouble(3)); arr.put(o); }
            }
        }
        return arr;
    }

    private JSONArray queryTodaySoldItems(Connection c, String date) throws Exception {
        return queryDailyAggregatedItems(c, true, date, 8);
    }

    private JSONArray queryDailyAggregatedItems(Connection c, boolean sales, String date, int top) throws Exception {
        JSONArray arr = new JSONArray();
        if (c == null || date == null || date.trim().isEmpty()) return arr;
        String header = sales ? "sailfact" : "buyfact";
        String[] detailCandidates = sales
                ? new String[]{"subsailfact", "sub_sailfact", "sailfact_sub", "sail_items", "sailitem", "sale_items", "subsailfact_pish"}
                : new String[]{"subbuyfact", "sub_buyfact", "buyfact_sub", "buy_items", "buyitem", "purchase_items"};
        Set<String> h = columns(c, header);
        String detailTable = "";
        Set<String> d = new HashSet<>();
        for (String candidate : detailCandidates) {
            try {
                Set<String> cc = columns(c, candidate);
                if (cc != null && !cc.isEmpty()) { detailTable = candidate; d = cc; break; }
            } catch (Exception ignored) { }
        }
        if (detailTable.isEmpty() || d == null || d.isEmpty()) return arr;
        Set<String> inv = columns(c, "inventory");
        Set<String> grp = columns(c, "kagroup");
        String dateCol = sales ? resolveFlexible(h, "date", "DATE", "tarikh", "Date", "t_date", "تاریخ", "tarikh_factor") : resolveFlexible(h, "DATE", "date", "tarikh", "Date", "t_date", "تاریخ", "tarikh_factor");
        String detailDate = resolveFlexible(d, "date", "DATE", "tarikh", "Date", "t_date", "تاریخ", "tarikh_factor");
        String numberCol = sales ? resolveFlexible(h, "shfacfo", "shfac", "shfacfor", "factor_no", "invoice_no", "fac_no", "number", "serial", "شماره") : resolveFlexible(h, "shfackh", "shfac", "buy_no", "factor_no", "invoice_no", "fac_no", "number", "serial", "شماره");
        String detailNumber = sales ? resolveFlexible(d, "shfacfo", "shfac", "shfacfor", "factor_no", "invoice_no", "fac_no", "number", "serial", "شماره") : resolveFlexible(d, "shfackh", "shfac", "buy_no", "factor_no", "invoice_no", "fac_no", "number", "serial", "شماره");
        String key = resolveFlexible(d, "SHKA", "shka", "KalaCode", "kala", "item_code", "product_code", "code", "کدکالا", "کد_کالا");
        String detailName = sales ? resolveFlexible(d, "naka", "Naka", "name", "Name", "Desc_Naka", "KalaName", "item_name", "product_name", "نام_کالا") : resolveFlexible(d, "Desc_Naka", "naka", "Naka", "name", "Name", "KalaName", "item_name", "product_name", "نام_کالا");
        if (key == null && detailName == null) return arr;
        String lineAmount = sales ? resolveFlexible(d, "LINESUM", "LineSum", "line_sum", "tamam_joz", "amount", "mablagh", "Mablagh", "all", "kol", "total", "Total", "mabkol", "مبلغ") : resolveFlexible(d, "tamam_joz", "LINESUM", "LineSum", "line_sum", "amount", "mablagh", "Mablagh", "all", "kol", "total", "Total", "mabkol", "مبلغ");
        String unitPrice = resolveFlexible(d, "FI", "fi", "fee", "Fee", "price", "Price", "gimat", "Gheymat", "mablagh_vah", "price_vah", "unit_price", "فی");
        String qtyCol = resolveFlexible(d, "tedad", "Tedad", "TEDAD", "qty", "Qty", "quantity", "Quantity", "meghdar", "Meghdar", "ted", "TED", "tedadkol", "count", "Count", "مقدار", "تعداد");
        String invKey = resolveFlexible(inv, "shka", "SHKA", "KalaCode", "code");
        String invName = resolveFlexible(inv, "naka", "Name", "KalaName", "item_name", "product_name", "نام_کالا");
        String itemName;
        if (detailName != null && invName != null && invKey != null && key != null) itemName = "COALESCE(NULLIF(TRY_CONVERT(nvarchar(500),dd.[" + detailName + "]),N''),TRY_CONVERT(nvarchar(500),i.[" + invName + "]),N'بدون نام')";
        else if (detailName != null) itemName = "COALESCE(NULLIF(TRY_CONVERT(nvarchar(500),dd.[" + detailName + "]),N''),N'بدون نام')";
        else if (invName != null && invKey != null && key != null) itemName = "COALESCE(TRY_CONVERT(nvarchar(500),i.[" + invName + "]),N'بدون نام')";
        else itemName = "N'بدون نام'";
        String joinInv = (invKey == null || key == null) ? "" : " LEFT JOIN dbo.inventory i ON TRY_CONVERT(nvarchar(100),i.[" + invKey + "])=TRY_CONVERT(nvarchar(100),dd.[" + key + "]) ";
        String productCode = key == null ? itemName : ((invKey == null) ? "TRY_CONVERT(nvarchar(100),dd.[" + key + "])" : "COALESCE(TRY_CONVERT(nvarchar(100),i.[" + invKey + "]),TRY_CONVERT(nvarchar(100),dd.[" + key + "]))");
        String groupId = resolveFlexible(inv, "group_rdf", "GroupID", "VarietyID", "variety_rdf", "groupid");
        String groupKey = resolveFlexible(grp, "group_rdf", "ID", "GroupID", "rdf", "code");
        String groupName = resolveFlexible(grp, "group_name", "name", "Name", "GroupName", "nagr", "gname");
        String joinGroup = "";
        String groupExpr = "N'بدون گروه'";
        if (key != null && invKey != null && groupId != null && groupKey != null && groupName != null) {
            joinGroup = " LEFT JOIN dbo.kagroup g ON TRY_CONVERT(nvarchar(100),g.[" + groupKey + "])=TRY_CONVERT(nvarchar(100),i.[" + groupId + "]) ";
            groupExpr = "COALESCE(TRY_CONVERT(nvarchar(150),g.[" + groupName + "]),N'بدون گروه')";
        }
        String tedvah = hasCol(d, "TEDVAH") ? "ISNULL(TRY_CONVERT(decimal(19,4),dd.TEDVAH),0)" : "0";
        String tedjoz = hasCol(d, "TEDJOZ") ? "ISNULL(TRY_CONVERT(decimal(19,4),dd.TEDJOZ),0)" : "0";
        String qtyEach;
        if (qtyCol != null) qtyEach = "ISNULL(TRY_CONVERT(decimal(19,4),dd.[" + qtyCol + "]),0)";
        else if (hasCol(d, "TEDVAH") || hasCol(d, "TEDJOZ")) qtyEach = (invKey != null && hasCol(inv, "mohvah") && key != null) ? "(" + tedvah + "*ISNULL(TRY_CONVERT(decimal(19,4),i.mohvah),1)+" + tedjoz + ")" : "(" + tedvah + "+" + tedjoz + ")";
        else qtyEach = "1";
        String qty = "ISNULL(SUM(" + qtyEach + "),0)";
        String amountExpr;
        if (lineAmount != null) amountExpr = "ISNULL(SUM(TRY_CONVERT(decimal(19,2),dd.[" + lineAmount + "])),0)";
        else if (unitPrice != null) amountExpr = "ISNULL(SUM(" + qtyEach + "*ISNULL(TRY_CONVERT(decimal(19,2),dd.[" + unitPrice + "]),0)),0)";
        else amountExpr = "CAST(0 AS decimal(19,2))";
        boolean canJoinHeader = numberCol != null && detailNumber != null && dateCol != null;
        String from = " FROM dbo.[" + detailTable + "] dd";
        String dateExpr;
        if (canJoinHeader) {
            from += " JOIN dbo.[" + header + "] h ON TRY_CONVERT(nvarchar(100),h.[" + numberCol + "])=TRY_CONVERT(nvarchar(100),dd.[" + detailNumber + "])";
            dateExpr = "h.[" + dateCol + "]";
        } else if (detailDate != null) {
            dateExpr = "dd.[" + detailDate + "]";
        } else return arr;
        from += joinInv + joinGroup;
        String where = " WHERE (TRY_CONVERT(nvarchar(30)," + dateExpr + ")=? OR LEFT(TRY_CONVERT(nvarchar(30)," + dateExpr + "),10)=LEFT(?,10))";
        if (canJoinHeader) where += activeAnd(h, "h");
        List<Object> params = new ArrayList<>(); params.add(date.trim()); params.add(date.trim());
        int limit = Math.max(1, Math.min(180, top));
        String sql = "SELECT TOP (" + limit + ") " + productCode + ", " + itemName + ", " + qty + ", " + amountExpr + ", " + groupExpr + from + where + " GROUP BY " + productCode + "," + itemName + "," + groupExpr + " ORDER BY 4 DESC, 3 DESC";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    JSONObject o = new JSONObject();
                    o.put("code", stringOr(r.getString(1), ""));
                    o.put("item", stringOr(r.getString(2), "بدون نام"));
                    o.put("quantity", r.getDouble(3));
                    o.put("qty", r.getDouble(3));
                    o.put("amount", r.getDouble(4));
                    o.put("group", stringOr(r.getString(5), "بدون گروه"));
                    o.put("hint", stringOr(r.getString(5), "بدون گروه") + " • مقدار " + formatNumber(r.getDouble(3)));
                    o.put("value", r.getDouble(4));
                    arr.put(o);
                }
            }
        }
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
        if (t.contains("مشتری")) return "♙";
        if (t.contains("کالا")) return "◍";
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

    private String currentAccountName() {
        String u = prefs == null ? "" : prefs.getString(KEY_LAST_USER, "");
        if (u == null || u.trim().isEmpty()) u = session == null ? "" : session.userName;
        return u == null || u.trim().isEmpty() ? "user" : u.trim();
    }

    private boolean isAdminUser() {
        return identityLooksAdmin(currentAccountName(), session == null ? "" : session.userName);
    }

    private boolean isFullAccessUser() {
        String role = currentAccessRole();
        return isAdminUser() || "admin".equals(role) || "manager".equals(role);
    }

    private String currentAccessRole() {
        if (session != null && session.accessRole != null && !session.accessRole.trim().isEmpty()) {
            String role = canonicalAccessRole(session.accessRole);
            if (!"user".equals(role)) return role;
        }
        return resolveHeuristicAccessRole(currentAccountName(), session == null ? "" : session.userName, session == null ? null : session.visitorId);
    }

    private Set<String> currentPermissionSet() {
        if (isFullAccessUser()) return permissionSet(allPermissionString());
        String raw = session == null ? "" : session.permissions;
        if (raw == null || raw.trim().isEmpty()) raw = defaultPermissionString(currentAccessRole());
        return permissionSet(raw);
    }

    private boolean canUsePermission(String key) {
        if (key == null || key.trim().isEmpty()) return true;
        if (isFullAccessUser()) return true;
        return currentPermissionSet().contains(key);
    }

    private String pagePermissionKey(String page) {
        if ("health".equals(page)) return "connection_health";
        if ("management".equals(page)) return "management_access";
        if ("settings".equals(page)) return "settings";
        return page == null ? "" : page;
    }

    private String resolveHeuristicAccessRole(String login, String display, Integer visitorId) {
        if (identityLooksAdmin(login, display)) return "admin";
        if (identityLooksSenior(login, display)) return "senior";
        String n = normalizeIdentity((login == null ? "" : login) + " " + (display == null ? "" : display));
        if (n.contains("پخش") || n.contains("distribut")) return "distributor";
        if (n.contains("راننده") || n.contains("driver")) return "driver";
        if (n.contains("کارگر") || n.contains("worker")) return "worker";
        if (visitorId != null && visitorId > 0) return "visitor";
        return "user";
    }

    private String normalizeIdentity(String value) {
        if (value == null) return "";
        return value.toLowerCase(Locale.US).replace('ي', 'ی').replace('ك', 'ک').replace('ة', 'ه').replace("‌", " ").trim();
    }

    private boolean identityLooksAdmin(String login, String display) {
        String rawLogin = login == null ? "" : login.trim();
        if ("admin".equalsIgnoreCase(rawLogin) || "administrator".equalsIgnoreCase(rawLogin)) return true;
        String n = normalizeIdentity((login == null ? "" : login) + " " + (display == null ? "" : display));
        String compact = n.replace(" ", "");
        return compact.equals("مدیر") || compact.equals("مدير") || compact.contains("مدیرکل") || compact.contains("مديرکل") || compact.contains("modir") || compact.contains("manager");
    }

    private boolean identityLooksSenior(String login, String display) {
        String n = normalizeIdentity((login == null ? "" : login) + " " + (display == null ? "" : display));
        String compact = n.replace(" ", "");
        return (compact.contains("شادی") && compact.contains("نظری")) || compact.contains("shadinazari") || compact.contains("shadinazary") || compact.contains("shadynazari") || compact.contains("senior") || compact.contains("supervisor") || compact.contains("کاربرارشد") || compact.contains("ارشد");
    }

    private String canonicalAccessRole(String value) {
        String v = normalizeIdentity(value);
        if (v.isEmpty()) return "";
        if (v.contains("admin") || v.contains("administrator") || v.contains("مدیرکل") || v.contains("مديرکل")) return "admin";
        if (v.contains("manager") || v.contains("مدیر") || v.contains("مدير") || v.contains("modir")) return "manager";
        if (v.contains("senior") || v.contains("supervisor") || v.contains("ارشد")) return "senior";
        if (v.contains("senior_accountant") || v.contains("حسابدار ارشد") || v.contains("حسابدارارشد") || v.contains("senior accountant")) return "senior_accountant";
        if (v.contains("accountant") || v.contains("حسابدار")) return "accountant";
        if (v.contains("warehouse") || v.contains("انباردار") || v.contains("انبار")) return "warehouse";
        if (v.contains("collections") || v.contains("مطالبات") || v.contains("وصول")) return "collections";
        if (v.contains("sales_employee") || v.contains("کارمند بخش فروش") || v.contains("کارمندفروش") || v.contains("sales")) return "sales_employee";
        if (v.contains("employee") || v.contains("کارمند")) return "employee";
        if (v.contains("distributor") || v.contains("distribution") || v.contains("پخش") || v.contains("مامور پخش") || v.contains("مأمور پخش")) return "distributor";
        if (v.contains("driver") || v.contains("راننده")) return "driver";
        if (v.contains("worker") || v.contains("کارگر")) return "worker";
        if (v.contains("visitor") || v.contains("ویزیت") || v.contains("ويزيت") || v.contains("بازاریاب")) return "visitor";
        return "user";
    }

    private String accessRoleLabel(String role) {
        role = canonicalAccessRole(role);
        if ("admin".equals(role)) return "مدیر اصلی";
        if ("manager".equals(role)) return "مدیر";
        if ("senior".equals(role)) return "کاربر ارشد";
        if ("visitor".equals(role)) return "ویزیتور";
        if ("distributor".equals(role)) return "مامور پخش";
        if ("driver".equals(role)) return "راننده";
        if ("worker".equals(role)) return "کارگر";
        if ("warehouse".equals(role)) return "انباردار";
        if ("senior_accountant".equals(role)) return "حسابدار ارشد";
        if ("accountant".equals(role)) return "حسابدار";
        if ("employee".equals(role)) return "کارمند";
        if ("collections".equals(role)) return "مامور مطالبات";
        if ("sales_employee".equals(role)) return "کارمند بخش فروش";
        return "کاربر محدود";
    }

    private String[][] roleCatalog() {
        return new String[][]{
                {"admin", "مدیر اصلی / Admin"}, {"manager", "مدیر"}, {"senior", "کاربر ارشد"},
                {"visitor", "ویزیتور"}, {"distributor", "مامور پخش"}, {"driver", "راننده"},
                {"worker", "کارگر"}, {"warehouse", "انباردار"}, {"senior_accountant", "حسابدار ارشد"},
                {"accountant", "حسابدار"}, {"employee", "کارمند"}, {"collections", "مامور مطالبات"},
                {"sales_employee", "کارمند بخش فروش"}, {"user", "کاربر محدود"}
        };
    }

    private String[][] permissionCatalog() {
        return new String[][]{
                {"dashboard", "داشبورد", "اصلی"},
                {"visitor_dashboard", "داشبورد ویزیتور", "ویزیتور"},
                {"showcase", "ویترین محصولات", "ویزیتور"},
                {"cart", "سبد خرید", "ویزیتور"},
                {"cart_draft", "ذخیره پیش‌نویس سبد", "ویزیتور"},
                {"cart_submit", "ارسال پیش‌فاکتور", "ویزیتور"},
                {"cart_signature", "امضای مشتری", "ویزیتور"},
                {"customer_select", "انتخاب مشتری برای پیش‌فاکتور", "ویزیتور"},
                {"prefactor_list", "پیگیری پیش‌فاکتورهای من", "ویزیتور"},
                {"visit_route", "مسیر بازدید امروز", "ویزیتور"},
                {"cart_discount", "تخفیف و شرایط فروش", "ویزیتور"},
                {"cart_pdf", "PDF و اشتراک پیش‌فاکتور", "ویزیتور"},
                {"offline_queue", "صف ارسال آفلاین", "ویزیتور"},
                {"day_report", "گزارش پایان روز ویزیتور", "ویزیتور"},
                {"customers", "مشتریان", "اصلی"},
                {"customer_detail", "جزئیات و گردش مشتری", "مشتریان"},
                {"customer_call", "تماس سریع با مشتری", "مشتریان"},
                {"customer_message", "پیام آماده مشتری", "مشتریان"},
                {"products", "کالاها", "اصلی"},
                {"product_detail", "جزئیات کالا", "کالاها"},
                {"attendance", "حضور و غیاب", "پرسنلی"},
                {"attendance_self", "ثبت ورود/خروج خود کاربر", "پرسنلی"},
                {"leave_balance", "مشاهده مرخصی خود کاربر", "پرسنلی"},
                {"leave_request", "ارسال درخواست مرخصی", "پرسنلی"},
                {"attendance_admin", "مدیریت حضور و مرخصی همه", "پرسنلی"},
                {"personnel", "پرسنل", "مدیریتی"},
                {"reports", "گزارشات", "مدیریتی"},
                {"command", "فرماندهی", "مدیریتی"},
                {"assistant", "دستیار هوشمند", "هوشمند"},
                {"ai_settings", "تنظیم کلیدهای AI", "هوشمند"},
                {"chat", "گفتگو", "همکاری"},
                {"taxpayers", "مودیان", "سازمانی"},
                {"tax_settings", "تنظیمات سامانه مودیان", "سازمانی"},
                {"tax_send", "ارسال/گزارش مودیان", "سازمانی"},
                {"cameras", "دوربین", "سخت‌افزار"},
                {"alarm", "دزدگیر", "سخت‌افزار"},
                {"hardware_control", "فرمان سخت‌افزار امنیتی", "سخت‌افزار"},
                {"settings", "تنظیمات عمومی", "مدیریتی"},
                {"connection_health", "سلامت اتصال", "مدیریتی"},
                {"management_access", "مدیریت دسترسی کاربران", "مدیریتی"}
        };
    }

    private Set<String> permissionSet(String raw) {
        Set<String> out = new HashSet<>();
        if (raw == null) return out;
        for (String part : raw.split("[,;\\s]+")) {
            String p = part == null ? "" : part.trim();
            if (!p.isEmpty()) out.add(p);
        }
        return out;
    }

    private String permissionCsv(Set<String> set) {
        if (set == null || set.isEmpty()) return "";
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return join(list, ",");
    }

    private String allPermissionString() {
        Set<String> all = new HashSet<>();
        for (String[] p : permissionCatalog()) all.add(p[0]);
        return permissionCsv(all);
    }

    private String defaultPermissionString(String role) {
        role = canonicalAccessRole(role);
        Set<String> s = new HashSet<>();
        s.add("dashboard");
        s.add("attendance");
        s.add("attendance_self");
        s.add("leave_balance");
        s.add("leave_request");
        if ("admin".equals(role) || "manager".equals(role)) return allPermissionString();
        if ("senior".equals(role) || "visitor".equals(role) || "sales_employee".equals(role)) {
            s.add("visitor_dashboard"); s.add("showcase"); s.add("cart"); s.add("cart_draft"); s.add("cart_submit"); s.add("cart_signature"); s.add("customer_select");
            s.add("prefactor_list"); s.add("visit_route"); s.add("cart_discount"); s.add("cart_pdf"); s.add("offline_queue"); s.add("day_report");
            s.add("customers"); s.add("customer_detail"); s.add("customer_call"); s.add("customer_message");
            s.add("products"); s.add("product_detail");
        } else if ("distributor".equals(role)) {
            s.add("visitor_dashboard"); s.add("showcase"); s.add("cart"); s.add("cart_draft"); s.add("customer_select");
            s.add("prefactor_list"); s.add("visit_route"); s.add("offline_queue"); s.add("day_report");
            s.add("customers"); s.add("customer_detail"); s.add("customer_call"); s.add("products"); s.add("product_detail");
        } else if ("driver".equals(role)) {
            s.add("visitor_dashboard"); s.add("customers"); s.add("customer_call");
        } else if ("worker".equals(role)) {
            s.add("products");
        } else if ("warehouse".equals(role)) {
            s.add("products"); s.add("product_detail"); s.add("showcase");
        } else if ("senior_accountant".equals(role)) {
            s.add("customers"); s.add("customer_detail"); s.add("reports"); s.add("command"); s.add("taxpayers"); s.add("tax_send"); s.add("connection_health");
        } else if ("accountant".equals(role)) {
            s.add("customers"); s.add("customer_detail"); s.add("reports"); s.add("taxpayers");
        } else if ("employee".equals(role)) {
            s.add("customers"); s.add("products");
        } else if ("collections".equals(role)) {
            s.add("customers"); s.add("customer_detail"); s.add("customer_call"); s.add("customer_message"); s.add("reports");
        }
        return permissionCsv(s);
    }

    private boolean permissionEnabled(String raw, String key) {
        return permissionSet(raw).contains(key);
    }

    private UserSession withResolvedAccessRole(Connection c, UserSession base, String login) throws Exception {
        if (base == null) return null;
        Integer visitorId = base.visitorId;
        if (visitorId == null || visitorId <= 0) visitorId = resolveVisitorIdForAccount(c, login, base.userName, base.userId);
        AccessProfile profile = resolveAccessProfile(c, login, base.userName, base.userId, visitorId);
        if (!profile.enabled) throw new DbException("دسترسی این کاربر توسط مدیر غیرفعال شده است.");
        return new UserSession(base.userId, visitorId, base.userName, profile.role, profile.permissions);
    }

    private AccessProfile resolveAccessProfile(Connection c, String login, String display, Integer userId, Integer visitorId) {
        String role = resolveAccessRole(c, login, display, userId, visitorId);
        String permissions = defaultPermissionString(role);
        boolean enabled = true;
        try {
            if (c != null && tableExists(c, "meelano_access_users")) {
                try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) role_key, permissions, enabled FROM dbo.meelano_access_users WHERE LOWER(LTRIM(RTRIM(username)))=LOWER(LTRIM(RTRIM(?))) OR LOWER(LTRIM(RTRIM(display_name)))=LOWER(LTRIM(RTRIM(?))) ORDER BY updated_at DESC")) {
                    ps.setString(1, stringOr(login, ""));
                    ps.setString(2, stringOr(display, ""));
                    try (ResultSet r = ps.executeQuery()) {
                        if (r.next()) {
                            String storedRole = canonicalAccessRole(r.getString(1));
                            if (!storedRole.isEmpty()) { role = storedRole; permissions = defaultPermissionString(role); }
                            enabled = r.getBoolean(3);
                            String userPerms = stringOr(r.getString(2), "").trim();
                            if (!userPerms.isEmpty()) permissions = userPerms;
                        }
                    }
                }
            }
            if ((permissions == null || permissions.trim().isEmpty() || permissions.equals(defaultPermissionString(role))) && c != null && tableExists(c, "meelano_access_roles")) {
                try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) permissions FROM dbo.meelano_access_roles WHERE role_key=?")) {
                    ps.setString(1, role);
                    try (ResultSet r = ps.executeQuery()) { if (r.next() && r.getString(1) != null && !r.getString(1).trim().isEmpty()) permissions = r.getString(1).trim(); }
                }
            }
        } catch (Exception ignored) { }
        if (identityLooksAdmin(login, display)) { role = "admin"; permissions = allPermissionString(); enabled = true; }
        return new AccessProfile(role, permissions, enabled);
    }

    private Integer resolveVisitorIdForAccount(Connection c, String login, String display, Integer userId) {
        try {
            if (c == null || !tableExists(c, "visitors")) return null;
            Set<String> cols = columns(c, "visitors");
            String vid = resolveFlexible(cols, "vis_rdf", "rdf", "RDF", "ID", "id", "shvis");
            if (vid == null) return null;
            List<String> where = new ArrayList<>();
            List<Object> params = new ArrayList<>();
            String userIdCol = resolveFlexible(cols, "UserID", "user_id", "userid");
            if (userId != null && userId > 0 && userIdCol != null) { where.add("TRY_CONVERT(nvarchar(100),[" + userIdCol + "])=?"); params.add(String.valueOf(userId)); }
            String uname = resolveFlexible(cols, "Username", "username", "user_name", "login", "UserName");
            if (uname != null && login != null && !login.trim().isEmpty()) { where.add("LOWER(LTRIM(RTRIM(TRY_CONVERT(nvarchar(160),[" + uname + "]))))=LOWER(LTRIM(RTRIM(?)))"); params.add(login.trim()); }
            String name = resolveFlexible(cols, "vis_name", "name", "Name", "VisitorName", "moname");
            if (name != null && display != null && !display.trim().isEmpty()) { where.add("LOWER(LTRIM(RTRIM(TRY_CONVERT(nvarchar(220),[" + name + "]))))=LOWER(LTRIM(RTRIM(?)))"); params.add(display.trim()); }
            if (where.isEmpty()) return null;
            String sql = "SELECT TOP (1) TRY_CONVERT(int,[" + vid + "]) FROM dbo.visitors WHERE (" + join(where, " OR ") + ")" + activeAnd(cols, "") + " ORDER BY [" + vid + "]";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                setParams(ps, params);
                try (ResultSet r = ps.executeQuery()) { if (r.next() && r.getObject(1) != null) return r.getInt(1); }
            }
        } catch (Exception ignored) { }
        return null;
    }

    private String resolveAccessRole(Connection c, String login, String display, Integer userId, Integer visitorId) {
        try {
            if (identityLooksAdmin(login, display)) return "admin";
            String explicit = roleFromAccessTable(c, login, display);
            if (!explicit.isEmpty() && !"user".equals(explicit)) return explicit;
            explicit = roleFromFlexibleTable(c, "sys_users", "user_id", userId, new String[]{"role", "Role", "user_role", "access_role", "AccessRole", "semat", "سمت", "level", "AccessLevel"});
            if (!explicit.isEmpty() && !"user".equals(explicit)) return explicit;
            explicit = roleFromFlexibleTable(c, "visitors", "vis_rdf", visitorId, new String[]{"role", "Role", "vis_role", "access_role", "semat", "سمت", "level", "VisitorRole"});
            if (!explicit.isEmpty() && !"user".equals(explicit)) return explicit;
        } catch (Exception ignored) { }
        return resolveHeuristicAccessRole(login, display, visitorId);
    }

    private String roleFromAccessTable(Connection c, String login, String display) {
        try {
            if (c == null || !tableExists(c, "meelano_chat_members")) return "";
            try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) role FROM dbo.meelano_chat_members WHERE LOWER(LTRIM(RTRIM(username)))=LOWER(LTRIM(RTRIM(?))) OR LOWER(LTRIM(RTRIM(display_name)))=LOWER(LTRIM(RTRIM(?)))")) {
                ps.setString(1, stringOr(login, "")); ps.setString(2, stringOr(display, ""));
                try (ResultSet r = ps.executeQuery()) { if (r.next()) return canonicalAccessRole(r.getString(1)); }
            }
        } catch (Exception ignored) { }
        return "";
    }

    private String roleFromFlexibleTable(Connection c, String table, String idCandidate, Integer id, String[] roleCandidates) {
        try {
            if (c == null || id == null || id <= 0 || !tableExists(c, table)) return "";
            Set<String> cols = columns(c, table);
            String idCol = resolveFlexible(cols, idCandidate, "ID", "id", "UserID", "user_id", "rdf", "RDF");
            String roleCol = resolveFlexible(cols, roleCandidates);
            if (idCol == null || roleCol == null) return "";
            try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) TRY_CONVERT(nvarchar(160),[" + roleCol + "]) FROM dbo.[" + table + "] WHERE TRY_CONVERT(nvarchar(100),[" + idCol + "])=?")) {
                ps.setString(1, String.valueOf(id));
                try (ResultSet r = ps.executeQuery()) { if (r.next()) return canonicalAccessRole(r.getString(1)); }
            }
        } catch (Exception ignored) { }
        return "";
    }

    private Integer currentVisitorScopeId() {
        return session == null ? null : session.visitorId;
    }

    private boolean restrictCustomerData() {
        return !isFullAccessUser();
    }

    private String customerScopeCondition(Set<String> cols, String alias, List<Object> params) {
        if (!restrictCustomerData()) return "";
        Integer vid = currentVisitorScopeId();
        String vis = resolveFlexible(cols, "vis_rdf", "VisitorID", "visid", "visitor", "shvis");
        if (vid == null || vid <= 0 || vis == null) return "1=0";
        params.add(String.valueOf(vid));
        String p = alias == null || alias.trim().isEmpty() ? "" : alias.trim() + ".";
        return "TRY_CONVERT(nvarchar(100)," + p + "[" + vis + "])=?";
    }

    private String salesScopeCondition(Set<String> cols, String alias, List<Object> params) {
        if (!restrictCustomerData()) return "";
        Integer vid = currentVisitorScopeId();
        String vis = resolveFlexible(cols, "vis_rdf", "VisitorID", "visid", "visitor", "shvis");
        if (vid == null || vid <= 0 || vis == null) return "1=0";
        params.add(String.valueOf(vid));
        String p = alias == null || alias.trim().isEmpty() ? "" : alias.trim() + ".";
        return "TRY_CONVERT(nvarchar(100)," + p + "[" + vis + "])=?";
    }

    private boolean canOpenPage(String page) {
        if (page == null || page.trim().isEmpty() || "login".equals(page)) return true;
        return canUsePermission(pagePermissionKey(page));
    }

    private String sectionLabel(String page) {
        if ("dashboard".equals(page)) return "داشبورد";
        if ("customers".equals(page)) return "مشتریان";
        if ("products".equals(page)) return "کالاها";
        if ("reports".equals(page)) return "گزارشات";
        if ("command".equals(page)) return "فرماندهی";
        if ("assistant".equals(page)) return "دستیار";
        if ("chat".equals(page)) return "گفتگو";
        if ("personnel".equals(page)) return "پرسنل";
        if ("attendance".equals(page)) return "حضور و غیاب";
        if ("visitor_dashboard".equals(page)) return "داشبورد ویزیتور";
        if ("showcase".equals(page)) return "ویترین";
        if ("cart".equals(page)) return "سبد خرید";
        if ("taxpayers".equals(page)) return "مودیان";
        if ("cameras".equals(page)) return "دوربین";
        if ("alarm".equals(page)) return "دزدگیر";
        if ("settings".equals(page)) return "تنظیمات";
        if ("management".equals(page)) return "مدیریت";
        return page == null ? "بخش" : page;
    }

    private void clearUserScopedCaches() {
        dashboardCacheJson = ""; reportsCacheJson = ""; customersCacheJson = ""; productsCacheJson = "";
        customersCacheQuery = ""; customersCacheFilter = "all"; customersCacheAllRows = false; customersSortOrder = "smart";
        productsCacheQuery = ""; productsCacheFilter = "all";
        customerLedgerCache.clear(); taxCurrentInvoices = new JSONArray();
        if (prefs != null) prefs.edit().remove(KEY_CACHE_DASHBOARD).remove(KEY_CACHE_REPORTS).apply();
    }

    private void renderLockedSection(String page) {
        content.removeAllViews();
        String label = sectionLabel(page);
        addHero("بخش قفل‌شده", label + " برای نقش «" + accessRoleLabel(currentAccessRole()) + "» فعال نیست.");
        LinearLayout c = card();
        c.setGravity(Gravity.CENTER_HORIZONTAL);
        c.setBackground(gradient(new int[]{alpha(DANGER, 26), alpha(GOLD, 18), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 28));
        c.addView(report3dIcon("🔒", DANGER), new LinearLayout.LayoutParams(dp(62), dp(62)));
        TextView title = text("دسترسی محدود", 17, TEXT, Typeface.BOLD); title.setGravity(Gravity.CENTER); c.addView(title, new LinearLayout.LayoutParams(-1, -2));
        TextView body = text("این حساب با نام کاربری/رمز خودش وارد شده و فقط بخش‌های مجاز نقش خود را می‌بیند. مدیر اصلی یا نقش مدیر می‌تواند همه آیتم‌ها و تنظیمات مدیریتی را باز کند.", 11.1f, MUTED, Typeface.NORMAL);
        body.setGravity(Gravity.CENTER); body.setLineSpacing(dp(2), 1.05f);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); bp.setMargins(0, dp(8), 0, dp(10)); c.addView(body, bp);
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button dash = primaryButton("داشبورد"); dash.setOnClickListener(v -> showApp("dashboard")); row.addView(dash, weightedButtonLp());
        Button attendance = secondaryButton("حضور من"); attendance.setOnClickListener(v -> showApp("attendance")); row.addView(attendance, weightedButtonLp());
        if (canOpenPage("customers")) { Button customers = secondaryButton("مشتریان"); customers.setOnClickListener(v -> showApp("customers")); row.addView(customers, weightedButtonLp()); }
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private String sqlText(String v) {
        return v == null ? "" : v.replace("'", "''");
    }

    private void ensureMeelanoCollabTables(Connection c) throws Exception {
        try (Statement st = c.createStatement()) {
            st.execute("IF OBJECT_ID(N'dbo.meelano_chat_members',N'U') IS NULL CREATE TABLE dbo.meelano_chat_members (username nvarchar(120) NOT NULL PRIMARY KEY, display_name nvarchar(220) NULL, role nvarchar(30) NOT NULL DEFAULT N'user', kicked bit NOT NULL DEFAULT 0, muted_until datetime2 NULL, last_seen datetime2 NULL)");
            st.execute("IF OBJECT_ID(N'dbo.meelano_chat_settings',N'U') IS NULL CREATE TABLE dbo.meelano_chat_settings (setting_key nvarchar(80) NOT NULL PRIMARY KEY, setting_value nvarchar(max) NULL, updated_at datetime2 NOT NULL DEFAULT SYSDATETIME())");
            st.execute("IF OBJECT_ID(N'dbo.meelano_chat_messages',N'U') IS NULL CREATE TABLE dbo.meelano_chat_messages (id bigint IDENTITY(1,1) NOT NULL PRIMARY KEY, sender nvarchar(120) NOT NULL, display_name nvarchar(220) NULL, kind nvarchar(30) NOT NULL DEFAULT N'text', body nvarchar(max) NULL, attachment_name nvarchar(260) NULL, attachment_mime nvarchar(160) NULL, attachment_data varbinary(max) NULL, pinned bit NOT NULL DEFAULT 0, scheduled_at datetime2 NULL, created_at datetime2 NOT NULL DEFAULT SYSDATETIME(), deleted bit NOT NULL DEFAULT 0)");
            st.execute("IF OBJECT_ID(N'dbo.meelano_attendance',N'U') IS NULL CREATE TABLE dbo.meelano_attendance (id bigint IDENTITY(1,1) NOT NULL PRIMARY KEY, username nvarchar(120) NOT NULL, display_name nvarchar(220) NULL, event_type nvarchar(20) NOT NULL, event_time datetime2 NOT NULL DEFAULT SYSDATETIME(), wifi_ssid nvarchar(200) NULL, wifi_bssid nvarchar(100) NULL, gateway nvarchar(80) NULL, note nvarchar(500) NULL)");
            st.execute("IF OBJECT_ID(N'dbo.meelano_leave_requests',N'U') IS NULL CREATE TABLE dbo.meelano_leave_requests (id bigint IDENTITY(1,1) NOT NULL PRIMARY KEY, username nvarchar(120) NOT NULL, display_name nvarchar(220) NULL, leave_type nvarchar(80) NOT NULL, start_date nvarchar(30) NOT NULL, end_date nvarchar(30) NOT NULL, hours nvarchar(40) NULL, reason nvarchar(700) NULL, status nvarchar(30) NOT NULL DEFAULT N'pending', manager_note nvarchar(700) NULL, created_at datetime2 NOT NULL DEFAULT SYSDATETIME(), decided_at datetime2 NULL)");
            st.execute("IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_meelano_chat_messages_created' AND object_id=OBJECT_ID(N'dbo.meelano_chat_messages')) CREATE INDEX IX_meelano_chat_messages_created ON dbo.meelano_chat_messages(deleted,pinned,created_at DESC)");
            st.execute("IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_meelano_attendance_user_time' AND object_id=OBJECT_ID(N'dbo.meelano_attendance')) CREATE INDEX IX_meelano_attendance_user_time ON dbo.meelano_attendance(username,event_time DESC)");
            st.execute("IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_meelano_leave_status' AND object_id=OBJECT_ID(N'dbo.meelano_leave_requests')) CREATE INDEX IX_meelano_leave_status ON dbo.meelano_leave_requests(status,created_at DESC)");
        }
        upsertCollabMember(c);
    }

    private void upsertCollabMember(Connection c) throws Exception {
        String username = currentAccountName();
        String display = session == null ? username : stringOr(session.userName, username);
        String role = isAdminUser() ? "admin" : "user";
        String sql = "IF EXISTS (SELECT 1 FROM dbo.meelano_chat_members WHERE username=?) " +
                "UPDATE dbo.meelano_chat_members SET display_name=?, role=CASE WHEN role=N'user' AND ?=N'admin' THEN N'admin' ELSE role END, last_seen=SYSDATETIME() WHERE username=? " +
                "ELSE INSERT INTO dbo.meelano_chat_members(username,display_name,role,kicked,last_seen) VALUES(?,?,?,0,SYSDATETIME())";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username); ps.setString(2, display); ps.setString(3, role); ps.setString(4, username);
            ps.setString(5, username); ps.setString(6, display); ps.setString(7, role); ps.executeUpdate();
        }
    }

    private String chatSetting(Connection c, String key, String fallback) {
        try (PreparedStatement ps = c.prepareStatement("SELECT setting_value FROM dbo.meelano_chat_settings WHERE setting_key=?")) {
            ps.setString(1, key);
            try (ResultSet r = ps.executeQuery()) { if (r.next()) return stringOr(r.getString(1), fallback); }
        } catch (Exception ignored) { }
        return fallback;
    }

    private void setChatSetting(Connection c, String key, String value) throws Exception {
        try (PreparedStatement ps = c.prepareStatement("IF EXISTS (SELECT 1 FROM dbo.meelano_chat_settings WHERE setting_key=?) UPDATE dbo.meelano_chat_settings SET setting_value=?, updated_at=SYSDATETIME() WHERE setting_key=? ELSE INSERT INTO dbo.meelano_chat_settings(setting_key,setting_value) VALUES(?,?)")) {
            ps.setString(1, key); ps.setString(2, value); ps.setString(3, key); ps.setString(4, key); ps.setString(5, value); ps.executeUpdate();
        }
    }

    private String loadChatRoomSql() throws Exception {
        try (Connection c = openConnection()) {
            ensureMeelanoCollabTables(c);
            String username = currentAccountName();
            JSONObject out = new JSONObject();
            out.put("username", username);
            out.put("display", session == null ? username : session.userName);
            out.put("admin", isAdminUser() || "admin".equals(chatRole(c, username)) || "manager".equals(chatRole(c, username)));
            out.put("closed", "1".equals(chatSetting(c, "chat_closed", "0")));
            out.put("silentUntil", chatSetting(c, "silent_until", ""));
            out.put("scheduleDelay", prefs == null ? 0 : prefs.getInt("chat_schedule_delay", 0));
            out.put("members", queryChatMembers(c));
            out.put("kicked", isChatKicked(c, username));
            out.put("muted", isChatMuted(c, username));
            JSONArray msgs = new JSONArray();
            String sql = "SELECT TOP (200) id,sender,display_name,kind,body,attachment_name,attachment_mime,pinned,CONVERT(nvarchar(19),created_at,120),CASE WHEN attachment_data IS NULL THEN 0 ELSE DATALENGTH(attachment_data) END FROM dbo.meelano_chat_messages WHERE deleted=0 AND (scheduled_at IS NULL OR scheduled_at<=SYSDATETIME()) ORDER BY pinned DESC, created_at DESC, id DESC";
            try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    JSONObject m = new JSONObject();
                    m.put("id", r.getLong(1)); m.put("sender", stringOr(r.getString(2), "")); m.put("display", stringOr(r.getString(3), r.getString(2)));
                    m.put("kind", stringOr(r.getString(4), "text")); m.put("body", stringOr(r.getString(5), "")); m.put("file", stringOr(r.getString(6), ""));
                    m.put("mime", stringOr(r.getString(7), "")); m.put("pinned", r.getBoolean(8)); m.put("time", stringOr(r.getString(9), "")); m.put("bytes", r.getLong(10)); msgs.put(m);
                }
            }
            out.put("messages", msgs);
            return out.toString();
        }
    }

    private String chatRole(Connection c, String username) {
        try (PreparedStatement ps = c.prepareStatement("SELECT role FROM dbo.meelano_chat_members WHERE username=?")) {
            ps.setString(1, username);
            try (ResultSet r = ps.executeQuery()) { if (r.next()) return stringOr(r.getString(1), "user"); }
        } catch (Exception ignored) { }
        return isAdminUser() ? "admin" : "user";
    }

    private boolean isChatKicked(Connection c, String username) {
        try (PreparedStatement ps = c.prepareStatement("SELECT kicked FROM dbo.meelano_chat_members WHERE username=?")) {
            ps.setString(1, username);
            try (ResultSet r = ps.executeQuery()) { return r.next() && r.getBoolean(1); }
        } catch (Exception ignored) { return false; }
    }

    private boolean isChatMuted(Connection c, String username) {
        try (PreparedStatement ps = c.prepareStatement("SELECT CASE WHEN muted_until IS NOT NULL AND muted_until>SYSDATETIME() THEN 1 ELSE 0 END FROM dbo.meelano_chat_members WHERE username=?")) {
            ps.setString(1, username);
            try (ResultSet r = ps.executeQuery()) { return r.next() && r.getInt(1) == 1; }
        } catch (Exception ignored) { return false; }
    }

    private JSONArray queryChatMembers(Connection c) throws Exception {
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement("SELECT TOP (80) username,display_name,role,kicked,CONVERT(nvarchar(19),last_seen,120) FROM dbo.meelano_chat_members ORDER BY last_seen DESC")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    JSONObject o = new JSONObject(); o.put("username", stringOr(r.getString(1), "")); o.put("display", stringOr(r.getString(2), r.getString(1))); o.put("role", stringOr(r.getString(3), "user")); o.put("kicked", r.getBoolean(4)); o.put("last", stringOr(r.getString(5), "")); arr.put(o);
                }
            }
        }
        return arr;
    }

    private void loadChatRoom() {
        content.removeAllViews();
        addHero("گفتگو", "چت‌روم گروهی مدیر و کارکنان؛ بدون چت خصوصی بین کاربران");
        addManualRefreshPanel("chat", "بروزرسانی گفتگو", "پیام‌ها از اتاق گفتگوی مشترک خوانده می‌شوند", () -> loadChatRoom());
        addLoading(content, "در حال دریافت گفتگو…");
        runDb(this::loadChatRoomSql, new DbCallback() {
            @Override public void ok(String body) { try { renderChatRoom(new JSONObject(body)); } catch (Exception e) { showPageError("گفتگو", e, () -> loadChatRoom()); } }
            @Override public void fail(Exception e) { showPageError("گفتگو", e, () -> loadChatRoom()); }
        });
    }

    private void renderChatRoom(JSONObject state) {
        content.removeAllViews();
        addHero("گفتگو", "اتاق عمومی پرسنل Meelano؛ کاربران دیده می‌شوند اما گفتگوی خصوصی وجود ندارد.");
        addManualRefreshPanel("chat", "تازه‌سازی گفتگو", state.optBoolean("closed") ? "گفتگو موقتاً بسته است" : "ارسال گروهی فعال است", () -> loadChatRoom());
        boolean admin = state.optBoolean("admin");
        if (state.optBoolean("kicked")) { addEmptyTo(content, "دسترسی شما به اتاق گفتگو توسط مدیر بسته شده است."); return; }
        notifyNewChatMessages(state.optJSONArray("messages"));
        if (admin) addChatAdminPanel(state);
        addChatMembersStrip(state.optJSONArray("members"));
        addSearchBox("جستجو در پیام، فرستنده یا فایل…", chatSearchQuery, q -> { chatSearchQuery = q == null ? "" : q.trim(); renderChatRoom(state); });
        JSONArray messages = filterChatMessages(state.optJSONArray("messages"), chatSearchQuery);
        if (messages == null || messages.length() == 0) addEmptyTo(content, chatSearchQuery == null || chatSearchQuery.trim().isEmpty() ? "هنوز پیامی در گفتگو ثبت نشده است." : "پیامی مطابق جستجو پیدا نشد.");
        else for (int i = 0; i < messages.length(); i++) addChatMessageCard(messages.optJSONObject(i), admin);
        addChatComposer(state);
    }

    private JSONArray filterChatMessages(JSONArray messages, String query) {
        if (messages == null) return new JSONArray();
        String q = query == null ? "" : query.trim().toLowerCase(Locale.US);
        if (q.isEmpty()) return messages;
        JSONArray out = new JSONArray();
        boolean pinOnly = q.contains("پین") || q.equals("pin") || q.equals("pinned");
        for (int i = 0; i < messages.length(); i++) {
            JSONObject m = messages.optJSONObject(i);
            if (m == null) continue;
            String hay = (m.optString("display", "") + " " + m.optString("sender", "") + " " + m.optString("body", "") + " " + m.optString("file", "") + " " + m.optString("kind", "")).toLowerCase(Locale.US);
            if ((pinOnly && m.optBoolean("pinned")) || hay.contains(q)) out.put(m);
        }
        return out;
    }

    private void notifyNewChatMessages(JSONArray messages) {
        if (messages == null || messages.length() == 0 || prefs == null) return;
        long max = 0; String sender = "";
        for (int i = 0; i < messages.length(); i++) {
            JSONObject m = messages.optJSONObject(i);
            if (m != null && m.optLong("id") > max) { max = m.optLong("id"); sender = m.optString("display", m.optString("sender", "")); }
        }
        long last = prefs.getLong("chat_last_seen_message_id", 0);
        if (last > 0 && max > last) showLocalNotification("پیام جدید گفتگو", "پیام تازه از " + stringOr(sender, "گفتگو") + " ثبت شد.", false);
        if (max > last) prefs.edit().putLong("chat_last_seen_message_id", max).apply();
    }

    private void addChatMembersStrip(JSONArray members) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(navAccent("chat"), 28), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("اعضای گفتگو", 14.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        if (members != null) {
            LinearLayout row = null;
            for (int i = 0; i < Math.min(6, members.length()); i++) {
                if (i % 3 == 0) { row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); c.addView(row, new LinearLayout.LayoutParams(-1, -2)); }
                JSONObject m = members.optJSONObject(i);
                TextView chip = text((m == null ? "کاربر" : m.optString("display", "کاربر")) + (m != null && "admin".equals(m.optString("role")) ? " ★" : ""), 9.2f, TEXT, Typeface.BOLD);
                chip.setGravity(Gravity.CENTER); chip.setSingleLine(true); chip.setEllipsize(TextUtils.TruncateAt.END);
                chip.setBackground(roundedStroke(alpha(navAccent("chat"), 18), 999, alpha(navAccent("chat"), 66)));
                LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(32), 1f); cp.setMargins(dp(3), dp(6), dp(3), 0);
                if (row != null) row.addView(chip, cp);
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addChatMessageCard(JSONObject m, boolean admin) {
        if (m == null) return;
        int accent = m.optBoolean("pinned") ? GOLD : navAccent("chat");
        LinearLayout c = card();
        c.setPadding(dp(12), dp(10), dp(12), dp(10));
        c.setBackground(gradient(new int[]{alpha(Color.WHITE, isLightTheme() ? 60 : 16), alpha(accent, m.optBoolean("pinned") ? 34 : 22), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 20));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        TextView avatar = report3dIcon(m.optBoolean("pinned") ? "⌖" : initials(m.optString("display", m.optString("sender", "ک"))), accent);
        head.addView(avatar, new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(9), 0, dp(8), 0);
        TextView sender = text(m.optString("display", m.optString("sender", "کاربر")), 12.4f, TEXT, Typeface.BOLD);
        sender.setSingleLine(true); sender.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(sender, new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text((m.optBoolean("pinned") ? "پیام پین‌شده • " : "") + m.optString("time", "") + " • #" + m.optLong("id"), 9.6f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        head.addView(pill(chatKindLabel(m.optString("kind", "text")), accent, false), new LinearLayout.LayoutParams(-2, -2));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));

        if (!m.optString("body", "").isEmpty()) {
            TextView body = text(m.optString("body", ""), 12.5f, TEXT, Typeface.NORMAL);
            body.setLineSpacing(dp(3), 1.08f);
            body.setPadding(dp(9), dp(8), dp(9), dp(8));
            body.setBackground(roundedStroke(alpha(accent, isLightTheme() ? 10 : 22), 16, alpha(accent, 45)));
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); bp.setMargins(0, dp(8), 0, 0); c.addView(body, bp);
        }
        if (!m.optString("file", "").isEmpty()) {
            LinearLayout file = new LinearLayout(this); file.setOrientation(LinearLayout.HORIZONTAL); file.setGravity(Gravity.CENTER_VERTICAL); file.setPadding(dp(8), dp(7), dp(8), dp(7));
            file.setBackground(roundedStroke(alpha(accent, 18), 16, alpha(accent, 62)));
            file.addView(report3dIcon(chatAttachmentGlyph(m.optString("kind", "file")), accent), new LinearLayout.LayoutParams(dp(42), dp(42)));
            LinearLayout fc = new LinearLayout(this); fc.setOrientation(LinearLayout.VERTICAL); fc.setPadding(dp(8), 0, dp(8), 0);
            TextView fn = text(m.optString("file"), 10.8f, TEXT, Typeface.BOLD); fn.setSingleLine(true); fn.setEllipsize(TextUtils.TruncateAt.END);
            fc.addView(fn, new LinearLayout.LayoutParams(-1, -2));
            fc.addView(text(compactBytes(m.optLong("bytes")) + " • لمس برای ذخیره/اشتراک", 9.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
            file.addView(fc, new LinearLayout.LayoutParams(0, -2, 1f));
            long msgId = m.optLong("id"); String fileName = m.optString("file", "attachment");
            file.setClickable(true); file.setOnClickListener(v -> saveChatAttachment(msgId, fileName)); applyTouchFeedback(file);
            LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, -2); fp.setMargins(0, dp(7), 0, 0); c.addView(file, fp);
        }
        if (admin) {
            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
            Button pin = secondaryButton(m.optBoolean("pinned") ? withIcon("⌖", "برداشتن پین") : withIcon("⌖", "پین")); pin.setTextSize(9.2f);
            Button del = secondaryButton(withIcon("×", "حذف")); del.setTextSize(9.2f);
            long id = m.optLong("id"); boolean nextPinned = !m.optBoolean("pinned");
            pin.setOnClickListener(v -> chatAdminMessageAction(id, nextPinned, false));
            del.setOnClickListener(v -> chatAdminMessageAction(id, false, true));
            row.addView(pin, weightedButtonLp()); row.addView(del, weightedButtonLp());
            LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(7), 0, 0); c.addView(row, rp);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(8)); content.addView(c, lp);
    }

    private String chatKindLabel(String kind) {
        if ("image".equals(kind)) return "عکس";
        if ("video".equals(kind)) return "ویدیو";
        if ("audio".equals(kind)) return "ویس";
        if ("file".equals(kind)) return "فایل";
        return "متن";
    }

    private String chatAttachmentGlyph(String kind) {
        if ("image".equals(kind)) return "▧";
        if ("video".equals(kind)) return "▶";
        if ("audio".equals(kind)) return "♪";
        return "□";
    }

    private String compactBytes(long b) {
        if (b <= 0) return "بدون حجم";
        if (b > 1024L * 1024L) return formatNumber(b / 1024d / 1024d) + " MB";
        return formatNumber(b / 1024d) + " KB";
    }

    private void addChatComposer(JSONObject state) {
        boolean admin = state.optBoolean("admin");
        boolean closed = state.optBoolean("closed");
        boolean muted = state.optBoolean("muted");
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(navAccent("chat"), 30), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 24));
        if (closed && !admin) { c.addView(text("گفتگو موقتاً توسط مدیر بسته شده است.", 12.5f, WARNING, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2)); content.addView(c, new LinearLayout.LayoutParams(-1, -2)); return; }
        if (muted && !admin) { c.addView(text("ارسال پیام شما موقتاً بی‌صدا/محدود شده است.", 12.5f, WARNING, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2)); content.addView(c, new LinearLayout.LayoutParams(-1, -2)); return; }
        EditText input = input("پیام گروهی…", "", false);
        input.setMinLines(2); input.setMaxLines(4); c.addView(input, new LinearLayout.LayoutParams(-1, dp(74)));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button send = primaryButton(state.optInt("scheduleDelay", 0) > 0 ? withIcon("⏱", "ارسال زمان‌دار") : withIcon("↗", "ارسال")); send.setTextSize(10.2f);
        Button sticker = secondaryButton(withIcon("✦", "استیکر")); sticker.setTextSize(10.2f);
        send.setOnClickListener(v -> sendChatText(input.getText().toString()));
        sticker.setOnClickListener(v -> sendChatText("😊✨"));
        row.addView(send, weightedButtonLp()); row.addView(sticker, weightedButtonLp()); c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        Button photo = secondaryButton(withIcon("▧", "عکس")); Button video = secondaryButton(withIcon("▶", "ویدیو")); Button audio = secondaryButton(withIcon("♪", "ویس")); Button file = secondaryButton(withIcon("□", "فایل"));
        photo.setTextSize(9.3f); video.setTextSize(9.3f); audio.setTextSize(9.3f); file.setTextSize(9.3f);
        photo.setOnClickListener(v -> pickChatAttachment("image", "image/*")); video.setOnClickListener(v -> pickChatAttachment("video", "video/*")); audio.setOnClickListener(v -> pickChatAttachment("audio", "audio/*")); file.setOnClickListener(v -> pickChatAttachment("file", "*/*"));
        row2.addView(photo, weightedButtonLp()); row2.addView(video, weightedButtonLp()); row2.addView(audio, weightedButtonLp()); row2.addView(file, weightedButtonLp());
        LinearLayout.LayoutParams r2p = new LinearLayout.LayoutParams(-1, -2); r2p.setMargins(0, dp(6), 0, 0); c.addView(row2, r2p);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(6), 0, dp(12)); content.addView(c, lp);
    }

    private void addChatAdminPanel(JSONObject state) {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(DANGER, 20), alpha(navAccent("chat"), 22), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("مدیریت گفتگو", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("بستن موقت، سکوت، پین پیام، زمان‌بندی ارسال، اخراج یا اعطای دسترسی مدیر/کارمند", 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout r1 = new LinearLayout(this); r1.setOrientation(LinearLayout.HORIZONTAL);
        Button close = secondaryButton(state.optBoolean("closed") ? withIcon("✓", "بازکردن گفتگو") : withIcon("×", "بستن گفتگو"));
        Button silent = secondaryButton(withIcon("◌", "بی‌صدا ۱ ساعت"));
        close.setTextSize(9.4f); silent.setTextSize(9.4f);
        close.setOnClickListener(v -> chatSetClosed(!state.optBoolean("closed")));
        silent.setOnClickListener(v -> chatSetSilentOneHour());
        r1.addView(close, weightedButtonLp()); r1.addView(silent, weightedButtonLp()); c.addView(r1, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout r2 = new LinearLayout(this); r2.setOrientation(LinearLayout.HORIZONTAL);
        Button schedule = secondaryButton(withIcon("⏱", "تنظیم زمان ارسال")); Button grant = secondaryButton(withIcon("ID", "مدیر/کارمند"));
        schedule.setTextSize(9.4f); grant.setTextSize(9.4f);
        schedule.setOnClickListener(v -> showChatScheduleDialog());
        grant.setOnClickListener(v -> showChatMemberAdminDialog(false));
        r2.addView(schedule, weightedButtonLp()); r2.addView(grant, weightedButtonLp()); LinearLayout.LayoutParams r2p = new LinearLayout.LayoutParams(-1, -2); r2p.setMargins(0, dp(6), 0, 0); c.addView(r2, r2p);
        LinearLayout r3 = new LinearLayout(this); r3.setOrientation(LinearLayout.HORIZONTAL);
        Button kick = secondaryButton(withIcon("↺", "اخراج/بازگردانی")); Button mute = secondaryButton(withIcon("◌", "سکوت کاربر"));
        kick.setTextSize(9.4f); mute.setTextSize(9.4f);
        kick.setOnClickListener(v -> showChatMemberAdminDialog(true));
        mute.setOnClickListener(v -> showChatMuteDialog());
        r3.addView(kick, weightedButtonLp()); r3.addView(mute, weightedButtonLp()); LinearLayout.LayoutParams r3p = new LinearLayout.LayoutParams(-1, -2); r3p.setMargins(0, dp(6), 0, 0); c.addView(r3, r3p);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void sendChatText(String text) {
        String body = text == null ? "" : text.trim(); if (body.isEmpty()) return;
        runDb(() -> { insertChatMessage("text", body, null, null, null); return "ok"; }, new DbCallback() { @Override public void ok(String b) { loadChatRoom(); } @Override public void fail(Exception e) { showPageError("ارسال گفتگو", e, () -> loadChatRoom()); } });
    }

    private void insertChatMessage(String kind, String body, String fileName, String mime, byte[] data) throws Exception {
        try (Connection c = openConnection()) {
            ensureMeelanoCollabTables(c);
            String username = currentAccountName();
            boolean admin = isAdminUser() || "admin".equals(chatRole(c, username)) || "manager".equals(chatRole(c, username));
            if (isChatKicked(c, username)) throw new DbException("دسترسی شما به گفتگو بسته شده است.");
            if (!admin && "1".equals(chatSetting(c, "chat_closed", "0"))) throw new DbException("گفتگو موقتاً توسط مدیر بسته شده است.");
            if (!admin && isChatMuted(c, username)) throw new DbException("ارسال پیام شما موقتاً محدود است.");
            int delay = prefs == null ? 0 : prefs.getInt("chat_schedule_delay", 0);
            String display = session == null ? username : stringOr(session.userName, username);
            String sql = delay > 0 ? "INSERT INTO dbo.meelano_chat_messages(sender,display_name,kind,body,attachment_name,attachment_mime,attachment_data,scheduled_at) VALUES(?,?,?,?,?,?,?,DATEADD(minute,?,SYSDATETIME()))" : "INSERT INTO dbo.meelano_chat_messages(sender,display_name,kind,body,attachment_name,attachment_mime,attachment_data) VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, username); ps.setString(2, display); ps.setString(3, kind == null ? "text" : kind); ps.setString(4, body); ps.setString(5, fileName); ps.setString(6, mime); ps.setBytes(7, data); if (delay > 0) ps.setInt(8, delay); ps.executeUpdate();
            }
            if (prefs != null && delay > 0) prefs.edit().putInt("chat_schedule_delay", 0).apply();
        }
    }

    private void pickChatAttachment(String kind, String mime) {
        pendingChatAttachmentKind = kind == null ? "file" : kind;
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(mime == null ? "*/*" : mime);
        try { startActivityForResult(i, REQ_CHAT_ATTACHMENT); }
        catch (Exception ex) { Toast.makeText(this, "انتخاب فایل روی این دستگاه در دسترس نیست.", Toast.LENGTH_SHORT).show(); }
    }

    private String displayNameForUri(Uri uri) {
        if (uri == null) return "attachment";
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) return stringOr(cursor.getString(idx), "attachment");
            }
        } catch (Exception ignored) { }
        return stringOr(uri.getLastPathSegment(), "attachment");
    }

    private byte[] readUriBytes(Uri uri) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream in = getContentResolver().openInputStream(uri)) {
            if (in == null) return new byte[0];
            byte[] buf = new byte[8192]; int n;
            while ((n = in.read(buf)) >= 0) out.write(buf, 0, n);
        }
        return out.toByteArray();
    }

    private void handleChatAttachment(Uri uri) {
        if (uri == null) return;
        String name = displayNameForUri(uri);
        String mime = getContentResolver().getType(uri);
        String kind = pendingChatAttachmentKind == null ? "file" : pendingChatAttachmentKind;
        executor.execute(() -> {
            try {
                insertChatAttachmentMessage(uri, kind, name, mime);
                runOnUiThread(() -> { Toast.makeText(this, "پیوست در گفتگو ارسال شد.", Toast.LENGTH_SHORT).show(); loadChatRoom(); });
            } catch (Exception ex) { runOnUiThread(() -> showPageError("ارسال پیوست", ex, () -> loadChatRoom())); }
        });
    }

    private long uriSize(Uri uri) {
        if (uri == null) return -1;
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (idx >= 0 && !cursor.isNull(idx)) return cursor.getLong(idx);
            }
        } catch (Exception ignored) { }
        return -1;
    }

    private void insertChatAttachmentMessage(Uri uri, String kind, String fileName, String mime) throws Exception {
        try (Connection c = openConnection()) {
            ensureMeelanoCollabTables(c);
            String username = currentAccountName();
            boolean admin = isAdminUser() || "admin".equals(chatRole(c, username)) || "manager".equals(chatRole(c, username));
            if (isChatKicked(c, username)) throw new DbException("دسترسی شما به گفتگو بسته شده است.");
            if (!admin && "1".equals(chatSetting(c, "chat_closed", "0"))) throw new DbException("گفتگو موقتاً توسط مدیر بسته شده است.");
            if (!admin && isChatMuted(c, username)) throw new DbException("ارسال پیام شما موقتاً محدود است.");
            int delay = prefs == null ? 0 : prefs.getInt("chat_schedule_delay", 0);
            String display = session == null ? username : stringOr(session.userName, username);
            String sql = delay > 0 ? "INSERT INTO dbo.meelano_chat_messages(sender,display_name,kind,body,attachment_name,attachment_mime,attachment_data,scheduled_at) VALUES(?,?,?,?,?,?,?,DATEADD(minute,?,SYSDATETIME()))" : "INSERT INTO dbo.meelano_chat_messages(sender,display_name,kind,body,attachment_name,attachment_mime,attachment_data) VALUES(?,?,?,?,?,?,?)";
            try (InputStream in = getContentResolver().openInputStream(uri); PreparedStatement ps = c.prepareStatement(sql)) {
                if (in == null) throw new DbException("فایل قابل خواندن نیست.");
                ps.setString(1, username); ps.setString(2, display); ps.setString(3, kind == null ? "file" : kind); ps.setString(4, ""); ps.setString(5, fileName); ps.setString(6, mime);
                long size = uriSize(uri);
                if (size >= 0) ps.setBinaryStream(7, in, size); else ps.setBinaryStream(7, in);
                if (delay > 0) ps.setInt(8, delay);
                ps.executeUpdate();
            }
            if (prefs != null && delay > 0) prefs.edit().putInt("chat_schedule_delay", 0).apply();
        }
    }

    private void saveChatAttachment(long id, String fileName) {
        executor.execute(() -> {
            try (Connection c = openConnection()) {
                ensureMeelanoCollabTables(c);
                File dir = getExternalFilesDir("chat"); if (dir == null) dir = getFilesDir();
                if (!dir.exists()) dir.mkdirs();
                String safe = fileName == null || fileName.trim().isEmpty() ? ("chat-" + id + ".bin") : fileName.replace('/', '_').replace('\\', '_');
                File out = new File(dir, safe);
                try (PreparedStatement ps = c.prepareStatement("SELECT attachment_data FROM dbo.meelano_chat_messages WHERE id=? AND deleted=0")) {
                    ps.setLong(1, id);
                    try (ResultSet r = ps.executeQuery()) {
                        if (!r.next()) throw new DbException("پیوست پیدا نشد.");
                        try (InputStream in = r.getBinaryStream(1); FileOutputStream fos = new FileOutputStream(out)) {
                            if (in == null) throw new DbException("این پیام پیوست ندارد.");
                            byte[] buf = new byte[8192]; int n;
                            while ((n = in.read(buf)) >= 0) fos.write(buf, 0, n);
                        }
                    }
                }
                runOnUiThread(() -> sharePlainText("پیوست گفتگو", "پیوست ذخیره شد:\n" + out.getAbsolutePath(), null));
            } catch (Exception ex) { runOnUiThread(() -> Toast.makeText(this, "ذخیره پیوست ممکن نشد: " + shortError(ex), Toast.LENGTH_LONG).show()); }
        });
    }

    private void showChatScheduleDialog() {
        EditText minutes = input("دقیقه تا ارسال", "15", false);
        minutes.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("زمان ارسال پیام بعدی")
                .setView(minutes)
                .setNegativeButton("لغو زمان‌بندی", (d, w) -> { if (prefs != null) prefs.edit().putInt("chat_schedule_delay", 0).apply(); Toast.makeText(this, "زمان‌بندی غیرفعال شد.", Toast.LENGTH_SHORT).show(); loadChatRoom(); })
                .setPositiveButton("ثبت", (d, w) -> {
                    int m = 0; try { m = Integer.parseInt(minutes.getText().toString().trim()); } catch (Exception ignored) { }
                    if (m < 0) m = 0; if (m > 1440) m = 1440;
                    if (prefs != null) prefs.edit().putInt("chat_schedule_delay", m).apply();
                    Toast.makeText(this, m == 0 ? "ارسال فوری فعال شد." : "پیام بعدی " + m + " دقیقه بعد ارسال می‌شود.", Toast.LENGTH_SHORT).show();
                    loadChatRoom();
                }).create();
        styleMeelanoDialog(dialog, navAccent("chat")); dialog.show();
    }

    private void chatSetClosed(boolean closed) { runDb(() -> { try (Connection c = openConnection()) { ensureMeelanoCollabTables(c); setChatSetting(c, "chat_closed", closed ? "1" : "0"); } return "ok"; }, new DbCallback() { @Override public void ok(String b) { loadChatRoom(); } @Override public void fail(Exception e) { showPageError("مدیریت گفتگو", e, () -> loadChatRoom()); } }); }
    private void chatSetSilentOneHour() { runDb(() -> { try (Connection c = openConnection()) { ensureMeelanoCollabTables(c); setChatSetting(c, "silent_until", nowText() + " +۱ ساعت"); } return "ok"; }, new DbCallback() { @Override public void ok(String b) { Toast.makeText(MainActivity.this, "گفتگو برای اعلان‌ها بی‌صدا شد.", Toast.LENGTH_SHORT).show(); loadChatRoom(); } @Override public void fail(Exception e) { showPageError("مدیریت گفتگو", e, () -> loadChatRoom()); } }); }

    private void chatAdminMessageAction(long id, boolean pin, boolean del) {
        runDb(() -> { try (Connection c = openConnection()) { ensureMeelanoCollabTables(c); try (PreparedStatement ps = c.prepareStatement(del ? "UPDATE dbo.meelano_chat_messages SET deleted=1 WHERE id=?" : "UPDATE dbo.meelano_chat_messages SET pinned=? WHERE id=?")) { if (del) ps.setLong(1, id); else { ps.setBoolean(1, pin); ps.setLong(2, id); } ps.executeUpdate(); } } return "ok"; }, new DbCallback() { @Override public void ok(String b) { loadChatRoom(); } @Override public void fail(Exception e) { showPageError("مدیریت پیام", e, () -> loadChatRoom()); } });
    }

    private void showChatMemberAdminDialog(boolean kickMode) {
        EditText user = input("نام کاربری", "", false);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(kickMode ? "اخراج یا بازگردانی کاربر" : "اعطای دسترسی").setView(user).setNegativeButton("بستن", null).setNeutralButton(kickMode ? "بازگردانی" : "کارمند", (d,w) -> chatMemberUpdate(user.getText().toString(), kickMode ? "restore" : "user")).setPositiveButton(kickMode ? "اخراج" : "مدیر", (d,w) -> chatMemberUpdate(user.getText().toString(), kickMode ? "kick" : "admin")).create();
        styleMeelanoDialog(dialog, navAccent("chat")); dialog.show();
    }

    private void showChatMuteDialog() {
        EditText user = input("نام کاربری برای سکوت ۲ ساعت", "", false);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("سکوت کاربر").setView(user).setNegativeButton("بستن", null).setPositiveButton("اعمال", (d,w) -> chatMemberUpdate(user.getText().toString(), "mute")).create();
        styleMeelanoDialog(dialog, navAccent("chat")); dialog.show();
    }

    private void chatMemberUpdate(String username, String action) {
        String u = username == null ? "" : username.trim(); if (u.isEmpty()) return;
        runDb(() -> {
            try (Connection c = openConnection()) {
                ensureMeelanoCollabTables(c);
                try (PreparedStatement ins = c.prepareStatement("IF NOT EXISTS (SELECT 1 FROM dbo.meelano_chat_members WHERE username=?) INSERT INTO dbo.meelano_chat_members(username,display_name,role,kicked,last_seen) VALUES(?,?,N'user',0,SYSDATETIME())")) {
                    ins.setString(1, u); ins.setString(2, u); ins.executeUpdate();
                }
                String sql;
                if ("kick".equals(action)) sql = "UPDATE dbo.meelano_chat_members SET kicked=1 WHERE username=?";
                else if ("restore".equals(action)) sql = "UPDATE dbo.meelano_chat_members SET kicked=0, muted_until=NULL WHERE username=?";
                else if ("mute".equals(action)) sql = "UPDATE dbo.meelano_chat_members SET muted_until=DATEADD(hour,2,SYSDATETIME()) WHERE username=?";
                else sql = "UPDATE dbo.meelano_chat_members SET role=? WHERE username=?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    if ("admin".equals(action) || "user".equals(action)) { ps.setString(1, action); ps.setString(2, u); }
                    else ps.setString(1, u);
                    ps.executeUpdate();
                }
            }
            return "ok";
        }, new DbCallback() { @Override public void ok(String b) { loadChatRoom(); } @Override public void fail(Exception e) { showPageError("مدیریت کاربر", e, () -> loadChatRoom()); } });
    }

    private void loadPersonnel() {
        content.removeAllViews(); addHero("پرسنل", "کارت حرفه‌ای پرسنل، مانده حساب و عملکرد لحظه‌ای"); addManualRefreshPanel("personnel", "بروزرسانی دستی پرسنل", "اطلاعات تا تازه‌سازی دستی ثابت می‌ماند", () -> loadPersonnel()); addLoading(content, "در حال دریافت پرسنل…");
        runDb(this::queryPersonnel, new DbCallback() { @Override public void ok(String body) { try { renderPersonnel(new JSONArray(body)); markRefresh("personnel"); } catch (Exception e) { showPageError("پرسنل", e, () -> loadPersonnel()); } } @Override public void fail(Exception e) { showPageError("پرسنل", e, () -> loadPersonnel()); } });
    }

    private String queryPersonnel() throws Exception {
        try (Connection c = openConnection()) {
            JSONArray arr = new JSONArray();
            try { appendVisitorsPersonnel(c, arr); } catch (Exception ignored) { }
            try { appendSysUsersPersonnel(c, arr); } catch (Exception ignored) { }
            if (arr.length() == 0 && session != null) {
                JSONObject me = new JSONObject();
                me.put("id", session.userId == null ? "" : String.valueOf(session.userId));
                me.put("name", stringOr(session.userName, currentAccountName()));
                me.put("username", currentAccountName());
                me.put("phone", ""); me.put("balance", 0); me.put("docs", 0); me.put("sales", 0); me.put("last", ""); me.put("source", "session");
                arr.put(me);
            }
            return arr.toString();
        }
    }

    private void appendVisitorsPersonnel(Connection c, JSONArray arr) throws Exception {
        if (!tableExists(c, "visitors")) return;
        Set<String> v = columns(c, "visitors"); Set<String> sail = columns(c, "sailfact");
        String id = resolve(v, "vis_rdf", "rdf", "RDF", "ID", "id", "shvis");
        if (id == null) return;
        String name = resolve(v, "vis_name", "name", "Name", "MONAME", "moname", "full_name");
        String username = resolve(v, "Username", "username", "user_name", "user", "login", "UserName");
        String phone = resolve(v, "mobile", "Mobile", "tel", "Tell", "phone", "Phone", "mobile_no");
        String balance = resolveFlexible(v, "man", "mande", "mandeh", "mande_hesab", "mande hesab", "balance", "Balance", "Mandeh", "hesab", "account_balance", "bed", "bes", "bedehkar", "bestankar", "bedehi", "مانده", "مانده_حساب");
        String nameExpr = name == null ? "TRY_CONVERT(nvarchar(220),v.[" + id + "])" : "TRY_CONVERT(nvarchar(220),v.[" + name + "])";
        String userExpr = username == null ? "CAST(NULL AS nvarchar(120))" : "TRY_CONVERT(nvarchar(120),v.[" + username + "])";
        String phoneExpr = phone == null ? "CAST(NULL AS nvarchar(120))" : "TRY_CONVERT(nvarchar(120),v.[" + phone + "])";
        String balExpr = balance == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(TRY_CONVERT(decimal(19,2),v.[" + balance + "]),0)";
        String apply = ""; String docCount="CAST(0 AS bigint)", saleTotal="CAST(0 AS decimal(19,2))", lastDate="CAST(NULL AS nvarchar(30))";
        String visitorRef = resolve(sail, "vis_rdf", "visitor", "visitor_id", "shvis");
        String amount = resolve(sail, "all", "amount", "total");
        if (visitorRef != null && amount != null) {
            String date = resolve(sail,"date", "DATE");
            apply = " OUTER APPLY (SELECT COUNT_BIG(1) doc_count, ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[" + amount + "])),0) sale_total" + (date == null ? ", CAST(NULL AS nvarchar(30)) last_date" : ", MAX(TRY_CONVERT(nvarchar(30),s.[" + date + "])) last_date") + " FROM dbo.sailfact s WHERE TRY_CONVERT(nvarchar(100),s.[" + visitorRef + "])=TRY_CONVERT(nvarchar(100),v.[" + id + "])) a";
            docCount="ISNULL(a.doc_count,0)"; saleTotal="ISNULL(a.sale_total,0)"; lastDate="a.last_date";
        }
        String sql = "SELECT TOP (160) TRY_CONVERT(nvarchar(100),v.[" + id + "]), " + nameExpr + ", " + userExpr + ", " + phoneExpr + ", " + balExpr + ", " + docCount + ", " + saleTotal + ", " + lastDate + " FROM dbo.visitors v" + apply + " ORDER BY " + nameExpr;
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < arr.length(); i++) { JSONObject o = arr.optJSONObject(i); if (o != null) seen.add(o.optString("username", "") + "|" + o.optString("id", "")); }
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) {
                String pid = stringOr(r.getString(1), ""); String u = stringOr(r.getString(3), ""); String key = u + "|" + pid; if (seen.contains(key)) continue; seen.add(key);
                double liveBalance = r.getDouble(5);
                if (Math.abs(liveBalance) <= 0.0001) liveBalance = computedPersonnelBalance(c, pid, u);
                JSONObject o = new JSONObject(); o.put("id", pid); o.put("name", stringOr(r.getString(2), "پرسنل")); o.put("username", u); o.put("phone", stringOr(r.getString(4), "")); o.put("balance", liveBalance); o.put("docs", r.getLong(6)); o.put("sales", r.getDouble(7)); o.put("last", stringOr(r.getString(8), "")); o.put("source", "visitors"); arr.put(o);
            }
        }
    }

    private void appendSysUsersPersonnel(Connection c, JSONArray arr) throws Exception {
        if (!tableExists(c, "sys_users")) return;
        Set<String> ucols = columns(c, "sys_users");
        String uid = resolveFlexible(ucols, "user_id", "UserID", "userid", "id", "ID");
        String uname = resolveFlexible(ucols, "user_name", "username", "Username", "UserName", "name", "Name");
        if (uid == null || uname == null) return;
        String phone = resolveFlexible(ucols, "mobile", "Mobile", "tel", "Tell", "phone", "Phone", "mobile_no");
        String balance = resolveFlexible(ucols, "man", "mande", "mandeh", "mande_hesab", "balance", "Balance", "Mandeh", "hesab", "account_balance", "bed", "bes", "bedehi", "مانده", "مانده_حساب");
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < arr.length(); i++) { JSONObject o = arr.optJSONObject(i); if (o != null) { seen.add(o.optString("username", "").toLowerCase(Locale.US)); seen.add(o.optString("id", "")); } }
        String phoneExpr = phone == null ? "CAST(NULL AS nvarchar(120))" : "TRY_CONVERT(nvarchar(120),[" + phone + "])";
        String balExpr = balance == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(TRY_CONVERT(decimal(19,2),[" + balance + "]),0)";
        String usql = "SELECT TOP (160) TRY_CONVERT(nvarchar(100),[" + uid + "]), TRY_CONVERT(nvarchar(220),[" + uname + "]), " + phoneExpr + ", " + balExpr + " FROM dbo.sys_users ORDER BY [" + uname + "]";
        try (PreparedStatement ps = c.prepareStatement(usql); ResultSet r = ps.executeQuery()) {
            while (r.next()) {
                String id = stringOr(r.getString(1), ""); String user = stringOr(r.getString(2), "");
                if (seen.contains(user.toLowerCase(Locale.US)) || seen.contains(id)) continue;
                double liveBalance = r.getDouble(4);
                if (Math.abs(liveBalance) <= 0.0001) liveBalance = computedPersonnelBalance(c, id, user);
                JSONObject o = new JSONObject(); o.put("id", id); o.put("name", stringOr(user, "پرسنل")); o.put("username", user); o.put("phone", stringOr(r.getString(3), "")); o.put("balance", liveBalance); o.put("docs", 0); o.put("sales", 0); o.put("last", ""); o.put("source", "sys_users"); arr.put(o);
            }
        }
    }

    private List<String> personnelVisitorIds(Connection c, String id, String username) {
        List<String> ids = new ArrayList<>();
        addUniqueValue(ids, id);
        try {
            if (c != null && tableExists(c, "sys_vis")) {
                Set<String> sv = columns(c, "sys_vis");
                String uid = resolveFlexible(sv, "UserID", "user_id", "userid", "user", "id_user");
                String shvis = resolveFlexible(sv, "shvis", "vis_rdf", "visitor", "visitor_id", "VisitorID");
                if (uid != null && shvis != null && id != null && !id.trim().isEmpty()) {
                    try (PreparedStatement ps = c.prepareStatement("SELECT TOP (5) TRY_CONVERT(nvarchar(100),[" + shvis + "]) FROM dbo.sys_vis WHERE TRY_CONVERT(nvarchar(100),[" + uid + "])=?")) {
                        ps.setString(1, id.trim());
                        try (ResultSet r = ps.executeQuery()) { while (r.next()) addUniqueValue(ids, r.getString(1)); }
                    }
                }
            }
        } catch (Exception ignored) { }
        try {
            if (c != null && tableExists(c, "visitors") && username != null && !username.trim().isEmpty()) {
                Set<String> v = columns(c, "visitors");
                String vid = resolveFlexible(v, "vis_rdf", "rdf", "RDF", "ID", "id", "shvis");
                String uname = resolveFlexible(v, "Username", "username", "user_name", "user", "login", "UserName");
                if (vid != null && uname != null) {
                    try (PreparedStatement ps = c.prepareStatement("SELECT TOP (5) TRY_CONVERT(nvarchar(100),[" + vid + "]) FROM dbo.visitors WHERE LOWER(LTRIM(RTRIM(TRY_CONVERT(nvarchar(160),[" + uname + "]))))=LOWER(LTRIM(RTRIM(?)))")) {
                        ps.setString(1, username.trim());
                        try (ResultSet r = ps.executeQuery()) { while (r.next()) addUniqueValue(ids, r.getString(1)); }
                    }
                }
            }
        } catch (Exception ignored) { }
        return ids;
    }

    private double computedPersonnelBalance(Connection c, String id, String username) {
        double balance = 0;
        try {
            if (c == null || !tableExists(c, "CUSTOMERS")) return 0;
            Set<String> cols = columns(c, "CUSTOMERS");
            String vis = resolveFlexible(cols, "vis_rdf", "VisitorID", "visid", "visitor", "shvis");
            String bal = resolveFlexible(cols, "man", "mande", "mandeh", "Balance", "Mandeh", "mande_hesab", "مانده", "مانده_حساب");
            if (vis == null || bal == null) return 0;
            List<String> visitorIds = personnelVisitorIds(c, id, username);
            Set<String> used = new HashSet<>();
            for (String vid : visitorIds) {
                if (vid == null || vid.trim().isEmpty() || used.contains(vid.trim())) continue;
                used.add(vid.trim());
                try (PreparedStatement ps = c.prepareStatement("SELECT ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + bal + "])),0) FROM dbo.CUSTOMERS WHERE TRY_CONVERT(nvarchar(100),[" + vis + "])=?")) {
                    ps.setString(1, vid.trim());
                    try (ResultSet r = ps.executeQuery()) { if (r.next()) balance += r.getDouble(1); }
                }
            }
        } catch (Exception ignored) { }
        if (Math.abs(balance) <= 0.0001) balance = personnelSalesRemainder(c, personnelVisitorIds(c, id, username));
        return balance;
    }

    private double personnelSalesRemainder(Connection c, List<String> visitorIds) {
        double total = 0;
        try {
            if (c == null || visitorIds == null || visitorIds.isEmpty() || !tableExists(c, "sailfact")) return 0;
            Set<String> sail = columns(c, "sailfact");
            String visitor = resolveFlexible(sail, "vis_rdf", "visitor", "visitor_id", "shvis", "VisitorID");
            String amount = resolveFlexible(sail, "all", "amount", "total", "Total", "mablagh", "مبلغ");
            if (visitor == null || amount == null) return 0;
            String paid = resolveFlexible(sail, "MabDaryaftFactor", "Daryaft", "received", "paid");
            String discount = resolveFlexible(sail, "tdf", "tafif", "takhfif", "discount");
            String expr = "ISNULL(TRY_CONVERT(decimal(19,2),[" + amount + "]),0)";
            if (paid != null) expr += "-ISNULL(TRY_CONVERT(decimal(19,2),[" + paid + "]),0)";
            if (discount != null) expr += "-ISNULL(TRY_CONVERT(decimal(19,2),[" + discount + "]),0)";
            Set<String> used = new HashSet<>();
            for (String vid : visitorIds) {
                if (vid == null || vid.trim().isEmpty() || used.contains(vid.trim())) continue;
                used.add(vid.trim());
                String sql = "SELECT ISNULL(SUM(CASE WHEN (" + expr + ")>0 THEN (" + expr + ") ELSE 0 END),0) FROM dbo.sailfact WHERE TRY_CONVERT(nvarchar(100),[" + visitor + "])=?" + activeAnd(sail, "");
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, vid.trim());
                    try (ResultSet r = ps.executeQuery()) { if (r.next()) total += r.getDouble(1); }
                }
            }
        } catch (Exception ignored) { }
        return total;
    }


    private void renderPersonnel(JSONArray rows) {
        content.removeAllViews();
        addHero("پرسنل", "مانده، فروش، آخرین فعالیت و گردش اختصاصی هر نفر");
        addManualRefreshPanel("personnel", "بروزرسانی دستی پرسنل", "آخرین بروزرسانی: " + lastRefreshText("personnel"), () -> loadPersonnel());
        if (isAdminUser()) addPersonnelAdminPanel();
        if (rows == null || rows.length() == 0) { addEmptyTo(content, "پرسنلی پیدا نشد؛ جدول visitors یا sys_users داده قابل نمایش ندارد."); return; }
        for (int i = 0; i < rows.length(); i++) {
            JSONObject r = rows.optJSONObject(i); if (r == null) continue;
            int accent = i % 3 == 0 ? navAccent("personnel") : (i % 3 == 1 ? INFO : GOLD);
            LinearLayout c = card();
            c.setBackground(gradient(new int[]{alpha(Color.WHITE, isLightTheme() ? 60 : 18), alpha(accent, isLightTheme() ? 24 : 36), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
            c.setClickable(true); c.setOnClickListener(v -> showPersonnelDetail(r)); applyTouchFeedback(c);

            LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
            TextView avatar = text(initials(r.optString("name", "پ")), 16.5f, onColorFor(accent), Typeface.BOLD);
            avatar.setGravity(Gravity.CENTER);
            avatar.setShadowLayer(dp(3), 0, dp(1), alpha(Color.BLACK, 130));
            avatar.setBackground(gradient(new int[]{mix(accent, Color.WHITE, 0.25f), accent, mix(GOLD_2, accent, 0.35f)}, GradientDrawable.Orientation.TL_BR, 999));
            head.addView(avatar, new LinearLayout.LayoutParams(dp(54), dp(54)));
            LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(8), 0);
            TextView name = text(r.optString("name", "پرسنل"), 15.5f, TEXT, Typeface.BOLD);
            name.setSingleLine(true); name.setEllipsize(TextUtils.TruncateAt.END);
            copy.addView(name, new LinearLayout.LayoutParams(-1, -2));
            copy.addView(text("نام کاربری: " + stringOr(r.optString("username"), "—") + " • کد: " + r.optString("id", "—"), 10.3f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
            head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
            head.addView(pill(r.optString("source", "پرسنل"), accent, false), new LinearLayout.LayoutParams(-2, -2));
            c.addView(head, new LinearLayout.LayoutParams(-1, -2));

            LinearLayout badges = new LinearLayout(this); badges.setOrientation(LinearLayout.HORIZONTAL);
            addCustomerTagChip(badges, stringOr(r.optString("last", ""), "فعالیت نامشخص"), INFO);
            addCustomerTagChip(badges, r.optString("phone", "").trim().isEmpty() ? "بدون تماس" : "تماس ثبت‌شده", r.optString("phone", "").trim().isEmpty() ? WARNING : SUCCESS);
            addCustomerTagChip(badges, "پرونده زنده", GOLD);
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); bp.setMargins(0, dp(8), 0, 0); c.addView(badges, bp);

            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
            row.addView(customerMiniMetric("مانده", money(r.opt("balance")), accent), weightedMiniLp());
            row.addView(customerMiniMetric("فروش", compactMoney(r.opt("sales")), GOLD), weightedMiniLp());
            row.addView(customerMiniMetric("فاکتور", formatNumber(r.opt("docs")), INFO), weightedMiniLp());
            LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(9), 0, 0); c.addView(row, rp);

            TextView more = pill(withIcon("☷", "مشاهده پرونده، حضور، مرخصی و گردش مالی"), accent, false);
            more.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, dp(38)); mp.setMargins(0, dp(9), 0, 0); c.addView(more, mp);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); content.addView(c, lp);
        }
    }

    private void addPersonnelAdminPanel() {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(navAccent("personnel"), 25), alpha(GOLD, 16), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 22));
        c.addView(text("مدیریت دسترسی پرسنل", 15, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("تعریف مدیر/کارمند گفتگو، سکوت یا بازگردانی کاربران از همین بخش انجام می‌شود.", 10.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1,-2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button role = secondaryButton("نقش گفتگو"); Button kick = secondaryButton(withIcon("↺", "اخراج/بازگردانی")); Button mute = secondaryButton(withIcon("◌", "سکوت کاربر"));
        role.setTextSize(9.5f); kick.setTextSize(9.5f); mute.setTextSize(9.5f);
        role.setOnClickListener(v -> showChatMemberAdminDialog(false)); kick.setOnClickListener(v -> showChatMemberAdminDialog(true)); mute.setOnClickListener(v -> showChatMuteDialog());
        row.addView(role, weightedButtonLp()); row.addView(kick, weightedButtonLp()); row.addView(mute, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0, dp(9), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c, lp);
    }

    private void showPersonnelDetail(JSONObject person) { showPersonnelDetail(person, "", ""); }

    private void showPersonnelDetail(JSONObject person, String fromDate, String toDate) {
        final String from = fromDate == null ? "" : fromDate.trim();
        final String to = toDate == null ? "" : toDate.trim();
        content.removeAllViews();
        addHero("پرونده پرسنلی", person.optString("name", "پرسنل") + (from.isEmpty() && to.isEmpty() ? "" : " • بازه " + stringOr(from, "ابتدا") + " تا " + stringOr(to, "امروز")));
        addPersonnelBackBar(person.optString("name", "پرسنل"));
        addPersonnelDateFilterCard(person, from, to);
        addLoading(content, "در حال دریافت پرونده تفکیک‌شده پرسنل…");
        runDb(() -> queryPersonnelDetail(person.optString("id", ""), person.optString("username", ""), from, to), new DbCallback(){
            @Override public void ok(String body){ try { renderPersonnelDetail(person, new JSONObject(body), from, to); } catch(Exception e){ showPageError("پرسنل", e, () -> showPersonnelDetail(person, from, to)); } }
            @Override public void fail(Exception e){ showPageError("پرسنل", e, () -> showPersonnelDetail(person, from, to)); }
        });
    }

    private void addPersonnelBackBar(String title) {
        LinearLayout bar = card();
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(10), dp(8), dp(10), dp(8));
        int accent = navAccent("personnel");
        bar.setBackground(gradient(new int[]{alpha(accent, isLightTheme() ? 22 : 34), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 20));
        Button back = secondaryButton(withIcon("↩", "بازگشت به پرسنل"));
        back.setTextSize(10.2f); back.setOnClickListener(v -> loadPersonnel());
        bar.addView(back, new LinearLayout.LayoutParams(dp(150), dp(42)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("پرونده تفکیک‌شده", 12.4f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text(stringOr(title, "پرسنل") + " • فاکتور، دریافت، پرداخت، اهداف و مشتریان اختصاصی", 9.7f, MUTED, Typeface.NORMAL);
        sub.setSingleLine(true); sub.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        bar.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); content.addView(bar, lp);
    }

    private void addPersonnelDateFilterCard(JSONObject person, String from, String to) {
        LinearLayout c = card();
        int accent = navAccent("personnel");
        c.setBackground(gradient(new int[]{alpha(accent, 22), alpha(GOLD, 12), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 22));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("◷", accent), new LinearLayout.LayoutParams(dp(44), dp(44)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(9),0,dp(8),0);
        copy.addView(text("فیلتر تاریخ و نمایش گزارش", 14.2f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1,-2));
        copy.addView(text("برای تفکیک فاکتورها، دریافتی‌ها، پرداختی‌ها، اهداف و مشتریان همین بازه را وارد کنید.", 9.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1,-2));
        head.addView(copy, new LinearLayout.LayoutParams(0,-2,1f));
        c.addView(head, new LinearLayout.LayoutParams(-1,-2));
        EditText start = input("از تاریخ مثل 1403/01/01", from, false);
        EditText end = input("تا تاریخ مثل 1403/12/29", to, false);
        start.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL); end.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { start.setTextDirection(View.TEXT_DIRECTION_RTL); end.setTextDirection(View.TEXT_DIRECTION_RTL); }
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(start, weightedButtonLp()); row.addView(end, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0, dp(9), 0, dp(8)); c.addView(row, rp);
        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
        Button apply = primaryButton(withIcon("⌕", "نمایش گزارش"));
        Button clear = secondaryButton(withIcon("×", "حذف فیلتر"));
        apply.setTextSize(10.2f); clear.setTextSize(10.2f);
        apply.setOnClickListener(v -> showPersonnelDetail(person, start.getText().toString(), end.getText().toString()));
        clear.setOnClickListener(v -> showPersonnelDetail(person, "", ""));
        actions.addView(apply, weightedButtonLp()); actions.addView(clear, weightedButtonLp());
        c.addView(actions, new LinearLayout.LayoutParams(-1,-2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c, lp);
    }

    private String queryPersonnelDetail(String id, String username, String fromDate, String toDate) throws Exception {
        try(Connection c=openConnection()) {
            JSONObject out=new JSONObject();
            String from = fromDate == null ? "" : fromDate.trim();
            String to = toDate == null ? "" : toDate.trim();
            JSONArray invoices=new JSONArray(); JSONArray receipts=new JSONArray(); JSONArray payments=new JSONArray();
            List<String> matchValues = personnelMatchValues(c, id, username);
            if(id!=null&&!id.trim().isEmpty()){
                try { appendPersonnelInvoices(c, invoices, id.trim(), from, to); } catch(Exception ignored) { }
                try { appendPersonnelChecks(c, receipts, id.trim(), true, from, to); } catch(Exception ignored) { }
                try { appendPersonnelChecks(c, payments, id.trim(), false, from, to); } catch(Exception ignored) { }
                try { appendPersonnelUserRegisteredRows(c, receipts, matchValues, true, from, to); } catch(Exception ignored) { }
                try { appendPersonnelUserRegisteredRows(c, payments, matchValues, false, from, to); } catch(Exception ignored) { }
                try { appendPersonnelPayrollRows(c, receipts, matchValues, from, to); } catch(Exception ignored) { }
                try { out.put("assignedCustomers", queryPersonnelAssignedCustomers(c, id.trim())); } catch(Exception ignored) { out.put("assignedCustomers", new JSONArray()); }
                try { out.put("goals", queryPersonnelGoals(c, id.trim(), from, to)); } catch(Exception ignored) { out.put("goals", new JSONArray()); }
            } else { try { appendPersonnelUserRegisteredRows(c, receipts, matchValues, true, from, to); appendPersonnelUserRegisteredRows(c, payments, matchValues, false, from, to); appendPersonnelPayrollRows(c, receipts, matchValues, from, to); } catch(Exception ignored) { } out.put("assignedCustomers", new JSONArray()); out.put("goals", new JSONArray()); }
            out.put("invoices", sortLedgerRows(invoices));
            out.put("receipts", sortLedgerRows(receipts));
            out.put("payments", sortLedgerRows(payments));
            JSONArray all = new JSONArray(); appendJsonArray(all, invoices); appendJsonArray(all, receipts); appendJsonArray(all, payments); out.put("documents", sortLedgerRows(all));
            ensureMeelanoCollabTables(c);
            String u=username==null?"":username.trim();
            if(!u.isEmpty()){ out.put("attendance", queryAttendanceRows(c,u,false)); out.put("leaves", queryLeaveRequestsForUser(c,u)); }
            else { out.put("attendance", new JSONArray()); out.put("leaves", new JSONArray()); }
            out.put("from", from); out.put("to", to);
            return out.toString();
        }
    }

    private void appendJsonArray(JSONArray target, JSONArray source) {
        if (target == null || source == null) return;
        for (int i=0;i<source.length();i++) { JSONObject o=source.optJSONObject(i); if(o!=null) target.put(o); }
    }

    private List<String> personnelMatchValues(Connection c, String id, String username) {
        List<String> values = new ArrayList<>();
        addUniqueValue(values, id);
        addUniqueValue(values, username);
        try {
            if (tableExists(c, "sys_users")) {
                Set<String> ucols = columns(c, "sys_users");
                String uid = resolveFlexible(ucols, "user_id", "UserID", "id", "ID");
                String uname = resolveFlexible(ucols, "user_name", "username", "Username", "UserName", "name", "Name");
                if (uid != null && uname != null && username != null && !username.trim().isEmpty()) {
                    try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) TRY_CONVERT(nvarchar(100),[" + uid + "]) FROM dbo.sys_users WHERE LOWER(LTRIM(RTRIM(TRY_CONVERT(nvarchar(160),[" + uname + "]))))=LOWER(LTRIM(RTRIM(?)))")) {
                        ps.setString(1, username.trim());
                        try (ResultSet r = ps.executeQuery()) { if (r.next()) addUniqueValue(values, r.getString(1)); }
                    }
                }
            }
        } catch (Exception ignored) { }
        try {
            if (tableExists(c, "sys_vis") && id != null && !id.trim().isEmpty()) {
                Set<String> sv = columns(c, "sys_vis");
                String uid = resolveFlexible(sv, "UserID", "user_id", "userid");
                String shvis = resolveFlexible(sv, "shvis", "vis_rdf", "visitor", "visitor_id");
                if (uid != null && shvis != null) {
                    try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) TRY_CONVERT(nvarchar(100),[" + uid + "]) FROM dbo.sys_vis WHERE TRY_CONVERT(nvarchar(100),[" + shvis + "])=?")) {
                        ps.setString(1, id.trim());
                        try (ResultSet r = ps.executeQuery()) { if (r.next()) addUniqueValue(values, r.getString(1)); }
                    }
                }
            }
        } catch (Exception ignored) { }
        return values;
    }

    private void addUniqueValue(List<String> values, String value) {
        if (values == null || value == null) return;
        String v = value.trim();
        if (v.isEmpty() || "—".equals(v) || "-".equals(v)) return;
        for (String old : values) if (old != null && old.equalsIgnoreCase(v)) return;
        values.add(v);
    }

    private String userRegisteredColumn(Set<String> cols) {
        return resolveFlexible(cols, "user_id", "UserID", "userid", "id_user", "created_by", "CreateUser", "creator", "sabt_user", "sabtUser", "username", "user_name", "UserName", "karbar", "کاربر", "operator", "op_user", "shuser", "ShUser");
    }

    private String personnelMatchWhere(String alias, String col, List<String> values, List<Object> params) {
        if (col == null || values == null || values.isEmpty()) return "";
        String prefix = alias == null || alias.trim().isEmpty() ? "" : alias + ".";
        List<String> parts = new ArrayList<>();
        for (String v : values) {
            if (v == null || v.trim().isEmpty()) continue;
            parts.add("LOWER(LTRIM(RTRIM(TRY_CONVERT(nvarchar(160)," + prefix + "[" + col + "]))))=LOWER(LTRIM(RTRIM(?)))");
            params.add(v.trim());
        }
        return parts.isEmpty() ? "" : "(" + join(parts, " OR ") + ")";
    }

    private String personnelDateWhere(String dateExpr, String from, String to, List<Object> params) {
        String w = "";
        if (dateExpr == null || dateExpr.trim().isEmpty()) return w;
        if (from != null && !from.trim().isEmpty()) { w += " AND TRY_CONVERT(nvarchar(30)," + dateExpr + ")>=?"; params.add(from.trim()); }
        if (to != null && !to.trim().isEmpty()) { w += " AND TRY_CONVERT(nvarchar(30)," + dateExpr + ")<=?"; params.add(to.trim()); }
        return w;
    }

    private void appendPersonnelInvoices(Connection c, JSONArray rows, String id, String from, String to) throws Exception {
        Set<String> sail=columns(c,"sailfact");
        String vis=resolveFlexible(sail,"vis_rdf","visitor","visitor_id","shvis","VisitorID");
        String number=resolveFlexible(sail,"shfacfo","factor_no","number","shomare");
        String date=resolveFlexible(sail,"date","t_date","Date");
        String amount=resolveFlexible(sail,"all","all_fel","amount","total");
        String paid=resolveFlexible(sail,"MabDaryaftFactor","Daryaft","received","paid");
        String shmo=resolveFlexible(sail,"shmo","SHMO","customer","CustomerCode");
        String desc=resolveFlexible(sail,"description","Explain","tozihat","شرح");
        if(vis==null || amount==null) return;
        List<Object> params=new ArrayList<>(); params.add(id);
        String dateExpr = date==null ? null : "sf.["+date+"]";
        String sql="SELECT TOP (160) "+(date==null?"CAST(NULL AS nvarchar(30))":"TRY_CONVERT(nvarchar(30),sf.["+date+"])") +", "+(number==null?"CAST(NULL AS nvarchar(80))":"TRY_CONVERT(nvarchar(80),sf.["+number+"])") +", "+(shmo==null?"CAST(NULL AS nvarchar(100))":"TRY_CONVERT(nvarchar(100),sf.["+shmo+"])") +", TRY_CONVERT(decimal(19,2),sf.["+amount+"]), "+(paid==null?"CAST(0 AS decimal(19,2))":"TRY_CONVERT(decimal(19,2),sf.["+paid+"])") +", "+(desc==null?"CAST(NULL AS nvarchar(500))":"TRY_CONVERT(nvarchar(500),sf.["+desc+"])") +" FROM dbo.sailfact sf WHERE TRY_CONVERT(nvarchar(100),sf.["+vis+"])=?"+personnelDateWhere(dateExpr,from,to,params)+" ORDER BY "+(date==null?"1":"sf.["+date+"] DESC");
        try(PreparedStatement ps=c.prepareStatement(sql)){ setParams(ps,params); try(ResultSet r=ps.executeQuery()){ while(r.next()){ JSONObject o=new JSONObject(); o.put("type","فاکتور فروش"); o.put("date",stringOr(r.getString(1),"—")); o.put("title","فاکتور "+stringOr(r.getString(2),"—")+" • مشتری "+stringOr(r.getString(3),"—")); o.put("amount",r.getDouble(4)); o.put("received",r.getDouble(5)); o.put("status","دریافتی: "+money(r.getObject(5))); o.put("description",stringOr(r.getString(6),"")); rows.put(o);} } }
    }

    private void appendPersonnelChecks(Connection c, JSONArray rows, String id, boolean incoming, String from, String to) throws Exception {
        String table=incoming?"getchk":"putchk";
        Set<String> cols=columns(c,table); Set<String> typeCols=columns(c,"CheckTypes");
        String vis=resolveFlexible(cols,"vis_rdf","visitor","visitor_id","shvis","VisitorID");
        if(vis==null) return;
        String amount=incoming?resolveFlexible(cols,"getchkmab","mablagh","amount"):resolveFlexible(cols,"putchkmab","mablagh","amount");
        if(amount==null) return;
        String date=incoming?resolveFlexible(cols,"getchkdate","chkdate","date","sarresid"):resolveFlexible(cols,"putchkdate","chkdate","date","sarresid");
        String number=incoming?resolveFlexible(cols,"getchknum","chknum","number","serial"):resolveFlexible(cols,"putchknum","chknum","number","serial");
        String shmo=resolveFlexible(cols,"shmo","SHMO","customer","CustomerCode");
        String status=incoming?resolveFlexible(cols,"chk_satus","status","Status"):resolveFlexible(cols,"putchk_status","status","Status");
        String desc=resolveFlexible(cols,"description","Explain","tozihat","شرح");
        String statusExpr=status==null?"N'نامشخص'":(hasCol(typeCols,"ID")&&hasCol(typeCols,"Desciption")?"COALESCE(TRY_CONVERT(nvarchar(120),t.Desciption),TRY_CONVERT(nvarchar(50),x.["+status+"]))":"TRY_CONVERT(nvarchar(50),x.["+status+"])");
        String join=status!=null&&hasCol(typeCols,"ID")&&hasCol(typeCols,"Desciption")?" LEFT JOIN dbo.CheckTypes t ON t.ID=x.["+status+"]":"";
        List<Object> params=new ArrayList<>(); params.add(id);
        String dateExpr=date==null?null:"x.["+date+"]";
        String sql="SELECT TOP (120) "+(date==null?"CAST(NULL AS nvarchar(30))":"TRY_CONVERT(nvarchar(30),x.["+date+"])") +", "+(number==null?"CAST(NULL AS nvarchar(80))":"TRY_CONVERT(nvarchar(80),x.["+number+"])") +", "+(shmo==null?"CAST(NULL AS nvarchar(100))":"TRY_CONVERT(nvarchar(100),x.["+shmo+"])") +", TRY_CONVERT(decimal(19,2),x.["+amount+"]), "+statusExpr+", "+(desc==null?"CAST(NULL AS nvarchar(500))":"TRY_CONVERT(nvarchar(500),x.["+desc+"])") +" FROM dbo.["+table+"] x"+join+" WHERE TRY_CONVERT(nvarchar(100),x.["+vis+"])=?"+personnelDateWhere(dateExpr,from,to,params)+" ORDER BY 1 DESC";
        try(PreparedStatement ps=c.prepareStatement(sql)){ setParams(ps,params); try(ResultSet r=ps.executeQuery()){ while(r.next()){ JSONObject o=new JSONObject(); o.put("type",incoming?"چک دریافتی":"چک پرداختی"); o.put("date",stringOr(r.getString(1),"—")); o.put("title","چک "+stringOr(r.getString(2),"—")+" • مشتری "+stringOr(r.getString(3),"—")); o.put("amount",r.getDouble(4)); o.put("status",stringOr(r.getString(5),"—")); o.put("description",stringOr(r.getString(6),"")); rows.put(o);} } }
    }

    private void appendPersonnelUserRegisteredRows(Connection c, JSONArray rows, List<String> matchValues, boolean incoming, String from, String to) throws Exception {
        if (matchValues == null || matchValues.isEmpty()) return;
        String table = incoming ? "getchk" : "putchk";
        if (!tableExists(c, table)) return;
        Set<String> cols = columns(c, table); Set<String> typeCols = columns(c, "CheckTypes");
        String userCol = userRegisteredColumn(cols);
        if (userCol == null) return;
        String amount = incoming ? resolveFlexible(cols,"getchkmab","mablagh","amount") : resolveFlexible(cols,"putchkmab","mablagh","amount");
        if (amount == null) return;
        String date = incoming ? resolveFlexible(cols,"getchkdate","chkdate","date","sarresid") : resolveFlexible(cols,"putchkdate","chkdate","date","sarresid");
        String number = incoming ? resolveFlexible(cols,"getchknum","chknum","number","serial") : resolveFlexible(cols,"putchknum","chknum","number","serial");
        String shmo = resolveFlexible(cols,"shmo","SHMO","customer","CustomerCode");
        String status = incoming ? resolveFlexible(cols,"chk_satus","status","Status") : resolveFlexible(cols,"putchk_status","status","Status");
        String desc = resolveFlexible(cols,"description","Explain","tozihat","شرح","comment","note");
        String statusExpr = status==null ? "N'ثبت کاربر'" : (hasCol(typeCols,"ID")&&hasCol(typeCols,"Desciption") ? "COALESCE(TRY_CONVERT(nvarchar(120),t.Desciption),TRY_CONVERT(nvarchar(50),x.["+status+"]))" : "TRY_CONVERT(nvarchar(50),x.["+status+"])");
        String join = status!=null&&hasCol(typeCols,"ID")&&hasCol(typeCols,"Desciption") ? " LEFT JOIN dbo.CheckTypes t ON t.ID=x.["+status+"]" : "";
        List<Object> params = new ArrayList<>();
        String match = personnelMatchWhere("x", userCol, matchValues, params);
        if (match.isEmpty()) return;
        String dateExpr = date==null ? null : "x.["+date+"]";
        String sql="SELECT TOP (120) "+(date==null?"CAST(NULL AS nvarchar(30))":"TRY_CONVERT(nvarchar(30),x.["+date+"])") +", "+(number==null?"CAST(NULL AS nvarchar(80))":"TRY_CONVERT(nvarchar(80),x.["+number+"])") +", "+(shmo==null?"CAST(NULL AS nvarchar(100))":"TRY_CONVERT(nvarchar(100),x.["+shmo+"])") +", TRY_CONVERT(decimal(19,2),x.["+amount+"]), "+statusExpr+", "+(desc==null?"CAST(NULL AS nvarchar(500))":"TRY_CONVERT(nvarchar(500),x.["+desc+"])") +" FROM dbo.["+table+"] x"+join+" WHERE "+match+personnelDateWhere(dateExpr,from,to,params)+" ORDER BY 1 DESC";
        try(PreparedStatement ps=c.prepareStatement(sql)){ setParams(ps,params); try(ResultSet r=ps.executeQuery()){ while(r.next()){ JSONObject o=new JSONObject(); o.put("type",incoming?"دریافتی ثبت‌شده توسط کاربر":"پرداختی ثبت‌شده توسط کاربر"); o.put("date",stringOr(r.getString(1),"—")); o.put("title","ثبت کاربر • "+stringOr(r.getString(2),"—")+" • مشتری "+stringOr(r.getString(3),"—")); o.put("amount",r.getDouble(4)); o.put("status",stringOr(r.getString(5),"ثبت با اکانت پرسنل")); o.put("description",stringOr(r.getString(6),"")); rows.put(o);} } }
    }

    private void appendPersonnelPayrollRows(Connection c, JSONArray rows, List<String> matchValues, String from, String to) throws Exception {
        if (matchValues == null || matchValues.isEmpty()) return;
        String[] tables = {"salary","Salary","payroll","Payroll","hoghoogh","Hoghoogh","hoghogh","Hoghogh","bimeh","Bimeh","insurance","Insurance","personnel_payments","PersonnelPayments","hr_payments","HRPayments","pardakht_hoghoogh","PardakhtHoghoogh","حقوق","بیمه"};
        for (String table : tables) {
            if (!tableExists(c, table)) continue;
            try { appendPersonnelPayrollRowsFromTable(c, rows, table, matchValues, from, to); } catch (Exception ignored) { }
        }
    }

    private void appendPersonnelPayrollRowsFromTable(Connection c, JSONArray rows, String table, List<String> matchValues, String from, String to) throws Exception {
        Set<String> cols = columns(c, table);
        if (cols == null || cols.isEmpty()) return;
        String person = resolveFlexible(cols,"vis_rdf","shvis","visitor","visitor_id","personnel_id","person_id","employee_id","staff_id","user_id","UserID","userid","username","user_name","UserName","name","Name","karbar","کاربر","personnel","employee");
        String amount = resolveFlexible(cols,"amount","mablagh","mab","price","salary","hoghogh","حقوق","bimeh","insurance","mablagh_nahayi","net","net_amount","payment","pay");
        if (person == null || amount == null) return;
        String date = resolveFlexible(cols,"date","t_date","Date","created_at","created","pay_date","salary_date","tarikh","تاریخ");
        String title = resolveFlexible(cols,"title","subject","name","description","Explain","tozihat","شرح","type","kind");
        String desc = resolveFlexible(cols,"description","Explain","tozihat","شرح","comment","note","notes");
        List<Object> params = new ArrayList<>();
        String match = personnelMatchWhere("p", person, matchValues, params);
        if (match.isEmpty()) return;
        String dateExpr = date == null ? null : "p.["+date+"]";
        String label = payrollLabelFor(table);
        String sql = "SELECT TOP (80) " + (date==null?"CAST(NULL AS nvarchar(30))":"TRY_CONVERT(nvarchar(30),p.["+date+"])") + ", " + (title==null?"N'"+label+"'":"TRY_CONVERT(nvarchar(240),p.["+title+"])") + ", TRY_CONVERT(decimal(19,2),p.["+amount+"]), " + (desc==null?"CAST(NULL AS nvarchar(500))":"TRY_CONVERT(nvarchar(500),p.["+desc+"])") + " FROM dbo.["+table+"] p WHERE " + match + personnelDateWhere(dateExpr,from,to,params) + " ORDER BY " + (date==null?"1":"p.["+date+"] DESC");
        try (PreparedStatement ps = c.prepareStatement(sql)) { setParams(ps, params); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("type", label); o.put("date", stringOr(r.getString(1), "—")); o.put("title", stringOr(r.getString(2), label)); o.put("amount", r.getDouble(3)); o.put("status", "حقوق/بیمه/مزایا"); o.put("description", stringOr(r.getString(4), "")); rows.put(o); } } }
    }

    private String payrollLabelFor(String table) {
        String t = table == null ? "" : table.toLowerCase(Locale.US);
        if (t.contains("bimeh") || t.contains("insurance") || t.contains("بیم")) return "دریافتی بیمه/مزایا";
        if (t.contains("salary") || t.contains("payroll") || t.contains("hog") || t.contains("حقوق")) return "دریافتی حقوق";
        return "دریافتی حقوق/بیمه";
    }

    private JSONArray queryPersonnelAssignedCustomers(Connection c, String id) throws Exception {
        JSONArray arr=new JSONArray();
        Set<String> cols=columns(c,"CUSTOMERS");
        String vis=resolveFlexible(cols,"vis_rdf","VisitorID","visid","visitor","shvis");
        String code=resolveFlexible(cols,"SHMO","shmo","CustomerCode");
        String name=resolveFlexible(cols,"MONAME","Name","CusName","CustomerName");
        String balance=resolveFlexible(cols,"man","Balance","Mandeh","mande");
        String phone=resolveFlexible(cols,"cell","mobile","Mobile","tell1","tel1","phone","Phone");
        if(vis==null||code==null) return arr;
        String sql="SELECT TOP (100) TRY_CONVERT(nvarchar(100),c.["+code+"]), "+(name==null?"N'بدون نام'":"TRY_CONVERT(nvarchar(250),c.["+name+"])") +", "+(balance==null?"CAST(0 AS decimal(19,2))":"TRY_CONVERT(decimal(19,2),c.["+balance+"])") +", "+(phone==null?"CAST(NULL AS nvarchar(100))":"TRY_CONVERT(nvarchar(100),c.["+phone+"])") +", "+customerAddressExpr(cols,"c")+" FROM dbo.CUSTOMERS c WHERE TRY_CONVERT(nvarchar(100),c.["+vis+"])=? ORDER BY "+(name==null?"1":"c.["+name+"]");
        try(PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,id); try(ResultSet r=ps.executeQuery()){ while(r.next()){ JSONObject o=new JSONObject(); o.put("code",stringOr(r.getString(1),"")); o.put("name",stringOr(r.getString(2),"بدون نام")); o.put("balance",r.getDouble(3)); o.put("phone",stringOr(r.getString(4),"")); o.put("address",stringOr(r.getString(5),"")); arr.put(o);} } }
        return arr;
    }

    private JSONArray queryPersonnelGoals(Connection c, String id, String from, String to) throws Exception {
        JSONArray arr=new JSONArray();
        if(!tableExists(c,"vis_goals")) return arr;
        Set<String> cols=columns(c,"vis_goals");
        String vis=resolveFlexible(cols,"vis_rdf","visitor","visitor_id","shvis","VisitorID");
        if(vis==null) return arr;
        String date=resolveFlexible(cols,"date","t_date","start_date","goal_date","Date");
        String title=resolveFlexible(cols,"title","subject","name","description","Explain","tozihat");
        String target=resolveFlexible(cols,"target","goal","amount","mablagh","mablagh_goal","sale_goal","forosh");
        String done=resolveFlexible(cols,"done","achieved","sale","amount_done","actual","forosh_done");
        List<Object> params=new ArrayList<>(); params.add(id);
        String dateExpr=date==null?null:"g.["+date+"]";
        String sql="SELECT TOP (80) "+(date==null?"CAST(NULL AS nvarchar(30))":"TRY_CONVERT(nvarchar(30),g.["+date+"])") +", "+(title==null?"N'هدف ویزیتور'":"TRY_CONVERT(nvarchar(250),g.["+title+"])") +", "+(target==null?"CAST(0 AS decimal(19,2))":"TRY_CONVERT(decimal(19,2),g.["+target+"])") +", "+(done==null?"CAST(0 AS decimal(19,2))":"TRY_CONVERT(decimal(19,2),g.["+done+"])") +" FROM dbo.vis_goals g WHERE TRY_CONVERT(nvarchar(100),g.["+vis+"])=?"+personnelDateWhere(dateExpr,from,to,params)+" ORDER BY "+(date==null?"1":"g.["+date+"] DESC");
        try(PreparedStatement ps=c.prepareStatement(sql)){ setParams(ps,params); try(ResultSet r=ps.executeQuery()){ while(r.next()){ JSONObject o=new JSONObject(); o.put("date",stringOr(r.getString(1),"—")); o.put("title",stringOr(r.getString(2),"هدف ویزیتور")); o.put("target",r.getDouble(3)); o.put("done",r.getDouble(4)); arr.put(o);} } }
        return arr;
    }

    private void renderPersonnelDetail(JSONObject person, JSONObject data, String from, String to) {
        content.removeAllViews();
        addHero("پرونده پرسنلی", person.optString("name","پرسنل") + " • مانده " + money(person.opt("balance")));
        addPersonnelBackBar(person.optString("name","پرسنل"));
        addPersonnelDateFilterCard(person, from, to);
        LinearLayout top=card(); top.setBackground(gradient(new int[]{alpha(navAccent("personnel"),30), alpha(SURFACE,250)}, GradientDrawable.Orientation.TL_BR,22));
        top.addView(text("اطلاعات ضروری",15,TEXT,Typeface.BOLD), new LinearLayout.LayoutParams(-1,-2));
        top.addView(text("نام کاربری: "+stringOr(person.optString("username"),"—")+" • تماس: "+stringOr(person.optString("phone"),"—")+" • آخرین فعالیت: "+stringOr(person.optString("last"),"—"),10.5f,MUTED,Typeface.NORMAL), new LinearLayout.LayoutParams(-1,-2));
        LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(-1,-2); tp.setMargins(0,0,0,dp(10)); content.addView(top,tp);
        addPersonnelFinanceSummary(data);
        addPersonnelLedgerSection("فاکتورهای فروش", "فروش‌های ثبت‌شده برای این ویزیتور/پرسنل", data.optJSONArray("invoices"), GOLD);
        addPersonnelLedgerSection("دریافتی‌ها", "چک‌ها و وصول‌های قابل انتساب به این نفر", data.optJSONArray("receipts"), SUCCESS);
        addPersonnelLedgerSection("پرداختی‌ها", "چک‌ها/پرداختی‌های قابل انتساب؛ برای کنترل تعهدات", data.optJSONArray("payments"), WARNING);
        addPersonnelAssignedCustomers(data.optJSONArray("assignedCustomers"));
        addPersonnelGoalsSection(data.optJSONArray("goals"));
        addAttendanceRows("حضور و خروج این پرسنل", data.optJSONArray("attendance"));
        addLeaveList("مرخصی‌های این پرسنل", data.optJSONArray("leaves"), false);
    }

    private void addPersonnelFinanceSummary(JSONObject data) {
        JSONArray inv=data.optJSONArray("invoices"), rec=data.optJSONArray("receipts"), pay=data.optJSONArray("payments"), cust=data.optJSONArray("assignedCustomers"), goals=data.optJSONArray("goals");
        double invSum=sumAmount(inv,"amount"), recSum=sumAmount(rec,"amount"), paySum=sumAmount(pay,"amount");
        LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(navAccent("personnel"),24), alpha(GOLD,14), alpha(SURFACE,250)}, GradientDrawable.Orientation.RIGHT_LEFT,22));
        c.addView(text("خلاصه تفکیک‌شده مدیر",15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("فاکتور، دریافتی، پرداختی، اهداف و مشتریان اختصاصی جدا شده‌اند تا گزارش قابل تصمیم‌گیری باشد.",10.2f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        LinearLayout r1=new LinearLayout(this); r1.setOrientation(LinearLayout.HORIZONTAL);
        r1.addView(customerMiniMetric("فروش", compactMoney(invSum), GOLD), weightedMiniLp());
        r1.addView(customerMiniMetric("دریافتی", compactMoney(recSum), SUCCESS), weightedMiniLp());
        r1.addView(customerMiniMetric("پرداختی", compactMoney(paySum), WARNING), weightedMiniLp());
        LinearLayout.LayoutParams r1p=new LinearLayout.LayoutParams(-1,-2); r1p.setMargins(0,dp(8),0,0); c.addView(r1,r1p);
        LinearLayout r2=new LinearLayout(this); r2.setOrientation(LinearLayout.HORIZONTAL);
        r2.addView(customerMiniMetric("مشتری اختصاصی", formatNumber(cust==null?0:cust.length()), INFO), weightedMiniLp());
        r2.addView(customerMiniMetric("هدف", formatNumber(goals==null?0:goals.length()), navAccent("personnel")), weightedMiniLp());
        r2.addView(customerMiniMetric("خالص", compactMoney(recSum-paySum), recSum>=paySum?SUCCESS:DANGER), weightedMiniLp());
        LinearLayout.LayoutParams r2p=new LinearLayout.LayoutParams(-1,-2); r2p.setMargins(0,dp(7),0,0); c.addView(r2,r2p);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private double sumAmount(JSONArray rows, String key) {
        double sum=0; if(rows!=null) for(int i=0;i<rows.length();i++){ JSONObject o=rows.optJSONObject(i); if(o!=null) sum += o.optDouble(key,0); }
        return sum;
    }

    private void addPersonnelLedgerSection(String title, String sub, JSONArray rows, int accent) {
        LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(accent,20), alpha(SURFACE,250)}, GradientDrawable.Orientation.TL_BR,22));
        LinearLayout head=new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon(reportGlyph(title), accent), new LinearLayout.LayoutParams(dp(44), dp(44)));
        LinearLayout copy=new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(9),0,dp(8),0);
        copy.addView(text(title,15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        copy.addView(text(sub + " • تعداد: " + formatNumber(rows==null?0:rows.length()),10.0f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        head.addView(copy,new LinearLayout.LayoutParams(0,-2,1f)); c.addView(head,new LinearLayout.LayoutParams(-1,-2));
        if(rows==null||rows.length()==0) c.addView(text("رکوردی در این بخش/بازه پیدا نشد.",10.8f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,dp(48)));
        else for(int i=0;i<Math.min(120,rows.length());i++){ JSONObject r=rows.optJSONObject(i); if(r!=null)c.addView(personnelFinanceRow(r), compactRowLp()); }
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private void addPersonnelAssignedCustomers(JSONArray rows) {
        LinearLayout c=card(); int accent=SUCCESS; c.setBackground(gradient(new int[]{alpha(accent,20), alpha(SURFACE,250)}, GradientDrawable.Orientation.RIGHT_LEFT,22));
        c.addView(text("مشتریان اختصاص‌داده‌شده",15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("مشتریانی که با کد ویزیتور/پرسنل مرتبط هستند؛ برای کنترل پوشش منطقه و پیگیری فروش.",10.1f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        if(rows==null||rows.length()==0)c.addView(text("مشتری اختصاصی قابل نمایش پیدا نشد.",10.8f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,dp(48)));
        else for(int i=0;i<Math.min(80,rows.length());i++){ JSONObject r=rows.optJSONObject(i); if(r==null)continue; LinearLayout item=new LinearLayout(this); item.setOrientation(LinearLayout.VERTICAL); item.setPadding(dp(9),dp(8),dp(9),dp(8)); item.setBackground(roundedStroke(alpha(accent,16),16,alpha(accent,62))); TextView name=text(r.optString("name","مشتری")+" • کد "+r.optString("code","—"),11.2f,TEXT,Typeface.BOLD); name.setSingleLine(true); name.setEllipsize(TextUtils.TruncateAt.END); item.addView(name,new LinearLayout.LayoutParams(-1,-2)); item.addView(text("مانده: "+money(r.opt("balance"))+" • تماس: "+stringOr(r.optString("phone"),"—"),9.5f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); String addr=cleanCustomerAddress(r.optString("address","")); if(!addr.isEmpty()){ TextView a=text("نشانی: "+addr,9.3f,MUTED,Typeface.NORMAL); a.setMaxLines(2); a.setEllipsize(TextUtils.TruncateAt.END); item.addView(a,new LinearLayout.LayoutParams(-1,-2)); } LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(-1,-2); ip.setMargins(0,dp(7),0,0); c.addView(item,ip); }
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private void addPersonnelGoalsSection(JSONArray rows) {
        LinearLayout c=card(); int accent=INFO; c.setBackground(gradient(new int[]{alpha(accent,20), alpha(GOLD,12), alpha(SURFACE,250)}, GradientDrawable.Orientation.TL_BR,22));
        c.addView(text("اهداف ویزیتور",15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("هدف، عملکرد و درصد تحقق در بازه انتخابی نمایش داده می‌شود؛ اگر ستون‌های هدف متفاوت باشند، برنامه بهترین ستون‌های موجود را استفاده می‌کند.",10.0f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        if(rows==null||rows.length()==0)c.addView(text("هدف قابل نمایش برای این ویزیتور پیدا نشد.",10.8f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,dp(48)));
        else for(int i=0;i<Math.min(80,rows.length());i++){ JSONObject r=rows.optJSONObject(i); if(r==null)continue; double target=r.optDouble("target",0), done=r.optDouble("done",0); double pct=target==0?0:(done/Math.abs(target))*100d; LinearLayout item=new LinearLayout(this); item.setOrientation(LinearLayout.VERTICAL); item.setPadding(dp(9),dp(8),dp(9),dp(8)); item.setBackground(roundedStroke(alpha(accent,16),16,alpha(accent,62))); item.addView(text(r.optString("title","هدف ویزیتور")+" • "+r.optString("date","—"),11.2f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); row.addView(customerMiniMetric("هدف", compactMoney(target), GOLD), weightedMiniLp()); row.addView(customerMiniMetric("عملکرد", compactMoney(done), SUCCESS), weightedMiniLp()); row.addView(customerMiniMetric("تحقق", formatNumber(pct)+"٪", pct>=100?SUCCESS:WARNING), weightedMiniLp()); item.addView(row,new LinearLayout.LayoutParams(-1,-2)); LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(-1,-2); ip.setMargins(0,dp(7),0,0); c.addView(item,ip); }
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private LinearLayout personnelFinanceRow(JSONObject d) {
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);
        line.setPadding(dp(9), dp(8), dp(9), dp(8));
        String type = d.optString("type", "گردش");
        int accent = type.contains("دریافتی") ? SUCCESS : (type.contains("پرداخت") ? WARNING : GOLD);
        line.setBackground(roundedStroke(alpha(accent, 16), 16, alpha(accent, 68)));
        TextView badge = text(type.contains("چک") ? "چک" : "فاکتور", 9.2f, onColorFor(accent), Typeface.BOLD);
        badge.setGravity(Gravity.CENTER); badge.setSingleLine(true);
        badge.setBackground(luxuryButtonBg(accent, true, 999));
        line.addView(badge, new LinearLayout.LayoutParams(dp(58), dp(34)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(8),0,dp(8),0);
        TextView title = text(type + " • " + d.optString("date", "—"), 11.2f, TEXT, Typeface.BOLD);
        title.setSingleLine(true); title.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(title, new LinearLayout.LayoutParams(-1,-2));
        TextView sub = text(d.optString("title", "—") + " • " + d.optString("status", "—"), 9.4f, MUTED, Typeface.NORMAL);
        sub.setSingleLine(true); sub.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(sub, new LinearLayout.LayoutParams(-1,-2));
        if (!d.optString("description", "").isEmpty()) {
            TextView desc = text(d.optString("description", ""), 9.2f, MUTED, Typeface.NORMAL);
            desc.setMaxLines(2); desc.setEllipsize(TextUtils.TruncateAt.END);
            copy.addView(desc, new LinearLayout.LayoutParams(-1,-2));
        }
        line.addView(copy, new LinearLayout.LayoutParams(0,-2,1f));
        TextView amount = text(compactMoney(d.opt("amount")), 10.4f, TEXT, Typeface.BOLD);
        amount.setGravity(Gravity.CENTER); amount.setSingleLine(true); amount.setPadding(dp(8), dp(5), dp(8), dp(5));
        amount.setBackground(roundedStroke(alpha(accent, 20), 999, alpha(accent, 76)));
        line.addView(amount, new LinearLayout.LayoutParams(-2,-2));
        return line;
    }

    private void loadAttendance() {
        content.removeAllViews(); addHero("حضور", "ثبت حضور با مودم محل کار، مرخصی و گزارش مدیریتی"); addManualRefreshPanel("attendance", "بروزرسانی حضور و غیاب", "نمای فعلی ثابت است تا تازه‌سازی دستی", () -> loadAttendance()); addLoading(content,"در حال دریافت حضور و غیاب…");
        runDb(this::queryAttendanceState, new DbCallback(){ @Override public void ok(String body){ try{ renderAttendance(new JSONObject(body)); markRefresh("attendance"); }catch(Exception e){ showPageError("حضور",e,()->loadAttendance()); } } @Override public void fail(Exception e){ showPageError("حضور",e,()->loadAttendance()); }});
    }

    private String queryAttendanceState() throws Exception { try(Connection c=openConnection()){ ensureMeelanoCollabTables(c); JSONObject out=new JSONObject(); boolean admin=canUsePermission("attendance_admin"); out.put("admin",admin); out.put("wifiSsid",chatSetting(c,"work_wifi_ssid","")); out.put("wifiBssid",chatSetting(c,"work_wifi_bssid","")); out.put("wifiGateway",chatSetting(c,"work_wifi_gateway","")); out.put("mine",queryAttendanceRows(c, currentAccountName(), false)); if(admin){ out.put("today",queryAttendanceRows(c,"", true)); out.put("leaves",queryLeaveRequests(c)); } else out.put("leaves",queryLeaveRequestsForUser(c,currentAccountName())); return out.toString(); } }

    private JSONArray queryAttendanceRows(Connection c, String username, boolean todayAll) throws Exception { JSONArray arr=new JSONArray(); String sql=todayAll?"SELECT TOP (150) username,display_name,event_type,CONVERT(nvarchar(19),event_time,120),wifi_ssid,wifi_bssid,gateway FROM dbo.meelano_attendance WHERE CONVERT(date,event_time)=CONVERT(date,SYSDATETIME()) ORDER BY event_time DESC":"SELECT TOP (80) username,display_name,event_type,CONVERT(nvarchar(19),event_time,120),wifi_ssid,wifi_bssid,gateway FROM dbo.meelano_attendance WHERE username=? ORDER BY event_time DESC"; try(PreparedStatement ps=c.prepareStatement(sql)){ if(!todayAll) ps.setString(1,username); try(ResultSet r=ps.executeQuery()){ while(r.next()){ JSONObject o=new JSONObject(); o.put("username",stringOr(r.getString(1),"")); o.put("display",stringOr(r.getString(2),r.getString(1))); o.put("type",stringOr(r.getString(3),"")); o.put("time",stringOr(r.getString(4),"")); o.put("ssid",stringOr(r.getString(5),"")); o.put("bssid",stringOr(r.getString(6),"")); o.put("gateway",stringOr(r.getString(7),"")); arr.put(o);} } } return arr; }

    private JSONArray queryLeaveRequests(Connection c) throws Exception { JSONArray arr=new JSONArray(); try(PreparedStatement ps=c.prepareStatement("SELECT TOP (80) id,username,display_name,leave_type,start_date,end_date,hours,reason,status,created_at FROM dbo.meelano_leave_requests ORDER BY CASE WHEN status=N'pending' THEN 0 ELSE 1 END, created_at DESC")){ try(ResultSet r=ps.executeQuery()){ while(r.next()) arr.put(leaveRow(r)); } } return arr; }
    private JSONArray queryLeaveRequestsForUser(Connection c,String username) throws Exception { JSONArray arr=new JSONArray(); try(PreparedStatement ps=c.prepareStatement("SELECT TOP (30) id,username,display_name,leave_type,start_date,end_date,hours,reason,status,created_at FROM dbo.meelano_leave_requests WHERE username=? ORDER BY created_at DESC")){ ps.setString(1,username); try(ResultSet r=ps.executeQuery()){ while(r.next()) arr.put(leaveRow(r)); } } return arr; }
    private JSONObject leaveRow(ResultSet r) throws Exception { JSONObject o=new JSONObject(); o.put("id",r.getLong(1)); o.put("username",stringOr(r.getString(2),"")); o.put("display",stringOr(r.getString(3),r.getString(2))); o.put("type",stringOr(r.getString(4),"")); o.put("start",stringOr(r.getString(5),"")); o.put("end",stringOr(r.getString(6),"")); o.put("hours",stringOr(r.getString(7),"")); o.put("reason",stringOr(r.getString(8),"")); o.put("status",stringOr(r.getString(9),"")); o.put("created",stringOr(r.getString(10),"")); return o; }

    private void renderAttendance(JSONObject state) {
        content.removeAllViews(); addHero("حضور", state.optBoolean("admin")?"پنل مدیریت حضور، خروج و مرخصی پرسنل":"ثبت ورود/خروج و درخواست مرخصی"); addManualRefreshPanel("attendance","بروزرسانی حضور","آخرین بروزرسانی: "+lastRefreshText("attendance"),()->loadAttendance()); addAttendanceWifiCard(state); if(state.optBoolean("admin")) addAttendanceAdminBlocks(state); else { addAttendanceUserActions(state); addLeaveBalanceCard(state.optJSONArray("leaves")); addLeaveList("درخواست‌های مرخصی من", state.optJSONArray("leaves"), false); } }

    private void addAttendanceWifiCard(JSONObject state){ LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(navAccent("attendance"),28),alpha(SURFACE,250)},GradientDrawable.Orientation.TL_BR,22)); JSONObject wifi=currentWifiFingerprint(); c.addView(text("مودم محل کار",15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); c.addView(text("ثبت‌شده: "+stringOr(state.optString("wifiSsid"),"تنظیم نشده")+" • فعلی: "+stringOr(wifi.optString("ssid"),"نامشخص"),10.5f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); if(state.optBoolean("admin")){ LinearLayout wr=new LinearLayout(this); wr.setOrientation(LinearLayout.HORIZONTAL); Button cap=primaryButton(withIcon("⌁", "ثبت همین مودم")); Button hours=secondaryButton(withIcon("⏱", "ساعت مجاز")); cap.setTextSize(9.6f); hours.setTextSize(9.6f); cap.setOnClickListener(v->captureWorkWifi()); hours.setOnClickListener(v->showAttendanceHoursDialog()); wr.addView(cap,weightedButtonLp()); wr.addView(hours,weightedButtonLp()); LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2); cp.setMargins(0,dp(10),0,0); c.addView(wr,cp);} LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp); }

    private void addAttendanceUserActions(JSONObject state){ LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(SUCCESS,24),alpha(SURFACE,250)},GradientDrawable.Orientation.RIGHT_LEFT,22)); c.addView(text("ثبت حضور با تأیید مودم",15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); c.addView(text("برای ثبت ورود یا خروج، گوشی باید به شبکه محل کار متصل باشد؛ رمز مودم در برنامه ذخیره نمی‌شود.",10.5f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); if(canUsePermission("attendance_self")){ LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); Button in=primaryButton(withIcon("↘", "ثبت ورود")); Button out=secondaryButton(withIcon("↗", "ثبت خروج")); in.setOnClickListener(v->recordAttendance("in")); out.setOnClickListener(v->recordAttendance("out")); row.addView(in,weightedButtonLp()); row.addView(out,weightedButtonLp()); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(10),0,0); c.addView(row,rp); } if(canUsePermission("leave_request")){ Button leave=secondaryButton(withIcon("☘", "درخواست مرخصی")); leave.setOnClickListener(v->showLeaveRequestDialog()); LinearLayout.LayoutParams lpv=new LinearLayout.LayoutParams(-1,dp(44)); lpv.setMargins(0,dp(8),0,0); c.addView(leave,lpv); } LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp); if(canUsePermission("attendance_self")) addAttendanceRows("آخرین ورود/خروج من", state.optJSONArray("mine")); }

    private void addAttendanceAdminBlocks(JSONObject state){ addAttendanceReportActions(state); addAttendanceRows("حضور امروز پرسنل", state.optJSONArray("today")); addLeaveBalanceCard(state.optJSONArray("leaves")); addLeaveList("درخواست‌های مرخصی", state.optJSONArray("leaves"), true); notifyPendingLeavesOnce(state.optJSONArray("leaves")); }

    private void addAttendanceReportActions(JSONObject state) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(GOLD, 22), alpha(navAccent("attendance"), 18), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("خروجی مدیریتی حضور و مرخصی", 15, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("گزارش سریع برای بایگانی، حقوق و کنترل منابع انسانی", 10.3f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1,-2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button csv = primaryButton(withIcon("CSV", "حضور")); Button pdf = secondaryButton(withIcon("PDF", "خلاصه"));
        csv.setTextSize(9.7f); pdf.setTextSize(9.7f);
        csv.setOnClickListener(v -> exportAttendanceCsv(state.optJSONArray("today"), state.optJSONArray("leaves")));
        pdf.setOnClickListener(v -> exportAttendancePdf(state.optJSONArray("today"), state.optJSONArray("leaves")));
        row.addView(csv, weightedButtonLp()); row.addView(pdf, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0, dp(9), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c, lp);
    }

    private void addLeaveBalanceCard(JSONArray leaves) {
        int pending=0, approved=0, rejected=0;
        double usedDays=0;
        if (leaves != null) for (int i=0;i<leaves.length();i++) {
            JSONObject l=leaves.optJSONObject(i); if(l==null) continue;
            String st=l.optString("status"); if("approved".equals(st)){approved++; usedDays += estimatedLeaveDays(l);} else if("rejected".equals(st)) rejected++; else pending++;
        }
        LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(INFO,20), alpha(SURFACE,250)}, GradientDrawable.Orientation.TL_BR,22));
        c.addView(text("خلاصه مرخصی",15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("برآورد کاربردی بر پایه درخواست‌های ثبت‌شده؛ برای قوانین دقیق حقوقی، خروجی را با اسناد اداری تطبیق دهید.",9.8f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(customerMiniMetric("تأیید شده", formatNumber(approved), SUCCESS), weightedMiniLp());
        row.addView(customerMiniMetric("در انتظار", formatNumber(pending), WARNING), weightedMiniLp());
        row.addView(customerMiniMetric("روز مصرفی", formatNumber(usedDays), INFO), weightedMiniLp());
        LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(8),0,0); c.addView(row,rp);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private double estimatedLeaveDays(JSONObject l) {
        if (l == null) return 0;
        String hours = l.optString("hours", "").replace('۰','0').replace('۱','1').replace('۲','2').replace('۳','3').replace('۴','4').replace('۵','5').replace('۶','6').replace('۷','7').replace('۸','8').replace('۹','9');
        try { if (!hours.trim().isEmpty()) { double h=Double.parseDouble(hours.replaceAll("[^0-9.]", "")); if (h > 0) return Math.max(0.25, h / 7.33); } } catch (Exception ignored) { }
        return 1;
    }

    private void exportAttendanceCsv(JSONArray rows, JSONArray leaves) {
        try {
            File dir=getExternalFilesDir(null); if(dir==null)dir=getFilesDir();
            File file=new File(dir,"Meelano-Attendance-v3.37.csv");
            StringBuilder b=new StringBuilder("section,user,display,type,time,ssid,status,start,end,hours,reason\n");
            if(rows!=null) for(int i=0;i<rows.length();i++){ JSONObject r=rows.optJSONObject(i); if(r==null)continue; b.append("attendance,").append(csvSafe(r.optString("username"))).append(',').append(csvSafe(r.optString("display"))).append(',').append(csvSafe(r.optString("type"))).append(',').append(csvSafe(r.optString("time"))).append(',').append(csvSafe(r.optString("ssid"))).append(",,,,,\n"); }
            if(leaves!=null) for(int i=0;i<leaves.length();i++){ JSONObject l=leaves.optJSONObject(i); if(l==null)continue; b.append("leave,").append(csvSafe(l.optString("username"))).append(',').append(csvSafe(l.optString("display"))).append(',').append(csvSafe(l.optString("type"))).append(",,,").append(csvSafe(l.optString("status"))).append(',').append(csvSafe(l.optString("start"))).append(',').append(csvSafe(l.optString("end"))).append(',').append(csvSafe(l.optString("hours"))).append(',').append(csvSafe(l.optString("reason"))).append('\n'); }
            try(FileOutputStream fos=new FileOutputStream(file)){ fos.write(b.toString().getBytes(StandardCharsets.UTF_8)); }
            sharePlainText("گزارش حضور Meelano", "خروجی CSV ساخته شد:\n"+file.getAbsolutePath(), null);
        } catch(Exception ex){ Toast.makeText(this,"خروجی CSV ساخته نشد: "+shortError(ex),Toast.LENGTH_LONG).show(); }
    }

    private void exportAttendancePdf(JSONArray rows, JSONArray leaves) {
        try {
            File dir=getExternalFilesDir(null); if(dir==null)dir=getFilesDir();
            File file=new File(dir,"Meelano-Attendance-v3.37.pdf");
            PdfDocument doc=new PdfDocument();
            PdfDocument.Page page=doc.startPage(new PdfDocument.PageInfo.Builder(595,842,1).create());
            Canvas canvas=page.getCanvas(); Paint pnt=new Paint(Paint.ANTI_ALIAS_FLAG);
            pnt.setColor(Color.rgb(20,30,45)); pnt.setTextSize(18); pnt.setTypeface(Typeface.DEFAULT_BOLD); canvas.drawText("MEELANO Attendance Summary",40,50,pnt);
            pnt.setTextSize(12); pnt.setTypeface(Typeface.DEFAULT); pnt.setColor(Color.rgb(60,70,90));
            int y=82; canvas.drawText("Generated: "+nowText(),40,y,pnt); y+=28;
            canvas.drawText("Today attendance records: "+(rows==null?0:rows.length()),40,y,pnt); y+=22;
            canvas.drawText("Leave requests: "+(leaves==null?0:leaves.length()),40,y,pnt); y+=34;
            pnt.setTypeface(Typeface.DEFAULT_BOLD); canvas.drawText("Recent attendance",40,y,pnt); y+=20; pnt.setTypeface(Typeface.DEFAULT);
            if(rows!=null) for(int i=0;i<Math.min(18,rows.length());i++){ JSONObject r=rows.optJSONObject(i); if(r==null)continue; canvas.drawText((i+1)+") "+r.optString("display")+" - "+r.optString("type")+" - "+r.optString("time"),40,y,pnt); y+=18; }
            y+=14; pnt.setTypeface(Typeface.DEFAULT_BOLD); canvas.drawText("Pending leaves",40,y,pnt); y+=20; pnt.setTypeface(Typeface.DEFAULT);
            if(leaves!=null) for(int i=0;i<Math.min(12,leaves.length());i++){ JSONObject l=leaves.optJSONObject(i); if(l==null||!"pending".equals(l.optString("status")))continue; canvas.drawText("#"+l.optLong("id")+" "+l.optString("display")+" "+l.optString("type")+" "+l.optString("start")+"-"+l.optString("end"),40,y,pnt); y+=18; }
            doc.finishPage(page); try(FileOutputStream fos=new FileOutputStream(file)){ doc.writeTo(fos); } doc.close();
            sharePlainText("PDF حضور Meelano", "فایل PDF ساخته شد:\n"+file.getAbsolutePath(), null);
        } catch(Exception ex){ Toast.makeText(this,"PDF ساخته نشد: "+shortError(ex),Toast.LENGTH_LONG).show(); }
    }

    private void showAttendanceHoursDialog() {
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12),dp(10),dp(12),dp(8));
        box.addView(text("بازه مجاز ثبت حضور", 14.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1,-2));
        box.addView(text("اگر خالی بگذارید محدودیت زمانی حذف می‌شود.", 10.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1,-2));
        EditText start=input("شروع مجاز مثل 08:00", prefs==null?"":prefs.getString("attendance_start_hint","08:00"), false);
        EditText end=input("پایان مجاز مثل 18:00", prefs==null?"":prefs.getString("attendance_end_hint","18:00"), false);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-1,dp(50)); ip.setMargins(0,dp(8),0,0);
        box.addView(start,ip); box.addView(end,new LinearLayout.LayoutParams(-1,dp(50)));
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("تنظیم ساعت حضور").setView(box).setNegativeButton("حذف محدودیت",(d,w)->setAttendanceHours("","",true)).setPositiveButton("ثبت",(d,w)->setAttendanceHours(start.getText().toString(),end.getText().toString(),false)).create();
        styleMeelanoDialog(dialog, navAccent("attendance")); dialog.show();
    }

    private void setAttendanceHours(String start, String end, boolean clear) {
        runDb(() -> { try(Connection c=openConnection()){ ensureMeelanoCollabTables(c); setChatSetting(c,"attendance_start", clear?"":(start==null?"":start.trim())); setChatSetting(c,"attendance_end", clear?"":(end==null?"":end.trim())); } if(prefs!=null)prefs.edit().putString("attendance_start_hint",start==null?"":start.trim()).putString("attendance_end_hint",end==null?"":end.trim()).apply(); return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this,"بازه حضور ذخیره شد.",Toast.LENGTH_SHORT).show(); loadAttendance(); } @Override public void fail(Exception e){ showPageError("ساعت حضور",e,()->loadAttendance()); }});
    }

    private void addAttendanceRows(String title, JSONArray rows){
        LinearLayout c=card();
        c.setBackground(gradient(new int[]{alpha(navAccent("attendance"),22), alpha(SURFACE,250)},GradientDrawable.Orientation.RIGHT_LEFT,22));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("⏱", navAccent("attendance")), new LinearLayout.LayoutParams(dp(44), dp(44)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(9),0,dp(8),0);
        copy.addView(text(title,15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        copy.addView(text("خط زمانی ورود و خروج با رنگ‌بندی هماهنگ با تم فعال",9.7f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        head.addView(copy,new LinearLayout.LayoutParams(0,-2,1f));
        c.addView(head,new LinearLayout.LayoutParams(-1,-2));
        if(rows==null||rows.length()==0)c.addView(text("رکوردی ثبت نشده است.",11,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,dp(54)));
        else for(int i=0;i<Math.min(120,rows.length());i++){ JSONObject r=rows.optJSONObject(i); if(r!=null)c.addView(attendanceEventRow(r),compactRowLp()); }
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private LinearLayout attendanceEventRow(JSONObject r) {
        boolean in = "in".equals(r.optString("type"));
        int accent = in ? SUCCESS : WARNING;
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);
        line.setPadding(dp(8), dp(8), dp(8), dp(8));
        line.setBackground(roundedStroke(alpha(accent, isLightTheme() ? 13 : 24), 18, alpha(accent, 70)));
        LinearLayout rail = new LinearLayout(this);
        rail.setOrientation(LinearLayout.VERTICAL);
        rail.setGravity(Gravity.CENTER);
        TextView dot = report3dIcon(in ? "↘" : "↗", accent);
        rail.addView(dot, new LinearLayout.LayoutParams(dp(36), dp(36)));
        line.addView(rail, new LinearLayout.LayoutParams(dp(42), -2));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(8),0,dp(8),0);
        TextView name = text(r.optString("display", r.optString("username", "کاربر")) + " • " + r.optString("time", ""), 11.4f, TEXT, Typeface.BOLD);
        name.setSingleLine(true); name.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(name, new LinearLayout.LayoutParams(-1,-2));
        TextView meta = text("مودم: " + stringOr(r.optString("ssid", ""), "نامشخص") + " • BSSID: " + stringOr(r.optString("bssid", ""), "—"), 9.2f, MUTED, Typeface.NORMAL);
        meta.setSingleLine(true); meta.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(meta, new LinearLayout.LayoutParams(-1,-2));
        line.addView(copy, new LinearLayout.LayoutParams(0,-2,1f));
        line.addView(pill(in ? "ورود" : "خروج", accent, false), new LinearLayout.LayoutParams(-2,-2));
        return line;
    }

    private void notifyPendingLeavesOnce(JSONArray leaves) {
        if (leaves == null || leaves.length() == 0 || prefs == null) return;
        int pending = 0;
        for (int i=0;i<leaves.length();i++){ JSONObject r=leaves.optJSONObject(i); if(r!=null && "pending".equals(r.optString("status"))) pending++; }
        if (pending <= 0) return;
        long bucket = System.currentTimeMillis() / 3600000L;
        String key = "last_leave_notify_" + bucket;
        if (prefs.getBoolean(key, false)) return;
        prefs.edit().putBoolean(key, true).apply();
        showLocalNotification("درخواست مرخصی", formatNumber(pending) + " درخواست مرخصی در انتظار بررسی است.", false);
    }
    private void addLeaveList(String title, JSONArray rows, boolean admin){ LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(INFO,20),alpha(SURFACE,250)},GradientDrawable.Orientation.TL_BR,22)); c.addView(text(title,15,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); if(rows==null||rows.length()==0)c.addView(text("درخواستی ثبت نشده است.",11,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,dp(54))); else for(int i=0;i<Math.min(80,rows.length());i++){ JSONObject r=rows.optJSONObject(i); LinearLayout item=new LinearLayout(this); item.setOrientation(LinearLayout.VERTICAL); item.setPadding(dp(9),dp(8),dp(9),dp(8)); item.setBackground(roundedStroke(alpha(navAccent("attendance"),16),16,alpha(navAccent("attendance"),60))); item.addView(text("#"+r.optLong("id")+" • "+r.optString("display")+" • "+leaveStatusFa(r.optString("status")),11.2f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); item.addView(text(r.optString("type")+" • "+r.optString("start")+" تا "+r.optString("end")+" • "+r.optString("hours"),10.2f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); if(!r.optString("reason").isEmpty()) item.addView(text(r.optString("reason"),10.2f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); if(admin&&"pending".equals(r.optString("status"))){ LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); Button ok=primaryButton(withIcon("✓", "تأیید")); Button no=secondaryButton(withIcon("×", "رد")); long id=r.optLong("id"); ok.setOnClickListener(v->decideLeave(id,true)); no.setOnClickListener(v->decideLeave(id,false)); row.addView(ok,weightedButtonLp()); row.addView(no,weightedButtonLp()); item.addView(row,new LinearLayout.LayoutParams(-1,-2)); } LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(-1,-2); ip.setMargins(0,dp(7),0,0); c.addView(item,ip);} LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp); }
    private String leaveStatusFa(String s){ if("approved".equals(s))return "تأیید شده"; if("rejected".equals(s))return "رد شده"; return "در انتظار"; }

    private JSONObject currentWifiFingerprint(){ JSONObject o=new JSONObject(); try{ if(Build.VERSION.SDK_INT>=23&&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){ requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_WIFI_PERMISSION); o.put("error","نیاز به مجوز موقعیت برای خواندن نام WiFi"); return o;} WifiManager wm=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE); if(wm==null)return o; WifiInfo info=wm.getConnectionInfo(); if(info!=null){ String ssid=info.getSSID(); if(ssid!=null)ssid=ssid.replace("\"",""); o.put("ssid",ssid); o.put("bssid",stringOr(info.getBSSID(),"")); } try{ int g=wm.getDhcpInfo()==null?0:wm.getDhcpInfo().gateway; o.put("gateway", ((g)&0xff)+"."+((g>>8)&0xff)+"."+((g>>16)&0xff)+"."+((g>>24)&0xff)); }catch(Exception ignored){} }catch(Exception ignored){} return o; }
    private void captureWorkWifi(){ JSONObject w=currentWifiFingerprint(); runDb(() -> { try(Connection c=openConnection()){ ensureMeelanoCollabTables(c); setChatSetting(c,"work_wifi_ssid",w.optString("ssid","")); setChatSetting(c,"work_wifi_bssid",w.optString("bssid","")); setChatSetting(c,"work_wifi_gateway",w.optString("gateway","")); } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this,"مودم محل کار ثبت شد.",Toast.LENGTH_SHORT).show(); loadAttendance(); } @Override public void fail(Exception e){ showPageError("ثبت مودم",e,()->loadAttendance()); }}); }
    private void recordAttendance(String type){ JSONObject w=currentWifiFingerprint(); runDb(() -> { try(Connection c=openConnection()){ ensureMeelanoCollabTables(c); String ssid=chatSetting(c,"work_wifi_ssid",""); String bssid=chatSetting(c,"work_wifi_bssid",""); String gateway=chatSetting(c,"work_wifi_gateway",""); boolean ok=(!bssid.isEmpty()&&bssid.equalsIgnoreCase(w.optString("bssid")))||(!ssid.isEmpty()&&ssid.equals(w.optString("ssid")))||(!gateway.isEmpty()&&gateway.equals(w.optString("gateway"))); if(!ok) throw new DbException("برای ثبت حضور باید به مودم محل کار متصل باشید."); validateAttendanceTimeWindow(c); preventDuplicateAttendance(c,type); try(PreparedStatement ps=c.prepareStatement("INSERT INTO dbo.meelano_attendance(username,display_name,event_type,wifi_ssid,wifi_bssid,gateway,note) VALUES(?,?,?,?,?,?,?)")){ ps.setString(1,currentAccountName()); ps.setString(2,session==null?currentAccountName():session.userName); ps.setString(3,type); ps.setString(4,w.optString("ssid","")); ps.setString(5,w.optString("bssid","")); ps.setString(6,w.optString("gateway","")); ps.setString(7,"ثبت از موبایل"); ps.executeUpdate(); } } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this, "in".equals(type)?"حضور شما ثبت شد":"خروج شما ثبت شد", Toast.LENGTH_LONG).show(); loadAttendance(); } @Override public void fail(Exception e){ showPageError("ثبت حضور",e,()->loadAttendance()); }}); }

    private void validateAttendanceTimeWindow(Connection c) throws Exception {
        String start = chatSetting(c, "attendance_start", "");
        String end = chatSetting(c, "attendance_end", "");
        if (start == null || end == null || start.trim().isEmpty() || end.trim().isEmpty()) return;
        Calendar cal = Calendar.getInstance();
        int now = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        int s = parseHourMinute(start); int e = parseHourMinute(end);
        if (s < 0 || e < 0) return;
        boolean ok = s <= e ? (now >= s && now <= e) : (now >= s || now <= e);
        if (!ok) throw new DbException("ثبت حضور خارج از بازه مجاز مدیر است: " + start + " تا " + end);
    }

    private int parseHourMinute(String value) {
        if (value == null) return -1;
        String v = value.trim().replace('۰','0').replace('۱','1').replace('۲','2').replace('۳','3').replace('۴','4').replace('۵','5').replace('۶','6').replace('۷','7').replace('۸','8').replace('۹','9');
        try { String[] p=v.split(":"); int h=Integer.parseInt(p[0]); int m=p.length>1?Integer.parseInt(p[1]):0; if(h<0||h>23||m<0||m>59)return -1; return h*60+m; } catch(Exception ignored){ return -1; }
    }

    private void preventDuplicateAttendance(Connection c, String type) throws Exception {
        try (PreparedStatement ps = c.prepareStatement("SELECT TOP (1) DATEDIFF(minute,event_time,SYSDATETIME()) FROM dbo.meelano_attendance WHERE username=? AND event_type=? ORDER BY event_time DESC")) {
            ps.setString(1, currentAccountName()); ps.setString(2, type);
            try (ResultSet r = ps.executeQuery()) { if (r.next() && r.getLong(1) >= 0 && r.getLong(1) < 5) throw new DbException("این ورود/خروج همین چند دقیقه قبل ثبت شده است؛ برای جلوگیری از ثبت تکراری کمی صبر کنید."); }
        }
    }

    private void showLeaveRequestDialog(){
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(14),dp(12),dp(14),dp(8));
        box.addView(text("درخواست مرخصی",14.5f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        box.addView(text("نوع، تاریخ و توضیح کوتاه را کامل وارد کنید تا مدیر سریع‌تر تصمیم بگیرد.",10.2f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        final String[] type={"مرخصی استحقاقی"};
        Button typeBtn=secondaryButton(withIcon("☘", type[0]));
        typeBtn.setOnClickListener(v->{ String[] items={"مرخصی استحقاقی","مرخصی استعلاجی","مرخصی ساعتی","ماموریت","سایر"}; AlertDialog d=new AlertDialog.Builder(this).setItems(items,(di,which)->{type[0]=items[which]; typeBtn.setText(withIcon("☘", type[0]));}).create(); styleMeelanoDialog(d, navAccent("attendance")); d.show(); });
        Button start=secondaryButton(withIcon("◷", todayDateText())); Button end=secondaryButton(withIcon("◷", todayDateText()));
        start.setOnClickListener(v->showDatePickForButton(start)); end.setOnClickListener(v->showDatePickForButton(end));
        EditText hours=input("ساعت/مدت", "", false); EditText reason=input("توضیح درخواست", "", false); reason.setMinLines(2);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,dp(46)); lp.setMargins(0,dp(8),0,0);
        box.addView(typeBtn,lp); box.addView(start,new LinearLayout.LayoutParams(-1,dp(46))); box.addView(end,new LinearLayout.LayoutParams(-1,dp(46))); box.addView(hours,new LinearLayout.LayoutParams(-1,dp(46))); box.addView(reason,new LinearLayout.LayoutParams(-1,dp(76)));
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("درخواست مرخصی").setView(box).setNegativeButton("بستن",null).setPositiveButton("ارسال",(d,w)->submitLeaveRequest(type[0],start.getText().toString().replace("◷","").trim(),end.getText().toString().replace("◷","").trim(),hours.getText().toString(),reason.getText().toString())).create();
        styleMeelanoDialog(dialog, navAccent("attendance")); dialog.show();
    }

    private void showDatePickForButton(Button b){
        Calendar cal=Calendar.getInstance(new Locale("fa","IR")); DatePicker picker=new DatePicker(this); picker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),(v,y,m,d)->{});
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("انتخاب تاریخ").setView(picker).setNegativeButton("بستن",null).setPositiveButton("ثبت",(d,w)->b.setText(withIcon("◷", String.format(Locale.US,"%04d/%02d/%02d",picker.getYear(),picker.getMonth()+1,picker.getDayOfMonth())))).create();
        styleMeelanoDialog(dialog, navAccent("attendance")); dialog.show();
    }

    private void submitLeaveRequest(String type,String start,String end,String hours,String reason){
        if (type == null || type.trim().isEmpty()) { Toast.makeText(this,"نوع مرخصی را انتخاب کنید.",Toast.LENGTH_SHORT).show(); return; }
        if (start == null || start.trim().isEmpty() || end == null || end.trim().isEmpty()) { Toast.makeText(this,"تاریخ شروع و پایان الزامی است.",Toast.LENGTH_SHORT).show(); return; }
        if (reason == null || reason.trim().length() < 3) { Toast.makeText(this,"توضیح کوتاه درخواست را وارد کنید.",Toast.LENGTH_SHORT).show(); return; }
        runDb(() -> { try(Connection c=openConnection()){ ensureMeelanoCollabTables(c); try(PreparedStatement ps=c.prepareStatement("INSERT INTO dbo.meelano_leave_requests(username,display_name,leave_type,start_date,end_date,hours,reason) VALUES(?,?,?,?,?,?,?)")){ ps.setString(1,currentAccountName()); ps.setString(2,session==null?currentAccountName():session.userName); ps.setString(3,type); ps.setString(4,start); ps.setString(5,end); ps.setString(6,hours); ps.setString(7,reason); ps.executeUpdate(); } } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this,"درخواست مرخصی ارسال شد.",Toast.LENGTH_SHORT).show(); showLocalNotification("درخواست مرخصی", "درخواست شما ثبت شد و برای مدیر قابل مشاهده است.", false); loadAttendance(); } @Override public void fail(Exception e){ showPageError("مرخصی",e,()->loadAttendance()); }});
    }

    private void decideLeave(long id, boolean approve){ runDb(() -> { try(Connection c=openConnection()){ ensureMeelanoCollabTables(c); try(PreparedStatement ps=c.prepareStatement("UPDATE dbo.meelano_leave_requests SET status=?, decided_at=SYSDATETIME(), manager_note=? WHERE id=?")){ ps.setString(1,approve?"approved":"rejected"); ps.setString(2,approve?"تأیید مدیر":"رد مدیر"); ps.setLong(3,id); ps.executeUpdate(); } } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ loadAttendance(); } @Override public void fail(Exception e){ showPageError("مرخصی",e,()->loadAttendance()); }}); }

    private interface NetworkJob { String run() throws Exception; }
    private interface NetworkCallback { void ok(String body); void fail(Exception e); }

    private void runNetworkJob(String title, NetworkJob job, NetworkCallback callback) {
        setConnectionStatus("loading");
        executor.execute(() -> {
            try {
                String result = job.run();
                runOnUiThread(() -> { setConnectionStatus("connected"); if (callback != null) callback.ok(result); });
            } catch (Exception e) {
                runOnUiThread(() -> { setConnectionStatus("offline"); if (callback != null) callback.fail(e); else Toast.makeText(MainActivity.this, shortError(e), Toast.LENGTH_LONG).show(); });
            }
        });
    }

    private String prefString(String key, String def) { return prefs == null ? def : prefs.getString(key, def); }
    private int prefIntText(String key, int def) { try { return Integer.parseInt(prefString(key, String.valueOf(def)).trim()); } catch (Exception ignored) { return def; } }
    private String prefSecret(String key) { return prefs == null ? "" : unprotectSecret(prefs.getString(key, "")); }
    private void putSecret(SharedPreferences.Editor e, String key, String value) { if (e != null) e.putString(key, protectSecret(value == null ? "" : value)); }
    private Set<String> prefSetCopy(String key) { return prefs == null ? new HashSet<>() : new HashSet<>(prefs.getStringSet(key, new HashSet<String>())); }
    private void savePrefSet(String key, Set<String> set) { if (prefs != null) prefs.edit().putStringSet(key, set == null ? new HashSet<String>() : new HashSet<>(set)).apply(); }

    private void loadTaxpayers() {
        content.removeAllViews();
        addHero("مودیان", "اتصال مدیریتی سامانه مودیان، آماده‌سازی فاکتورهای فروش و برگشت، کنترل ارسال‌نشده‌ها و جلوگیری از ارسال اشتباه.");
        addTaxpayerStatusCard();
        addTaxpayerFilterCard();
        addTaxpayerReportCard();
        addLoading(content, "در حال فراخوانی فاکتورهای قابل ارسال…");
        runDb(() -> queryTaxInvoices(taxPeriod, taxDocType), new DbCallback() {
            @Override public void ok(String body) { try { renderTaxInvoices(new JSONArray(body)); } catch (Exception e) { showPageError("مودیان", e, () -> loadTaxpayers()); } }
            @Override public void fail(Exception e) { showPageError("مودیان", e, () -> loadTaxpayers()); }
        });
    }

    private void addTaxpayerStatusCard() {
        LinearLayout c = card();
        int accent = navAccent("taxpayers");
        c.setBackground(gradient(new int[]{alpha(accent, 32), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 24));
        c.addView(text("اتصال سامانه مودیان", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        String base = prefString(KEY_TAX_BASE_URL, "");
        String mem = prefString(KEY_TAX_MEMORY_ID, "");
        String eco = prefString(KEY_TAX_ECONOMIC_ID, "");
        String last = prefString(KEY_TAX_LAST_REPORT, "گزارشی ثبت نشده است.");
        c.addView(text((base.trim().isEmpty() ? "آدرس سرویس ثبت نشده" : "سرویس: " + base) + "\nشناسه حافظه: " + stringOr(mem, "ثبت نشده") + " • اقتصادی: " + stringOr(eco, "ثبت نشده") + "\nآخرین گزارش: " + last, 10.6f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button settings = primaryButton(withIcon("⚙", "تنظیمات دقیق")); settings.setTextSize(9.4f); settings.setOnClickListener(v -> { if (ensurePermission("tax_settings", "تنظیمات مودیان")) showTaxpayerSettingsDialog(); });
        Button test = secondaryButton(withIcon("✓", "تست اتصال")); test.setTextSize(9.4f); test.setOnClickListener(v -> { if (ensurePermission("tax_settings", "تست مودیان")) testTaxpayerConnection(); });
        row.addView(settings, weightedButtonLp()); row.addView(test, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private boolean ensureManagerOnly() {
        return ensurePermission("management_access", "مدیریت");
    }

    private boolean ensurePermission(String permission, String label) {
        if (canUsePermission(permission)) return true;
        Toast.makeText(this, "دسترسی «" + stringOr(label, permission) + "» برای این حساب فعال نیست.", Toast.LENGTH_LONG).show();
        return false;
    }

    private void addTaxpayerFilterCard() {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(GOLD, 18), alpha(INFO, 16), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("فراخوانی فاکتورها", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("بازه و نوع سند را انتخاب کن؛ فروش و برگشت از فروش جداگانه و با وضعیت ارسال نمایش داده می‌شوند.", 10.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout period = new LinearLayout(this); period.setOrientation(LinearLayout.HORIZONTAL);
        addTaxChip(period, "day", "روزانه", true); addTaxChip(period, "week", "هفتگی", true); addTaxChip(period, "month", "ماهانه", true);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, -2); pp.setMargins(0, dp(10), 0, dp(7)); c.addView(period, pp);
        LinearLayout type = new LinearLayout(this); type.setOrientation(LinearLayout.HORIZONTAL);
        addTaxChip(type, "all", "همه", false); addTaxChip(type, "sale", "فروش", false); addTaxChip(type, "return", "برگشت", false);
        c.addView(type, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addTaxChip(LinearLayout row, String key, String label, boolean periodMode) {
        boolean active = periodMode ? key.equals(taxPeriod) : key.equals(taxDocType);
        Button b = active ? primaryButton(label) : secondaryButton(label);
        b.setTextSize(9.4f);
        b.setOnClickListener(v -> { if (periodMode) taxPeriod = key; else taxDocType = key; taxSelectedKeys.clear(); loadTaxpayers(); });
        row.addView(b, weightedButtonLp());
    }

    private void addTaxpayerReportCard() {
        Set<String> sent = prefSetCopy(KEY_TAX_SENT), excluded = prefSetCopy(KEY_TAX_EXCLUDED), failed = prefSetCopy(KEY_TAX_FAILED);
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(SUCCESS, 16), alpha(WARNING, 14), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 22));
        c.addView(text("کنترل ارسال و عدم ارسال", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("ارسالی: " + formatNumber(sent.size()) + " • ارسال‌نشده/ناتمام: " + formatNumber(failed.size()) + " • کنارگذاشته‌شده: " + formatNumber(excluded.size()), 10.6f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button retry = primaryButton(withIcon("⟳", "ارسال مجدد ناقص‌ها")); retry.setTextSize(9.0f); retry.setOnClickListener(v -> retryFailedTaxInvoices());
        Button excludedBtn = secondaryButton(withIcon("⊘", "لیست کنارگذاشته")); excludedBtn.setTextSize(9.0f); excludedBtn.setOnClickListener(v -> showTaxKeyList("فاکتورهای کنارگذاشته‌شده", KEY_TAX_EXCLUDED));
        row.addView(retry, weightedButtonLp()); row.addView(excludedBtn, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void renderTaxInvoices(JSONArray rows) {
        taxCurrentInvoices = rows == null ? new JSONArray() : rows;
        content.removeAllViews();
        addHero("مودیان", "انتخاب امن فاکتورهای فروش و برگشت از فروش برای ارسال به سامانه.");
        addTaxpayerStatusCard(); addTaxpayerFilterCard(); addTaxpayerReportCard(); addTaxBatchActions();
        if (taxCurrentInvoices.length() == 0) { addEmptyTo(content, "در این بازه فاکتور قابل نمایش پیدا نشد یا همه در لیست کنارگذاشته‌شده هستند."); return; }
        for (int i = 0; i < taxCurrentInvoices.length(); i++) addTaxInvoiceCard(taxCurrentInvoices.optJSONObject(i));
    }

    private void addTaxBatchActions() {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(navAccent("taxpayers"), 24), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("عملیات گروهی", 15, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("انتخاب‌شده: " + formatNumber(taxSelectedKeys.size()) + " سند", 10.6f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        Button all = primaryButton(withIcon("✓", "انتخاب همه فیلتر")); all.setTextSize(8.8f); all.setOnClickListener(v -> { for (int i=0;i<taxCurrentInvoices.length();i++){ JSONObject o=taxCurrentInvoices.optJSONObject(i); if(o!=null && !"sent".equals(o.optString("status"))) taxSelectedKeys.add(o.optString("key")); } renderTaxInvoices(taxCurrentInvoices); });
        Button none = secondaryButton(withIcon("×", "لغو انتخاب")); none.setTextSize(8.8f); none.setOnClickListener(v -> { taxSelectedKeys.clear(); renderTaxInvoices(taxCurrentInvoices); });
        row1.addView(all, weightedButtonLp()); row1.addView(none, weightedButtonLp()); c.addView(row1, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        Button send = primaryButton(withIcon("⇧", "ارسال انتخابی")); send.setTextSize(8.8f); send.setOnClickListener(v -> sendSelectedTaxInvoices());
        Button hide = secondaryButton(withIcon("⊘", "عدم ارسال")); hide.setTextSize(8.8f); hide.setOnClickListener(v -> excludeSelectedTaxInvoices());
        row2.addView(send, weightedButtonLp()); row2.addView(hide, weightedButtonLp()); LinearLayout.LayoutParams r2p=new LinearLayout.LayoutParams(-1,-2); r2p.setMargins(0,dp(8),0,0); c.addView(row2,r2p);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addTaxInvoiceCard(JSONObject o) {
        if (o == null) return;
        String key = o.optString("key");
        boolean selected = taxSelectedKeys.contains(key);
        boolean sent = "sent".equals(o.optString("status"));
        boolean failed = "failed".equals(o.optString("status"));
        int accent = "return".equals(o.optString("type")) ? WARNING : SUCCESS;
        LinearLayout c = card(); c.setBackground(roundedStroke(alpha(accent, selected ? 34 : 16), 20, alpha(selected ? GOLD : accent, selected ? 120 : 65)));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        TextView glyph = report3dIcon("return".equals(o.optString("type")) ? "↩" : "٪", accent); head.addView(glyph, new LinearLayout.LayoutParams(dp(44), dp(44)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(9),0,dp(9),0);
        copy.addView(text(o.optString("typeFa") + " • شماره " + o.optString("number", "—") + (selected ? "  ✓" : ""), 12.7f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(o.optString("date", "—") + " • " + o.optString("party", "بدون نام") + " • " + money(o.opt("amount")), 10.3f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView badge = text(sent ? "ارسال‌شده" : (failed ? "ناقص" : "آماده"), 9.2f, sent ? SUCCESS : (failed ? DANGER : GOLD), Typeface.BOLD); badge.setGravity(Gravity.CENTER); badge.setBackground(roundedStroke(alpha(sent ? SUCCESS : (failed ? DANGER : GOLD), 16), 999, alpha(sent ? SUCCESS : (failed ? DANGER : GOLD), 70))); head.addView(badge, new LinearLayout.LayoutParams(dp(82), dp(32)));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("مالیات: " + money(o.opt("tax")) + " • تخفیف: " + money(o.opt("discount")) + " • دریافتی: " + money(o.opt("paid")), 10.1f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button pick = selected ? primaryButton(withIcon("✓", "انتخاب شد")) : secondaryButton(withIcon("□", "انتخاب")); pick.setTextSize(8.8f); pick.setOnClickListener(v -> { if (taxSelectedKeys.contains(key)) taxSelectedKeys.remove(key); else if (!sent) taxSelectedKeys.add(key); renderTaxInvoices(taxCurrentInvoices); });
        Button hide = secondaryButton(withIcon("⊘", "عدم ارسال")); hide.setTextSize(8.8f); hide.setOnClickListener(v -> { taxSelectedKeys.clear(); taxSelectedKeys.add(key); excludeSelectedTaxInvoices(); });
        row.addView(pick, weightedButtonLp()); row.addView(hide, weightedButtonLp()); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(8),0,0); c.addView(row,rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(9)); content.addView(c, lp);
    }

    private String queryTaxInvoices(String period, String type) throws Exception {
        JSONArray arr = new JSONArray();
        Set<String> excluded = prefSetCopy(KEY_TAX_EXCLUDED), sent = prefSetCopy(KEY_TAX_SENT), failed = prefSetCopy(KEY_TAX_FAILED);
        try (Connection c = openConnection()) {
            if (!"return".equals(type)) appendTaxInvoicesFromTable(c, arr, "sailfact", "sale", period, excluded, sent, failed);
            if (!"sale".equals(type)) {
                String[] tables = {"retsailfact","ret_sailfact","return_sailfact","bargsailfact","barge_sailfact","sailback","backsailfact","sale_return","sales_return","marjoei_sailfact","برگشت_فروش"};
                for (String t : tables) if (tableExists(c, t)) { appendTaxInvoicesFromTable(c, arr, t, "return", period, excluded, sent, failed); break; }
            }
        }
        return arr.toString();
    }

    private void appendTaxInvoicesFromTable(Connection c, JSONArray arr, String table, String kind, String period, Set<String> excluded, Set<String> sent, Set<String> failed) throws Exception {
        Set<String> cols = columns(c, table); if (cols == null || cols.isEmpty()) return;
        String date = resolveFlexible(cols, "date", "DATE", "tarikh", "Date", "t_date", "تاریخ");
        String number = resolveFlexible(cols, "shfacfo", "shfac", "factor_no", "invoice_no", "number", "serial", "شماره");
        String amount = resolveFlexible(cols, "all", "amount", "mablagh", "total", "Total", "sum", "جمع");
        if (number == null || amount == null) return;
        String shmo = resolveFlexible(cols, "shmo", "SHMO", "customer", "CustomerCode");
        String partyLocal = resolveFlexible(cols, "moname", "MONAME", "customer_name", "name", "Name");
        String tax = resolveFlexible(cols, "tax", "Tax", "maliat", "مالیات");
        String discount = resolveFlexible(cols, "tafif", "takhfif", "discount", "تخفیف");
        String paid = resolveFlexible(cols, "MabDaryaftFactor", "Daryaft", "received", "paid");
        String desc = resolveFlexible(cols, "description", "Explain", "tozihat", "شرح");
        Set<String> cust = columns(c, "CUSTOMERS");
        String join = ""; String partyExpr = partyLocal == null ? "N'بدون نام'" : "TRY_CONVERT(nvarchar(250),h.[" + partyLocal + "])";
        if (shmo != null && hasCol(cust, "SHMO") && hasCol(cust, "MONAME")) { join = " LEFT JOIN dbo.CUSTOMERS cu ON TRY_CONVERT(nvarchar(100),cu.SHMO)=TRY_CONVERT(nvarchar(100),h.[" + shmo + "])"; partyExpr = "COALESCE(TRY_CONVERT(nvarchar(250),cu.MONAME)," + partyExpr + ",N'بدون نام')"; }
        String latest = date == null ? "" : latestDate(c, table, date);
        int dateLimit = "month".equals(period) ? 31 : ("week".equals(period) ? 7 : 1);
        int top = "month".equals(period) ? 420 : ("week".equals(period) ? 180 : 80);
        String sql = "SELECT TOP (" + top + ") " + (date==null?"CAST(NULL AS nvarchar(30))":"TRY_CONVERT(nvarchar(30),h.["+date+"])") + ", TRY_CONVERT(nvarchar(80),h.["+number+"]), " + partyExpr + ", TRY_CONVERT(decimal(19,2),h.["+amount+"]), " + (tax==null?"CAST(0 AS decimal(19,2))":"TRY_CONVERT(decimal(19,2),h.["+tax+"])") + ", " + (discount==null?"CAST(0 AS decimal(19,2))":"TRY_CONVERT(decimal(19,2),h.["+discount+"])") + ", " + (paid==null?"CAST(0 AS decimal(19,2))":"TRY_CONVERT(decimal(19,2),h.["+paid+"])") + ", " + (desc==null?"CAST(NULL AS nvarchar(500))":"TRY_CONVERT(nvarchar(500),h.["+desc+"])") + " FROM dbo.["+table+"] h" + join + activeWhereForAlias(cols, "h") + " ORDER BY " + (date==null?"1":"h.["+date+"] DESC") + ", h.["+number+"] DESC";
        Set<String> seenDates = new HashSet<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) { try (ResultSet r = ps.executeQuery()) { while (r.next()) {
            String d = stringOr(r.getString(1), "—");
            if (dateLimit == 1 && !latest.isEmpty() && !latest.equals(d)) continue;
            if (!d.equals("—")) { seenDates.add(d); if (seenDates.size() > dateLimit) break; }
            String no = stringOr(r.getString(2), "—"); String key = kind + "|" + d + "|" + no;
            if (excluded != null && excluded.contains(key)) continue;
            JSONObject o = new JSONObject(); o.put("key", key); o.put("type", kind); o.put("typeFa", "return".equals(kind) ? "برگشت از فروش" : "فروش"); o.put("date", d); o.put("number", no); o.put("party", stringOr(r.getString(3), "بدون نام")); o.put("amount", r.getDouble(4)); o.put("tax", r.getDouble(5)); o.put("discount", r.getDouble(6)); o.put("paid", r.getDouble(7)); o.put("description", stringOr(r.getString(8), "")); o.put("status", sent.contains(key) ? "sent" : (failed.contains(key) ? "failed" : "ready")); arr.put(o);
        } } }
    }

    private String activeWhereForAlias(Set<String> cols, String alias) {
        String w = activeWhere(cols, alias);
        return w == null || w.trim().isEmpty() ? "" : " " + w;
    }

    private void showTaxpayerSettingsDialog() {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(10), dp(8), dp(10), dp(4));
        box.addView(text("تنظیمات سامانه مودیان", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        EditText base = input("آدرس سرویس/درگاه ارسال سامانه مودیان", prefString(KEY_TAX_BASE_URL, ""), false);
        EditText mem = input("شناسه حافظه مالیاتی", prefString(KEY_TAX_MEMORY_ID, ""), false);
        EditText eco = input("شناسه اقتصادی/ملی", prefString(KEY_TAX_ECONOMIC_ID, ""), false);
        EditText client = input("Client ID / شناسه کاربری سرویس", prefString(KEY_TAX_CLIENT_ID, ""), false);
        EditText auth = input("کلید احراز / Token" + (prefSecret(KEY_TAX_AUTH_KEY).isEmpty()?"":" • ذخیره‌شده"), "", true);
        EditText pk = input("کلید خصوصی/امضای دیجیتال" + (prefSecret(KEY_TAX_PRIVATE_KEY).isEmpty()?"":" • ذخیره‌شده"), "", true);
        for (EditText e : new EditText[]{base,mem,eco,client,auth,pk}) { LinearLayout.LayoutParams ep=new LinearLayout.LayoutParams(-1, dp(48)); ep.setMargins(0, dp(8), 0, 0); box.addView(e, ep); }
        AlertDialog dlg = new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن", null).setPositiveButton("ذخیره", null).create();
        dlg.setOnShowListener(di -> { styleMeelanoDialog(dlg, navAccent("taxpayers")); Button ok = dlg.getButton(AlertDialog.BUTTON_POSITIVE); if (ok != null) ok.setOnClickListener(v -> { SharedPreferences.Editor ed=prefs.edit(); ed.putString(KEY_TAX_BASE_URL, base.getText().toString().trim()); ed.putString(KEY_TAX_MEMORY_ID, mem.getText().toString().trim()); ed.putString(KEY_TAX_ECONOMIC_ID, eco.getText().toString().trim()); ed.putString(KEY_TAX_CLIENT_ID, client.getText().toString().trim()); if(auth.getText()!=null && auth.getText().toString().trim().length()>0) putSecret(ed, KEY_TAX_AUTH_KEY, auth.getText().toString()); if(pk.getText()!=null && pk.getText().toString().trim().length()>0) putSecret(ed, KEY_TAX_PRIVATE_KEY, pk.getText().toString()); ed.apply(); dlg.dismiss(); Toast.makeText(this,"تنظیمات مودیان ذخیره شد.",Toast.LENGTH_SHORT).show(); loadTaxpayers(); }); });
        dlg.show();
    }

    private void testTaxpayerConnection() {
        String url = prefString(KEY_TAX_BASE_URL, "").trim(); if (url.isEmpty()) { Toast.makeText(this,"ابتدا آدرس سرویس سامانه مودیان را وارد کنید.",Toast.LENGTH_LONG).show(); return; }
        runNetworkJob("tax-test", () -> httpRequest(url, "GET", null, prefSecret(KEY_TAX_AUTH_KEY), 9000), new NetworkCallback(){ @Override public void ok(String b){ prefs.edit().putString(KEY_TAX_LAST_REPORT, "تست اتصال موفق • " + nowText()).apply(); Toast.makeText(MainActivity.this,"اتصال سامانه مودیان موفق بود.",Toast.LENGTH_LONG).show(); loadTaxpayers(); } @Override public void fail(Exception e){ prefs.edit().putString(KEY_TAX_LAST_REPORT, "تست ناموفق • " + shortError(e)).apply(); showPageError("تست مودیان", e, () -> loadTaxpayers()); }});
    }

    private void sendSelectedTaxInvoices() {
        if (!ensurePermission("tax_send", "ارسال مودیان")) return;
        if (taxSelectedKeys.isEmpty()) { Toast.makeText(this,"هیچ فاکتوری انتخاب نشده است.",Toast.LENGTH_SHORT).show(); return; }
        JSONArray selected = selectedTaxRows(taxSelectedKeys);
        runNetworkJob("tax-send", () -> sendTaxRows(selected), new NetworkCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this,b,Toast.LENGTH_LONG).show(); taxSelectedKeys.clear(); loadTaxpayers(); } @Override public void fail(Exception e){ showPageError("ارسال مودیان", e, () -> loadTaxpayers()); }});
    }

    private JSONArray selectedTaxRows(Set<String> keys) {
        JSONArray arr = new JSONArray(); if (keys == null) return arr;
        for (int i=0;i<taxCurrentInvoices.length();i++){ JSONObject o=taxCurrentInvoices.optJSONObject(i); if(o!=null && keys.contains(o.optString("key"))) arr.put(o); }
        return arr;
    }

    private String sendTaxRows(JSONArray rows) throws Exception {
        String endpoint = prefString(KEY_TAX_BASE_URL, "").trim(); if (endpoint.isEmpty()) throw new DbException("آدرس سرویس مودیان تنظیم نشده است.");
        Set<String> sent = prefSetCopy(KEY_TAX_SENT), failed = prefSetCopy(KEY_TAX_FAILED); int ok=0, bad=0;
        for (int i=0;i<rows.length();i++) { JSONObject row = rows.optJSONObject(i); if(row==null) continue; String key=row.optString("key"); try { JSONObject payload = buildTaxPayload(row); httpRequest(endpoint, "POST", payload.toString(), prefSecret(KEY_TAX_AUTH_KEY), 20000); sent.add(key); failed.remove(key); ok++; } catch(Exception ex){ failed.add(key); bad++; } }
        savePrefSet(KEY_TAX_SENT, sent); savePrefSet(KEY_TAX_FAILED, failed); String report = "ارسال موفق: " + formatNumber(ok) + " • ناقص: " + formatNumber(bad) + " • " + nowText(); prefs.edit().putString(KEY_TAX_LAST_REPORT, report).apply(); return report;
    }

    private JSONObject buildTaxPayload(JSONObject row) throws Exception {
        JSONObject payload = new JSONObject(); payload.put("memoryId", prefString(KEY_TAX_MEMORY_ID, "")); payload.put("economicId", prefString(KEY_TAX_ECONOMIC_ID, "")); payload.put("clientId", prefString(KEY_TAX_CLIENT_ID, "")); payload.put("signedByDevice", true); payload.put("createdAt", nowText()); payload.put("invoice", row); return payload;
    }

    private void excludeSelectedTaxInvoices() {
        if (taxSelectedKeys.isEmpty()) { Toast.makeText(this,"برای انتقال به عدم ارسال، ابتدا سند را انتخاب کنید.",Toast.LENGTH_SHORT).show(); return; }
        Set<String> excluded = prefSetCopy(KEY_TAX_EXCLUDED); excluded.addAll(taxSelectedKeys); savePrefSet(KEY_TAX_EXCLUDED, excluded); taxSelectedKeys.clear(); Toast.makeText(this,"به لیست عدم ارسال منتقل شد و از جستجوهای بعدی حذف می‌شود.",Toast.LENGTH_LONG).show(); loadTaxpayers();
    }

    private void retryFailedTaxInvoices() {
        Set<String> failed = prefSetCopy(KEY_TAX_FAILED); if (failed.isEmpty()) { Toast.makeText(this,"ارسال ناقصی برای تلاش مجدد ثبت نشده است.",Toast.LENGTH_SHORT).show(); return; }
        taxSelectedKeys.clear(); taxSelectedKeys.addAll(failed); Toast.makeText(this,"اسناد ناقص انتخاب شدند؛ اگر در لیست بازه فعلی باشند می‌توانید ارسال مجدد بزنید.",Toast.LENGTH_LONG).show(); loadTaxpayers();
    }

    private void showTaxKeyList(String title, String prefKey) {
        Set<String> set = prefSetCopy(prefKey); LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12),dp(10),dp(12),dp(6)); box.addView(text(title,16,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        TextView body=text(set.isEmpty()?"موردی ثبت نشده است.":join(new ArrayList<>(set), "\n"),10.5f,MUTED,Typeface.NORMAL); body.setLineSpacing(dp(2),1.05f); box.addView(body,new LinearLayout.LayoutParams(-1,-2));
        AlertDialog dlg=new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن",null).setPositiveButton("پاک کردن لیست",(d,w)->{ savePrefSet(prefKey,new HashSet<String>()); loadTaxpayers(); }).create(); styleMeelanoDialog(dlg, navAccent("taxpayers")); dlg.show();
    }

    private String httpRequest(String target, String method, String body, String bearer, int timeoutMs) throws Exception {
        String u = target.startsWith("http://") || target.startsWith("https://") ? target : "http://" + target;
        HttpURLConnection con = (HttpURLConnection) new URL(u).openConnection(); con.setConnectTimeout(timeoutMs); con.setReadTimeout(timeoutMs); con.setRequestMethod(method == null ? "GET" : method); con.setRequestProperty("Accept", "application/json,text/plain,*/*");
        if (bearer != null && !bearer.trim().isEmpty()) con.setRequestProperty("Authorization", "Bearer " + bearer.trim());
        String mem = prefString(KEY_TAX_MEMORY_ID, ""); if (!mem.isEmpty()) con.setRequestProperty("X-Tax-Memory-Id", mem);
        if (body != null) { con.setDoOutput(true); con.setRequestProperty("Content-Type", "application/json; charset=utf-8"); try(OutputStream os=con.getOutputStream()){ os.write(body.getBytes(StandardCharsets.UTF_8)); } }
        int code = con.getResponseCode(); InputStream in = code >= 200 && code < 300 ? con.getInputStream() : con.getErrorStream(); String resp = readStreamText(in); if (code < 200 || code >= 300) throw new DbException("HTTP " + code + " • " + limitText(resp, 160)); return resp;
    }

    private String readStreamText(InputStream in) throws Exception {
        if (in == null) return ""; ByteArrayOutputStream bos = new ByteArrayOutputStream(); byte[] buf = new byte[4096]; int n; while((n=in.read(buf))>0) bos.write(buf,0,n); return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }

    private void renderCamerasPage() {
        content.removeAllViews();
        addHero("دوربین", "پخش زنده DVR/NVR با ذخیره امن مشخصات، چیدمان دلخواه، صدا، بزرگ‌نمایی و ورود سریع به بازپخش.");
        addCameraSettingsCard(); addCameraGridCard(); addCameraReplayCard();
    }

    private void addCameraSettingsCard() {
        LinearLayout c=card(); int accent=navAccent("cameras"); c.setBackground(gradient(new int[]{alpha(accent,30),alpha(SURFACE,248)},GradientDrawable.Orientation.TL_BR,24));
        c.addView(text("تنظیمات DVR",15.5f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("IP: "+stringOr(prefString(KEY_CAMERA_HOST,""),"ثبت نشده")+" • Port: "+prefString(KEY_CAMERA_PORT,"554")+" • کانال‌ها: "+prefString(KEY_CAMERA_CHANNELS,"4")+" • چیدمان: "+prefString(KEY_CAMERA_LAYOUT,"2"),10.5f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); Button set=primaryButton(withIcon("⚙","تنظیم DVR")); Button test=secondaryButton(withIcon("⌁","تست پورت")); set.setTextSize(9.2f); test.setTextSize(9.2f); set.setOnClickListener(v->showCameraSettingsDialog()); test.setOnClickListener(v->testCameraConnection()); row.addView(set,weightedButtonLp()); row.addView(test,weightedButtonLp()); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(10),0,0); c.addView(row,rp); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private void showCameraSettingsDialog() {
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(10),dp(8),dp(10),dp(4)); box.addView(text("تنظیمات دوربین مداربسته",16,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        EditText host=input("IP یا دامنه DVR",prefString(KEY_CAMERA_HOST,""),false); EditText port=input("Port RTSP/HTTP",prefString(KEY_CAMERA_PORT,"554"),false); EditText user=input("نام کاربری DVR",prefString(KEY_CAMERA_USER,""),false); EditText pass=input("کلمه عبور"+(prefSecret(KEY_CAMERA_PASS).isEmpty()?"":" • ذخیره‌شده"),"",true); EditText channels=input("تعداد کانال",prefString(KEY_CAMERA_CHANNELS,"4"),false); EditText tpl=input("قالب پخش زنده",prefString(KEY_CAMERA_TEMPLATE,"rtsp://{user}:{pass}@{host}:{port}/cam/realmonitor?channel={ch}&subtype=0"),false); EditText replay=input("قالب بازپخش",prefString(KEY_CAMERA_REPLAY_TEMPLATE,"rtsp://{user}:{pass}@{host}:{port}/cam/playback?channel={ch}"),false);
        for(EditText e:new EditText[]{host,port,user,pass,channels,tpl,replay}){ LinearLayout.LayoutParams ep=new LinearLayout.LayoutParams(-1,dp(48)); ep.setMargins(0,dp(8),0,0); box.addView(e,ep); }
        AlertDialog dlg=new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن",null).setPositiveButton("ذخیره",null).create(); dlg.setOnShowListener(di->{ styleMeelanoDialog(dlg,navAccent("cameras")); Button ok=dlg.getButton(AlertDialog.BUTTON_POSITIVE); if(ok!=null)ok.setOnClickListener(v->{ SharedPreferences.Editor ed=prefs.edit(); ed.putString(KEY_CAMERA_HOST,host.getText().toString().trim()); ed.putString(KEY_CAMERA_PORT,port.getText().toString().trim()); ed.putString(KEY_CAMERA_USER,user.getText().toString().trim()); ed.putString(KEY_CAMERA_CHANNELS,channels.getText().toString().trim()); ed.putString(KEY_CAMERA_TEMPLATE,tpl.getText().toString().trim()); ed.putString(KEY_CAMERA_REPLAY_TEMPLATE,replay.getText().toString().trim()); if(pass.getText()!=null&&pass.getText().toString().trim().length()>0)putSecret(ed,KEY_CAMERA_PASS,pass.getText().toString()); ed.apply(); dlg.dismiss(); renderCamerasPage(); }); }); dlg.show();
    }

    private void addCameraGridCard() {
        int channels=Math.max(1,Math.min(32,prefIntText(KEY_CAMERA_CHANNELS,4))); int cols=Math.max(1,Math.min(4,prefIntText(KEY_CAMERA_LAYOUT,2)));
        LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(navAccent("cameras"),18),alpha(SURFACE,248)},GradientDrawable.Orientation.RIGHT_LEFT,22)); c.addView(text("مانیتورینگ زنده",15.5f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        LinearLayout layoutRow=new LinearLayout(this); layoutRow.setOrientation(LinearLayout.HORIZONTAL); for(int k=1;k<=4;k++){ final int kk=k; Button b=(kk==cols?primaryButton(kk+" ستونه"):secondaryButton(kk+" ستونه")); b.setTextSize(8.4f); b.setOnClickListener(v->{ prefs.edit().putString(KEY_CAMERA_LAYOUT,String.valueOf(kk)).apply(); renderCamerasPage(); }); layoutRow.addView(b,weightedButtonLp()); } LinearLayout.LayoutParams lr=new LinearLayout.LayoutParams(-1,-2); lr.setMargins(0,dp(8),0,dp(8)); c.addView(layoutRow,lr);
        LinearLayout current=null; for(int i=1;i<=channels;i++){ if((i-1)%cols==0){ current=new LinearLayout(this); current.setOrientation(LinearLayout.HORIZONTAL); LinearLayout.LayoutParams cr=new LinearLayout.LayoutParams(-1,-2); cr.setMargins(0,dp(6),0,0); c.addView(current,cr);} addCameraTile(current,i); }
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private void addCameraTile(LinearLayout row, int ch) {
        LinearLayout tile=new LinearLayout(this); tile.setOrientation(LinearLayout.VERTICAL); tile.setGravity(Gravity.CENTER); tile.setPadding(dp(8),dp(8),dp(8),dp(8)); tile.setBackground(roundedStroke(alpha(navAccent("cameras"),18),18,alpha(navAccent("cameras"),70))); tile.addView(report3dIcon("◎",navAccent("cameras")),new LinearLayout.LayoutParams(dp(46),dp(46))); TextView name=text("دوربین " + formatNumber(ch),10.5f,TEXT,Typeface.BOLD); name.setGravity(Gravity.CENTER); tile.addView(name,new LinearLayout.LayoutParams(-1,-2)); Button live=secondaryButton("پخش"); live.setTextSize(8.2f); live.setOnClickListener(v->showCameraPlayer(ch,false)); tile.addView(live,new LinearLayout.LayoutParams(-1,dp(36))); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(142),1f); lp.setMargins(dp(3),0,dp(3),0); if(row!=null)row.addView(tile,lp);
    }

    private void addCameraReplayCard(){
        LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(INFO,14),alpha(GOLD,12),alpha(SURFACE,248)},GradientDrawable.Orientation.TL_BR,22));
        c.addView(text("بازپخش و مرور فیلم‌ها",15.5f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("کانال و قالب بازپخش قابل تنظیم است؛ اگر DVR پارامتر زمان را لازم داشته باشد آن را داخل قالب replay با متغیرهای دستگاه وارد کنید.",10.4f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        EditText ch=input("شماره کانال برای بازپخش", "1", false); LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,dp(46)); cp.setMargins(0,dp(9),0,0); c.addView(ch,cp);
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button b=primaryButton(withIcon("◷","بازپخش")); Button live=secondaryButton(withIcon("▣","پخش همان کانال")); b.setTextSize(9.2f); live.setTextSize(9.2f);
        b.setOnClickListener(v->{ int channel=1; try{channel=Integer.parseInt(ch.getText().toString().trim());}catch(Exception ignored){} showCameraPlayer(Math.max(1,channel),true); });
        live.setOnClickListener(v->{ int channel=1; try{channel=Integer.parseInt(ch.getText().toString().trim());}catch(Exception ignored){} showCameraPlayer(Math.max(1,channel),false); });
        row.addView(b,weightedButtonLp()); row.addView(live,weightedButtonLp()); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(9),0,0); c.addView(row,rp);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp);
    }

    private void testCameraConnection(){ String host=prefString(KEY_CAMERA_HOST,"").trim(); int port=prefIntText(KEY_CAMERA_PORT,554); if(host.isEmpty()){Toast.makeText(this,"IP دوربین را ثبت کنید.",Toast.LENGTH_SHORT).show();return;} runNetworkJob("camera-test",()->testTcp(host,port),new NetworkCallback(){@Override public void ok(String b){Toast.makeText(MainActivity.this,"ارتباط با DVR برقرار شد.",Toast.LENGTH_LONG).show();}@Override public void fail(Exception e){showPageError("تست دوربین",e,()->renderCamerasPage());}}); }

    private String testTcp(String host,int port)throws Exception{ try(Socket s=new Socket()){ s.connect(new InetSocketAddress(host,port),7000); return "ok"; } }

    private String cameraUrl(int ch, boolean replay){ String tpl=prefString(replay?KEY_CAMERA_REPLAY_TEMPLATE:KEY_CAMERA_TEMPLATE, replay?"rtsp://{user}:{pass}@{host}:{port}/cam/playback?channel={ch}":"rtsp://{user}:{pass}@{host}:{port}/cam/realmonitor?channel={ch}&subtype=0"); return tpl.replace("{host}",prefString(KEY_CAMERA_HOST,"")).replace("{port}",prefString(KEY_CAMERA_PORT,"554")).replace("{user}",urlPart(prefString(KEY_CAMERA_USER,""))).replace("{pass}",urlPart(prefSecret(KEY_CAMERA_PASS))).replace("{ch}",String.valueOf(ch)); }
    private String urlPart(String v){ try{return URLEncoder.encode(v==null?"":v,"UTF-8");}catch(Exception ignored){return v==null?"":v;} }

    private void showCameraPlayer(int ch, boolean replay){ String url=cameraUrl(ch,replay); if(prefString(KEY_CAMERA_HOST,"").trim().isEmpty()){Toast.makeText(this,"ابتدا تنظیمات DVR را ثبت کنید.",Toast.LENGTH_SHORT).show();return;} LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(8),dp(8),dp(8),dp(4)); box.addView(text((replay?"بازپخش ":"پخش زنده ")+"دوربین "+formatNumber(ch),16,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); VideoView video=new VideoView(this); MediaController mc=new MediaController(this); mc.setAnchorView(video); video.setMediaController(mc); video.setVideoURI(Uri.parse(url)); FrameLayout wrap=new FrameLayout(this); wrap.setBackgroundColor(Color.BLACK); wrap.addView(video,new FrameLayout.LayoutParams(-1,-1,Gravity.CENTER)); LinearLayout.LayoutParams vp=new LinearLayout.LayoutParams(-1,dp(280)); vp.setMargins(0,dp(8),0,dp(8)); box.addView(wrap,vp); LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); Button play=primaryButton("شروع پخش"); Button zoom=secondaryButton("زوم ×۱.۳"); Button external=secondaryButton("پلیر خارجی"); final float[] scale={1f}; play.setOnClickListener(v->{ try{video.start();}catch(Exception ex){Toast.makeText(this,"پخش شروع نشد: "+shortError(ex),Toast.LENGTH_LONG).show();} }); zoom.setOnClickListener(v->{ scale[0]=scale[0]<1.6f?scale[0]+0.3f:1f; video.setScaleX(scale[0]); video.setScaleY(scale[0]); zoom.setText("زوم ×"+String.format(Locale.US,"%.1f",scale[0])); }); external.setOnClickListener(v->openExternalStream(url)); row.addView(play,weightedButtonLp()); row.addView(zoom,weightedButtonLp()); row.addView(external,weightedButtonLp()); box.addView(row,new LinearLayout.LayoutParams(-1,-2)); AlertDialog dlg=new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن",(d,w)->{try{video.stopPlayback();}catch(Exception ignored){}}).create(); styleMeelanoDialog(dlg,navAccent("cameras")); dlg.setOnShowListener(d-> { try{ video.start(); }catch(Exception ignored){} }); dlg.show(); }


    private void openExternalStream(String url){ try{ Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(url)); i.setDataAndType(Uri.parse(url), "video/*"); startActivity(Intent.createChooser(i,"باز کردن تصویر دوربین")); }catch(Exception ex){ Toast.makeText(this,"پلیر سازگار پیدا نشد.",Toast.LENGTH_LONG).show(); } }

    private void renderAlarmPage(){ content.removeAllViews(); addHero("دزدگیر", "مدیریت حرفه‌ای دو دستگاه دزدگیر Z4 و Extra G1 با ذخیره امن مشخصات، تست ارتباط و فرمان‌های سریع."); addAlarmOverviewCard(); addAlarmDeviceCard(1); addAlarmDeviceCard(2); }

    private String alarmKey(int index,String name){ return "alarm_"+index+"_"+name; }
    private String alarmSecret(int index,String name){ return prefSecret(alarmKey(index,name)); }
    private String alarmString(int index,String name,String def){ return prefString(alarmKey(index,name),def); }

    private void addAlarmOverviewCard(){ LinearLayout c=card(); c.setBackground(gradient(new int[]{alpha(navAccent("alarm"),26),alpha(SURFACE,248)},GradientDrawable.Orientation.TL_BR,24)); c.addView(text("کنسول امنیت فروشگاه",16,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); c.addView(text("آخرین رویداد: "+prefString(KEY_ALARM_LOG,"ثبت نشده"),10.6f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); Button d1=alarmActiveDevice==1?primaryButton("دزدگیر ۱"):secondaryButton("دزدگیر ۱"); Button d2=alarmActiveDevice==2?primaryButton("دزدگیر ۲"):secondaryButton("دزدگیر ۲"); d1.setOnClickListener(v->{alarmActiveDevice=1;renderAlarmPage();}); d2.setOnClickListener(v->{alarmActiveDevice=2;renderAlarmPage();}); row.addView(d1,weightedButtonLp()); row.addView(d2,weightedButtonLp()); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(10),0,0); c.addView(row,rp); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp); }

    private void addAlarmDeviceCard(int index){ boolean active=index==alarmActiveDevice; int accent=active?navAccent("alarm"):INFO; LinearLayout c=card(); c.setBackground(roundedStroke(alpha(accent,active?28:14),22,alpha(accent,active?95:55))); c.addView(text("دزدگیر "+formatNumber(index)+" • "+alarmString(index,"model",index==1?"Z4":"Extra G1"),15.5f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); c.addView(text("IP: "+stringOr(alarmString(index,"host",""),"ثبت نشده")+" • Port: "+alarmString(index,"port","80")+" • مسیرها قابل تنظیم هستند",10.4f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); LinearLayout row1=new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL); Button set=secondaryButton(withIcon("⚙","تعریف/تنظیم")); Button test=secondaryButton(withIcon("⌁","تست")); set.setTextSize(8.7f); test.setTextSize(8.7f); set.setOnClickListener(v->showAlarmSettingsDialog(index)); test.setOnClickListener(v->sendAlarmCommand(index,"status")); row1.addView(set,weightedButtonLp()); row1.addView(test,weightedButtonLp()); LinearLayout.LayoutParams r1=new LinearLayout.LayoutParams(-1,-2); r1.setMargins(0,dp(9),0,0); c.addView(row1,r1); LinearLayout row2=new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL); Button arm=primaryButton(withIcon("⌁","فعال")); Button home=secondaryButton(withIcon("◐","نیمه")); Button off=secondaryButton(withIcon("○","غیرفعال")); arm.setTextSize(8.4f); home.setTextSize(8.4f); off.setTextSize(8.4f); arm.setOnClickListener(v->sendAlarmCommand(index,"arm")); home.setOnClickListener(v->sendAlarmCommand(index,"home")); off.setOnClickListener(v->sendAlarmCommand(index,"disarm")); row2.addView(arm,weightedButtonLp()); row2.addView(home,weightedButtonLp()); row2.addView(off,weightedButtonLp()); LinearLayout.LayoutParams r2=new LinearLayout.LayoutParams(-1,-2); r2.setMargins(0,dp(8),0,0); c.addView(row2,r2); LinearLayout row3=new LinearLayout(this); row3.setOrientation(LinearLayout.HORIZONTAL); Button panic=secondaryButton(withIcon("!","آژیر/هشدار")); Button sirenOff=secondaryButton(withIcon("×","قطع آژیر")); panic.setTextSize(8.4f); sirenOff.setTextSize(8.4f); panic.setOnClickListener(v->sendAlarmCommand(index,"panic")); sirenOff.setOnClickListener(v->sendAlarmCommand(index,"sirenOff")); row3.addView(panic,weightedButtonLp()); row3.addView(sirenOff,weightedButtonLp()); LinearLayout.LayoutParams r3=new LinearLayout.LayoutParams(-1,-2); r3.setMargins(0,dp(8),0,0); c.addView(row3,r3); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); content.addView(c,lp); }

    private void showAlarmSettingsDialog(int index){ LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(10),dp(8),dp(10),dp(4)); box.addView(text("تعریف دزدگیر "+formatNumber(index),16,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); EditText model=input("مدل: Z4 یا Extra G1",alarmString(index,"model",index==1?"Z4":"Extra G1"),false); EditText host=input("IP یا دامنه دستگاه",alarmString(index,"host",""),false); EditText port=input("Port",alarmString(index,"port","80"),false); EditText user=input("نام کاربری",alarmString(index,"user",""),false); EditText pass=input("کلمه عبور"+(alarmSecret(index,"pass").isEmpty()?"":" • ذخیره‌شده"),"",true); EditText token=input("Token/کلید"+(alarmSecret(index,"token").isEmpty()?"":" • ذخیره‌شده"),"",true); EditText arm=input("مسیر فعال‌سازی",alarmString(index,"arm","/arm"),false); EditText dis=input("مسیر غیرفعال",alarmString(index,"disarm","/disarm"),false); EditText home=input("مسیر نیمه‌فعال",alarmString(index,"home","/home"),false); EditText status=input("مسیر وضعیت",alarmString(index,"status","/status"),false); EditText panic=input("مسیر آژیر/هشدار",alarmString(index,"panic","/panic"),false); EditText sirenOff=input("مسیر قطع آژیر",alarmString(index,"sirenOff","/siren-off"),false); for(EditText e:new EditText[]{model,host,port,user,pass,token,arm,dis,home,status,panic,sirenOff}){ LinearLayout.LayoutParams ep=new LinearLayout.LayoutParams(-1,dp(48)); ep.setMargins(0,dp(7),0,0); box.addView(e,ep);} AlertDialog dlg=new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن",null).setPositiveButton("ذخیره",null).create(); dlg.setOnShowListener(di->{ styleMeelanoDialog(dlg,navAccent("alarm")); Button ok=dlg.getButton(AlertDialog.BUTTON_POSITIVE); if(ok!=null)ok.setOnClickListener(v->{ SharedPreferences.Editor ed=prefs.edit(); ed.putString(alarmKey(index,"model"),model.getText().toString().trim()); ed.putString(alarmKey(index,"host"),host.getText().toString().trim()); ed.putString(alarmKey(index,"port"),port.getText().toString().trim()); ed.putString(alarmKey(index,"user"),user.getText().toString().trim()); ed.putString(alarmKey(index,"arm"),arm.getText().toString().trim()); ed.putString(alarmKey(index,"disarm"),dis.getText().toString().trim()); ed.putString(alarmKey(index,"home"),home.getText().toString().trim()); ed.putString(alarmKey(index,"status"),status.getText().toString().trim()); ed.putString(alarmKey(index,"panic"),panic.getText().toString().trim()); ed.putString(alarmKey(index,"sirenOff"),sirenOff.getText().toString().trim()); if(pass.getText()!=null&&pass.getText().toString().trim().length()>0)putSecret(ed,alarmKey(index,"pass"),pass.getText().toString()); if(token.getText()!=null&&token.getText().toString().trim().length()>0)putSecret(ed,alarmKey(index,"token"),token.getText().toString()); ed.apply(); dlg.dismiss(); renderAlarmPage(); }); }); dlg.show(); }

    private void sendAlarmCommand(int index,String cmd){ String host=alarmString(index,"host","").trim(); if(host.isEmpty()){Toast.makeText(this,"ابتدا دستگاه "+index+" را تعریف کنید.",Toast.LENGTH_SHORT).show();return;} String path=alarmString(index,cmd,"/"+cmd); String url="http://"+host+":"+alarmString(index,"port","80")+(path.startsWith("/")?path:"/"+path); String token=alarmSecret(index,"token"); runNetworkJob("alarm",()->httpRequestWithBasic(url,"GET",null,token,alarmString(index,"user",""),alarmSecret(index,"pass"),10000),new NetworkCallback(){@Override public void ok(String b){String msg="دزدگیر "+index+" • "+alarmCommandFa(cmd)+" موفق • "+nowText(); prefs.edit().putString(KEY_ALARM_LOG,msg).apply(); Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show(); renderAlarmPage();}@Override public void fail(Exception e){String msg="دزدگیر "+index+" ناموفق: "+shortError(e); prefs.edit().putString(KEY_ALARM_LOG,msg).apply(); showPageError("دزدگیر",e,()->renderAlarmPage());}}); }

    private String alarmCommandFa(String c){ if("arm".equals(c))return "فعال‌سازی"; if("disarm".equals(c))return "غیرفعال‌سازی"; if("home".equals(c))return "نیمه‌فعال"; if("panic".equals(c))return "آژیر/هشدار"; if("sirenOff".equals(c))return "قطع آژیر"; return "استعلام وضعیت"; }

    private String httpRequestWithBasic(String target,String method,String body,String bearer,String user,String pass,int timeoutMs)throws Exception{ HttpURLConnection con=(HttpURLConnection)new URL(target).openConnection(); con.setConnectTimeout(timeoutMs); con.setReadTimeout(timeoutMs); con.setRequestMethod(method==null?"GET":method); con.setRequestProperty("Accept","application/json,text/plain,*/*"); if(bearer!=null&&!bearer.trim().isEmpty())con.setRequestProperty("Authorization","Bearer "+bearer.trim()); else if(user!=null&&!user.trim().isEmpty()){ String raw=user+":"+(pass==null?"":pass); con.setRequestProperty("Authorization","Basic "+Base64.encodeToString(raw.getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP)); } if(body!=null){con.setDoOutput(true); try(OutputStream os=con.getOutputStream()){os.write(body.getBytes(StandardCharsets.UTF_8));}} int code=con.getResponseCode(); String resp=readStreamText(code>=200&&code<300?con.getInputStream():con.getErrorStream()); if(code<200||code>=300)throw new DbException("HTTP "+code+" • "+limitText(resp,160)); return resp; }


    private void loadCustomers(String query) {
        loadCustomers(query, "all", false);
    }

    private void loadCustomers(String query, String filter) {
        loadCustomers(query, filter, false);
    }

    private void loadCustomers(String query, String filter, boolean force) {
        String q = query == null ? "" : query;
        String f = filter == null || filter.trim().isEmpty() ? "all" : filter;
        String previousFilter = customersCacheFilter == null ? "all" : customersCacheFilter;
        if (customersSortOrder == null || customersSortOrder.trim().isEmpty() || !f.equals(previousFilter)) customersSortOrder = defaultCustomerSort(f);
        if (!force && customersCacheJson != null && !customersCacheJson.trim().isEmpty() && q.equals(customersCacheQuery)) {
            try {
                JSONArray cached = new JSONArray(customersCacheJson);
                if (customersCacheAllRows || f.equals(customersCacheFilter)) { customersCacheFilter = f; renderCustomersFromJson(cached, q, f); return; }
            } catch (Exception ignored) { }
        }
        content.removeAllViews();
        addHero("مشتریان", "اطلاعات مشتریان ثابت می‌ماند؛ برای داده جدید از تازه‌سازی دستی استفاده کنید.");
        addManualRefreshPanel("customers", "بروزرسانی دستی مشتریان", "بازگشت از گردش حساب دیگر لیست را دوباره فراخوانی نمی‌کند", () -> loadCustomers(q, f, true));
        addSearchBox("جستجوی مشتری…", q, qq -> loadCustomers(qq, f, true));
        addCustomerFilterChips(q, f, new JSONArray());
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت مشتریان…");
        runDb(() -> queryCustomers(q, "all"), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    customersCacheJson = body;
                    customersCacheQuery = q;
                    customersCacheFilter = f;
                    customersCacheAllRows = true;
                    markRefresh("customers");
                    renderCustomersFromJson(new JSONArray(body), q, f);
                } catch (Exception e) { showPageError("مشتریان", e, () -> loadCustomers(q, f, true)); }
            }
            @Override public void fail(Exception e) { showPageError("مشتریان", e, () -> loadCustomers(q, f, true)); }
        });
    }

    private void renderCustomersFromJson(JSONArray rows, String query, String filter) {
        content.removeAllViews();
        addHero("مشتریان", "فیلتر هوشمند بدهکاران، بستانکاران، بدون خرید و پرخریدها");
        addManualRefreshPanel("customers", "بروزرسانی دستی مشتریان", "آخرین لیست ثابت نگه داشته شده است", () -> loadCustomers(query, filter, true));
        addSearchBox("جستجوی مشتری…", query, q -> loadCustomers(q, filter, true));
        JSONArray allRows = rows == null ? new JSONArray() : rows;
        addCustomerFilterChips(query, filter, allRows);
        if (allRows.length() == 0) { LinearLayout empty = new LinearLayout(this); empty.setOrientation(LinearLayout.VERTICAL); content.addView(empty, new LinearLayout.LayoutParams(-1, -2)); addEmptyTo(empty, "مشتری مطابق جستجو پیدا نشد."); return; }
        JSONArray visibleRows = customerFilteredRows(allRows, filter);
        if (visibleRows.length() == 0) { LinearLayout empty = new LinearLayout(this); empty.setOrientation(LinearLayout.VERTICAL); content.addView(empty, new LinearLayout.LayoutParams(-1, -2)); addEmptyTo(empty, "در این دسته مشتری قابل نمایش وجود ندارد."); return; }
        addCustomerSortPanel(query, filter, visibleRows);
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        JSONArray sorted = sortedCustomers(visibleRows, filter, customersSortOrder);
        for (int i = 0; i < sorted.length(); i++) addCustomerCard(list, sorted.optJSONObject(i));
    }

    private void addCustomerSortPanel(String query, String filter, JSONArray rows) {
        LinearLayout panel = card();
        panel.setPadding(dp(12), dp(12), dp(12), dp(11));
        panel.setBackground(gradient(new int[]{alpha(INFO, 22), alpha(GOLD, 13), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        LinearLayout titleRow = new LinearLayout(this);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setGravity(Gravity.CENTER_VERTICAL);
        TextView icon = text("⇅", 17, Color.WHITE, Typeface.BOLD);
        icon.setGravity(Gravity.CENTER);
        icon.setShadowLayer(dp(3), 0, dp(1), alpha(Color.BLACK, 120));
        icon.setBackground(gradient(new int[]{mix(INFO, Color.WHITE, 0.15f), INFO, alpha(GOLD_2, 140)}, GradientDrawable.Orientation.TL_BR, 17));
        titleRow.addView(icon, new LinearLayout.LayoutParams(dp(40), dp(40)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("مرتب کردن این لیست", 13.8f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("هوشمند و بدون شلوغی؛ فقط ترتیب کارت‌های همین لیست عوض می‌شود.", 10.1f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        titleRow.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView active = text(customerSortLabel(customersSortOrder), 9.8f, INFO, Typeface.BOLD);
        active.setGravity(Gravity.CENTER);
        active.setPadding(dp(9), dp(5), dp(9), dp(5));
        active.setSingleLine(true);
        active.setBackground(roundedStroke(alpha(INFO, 24), 999, alpha(INFO, 85)));
        titleRow.addView(active, new LinearLayout.LayoutParams(-2, -2));
        panel.addView(titleRow, new LinearLayout.LayoutParams(-1, -2));

        HorizontalScrollView scroll = new HorizontalScrollView(this);
        styleHorizontalScroll(scroll);
        LinearLayout chips = new LinearLayout(this);
        chips.setOrientation(LinearLayout.HORIZONTAL);
        String[][] options = customerSortOptions(filter);
        for (String[] opt : options) {
            final String key = opt[0];
            Button b = key.equals(customersSortOrder) ? primaryButton(opt[1]) : secondaryButton(opt[1]);
            b.setTextSize(9.6f);
            b.setMinWidth(0);
            b.setPadding(dp(9), 0, dp(9), 0);
            b.setOnClickListener(v -> { customersSortOrder = key; renderCustomersFromJson(rows, query, filter); });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, dp(38));
            lp.setMargins(dp(3), 0, dp(3), 0);
            chips.addView(b, lp);
        }
        scroll.addView(chips, new FrameLayout.LayoutParams(-2, -2));
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.setMargins(0, dp(11), 0, 0);
        panel.addView(scroll, sp);

        TextView hint = text(customerSortHint(filter, rows), 9.7f, alpha(TEXT, 185), Typeface.NORMAL);
        hint.setGravity(Gravity.RIGHT);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.setMargins(0, dp(8), 0, 0);
        panel.addView(hint, hp);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, -2);
        pp.setMargins(0, dp(8), 0, dp(10));
        content.addView(panel, pp);
    }

    private String defaultCustomerSort(String filter) { return "smart"; }

    private String[][] customerSortOptions(String filter) {
        if ("debt".equals(filter)) return new String[][]{{"smart","اولویت وصول"},{"debt_desc","بیشترین بدهی"},{"debt_asc","کمترین بدهی"},{"oldest_sale","قدیمی‌ترین خرید"},{"latest_sale","جدیدترین خرید"},{"risk_desc","ریسک بیشتر"},{"check_desc","چک بیشتر"}};
        if ("credit".equals(filter)) return new String[][]{{"smart","اولویت تسویه"},{"credit_desc","بستانکاری بیشتر"},{"credit_asc","بستانکاری کمتر"},{"latest_sale","جدیدترین خرید"},{"sales_desc","بیشترین فروش"},{"name_asc","الفبایی"}};
        if ("no_buy".equals(filter)) return new String[][]{{"smart","فرصت فروش"},{"debt_desc","مانده بالاتر"},{"risk_desc","ریسک بیشتر"},{"name_asc","الفبایی"},{"latest_sale","آخرین تعامل"}};
        if ("settled".equals(filter)) return new String[][]{{"smart","آماده فروش"},{"latest_sale","جدیدترین خرید"},{"sales_desc","بیشترین فروش"},{"invoice_desc","فاکتور بیشتر"},{"name_asc","الفبایی"}};
        if ("top".equals(filter)) return new String[][]{{"smart","ارزشمندترین"},{"sales_desc","بیشترین فروش"},{"invoice_desc","فاکتور بیشتر"},{"latest_sale","جدیدترین خرید"},{"debt_desc","مانده بدهی"},{"risk_desc","ریسک بیشتر"}};
        return new String[][]{{"smart","پیشنهاد میلو"},{"name_asc","الفبایی"},{"debt_desc","بیشترین بدهی"},{"latest_sale","جدیدترین خرید"},{"sales_desc","بیشترین فروش"},{"risk_desc","ریسک بیشتر"}};
    }

    private String customerSortLabel(String sort) {
        if ("debt_desc".equals(sort)) return "بیشترین بدهی";
        if ("debt_asc".equals(sort)) return "کمترین بدهی";
        if ("credit_desc".equals(sort)) return "بستانکاری بیشتر";
        if ("credit_asc".equals(sort)) return "بستانکاری کمتر";
        if ("oldest_sale".equals(sort)) return "قدیمی‌ترین خرید";
        if ("latest_sale".equals(sort)) return "جدیدترین خرید";
        if ("sales_desc".equals(sort)) return "بیشترین فروش";
        if ("sales_asc".equals(sort)) return "کمترین فروش";
        if ("invoice_desc".equals(sort)) return "فاکتور بیشتر";
        if ("check_desc".equals(sort)) return "چک بیشتر";
        if ("risk_desc".equals(sort)) return "ریسک بیشتر";
        if ("name_asc".equals(sort)) return "الفبایی";
        return "هوشمند";
    }

    private String customerSortHint(String filter, JSONArray rows) {
        int count = rows == null ? 0 : rows.length();
        if ("debt".equals(filter)) return "نمایش " + formatNumber(count) + " بدهکار؛ اولویت وصول ترکیبی از مانده، ریسک، چک و قدیمی‌بودن خرید است.";
        if ("credit".equals(filter)) return "نمایش " + formatNumber(count) + " بستانکار؛ برای تسویه یا تهاتر سریع مرتب کنید.";
        if ("no_buy".equals(filter)) return "نمایش " + formatNumber(count) + " مشتری بدون خرید؛ مناسب کمپین تماس و بازفعال‌سازی.";
        if ("top".equals(filter)) return "نمایش " + formatNumber(count) + " مشتری پرخرید؛ مناسب نگهداشت و پیشنهاد اختصاصی.";
        if ("settled".equals(filter)) return "نمایش " + formatNumber(count) + " مشتری بدون مانده؛ مناسب فروش مجدد بدون ریسک وصول.";
        return "نمایش " + formatNumber(count) + " مشتری؛ مرتب‌سازی بدون بارگذاری دوباره انجام می‌شود.";
    }

    private JSONArray sortedCustomers(JSONArray rows, String filter, String sort) {
        JSONArray out = new JSONArray();
        if (rows == null) return out;
        final String f = filter == null ? "all" : filter;
        final String s = (sort == null || sort.trim().isEmpty()) ? defaultCustomerSort(f) : sort;
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) { JSONObject o = rows.optJSONObject(i); if (o != null) list.add(o); }
        Collections.sort(list, new Comparator<JSONObject>() {
            @Override public int compare(JSONObject a, JSONObject b) { return compareCustomersForSort(a, b, f, s); }
        });
        for (JSONObject o : list) out.put(o);
        return out;
    }

    private int compareCustomersForSort(JSONObject a, JSONObject b, String filter, String sort) {
        if ("debt_desc".equals(sort)) return chain(compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareName(a,b));
        if ("debt_asc".equals(sort)) return chain(compareDouble(customerPositiveBalance(a), customerPositiveBalance(b)), compareName(a,b));
        if ("credit_desc".equals(sort)) return chain(compareDouble(customerCreditAmount(b), customerCreditAmount(a)), compareName(a,b));
        if ("credit_asc".equals(sort)) return chain(compareDouble(customerCreditAmount(a), customerCreditAmount(b)), compareName(a,b));
        if ("oldest_sale".equals(sort)) return chain(compareLong(customerLastSaleValue(a), customerLastSaleValue(b)), compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareName(a,b));
        if ("latest_sale".equals(sort)) return chain(compareLong(customerLastSaleValue(b), customerLastSaleValue(a)), compareDouble(num(b,"جمع_فروش"), num(a,"جمع_فروش")), compareName(a,b));
        if ("sales_desc".equals(sort)) return chain(compareDouble(num(b,"جمع_فروش"), num(a,"جمع_فروش")), compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareName(a,b));
        if ("sales_asc".equals(sort)) return chain(compareDouble(num(a,"جمع_فروش"), num(b,"جمع_فروش")), compareName(a,b));
        if ("invoice_desc".equals(sort)) return chain(compareInt(b.optInt("تعداد_فاکتور",0), a.optInt("تعداد_فاکتور",0)), compareDouble(num(b,"جمع_فروش"), num(a,"جمع_فروش")), compareName(a,b));
        if ("check_desc".equals(sort)) return chain(compareDouble(num(b,"جمع_چک"), num(a,"جمع_چک")), compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareName(a,b));
        if ("risk_desc".equals(sort)) return chain(compareInt(customerRiskScore(b), customerRiskScore(a)), compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareName(a,b));
        if ("name_asc".equals(sort)) return compareName(a,b);
        return compareCustomersSmart(a, b, filter);
    }

    private int compareCustomersSmart(JSONObject a, JSONObject b, String filter) {
        if ("top".equals(filter)) return chain(compareDouble(num(b,"جمع_فروش"), num(a,"جمع_فروش")), compareInt(b.optInt("تعداد_فاکتور",0), a.optInt("تعداد_فاکتور",0)), compareLong(customerLastSaleValue(b), customerLastSaleValue(a)), compareName(a,b));
        if ("credit".equals(filter)) return chain(compareDouble(customerCreditAmount(b), customerCreditAmount(a)), compareDouble(num(b,"جمع_فروش"), num(a,"جمع_فروش")), compareLong(customerLastSaleValue(b), customerLastSaleValue(a)), compareName(a,b));
        if ("no_buy".equals(filter)) return chain(compareInt(customerRiskScore(b), customerRiskScore(a)), compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareName(a,b));
        if ("settled".equals(filter)) return chain(compareDouble(num(b,"جمع_فروش"), num(a,"جمع_فروش")), compareLong(customerLastSaleValue(b), customerLastSaleValue(a)), compareName(a,b));
        if ("debt".equals(filter)) return chain(compareInt(customerRiskScore(b), customerRiskScore(a)), compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareDouble(num(b,"جمع_چک"), num(a,"جمع_چک")), compareLong(customerLastSaleValue(a), customerLastSaleValue(b)), compareName(a,b));
        return chain(compareInt(customerRiskScore(b), customerRiskScore(a)), compareDouble(customerPositiveBalance(b), customerPositiveBalance(a)), compareDouble(num(b,"جمع_فروش"), num(a,"جمع_فروش")), compareName(a,b));
    }

    private int chain(int... values) { for (int v : values) if (v != 0) return v; return 0; }
    private int compareDouble(double left, double right) { return left < right ? -1 : (left > right ? 1 : 0); }
    private int compareLong(long left, long right) { return left < right ? -1 : (left > right ? 1 : 0); }
    private int compareInt(int left, int right) { return left < right ? -1 : (left > right ? 1 : 0); }
    private int compareName(JSONObject a, JSONObject b) { return txt(a,"نام").compareToIgnoreCase(txt(b,"نام")); }
    private double customerPositiveBalance(JSONObject r) { return Math.max(0, num(r, "مانده")); }
    private double customerCreditAmount(JSONObject r) { return Math.max(0, -num(r, "مانده")); }
    private double num(JSONObject r, String key) { return r == null ? 0 : r.optDouble(key, 0); }
    private String txt(JSONObject r, String key) { return r == null ? "" : stringOr(r.optString(key, ""), ""); }

    private long customerLastSaleValue(JSONObject r) {
        String raw = r == null ? "" : r.optString("آخرین_خرید", "");
        String norm = normalizeDigits(raw);
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < norm.length(); i++) { char ch = norm.charAt(i); if (ch >= '0' && ch <= '9') digits.append(ch); }
        if (digits.length() == 0) return 0;
        String d = digits.length() >= 8 ? digits.substring(0, 8) : digits.toString();
        try { return Long.parseLong(d); } catch (Exception ignored) { return 0; }
    }

    private String normalizeDigits(String value) {
        if (value == null) return "";
        return value.replace('۰','0').replace('۱','1').replace('۲','2').replace('۳','3').replace('۴','4').replace('۵','5').replace('۶','6').replace('۷','7').replace('۸','8').replace('۹','9')
                .replace('٠','0').replace('١','1').replace('٢','2').replace('٣','3').replace('٤','4').replace('٥','5').replace('٦','6').replace('٧','7').replace('٨','8').replace('٩','9');
    }

    private JSONArray customerFilteredRows(JSONArray rows, String filter) {
        JSONArray out = new JSONArray();
        if (rows == null) return out;
        for (int i = 0; i < rows.length(); i++) {
            JSONObject r = rows.optJSONObject(i);
            if (r != null && customerMatchesFilter(r, filter)) out.put(r);
        }
        return out;
    }

    private int customerFilterCount(JSONArray rows, String filter) {
        int count = 0;
        if (rows != null) for (int i = 0; i < rows.length(); i++) if (customerMatchesFilter(rows.optJSONObject(i), filter)) count++;
        return count;
    }

    private boolean customerMatchesFilter(JSONObject r, String filter) {
        if (r == null) return false;
        String f = filter == null || filter.trim().isEmpty() ? "all" : filter;
        double balance = r.optDouble("مانده", 0);
        if ("debt".equals(f)) return balance > 0.0001;
        if ("credit".equals(f)) return balance < -0.0001;
        if ("settled".equals(f)) return Math.abs(balance) <= 0.0001;
        if ("no_buy".equals(f)) return r.optInt("تعداد_فاکتور", 0) <= 0;
        if ("top".equals(f)) return r.optDouble("جمع_فروش", 0) > 0 || r.optInt("تعداد_فاکتور", 0) > 0;
        return true;
    }

    private void addCustomerFilterChips(String query, String activeFilter, JSONArray allRows) {
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
        copy.addView(text("مشتری‌ها بر اساس مانده و وضعیت خرید، همین‌جا و بدون دریافت دوباره از SQL دسته‌بندی می‌شوند.", 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        titleRow.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        panel.addView(titleRow, new LinearLayout.LayoutParams(-1, -2));

        HorizontalScrollView filterScroll = new HorizontalScrollView(this);
        styleHorizontalScroll(filterScroll);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setGravity(Gravity.CENTER_VERTICAL);
        final JSONArray rowsForFilter = allRows == null ? new JSONArray() : allRows;
        String[][] filters = {{"all","همه"},{"debt","بدهکار"},{"credit","بستانکار"},{"settled","بدون حساب"},{"no_buy","بدون خرید"},{"top","پرخرید"}};
        for (String[] f : filters) {
            final String nextFilter = f[0];
            String label = f[1] + " " + formatNumber(customerFilterCount(rowsForFilter, nextFilter));
            Button b = activeFilter.equals(nextFilter) ? primaryButton(label) : secondaryButton(label);
            b.setTextSize(9.2f);
            b.setOnClickListener(v -> { if (!nextFilter.equals(activeFilter)) customersSortOrder = defaultCustomerSort(nextFilter); customersCacheFilter = nextFilter; renderCustomersFromJson(rowsForFilter, query, nextFilter); });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(106), dp(42));
            lp.setMargins(dp(2), 0, dp(2), 0);
            box.addView(b, lp);
        }
        filterScroll.addView(box, new FrameLayout.LayoutParams(-2, -2));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2);
        bp.setMargins(0, dp(14), 0, 0);
        panel.addView(filterScroll, bp);
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
            String address = resolveFlexible(cols, "address", "Address", "ADDRESS", "adr", "addr", "ADDR", "Adress", "adress", "address1", "Address1", "customer_address", "CustomerAddress", "neshani", "Neshani", "NESHANI", "نشانی", "نشاني", "آدرس", "ادرس", "manzel", "Manzel", "MOADD", "MOADR", "moneshan", "MONESHAN");
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
            select.add(customerAddressExpr(cols, "c") + " AS نشانی");
            select.add(balance == null ? "CAST(0 AS decimal(19,2)) AS مانده" : "TRY_CONVERT(decimal(19,2),c.[" + balance + "]) AS مانده");
            select.add(credit == null ? "CAST(NULL AS decimal(19,2)) AS اعتبار" : "TRY_CONVERT(decimal(19,2),c.[" + credit + "]) AS اعتبار");
            select.add(economic == null ? "CAST(NULL AS nvarchar(100)) AS اقتصادی" : "TRY_CONVERT(nvarchar(100),c.[" + economic + "]) AS اقتصادی");
            select.add(national == null ? "CAST(NULL AS nvarchar(100)) AS ملی" : "TRY_CONVERT(nvarchar(100),c.[" + national + "]) AS ملی");
            select.add(vis == null ? "CAST(NULL AS int) AS کد_ویزیتور" : "TRY_CONVERT(int,c.[" + vis + "]) AS کد_ویزیتور");
            select.add("ISNULL(sf.sales_total,0) AS جمع_فروش");
            select.add("ISNULL(sf.sales_count,0) AS تعداد_فاکتور");
            select.add("ISNULL(sf.last_sale,N'') AS آخرین_خرید");
            select.add("ISNULL(ch.check_total,0) AS جمع_چک");

            List<String> where = new ArrayList<>();
            List<Object> params = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                List<String> parts = new ArrayList<>();
                List<String> searchCols = new ArrayList<>();
                for (String col : new String[]{name, phone, phone2, phone3, address, shmo, national, economic}) if (col != null && !searchCols.contains(col)) searchCols.add(col);
                for (String col : customerAddressColumns(cols)) if (col != null && !searchCols.contains(col)) searchCols.add(col);
                for (String col : searchCols) {
                    parts.add("TRY_CONVERT(nvarchar(500),c.[" + col + "]) LIKE N'%' + ? + N'%'"); params.add(search.trim());
                }
                where.add("(" + join(parts, " OR ") + ")");
            }
            String scope = customerScopeCondition(cols, "c", params);
            if (!scope.isEmpty()) where.add(scope);
            boolean canSales = hasCol(saleCols, "shmo") && hasCol(saleCols, "all");
            boolean canChecks = hasCol(checkCols, "shmo") && hasCol(checkCols, "getchkmab");
            String saleDate = resolve(saleCols, "date", "t_date", "Date");
            String lastSaleExpr = saleDate == null ? "CAST(N'' AS nvarchar(30))" : "ISNULL(MAX(TRY_CONVERT(nvarchar(30),s.[" + saleDate + "])),N'')";
            String saleApply = canSales ? "OUTER APPLY (SELECT COUNT_BIG(1) sales_count, ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[" + resolve(saleCols, "all") + "])),0) sales_total, " + lastSaleExpr + " last_sale FROM dbo.sailfact s WHERE s.[" + resolve(saleCols, "shmo") + "]=c.[" + shmo + "]) sf " : "OUTER APPLY (SELECT CAST(0 AS bigint) sales_count, CAST(0 AS decimal(19,2)) sales_total, CAST(N'' AS nvarchar(30)) last_sale) sf ";
            String checkApply = canChecks ? "OUTER APPLY (SELECT ISNULL(SUM(TRY_CONVERT(decimal(19,2),g.[" + resolve(checkCols, "getchkmab") + "])),0) check_total FROM dbo.getchk g WHERE g.[" + resolve(checkCols, "shmo") + "]=c.[" + shmo + "]) ch " : "OUTER APPLY (SELECT CAST(0 AS decimal(19,2)) check_total) ch ";
            if ("debt".equals(filter) && balance != null) where.add("TRY_CONVERT(decimal(19,2),c.[" + balance + "])>0");
            if ("credit".equals(filter) && balance != null) where.add("TRY_CONVERT(decimal(19,2),c.[" + balance + "])<0");
            if ("settled".equals(filter) && balance != null) where.add("ABS(ISNULL(TRY_CONVERT(decimal(19,2),c.[" + balance + "]),0))<=0.0001");
            if ("no_buy".equals(filter) && canSales) where.add("ISNULL(sf.sales_count,0)=0");
            String order = "top".equals(filter) ? " ORDER BY جمع_فروش DESC, نام" : ("debt".equals(filter) ? " ORDER BY مانده DESC, نام" : " ORDER BY نام, کد");
            String sql = "SELECT TOP (350) " + join(select, ",") + " FROM dbo.[CUSTOMERS] c " + saleApply + checkApply +
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
        double salesTotal = r.optDouble("جمع_فروش", 0);
        double creditLimit = r.optDouble("اعتبار", 0);
        int invoices = r.optInt("تعداد_فاکتور", 0);
        int accent = balance > 0 ? DANGER : (balance < 0 ? SUCCESS : INFO);
        String statusText = balance > 0 ? "بدهکار" : (balance < 0 ? "بستانکار" : "تسویه");
        int riskScore = customerRiskScore(r);

        LinearLayout c = card();
        c.setOrientation(LinearLayout.HORIZONTAL);
        c.setPadding(0, 0, 0, 0);
        c.setClickable(true);
        c.setBackground(gradient(new int[]{alpha(Color.WHITE, isLightTheme() ? 70 : 18), alpha(accent, isLightTheme() ? 28 : 44), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 26));
        c.setOnClickListener(v -> { if (ensurePermission("customer_detail", "جزئیات مشتری")) showCustomerDetail(r, "all"); });
        applyTouchFeedback(c);

        View rail = new View(this);
        rail.setBackground(gradient(new int[]{mix(accent, Color.WHITE, 0.35f), accent, mix(accent, Color.BLACK, isLightTheme() ? 0.10f : 0.28f)}, GradientDrawable.Orientation.TOP_BOTTOM, 999));
        LinearLayout.LayoutParams railLp = new LinearLayout.LayoutParams(dp(6), -1);
        railLp.setMargins(0, dp(10), dp(8), dp(10));
        c.addView(rail, railLp);

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        int pad = compactUi() ? dp(10) : dp(13);
        body.setPadding(pad, pad, pad, pad);
        c.addView(body, new LinearLayout.LayoutParams(0, -2, 1f));

        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        TextView avatar = text(initials(r.optString("نام", "م")), 17, onColorFor(accent), Typeface.BOLD);
        avatar.setGravity(Gravity.CENTER);
        avatar.setShadowLayer(dp(3), 0, dp(1), alpha(Color.BLACK, 130));
        avatar.setBackground(gradient(new int[]{mix(accent, Color.WHITE, 0.24f), accent, mix(GOLD_2, accent, 0.34f)}, GradientDrawable.Orientation.TL_BR, 20));
        head.addView(avatar, new LinearLayout.LayoutParams(dp(56), dp(56)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(10), 0);
        String name = r.optString("نام", "بدون نام");
        TextView nameView = text(name, 15.8f, TEXT, Typeface.BOLD);
        nameView.setSingleLine(true); nameView.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(nameView, new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("کد " + r.optString("کد", "-") + " • آخرین خرید: " + stringOr(r.optString("آخرین_خرید", ""), "—"), 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView chip = pill(statusText, accent, false);
        head.addView(chip, new LinearLayout.LayoutParams(-2, -2));
        body.addView(head, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout priority = new LinearLayout(this);
        priority.setOrientation(LinearLayout.HORIZONTAL);
        priority.setGravity(Gravity.CENTER_VERTICAL);
        addCustomerTagChip(priority, collectionPriorityLabel(r), accent);
        addCustomerTagChip(priority, customerSmartTag(balance, salesTotal, invoices, creditLimit), accent);
        addCustomerTagChip(priority, customerRiskLabel(riskScore), customerRiskAccent(riskScore));
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, -2); pp.setMargins(0, dp(10), 0, 0); body.addView(priority, pp);

        LinearLayout tags = new LinearLayout(this);
        tags.setOrientation(LinearLayout.HORIZONTAL);
        addCustomerTagChip(tags, invoices == 0 ? "کمپین بازفعال‌سازی" : "آخرین فاکتور: " + stringOr(r.optString("آخرین_خرید", ""), "—"), invoices == 0 ? WARNING : INFO);
        addCustomerTagChip(tags, importanceLabel(salesTotal, invoices, balance), salesTotal > 0 ? GOLD : MUTED);
        addCustomerTagChip(tags, firstPhone(r).equals("—") ? "بدون تماس" : "تماس آماده", firstPhone(r).equals("—") ? WARNING : SUCCESS);
        LinearLayout.LayoutParams tagp = new LinearLayout.LayoutParams(-1, -2); tagp.setMargins(0, dp(7), 0, 0); body.addView(tags, tagp);

        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.addView(customerMiniMetric("مانده", money(r.opt("مانده")), accent), weightedMiniLp());
        row1.addView(customerMiniMetric("فروش", money(r.opt("جمع_فروش")), GOLD), weightedMiniLp());
        row1.addView(customerMiniMetric("فاکتور", formatNumber(r.opt("تعداد_فاکتور")), INFO), weightedMiniLp());
        LinearLayout.LayoutParams r1p = new LinearLayout.LayoutParams(-1, -2); r1p.setMargins(0, dp(10), 0, 0); body.addView(row1, r1p);

        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.addView(customerMiniMetric("چک", money(r.opt("جمع_چک")), WARNING), weightedMiniLp());
        row2.addView(customerMiniMetric("اعتبار", money(r.opt("اعتبار")), SUCCESS), weightedMiniLp());
        row2.addView(customerMiniMetric("تماس", firstPhone(r), INFO), weightedMiniLp());
        LinearLayout.LayoutParams r2p = new LinearLayout.LayoutParams(-1, -2); r2p.setMargins(0, dp(7), 0, 0); body.addView(row2, r2p);

        String addrValue = cleanCustomerAddress(r.optString("نشانی", ""));
        TextView address = text("نشانی: " + stringOr(addrValue, "ثبت نشده"), 10.5f, alpha(TEXT, 190), Typeface.NORMAL);
        address.setMaxLines(2); address.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2); ap.setMargins(0, dp(8), 0, 0); body.addView(address, ap);
        if (!addrValue.isEmpty()) addCustomerAddressActions(body, addrValue);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        Button ledger = secondaryButton(withIcon("☷", "گردش حساب"));
        ledger.setTextSize(10.2f);
        ledger.setOnClickListener(v -> { if (ensurePermission("customer_detail", "گردش مشتری")) showCustomerDetail(r, "all"); });
        Button call = primaryButton(withIcon("☎", "تماس سریع"));
        call.setTextSize(10.2f);
        call.setOnClickListener(v -> { if (ensurePermission("customer_call", "تماس مشتری")) openPhoneDialer(firstPhone(r)); });
        Button msg = secondaryButton(withIcon("✉", "پیام میلو"));
        msg.setTextSize(10.0f);
        msg.setOnClickListener(v -> { if (ensurePermission("customer_message", "پیام مشتری")) showCustomerMessageDialog(r); });
        if (canUsePermission("customer_detail")) actions.addView(ledger, weightedButtonLp());
        if (canUsePermission("customer_message")) actions.addView(msg, weightedButtonLp());
        if (canUsePermission("customer_call")) actions.addView(call, weightedButtonLp());
        if (actions.getChildCount() > 0) { LinearLayout.LayoutParams alp = new LinearLayout.LayoutParams(-1, -2); alp.setMargins(0, dp(8), 0, 0); body.addView(actions, alp); }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        parent.addView(c, lp);
    }

    private String collectionPriorityLabel(JSONObject r) {
        double balance = customerPositiveBalance(r);
        int risk = customerRiskScore(r);
        long last = customerLastSaleValue(r);
        if (balance <= 0) return r != null && r.optDouble("مانده", 0) < 0 ? "تهاتر/تسویه" : "بدون مانده";
        if (risk >= 72) return "فوری امروز";
        if (last > 0 && last < 14000101L) return "قدیمی و مهم";
        if (risk >= 45) return "پیگیری نزدیک";
        return "وصول منظم";
    }

    private void addCustomerTagChip(LinearLayout parent, String label, int accent) {
        TextView chip = text(label, 9.4f, accent, Typeface.BOLD);
        chip.setGravity(Gravity.CENTER);
        chip.setSingleLine(true);
        chip.setPadding(dp(6), 0, dp(6), 0);
        chip.setBackground(roundedStroke(alpha(accent, 18), 999, alpha(accent, 70)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(32), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        parent.addView(chip, lp);
    }

    private String customerSmartTag(double balance, double salesTotal, int invoices, double creditLimit) {
        if (invoices <= 0) return "مشتری خاموش";
        if (balance > 0 && (creditLimit <= 0 || balance > creditLimit)) return "پرریسک اعتباری";
        if (salesTotal > 0 && invoices >= 5 && balance <= 0) return "مشتری طلایی";
        if (salesTotal > 0 && invoices >= 2) return "مشتری فعال";
        return balance > 0 ? "نیازمند پیگیری" : "عادی";
    }

    private int customerRiskScore(JSONObject r) {
        if (r == null) return 0;
        double balance = Math.max(0, r.optDouble("مانده", 0));
        double credit = Math.max(0, r.optDouble("اعتبار", 0));
        double checks = Math.max(0, r.optDouble("جمع_چک", 0));
        double sales = Math.max(0, r.optDouble("جمع_فروش", 0));
        int invoices = r.optInt("تعداد_فاکتور", 0);
        int score = 8;
        if (balance > 0) score += Math.min(42, (int)(balance / Math.max(1, sales / Math.max(1, invoices)) * 12));
        if (credit > 0 && balance > credit) score += 24;
        if (checks > 0 && checks > Math.max(1, balance) * 0.55) score += 13;
        if (invoices == 0) score += 18;
        if (sales > 0 && invoices >= 5 && balance <= 0) score -= 18;
        return Math.max(0, Math.min(100, score));
    }

    private String customerRiskLabel(int score) {
        if (score >= 72) return "ریسک بالا؛ فروش نقدی";
        if (score >= 45) return "ریسک متوسط";
        if (score >= 22) return "ریسک کم";
        return "امن و وفادار";
    }

    private int customerRiskAccent(int score) {
        if (score >= 72) return DANGER;
        if (score >= 45) return WARNING;
        if (score >= 22) return INFO;
        return SUCCESS;
    }

    private void showCustomerMessageDialog(JSONObject r) {
        String[] labels = {"پیام وصول محترمانه", "پیام فروش مجدد", "یادآوری چک/تعهد"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("میلو چه پیامی بدهد؟")
                .setItems(labels, (d, which) -> {
                    String body = buildCustomerMessage(r, which);
                    AlertDialog inner = new AlertDialog.Builder(this)
                            .setTitle(labels[which])
                            .setMessage(body)
                            .setNegativeButton("بستن", null)
                            .setPositiveButton("ارسال/کپی", (dd, w) -> sharePlainText("پیام مشتری Meelano", body, null))
                            .create();
                    styleMeelanoDialog(inner, SUCCESS);
                    inner.show();
                })
                .create();
        styleMeelanoDialog(dialog, SUCCESS);
        dialog.show();
    }

    private String buildCustomerMessage(JSONObject r, int type) {
        String name = r == null ? "" : r.optString("نام", "").trim();
        if (name.isEmpty()) name = "همکار گرامی";
        String amount = r == null ? "" : money(r.opt("مانده"));
        if (type == 1) return "سلام " + name + " عزیز، وقت شما بخیر. برای شما یک پیشنهاد/موجودی جدید آماده کرده‌ایم که می‌تواند برای خرید بعدی مناسب باشد. اگر مایل باشید جزئیات را ارسال کنم. با احترام، Meelano";
        if (type == 2) return "سلام " + name + " عزیز، وقت بخیر. جهت یادآوری تعهد/چک ثبت‌شده، لطفاً وضعیت پرداخت را اطلاع دهید تا برنامه‌ریزی مالی دقیق انجام شود. سپاس از همکاری شما.";
        return "سلام " + name + " عزیز، وقت بخیر. بابت مانده حساب " + amount + " لطفاً زمان تسویه یا پرداخت مرحله‌ای را اعلام بفرمایید. هدف فقط هماهنگی دقیق‌تر است؛ میلو هم قول می‌دهد خیلی غر نزند!";
    }

    private void appendFollowupHistory(String key, String status, String note) {
        if (prefs == null || key == null) return;
        String old = prefs.getString(key, "");
        String line = nowText() + " • " + status + (note == null || note.trim().isEmpty() ? "" : " • " + note.trim());
        prefs.edit().putString(key, line + (old == null || old.isEmpty() ? "" : "\n" + old)).apply();
    }

    private String importanceLabel(double salesTotal, int invoices, double balance) {
        if (salesTotal > 0 && invoices >= 5) return "اهمیت بالا";
        if (balance > 0) return "اولویت وصول";
        if (invoices == 0) return "فرصت فروش";
        return "اهمیت متوسط";
    }

    private String cleanCustomerAddress(String address) {
        if (address == null) return "";
        String a = address.replace("null", "").replace("NULL", "").replace("—", "").trim();
        if (a.equals("-") || a.equals(".")) return "";
        while (a.contains("  ")) a = a.replace("  ", " ");
        return a;
    }

    private void addCustomerAddressActions(LinearLayout parent, String address) {
        String addr = cleanCustomerAddress(address);
        if (parent == null || addr.isEmpty()) return;
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        Button copy = secondaryButton(withIcon("□", "کپی نشانی"));
        Button map = secondaryButton(withIcon("⌖", "نمایش روی نقشه"));
        copy.setTextSize(9.6f); map.setTextSize(9.6f);
        copy.setOnClickListener(v -> copyToClipboard("نشانی مشتری", addr));
        map.setOnClickListener(v -> openAddressInMap(addr));
        row.addView(copy, weightedButtonLp());
        row.addView(map, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
        rp.setMargins(0, dp(7), 0, 0);
        parent.addView(row, rp);
    }

    private void copyToClipboard(String label, String value) {
        try {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) cm.setPrimaryClip(ClipData.newPlainText(label == null ? "Meelano" : label, value == null ? "" : value));
            Toast.makeText(this, "کپی شد.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) { Toast.makeText(this, "کپی ممکن نشد.", Toast.LENGTH_SHORT).show(); }
    }

    private void openAddressInMap(String address) {
        try {
            String addr = cleanCustomerAddress(address);
            if (addr.isEmpty()) { Toast.makeText(this, "نشانی معتبر ثبت نشده است.", Toast.LENGTH_SHORT).show(); return; }
            Uri uri = Uri.parse("geo:0,0?q=" + URLEncoder.encode(addr, "UTF-8"));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(intent, "نمایش نشانی مشتری"));
        } catch (Exception ex) { Toast.makeText(this, "باز کردن نقشه ممکن نشد.", Toast.LENGTH_SHORT).show(); }
    }

    private String firstPhone(JSONObject r) {
        if (r == null) return "";
        for (String key : new String[]{"همراه", "تلفن", "تلفن۲"}) {
            String v = r.optString(key, "").trim();
            if (!v.isEmpty() && !"-".equals(v) && !"—".equals(v)) return v;
        }
        return "—";
    }

    private LinearLayout.LayoutParams weightedButtonLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(44), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        return lp;
    }

    private void openPhoneDialer(String phone) {
        try {
            String p = phone == null ? "" : phone.replace(" ", "").replace("-", "").trim();
            if (p.isEmpty() || "—".equals(p)) { Toast.makeText(this, "شماره‌ای برای تماس ثبت نشده است.", Toast.LENGTH_SHORT).show(); return; }
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + p)));
        } catch (Exception ex) { Toast.makeText(this, "باز کردن تماس ممکن نشد.", Toast.LENGTH_SHORT).show(); }
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
        box.setBackground(roundedStroke(alpha(accent, isLightTheme() ? 16 : 28), 14, alpha(accent, 72)));
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

    private void backFromCustomerDetail(String backTarget) {
        if ("dashboard".equals(backTarget)) showApp("dashboard");
        else {
            activePage = "customers";
            loadCustomers(customersCacheQuery == null ? "" : customersCacheQuery, customersCacheFilter == null ? "all" : customersCacheFilter, false);
        }
    }

    private void addCustomerBackBar(String backTarget, String title) {
        LinearLayout bar = card();
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(10), dp(8), dp(10), dp(8));
        int accent = "dashboard".equals(backTarget) ? navAccent("dashboard") : navAccent("customers");
        bar.setBackground(gradient(new int[]{alpha(accent, isLightTheme() ? 22 : 34), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 20));
        Button back = secondaryButton(withIcon("↩", "بازگشت"));
        back.setTextSize(10.2f);
        back.setOnClickListener(v -> backFromCustomerDetail(backTarget));
        bar.addView(back, new LinearLayout.LayoutParams(dp(112), dp(42)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("جزئیات مشتری", 12.4f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text(stringOr(title, "مشتری") + " • " + ("dashboard".equals(backTarget) ? "بازگشت به داشبورد" : "بازگشت به لیست قبلی مشتریان"), 9.7f, MUTED, Typeface.NORMAL);
        sub.setSingleLine(true); sub.setEllipsize(TextUtils.TruncateAt.END);
        copy.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        bar.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        content.addView(bar, lp);
    }

    private void showCustomerDetail(JSONObject customer, String filter) {
        String code = customer.optString("کد", "");
        String name = customer.optString("نام", "Customer 360");
        content.removeAllViews();
        addHero("گردش حساب مشتری", name + " • کد " + code);
        String backTarget = customer.optString("_back", "customers");
        addCustomerBackBar(backTarget, name);
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
        addCustomerBackBar(backTarget, customer.optString("نام", "Customer 360"));
        addCustomerLedgerFilters(customer, filter);
        addCustomer360Summary(customer);
        addCustomerFollowupNotebook(customer);
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

    private void addCustomer360Summary(JSONObject customer) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 22), alpha(INFO, 14), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("360", SUCCESS), new LinearLayout.LayoutParams(dp(54), dp(54)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("پروفایل ۳۶۰ درجه مشتری", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(customer.optString("نام", "مشتری") + " • " + customerSmartTag(customer.optDouble("مانده", 0), customer.optDouble("جمع_فروش", 0), customer.optInt("تعداد_فاکتور", 0), customer.optDouble("اعتبار", 0)), 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f)); c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        double avg = customer.optInt("تعداد_فاکتور", 0) == 0 ? 0 : customer.optDouble("جمع_فروش", 0) / Math.max(1, customer.optInt("تعداد_فاکتور", 0));
        int risk = customerRiskScore(customer);
        row.addView(customerMiniMetric("میانگین خرید", money(avg), GOLD), weightedMiniLp());
        row.addView(customerMiniMetric("ریسک", formatNumber(risk) + "٪", customerRiskAccent(risk)), weightedMiniLp());
        row.addView(customerMiniMetric("آخرین خرید", stringOr(customer.optString("آخرین_خرید", ""), "—"), INFO), weightedMiniLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); c.addView(row, rp);
        TextView advice = text("پیشنهاد میلو: " + customerAdvice(customer), 10.8f, alpha(TEXT, 220), Typeface.BOLD);
        advice.setGravity(Gravity.CENTER); advice.setPadding(dp(10), dp(8), dp(10), dp(8)); advice.setBackground(roundedStroke(alpha(INFO, 18), 16, alpha(INFO, 62)));
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2); ap.setMargins(0, dp(10), 0, 0); c.addView(advice, ap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); content.addView(c, lp);
    }

    private String customerAdvice(JSONObject customer) {
        double balance = customer.optDouble("مانده", 0);
        int invoices = customer.optInt("تعداد_فاکتور", 0);
        double sales = customer.optDouble("جمع_فروش", 0);
        if (balance > 0 && sales > 0) return "مشتری ارزشمند اما وصول‌محور است؛ فروش جدید را با تسویه بخشی از مانده جلو ببر.";
        if (invoices == 0) return "برای بازفعال‌سازی، یک پیشنهاد محدود و تماس کوتاه بهتر از تخفیف کور است.";
        if (balance < 0) return "اعتبار مثبت دارد؛ برای نگهداشت و خرید بعدی پیشنهاد ویژه بده.";
        return "وضعیت متعادل است؛ رابطه را حفظ کن و سقف اعتبار را بی‌دلیل بالا نبر.";
    }

    private String todayDateText() {
        try { return new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(new Date()); }
        catch (Exception ignored) { return nowText().split(" ")[0]; }
    }

    private String selectedDateFromButton(Button b) {
        if (b == null) return todayDateText();
        String t = b.getText() == null ? "" : b.getText().toString();
        int idx = t.lastIndexOf(':');
        String d = idx >= 0 ? t.substring(idx + 1).trim() : t.trim();
        return d.isEmpty() ? todayDateText() : d;
    }

    private void showFollowupDatePicker(Button target) {
        try {
            String initial = selectedDateFromButton(target);
            int y, m, d;
            try {
                String[] parts = initial.split("/");
                y = Integer.parseInt(parts[0]); m = Integer.parseInt(parts[1]) - 1; d = Integer.parseInt(parts[2]);
            } catch (Exception ex) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                y = cal.get(java.util.Calendar.YEAR); m = cal.get(java.util.Calendar.MONTH); d = cal.get(java.util.Calendar.DAY_OF_MONTH);
            }
            LinearLayout box = new LinearLayout(this);
            box.setOrientation(LinearLayout.VERTICAL);
            box.setPadding(dp(10), dp(10), dp(10), dp(6));
            TextView title = text("تقویم یادآوری مشتری", 16, TEXT, Typeface.BOLD);
            title.setGravity(Gravity.CENTER);
            title.setPadding(dp(8), dp(8), dp(8), dp(8));
            title.setBackground(gradient(new int[]{alpha(GOLD_2, 52), alpha(INFO, 28)}, GradientDrawable.Orientation.RIGHT_LEFT, 18));
            box.addView(title, new LinearLayout.LayoutParams(-1, -2));
            DatePicker picker = new DatePicker(this);
            picker.setCalendarViewShown(true);
            picker.setSpinnersShown(true);
            picker.init(y, m, d, null);
            LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, -2); pp.setMargins(0, dp(10), 0, 0); box.addView(picker, pp);
            AlertDialog dlg = new AlertDialog.Builder(this)
                    .setView(box)
                    .setNegativeButton("بستن", null)
                    .setPositiveButton("انتخاب", (di, w) -> {
                        String value = String.format(Locale.US, "%04d/%02d/%02d", picker.getYear(), picker.getMonth() + 1, picker.getDayOfMonth());
                        if (target != null) target.setText("انتخاب تاریخ یادآوری: " + value);
                    })
                    .create();
            dlg.setOnShowListener(di -> { if (dlg.getWindow() != null) dlg.getWindow().setBackgroundDrawable(roundedStroke(alpha(SURFACE, 250), 28, alpha(GOLD, 85))); });
            dlg.show();
        } catch (Exception ex) { Toast.makeText(this, "باز کردن تقویم ممکن نشد.", Toast.LENGTH_SHORT).show(); }
    }

    private void addCustomerFollowupNotebook(JSONObject customer) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(GOLD, 20), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("دفترچه پیگیری مشتری", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        String code = customer.optString("کد", "");
        String noteKey = "follow_note_" + code;
        String dateKey = "follow_date_" + code;
        String statusKey = "follow_status_" + code;
        String historyKey = "follow_history_" + code;
        c.addView(text("وضعیت فعلی: " + prefs.getString(statusKey, "ثبت نشده") + " • یادآوری بعدی: " + prefs.getString(dateKey, "—"), 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        String hist = prefs.getString(historyKey, "");
        if (hist != null && !hist.trim().isEmpty()) {
            TextView timeline = text("تاریخچه:\n" + limitText(hist, 420), 10.2f, alpha(TEXT, 205), Typeface.NORMAL);
            timeline.setLineSpacing(dp(2), 1.04f);
            timeline.setBackground(roundedStroke(alpha(INFO, 14), 14, alpha(INFO, 55)));
            timeline.setPadding(dp(8), dp(7), dp(8), dp(7));
            LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2); hp.setMargins(0, dp(8), 0, 0); c.addView(timeline, hp);
        }
        EditText note = input("توضیح پیگیری / قول پرداخت", prefs.getString(noteKey, ""), false);
        note.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL); if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) note.setTextDirection(View.TEXT_DIRECTION_RTL);
        Button next = secondaryButton("انتخاب تاریخ یادآوری: " + stringOr(prefs.getString(dateKey, ""), todayDateText()));
        next.setTextSize(10.8f);
        next.setOnClickListener(v -> showFollowupDatePicker(next));
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(-1, dp(48)); np.setMargins(0, dp(10), 0, dp(7)); c.addView(note, np);
        LinearLayout.LayoutParams dpLp = new LinearLayout.LayoutParams(-1, dp(48)); dpLp.setMargins(0, 0, 0, dp(8)); c.addView(next, dpLp);
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button called = secondaryButton("تماس شد"); Button promised = secondaryButton("قول پرداخت"); Button save = primaryButton("ذخیره");
        called.setTextSize(9.8f); promised.setTextSize(9.8f); save.setTextSize(9.8f);
        called.setOnClickListener(v -> { appendFollowupHistory(historyKey, "تماس گرفته شد", note.getText().toString()); prefs.edit().putString(statusKey, "تماس گرفته شد").apply(); Toast.makeText(this, "ثبت شد.", Toast.LENGTH_SHORT).show(); showCustomerDetail(customer, "all"); });
        promised.setOnClickListener(v -> { appendFollowupHistory(historyKey, "قول پرداخت داد", note.getText().toString()); prefs.edit().putString(statusKey, "قول پرداخت داد").apply(); Toast.makeText(this, "ثبت شد.", Toast.LENGTH_SHORT).show(); showCustomerDetail(customer, "all"); });
        save.setOnClickListener(v -> { appendFollowupHistory(historyKey, "یادداشت", note.getText().toString()); prefs.edit().putString(noteKey, note.getText().toString()).putString(dateKey, selectedDateFromButton(next)).putString(statusKey, "نیازمند پیگیری").apply(); Toast.makeText(this, "یادداشت پیگیری ذخیره شد.", Toast.LENGTH_SHORT).show(); showCustomerDetail(customer, "all"); });
        row.addView(called, weightedButtonLp()); row.addView(promised, weightedButtonLp()); row.addView(save, weightedButtonLp());
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); content.addView(c, lp);
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


    private void loadVisitorDashboard() {
        content.removeAllViews();
        addHero("داشبورد ویزیتور", "فروش روزانه، اهداف، وضعیت مشتریان و مسیر اقدام سریع برای ویزیتور");
        addManualRefreshPanel("visitor_dashboard", "بروزرسانی داشبورد ویزیتور", "اطلاعات اختصاصی کاربر فعلی", () -> loadVisitorDashboard());
        addLoading(content, "در حال ساخت داشبورد ویزیتور…");
        runDb(this::queryVisitorDashboardSql, new DbCallback() {
            @Override public void ok(String body) {
                try { renderVisitorDashboard(new JSONObject(body)); markRefresh("visitor_dashboard"); }
                catch (Exception e) { showPageError("داشبورد ویزیتور", e, () -> loadVisitorDashboard()); }
            }
            @Override public void fail(Exception e) { showPageError("داشبورد ویزیتور", e, () -> loadVisitorDashboard()); }
        });
    }

    private String queryVisitorDashboardSql() throws Exception {
        try (Connection c = openConnection()) {
            JSONObject out = new JSONObject();
            Integer vid = currentVisitorScopeId();
            out.put("visitorId", vid == null ? JSONObject.NULL : vid);
            out.put("visitor", session == null ? currentAccountName() : session.userName);
            out.put("date", latestDate(c, "sailfact", "date"));
            JSONObject sales = new JSONObject();
            sales.put("total", 0); sales.put("count", 0); sales.put("customers", 0); sales.put("avg", 0);
            if (vid != null && vid > 0 && tableExists(c, "sailfact")) {
                Set<String> sf = columns(c, "sailfact");
                String vis = resolveFlexible(sf, "vis_rdf", "VisitorID", "visid", "visitor", "shvis");
                String amount = resolveFlexible(sf, "all", "amount", "Total", "mablagh");
                String date = resolveFlexible(sf, "date", "DATE", "t_date");
                String shmo = resolveFlexible(sf, "shmo", "SHMO", "CustomerCode");
                if (vis != null && amount != null) {
                    String latest = out.optString("date", "");
                    String whereDate = date != null && latest != null && !latest.trim().isEmpty() ? " AND TRY_CONVERT(nvarchar(30),[" + date + "])=?" : "";
                    String sql = "SELECT COUNT_BIG(1), ISNULL(SUM(TRY_CONVERT(decimal(19,2),[" + amount + "])),0), " + (shmo == null ? "CAST(0 AS bigint)" : "COUNT(DISTINCT [" + shmo + "])") + " FROM dbo.sailfact WHERE TRY_CONVERT(nvarchar(100),[" + vis + "])=?" + whereDate + activeAnd(sf, "");
                    try (PreparedStatement ps = c.prepareStatement(sql)) {
                        ps.setString(1, String.valueOf(vid));
                        if (!whereDate.isEmpty()) ps.setString(2, latest);
                        try (ResultSet r = ps.executeQuery()) {
                            if (r.next()) { long count = r.getLong(1); double total = r.getDouble(2); sales.put("count", count); sales.put("total", total); sales.put("customers", r.getLong(3)); sales.put("avg", count == 0 ? 0 : total / count); }
                        }
                    }
                }
            }
            out.put("sales", sales);
            out.put("topDebtors", queryTopDebtors(c));
            out.put("overdueInvoices", queryOverdueInvoices(c));
            out.put("inactiveCustomers", queryInactiveCustomers(c));
            out.put("goals", queryVisitorGoals(c, vid));
            return out.toString();
        }
    }

    private JSONArray queryVisitorGoals(Connection c, Integer vid) {
        JSONArray arr = new JSONArray();
        try {
            if (c == null || vid == null || vid <= 0 || !tableExists(c, "vis_goals")) return arr;
            Set<String> cols = columns(c, "vis_goals");
            String vis = resolveFlexible(cols, "vis_rdf", "VisitorID", "visid", "visitor", "shvis");
            String title = resolveFlexible(cols, "title", "name", "Name", "goal_name", "شرح", "sharh");
            String target = resolveFlexible(cols, "target", "amount", "mablagh", "goal", "هدف");
            String done = resolveFlexible(cols, "done", "achieved", "sale", "forosh", "انجام");
            String date = resolveFlexible(cols, "date", "tarikh", "DATE");
            if (vis == null) return arr;
            String sql = "SELECT TOP (6) " + (date == null ? "CAST(NULL AS nvarchar(30))" : "TRY_CONVERT(nvarchar(30),[" + date + "])") + ", " + (title == null ? "N'هدف فروش'" : "TRY_CONVERT(nvarchar(220),[" + title + "])") + ", " + (target == null ? "CAST(0 AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),[" + target + "])") + ", " + (done == null ? "CAST(0 AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),[" + done + "])") + " FROM dbo.vis_goals WHERE TRY_CONVERT(nvarchar(100),[" + vis + "])=? ORDER BY 1 DESC";
            try (PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1, String.valueOf(vid)); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("date", stringOr(r.getString(1), "—")); o.put("title", stringOr(r.getString(2), "هدف فروش")); o.put("target", r.getDouble(3)); o.put("done", r.getDouble(4)); arr.put(o); } } }
        } catch (Exception ignored) { }
        return arr;
    }

    private void renderVisitorDashboard(JSONObject data) {
        content.removeAllViews();
        addHero("داشبورد ویزیتور", "نمای اختصاصی " + stringOr(data.optString("visitor"), "ویزیتور") + " • تاریخ فروش: " + stringOr(data.optString("date"), "—"));
        addManualRefreshPanel("visitor_dashboard", "بروزرسانی داشبورد ویزیتور", "آخرین بروزرسانی: " + lastRefreshText("visitor_dashboard"), () -> loadVisitorDashboard());
        JSONObject sales = data.optJSONObject("sales");
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(navAccent("visitor_dashboard"), 30), alpha(GOLD, 16), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 26));
        c.addView(text("عملکرد فروش من", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(dashboardMiniMetric("فروش روز", money(sales == null ? 0 : sales.opt("total")), "جمع", GOLD), dashboardMiniLp());
        row.addView(dashboardMiniMetric("فاکتور", formatNumber(sales == null ? 0 : sales.opt("count")), "امروز", INFO), dashboardMiniLp());
        row.addView(dashboardMiniMetric("مشتری", formatNumber(sales == null ? 0 : sales.opt("customers")), "خریدار", SUCCESS), dashboardMiniLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); c.addView(row, rp);
        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        Button showcase = primaryButton("ورود به ویترین"); showcase.setOnClickListener(v -> showApp("showcase"));
        Button cart = secondaryButton("سبد خرید " + formatNumber(visitorCartItems.length())); cart.setOnClickListener(v -> showApp("cart"));
        row2.addView(showcase, weightedButtonLp()); row2.addView(cart, weightedButtonLp());
        LinearLayout.LayoutParams r2p = new LinearLayout.LayoutParams(-1, -2); r2p.setMargins(0, dp(10), 0, 0); c.addView(row2, r2p);
        LinearLayout row3 = new LinearLayout(this); row3.setOrientation(LinearLayout.HORIZONTAL);
        Button mine = secondaryButton("پیش‌فاکتورهای من"); mine.setOnClickListener(v -> loadMyPrefactors());
        Button route = secondaryButton("مسیر بازدید امروز"); route.setOnClickListener(v -> loadVisitRoutePage(""));
        row3.addView(mine, weightedButtonLp()); row3.addView(route, weightedButtonLp());
        LinearLayout.LayoutParams r3p = new LinearLayout.LayoutParams(-1, -2); r3p.setMargins(0, dp(8), 0, 0); c.addView(row3, r3p);
        LinearLayout row4 = new LinearLayout(this); row4.setOrientation(LinearLayout.HORIZONTAL);
        Button report = secondaryButton("جمع‌بندی پایان روز"); report.setOnClickListener(v -> showEndOfDayReportDialog());
        Button queue = offlineQueue().length() > 0 ? primaryButton("ارسال صف آفلاین") : secondaryButton("صف آفلاین خالی"); queue.setOnClickListener(v -> trySendOfflineQueue());
        row4.addView(report, weightedButtonLp()); row4.addView(queue, weightedButtonLp());
        LinearLayout.LayoutParams r4p = new LinearLayout.LayoutParams(-1, -2); r4p.setMargins(0, dp(8), 0, 0); c.addView(row4, r4p);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, 0, 0, dp(12)); content.addView(c, cp);
        addVisitorGoalsCard(data.optJSONArray("goals"));
        addDashboardInsightTable(data);
    }

    private void addVisitorGoalsCard(JSONArray goals) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 20), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("اهداف فروش ویزیتور", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        if (goals == null || goals.length() == 0) { c.addView(text("هدف ثبت‌شده‌ای برای این ویزیتور پیدا نشد؛ مدیر می‌تواند در جدول اهداف، هدف روزانه/ماهانه تعریف کند.", 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2)); }
        for (int i = 0; goals != null && i < goals.length(); i++) {
            JSONObject g = goals.optJSONObject(i); if (g == null) continue;
            double target = g.optDouble("target", 0), done = g.optDouble("done", 0);
            int accent = done >= target && target > 0 ? SUCCESS : WARNING;
            addActionItem(c, g.optString("date", "—"), g.optString("title", "هدف فروش") + " • انجام: " + money(done) + " از " + money(target), accent);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void loadShowcase(String query, String filter) {
        if (!canUsePermission("showcase")) { renderLockedSection("showcase"); return; }
        String q = query == null ? "" : query;
        String f = filter == null || filter.trim().isEmpty() ? "all" : filter;
        content.removeAllViews();
        addHero("ویترین محصولات", "کارت‌های سه‌بعدی هماهنگ با تم، فیلتر هوشمند و افزودن سریع به سبد خرید");
        addManualRefreshPanel("showcase", "بروزرسانی ویترین", "سبد فعلی: " + formatNumber(visitorCartItems.length()) + " قلم", () -> loadShowcase(q, f));
        addSearchBox("جستجوی محصول، کد یا بارکد…", q, qq -> loadShowcase(qq, f));
        addShowcaseFilters(q, f);
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال چیدن ویترین…");
        runDb(() -> queryProducts(q, f), new DbCallback() {
            @Override public void ok(String body) {
                try { renderShowcaseProducts(new JSONArray(body), q, f); markRefresh("showcase"); }
                catch (Exception e) { showPageError("ویترین", e, () -> loadShowcase(q, f)); }
            }
            @Override public void fail(Exception e) { showPageError("ویترین", e, () -> loadShowcase(q, f)); }
        });
    }

    private void addShowcaseFilters(String query, String active) {
        HorizontalScrollView scroll = new HorizontalScrollView(this); styleHorizontalScroll(scroll);
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        String[][] filters = {{"all","همه"},{"stock","موجود"},{"top","پرفروش"},{"low","اتمام/کمبود"},{"idle","کم‌گردش"},{"priced","قیمت‌دار"},{"image","تصویردار"},{"package","بسته‌بندی"}};
        for (String[] f : filters) {
            Button b = f[0].equals(active) ? primaryButton(f[1]) : secondaryButton(f[1]);
            b.setTextSize(9.5f); b.setOnClickListener(v -> loadShowcase(query, f[0]));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(92), dp(39)); lp.setMargins(dp(3), 0, dp(3), dp(8)); row.addView(b, lp);
        }
        Button scan = secondaryButton("بارکد/کدخوان"); scan.setTextSize(9.0f); scan.setOnClickListener(v -> showBarcodeSearchDialog());
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(dp(112), dp(39)); sp.setMargins(dp(3), 0, dp(3), dp(8)); row.addView(scan, sp);
        Button cart = primaryButton("سبد: " + formatNumber(visitorCartItems.length())); cart.setTextSize(9.5f); cart.setOnClickListener(v -> showApp("cart"));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(dp(100), dp(39)); cp.setMargins(dp(3), 0, dp(3), dp(8)); row.addView(cart, cp);
        scroll.addView(row, new FrameLayout.LayoutParams(-2, -2)); content.addView(scroll, new LinearLayout.LayoutParams(-1, -2));
    }

    private void showBarcodeSearchDialog() {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(10), dp(8), dp(10), dp(6));
        box.addView(text("جستجوی سریع با بارکد/کد کالا", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        EditText code = input("بارکد یا کد کالا را وارد/اسکن کنید", "", false); code.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, dp(50)); cp.setMargins(0, dp(8), 0, 0); box.addView(code, cp);
        AlertDialog dlg = new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن", null).setPositiveButton("جستجو", null).create();
        dlg.setOnShowListener(d -> { styleMeelanoDialog(dlg, navAccent("showcase")); Button ok = dlg.getButton(AlertDialog.BUTTON_POSITIVE); if (ok != null) ok.setOnClickListener(v -> { String q = code.getText().toString().trim(); dlg.dismiss(); loadShowcase(q, "all"); }); });
        dlg.show();
    }

    private void renderShowcaseProducts(JSONArray rows, String query, String filter) {
        content.removeAllViews();
        addHero("ویترین محصولات", "انتخاب محصول، تعداد/وزن و انتقال به سبد پیش‌فاکتور");
        addManualRefreshPanel("showcase", "بروزرسانی ویترین", "آخرین بروزرسانی: " + lastRefreshText("showcase"), () -> loadShowcase(query, filter));
        addSearchBox("جستجوی محصول، کد یا بارکد…", query, q -> loadShowcase(q, filter));
        addShowcaseFilters(query, filter);
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        if (rows == null || rows.length() == 0) { addEmptyTo(list, "محصولی برای این فیلتر پیدا نشد."); addShowcaseFloatingCartBar(); return; }
        for (int i = 0; i < rows.length(); i++) addShowcaseProductCard(list, rows.optJSONObject(i), i, query, filter);
        addShowcaseFloatingCartBar();
    }

    private void addShowcaseFloatingCartBar() {
        LinearLayout c = card();
        c.setPadding(dp(10), dp(9), dp(10), dp(9));
        c.setBackground(gradient(new int[]{alpha(navAccent("cart"), 70), alpha(GOLD, 28), alpha(SURFACE, 252)}, GradientDrawable.Orientation.LEFT_RIGHT, 22));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(text("سبد: " + formatNumber(visitorCartItems.length()) + " قلم • " + money(cartTotal()), 12.0f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(0, -2, 1f));
        Button b = primaryButton("رفتن به سبد"); b.setTextSize(9.8f); b.setOnClickListener(v -> showApp("cart")); row.addView(b, new LinearLayout.LayoutParams(dp(118), dp(42)));
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(4), 0, dp(12)); content.addView(c, lp);
    }

    private void addShowcaseProductCard(LinearLayout parent, JSONObject r, int index, String query, String filter) {
        if (r == null) return;
        int accent = index % 3 == 0 ? navAccent("showcase") : (index % 3 == 1 ? SUCCESS : GOLD);
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(Color.WHITE, isLightTheme()?66:16), alpha(accent, isLightTheme()?30:44), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 28));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img = new ImageView(this); img.setScaleType(ImageView.ScaleType.CENTER_CROP); img.setPadding(dp(5), dp(5), dp(5), dp(5)); img.setBackground(roundedStroke(alpha(accent, 40), 18, alpha(accent, 95))); applyProductImage(img, r);
        head.addView(img, new LinearLayout.LayoutParams(dp(74), dp(74)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text(r.optString("نام", "محصول"), 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("کد " + r.optString("کد", "—") + " • " + r.optString("گروه", "بدون گروه"), 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout metrics = new LinearLayout(this); metrics.setOrientation(LinearLayout.HORIZONTAL);
        metrics.addView(customerMiniMetric("موجودی", formatNumber(r.opt("موجودی")) + " " + r.optString("واحد", ""), SUCCESS), weightedMiniLp());
        metrics.addView(customerMiniMetric("در بسته", formatNumber(r.opt("تعداد_در_بسته")), INFO), weightedMiniLp());
        metrics.addView(customerMiniMetric("قیمت ۱", money(r.opt("قیمت_فروش")), GOLD), weightedMiniLp());
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2); mp.setMargins(0, dp(9), 0, 0); c.addView(metrics, mp);
        LinearLayout metrics2 = new LinearLayout(this); metrics2.setOrientation(LinearLayout.HORIZONTAL);
        metrics2.addView(customerMiniMetric("قیمت ۲", money(r.opt("قیمت_فروش۲")), WARNING), weightedMiniLp());
        metrics2.addView(customerMiniMetric("واحد", stringOr(r.optString("واحد", ""), "—"), INFO), weightedMiniLp());
        metrics2.addView(customerMiniMetric("سبد", cartFindIndex(r.optString("کد", "")) >= 0 ? "انتخاب شده" : "—", cartFindIndex(r.optString("کد", "")) >= 0 ? SUCCESS : MUTED), weightedMiniLp());
        LinearLayout.LayoutParams m2p = new LinearLayout.LayoutParams(-1, -2); m2p.setMargins(0, dp(6), 0, 0); c.addView(metrics2, m2p);
        LinearLayout action = new LinearLayout(this); action.setOrientation(LinearLayout.HORIZONTAL);
        EditText qty = input("تعداد/وزن", cartQtyFor(r.optString("کد", "")), false); qty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Button add = primaryButton(cartFindIndex(r.optString("کد", "")) >= 0 ? "بروزرسانی ۱" : "قیمت ۱"); add.setTextSize(8.7f); add.setOnClickListener(v -> { addOrUpdateCartItem(r, qty.getText().toString(), 1); Toast.makeText(this, "سبد بروزرسانی شد.", Toast.LENGTH_SHORT).show(); loadShowcase(query, filter); });
        Button add2 = secondaryButton("قیمت ۲"); add2.setTextSize(8.7f); add2.setOnClickListener(v -> { addOrUpdateCartItem(r, qty.getText().toString(), 2); Toast.makeText(this, "با قیمت ۲ به سبد اضافه شد.", Toast.LENGTH_SHORT).show(); loadShowcase(query, filter); });
        Button remove = secondaryButton("حذف"); remove.setTextSize(8.7f); remove.setOnClickListener(v -> { removeCartItem(r.optString("کد", "")); loadShowcase(query, filter); });
        action.addView(qty, new LinearLayout.LayoutParams(0, dp(44), 1f)); action.addView(add, weightedButtonLp()); action.addView(add2, weightedButtonLp()); if (cartFindIndex(r.optString("کد", "")) >= 0) action.addView(remove, weightedButtonLp());
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2); ap.setMargins(0, dp(10), 0, 0); c.addView(action, ap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(10)); parent.addView(c, lp);
    }

    private int cartFindIndex(String code) {
        if (code == null) code = "";
        for (int i = 0; i < visitorCartItems.length(); i++) { JSONObject o = visitorCartItems.optJSONObject(i); if (o != null && code.equals(o.optString("code", ""))) return i; }
        return -1;
    }

    private String cartQtyFor(String code) {
        int i = cartFindIndex(code); if (i < 0) return "1";
        JSONObject o = visitorCartItems.optJSONObject(i); return o == null ? "1" : String.valueOf(o.optDouble("qty", 1));
    }

    private void addOrUpdateCartItem(JSONObject product, String qtyText) { addOrUpdateCartItem(product, qtyText, 1); }

    private void addOrUpdateCartItem(JSONObject product, String qtyText, int priceTier) {
        if (product == null) return;
        double qty = parseNumber(qtyText, 1);
        if (qty <= 0) qty = 1;
        try {
            String code = product.optString("کد", "");
            int ix = cartFindIndex(code);
            JSONObject old = ix >= 0 ? visitorCartItems.optJSONObject(ix) : null;
            double price1 = product.optDouble("قیمت_فروش", 0);
            double price2 = product.optDouble("قیمت_فروش۲", 0);
            int tier = priceTier == 2 && price2 > 0 ? 2 : 1;
            double price = tier == 2 ? price2 : price1;
            JSONObject item = new JSONObject();
            item.put("code", code); item.put("name", product.optString("نام", "محصول")); item.put("unit", product.optString("واحد", "")); item.put("qty", qty);
            item.put("price", price); item.put("price1", price1); item.put("price2", price2); item.put("priceTier", String.valueOf(tier));
            item.put("stock", product.optDouble("موجودی", 0)); item.put("pack", product.optDouble("تعداد_در_بسته", 1)); item.put("image", product.optString("تصویر", ""));
            item.put("lineDiscount", old == null ? 0 : old.optDouble("lineDiscount", 0)); item.put("note", old == null ? "" : old.optString("note", "")); item.put("amount", cartItemNet(item));
            if (ix >= 0) visitorCartItems.put(ix, item); else visitorCartItems.put(item);
        } catch (Exception ignored) { }
    }

    private void removeCartItem(String code) {
        JSONArray out = new JSONArray();
        for (int i = 0; i < visitorCartItems.length(); i++) { JSONObject o = visitorCartItems.optJSONObject(i); if (o != null && !stringOr(code, "").equals(o.optString("code", ""))) out.put(o); }
        visitorCartItems = out;
    }

    private double parseNumber(String text, double fallback) {
        try {
            String n = normalizeDigits(text == null ? "" : text).replace(',', '.').replace(" ", "").trim();
            if (n.isEmpty()) return fallback;
            return Double.parseDouble(n);
        } catch (Exception ignored) { return fallback; }
    }

    private double cartItemGross(JSONObject item) { return item == null ? 0 : Math.max(0, item.optDouble("qty", 0) * item.optDouble("price", 0)); }
    private double cartItemDiscount(JSONObject item) { double g = cartItemGross(item); return item == null ? 0 : Math.max(0, Math.min(g, item.optDouble("lineDiscount", 0))); }
    private double cartItemNet(JSONObject item) { return Math.max(0, cartItemGross(item) - cartItemDiscount(item)); }
    private double cartSubtotal() { double t = 0; for (int i = 0; i < visitorCartItems.length(); i++) t += cartItemGross(visitorCartItems.optJSONObject(i)); return t; }
    private double cartLineDiscounts() { double t = 0; for (int i = 0; i < visitorCartItems.length(); i++) t += cartItemDiscount(visitorCartItems.optJSONObject(i)); return t; }
    private double cartNetBeforeTax() { return Math.max(0, cartSubtotal() - cartLineDiscounts() - Math.max(0, visitorCartGlobalDiscount)); }
    private double cartTaxAmount() { return Math.max(0, cartNetBeforeTax() * Math.max(0, visitorCartTaxPercent) / 100d); }
    private double cartTotal() { return cartNetBeforeTax() + cartTaxAmount(); }

    private void renderCartPage() {
        if (!canUsePermission("cart")) { renderLockedSection("cart"); return; }
        content.removeAllViews();
        addHero("سبد خرید و پیش‌فاکتور", "انتخاب مشتری، مرور اقلام، توضیحات، امضا و ارسال پیش‌فاکتور");
        addManualRefreshPanel("cart", "بروزرسانی سبد", "اقلام فعلی: " + formatNumber(visitorCartItems.length()), () -> renderCartPage());
        addCartWorkflowButtons();
        addCartCustomerCard();
        addCartItemsCard();
        addCartTermsCard();
        addCartNotesAndActions();
        addCartDraftsAndOfflineCard();
    }

    private void addCartWorkflowButtons() {
        LinearLayout c = card(); c.setPadding(dp(10), dp(10), dp(10), dp(10)); c.setBackground(gradient(new int[]{alpha(navAccent("cart"), 26), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button mine = secondaryButton("پیش‌فاکتورهای من"); mine.setTextSize(9.3f); mine.setOnClickListener(v -> loadMyPrefactors());
        Button route = secondaryButton("مسیر بازدید"); route.setTextSize(9.3f); route.setOnClickListener(v -> loadVisitRoutePage(""));
        Button barcode = secondaryButton("بارکد/کد"); barcode.setTextSize(9.3f); barcode.setOnClickListener(v -> showBarcodeSearchDialog());
        row.addView(mine, weightedButtonLp()); row.addView(route, weightedButtonLp()); row.addView(barcode, weightedButtonLp());
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addCartCustomerCard() {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(navAccent("cart"), 24), alpha(SURFACE, 250)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("مشتری پیش‌فاکتور", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        if (visitorCartCustomer == null) c.addView(text("هنوز مشتری انتخاب نشده است. برای ویزیتور فقط مشتریان مرتبط با خودش نمایش داده می‌شود.", 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        else {
            c.addView(text(visitorCartCustomer.optString("name", "مشتری") + " • کد " + visitorCartCustomer.optString("code", "—"), 13, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
            row.addView(dashboardMiniMetric("مانده", money(visitorCartCustomer.opt("balance")), "فعلی", visitorCartCustomer.optDouble("balance",0)>0?DANGER:SUCCESS), dashboardMiniLp());
            row.addView(dashboardMiniMetric("سقف اعتبار", visitorCartCustomer.optDouble("creditLimit",0)>0?money(visitorCartCustomer.opt("creditLimit")):"تعریف نشده", "کنترل", creditRisk() ? DANGER : INFO), dashboardMiniLp());
            row.addView(dashboardMiniMetric("آخرین خرید", stringOr(visitorCartCustomer.optString("lastSale"), "—"), formatNumber(visitorCartCustomer.opt("invoiceCount")) + " فاکتور", GOLD), dashboardMiniLp());
            LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(8), 0, 0); c.addView(row, rp);
            String risk = cartApprovalReason();
            if (!risk.isEmpty()) c.addView(text("هشدار فروش: " + risk, 10.4f, DANGER, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            if (!visitorCartCustomer.optString("address", "").trim().isEmpty()) c.addView(text("آدرس پیش‌فرض: " + visitorCartCustomer.optString("address", ""), 10.3f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        }
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button pick = primaryButton("انتخاب/جستجوی مشتری"); pick.setOnClickListener(v -> { if (ensurePermission("customer_select", "انتخاب مشتری")) showCartCustomerPicker(""); });
        Button showcase = secondaryButton("افزودن محصول"); showcase.setOnClickListener(v -> showApp("showcase"));
        row.addView(pick, weightedButtonLp()); row.addView(showcase, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private boolean creditRisk() {
        if (visitorCartCustomer == null) return false;
        double bal = visitorCartCustomer.optDouble("balance", 0), credit = visitorCartCustomer.optDouble("creditLimit", 0);
        return (credit > 0 && bal + cartTotal() > credit) || visitorCartCustomer.optBoolean("blocked", false);
    }

    private String cartApprovalReason() {
        List<String> r = new ArrayList<>();
        if (visitorCartCustomer != null) {
            double bal = visitorCartCustomer.optDouble("balance", 0), credit = visitorCartCustomer.optDouble("creditLimit", 0);
            if (visitorCartCustomer.optBoolean("blocked", false)) r.add("مشتری مسدود/پرریسک است");
            if (credit > 0 && bal + cartTotal() > credit) r.add("عبور از سقف اعتبار مشتری");
            if (bal > 0 && "اعتباری".equals(visitorCartSettlement)) r.add("فروش اعتباری برای مشتری بدهکار");
        }
        if (cartLineDiscounts() + visitorCartGlobalDiscount > 0) r.add("دارای تخفیف و نیازمند تایید فروش/مدیریت");
        return join(r, "، ");
    }

    private String finalCartStatus() { return cartApprovalReason().trim().isEmpty() ? "sent" : "pending_approval"; }

    private void addCartItemsCard() {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(GOLD, 22), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("اقلام انتخاب‌شده", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        if (visitorCartItems.length() == 0) addEmptyTo(c, "سبد خالی است؛ از ویترین محصول اضافه کنید.");
        for (int i = 0; i < visitorCartItems.length(); i++) {
            JSONObject item = visitorCartItems.optJSONObject(i); if (item == null) continue;
            addCartItemRow(c, item, i);
        }
        LinearLayout total = new LinearLayout(this); total.setOrientation(LinearLayout.HORIZONTAL); total.setGravity(Gravity.CENTER_VERTICAL); total.setPadding(dp(10), dp(9), dp(10), dp(9)); total.setBackground(roundedStroke(alpha(SUCCESS, 18), 16, alpha(SUCCESS, 72)));
        total.addView(text("جمع خام: " + money(cartSubtotal()) + " • تخفیف: " + money(cartLineDiscounts() + visitorCartGlobalDiscount), 10.7f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(0, -2, 1f));
        total.addView(text(money(cartTotal()), 13, SUCCESS, Typeface.BOLD), new LinearLayout.LayoutParams(-2, -2));
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2); tp.setMargins(0, dp(10), 0, 0); c.addView(total, tp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addCartItemRow(LinearLayout parent, JSONObject item, int index) {
        int accent = index % 2 == 0 ? navAccent("cart") : INFO;
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.VERTICAL); row.setPadding(dp(8), dp(8), dp(8), dp(8)); row.setBackground(roundedStroke(alpha(accent, 14), 16, alpha(accent, 58)));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        TextView idx = text(formatNumber(index + 1), 10, Color.WHITE, Typeface.BOLD); idx.setGravity(Gravity.CENTER); idx.setBackground(gradient(new int[]{accent, mix(accent, Color.BLACK, .25f)}, GradientDrawable.Orientation.TL_BR, 999)); head.addView(idx, new LinearLayout.LayoutParams(dp(30), dp(30)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(8), 0, dp(8), 0);
        copy.addView(text(item.optString("name", "محصول"), 12.4f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text("کد " + item.optString("code", "—") + " • موجودی " + formatNumber(item.optDouble("stock", 0)) + " • بسته " + formatNumber(item.optDouble("pack", 1)), 9.7f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        LinearLayout buttons = new LinearLayout(this); buttons.setOrientation(LinearLayout.VERTICAL);
        Button edit = secondaryButton("ویرایش"); edit.setTextSize(8.0f); edit.setOnClickListener(v -> showEditCartItemDialog(index));
        Button del = secondaryButton("حذف"); del.setTextSize(8.0f); del.setOnClickListener(v -> { removeCartItem(item.optString("code", "")); renderCartPage(); });
        buttons.addView(edit, new LinearLayout.LayoutParams(dp(68), dp(31))); buttons.addView(del, new LinearLayout.LayoutParams(dp(68), dp(31))); head.addView(buttons, new LinearLayout.LayoutParams(dp(72), -2));
        row.addView(head, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout metrics = new LinearLayout(this); metrics.setOrientation(LinearLayout.HORIZONTAL);
        metrics.addView(customerMiniMetric("تعداد", formatNumber(item.optDouble("qty", 0)) + " " + item.optString("unit", ""), SUCCESS), weightedMiniLp());
        metrics.addView(customerMiniMetric("فی", money(item.optDouble("price", 0)), GOLD), weightedMiniLp());
        metrics.addView(customerMiniMetric("خالص", money(cartItemNet(item)), accent), weightedMiniLp());
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, -2); mp.setMargins(0, dp(7), 0, 0); row.addView(metrics, mp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(8), 0, 0); parent.addView(row, lp);
    }

    private void showEditCartItemDialog(int index) {
        JSONObject item = visitorCartItems.optJSONObject(index); if (item == null) return;
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(10), dp(8), dp(10), dp(6));
        box.addView(text("ویرایش کالا: " + item.optString("name", "محصول"), 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        EditText qty = input("تعداد/وزن", String.valueOf(item.optDouble("qty", 1)), false); qty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText price = input("قیمت واحد", String.valueOf(item.optDouble("price", 0)), false); price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText discount = input("تخفیف ردیف", String.valueOf(item.optDouble("lineDiscount", 0)), false); discount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText note = input("توضیح این کالا", item.optString("note", ""), false); note.setSingleLine(false); note.setMinLines(2);
        for (EditText e : new EditText[]{qty, price, discount}) { LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(-1, dp(48)); ep.setMargins(0, dp(7), 0, 0); box.addView(e, ep); }
        LinearLayout priceRow = new LinearLayout(this); priceRow.setOrientation(LinearLayout.HORIZONTAL);
        Button p1 = secondaryButton("قیمت ۱: " + compactMoney(item.optDouble("price1", item.optDouble("price", 0)))); p1.setTextSize(8.5f); p1.setOnClickListener(v -> { price.setText(String.valueOf(item.optDouble("price1", item.optDouble("price", 0)))); try { item.put("priceTier", "1"); } catch (Exception ignored) { } });
        Button p2 = secondaryButton("قیمت ۲: " + compactMoney(item.optDouble("price2", 0))); p2.setTextSize(8.5f); p2.setOnClickListener(v -> { price.setText(String.valueOf(item.optDouble("price2", item.optDouble("price", 0)))); try { item.put("priceTier", "2"); } catch (Exception ignored) { } });
        priceRow.addView(p1, weightedButtonLp()); priceRow.addView(p2, weightedButtonLp()); LinearLayout.LayoutParams prp = new LinearLayout.LayoutParams(-1, -2); prp.setMargins(0, dp(7), 0, 0); box.addView(priceRow, prp);
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(-1, dp(76)); np.setMargins(0, dp(7), 0, 0); box.addView(note, np);
        AlertDialog dlg = new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن", null).setPositiveButton("ذخیره", null).create();
        dlg.setOnShowListener(d -> { styleMeelanoDialog(dlg, navAccent("cart")); Button ok = dlg.getButton(AlertDialog.BUTTON_POSITIVE); if (ok != null) ok.setOnClickListener(v -> { try { item.put("qty", Math.max(0.001, parseNumber(qty.getText().toString(), item.optDouble("qty", 1)))); item.put("price", Math.max(0, parseNumber(price.getText().toString(), item.optDouble("price", 0)))); item.put("lineDiscount", Math.max(0, parseNumber(discount.getText().toString(), 0))); item.put("note", note.getText().toString().trim()); item.put("amount", cartItemNet(item)); visitorCartItems.put(index, item); } catch (Exception ignored) { } dlg.dismiss(); renderCartPage(); }); });
        dlg.show();
    }

    private void addCartTermsCard() {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(INFO, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("شرایط فروش و تحویل", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        HorizontalScrollView hs = new HorizontalScrollView(this); styleHorizontalScroll(hs); LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        for (String opt : new String[]{"نقدی", "کارت‌خوان", "حواله", "چکی", "اعتباری"}) {
            Button b = opt.equals(visitorCartSettlement) ? primaryButton(opt) : secondaryButton(opt); b.setTextSize(9.1f); b.setOnClickListener(v -> { syncCartFormInputs(); visitorCartSettlement = opt; renderCartPage(); });
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(dp(92), dp(39)); bp.setMargins(dp(3), 0, dp(3), dp(8)); row.addView(b, bp);
        }
        hs.addView(row, new FrameLayout.LayoutParams(-2, -2)); c.addView(hs, new LinearLayout.LayoutParams(-1, -2));
        cartDeliveryInput = input("تاریخ تحویل", visitorCartDeliveryDate, false);
        cartAddressInput = input("آدرس تحویل", stringOr(visitorCartAddress, visitorCartCustomer == null ? "" : visitorCartCustomer.optString("address", "")), false);
        cartPaymentRefInput = input("شماره پیگیری/چک/حواله", visitorCartPaymentRef, false);
        cartDiscountInput = input("تخفیف کلی", String.valueOf(visitorCartGlobalDiscount), false); cartDiscountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        cartTaxInput = input("درصد مالیات/ارزش افزوده", String.valueOf(visitorCartTaxPercent), false); cartTaxInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        for (EditText e : new EditText[]{cartDeliveryInput, cartAddressInput, cartPaymentRefInput, cartDiscountInput, cartTaxInput}) { LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(-1, dp(48)); ep.setMargins(0, dp(7), 0, 0); c.addView(e, ep); }
        LinearLayout summary = new LinearLayout(this); summary.setOrientation(LinearLayout.HORIZONTAL);
        summary.addView(customerMiniMetric("خالص", money(cartNetBeforeTax()), SUCCESS), weightedMiniLp());
        summary.addView(customerMiniMetric("مالیات", money(cartTaxAmount()), WARNING), weightedMiniLp());
        summary.addView(customerMiniMetric("نهایی", money(cartTotal()), GOLD), weightedMiniLp());
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2); sp.setMargins(0, dp(8), 0, 0); c.addView(summary, sp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void syncCartFormInputs() {
        try { if (cartNotesInput != null) visitorCartNotes = cartNotesInput.getText().toString(); } catch (Exception ignored) { }
        try { if (cartDiscountInput != null) visitorCartGlobalDiscount = Math.max(0, parseNumber(cartDiscountInput.getText().toString(), visitorCartGlobalDiscount)); } catch (Exception ignored) { }
        try { if (cartTaxInput != null) visitorCartTaxPercent = Math.max(0, parseNumber(cartTaxInput.getText().toString(), visitorCartTaxPercent)); } catch (Exception ignored) { }
        try { if (cartDeliveryInput != null) visitorCartDeliveryDate = cartDeliveryInput.getText().toString().trim(); } catch (Exception ignored) { }
        try { if (cartAddressInput != null) visitorCartAddress = cartAddressInput.getText().toString().trim(); } catch (Exception ignored) { }
        try { if (cartPaymentRefInput != null) visitorCartPaymentRef = cartPaymentRefInput.getText().toString().trim(); } catch (Exception ignored) { }
    }

    private void addCartNotesAndActions() {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(INFO, 20), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("توضیحات، امضا و ثبت", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        cartNotesInput = input("توضیحات مشتری یا اقلام انتخابی", visitorCartNotes, false); cartNotesInput.setSingleLine(false); cartNotesInput.setMinLines(3);
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(-1, dp(88)); np.setMargins(0, dp(8), 0, dp(8)); c.addView(cartNotesInput, np);
        LinearLayout signRow = new LinearLayout(this); signRow.setOrientation(LinearLayout.HORIZONTAL);
        Button sign = secondaryButton(visitorCartSignature.isEmpty() ? "ثبت امضای مشتری" : "امضا ثبت شد ✓"); sign.setOnClickListener(v -> { syncCartFormInputs(); if (ensurePermission("cart_signature", "امضای مشتری")) showSignatureDialog(); });
        Button pdf = secondaryButton("PDF/اشتراک"); pdf.setOnClickListener(v -> { syncCartFormInputs(); generateCurrentCartPdfAndShare(false); });
        signRow.addView(sign, weightedButtonLp()); signRow.addView(pdf, weightedButtonLp()); c.addView(signRow, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button draft = secondaryButton("ذخیره پیش‌نویس"); draft.setOnClickListener(v -> { syncCartFormInputs(); if (ensurePermission("cart_draft", "پیش‌نویس")) saveCartDraftOnly(); });
        Button send = primaryButton("پیش‌نمایش و ارسال"); send.setOnClickListener(v -> { syncCartFormInputs(); if (ensurePermission("cart_submit", "ارسال پیش‌فاکتور")) showPrefactorPreviewDialog(); });
        row.addView(draft, weightedButtonLp()); row.addView(send, weightedButtonLp()); LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(9), 0, 0); c.addView(row, rp);
        Button restore = secondaryButton("پیش‌نویس‌ها و بازیابی"); restore.setOnClickListener(v -> restoreCartDraft()); LinearLayout.LayoutParams rp2 = new LinearLayout.LayoutParams(-1, dp(44)); rp2.setMargins(0, dp(8), 0, 0); c.addView(restore, rp2);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addCartDraftsAndOfflineCard() {
        LinearLayout c = card(); c.setBackground(gradient(new int[]{alpha(WARNING, 16), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("پیش‌نویس‌ها و صف ارسال", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("پیش‌نویس‌های چندتایی و سفارش‌های ارسال‌نشده در گوشی نگهداری می‌شوند تا در اتصال بعدی ارسال شوند.", 10.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button drafts = secondaryButton("پیش‌نویس‌ها: " + formatNumber(localDrafts().length())); drafts.setOnClickListener(v -> showSavedDraftsDialog());
        Button queue = offlineQueue().length() > 0 ? primaryButton("ارسال صف: " + formatNumber(offlineQueue().length())) : secondaryButton("صف خالی"); queue.setOnClickListener(v -> trySendOfflineQueue());
        row.addView(drafts, weightedButtonLp()); row.addView(queue, weightedButtonLp()); LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(9), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void showSignatureDialog() {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(10), dp(8), dp(10), dp(6));
        box.addView(text("امضای مشتری", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        SignaturePadView pad = new SignaturePadView(this);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(220)); pp.setMargins(0, dp(8), 0, dp(8)); box.addView(pad, pp);
        Button clear = secondaryButton("پاک کردن امضا"); clear.setOnClickListener(v -> pad.clear()); box.addView(clear, new LinearLayout.LayoutParams(-1, dp(42)));
        AlertDialog dlg = new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن", null).setPositiveButton("ثبت امضا", null).create();
        dlg.setOnShowListener(d -> { styleMeelanoDialog(dlg, navAccent("cart")); Button ok = dlg.getButton(AlertDialog.BUTTON_POSITIVE); if (ok != null) ok.setOnClickListener(v -> { visitorCartSignature = pad.exportPngBase64(); Toast.makeText(this, "امضا ثبت شد.", Toast.LENGTH_SHORT).show(); dlg.dismiss(); renderCartPage(); }); });
        dlg.show();
    }

    private void showCartCustomerPicker(String search) {
        runDb(() -> queryCartCustomers(search), new DbCallback() {
            @Override public void ok(String body) { try { renderCartCustomerPicker(search, new JSONArray(body)); } catch (Exception e) { showPageError("انتخاب مشتری", e, () -> renderCartPage()); } }
            @Override public void fail(Exception e) { showPageError("انتخاب مشتری", e, () -> renderCartPage()); }
        });
    }

    private void renderCartCustomerPicker(String search, JSONArray rows) {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12), dp(10), dp(12), dp(6));
        box.addView(text("انتخاب مشتری", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        EditText q = input("جستجوی نام/کد/موبایل", search, false); box.addView(q, new LinearLayout.LayoutParams(-1, dp(48)));
        AlertDialog[] dlg = new AlertDialog[1];
        Button searchBtn = primaryButton("جستجو"); searchBtn.setOnClickListener(v -> { if (dlg[0] != null) dlg[0].dismiss(); showCartCustomerPicker(q.getText().toString().trim()); }); box.addView(searchBtn, new LinearLayout.LayoutParams(-1, dp(42)));
        ScrollView scroll = new ScrollView(this); styleVerticalScroll(scroll); LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL);
        if (rows == null || rows.length() == 0) addEmptyTo(list, "مشتری مطابق جستجو پیدا نشد.");
        for (int i = 0; rows != null && i < rows.length(); i++) {
            JSONObject r = rows.optJSONObject(i); if (r == null) continue;
            Button b = secondaryButton(r.optString("name", "مشتری") + " • مانده " + compactMoney(r.opt("balance")));
            b.setTextSize(9.6f); b.setOnClickListener(v -> { visitorCartCustomer = r; if (visitorCartAddress.trim().isEmpty()) visitorCartAddress = r.optString("address", ""); if (dlg[0] != null) dlg[0].dismiss(); renderCartPage(); });
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(44)); bp.setMargins(0, dp(6), 0, 0); list.addView(b, bp);
        }
        scroll.addView(list, new ScrollView.LayoutParams(-1, -2)); box.addView(scroll, new LinearLayout.LayoutParams(-1, dp(330)));
        dlg[0] = new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن", null).create();
        dlg[0].setOnShowListener(d -> styleMeelanoDialog(dlg[0], navAccent("customers")));
        dlg[0].show();
    }

    private String queryCartCustomers(String search) throws Exception {
        try (Connection c = openConnection()) {
            Set<String> cols = columns(c, "CUSTOMERS"); Set<String> sail = columns(c, "sailfact");
            String shmo = resolve(cols, "SHMO", "shmo", "CustomerCode"); String name = resolve(cols, "MONAME", "Name", "CusName"); String balance = resolve(cols, "man", "Balance", "Mandeh", "mande"); String phone = resolve(cols, "cell", "mobile", "Mobile", "tell1", "tel1");
            String credit = resolveFlexible(cols, "credit", "Credit", "credit_limit", "Limit", "etebar", "Etebar", "سقف_اعتبار");
            String address = resolveFlexible(cols, "address", "Address", "addr", "Adress", "آدرس");
            String blocked = resolveFlexible(cols, "blocked", "blacklist", "lock", "is_blocked", "active", "Active");
            if (shmo == null) return "[]";
            String sailShmo = resolve(sail, "shmo", "SHMO"); String amount = resolve(sail, "all", "amount"); String saleDate = resolve(sail, "date", "DATE", "t_date");
            String label = name == null ? "TRY_CONVERT(nvarchar(220),c.[" + shmo + "])" : "TRY_CONVERT(nvarchar(220),c.[" + name + "])";
            List<String> where = new ArrayList<>(); List<Object> params = new ArrayList<>();
            String scope = customerScopeCondition(cols, "c", params); if (!scope.isEmpty()) where.add(scope);
            if (search != null && !search.trim().isEmpty()) { String q = search.trim(); List<String> parts = new ArrayList<>(); for (String col : new String[]{shmo, name, phone}) if (col != null) { parts.add("TRY_CONVERT(nvarchar(400),c.[" + col + "]) LIKE N'%' + ? + N'%'"); params.add(q); } if (!parts.isEmpty()) where.add("(" + join(parts, " OR ") + ")"); }
            String salesApply = sailShmo == null ? "OUTER APPLY (SELECT CAST(0 AS bigint) cnt, CAST(0 AS decimal(19,2)) total, CAST(N'' AS nvarchar(30)) lastSale) sf " : "OUTER APPLY (SELECT COUNT_BIG(1) cnt, " + (amount == null ? "CAST(0 AS decimal(19,2))" : "ISNULL(SUM(TRY_CONVERT(decimal(19,2),s.[" + amount + "])),0)") + " total, " + (saleDate == null ? "CAST(N'' AS nvarchar(30))" : "ISNULL(MAX(TRY_CONVERT(nvarchar(30),s.[" + saleDate + "])),N'')") + " lastSale FROM dbo.sailfact s WHERE TRY_CONVERT(nvarchar(100),s.[" + sailShmo + "])=TRY_CONVERT(nvarchar(100),c.[" + shmo + "])) sf ";
            String sql = "SELECT TOP (40) TRY_CONVERT(nvarchar(100),c.[" + shmo + "]), " + label + ", " + (balance == null ? "CAST(0 AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),c.[" + balance + "])") + ", " + (phone == null ? "CAST(NULL AS nvarchar(100))" : "TRY_CONVERT(nvarchar(100),c.[" + phone + "])") + ", sf.cnt, sf.total, sf.lastSale, " + (credit == null ? "CAST(0 AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),c.[" + credit + "])") + ", " + (address == null ? "CAST(NULL AS nvarchar(500))" : "TRY_CONVERT(nvarchar(500),c.[" + address + "])") + ", " + (blocked == null ? "CAST(NULL AS nvarchar(40))" : "TRY_CONVERT(nvarchar(40),c.[" + blocked + "])") + " FROM dbo.CUSTOMERS c " + salesApply + (where.isEmpty() ? "" : " WHERE " + join(where, " AND ")) + " ORDER BY " + label;
            JSONArray arr = new JSONArray(); try (PreparedStatement ps = c.prepareStatement(sql)) { setParams(ps, params); try (ResultSet r = ps.executeQuery()) { while (r.next()) { JSONObject o = new JSONObject(); o.put("code", stringOr(r.getString(1), "")); o.put("name", stringOr(r.getString(2), "مشتری")); o.put("balance", r.getDouble(3)); o.put("phone", stringOr(r.getString(4), "")); o.put("invoiceCount", r.getLong(5)); o.put("totalSales", r.getDouble(6)); o.put("lastSale", stringOr(r.getString(7), "")); o.put("creditLimit", r.getDouble(8)); o.put("address", stringOr(r.getString(9), "")); String bv = stringOr(r.getString(10), ""); o.put("blockedRaw", bv); o.put("blocked", bv.equals("0") || bv.equalsIgnoreCase("blocked") || bv.contains("مسدود")); arr.put(o); } } }
            return arr.toString();
        }
    }

    private JSONObject currentCartSnapshot(String status) {
        syncCartFormInputs();
        JSONObject d = new JSONObject();
        try {
            if (visitorCartDraftId == null || visitorCartDraftId.trim().isEmpty()) visitorCartDraftId = "MEELANO-" + System.currentTimeMillis();
            d.put("clientUuid", visitorCartDraftId);
            d.put("items", new JSONArray(visitorCartItems.toString()));
            d.put("customer", visitorCartCustomer == null ? JSONObject.NULL : new JSONObject(visitorCartCustomer.toString()));
            d.put("customerCode", visitorCartCustomer == null ? "" : visitorCartCustomer.optString("code", ""));
            d.put("customerName", visitorCartCustomer == null ? "" : visitorCartCustomer.optString("name", ""));
            d.put("notes", visitorCartNotes); d.put("signature", visitorCartSignature);
            d.put("settlement", visitorCartSettlement); d.put("deliveryDate", visitorCartDeliveryDate); d.put("address", visitorCartAddress); d.put("paymentRef", visitorCartPaymentRef);
            d.put("subtotal", cartSubtotal()); d.put("lineDiscount", cartLineDiscounts()); d.put("globalDiscount", visitorCartGlobalDiscount); d.put("taxPercent", visitorCartTaxPercent); d.put("taxAmount", cartTaxAmount()); d.put("grandTotal", cartTotal());
            d.put("status", status == null ? finalCartStatus() : status); d.put("approvalReason", cartApprovalReason()); d.put("createdLocal", nowText());
            d.put("visitor", currentAccountName()); d.put("visitorId", currentVisitorScopeId() == null ? "" : String.valueOf(currentVisitorScopeId()));
        } catch (Exception ignored) { }
        return d;
    }

    private void saveCartDraftOnly() {
        try {
            JSONObject snap = currentCartSnapshot("draft");
            saveLocalDraftSnapshot(snap);
            if (prefs != null) prefs.edit().putString(KEY_CART_DRAFT, snap.toString()).apply();
            Toast.makeText(this, "پیش‌نویس محلی ذخیره شد.", Toast.LENGTH_SHORT).show();
            if (visitorCartCustomer != null && visitorCartItems.length() > 0) submitCartSnapshot(snap, "draft", false);
            else renderCartPage();
        } catch (Exception ex) { Toast.makeText(this, "ذخیره پیش‌نویس ممکن نشد.", Toast.LENGTH_SHORT).show(); }
    }

    private JSONArray localDrafts() { try { String raw = prefs == null ? "[]" : prefs.getString(KEY_CART_DRAFTS, "[]"); return new JSONArray(raw == null || raw.trim().isEmpty() ? "[]" : raw); } catch (Exception ignored) { return new JSONArray(); } }
    private void writeLocalDrafts(JSONArray a) { if (prefs != null) prefs.edit().putString(KEY_CART_DRAFTS, a == null ? "[]" : a.toString()).apply(); }

    private String saveLocalDraftSnapshot(JSONObject snap) {
        String id = snap == null ? "" : snap.optString("clientUuid", "");
        if (id.trim().isEmpty()) id = "MEELANO-" + System.currentTimeMillis();
        try { snap.put("clientUuid", id); snap.put("savedAt", nowText()); snap.put("status", "draft"); } catch (Exception ignored) { }
        JSONArray old = localDrafts(); JSONArray out = new JSONArray(); boolean done = false;
        for (int i = 0; i < old.length(); i++) { JSONObject o = old.optJSONObject(i); if (o == null) continue; if (id.equals(o.optString("clientUuid", ""))) { out.put(snap); done = true; } else out.put(o); }
        if (!done) out.put(snap);
        JSONArray limited = new JSONArray(); for (int i = Math.max(0, out.length() - 50); i < out.length(); i++) limited.put(out.optJSONObject(i));
        writeLocalDrafts(limited); return id;
    }

    private void removeLocalDraft(String id) {
        if (id == null || id.trim().isEmpty()) return;
        JSONArray old = localDrafts(); JSONArray out = new JSONArray();
        for (int i = 0; i < old.length(); i++) { JSONObject o = old.optJSONObject(i); if (o != null && !id.equals(o.optString("clientUuid", ""))) out.put(o); }
        writeLocalDrafts(out);
    }

    private void restoreCartDraft() { showSavedDraftsDialog(); }

    private void showSavedDraftsDialog() {
        JSONArray rows = localDrafts();
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12), dp(10), dp(12), dp(6));
        box.addView(text("پیش‌نویس‌های ذخیره‌شده", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        ScrollView sc = new ScrollView(this); styleVerticalScroll(sc); LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL);
        AlertDialog[] dlg = new AlertDialog[1];
        if (rows.length() == 0) addEmptyTo(list, "پیش‌نویس ذخیره‌شده وجود ندارد.");
        for (int i = rows.length() - 1; i >= 0; i--) {
            JSONObject r = rows.optJSONObject(i); if (r == null) continue;
            Button b = secondaryButton(r.optString("customerName", "بدون مشتری") + " • " + compactMoney(r.optDouble("grandTotal", 0)) + " • " + r.optString("savedAt", ""));
            b.setTextSize(9.2f); b.setOnClickListener(v -> { restoreCartSnapshot(r); if (dlg[0] != null) dlg[0].dismiss(); renderCartPage(); });
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(46)); bp.setMargins(0, dp(6), 0, 0); list.addView(b, bp);
        }
        sc.addView(list, new ScrollView.LayoutParams(-1, -2)); box.addView(sc, new LinearLayout.LayoutParams(-1, dp(360)));
        dlg[0] = new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن", null).create(); dlg[0].setOnShowListener(d -> styleMeelanoDialog(dlg[0], navAccent("cart"))); dlg[0].show();
    }

    private void restoreCartSnapshot(JSONObject d) {
        try {
            visitorCartDraftId = d.optString("clientUuid", "");
            visitorCartItems = d.optJSONArray("items") == null ? new JSONArray() : new JSONArray(d.optJSONArray("items").toString());
            visitorCartCustomer = d.optJSONObject("customer"); visitorCartNotes = d.optString("notes", ""); visitorCartSignature = d.optString("signature", "");
            visitorCartSettlement = stringOr(d.optString("settlement", ""), "اعتباری"); visitorCartDeliveryDate = d.optString("deliveryDate", ""); visitorCartAddress = d.optString("address", ""); visitorCartPaymentRef = d.optString("paymentRef", "");
            visitorCartGlobalDiscount = d.optDouble("globalDiscount", 0); visitorCartTaxPercent = d.optDouble("taxPercent", 0);
        } catch (Exception ignored) { }
    }

    private JSONArray offlineQueue() { try { String raw = prefs == null ? "[]" : prefs.getString(KEY_OFFLINE_PREF_QUEUE, "[]"); return new JSONArray(raw == null || raw.trim().isEmpty() ? "[]" : raw); } catch (Exception ignored) { return new JSONArray(); } }
    private void writeOfflineQueue(JSONArray a) { if (prefs != null) prefs.edit().putString(KEY_OFFLINE_PREF_QUEUE, a == null ? "[]" : a.toString()).apply(); }

    private void saveOfflinePrefactor(JSONObject snap, String reason) {
        try { snap.put("offlineReason", reason == null ? "" : reason); snap.put("queuedAt", nowText()); } catch (Exception ignored) { }
        JSONArray q = offlineQueue(); q.put(snap); writeOfflineQueue(q);
        Toast.makeText(this, "اتصال کامل نشد؛ پیش‌فاکتور در صف ارسال آفلاین ذخیره شد.", Toast.LENGTH_LONG).show();
    }

    private void trySendOfflineQueue() {
        JSONArray q = offlineQueue();
        if (q.length() == 0) { Toast.makeText(this, "صف ارسال آفلاین خالی است.", Toast.LENGTH_SHORT).show(); return; }
        runDb(() -> {
            int ok = 0;
            for (int i = 0; i < q.length(); i++) { JSONObject snap = q.optJSONObject(i); if (snap == null) continue; insertPrefactorSnapshot(snap, snap.optString("status", "sent")); ok++; }
            return "ارسال شد: " + formatNumber(ok);
        }, new DbCallback() {
            @Override public void ok(String body) { writeOfflineQueue(new JSONArray()); Toast.makeText(MainActivity.this, body, Toast.LENGTH_LONG).show(); if ("cart".equals(activePage)) renderCartPage(); }
            @Override public void fail(Exception e) { showPageError("ارسال صف آفلاین", e, () -> renderCartPage()); }
        });
    }

    private void showPrefactorPreviewDialog() {
        if (visitorCartItems.length() == 0) { Toast.makeText(this, "سبد خالی است.", Toast.LENGTH_SHORT).show(); return; }
        if (visitorCartCustomer == null) { Toast.makeText(this, "ابتدا مشتری را انتخاب کنید.", Toast.LENGTH_LONG).show(); return; }
        JSONObject snap = currentCartSnapshot(finalCartStatus());
        runDb(() -> preflightCartSnapshot(snap), new DbCallback() {
            @Override public void ok(String body) { try { showPrefactorPreviewDialogWithCheck(snap, new JSONObject(body), false); } catch (Exception ex) { showPrefactorPreviewDialogWithCheck(snap, new JSONObject(), false); } }
            @Override public void fail(Exception e) { JSONObject warn = new JSONObject(); try { warn.put("offline", true); warn.put("message", readableError(e)); } catch (Exception ignored) { } showPrefactorPreviewDialogWithCheck(snap, warn, true); }
        });
    }

    private void showPrefactorPreviewDialogWithCheck(JSONObject snap, JSONObject check, boolean offline) {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12), dp(10), dp(12), dp(6));
        box.addView(text("پیش‌نمایش نهایی پیش‌فاکتور", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        String status = snap.optString("status", "sent");
        box.addView(text("مشتری: " + snap.optString("customerName", "—") + "\nنوع تسویه: " + snap.optString("settlement", "—") + " • تاریخ تحویل: " + stringOr(snap.optString("deliveryDate"), "—") + "\nوضعیت ارسال: " + prefactorStatusFa(status), 11.3f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        box.addView(text("جمع خام: " + money(snap.optDouble("subtotal",0)) + "\nتخفیف: " + money(snap.optDouble("lineDiscount",0)+snap.optDouble("globalDiscount",0)) + "\nمالیات: " + money(snap.optDouble("taxAmount",0)) + "\nقابل پرداخت: " + money(snap.optDouble("grandTotal",0)), 11.0f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        String reason = snap.optString("approvalReason", ""); if (!reason.trim().isEmpty()) box.addView(text("نیازمند تایید: " + reason, 10.5f, WARNING, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        JSONArray warnings = check.optJSONArray("warnings");
        if (offline) box.addView(text("کنترل آنلاین موجودی انجام نشد؛ می‌توانید در صف آفلاین نگه دارید یا دوباره تلاش کنید.", 10.4f, DANGER, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        else if (warnings != null && warnings.length() > 0) for (int i=0;i<warnings.length();i++) box.addView(text("• " + warnings.optString(i), 10.2f, check.optBoolean("blocked", false)?DANGER:WARNING, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        else box.addView(text("کنترل نهایی موجودی و قیمت با موفقیت انجام شد.", 10.4f, SUCCESS, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        AlertDialog dlg = new AlertDialog.Builder(this).setView(box).setNegativeButton("بازگشت", null).setPositiveButton(offline ? "ذخیره در صف" : "تایید و ارسال", null).create();
        dlg.setOnShowListener(d -> { styleMeelanoDialog(dlg, navAccent("cart")); Button ok = dlg.getButton(AlertDialog.BUTTON_POSITIVE); if (ok != null) ok.setOnClickListener(v -> { dlg.dismiss(); if (offline) { saveOfflinePrefactor(snap, check.optString("message", "offline")); renderCartPage(); } else submitCartSnapshot(snap, snap.optString("status", "sent"), true); }); });
        dlg.show();
    }

    private String preflightCartSnapshot(JSONObject snap) throws Exception {
        try (Connection c = openConnection()) {
            ensurePrefactorTables(c);
            return checkCartSnapshotAgainstInventory(c, snap.optJSONArray("items"), false).toString();
        }
    }

    private void submitCartPrefactor(String status) { submitCartSnapshot(currentCartSnapshot(status == null ? finalCartStatus() : status), status == null ? finalCartStatus() : status, true); }

    private void submitCartSnapshot(JSONObject snap, String status, boolean clearOnSuccess) {
        if (snap == null || snap.optJSONArray("items") == null || snap.optJSONArray("items").length() == 0) { Toast.makeText(this, "سبد خالی است.", Toast.LENGTH_SHORT).show(); return; }
        if (snap.optString("customerCode", "").trim().isEmpty()) { Toast.makeText(this, "ابتدا مشتری را انتخاب کنید.", Toast.LENGTH_LONG).show(); return; }
        runDb(() -> insertPrefactorSnapshot(snap, status), new DbCallback() {
            @Override public void ok(String body) { Toast.makeText(MainActivity.this, "draft".equals(status) ? "پیش‌نویس ذخیره شد." : "پیش‌فاکتور ثبت شد. شماره: " + body, Toast.LENGTH_LONG).show(); if (clearOnSuccess && !"draft".equals(status)) { removeLocalDraft(snap.optString("clientUuid", "")); resetCartState(); } renderCartPage(); }
            @Override public void fail(Exception e) { if (!"draft".equals(status)) saveOfflinePrefactor(snap, shortError(e)); else Toast.makeText(MainActivity.this, "پیش‌نویس فقط محلی ماند.", Toast.LENGTH_LONG).show(); renderCartPage(); }
        });
    }

    private void resetCartState() {
        visitorCartItems = new JSONArray(); visitorCartCustomer = null; visitorCartNotes = ""; visitorCartSignature = ""; visitorCartDraftId = ""; visitorCartSettlement = "اعتباری"; visitorCartDeliveryDate = ""; visitorCartAddress = ""; visitorCartPaymentRef = ""; visitorCartGlobalDiscount = 0; visitorCartTaxPercent = 0;
    }

    private String insertPrefactor(String status) throws Exception { return insertPrefactorSnapshot(currentCartSnapshot(status), status); }

    private String insertPrefactorSnapshot(JSONObject snap, String status) throws Exception {
        try (Connection c = openConnection()) {
            ensurePrefactorTables(c);
            String uuid = snap.optString("clientUuid", "");
            if (!uuid.trim().isEmpty()) try (PreparedStatement find = c.prepareStatement("SELECT TOP (1) id FROM dbo.meelano_prefactors WHERE client_uuid=?")) { find.setString(1, uuid); try (ResultSet r = find.executeQuery()) { if (r.next()) return String.valueOf(r.getLong(1)); } } catch (Exception ignored) { }
            c.setAutoCommit(false);
            try {
                if (!"draft".equals(status)) checkCartSnapshotAgainstInventory(c, snap.optJSONArray("items"), true);
                long id;
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO dbo.meelano_prefactors(client_uuid,visitor_username,visitor_id,customer_code,customer_name,notes,signature_data,total_amount,status,created_at,updated_at,delivery_date,delivery_address,settlement_type,payment_ref,subtotal_amount,global_discount,tax_percent,tax_amount,grand_total,approval_reason) VALUES(?,?,?,?,?,?,?,?,?,SYSDATETIME(),SYSDATETIME(),?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, uuid); ps.setString(2, currentAccountName()); ps.setString(3, snap.optString("visitorId", "")); ps.setString(4, snap.optString("customerCode", "")); ps.setString(5, snap.optString("customerName", "")); ps.setString(6, snap.optString("notes", "")); ps.setString(7, snap.optString("signature", "")); ps.setDouble(8, snap.optDouble("grandTotal", 0)); ps.setString(9, status == null ? snap.optString("status", "sent") : status);
                    ps.setString(10, snap.optString("deliveryDate", "")); ps.setString(11, snap.optString("address", "")); ps.setString(12, snap.optString("settlement", "")); ps.setString(13, snap.optString("paymentRef", "")); ps.setDouble(14, snap.optDouble("subtotal", 0)); ps.setDouble(15, snap.optDouble("globalDiscount", 0)); ps.setDouble(16, snap.optDouble("taxPercent", 0)); ps.setDouble(17, snap.optDouble("taxAmount", 0)); ps.setDouble(18, snap.optDouble("grandTotal", 0)); ps.setString(19, snap.optString("approvalReason", "")); ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) { id = keys.next() ? keys.getLong(1) : 0; }
                }
                if (id <= 0) { try (PreparedStatement ps = c.prepareStatement("SELECT ISNULL(MAX(id),0) FROM dbo.meelano_prefactors WHERE visitor_username=?")) { ps.setString(1, currentAccountName()); try (ResultSet r = ps.executeQuery()) { if (r.next()) id = r.getLong(1); } } }
                JSONArray items = snap.optJSONArray("items");
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO dbo.meelano_prefactor_items(prefactor_id,product_code,product_name,unit,qty,price,amount,stock_snapshot,pack_count,price_tier,line_discount,line_note) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)")) {
                    for (int i = 0; items != null && i < items.length(); i++) { JSONObject it = items.optJSONObject(i); if (it == null) continue; ps.setLong(1, id); ps.setString(2, it.optString("code", "")); ps.setString(3, it.optString("name", "")); ps.setString(4, it.optString("unit", "")); ps.setDouble(5, it.optDouble("qty", 0)); ps.setDouble(6, it.optDouble("price", 0)); ps.setDouble(7, cartItemNet(it)); ps.setDouble(8, it.optDouble("stock", 0)); ps.setDouble(9, it.optDouble("pack", 1)); ps.setString(10, it.optString("priceTier", "1")); ps.setDouble(11, it.optDouble("lineDiscount", 0)); ps.setString(12, it.optString("note", "")); ps.addBatch(); }
                    ps.executeBatch();
                }
                c.commit();
                return String.valueOf(id);
            } catch (Exception ex) { try { c.rollback(); } catch (Exception ignored) { } throw ex; }
            finally { try { c.setAutoCommit(true); } catch (Exception ignored) { } }
        }
    }

    private JSONObject checkCartSnapshotAgainstInventory(Connection c, JSONArray items, boolean strict) throws Exception {
        JSONObject out = new JSONObject(); JSONArray warnings = new JSONArray(); boolean blocked = false;
        Set<String> cols = columns(c, "inventory"); String shka = resolve(cols, "shka", "SHKA"); if (shka == null) { out.put("warnings", warnings); return out; }
        String stock = resolve(cols, "Mojoodi", "mojoodi", "Stock", "Qty", "tedad", "Tedad", "inventorycount");
        String price1 = resolveFlexible(cols, "FinalSalePrice", "SalePrice", "Price", "forosh1", "price1", "Price1", "قیمت_فروش1");
        String price2 = resolveFlexible(cols, "FinalSalePrice2", "SalePrice2", "Price2", "forosh2", "price_2", "قیمت_فروش2", "gheymat2");
        String sql = "SELECT " + (stock == null ? "CAST(NULL AS decimal(19,3))" : "TRY_CONVERT(decimal(19,3),["+stock+"])") + "," + (price1 == null ? "CAST(NULL AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),["+price1+"])") + "," + (price2 == null ? "CAST(NULL AS decimal(19,2))" : "TRY_CONVERT(decimal(19,2),["+price2+"])") + " FROM dbo.inventory WHERE TRY_CONVERT(nvarchar(100),["+shka+"])=?";
        for (int i=0; items!=null && i<items.length(); i++) {
            JSONObject it=items.optJSONObject(i); if (it==null) continue;
            try (PreparedStatement ps=c.prepareStatement(sql)) { ps.setString(1,it.optString("code","")); try(ResultSet r=ps.executeQuery()) { if(!r.next()){ warnings.put("کالا پیدا نشد: "+it.optString("name","")); blocked=true; continue; } double st=r.getDouble(1); double p1=r.getDouble(2); double p2=r.getDouble(3); if(stock!=null && st<it.optDouble("qty",0)){ warnings.put("موجودی کافی نیست: "+it.optString("name","")+" / موجودی "+formatNumber(st)); blocked=true; } double dbPrice="2".equals(it.optString("priceTier","1"))&&p2>0?p2:p1; if(dbPrice>0 && Math.abs(dbPrice-it.optDouble("price",0))>1){ warnings.put("قیمت کالا تغییر کرده: "+it.optString("name","")); if(strict) blocked=true; } } }
        }
        out.put("warnings", warnings); out.put("blocked", blocked); out.put("ok", !blocked);
        if (strict && blocked) throw new DbException(warnings.length()>0 ? warnings.optString(0) : "کنترل موجودی/قیمت تایید نشد.");
        return out;
    }

    private void ensurePrefactorTables(Connection c) throws Exception {
        try (Statement st = c.createStatement()) {
            st.execute("IF OBJECT_ID(N'dbo.meelano_prefactors',N'U') IS NULL CREATE TABLE dbo.meelano_prefactors (id bigint IDENTITY(1,1) NOT NULL PRIMARY KEY, client_uuid nvarchar(80) NULL, visitor_username nvarchar(160) NULL, visitor_id nvarchar(80) NULL, customer_code nvarchar(100) NULL, customer_name nvarchar(250) NULL, notes nvarchar(max) NULL, signature_data nvarchar(max) NULL, total_amount decimal(19,2) NULL, subtotal_amount decimal(19,2) NULL, global_discount decimal(19,2) NULL, tax_percent decimal(9,3) NULL, tax_amount decimal(19,2) NULL, grand_total decimal(19,2) NULL, status nvarchar(30) NOT NULL DEFAULT N'draft', approval_reason nvarchar(700) NULL, delivery_date nvarchar(40) NULL, delivery_address nvarchar(700) NULL, settlement_type nvarchar(80) NULL, payment_ref nvarchar(160) NULL, created_at datetime2 NOT NULL DEFAULT SYSDATETIME(), updated_at datetime2 NULL)");
            st.execute("IF OBJECT_ID(N'dbo.meelano_prefactor_items',N'U') IS NULL CREATE TABLE dbo.meelano_prefactor_items (id bigint IDENTITY(1,1) NOT NULL PRIMARY KEY, prefactor_id bigint NOT NULL, product_code nvarchar(100) NULL, product_name nvarchar(500) NULL, unit nvarchar(80) NULL, qty decimal(19,4) NULL, price decimal(19,2) NULL, amount decimal(19,2) NULL, stock_snapshot decimal(19,4) NULL, pack_count decimal(19,4) NULL, price_tier nvarchar(10) NULL, line_discount decimal(19,2) NULL, line_note nvarchar(700) NULL)");
            for (String sql : new String[]{
                    "IF COL_LENGTH('dbo.meelano_prefactors','client_uuid') IS NULL ALTER TABLE dbo.meelano_prefactors ADD client_uuid nvarchar(80) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','delivery_date') IS NULL ALTER TABLE dbo.meelano_prefactors ADD delivery_date nvarchar(40) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','delivery_address') IS NULL ALTER TABLE dbo.meelano_prefactors ADD delivery_address nvarchar(700) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','settlement_type') IS NULL ALTER TABLE dbo.meelano_prefactors ADD settlement_type nvarchar(80) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','payment_ref') IS NULL ALTER TABLE dbo.meelano_prefactors ADD payment_ref nvarchar(160) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','subtotal_amount') IS NULL ALTER TABLE dbo.meelano_prefactors ADD subtotal_amount decimal(19,2) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','global_discount') IS NULL ALTER TABLE dbo.meelano_prefactors ADD global_discount decimal(19,2) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','tax_percent') IS NULL ALTER TABLE dbo.meelano_prefactors ADD tax_percent decimal(9,3) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','tax_amount') IS NULL ALTER TABLE dbo.meelano_prefactors ADD tax_amount decimal(19,2) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','grand_total') IS NULL ALTER TABLE dbo.meelano_prefactors ADD grand_total decimal(19,2) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','approval_reason') IS NULL ALTER TABLE dbo.meelano_prefactors ADD approval_reason nvarchar(700) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactors','updated_at') IS NULL ALTER TABLE dbo.meelano_prefactors ADD updated_at datetime2 NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactor_items','price_tier') IS NULL ALTER TABLE dbo.meelano_prefactor_items ADD price_tier nvarchar(10) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactor_items','line_discount') IS NULL ALTER TABLE dbo.meelano_prefactor_items ADD line_discount decimal(19,2) NULL",
                    "IF COL_LENGTH('dbo.meelano_prefactor_items','line_note') IS NULL ALTER TABLE dbo.meelano_prefactor_items ADD line_note nvarchar(700) NULL"
            }) st.execute(sql);
            st.execute("IF OBJECT_ID(N'dbo.meelano_visit_results',N'U') IS NULL CREATE TABLE dbo.meelano_visit_results (id bigint IDENTITY(1,1) PRIMARY KEY, visitor_username nvarchar(160) NULL, visitor_id nvarchar(80) NULL, customer_code nvarchar(100) NULL, customer_name nvarchar(250) NULL, result nvarchar(120) NULL, notes nvarchar(700) NULL, created_at datetime2 NOT NULL DEFAULT SYSDATETIME())");
            st.execute("IF OBJECT_ID(N'dbo.meelano_day_reports',N'U') IS NULL CREATE TABLE dbo.meelano_day_reports (id bigint IDENTITY(1,1) PRIMARY KEY, visitor_username nvarchar(160) NULL, visitor_id nvarchar(80) NULL, report_text nvarchar(max) NULL, created_at datetime2 NOT NULL DEFAULT SYSDATETIME())");
        }
    }


    private String prefactorStatusFa(String st) {
        if ("draft".equals(st)) return "پیش‌نویس";
        if ("sent".equals(st)) return "ارسال‌شده";
        if ("pending_approval".equals(st)) return "در انتظار تایید";
        if ("approved".equals(st)) return "تایید شده";
        if ("rejected".equals(st)) return "رد شده";
        if ("needs_revision".equals(st)) return "نیازمند اصلاح";
        if ("invoiced".equals(st)) return "تبدیل‌شده به فاکتور";
        if ("canceled".equals(st)) return "لغو شده";
        return stringOr(st, "نامشخص");
    }

    private String prefactorScopeWhere(String alias, List<Object> params) {
        String pfx = alias == null || alias.trim().isEmpty() ? "" : alias + ".";
        String role = session == null ? "" : canonicalAccessRole(session.accessRole);
        if ("admin".equals(role) || "manager".equals(role) || "senior_accountant".equals(role)) return "";
        List<String> parts = new ArrayList<>();
        String user = currentAccountName();
        if (user != null && !user.trim().isEmpty()) { parts.add("LTRIM(RTRIM(" + pfx + "visitor_username))=?"); params.add(user.trim()); }
        Integer vid = currentVisitorScopeId();
        if (vid != null && vid > 0) { parts.add("LTRIM(RTRIM(" + pfx + "visitor_id))=?"); params.add(String.valueOf(vid)); }
        return parts.isEmpty() ? "" : " WHERE (" + join(parts, " OR ") + ")";
    }

    private void loadMyPrefactors() {
        if (!canUsePermission("cart")) { renderLockedSection("cart"); return; }
        content.removeAllViews(); addHero("پیش‌فاکتورهای من", "پیگیری، ویرایش پیش‌نویس، ارسال مجدد، PDF و وضعیت تایید");
        addManualRefreshPanel("cart", "بروزرسانی پیش‌فاکتورها", "نمایش آخرین سفارش‌های ویزیتور", () -> loadMyPrefactors());
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list, new LinearLayout.LayoutParams(-1, -2)); addLoading(list, "در حال دریافت پیش‌فاکتورها…");
        runDb(this::queryMyPrefactorsSql, new DbCallback() { @Override public void ok(String body) { try { renderMyPrefactors(new JSONArray(body)); } catch(Exception e){ showPageError("پیش‌فاکتورهای من", e, () -> loadMyPrefactors()); } } @Override public void fail(Exception e){ showPageError("پیش‌فاکتورهای من", e, () -> loadMyPrefactors()); } });
    }

    private String queryMyPrefactorsSql() throws Exception {
        try (Connection c = openConnection()) {
            ensurePrefactorTables(c);
            List<Object> params = new ArrayList<>(); String where = prefactorScopeWhere("p", params);
            String sql = "SELECT TOP (100) p.id, TRY_CONVERT(nvarchar(30),p.created_at), p.status, p.customer_code, p.customer_name, ISNULL(p.grand_total,p.total_amount), p.settlement_type, p.delivery_date, p.approval_reason, ISNULL(x.cnt,0) FROM dbo.meelano_prefactors p OUTER APPLY (SELECT COUNT_BIG(1) cnt FROM dbo.meelano_prefactor_items i WHERE i.prefactor_id=p.id) x" + where + " ORDER BY p.id DESC";
            JSONArray arr = new JSONArray();
            try (PreparedStatement ps = c.prepareStatement(sql)) { setParams(ps, params); try(ResultSet r = ps.executeQuery()) { while(r.next()){ JSONObject o=new JSONObject(); o.put("id", r.getLong(1)); o.put("date", stringOr(r.getString(2), "")); o.put("status", stringOr(r.getString(3), "")); o.put("customerCode", stringOr(r.getString(4), "")); o.put("customerName", stringOr(r.getString(5), "")); o.put("total", r.getDouble(6)); o.put("settlement", stringOr(r.getString(7), "")); o.put("delivery", stringOr(r.getString(8), "")); o.put("reason", stringOr(r.getString(9), "")); o.put("items", r.getLong(10)); arr.put(o); } } }
            return arr.toString();
        }
    }

    private void renderMyPrefactors(JSONArray rows) {
        content.removeAllViews(); addHero("پیش‌فاکتورهای من", "وضعیت همه سفارش‌ها و پیش‌نویس‌های ثبت‌شده");
        LinearLayout top = card(); top.setBackground(gradient(new int[]{alpha(navAccent("cart"),22), alpha(SURFACE,248)}, GradientDrawable.Orientation.RIGHT_LEFT,22));
        LinearLayout tr = new LinearLayout(this); tr.setOrientation(LinearLayout.HORIZONTAL); Button cart = primaryButton("بازگشت به سبد"); cart.setOnClickListener(v -> showApp("cart")); Button route = secondaryButton("مسیر بازدید"); route.setOnClickListener(v -> loadVisitRoutePage("")); tr.addView(cart, weightedButtonLp()); tr.addView(route, weightedButtonLp()); top.addView(tr, new LinearLayout.LayoutParams(-1,-2)); content.addView(top, new LinearLayout.LayoutParams(-1,-2));
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(-1,-2); lp0.setMargins(0,dp(12),0,0); content.addView(list, lp0);
        if (rows == null || rows.length()==0) { addEmptyTo(list, "پیش‌فاکتوری ثبت نشده است."); return; }
        for(int i=0;i<rows.length();i++) addPrefactorListCard(list, rows.optJSONObject(i));
    }

    private void addPrefactorListCard(LinearLayout parent, JSONObject r) {
        if (r == null) return; String st=r.optString("status",""); int accent="approved".equals(st)?SUCCESS:("rejected".equals(st)||"needs_revision".equals(st)?DANGER:("pending_approval".equals(st)?WARNING:navAccent("cart")));
        LinearLayout c=card(); c.setBackground(roundedStroke(alpha(accent,16),22,alpha(accent,70)));
        c.addView(text("#"+formatNumber(r.optLong("id"))+" • "+r.optString("customerName","مشتری")+" • "+prefactorStatusFa(st),13.0f,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("تاریخ: "+r.optString("date","")+" • اقلام: "+formatNumber(r.opt("items"))+" • تسویه: "+stringOr(r.optString("settlement"),"—"),10.2f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        c.addView(text("مبلغ: "+money(r.opt("total"))+(!r.optString("reason","").isEmpty()?"\nعلت تایید: "+r.optString("reason",""):""),10.5f,accent,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); Button det=secondaryButton("جزئیات/PDF"); det.setTextSize(8.6f); det.setOnClickListener(v -> loadPrefactorDetails(r.optLong("id"), false)); Button edit=secondaryButton("کپی به سبد"); edit.setTextSize(8.6f); edit.setOnClickListener(v -> loadPrefactorDetails(r.optLong("id"), true)); row.addView(det, weightedButtonLp()); row.addView(edit, weightedButtonLp()); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(8),0,0); c.addView(row,rp);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(10)); parent.addView(c,lp);
    }

    private void loadPrefactorDetails(long id, boolean copyToCart) {
        runDb(() -> queryPrefactorDetailSql(id), new DbCallback(){ @Override public void ok(String body){ try{ JSONObject d=new JSONObject(body); if(copyToCart){ restoreCartSnapshot(prefactorDetailToCartSnapshot(d)); showApp("cart"); } else showPrefactorDetailDialog(d); }catch(Exception e){ showPageError("جزئیات پیش‌فاکتور",e,()->loadMyPrefactors()); }} @Override public void fail(Exception e){ showPageError("جزئیات پیش‌فاکتور",e,()->loadMyPrefactors()); }});
    }

    private String queryPrefactorDetailSql(long id) throws Exception {
        try(Connection c=openConnection()){
            ensurePrefactorTables(c); JSONObject out=new JSONObject();
            try(PreparedStatement ps=c.prepareStatement("SELECT id,client_uuid,visitor_username,visitor_id,customer_code,customer_name,notes,signature_data,ISNULL(grand_total,total_amount),status,delivery_date,delivery_address,settlement_type,payment_ref,subtotal_amount,global_discount,tax_percent,tax_amount,approval_reason,TRY_CONVERT(nvarchar(30),created_at) FROM dbo.meelano_prefactors WHERE id=?")){ ps.setLong(1,id); try(ResultSet r=ps.executeQuery()){ if(r.next()){ out.put("id",r.getLong(1)); out.put("clientUuid",stringOr(r.getString(2),"")); out.put("visitor",stringOr(r.getString(3),"")); out.put("visitorId",stringOr(r.getString(4),"")); out.put("customerCode",stringOr(r.getString(5),"")); out.put("customerName",stringOr(r.getString(6),"")); out.put("notes",stringOr(r.getString(7),"")); out.put("signature",stringOr(r.getString(8),"")); out.put("grandTotal",r.getDouble(9)); out.put("status",stringOr(r.getString(10),"")); out.put("deliveryDate",stringOr(r.getString(11),"")); out.put("address",stringOr(r.getString(12),"")); out.put("settlement",stringOr(r.getString(13),"")); out.put("paymentRef",stringOr(r.getString(14),"")); out.put("subtotal",r.getDouble(15)); out.put("globalDiscount",r.getDouble(16)); out.put("taxPercent",r.getDouble(17)); out.put("taxAmount",r.getDouble(18)); out.put("approvalReason",stringOr(r.getString(19),"")); out.put("date",stringOr(r.getString(20),"")); } } }
            JSONArray items=new JSONArray(); try(PreparedStatement ps=c.prepareStatement("SELECT product_code,product_name,unit,qty,price,amount,stock_snapshot,pack_count,price_tier,line_discount,line_note FROM dbo.meelano_prefactor_items WHERE prefactor_id=? ORDER BY id")){ ps.setLong(1,id); try(ResultSet r=ps.executeQuery()){ while(r.next()){ JSONObject it=new JSONObject(); it.put("code",stringOr(r.getString(1),"")); it.put("name",stringOr(r.getString(2),"")); it.put("unit",stringOr(r.getString(3),"")); it.put("qty",r.getDouble(4)); it.put("price",r.getDouble(5)); it.put("amount",r.getDouble(6)); it.put("stock",r.getDouble(7)); it.put("pack",r.getDouble(8)); it.put("priceTier",stringOr(r.getString(9),"1")); it.put("lineDiscount",r.getDouble(10)); it.put("note",stringOr(r.getString(11),"")); items.put(it); } } }
            out.put("items",items); return out.toString();
        }
    }

    private JSONObject prefactorDetailToCartSnapshot(JSONObject d){ try{ JSONObject cst=new JSONObject(d.toString()); JSONObject cust=new JSONObject(); cust.put("code",d.optString("customerCode","")); cust.put("name",d.optString("customerName","")); cst.put("customer",cust); return cst; }catch(Exception e){ return d; } }

    private void showPrefactorDetailDialog(JSONObject d){
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12),dp(10),dp(12),dp(6));
        box.addView(text("پیش‌فاکتور #"+formatNumber(d.optLong("id")),16,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2));
        box.addView(text("مشتری: "+d.optString("customerName","")+"\nوضعیت: "+prefactorStatusFa(d.optString("status",""))+"\nمبلغ: "+money(d.optDouble("grandTotal",0))+"\nتوضیحات: "+d.optString("notes",""),11,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2));
        JSONArray items=d.optJSONArray("items"); for(int i=0;items!=null&&i<items.length()&&i<12;i++){ JSONObject it=items.optJSONObject(i); if(it!=null) box.addView(text("• "+it.optString("name","")+" × "+formatNumber(it.optDouble("qty",0))+" = "+money(it.optDouble("amount",0)),10.2f,TEXT,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); }
        AlertDialog dlg=new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن",null).setPositiveButton("ساخت PDF",null).create(); dlg.setOnShowListener(x->{ styleMeelanoDialog(dlg,navAccent("cart")); Button ok=dlg.getButton(AlertDialog.BUTTON_POSITIVE); if(ok!=null) ok.setOnClickListener(v->{ generatePrefactorPdf(d,"Meelano-Prefactor-"+d.optLong("id")+".pdf"); sharePlainText("پیش‌فاکتور MEELANO", prefactorShareText(d), null); }); }); dlg.show();
    }

    private void generateCurrentCartPdfAndShare(boolean shareOnly){ JSONObject snap=currentCartSnapshot(finalCartStatus()); generatePrefactorPdf(snap,"Meelano-Prefactor-Draft-v3.37.pdf"); sharePlainText("پیش‌فاکتور MEELANO", prefactorShareText(snap), null); }

    private String prefactorShareText(JSONObject d){
        StringBuilder b=new StringBuilder(); b.append("پیش‌فاکتور MEELANO\n"); b.append("مشتری: ").append(d.optString("customerName","")).append('\n'); b.append("مبلغ نهایی: ").append(money(d.optDouble("grandTotal",0))).append('\n'); JSONArray items=d.optJSONArray("items"); for(int i=0;items!=null&&i<items.length();i++){ JSONObject it=items.optJSONObject(i); if(it!=null)b.append("- ").append(it.optString("name","")).append(" × ").append(formatNumber(it.optDouble("qty",0))).append(" = ").append(money(cartItemNet(it))).append('\n'); } b.append("توضیحات: ").append(d.optString("notes","")); return b.toString();
    }

    private void generatePrefactorPdf(JSONObject d,String name){
        PdfDocument doc=null; try{ doc=new PdfDocument(); PdfDocument.Page page=doc.startPage(new PdfDocument.PageInfo.Builder(595,842,1).create()); Canvas canvas=page.getCanvas(); Paint pnt=new Paint(Paint.ANTI_ALIAS_FLAG); pnt.setColor(Color.rgb(248,251,255)); canvas.drawRect(0,0,595,842,pnt); pnt.setColor(Color.rgb(22,68,123)); canvas.drawRoundRect(new RectF(28,24,567,106),24,24,pnt); pnt.setColor(Color.WHITE); pnt.setTextAlign(Paint.Align.RIGHT); pnt.setTypeface(Typeface.DEFAULT_BOLD); pnt.setTextSize(20); canvas.drawText("پیش‌فاکتور MEELANO",535,58,pnt); pnt.setTextSize(11); canvas.drawText(nowText(),535,82,pnt); int y=136; pnt.setColor(Color.rgb(45,61,84)); pnt.setTextSize(12); for(String line:wrapPdfLine(prefactorShareText(d),58)){ if(y>790)break; canvas.drawText(line,540,y,pnt); y+=18; } doc.finishPage(page); File dir=getExternalFilesDir(null); if(dir==null)dir=getFilesDir(); File file=new File(dir,name==null?"Meelano-Prefactor.pdf":name); try(FileOutputStream fos=new FileOutputStream(file)){ doc.writeTo(fos);} Toast.makeText(this,"PDF ساخته شد: "+file.getAbsolutePath(),Toast.LENGTH_LONG).show(); }catch(Exception ex){ Toast.makeText(this,"ساخت PDF پیش‌فاکتور ممکن نشد: "+shortError(ex),Toast.LENGTH_LONG).show(); } finally{ if(doc!=null)doc.close(); }
    }

    private void loadVisitRoutePage(String search){
        content.removeAllViews(); addHero("مسیر بازدید امروز","مشتریان قابل پیگیری، شروع سریع پیش‌فاکتور و ثبت نتیجه بازدید"); addSearchBox("جستجوی مشتری مسیر…", search, q -> loadVisitRoutePage(q)); LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list,new LinearLayout.LayoutParams(-1,-2)); addLoading(list,"در حال آماده‌سازی مسیر…");
        runDb(() -> queryCartCustomers(search), new DbCallback(){ @Override public void ok(String body){ try{ renderVisitRoute(new JSONArray(body), search); }catch(Exception e){ showPageError("مسیر بازدید",e,()->loadVisitRoutePage(search)); }} @Override public void fail(Exception e){ showPageError("مسیر بازدید",e,()->loadVisitRoutePage(search)); }});
    }

    private void renderVisitRoute(JSONArray rows,String search){
        content.removeAllViews(); addHero("مسیر بازدید امروز","نتیجه هر بازدید را ثبت کنید یا مستقیم برای مشتری پیش‌فاکتور بسازید"); addSearchBox("جستجوی مشتری مسیر…", search, q -> loadVisitRoutePage(q)); LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list,new LinearLayout.LayoutParams(-1,-2)); if(rows==null||rows.length()==0){ addEmptyTo(list,"مشتری برای مسیر امروز پیدا نشد."); return; }
        for(int i=0;i<rows.length();i++){ JSONObject r=rows.optJSONObject(i); if(r==null)continue; LinearLayout c=card(); int accent=r.optDouble("balance",0)>0?WARNING:SUCCESS; c.setBackground(roundedStroke(alpha(accent,16),22,alpha(accent,68))); c.addView(text(r.optString("name","مشتری")+" • کد "+r.optString("code",""),13,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); c.addView(text("مانده: "+money(r.optDouble("balance",0))+" • آخرین خرید: "+stringOr(r.optString("lastSale"),"—")+" • تلفن: "+stringOr(r.optString("phone"),"—"),10.2f,MUTED,Typeface.NORMAL),new LinearLayout.LayoutParams(-1,-2)); LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); Button order=primaryButton("شروع پیش‌فاکتور"); order.setTextSize(8.6f); order.setOnClickListener(v->{ visitorCartCustomer=r; if(visitorCartAddress.trim().isEmpty())visitorCartAddress=r.optString("address",""); showApp("cart"); }); Button result=secondaryButton("ثبت نتیجه بازدید"); result.setTextSize(8.6f); result.setOnClickListener(v->showVisitResultDialog(r)); row.addView(order,weightedButtonLp()); row.addView(result,weightedButtonLp()); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2); rp.setMargins(0,dp(8),0,0); c.addView(row,rp); LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2); cp.setMargins(0,0,0,dp(10)); list.addView(c,cp); }
    }

    private void showVisitResultDialog(JSONObject customer){
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12),dp(10),dp(12),dp(6)); box.addView(text("نتیجه بازدید: "+customer.optString("name","مشتری"),16,TEXT,Typeface.BOLD),new LinearLayout.LayoutParams(-1,-2)); EditText result=input("نتیجه: خرید کرد/نکرد، مغازه بسته بود، نیاز به پیگیری…","نیازمند پیگیری",false); EditText note=input("توضیحات بازدید","",false); note.setSingleLine(false); note.setMinLines(2); box.addView(result,new LinearLayout.LayoutParams(-1,dp(48))); box.addView(note,new LinearLayout.LayoutParams(-1,dp(82))); AlertDialog dlg=new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن",null).setPositiveButton("ثبت",null).create(); dlg.setOnShowListener(d->{ styleMeelanoDialog(dlg,navAccent("visitor_dashboard")); Button ok=dlg.getButton(AlertDialog.BUTTON_POSITIVE); if(ok!=null)ok.setOnClickListener(v->{ dlg.dismiss(); saveVisitResult(customer,result.getText().toString(),note.getText().toString()); }); }); dlg.show();
    }

    private void saveVisitResult(JSONObject customer,String result,String note){
        runDb(() -> { try(Connection c=openConnection()){ ensurePrefactorTables(c); try(PreparedStatement ps=c.prepareStatement("INSERT INTO dbo.meelano_visit_results(visitor_username,visitor_id,customer_code,customer_name,result,notes,created_at) VALUES(?,?,?,?,?,?,SYSDATETIME())")){ ps.setString(1,currentAccountName()); ps.setString(2,currentVisitorScopeId()==null?"":String.valueOf(currentVisitorScopeId())); ps.setString(3,customer.optString("code","")); ps.setString(4,customer.optString("name","")); ps.setString(5,result); ps.setString(6,note); ps.executeUpdate(); } } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this,"نتیجه بازدید ثبت شد.",Toast.LENGTH_SHORT).show(); } @Override public void fail(Exception e){ Toast.makeText(MainActivity.this,"نتیجه به‌صورت محلی نگهداری شد.",Toast.LENGTH_LONG).show(); }});
    }

    private void showEndOfDayReportDialog(){
        runDb(this::queryEndOfDayReportSql,new DbCallback(){ @Override public void ok(String body){ showEndOfDayReportText(body); } @Override public void fail(Exception e){ showEndOfDayReportText("جمع‌بندی آفلاین\nپیش‌نویس محلی: "+formatNumber(localDrafts().length())+"\nصف ارسال: "+formatNumber(offlineQueue().length())+"\n"+readableError(e)); }});
    }

    private String queryEndOfDayReportSql() throws Exception { try(Connection c=openConnection()){ ensurePrefactorTables(c); List<Object> params=new ArrayList<>(); String where=prefactorScopeWhere("p",params); String and=where.isEmpty()?" WHERE ":where+" AND "; String sql="SELECT COUNT_BIG(1), ISNULL(SUM(ISNULL(grand_total,total_amount)),0), SUM(CASE WHEN status=N'draft' THEN 1 ELSE 0 END), SUM(CASE WHEN status=N'pending_approval' THEN 1 ELSE 0 END) FROM dbo.meelano_prefactors p"+and+" CONVERT(date,p.created_at)=CONVERT(date,SYSDATETIME())"; long cnt=0,draft=0,pending=0; double total=0; try(PreparedStatement ps=c.prepareStatement(sql)){ setParams(ps,params); try(ResultSet r=ps.executeQuery()){ if(r.next()){cnt=r.getLong(1); total=r.getDouble(2); draft=r.getLong(3); pending=r.getLong(4);} } } return "گزارش پایان روز ویزیتور\nپیش‌فاکتور امروز: "+formatNumber(cnt)+"\nجمع مبلغ: "+money(total)+"\nپیش‌نویس: "+formatNumber(draft)+"\nدر انتظار تایید: "+formatNumber(pending)+"\nصف آفلاین گوشی: "+formatNumber(offlineQueue().length()); } }

    private void showEndOfDayReportText(String body){ AlertDialog dlg=new AlertDialog.Builder(this).setTitle("جمع‌بندی پایان روز").setMessage(body).setNegativeButton("بستن",null).setPositiveButton("اشتراک/ثبت",(d,w)->{ sharePlainText("گزارش پایان روز MEELANO",body,null); saveEndOfDayReport(body); }).create(); dlg.show(); }

    private void saveEndOfDayReport(String body){ runDb(() -> { try(Connection c=openConnection()){ ensurePrefactorTables(c); try(PreparedStatement ps=c.prepareStatement("INSERT INTO dbo.meelano_day_reports(visitor_username,visitor_id,report_text,created_at) VALUES(?,?,?,SYSDATETIME())")){ ps.setString(1,currentAccountName()); ps.setString(2,currentVisitorScopeId()==null?"":String.valueOf(currentVisitorScopeId())); ps.setString(3,body); ps.executeUpdate(); } } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this,"گزارش پایان روز ثبت شد.",Toast.LENGTH_SHORT).show(); } @Override public void fail(Exception e){ Toast.makeText(MainActivity.this,"ثبت گزارش آنلاین انجام نشد.",Toast.LENGTH_LONG).show(); }}); }

    private class SignaturePadView extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();
        SignaturePadView(Context context) { super(context); p.setColor(TEXT); p.setStrokeWidth(dp(3)); p.setStyle(Paint.Style.STROKE); p.setStrokeCap(Paint.Cap.ROUND); p.setStrokeJoin(Paint.Join.ROUND); setBackground(roundedStroke(alpha(SURFACE_2, 220), 18, alpha(navAccent("cart"), 90))); }
        @Override protected void onDraw(Canvas c) { super.onDraw(c); c.drawPath(path, p); }
        @Override public boolean onTouchEvent(MotionEvent e) { float x=e.getX(), y=e.getY(); if(e.getAction()==MotionEvent.ACTION_DOWN){ path.moveTo(x,y); invalidate(); return true; } if(e.getAction()==MotionEvent.ACTION_MOVE){ path.lineTo(x,y); invalidate(); return true; } return true; }
        void clear() { path.reset(); invalidate(); }
        String exportPngBase64() { try { Bitmap b=Bitmap.createBitmap(Math.max(1,getWidth()), Math.max(1,getHeight()), Bitmap.Config.ARGB_8888); Canvas c=new Canvas(b); draw(c); ByteArrayOutputStream out=new ByteArrayOutputStream(); b.compress(Bitmap.CompressFormat.PNG, 90, out); return Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP); } catch(Exception ex){ return ""; } }
    }

    private void loadProducts(String query) { loadProducts(query, "all", false); }

    private void loadProducts(String query, String filter) { loadProducts(query, filter, false); }

    private void loadProducts(String query, String filter, boolean force) {
        String q = query == null ? "" : query;
        String f = filter == null || filter.trim().isEmpty() ? "all" : filter;
        if (!force && productsCacheJson != null && !productsCacheJson.trim().isEmpty() && q.equals(productsCacheQuery) && f.equals(productsCacheFilter)) {
            try { renderProductsFromJson(new JSONArray(productsCacheJson), q, f); return; } catch (Exception ignored) { }
        }
        content.removeAllViews();
        addHero("کالا و انبار", "اطلاعات کالاها ثابت می‌ماند؛ برای داده جدید تازه‌سازی دستی کنید.");
        addManualRefreshPanel("products", "بروزرسانی دستی کالاها", "فیلتر فعلی بدون تازه‌سازی دستی ثابت می‌ماند", () -> loadProducts(q, f, true));
        addSearchBox("جستجوی کالا…", q, qq -> loadProducts(qq, f, true));
        addProductFilterChips(q, f);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        addLoading(list, "در حال دریافت کالاها…");
        runDb(() -> queryProducts(q, f), new DbCallback() {
            @Override public void ok(String body) {
                try {
                    productsCacheJson = body;
                    productsCacheQuery = q;
                    productsCacheFilter = f;
                    markRefresh("products");
                    renderProductsFromJson(new JSONArray(body), q, f);
                } catch (Exception e) { showPageError("کالا", e, () -> loadProducts(q, f, true)); }
            }
            @Override public void fail(Exception e) { showPageError("کالا", e, () -> loadProducts(q, f, true)); }
        });
    }

    private void renderProductsFromJson(JSONArray rows, String query, String filter) {
        content.removeAllViews();
        addHero("کالا و انبار", "فیلتر موجودی، گردش خرید/فروش و کارت‌های محصول با تم MEELANO");
        addManualRefreshPanel("products", "بروزرسانی دستی کالاها", "آخرین لیست ثابت نگه داشته شده است", () -> loadProducts(query, filter, true));
        addSearchBox("جستجوی کالا…", query, q -> loadProducts(q, filter, true));
        addProductFilterChips(query, filter);
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); content.addView(list, new LinearLayout.LayoutParams(-1, -2));
        if (rows == null || rows.length() == 0) { addEmptyTo(list, "کالایی مطابق فیلتر پیدا نشد."); return; }
        for (int i = 0; i < rows.length(); i++) addProductCard(list, rows.optJSONObject(i));
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
            String price = resolveFlexible(cols, "FinalSalePrice", "SalePrice", "Price", "forosh1", "price1", "Price1", "قیمت_فروش1");
            String price2 = resolveFlexible(cols, "FinalSalePrice2", "SalePrice2", "Price2", "forosh2", "price_2", "قیمت_فروش2", "gheymat2");
            String buyPrice = resolve(cols, "pure_buy_price", "BuyPrice", "buy_price", "LastBuyPrice");
            String stock = resolve(cols, "Mojoodi", "mojoodi", "Stock", "Qty", "tedad", "Tedad", "inventorycount");
            String unit = resolve(cols, "unit", "Unit", "vahed", "UnitName");
            String packCount = resolveFlexible(cols, "mohvah", "tedad_baste", "TedadDarBaste", "pack_qty", "packCount", "package_qty", "تعداد_در_بسته");
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
            select.add(price2 == null ? "CAST(0 AS decimal(19,2)) AS قیمت_فروش۲" : "TRY_CONVERT(decimal(19,2),i.[" + price2 + "]) AS قیمت_فروش۲");
            select.add(buyPrice == null ? "CAST(0 AS decimal(19,2)) AS بهای_خرید" : "TRY_CONVERT(decimal(19,2),i.[" + buyPrice + "]) AS بهای_خرید");
            select.add(stock == null ? "CAST(0 AS decimal(19,3)) AS موجودی" : "TRY_CONVERT(decimal(19,3),i.[" + stock + "]) AS موجودی");
            select.add(unit == null ? "CAST(NULL AS nvarchar(80)) AS واحد" : "TRY_CONVERT(nvarchar(80),i.[" + unit + "]) AS واحد");
            select.add(packCount == null ? "CAST(1 AS decimal(19,3)) AS تعداد_در_بسته" : "TRY_CONVERT(decimal(19,3),i.[" + packCount + "]) AS تعداد_در_بسته");
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
            if ("priced".equals(filter) && price != null) where.add("ISNULL(TRY_CONVERT(decimal(19,2),i.[" + price + "]),0)>0");
            if ("image".equals(filter) && imageText && imageCol != null) where.add("LEN(LTRIM(RTRIM(TRY_CONVERT(nvarchar(max),i.[" + imageCol + "]))))>20");
            if ("package".equals(filter) && packCount != null) where.add("ISNULL(TRY_CONVERT(decimal(19,3),i.[" + packCount + "]),0)>1");
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
        metrics2.addView(metric("قیمت فروش ۱", money(r.opt("قیمت_فروش"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics2.addView(metric("قیمت فروش ۲", money(r.opt("قیمت_فروش۲"))), new LinearLayout.LayoutParams(0, -2, 1f));
        metrics2.addView(metric("در بسته", formatNumber(r.opt("تعداد_در_بسته"))), new LinearLayout.LayoutParams(0, -2, 1f));
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
                + "قیمت فروش ۱: " + money(r.opt("قیمت_فروش")) + "\n"
                + "قیمت فروش ۲: " + money(r.opt("قیمت_فروش۲")) + "\n"
                + "تعداد در بسته: " + formatNumber(r.opt("تعداد_در_بسته")) + "\n"
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

    private void loadReports() { loadReports(false); }

    private void loadReports(boolean force) {
        if (!force && reportsCacheJson != null && !reportsCacheJson.trim().isEmpty()) {
            try { renderAnalytics(new JSONObject(reportsCacheJson)); return; } catch (Exception ignored) { }
        }
        content.removeAllViews();
        addHero("گزارشات کاربردی مدیریت", "گزارش‌ها تا زمان بروزرسانی دستی ثابت می‌مانند.");
        addManualRefreshPanel("reports", "بروزرسانی دستی گزارشات", "برای دریافت داده جدید این دکمه را بزنید", () -> loadReports(true));
        addLoading(content, "در حال آماده‌سازی گزارشات مدیریتی…");
        runDb(this::queryAnalytics, new DbCallback() {
            @Override public void ok(String body) {
                try {
                    reportsCacheJson = body;
                    markRefresh("reports");
                    if (prefs != null) prefs.edit().putString(KEY_CACHE_REPORTS, body).apply();
                    renderAnalytics(new JSONObject(body));
                } catch (Exception e) { showPageError("گزارش‌ها", e, () -> loadReports(true)); }
            }
            @Override public void fail(Exception e) {
                if (!renderCachedReports(e)) showPageError("گزارش‌ها", e, () -> loadReports(true));
            }
        });
    }

    private void renderAnalytics(JSONObject a) {
        content.removeAllViews();
        if (a == null) a = new JSONObject();
        lastReportJson = a.toString();
        lastReportSummary = buildExecutiveReportSummary(a);
        addHero("اتاق فرمان زنده گزارشات", "گزارشات کامل‌تر، دسته‌بندی‌شده و بدون نمودار؛ مخصوص تصمیم مدیریت");
        addManualRefreshPanel("reports", "بروزرسانی دستی گزارشات", "نمای فعلی ثابت است تا خودتان تازه‌سازی کنید", () -> loadReports(true));
        addReportConnectionHints(a.optJSONObject("reportErrors"));
        addReportCommandCenter(a);
        addExecutiveSummaryCard(a);
        addBusinessHealthScoreCard(a);
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

    private void addReportConnectionHints(JSONObject errors) {
        if (errors == null || errors.length() == 0) return;
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(WARNING, 26), alpha(INFO, 14), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("عیب‌یابی هوشمند گزارشات", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("بعضی گزارش‌ها با ستون‌های جایگزین یا داده کافی پیدا نشدند؛ سایر بخش‌ها مستقل نمایش داده می‌شوند.", 10.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        JSONArray names = errors.names();
        if (names != null) {
            for (int i = 0; i < Math.min(5, names.length()); i++) {
                String key = names.optString(i, "");
                addActionItem(c, reportErrorLabel(key), limitText(errors.optString(key, ""), 110), WARNING);
            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private String reportErrorLabel(String key) {
        if (key == null) return "گزارش";
        if (key.contains("Sales")) return "فروش";
        if (key.contains("Profit") || key.contains("Margin")) return "سود";
        if (key.contains("check")) return "چک";
        if (key.contains("Customer") || key.contains("Debtor")) return "مشتری";
        if (key.contains("category")) return "کالا";
        return "گزارش";
    }

    private void addExecutiveSummaryCard(JSONObject a) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(GOLD_2, 32), alpha(INFO, 20), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 28));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(report3dIcon("CEO", GOLD), new LinearLayout.LayoutParams(dp(60), dp(60)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(10), 0, dp(8), 0);
        copy.addView(text("جمع‌بندی مدیرعامل", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView sub = text("خلاصه اجرایی قابل ارسال؛ وضعیت، ریسک، فرصت و تصمیم پیشنهادی در یک نگاه.", 10.8f, MUTED, Typeface.NORMAL);
        sub.setLineSpacing(dp(2), 1.05f);
        copy.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));

        addActionItem(c, "وضعیت", executiveStatus(a), SUCCESS);
        addActionItem(c, "ریسک اصلی", "مطالبات/چک‌ها: " + labelOf(strongestPoint(a.optJSONArray("debtAging")), "label", "ریسک بحرانی دیده نشد") + " • " + checkStatusSummary(a.optJSONArray("checkStatuses")), DANGER);
        addActionItem(c, "فرصت", "تمرکز فروش روی «" + labelOf(strongestPoint(a.optJSONArray("categoryShare")), "label", "گروه پرفروش") + "» و نگهداشت «" + labelOf(strongestPoint(a.optJSONArray("topCustomers")), "label", "مشتری کلیدی") + "».", INFO);
        addActionItem(c, "تصمیم", "امروز فروش را با وصول همزمان جلو ببر؛ سقف اعتبار مشتریان پرریسک را کنترل و برای مشتریان خاموش کمپین بازفعال‌سازی اجرا کن.", GOLD);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        Button share = secondaryButton("اشتراک متن");
        Button whatsapp = secondaryButton("خلاصه واتساپی");
        Button pdf = primaryButton("ساخت PDF");
        share.setTextSize(10.3f); whatsapp.setTextSize(10.3f); pdf.setTextSize(10.3f);
        share.setOnClickListener(v -> sharePlainText("خلاصه مدیریتی Meelano", lastReportSummary, null));
        whatsapp.setOnClickListener(v -> sharePlainText("خلاصه مدیریتی Meelano", whatsappSummary(lastReportSummary), "com.whatsapp"));
        pdf.setOnClickListener(v -> generateReportPdf(lastReportSummary));
        buttons.addView(share, weightedButtonLp());
        buttons.addView(whatsapp, weightedButtonLp());
        buttons.addView(pdf, weightedButtonLp());
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); bp.setMargins(0, dp(12), 0, 0); c.addView(buttons, bp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addBusinessHealthScoreCard(JSONObject a) {
        int score = businessHealthScore(a);
        int accent = score >= 75 ? SUCCESS : (score >= 55 ? WARNING : DANGER);
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(accent, 38), alpha(INFO, 14), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 26));
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        TextView scoreView = report3dIcon(String.valueOf(score), accent);
        scoreView.setTextSize(18);
        head.addView(scoreView, new LinearLayout.LayoutParams(dp(64), dp(64)));
        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(11), 0, dp(8), 0);
        copy.addView(text("امتیاز سلامت کسب‌وکار", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(healthScoreTitle(score) + " • امتیاز از ۱۰۰ بر اساس فروش، سود، مشتری، وصول و چک", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        c.addView(head, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        addHealthCell(row, "فروش اخیر", comparisonText(a.optJSONArray("weeklySales"), false), "↗", GOLD);
        addHealthCell(row, "ماه جاری/قبلی", comparisonText(a.optJSONArray("monthlyPurchaseSales"), false), "⇄", INFO);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(12), 0, 0); c.addView(row, rp);
        TextView tip = text("برداشت سریع: " + healthScoreAdvice(score), 10.8f, alpha(TEXT, 220), Typeface.BOLD);
        tip.setGravity(Gravity.CENTER);
        tip.setPadding(dp(10), dp(8), dp(10), dp(8));
        tip.setBackground(roundedStroke(alpha(accent, 18), 16, alpha(accent, 70)));
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2); tp.setMargins(0, dp(10), 0, 0); c.addView(tip, tp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private String buildExecutiveReportSummary(JSONObject a) {
        if (a == null) a = new JSONObject();
        StringBuilder b = new StringBuilder();
        b.append("خلاصه مدیریتی Meelano\n");
        b.append("امتیاز سلامت: ").append(businessHealthScore(a)).append(" از ۱۰۰ - ").append(healthScoreTitle(businessHealthScore(a))).append("\n");
        b.append("وضعیت: ").append(executiveStatus(a)).append("\n");
        b.append("فروش اخیر: ").append(trendSummary(a.optJSONArray("weeklySales"), false)).append("\n");
        b.append("سود/حاشیه: ").append(trendSummary(a.optJSONArray("monthlyProfit"), false)).append(" / ").append(latestValueText(a.optJSONArray("netMargin"), true)).append("\n");
        b.append("ریسک مطالبات: ").append(labelOf(strongestPoint(a.optJSONArray("debtAging")), "label", "بدون داده")).append(" - ").append(moneyValue(strongestPoint(a.optJSONArray("debtAging")), "value")).append("\n");
        b.append("مشتری کلیدی: ").append(labelOf(strongestPoint(a.optJSONArray("topCustomers")), "label", "نامشخص")).append("\n");
        b.append("فرصت کالا/بازار: ").append(labelOf(strongestPoint(a.optJSONArray("categoryShare")), "label", "نامشخص")).append("\n");
        b.append("۵ کار مهم امروز:\n");
        b.append("۱) وصول بدهکار اولویت‌دار را پیگیری کن.\n");
        b.append("۲) چک‌های نزدیک/پرریسک را با موجودی بانک تطبیق بده.\n");
        b.append("۳) قیمت گروه پرفروش را با حاشیه سود کنترل کن.\n");
        b.append("۴) برای مشتری طلایی پیشنهاد نگهداشت بده.\n");
        b.append("۵) مشتریان خاموش را با پیام کوتاه بازفعال کن.\n");
        return b.toString();
    }

    private String executiveStatus(JSONObject a) {
        int score = businessHealthScore(a);
        if (score >= 75) return "وضعیت کلی خوب است؛ رشد را با کنترل وصول ادامه بده.";
        if (score >= 55) return "وضعیت قابل کنترل است؛ نقدینگی و حاشیه سود را جدی‌تر پایش کن.";
        return "نیازمند اقدام سریع است؛ اول وصول، چک و فروش کم‌ریسک را مدیریت کن.";
    }

    private int businessHealthScore(JSONObject a) {
        if (a == null) a = new JSONObject();
        int score = 68;
        if (delta(a.optJSONArray("weeklySales")) > 0) score += 10; else score -= 7;
        if (delta(a.optJSONArray("monthlyProfit")) > 0) score += 8; else score -= 8;
        if (latestNumeric(a.optJSONArray("netMargin")) > 0) score += 6; else score -= 5;
        if (delta(a.optJSONArray("customerGrowth")) > 0) score += 6;
        if (valueOf(strongestPoint(a.optJSONArray("debtAging"))) > 0) score -= 8;
        if (a.optJSONArray("inactiveCustomers") != null && a.optJSONArray("inactiveCustomers").length() > 0) score -= 4;
        return Math.max(1, Math.min(100, score));
    }

    private double latestNumeric(JSONArray arr) {
        if (arr == null || arr.length() == 0) return 0;
        return valueOf(arr.optJSONObject(arr.length() - 1));
    }

    private double delta(JSONArray arr) {
        if (arr == null || arr.length() < 2) return 0;
        return valueOf(arr.optJSONObject(arr.length() - 1)) - valueOf(arr.optJSONObject(arr.length() - 2));
    }

    private String comparisonText(JSONArray arr, boolean percent) {
        if (arr == null || arr.length() < 2) return "داده مقایسه کافی نیست";
        JSONObject prev = arr.optJSONObject(arr.length() - 2);
        JSONObject last = arr.optJSONObject(arr.length() - 1);
        double p = valueOf(prev), l = valueOf(last);
        double change = p == 0 ? (l == 0 ? 0 : 100) : ((l - p) / Math.abs(p)) * 100.0;
        String arrow = change >= 0 ? "▲" : "▼";
        return arrow + " " + String.format(Locale.US, "%.1f", Math.abs(change)) + "% نسبت به قبل" + (percent ? "" : "");
    }

    private String healthScoreTitle(int score) {
        if (score >= 75) return "سالم و روبه‌رشد";
        if (score >= 55) return "متوسط اما قابل مدیریت";
        return "پرریسک و نیازمند اقدام";
    }

    private String healthScoreAdvice(int score) {
        if (score >= 75) return "به‌جای تخفیف کور، وفاداری مشتریان خوب و وصول منظم را تقویت کن.";
        if (score >= 55) return "۳ تماس وصول، یک بازبینی قیمت و یک کمپین مشتری خاموش امروز کافی است.";
        return "فروش جدید را فقط با کنترل اعتبار جلو ببر؛ اول چک، بدهکار و نقدینگی.";
    }

    private String whatsappSummary(String summary) {
        return "*Meelano Executive Summary*\n" + (summary == null ? "" : summary.replace("•", "-").trim());
    }

    private void sharePlainText(String title, String body, String packageName) {
        try {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(Intent.EXTRA_SUBJECT, title == null ? "Meelano" : title);
            send.putExtra(Intent.EXTRA_TEXT, body == null || body.trim().isEmpty() ? "خلاصه مدیریتی Meelano آماده است." : body);
            if (packageName != null && !packageName.trim().isEmpty()) send.setPackage(packageName);
            try { startActivity(Intent.createChooser(send, "ارسال خلاصه مدیریتی")); }
            catch (Exception first) { send.setPackage(null); startActivity(Intent.createChooser(send, "ارسال خلاصه مدیریتی")); }
        } catch (Exception ex) { Toast.makeText(this, "اشتراک خلاصه ممکن نشد.", Toast.LENGTH_SHORT).show(); }
    }

    private void generateReportPdf(String summary) {
        PdfDocument doc = null;
        try {
            doc = new PdfDocument();
            PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = doc.startPage(info);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(248, 251, 255));
            canvas.drawRect(0, 0, 595, 842, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(22, 68, 123));
            canvas.drawRoundRect(new android.graphics.RectF(28, 24, 567, 114), 28, 28, paint);
            paint.setColor(Color.rgb(65, 157, 232));
            canvas.drawRoundRect(new android.graphics.RectF(42, 36, 250, 102), 24, 24, paint);
            paint.setColor(Color.rgb(243, 190, 97));
            canvas.drawCircle(520, 68, 25, paint);
            paint.setColor(Color.rgb(22, 68, 123));
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(26);
            canvas.drawText("M", 520, 78, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setColor(Color.WHITE);
            paint.setTextSize(21);
            canvas.drawText("گزارش مدیریتی Meelano", 490, 58, paint);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setTextSize(10.5f);
            canvas.drawText("Direct SQL Native Android • امن", 490, 82, paint);
            paint.setColor(Color.argb(220, 255, 255, 255));
            canvas.drawText(nowText(), 240, 76, paint);

            String text = summary == null || summary.trim().isEmpty() ? lastReportSummary : summary;
            String[] paragraphs = text.split("\\n");
            int y = 142;
            int cardTop = y;
            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(13);
            paint.setColor(Color.rgb(22, 68, 123));
            canvas.drawText("خلاصه اجرایی", 545, y, paint);
            y += 20;
            paint.setTypeface(Typeface.DEFAULT);
            paint.setTextSize(11);
            paint.setColor(Color.rgb(52, 70, 95));
            int lineNo = 0;
            for (String paragraph : paragraphs) {
                if (paragraph.trim().isEmpty()) { y += 8; continue; }
                if (lineNo % 5 == 0) {
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);
                    canvas.drawRoundRect(new android.graphics.RectF(38, Math.max(126, y - 20), 557, Math.min(806, y + 96)), 18, 18, paint);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(1.4f);
                    paint.setColor(Color.argb(70, 65, 157, 232));
                    canvas.drawRoundRect(new android.graphics.RectF(38, Math.max(126, y - 20), 557, Math.min(806, y + 96)), 18, 18, paint);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setStrokeWidth(1f);
                    paint.setColor(Color.rgb(52, 70, 95));
                }
                for (String line : wrapPdfLine(paragraph, 58)) {
                    if (y > 784) break;
                    if (line.trim().startsWith("•")) {
                        paint.setColor(Color.rgb(243, 190, 97));
                        canvas.drawCircle(546, y - 4, 3.5f, paint);
                        paint.setColor(Color.rgb(52, 70, 95));
                        canvas.drawText(line.replaceFirst("^•\\s*", ""), 535, y, paint);
                    } else {
                        canvas.drawText(line, 545, y, paint);
                    }
                    y += 18;
                    lineNo++;
                }
                y += 7;
                if (y > 784) break;
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(22, 68, 123));
            canvas.drawRoundRect(new android.graphics.RectF(38, 792, 557, 823), 12, 12, paint);
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(10.5f);
            canvas.drawText("امضای دیجیتال مدیریتی Meelano • تهیه‌شده برای " + displayFirstName(), 545, 812, paint);
            doc.finishPage(page);
            File dir = getExternalFilesDir(null);
            if (dir == null) dir = getFilesDir();
            File file = new File(dir, "Meelano-Management-Report-v3.37.pdf");
            try (FileOutputStream fos = new FileOutputStream(file)) { doc.writeTo(fos); }
            Toast.makeText(this, "PDF لوکس ساخته شد: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception ex) { Toast.makeText(this, "ساخت PDF ممکن نشد: " + shortError(ex), Toast.LENGTH_SHORT).show(); }
        finally { if (doc != null) doc.close(); }
    }

    private List<String> wrapPdfLine(String text, int maxChars) {
        List<String> out = new ArrayList<>();
        String t = text == null ? "" : text.trim();
        if (t.isEmpty()) { out.add(""); return out; }
        while (t.length() > maxChars) {
            int cut = t.lastIndexOf(' ', maxChars);
            if (cut <= 0) cut = maxChars;
            out.add(t.substring(0, cut));
            t = t.substring(cut).trim();
        }
        out.add(t);
        return out;
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
                {"مشتری فعال", customerGrowth, "م"}, {"ریسک مطالبات", debt, "!"}, {"چک‌ها", checks, "✓"}
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
        item.setBackground(roundedStroke(alpha(SURFACE, 244), 16, alpha(accent, 92)));
        TextView badge = text(tag, 10.4f, Color.WHITE, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER);
        badge.setSingleLine(true);
        badge.setShadowLayer(dp(2), 0, dp(1), alpha(Color.BLACK, 120));
        badge.setBackground(gradient(new int[]{mix(accent, Color.WHITE, 0.18f), accent, mix(accent, Color.BLACK, 0.24f)}, GradientDrawable.Orientation.LEFT_RIGHT, 999));
        item.addView(badge, new LinearLayout.LayoutParams(dp(78), dp(34)));
        TextView b = text(body, 10.9f, TEXT, Typeface.BOLD);
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
        addReportDecision(c, key, rows, accent);
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

    private void addReportDecision(LinearLayout parent, String key, JSONArray rows, int accent) {
        TextView d = text("تصمیم پیشنهادی: " + reportDecision(key, rows), 10.7f, alpha(TEXT, 225), Typeface.BOLD);
        d.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        d.setLineSpacing(dp(2), 1.05f);
        d.setPadding(dp(10), dp(8), dp(10), dp(8));
        d.setBackground(roundedStroke(alpha(accent, 18), 15, alpha(accent, 70)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(10), 0, 0);
        parent.addView(d, lp);
    }

    private String reportDecision(String key, JSONArray rows) {
        String k = key == null ? "" : key;
        JSONObject top = strongestPoint(rows);
        if ("weeklySales".equals(k)) return "اگر آخرین بازه افت دارد، فروشنده/کانال همان بازه را بررسی کن و یک پیشنهاد کوتاه فعال کن.";
        if ("monthlyProfit".equals(k)) return "ماه کم‌سود را با تخفیف‌ها و بهای تمام‌شده تطبیق بده؛ سود را فدای فروش ظاهری نکن.";
        if ("netMargin".equals(k)) return "حاشیه زیر انتظار یعنی قیمت‌گذاری یا تخفیف نیاز به اصلاح فوری دارد.";
        if ("checkStatuses".equals(k)) return "دسته‌های پرمبلغ را امروز با تاریخ سررسید و بانک مرتبط تطبیق بده.";
        if ("debtAging".equals(k)) return "قدیمی‌ترین/پرریسک‌ترین باکت بدهی را به برنامه تماس امروز اضافه کن.";
        if ("overdueInvoices".equals(k)) return "اولویت با فاکتور معوق " + labelOf(top, "party", "مشتری مهم") + " است؛ فروش جدید را به تسویه گره بزن.";
        if ("topDebtors".equals(k)) return "برای " + labelOf(top, "party", "بدهکار اول") + " سقف اعتبار موقت تعیین کن.";
        if ("inactiveCustomers".equals(k)) return "برای مشتریان خاموش پیام/تماس بازفعال‌سازی با پیشنهاد محدود ارسال کن.";
        if ("topCustomers".equals(k)) return "برای مشتری اول برنامه وفاداری بده، ولی وابستگی فروش را هم کنترل کن.";
        if ("categoryShare".equals(k)) return "گروه پرفروش را برای موجودی، تبلیغ و افزایش قیمت پله‌ای بررسی کن.";
        if ("customerGrowth".equals(k)) return "اگر رشد مشتری فعال کم شده، کمپین ویزیت/تماس را روی مشتریان خاموش اجرا کن.";
        return "این گزارش را به یک اقدام کوتاه امروز تبدیل کن؛ فقط مشاهده عدد کافی نیست.";
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
        TextView icon = text(glyph, glyph != null && glyph.length() > 2 ? 12.8f : 19.5f, onColorFor(accent), Typeface.BOLD);
        icon.setGravity(Gravity.CENTER);
        icon.setSingleLine(true);
        icon.setShadowLayer(dp(4), 0, dp(2), alpha(Color.BLACK, isLightTheme() ? 105 : 175));
        GradientDrawable bg = gradient(new int[]{mix(accent, Color.WHITE, isLightTheme() ? 0.38f : 0.22f), accent, mix(accent, GOLD_2, 0.20f), mix(accent, Color.BLACK, isLightTheme() ? 0.10f : 0.34f)}, GradientDrawable.Orientation.TL_BR, 18);
        bg.setStroke(dp(1), alpha(mix(accent, Color.WHITE, 0.58f), 132));
        icon.setBackground(bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) icon.setElevation(dp(5));
        applyTouchFeedback(icon);
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
            JSONObject errors = new JSONObject();
            JSONArray monthly = safeAnalyticsArray(errors, "monthlyPurchaseSales", () -> loadMonthlyPurchaseSales(c));
            JSONArray profit = safeAnalyticsArray(errors, "monthlyProfit", () -> loadMonthlyProfit(c));
            a.put("weeklySales", safeAnalyticsArray(errors, "weeklySales", () -> loadWeeklySales(c)));
            a.put("monthlyPurchaseSales", monthly);
            a.put("checkStatuses", safeAnalyticsArray(errors, "checkStatuses", () -> loadCheckStatuses(c)));
            a.put("topCustomers", safeAnalyticsArray(errors, "topCustomers", () -> loadTopCustomers(c)));
            a.put("debtAging", safeAnalyticsArray(errors, "debtAging", () -> loadDebtAging(c)));
            a.put("monthlyProfit", profit);
            a.put("banks", safeAnalyticsArray(errors, "banks", () -> loadBanks(c)));
            a.put("categoryShare", safeAnalyticsArray(errors, "categoryShare", () -> loadCategoryShare(c)));
            a.put("customerGrowth", safeAnalyticsArray(errors, "customerGrowth", () -> loadCustomerGrowth(c)));
            a.put("netMargin", safeAnalyticsArray(errors, "netMargin", () -> loadNetMargin(profit, monthly)));
            a.put("topDebtors", safeAnalyticsArray(errors, "topDebtors", () -> queryTopDebtors(c)));
            a.put("overdueInvoices", safeAnalyticsArray(errors, "overdueInvoices", () -> queryOverdueInvoices(c)));
            a.put("inactiveCustomers", safeAnalyticsArray(errors, "inactiveCustomers", () -> queryInactiveCustomers(c)));
            try { a.put("latestSalesDate", latestDate(c, "sailfact", "date")); } catch (Exception ex) { a.put("latestSalesDate", ""); }
            try { a.put("latestPurchaseDate", latestDate(c, "buyfact", "DATE")); } catch (Exception ex) { a.put("latestPurchaseDate", ""); }
            if (errors.length() > 0) a.put("reportErrors", errors);
            return a.toString();
        }
    }

    private JSONArray safeAnalyticsArray(JSONObject errors, String key, JsonArrayJob job) {
        try { return job.run(); }
        catch (Exception ex) {
            try { if (errors != null) errors.put(key, shortError(ex)); } catch (Exception ignored) { }
            return new JSONArray();
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
        if (t.contains("چک")) return "چ";
        if (t.contains("مشتری")) return "م";
        if (t.contains("مطالبات") || t.contains("ریسک") || t.contains("معوق")) return "!";
        if (t.contains("بانک")) return "بانک";
        if (t.contains("خرید")) return "↙";
        if (t.contains("سود") || t.contains("حاشیه")) return "٪";
        if (t.contains("کالا") || t.contains("دسته") || t.contains("گروه")) return "▦";
        if (t.contains("فروش") || t.contains("درآمد")) return "↗";
        if (t.contains("حضور")) return "⏱";
        return "◆";
    }

    private void initSpeechEngine() {
        try {
            if (tts != null) return;
            tts = new TextToSpeech(getApplicationContext(), statusCode -> {
                if (statusCode == TextToSpeech.SUCCESS && tts != null) {
                    ttsReady = configurePersianTts();
                    if (ttsReady && pendingTtsText != null && !pendingTtsText.trim().isEmpty()) {
                        String pending = pendingTtsText;
                        pendingTtsText = "";
                        if (stage != null) stage.postDelayed(() -> speakAssistantText(pending), 250);
                    }
                } else {
                    ttsReady = false;
                    if (pendingTtsText != null && !pendingTtsText.trim().isEmpty()) openTtsInstaller();
                }
            });
        } catch (Exception ignored) {
            ttsReady = false;
        }
    }

    private boolean configurePersianTts() {
        if (tts == null) return false;
        Locale[] locales = new Locale[]{new Locale("fa", "IR"), new Locale("fa"), new Locale("pes", "IR")};
        for (Locale locale : locales) {
            try {
                int result = tts.setLanguage(locale);
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    try { tts.setSpeechRate(0.92f); } catch (Exception ignored) { }
                    try { tts.setPitch(1.02f); } catch (Exception ignored) { }
                    return true;
                }
            } catch (Exception ignored) { }
        }
        return false;
    }

    private void preparePersianTts(String cleanText) {
        pendingTtsText = cleanText == null ? "" : cleanText;
        ttsReady = configurePersianTts();
        if (ttsReady) {
            String pending = pendingTtsText;
            pendingTtsText = "";
            speakAssistantText(pending);
            return;
        }
        Toast.makeText(this, "میلو در حال آماده‌سازی گفتار فارسی است…", Toast.LENGTH_SHORT).show();
        try {
            Intent check = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            check.putExtra(TextToSpeech.Engine.EXTRA_CHECK_VOICE_DATA_FOR, new String[]{"fa-IR", "fa"});
            startActivityForResult(check, REQ_TTS_CHECK);
        } catch (Exception ex) {
            openTtsInstaller();
        }
    }

    private void openTtsInstaller() {
        try {
            Intent install = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(install);
        } catch (Exception ex) {
            try { startActivity(new Intent("com.android.settings.TTS_SETTINGS")); }
            catch (Exception ignored) { Toast.makeText(this, "برای خواندن فارسی، موتور گفتار فارسی را از تنظیمات اندروید فعال کنید.", Toast.LENGTH_LONG).show(); }
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

        FrameLayout portrait = miloPortrait(dp(120));
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(120));
        pp.setMargins(0, 0, 0, dp(10));
        box.addView(portrait, pp);
        TextView title = text("پسته میلو چطور صدایت کند؟", 18, TEXT, Typeface.BOLD);
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

    private int miloResponsiveHeight() {
        try {
            int h = getResources().getDisplayMetrics().heightPixels;
            return Math.max(dp(112), Math.min(dp(142), h / 7));
        } catch (Exception ignored) { return dp(128); }
    }

    private FrameLayout miloPortrait(int heightPx) {
        FrameLayout frame = new FrameLayout(this);
        frame.setPadding(0, 0, 0, 0);
        frame.setBackgroundColor(Color.TRANSPARENT);
        LivingPistachioMiloView live = new LivingPistachioMiloView(this);
        live.setContentDescription("میلو، پسته زنده و لوکس دستیار هوشمند Meelano");
        live.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        frame.addView(live, new FrameLayout.LayoutParams(-1, heightPx <= 0 ? dp(150) : heightPx, Gravity.CENTER));
        return frame;
    }

    private class LivingPistachioMiloView extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private long startMs;
        LivingPistachioMiloView(Context context) { super(context); setWillNotDraw(false); }
        @Override protected void onAttachedToWindow() { super.onAttachedToWindow(); startMs = System.currentTimeMillis(); invalidate(); }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth(), h = getHeight();
            if (w <= 0 || h <= 0) return;
            boolean moving = motionAllowed();
            float t = moving ? (System.currentTimeMillis() - startMs) / 1000f : 0f;
            float sc = Math.min(w / 360f, h / 305f) * 0.78f;
            float cx = w / 2f;
            float cy = h * 0.50f + (float)Math.sin(t * 1.15f) * dp(0.8f);
            drawPistachioShadow(canvas, cx, h * 0.84f, sc);
            canvas.save();
            canvas.translate(0, (float)Math.sin(t * 0.9f) * dp(0.9f));
            drawPistachioBody(canvas, cx, cy, sc, t);
            drawMiloMoodOrbs(canvas, cx, cy, sc, t);
            drawPistachioFace(canvas, cx, cy - dp(10) * sc, sc, t);
            drawPistachioArms(canvas, cx, cy + dp(26) * sc, sc, t);
            drawPistachioCrownAndShine(canvas, cx, cy, sc, t);
            canvas.restore();
            if (moving) postInvalidateDelayed(80);
        }
        private void drawPistachioShadow(Canvas c, float cx, float cy, float sc) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(Color.BLACK, 48));
            c.drawOval(new RectF(cx - dp(66) * sc, cy - dp(8) * sc, cx + dp(66) * sc, cy + dp(12) * sc), p);
        }
        private void drawPistachioBody(Canvas c, float cx, float cy, float sc, float t) {
            float breathe = (float)Math.sin(t * 1.05f) * dp(1.0f) * sc;
            int shell = mix(mix(GOLD_2, Color.WHITE, 0.40f), INFO, 0.10f);
            int shellEdge = mix(GOLD, Color.BLACK, 0.12f);
            int kernel = mix(SUCCESS, GOLD, 0.28f);
            int kernelDark = mix(kernel, NAVY, 0.25f);
            RectF shellOval = new RectF(cx - dp(82) * sc, cy - dp(112) * sc - breathe, cx + dp(82) * sc, cy + dp(104) * sc + breathe);
            p.setStyle(Paint.Style.FILL);
            p.setShadowLayer(dp(10) * sc, 0, dp(4) * sc, alpha(Color.BLACK, 115));
            p.setColor(shellEdge);
            c.drawOval(shellOval, p);
            p.clearShadowLayer();
            p.setColor(shell);
            c.drawOval(new RectF(shellOval.left + dp(6) * sc, shellOval.top + dp(5) * sc, shellOval.right - dp(6) * sc, shellOval.bottom - dp(5) * sc), p);

            Path split = new Path();
            split.moveTo(cx, shellOval.top + dp(8) * sc);
            split.cubicTo(cx - dp(45) * sc, cy - dp(66) * sc, cx - dp(47) * sc, cy + dp(34) * sc, cx - dp(8) * sc, shellOval.bottom - dp(14) * sc);
            split.cubicTo(cx + dp(8) * sc, cy + dp(34) * sc, cx + dp(45) * sc, cy - dp(62) * sc, cx, shellOval.top + dp(8) * sc);
            p.setColor(kernelDark);
            c.drawPath(split, p);
            RectF core = new RectF(cx - dp(46) * sc, cy - dp(75) * sc, cx + dp(46) * sc, cy + dp(71) * sc);
            p.setColor(kernel);
            c.drawOval(core, p);
            p.setColor(mix(kernel, Color.WHITE, 0.22f));
            c.drawOval(new RectF(core.left + dp(9) * sc, core.top + dp(8) * sc, core.left + dp(35) * sc, core.top + dp(58) * sc), p);
            p.setColor(alpha(Color.WHITE, 38));
            c.drawOval(new RectF(core.left + dp(26) * sc, core.top + dp(12) * sc, core.right - dp(8) * sc, core.bottom - dp(28) * sc), p);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(dp(1.35f) * sc);
            p.setColor(alpha(mix(shellEdge, Color.BLACK, 0.10f), 115));
            for (int i = -2; i <= 2; i++) {
                float off = i * dp(17) * sc;
                Path grain = new Path();
                grain.moveTo(cx + off, shellOval.top + dp(24) * sc);
                grain.cubicTo(cx + off - dp(12) * sc, cy - dp(32) * sc, cx + off + dp(10) * sc, cy + dp(34) * sc, cx + off * 0.45f, shellOval.bottom - dp(28) * sc);
                c.drawPath(grain, p);
            }
            p.setStrokeWidth(dp(2.5f) * sc);
            p.setColor(alpha(GOLD, 185));
            c.drawArc(new RectF(cx - dp(72) * sc, cy + dp(42) * sc, cx + dp(72) * sc, cy + dp(108) * sc), 205, 130, false, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(GOLD_2, 175));
            c.drawCircle(cx - dp(46) * sc, cy + dp(60) * sc, dp(4.5f) * sc, p);
            p.setColor(alpha(INFO, 150));
            c.drawCircle(cx + dp(48) * sc, cy + dp(58) * sc, dp(4.2f) * sc, p);
        }

        private void drawPistachioFace(Canvas c, float cx, float cy, float sc, float t) {
            String mood = miloMood == null ? "happy" : miloMood;
            boolean thinking = miloThinking || miloSpeaking || "thinking".equals(mood);
            boolean worried = "worried".equals(mood);
            boolean excited = "excited".equals(mood);
            float blinkPhase = t % (thinking ? 3.6f : 4.9f);
            float blink = blinkPhase > (thinking ? 3.45f : 4.70f) ? 0.18f : 1f;
            float gaze = (float)Math.sin(t * (thinking ? 1.15f : 0.65f)) * dp(thinking ? 3.0f : 2.0f) * sc;
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(dp(2.2f) * sc);
            p.setColor(alpha(mix(NAVY, GOLD, 0.18f), 180));
            if (worried) {
                c.drawLine(cx - dp(34) * sc, cy - dp(34) * sc, cx - dp(14) * sc, cy - dp(27) * sc, p);
                c.drawLine(cx + dp(14) * sc, cy - dp(27) * sc, cx + dp(34) * sc, cy - dp(34) * sc, p);
            } else if (thinking) {
                c.drawLine(cx - dp(34) * sc, cy - dp(31) * sc, cx - dp(14) * sc, cy - dp(35) * sc, p);
                c.drawLine(cx + dp(14) * sc, cy - dp(35) * sc, cx + dp(34) * sc, cy - dp(31) * sc, p);
            } else {
                c.drawLine(cx - dp(34) * sc, cy - dp(28) * sc, cx - dp(14) * sc, cy - dp(34) * sc, p);
                c.drawLine(cx + dp(14) * sc, cy - dp(34) * sc, cx + dp(34) * sc, cy - dp(28) * sc, p);
            }
            drawFunnyEye(c, cx - dp(22) * sc, cy - dp(12) * sc, dp(excited ? 11.6f : 10.5f) * sc, blink, gaze, sc);
            drawFunnyEye(c, cx + dp(22) * sc, cy - dp(12) * sc, dp(excited ? 11.6f : 10.5f) * sc, blink, gaze, sc);

            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(mix(GOLD, NAVY, 0.28f), 155));
            Path nose = new Path();
            nose.moveTo(cx, cy - dp(1) * sc);
            nose.lineTo(cx - dp(5) * sc, cy + dp(10) * sc);
            nose.quadTo(cx, cy + dp(13) * sc, cx + dp(5) * sc, cy + dp(10) * sc);
            nose.close();
            c.drawPath(nose, p);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeCap(Paint.Cap.ROUND);
            if (thinking) {
                float open = dp(4.5f) * sc + Math.abs((float)Math.sin(t * 6.5f)) * dp(3) * sc;
                p.setStyle(Paint.Style.FILL);
                p.setColor(alpha(mix(DANGER, GOLD, 0.45f), 215));
                c.drawOval(new RectF(cx - dp(11) * sc, cy + dp(19) * sc - open * 0.5f, cx + dp(11) * sc, cy + dp(19) * sc + open), p);
                p.setColor(alpha(Color.WHITE, 180));
                c.drawOval(new RectF(cx - dp(5) * sc, cy + dp(16) * sc, cx + dp(5) * sc, cy + dp(20) * sc), p);
            } else if (worried) {
                p.setStrokeWidth(dp(3.0f) * sc);
                p.setColor(mix(DANGER, GOLD, 0.25f));
                c.drawArc(new RectF(cx - dp(22) * sc, cy + dp(25) * sc, cx + dp(22) * sc, cy + dp(48) * sc), 205, 130, false, p);
            } else {
                p.setStrokeWidth(dp(excited ? 4.5f : 4.0f) * sc);
                p.setColor(mix(DANGER, GOLD, 0.45f));
                RectF smile = new RectF(cx - dp(30) * sc, cy + dp(8) * sc, cx + dp(30) * sc, cy + dp(excited ? 48 : 43) * sc);
                c.drawArc(smile, 17, 146, false, p);
                p.setStrokeWidth(dp(1.4f) * sc);
                p.setColor(alpha(Color.WHITE, 205));
                c.drawLine(cx - dp(10) * sc, cy + dp(29) * sc, cx + dp(10) * sc, cy + dp(29) * sc, p);
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.WHITE);
                c.drawRoundRect(new RectF(cx - dp(7) * sc, cy + dp(24) * sc, cx + dp(7) * sc, cy + dp(35) * sc), dp(3) * sc, dp(3) * sc, p);
            }
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(worried ? WARNING : DANGER, worried ? 82 : 112));
            c.drawCircle(cx - dp(34) * sc, cy + dp(10) * sc, dp(worried ? 4 : 5) * sc, p);
            c.drawCircle(cx + dp(34) * sc, cy + dp(10) * sc, dp(worried ? 4 : 5) * sc, p);
        }

        private void drawMiloMoodOrbs(Canvas c, float cx, float cy, float sc, float t) {
            String mood = miloMood == null ? "happy" : miloMood;
            if (!(miloThinking || "thinking".equals(mood) || "worried".equals(mood) || "excited".equals(mood))) return;
            String glyph = "thinking".equals(mood) || miloThinking ? "؟" : ("worried".equals(mood) ? "!" : "✦");
            int accent = "worried".equals(mood) ? WARNING : ("excited".equals(mood) ? GOLD_2 : INFO);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(dp(15) * sc);
            for (int i = 0; i < 3; i++) {
                float a = t * 1.3f + i * 2.1f;
                float x = cx + (float)Math.cos(a) * dp(54 + i * 8) * sc;
                float y = cy - dp(92 + i * 10) * sc + (float)Math.sin(a * 1.7f) * dp(5) * sc;
                p.setStyle(Paint.Style.FILL);
                p.setColor(alpha(accent, 45));
                c.drawCircle(x, y, dp(11 - i) * sc, p);
                p.setColor(alpha(TEXT, 215));
                c.drawText(glyph, x, y + dp(5) * sc, p);
            }
        }

        private void drawFunnyEye(Canvas c, float x, float y, float r, float blink, float gaze, float sc) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(Color.WHITE, 245));
            c.drawOval(new RectF(x - r, y - r * blink, x + r, y + r * blink), p);
            p.setColor(alpha(mix(INFO, GOLD, 0.35f), 235));
            c.drawCircle(x + gaze, y, Math.max(dp(2.4f) * sc, r * 0.42f * blink), p);
            p.setColor(Color.rgb(18, 24, 28));
            c.drawCircle(x + gaze, y, Math.max(dp(1.3f) * sc, r * 0.19f * blink), p);
            p.setColor(alpha(Color.WHITE, 225));
            c.drawCircle(x + gaze - r * 0.17f, y - r * 0.20f, Math.max(1.2f, r * 0.14f), p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(1.3f) * sc);
            p.setColor(alpha(mix(NAVY, GOLD, 0.16f), 130));
            c.drawArc(new RectF(x - r * 1.1f, y - r * 1.06f, x + r * 1.1f, y + r * 0.96f), 200, 140, false, p);
            p.setStyle(Paint.Style.FILL);
        }

        private void drawPistachioArms(Canvas c, float cx, float y, float sc, float t) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(7) * sc);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setColor(mix(SUCCESS, GOLD, 0.24f));
            float wave = (float)Math.sin(t * 1.05f) * dp(1.5f) * sc;
            c.drawLine(cx - dp(52) * sc, y - dp(8) * sc, cx - dp(82) * sc, y + dp(18) * sc + wave, p);
            c.drawLine(cx + dp(52) * sc, y - dp(8) * sc, cx + dp(82) * sc, y + dp(13) * sc - wave, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(GOLD_2);
            c.drawCircle(cx - dp(84) * sc, y + dp(19) * sc + wave, dp(8) * sc, p);
            c.drawCircle(cx + dp(84) * sc, y + dp(14) * sc - wave, dp(8) * sc, p);
            p.setColor(mix(NAVY, INFO, 0.28f));
            c.drawOval(new RectF(cx - dp(42) * sc, y + dp(68) * sc, cx - dp(8) * sc, y + dp(86) * sc), p);
            c.drawOval(new RectF(cx + dp(8) * sc, y + dp(68) * sc, cx + dp(42) * sc, y + dp(86) * sc), p);
        }
        private void drawPistachioCrownAndShine(Canvas c, float cx, float cy, float sc, float t) {
            Path crown = new Path();
            float top = cy - dp(122) * sc;
            crown.moveTo(cx - dp(34) * sc, top + dp(22) * sc);
            crown.lineTo(cx - dp(19) * sc, top);
            crown.lineTo(cx, top + dp(18) * sc);
            crown.lineTo(cx + dp(19) * sc, top);
            crown.lineTo(cx + dp(34) * sc, top + dp(22) * sc);
            crown.close();
            p.setStyle(Paint.Style.FILL);
            p.setColor(GOLD);
            c.drawPath(crown, p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(1.6f) * sc);
            p.setColor(alpha(Color.WHITE, 125));
            c.drawPath(crown, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(alpha(Color.WHITE, 170));
            c.drawCircle(cx + (float)Math.sin(t * 1.4f) * dp(10) * sc, top + dp(36) * sc, dp(3.2f) * sc, p);
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
        addHero("میلو، پسته زنده هوشمند", "تحلیل کوتاه فروش، مشتری، کالا، چک و ریسک");
        if (!prefs.getBoolean(KEY_NAME_ASKED, false)) maybeAskFirstName(false);

        LinearLayout intro = card();
        intro.setPadding(dp(14), dp(14), dp(14), dp(14));
        intro.setBackground(gradient(new int[]{alpha(INFO, 30), alpha(GOLD, 24), alpha(SURFACE, 248)}, GradientDrawable.Orientation.LEFT_RIGHT, 26));
        int mh = miloResponsiveHeight();
        FrameLayout portrait = miloPortrait(mh);
        intro.addView(portrait, new LinearLayout.LayoutParams(-1, mh));
        TextView title = text("میلو؛ پسته لوکس و آماده تحلیل", 16.5f, TEXT, Typeface.BOLD);
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
        addAssistantQuickChip(chips, "امروز", "امروز چه چیزهایی برای مدیریت مهم‌تر است؟");
        addAssistantQuickChip(chips, "بدهکار", "بدهکارهای مهم و اولویت وصول امروز را بگو.");
        addAssistantQuickChip(chips, "فروش", "فروش امروز و فرصت رشد را کوتاه تحلیل کن.");
        addAssistantQuickChip(chips, "تصمیم", "یک تصمیم مدیریتی پیشنهادی برای امروز بده.");
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.setMargins(0, dp(12), 0, 0);
        intro.addView(chips, cp);
        LinearLayout chips2 = new LinearLayout(this);
        chips2.setOrientation(LinearLayout.HORIZONTAL);
        addAssistantQuickChip(chips2, "ریسک", "ریسک‌های وصول، زیان و مشتریان خطرناک را کوتاه بگو.");
        addAssistantQuickChip(chips2, "قیمت", "برای قیمت‌گذاری و بازار فروش پیشنهاد عملی بده.");
        addAssistantQuickChip(chips2, "پورسانت", "برای پورسانت ویزیتورها پیشنهاد بده.");
        addAssistantQuickChip(chips2, "شوخی", "با لحن بامزه اما مفید یک خلاصه مدیریتی بده.");
        LinearLayout.LayoutParams cp2 = new LinearLayout.LayoutParams(-1, -2);
        cp2.setMargins(0, dp(7), 0, 0);
        intro.addView(chips2, cp2);
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

    private void updateMiloMood(String mood) {
        miloMood = (mood == null || mood.trim().isEmpty()) ? "happy" : mood;
        miloThinking = "thinking".equals(miloMood);
    }

    private String moodForAssistantText(String text) {
        String v = text == null ? "" : text;
        if (v.contains("ریسک") || v.contains("زیان") || v.contains("بدهکار") || v.contains("چک") || v.contains("خطر")) return "worried";
        if (v.contains("رشد") || v.contains("خوب") || v.contains("فرصت") || v.contains("موفق")) return "excited";
        return "happy";
    }

    private void submitAssistantQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            Toast.makeText(this, "یک سوال کوتاه بنویس؛ ذهن‌خوانی هنوز در نسخه بتاست!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (handleAssistantNavigationCommand(question)) {
            if (assistantInput != null) assistantInput.setText("");
            return;
        }
        if (assistantInput != null) assistantInput.setText("");
        updateMiloMood("thinking");
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
                    String smartAnswer = naturalSmartSearchAnswer(question, snapshot);
                    if (smartAnswer != null) {
                        answer = smartAnswer;
                    } else {
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
                }
            } catch (Exception ex) {
                answer = "اتصال به داده‌ها کامل نشد، اما اصل ماجرا این است: اینترنت/VPN/SQL Server را چک کن و دوباره بپرس. خطا: " + shortError(ex);
            }
            final String finalAnswer = answer;
            runOnUiThread(() -> {
                setConnectionStatus("connected");
                updateMiloMood(moodForAssistantText(finalAnswer));
                if (pending != null) pending.setText(finalAnswer);
                lastAssistantAnswer = finalAnswer;
            });
        });
    }

    private boolean handleAssistantNavigationCommand(String question) {
        String q = question == null ? "" : question.trim().toLowerCase(Locale.US);
        boolean command = q.contains("نشان بده") || q.contains("باز کن") || q.contains("برو") || q.contains("بخوان") || q.contains("نمایش");
        if (!command) return false;
        if (session == null) { showLogin("برای اجرای فرمان صوتی ابتدا وارد شوید."); return true; }
        if (q.contains("بدهکار") || q.contains("مشتری پرریسک")) {
            showApp("customers");
            loadCustomers("", "debt");
            Toast.makeText(this, "میلو مشتریان بدهکار/پرریسک را باز کرد.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (q.contains("کالا") || q.contains("رادار")) { showApp("command"); Toast.makeText(this, "رادار کالا در فرماندهی باز شد.", Toast.LENGTH_SHORT).show(); return true; }
        if (q.contains("فروش امروز") || q.contains("ریسک امروز") || q.contains("نقدینگی") || q.contains("تقویم")) { showApp("command"); Toast.makeText(this, "فرماندهی امروز باز شد.", Toast.LENGTH_SHORT).show(); return true; }
        if (q.contains("گزارش") || q.contains("اتاق فرمان")) { showApp("reports"); return true; }
        if (q.contains("سلامت اتصال") || q.contains("اتصال")) { showApp("health"); return true; }
        if (q.contains("تنظیمات") || q.contains("تم")) { showApp("settings"); return true; }
        return false;
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

    private String naturalSmartSearchAnswer(String question, String snapshot) throws Exception {
        String q = question == null ? "" : question.trim().toLowerCase(Locale.US);
        if (q.isEmpty()) return null;
        boolean wantsDebtors = q.contains("بدهکار") || q.contains("مطالبات");
        boolean wantsSalesToday = q.contains("فروش امروز") || q.contains("فروش روز") || (q.contains("امروز") && q.contains("فروش"));
        boolean wantsRisk = q.contains("ریسک امروز") || q.contains("خطر امروز") || q.contains("زیان امروز") || (q.contains("ریسک") && q.contains("امروز"));
        boolean wantsChecks = q.contains("چک") && (q.contains("امروز") || q.contains("مهم"));
        if (!wantsDebtors && !wantsSalesToday && !wantsRisk && !wantsChecks) return null;
        JSONObject snap = new JSONObject(snapshot == null || snapshot.trim().isEmpty() ? "{}" : snapshot);
        JSONObject dashboard = snap.optJSONObject("dashboard");
        JSONObject today = dashboard == null ? null : dashboard.optJSONObject("today");
        if (today == null) return null;
        StringBuilder b = new StringBuilder(displayFirstName()).append(" عزیز، پاسخ جستجوی هوشمند میلو:\n");
        if (wantsSalesToday) {
            JSONObject sales = today.optJSONObject("sales");
            b.append("• فروش امروز: ").append(metricValue(sales, "جمع فروش", "نامشخص")).append(" در ").append(metricValue(sales, "تعداد اسناد", "۰")).append(" سند.\n");
        }
        if (wantsDebtors) {
            JSONArray debtors = today.optJSONArray("topDebtors");
            b.append("• بدهکارهای مهم:\n");
            int n = Math.min(5, debtors == null ? 0 : debtors.length());
            if (n == 0) b.append("  - مورد مهمی پیدا نشد.\n");
            for (int i = 0; i < n; i++) { JSONObject d = debtors.optJSONObject(i); b.append("  - ").append(labelOf(d, "party", "مشتری")).append(" • ").append(moneyValue(d, "amount")).append("\n"); }
        }
        if (wantsChecks) {
            JSONObject put = today.optJSONObject("putChecks");
            JSONObject get = today.optJSONObject("getChecks");
            b.append("• چک‌های امروز/مهم: دریافتی ").append(metricValue(get, "جمع مبلغ", "—")).append("؛ پرداختی ").append(metricValue(put, "جمع مبلغ", "—")).append(".\n");
        }
        if (wantsRisk) {
            JSONObject topDebtor = firstObject(today.optJSONArray("topDebtors"));
            JSONArray overdue = today.optJSONArray("overdueInvoices");
            b.append("• ریسک امروز: ").append(overdue != null && overdue.length() > 0 ? "فاکتورهای معوق و وصول بدهی اولویت دارند." : "ریسک خیلی تند دیده نشد، ولی وصول را ول نکن.").append("\n");
            b.append("• اولویت تماس: ").append(labelOf(topDebtor, "party", "ندارد")).append(" • ").append(moneyValue(topDebtor, "amount")).append("\n");
        }
        b.append("• پیشنهاد: از مرکز کارهای امروز شروع کن؛ میلو غر نمی‌زند، فقط کمی اصرار مدیریتی دارد.");
        return b.toString();
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
            if (m != null && m.optString("label", "").contains(contains)) {
                if (privacyMode() && looksSensitiveMetric(m.optString("label", ""))) return "•••• ریال";
                return m.optString("value", fallback);
            }
        }
        return fallback;
    }

    private boolean looksSensitiveMetric(String label) {
        if (label == null) return false;
        return label.contains("مبلغ") || label.contains("جمع") || label.contains("مانده") || label.contains("فروش") || label.contains("خرید") || label.contains("بده") || label.contains("بانک") || label.contains("سود");
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
        if ("groq".equals(provider)) return callChatCompletions("https://api.groq.com/openai/v1/chat/completions", "llama-3.3-70b-versatile", key, question, snapshot);
        if ("gapgpt".equals(provider)) return callChatCompletions(prefString(KEY_AI_GAPGPT_BASE, "https://api.gapgpt.app/v1") + "/chat/completions", "gpt-4o-mini", key, question, snapshot);
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
        String response = httpPost("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + encoded, req.toString(), new HashMap<>());
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
        if (!storedAiKey("groq").isEmpty()) return "groq";
        if (!storedAiKey("gapgpt").isEmpty()) return "gapgpt";
        if (!storedAiKey("grok").isEmpty()) return "grok";
        return selected == null ? "" : selected;
    }

    private String providerDisplayName(String provider) {
        if ("groq".equals(provider)) return "Groq";
        if ("gapgpt".equals(provider)) return "GapGPT";
        if ("grok".equals(provider)) return "Grok";
        if ("gemini".equals(provider)) return "Gemini";
        if ("chatgpt".equals(provider)) return "OpenAI";
        return "تحلیل داخلی";
    }

    private String providerPrefKey(String provider) {
        if ("groq".equals(provider)) return KEY_AI_GROQ;
        if ("gapgpt".equals(provider)) return KEY_AI_GAPGPT;
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
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) return "ks:" + keystoreEncrypt(raw.trim());
        } catch (Exception ignored) { }
        return "x:" + legacyProtectSecret(raw);
    }

    private String unprotectSecret(String encoded) {
        if (encoded == null || encoded.trim().isEmpty()) return "";
        String v = encoded.trim();
        try {
            if (v.startsWith("ks:")) return keystoreDecrypt(v.substring(3));
            if (v.startsWith("x:")) return legacyUnprotectSecret(v.substring(2));
            return legacyUnprotectSecret(v);
        } catch (Exception ignored) {
            try { return legacyUnprotectSecret(v); } catch (Exception ignored2) { return ""; }
        }
    }

    private String legacyProtectSecret(String raw) {
        byte[] b = raw.trim().getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < b.length; i++) b[i] = (byte) (b[i] ^ 0x5A);
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    private String legacyUnprotectSecret(String encoded) {
        byte[] b = Base64.decode(encoded, Base64.NO_WRAP);
        for (int i = 0; i < b.length; i++) b[i] = (byte) (b[i] ^ 0x5A);
        return new String(b, StandardCharsets.UTF_8).trim();
    }

    private SecretKey localSecretKey() throws Exception {
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        if (!ks.containsAlias(LOCAL_KEY_ALIAS)) {
            KeyGenerator kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(LOCAL_KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build();
            kg.init(spec);
            kg.generateKey();
        }
        return (SecretKey) ks.getKey(LOCAL_KEY_ALIAS, null);
    }

    private String keystoreEncrypt(String raw) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, localSecretKey());
        byte[] iv = cipher.getIV();
        byte[] enc = cipher.doFinal(raw.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(iv, Base64.NO_WRAP) + "." + Base64.encodeToString(enc, Base64.NO_WRAP);
    }

    private String keystoreDecrypt(String payload) throws Exception {
        String[] parts = payload.split("\\.", 2);
        if (parts.length != 2) return "";
        byte[] iv = Base64.decode(parts[0], Base64.NO_WRAP);
        byte[] enc = Base64.decode(parts[1], Base64.NO_WRAP);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, localSecretKey(), new GCMParameterSpec(128, iv));
        return new String(cipher.doFinal(enc), StandardCharsets.UTF_8).trim();
    }

    private EditText apiInput(String hint, String provider) {
        EditText e = input(hint + (storedAiKey(provider).isEmpty() ? "" : " • ذخیره‌شده"), "", true);
        e.setTextDirection(View.TEXT_DIRECTION_LTR);
        e.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        return e;
    }

    private String keyStatus(String provider) {
        String key = storedAiKey(provider);
        return providerDisplayName(provider) + ": " + (key.isEmpty() ? "ثبت نشده" : "ذخیره‌شده • ********");
    }

    private void addAiSettingsCard() {
        LinearLayout ai = card();
        LinearLayout.LayoutParams aip = new LinearLayout.LayoutParams(-1, -2);
        aip.setMargins(0, dp(12), 0, 0);
        ai.setBackground(gradient(new int[]{alpha(INFO, 28), alpha(GOLD, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.LEFT_RIGHT, 24));
        ai.addView(text("دستیار هوش مصنوعی", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView summary = text("مدل فعال: " + providerDisplayName(activeAiProvider()) + " • نام خطاب: " + displayFirstName(), 11.2f, MUTED, Typeface.NORMAL);
        ai.addView(summary, new LinearLayout.LayoutParams(-1, -2));
        TextView status = text(aiKeyStatusText(), 10.7f, alpha(TEXT, 205), Typeface.NORMAL);
        status.setLineSpacing(dp(2), 1.05f);
        LinearLayout.LayoutParams stp = new LinearLayout.LayoutParams(-1, -2); stp.setMargins(0, dp(8), 0, 0);
        ai.addView(status, stp);

        EditText chatgpt = apiInput("OpenAI API key • فقط ستاره‌ای ذخیره می‌شود", "chatgpt");
        EditText gemini = apiInput("Gemini API key • فقط ستاره‌ای ذخیره می‌شود", "gemini");
        EditText groq = apiInput("Groq API key • فقط ستاره‌ای ذخیره می‌شود", "groq");
        EditText gapgpt = apiInput("GapGPT API key • فقط ستاره‌ای ذخیره می‌شود", "gapgpt");
        addApiField(ai, "OpenAI", chatgpt);
        addApiField(ai, "Gemini", gemini);
        addApiField(ai, "Groq", groq);
        addApiField(ai, "GapGPT", gapgpt);

        Button save = primaryButton("ذخیره کلیدهای واردشده");
        save.setOnClickListener(v -> {
            saveEnteredAiKeys(chatgpt, gemini, groq, gapgpt);
            status.setText(aiKeyStatusText());
            summary.setText("مدل فعال: " + providerDisplayName(activeAiProvider()) + " • نام خطاب: " + displayFirstName());
            Toast.makeText(this, "کلیدهای AI ذخیره شدند", Toast.LENGTH_SHORT).show();
        });
        LinearLayout.LayoutParams savep = new LinearLayout.LayoutParams(-1, dp(48)); savep.setMargins(0, dp(12), 0, 0);
        ai.addView(save, savep);

        LinearLayout tests = new LinearLayout(this);
        tests.setOrientation(LinearLayout.HORIZONTAL);
        Button t1 = secondaryButton("تست ChatGPT");
        Button t2 = secondaryButton("تست Gemini");
        Button t3 = secondaryButton("تست Groq");
        t1.setTextSize(9.8f); t2.setTextSize(9.8f); t3.setTextSize(9.8f);
        t1.setOnClickListener(v -> validateKeyFromField("chatgpt", chatgpt, status));
        t2.setOnClickListener(v -> validateKeyFromField("gemini", gemini, status));
        t3.setOnClickListener(v -> validateKeyFromField("groq", groq, status));
        LinearLayout.LayoutParams bt = new LinearLayout.LayoutParams(0, dp(42), 1f); bt.setMargins(dp(3), dp(10), dp(3), 0);
        tests.addView(t1, bt);
        LinearLayout.LayoutParams bt2 = new LinearLayout.LayoutParams(0, dp(42), 1f); bt2.setMargins(dp(3), dp(10), dp(3), 0);
        tests.addView(t2, bt2);
        LinearLayout.LayoutParams bt3 = new LinearLayout.LayoutParams(0, dp(42), 1f); bt3.setMargins(dp(3), dp(10), dp(3), 0);
        tests.addView(t3, bt3);
        ai.addView(tests, new LinearLayout.LayoutParams(-1, -2));
        Button t4 = secondaryButton("تست GapGPT");
        t4.setTextSize(9.8f);
        t4.setOnClickListener(v -> validateKeyFromField("gapgpt", gapgpt, status));
        LinearLayout.LayoutParams bt4 = new LinearLayout.LayoutParams(-1, dp(42)); bt4.setMargins(dp(3), dp(8), dp(3), 0);
        ai.addView(t4, bt4);

        LinearLayout more = new LinearLayout(this);
        more.setOrientation(LinearLayout.HORIZONTAL);
        Button testAll = primaryButton("ذخیره و تست همه");
        Button choose = secondaryButton("انتخاب مدل پیش‌فرض");
        Button rename = secondaryButton("تغییر نام من");
        testAll.setTextSize(10.2f); choose.setTextSize(10.2f); rename.setTextSize(10.2f);
        testAll.setOnClickListener(v -> {
            saveEnteredAiKeys(chatgpt, gemini, groq, gapgpt);
            validateKeyFromField("chatgpt", chatgpt, status);
            validateKeyFromField("gemini", gemini, status);
            validateKeyFromField("groq", groq, status);
            validateKeyFromField("gapgpt", gapgpt, status);
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
                .setMessage("کلیدهای OpenAI، Gemini، Groq و GapGPT از همین دستگاه پاک شوند؟")
                .setNegativeButton("خیر", null)
                .setPositiveButton("بله، پاک کن", (d, w) -> {
                    prefs.edit().remove(KEY_AI_CHATGPT).remove(KEY_AI_GEMINI).remove(KEY_AI_GROK).remove(KEY_AI_GROQ).remove(KEY_AI_GAPGPT).apply();
                    status.setText(aiKeyStatusText());
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

    private String aiKeyStatusText() {
        return keyStatus("chatgpt") + "\n" + keyStatus("gemini") + "\n" + keyStatus("groq") + "\n" + keyStatus("gapgpt");
    }

    private boolean saveEnteredAiKeys(EditText chatgpt, EditText gemini, EditText groq, EditText gapgpt) {
        SharedPreferences.Editor ed = prefs.edit();
        if (prefString(KEY_AI_GAPGPT_BASE, "").trim().isEmpty()) ed.putString(KEY_AI_GAPGPT_BASE, "https://api.gapgpt.app/v1");
        boolean changed = false;
        String c = chatgpt == null ? "" : chatgpt.getText().toString().trim();
        String g = gemini == null ? "" : gemini.getText().toString().trim();
        String q = groq == null ? "" : groq.getText().toString().trim();
        String gp = gapgpt == null ? "" : gapgpt.getText().toString().trim();
        if (!c.isEmpty()) { ed.putString(KEY_AI_CHATGPT, protectSecret(c)); if (activeAiProvider().isEmpty()) ed.putString(KEY_AI_PROVIDER, "chatgpt"); changed = true; chatgpt.setText(""); }
        if (!g.isEmpty()) { ed.putString(KEY_AI_GEMINI, protectSecret(g)); if (activeAiProvider().isEmpty()) ed.putString(KEY_AI_PROVIDER, "gemini"); changed = true; gemini.setText(""); }
        if (!q.isEmpty()) { ed.putString(KEY_AI_GROQ, protectSecret(q)); if (activeAiProvider().isEmpty()) ed.putString(KEY_AI_PROVIDER, "groq"); changed = true; groq.setText(""); }
        if (!gp.isEmpty()) { ed.putString(KEY_AI_GAPGPT, protectSecret(gp)); if (activeAiProvider().isEmpty()) ed.putString(KEY_AI_PROVIDER, "gapgpt"); changed = true; gapgpt.setText(""); }
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
                result = "✅ " + providerDisplayName(provider) + " معتبر است و به‌عنوان مدل فعال انتخاب شد.\n" + aiKeyStatusText();
            } catch (Exception ex) {
                result = "❌ اعتبارسنجی " + providerDisplayName(provider) + " ناموفق بود: " + shortError(ex) + "\n" + aiKeyStatusText();
            }
            final String finalResult = result;
            runOnUiThread(() -> { if (status != null) status.setText(finalResult); });
        });
    }

    private void showAiProviderChooser(TextView summary) {
        String[] ids = {"chatgpt", "gemini", "groq", "gapgpt"};
        String[] labels = {"OpenAI", "Gemini", "Groq", "GapGPT"};
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
        String clean = text.replace("•", "").replace("✅", "").replace("❌", "").replace("🔊", "").replace("\n", ". ");
        if (tts == null) {
            pendingTtsText = clean;
            initSpeechEngine();
            Toast.makeText(this, "میلو در حال روشن کردن گفتار فارسی است…", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ttsReady) {
            preparePersianTts(clean);
            return;
        }
        miloSpeaking = true;
        miloMood = "thinking";
        if (stage != null) stage.postDelayed(() -> { miloSpeaking = false; miloMood = "happy"; }, Math.min(22000, Math.max(3500, clean.length() * 55)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tts.speak(clean, TextToSpeech.QUEUE_FLUSH, null, "meelano_ai_answer");
        else tts.speak(clean, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void addExperienceSettingsCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(INFO, 22), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("تجربه گرافیکی و مصرف باتری", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("برای گوشی‌های کوچک یا زمان Battery Saver، نمایش جمع‌وجور و حرکت کمتر فعال می‌شود.", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        Button compact = secondaryButton((compactUi() ? "✓ " : "") + "حالت جمع‌وجور کارت‌ها");
        compact.setOnClickListener(v -> { prefs.edit().putBoolean(KEY_COMPACT_UI, !prefs.getBoolean(KEY_COMPACT_UI, false)).apply(); showApp("settings"); });
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(-1, dp(48)); p1.setMargins(0, dp(12), 0, dp(8)); c.addView(compact, p1);
        Button motion = secondaryButton((motionAllowed() ? "" : "✓ ") + "کاهش حرکت و مصرف باتری");
        motion.setOnClickListener(v -> { prefs.edit().putBoolean(KEY_REDUCED_MOTION, !prefs.getBoolean(KEY_REDUCED_MOTION, false)).apply(); showApp("settings"); });
        c.addView(motion, new LinearLayout.LayoutParams(-1, dp(48)));
        content.addView(c, cp);
    }

    private void addHardwareToolsCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(GOLD, 24), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 22));
        c.addView(text("ابزارهای سخت‌افزاری و عملیاتی", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("اسکن بارکد/QR، آماده‌سازی چاپ/اشتراک خلاصه، دسترسی سریع به بلوتوث و تست یادآوری محلی.", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        Button scan = secondaryButton("اسکن QR/بارکد");
        Button bt = secondaryButton("پرینتر/بلوتوث");
        scan.setTextSize(10.5f); bt.setTextSize(10.5f);
        scan.setOnClickListener(v -> startBarcodeScan());
        bt.setOnClickListener(v -> openBluetoothSettings());
        row1.addView(scan, hardwareButtonLp()); row1.addView(bt, hardwareButtonLp());
        c.addView(row1, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        Button share = secondaryButton("ارسال/چاپ خلاصه");
        Button notify = secondaryButton("تست یادآوری");
        share.setTextSize(10.5f); notify.setTextSize(10.5f);
        share.setOnClickListener(v -> shareOperationalSummary());
        notify.setOnClickListener(v -> showLocalNotification("یادآوری Meelano", "چک‌ها، مطالبات و گزارش روزانه را بررسی کن."));
        row2.addView(share, hardwareButtonLp()); row2.addView(notify, hardwareButtonLp());
        c.addView(row2, new LinearLayout.LayoutParams(-1, -2));
        content.addView(c, cp);
    }

    private void addSecureDistributionCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 22));
        c.addView(text("مسیر نصب امن و انتشار رسمی", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView body = text("برای کمترین هشدار نصب، نسخه نهایی باید با Keystore اختصاصی شرکت امضا و از Google Play، Managed Play یا کانال سازمانی مورداعتماد منتشر شود. هشدار منابع ناشناس سیاست اندروید است و از داخل APK کامل حذف نمی‌شود.", 10.8f, MUTED, Typeface.NORMAL);
        body.setLineSpacing(dp(2), 1.05f);
        c.addView(body, new LinearLayout.LayoutParams(-1, -2));
        content.addView(c, cp);
    }

    private LinearLayout.LayoutParams hardwareButtonLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(46), 1f);
        lp.setMargins(dp(3), dp(12), dp(3), 0);
        return lp;
    }

    private void startBarcodeScan() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_FORMATS", "QR_CODE,CODE_128,CODE_39,EAN_13,EAN_8,UPC_A,UPC_E");
            startActivityForResult(intent, REQ_BARCODE_SCAN);
        } catch (Exception ex) {
            Toast.makeText(this, "برای اسکن واقعی، یک Barcode Scanner نصب کنید؛ سپس دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
        }
    }

    private void openBluetoothSettings() {
        try { startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS)); }
        catch (Exception ex) { Toast.makeText(this, "تنظیمات بلوتوث روی این دستگاه در دسترس نیست.", Toast.LENGTH_SHORT).show(); }
    }

    private void shareOperationalSummary() {
        String body = lastAssistantAnswer == null || lastAssistantAnswer.trim().isEmpty() ? "خلاصه مدیریتی Meelano آماده چاپ/اشتراک است. برای گزارش دقیق، ابتدا یک سؤال از میلو بپرسید." : lastAssistantAnswer;
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_SUBJECT, "خلاصه مدیریتی Meelano");
        send.putExtra(Intent.EXTRA_TEXT, body);
        try { startActivity(Intent.createChooser(send, "چاپ یا ارسال خلاصه")); }
        catch (Exception ex) { Toast.makeText(this, "برنامه‌ای برای ارسال/چاپ متن پیدا نشد.", Toast.LENGTH_SHORT).show(); }
    }

    private void showLocalNotification(String title, String body) { showLocalNotification(title, body, true); }

    private void showLocalNotification(String title, String body, boolean askPermission) {
        try {
            if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (askPermission) {
                    requestNotificationPermissionIfNeeded();
                    Toast.makeText(this, "اجازه اعلان را فعال کنید تا یادآوری نمایش داده شود.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);
            Notification.Builder b = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? new Notification.Builder(this, NOTIFY_CHANNEL) : new Notification.Builder(this);
            b.setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title == null ? "Meelano" : title)
                    .setContentText(body == null ? "یادآوری مدیریتی" : body)
                    .setStyle(new Notification.BigTextStyle().bigText(body == null ? "یادآوری مدیریتی" : body))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis());
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.notify(1414, b.build());
            Toast.makeText(this, "یادآوری ثبت/نمایش داده شد.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) { Toast.makeText(this, "نمایش اعلان ممکن نشد: " + shortError(ex), Toast.LENGTH_SHORT).show(); }
    }

    private void addQuickLoginSettingsCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 20), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("ورود سریع با PIN / اثر انگشت", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text(prefs.getBoolean(KEY_QUICK_LOGIN_ENABLED, false) ? "فعال است؛ می‌توانید از صفحه ورود با PIN یا اثر انگشت وارد شوید." : "برای دفعات بعد، ورود سریع را با PIN فعال کنید؛ اثر انگشت هم از جلسه ذخیره‌شده استفاده می‌کند.", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button set = primaryButton("تنظیم/تغییر PIN"); set.setTextSize(10.5f); set.setOnClickListener(v -> showSetQuickPinDialog());
        Button off = secondaryButton("غیرفعال"); off.setTextSize(10.5f); off.setOnClickListener(v -> { prefs.edit().putBoolean(KEY_QUICK_LOGIN_ENABLED, false).remove(KEY_QUICK_PIN).apply(); Toast.makeText(this, "ورود سریع غیرفعال شد.", Toast.LENGTH_SHORT).show(); renderSettings(); });
        row.addView(set, weightedButtonLp()); row.addView(off, weightedButtonLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(12), 0, 0); c.addView(row, rp);
        content.addView(c, cp);
    }

    private void addLocalSecurityCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(Color.rgb(126, 87, 255), 28), alpha(SUCCESS, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("امنیت محلی پیشرفته", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("PIN ورود سریع و کلیدهای AI از این نسخه با Android Keystore رمزنگاری می‌شوند؛ داده‌های قدیمی هنگام ذخیره بعدی خودکار به قالب امن‌تر مهاجرت می‌کنند.", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        TextView badge = text(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? "✓ Keystore فعال روی این دستگاه" : "حالت سازگار قدیمی", 11, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? SUCCESS : WARNING, Typeface.BOLD);
        badge.setGravity(Gravity.CENTER); badge.setPadding(dp(8), dp(8), dp(8), dp(8));
        badge.setBackground(roundedStroke(alpha(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? SUCCESS : WARNING, 18), 16, alpha(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? SUCCESS : WARNING, 70)));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); bp.setMargins(0, dp(10), 0, 0); c.addView(badge, bp);
        content.addView(c, cp);
    }

    private void addReminderSettingsCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(WARNING, 22), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("یادآوری‌های هوشمند", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("اعلان‌ها فقط وقتی خودتان فعال کنید درخواست مجوز می‌دهند؛ مناسب چک، بدهکار، مشتری خاموش و گزارش روزانه.", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        addReminderToggle(c, KEY_REMIND_CHECKS, "چک‌های سررسید");
        addReminderToggle(c, KEY_REMIND_DEBTORS, "بدهکاران مهم");
        addReminderToggle(c, KEY_REMIND_INACTIVE, "مشتریان خاموش");
        addReminderToggle(c, KEY_REMIND_DAILY, "گزارش روزانه");
        EditText hour = input("ساعت یادآوری، مثلا 09:30", prefs.getString(KEY_REMIND_HOUR, "09:00"), false);
        Button save = secondaryButton("ذخیره ساعت یادآوری");
        save.setOnClickListener(v -> { prefs.edit().putString(KEY_REMIND_HOUR, hour.getText().toString().trim()).apply(); Toast.makeText(this, "ساعت یادآوری ذخیره شد.", Toast.LENGTH_SHORT).show(); });
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, dp(48)); hp.setMargins(0, dp(10), 0, dp(8)); c.addView(hour, hp);
        c.addView(save, new LinearLayout.LayoutParams(-1, dp(46)));
        content.addView(c, cp);
    }

    private void addReminderToggle(LinearLayout parent, String key, String label) {
        Button b = prefs.getBoolean(key, false) ? primaryButton(label + " ✓") : secondaryButton(label);
        b.setTextSize(10.5f);
        b.setOnClickListener(v -> {
            boolean next = !prefs.getBoolean(key, false);
            prefs.edit().putBoolean(key, next).apply();
            if (next) requestNotificationPermissionIfNeeded();
            renderSettings();
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(44)); lp.setMargins(0, dp(8), 0, 0); parent.addView(b, lp);
    }

    private void addPrivacySettingsCard() { }

    private void addConnectionHealthCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 18), alpha(INFO, 14), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("سلامت اتصال", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("آخرین اتصال موفق: " + prefs.getString(KEY_LAST_CONNECTION_OK, "ثبت نشده"), 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("آخرین خطا: " + prefs.getString(KEY_LAST_CONNECTION_ERROR, "ندارد"), 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        Button test = primaryButton("تست سلامت اتصال");
        test.setOnClickListener(v -> showApp("health"));
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, dp(48)); tp.setMargins(0, dp(12), 0, 0); c.addView(test, tp);
        content.addView(c, cp);
    }

    private void renderConnectionHealthPage() {
        content.removeAllViews();
        addHero("صفحه سلامت اتصال", "وضعیت SQL، زمان پاسخ، آخرین موفقیت/خطا و تست اتصال بدون نمایش جزئیات حساس.");
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(INFO, 26), alpha(SUCCESS, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 26));
        c.addView(text("SQL Server Direct Health", 17, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("وضعیت فعلی: " + (connectionIndicator == null ? "—" : "نمایشگر بالای صفحه"), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("آخرین اتصال موفق: " + prefs.getString(KEY_LAST_CONNECTION_OK, "ثبت نشده"), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("آخرین خطا: " + prefs.getString(KEY_LAST_CONNECTION_ERROR, "ندارد"), 11, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        TextView safe = text("جزئیات فنی اتصال و رمز SQL طبق سیاست Meelano مخفی است؛ فقط وضعیت کاربردی به کاربر نمایش داده می‌شود.", 10.6f, alpha(TEXT, 220), Typeface.BOLD);
        safe.setPadding(dp(10), dp(9), dp(10), dp(9));
        safe.setGravity(Gravity.CENTER); safe.setBackground(roundedStroke(alpha(GOLD, 18), 16, alpha(GOLD, 65)));
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2); sp.setMargins(0, dp(10), 0, dp(10)); c.addView(safe, sp);
        LinearLayout tests = new LinearLayout(this); tests.setOrientation(LinearLayout.HORIZONTAL);
        Button test = primaryButton("تست latency");
        Button diag = secondaryButton("عیب‌یابی مرحله‌ای");
        test.setTextSize(10.5f); diag.setTextSize(10.5f);
        test.setOnClickListener(v -> testConnectionHealth());
        diag.setOnClickListener(v -> runConnectionDiagnostics());
        tests.addView(test, weightedButtonLp()); tests.addView(diag, weightedButtonLp());
        c.addView(tests, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(12), 0, 0); content.addView(c, lp);
    }

    private void runConnectionDiagnostics() {
        runDb(() -> {
            long t0 = System.currentTimeMillis();
            StringBuilder out = new StringBuilder();
            out.append("✓ تنظیمات اتصال مخفی Meelano آماده است\n");
            try (Connection c = openConnection()) {
                long connected = System.currentTimeMillis();
                out.append("✓ ورود SQL موفق • ").append(connected - t0).append("ms\n");
                try (PreparedStatement ps = c.prepareStatement("SELECT DB_NAME(), @@VERSION")) {
                    try (ResultSet r = ps.executeQuery()) {
                        if (r.next()) {
                            out.append("✓ اجرای Query موفق • پایگاه: ").append(stringOr(r.getString(1), "—")).append("\n");
                            out.append("✓ نسخه سرور دریافت شد؛ جزئیات حساس نمایش داده نمی‌شود\n");
                        }
                    }
                }
                out.append("✓ نتیجه: اتصال سالم است؛ اگر داده‌ها خالی‌اند، دسترسی جدول/فیلتر تاریخ را بررسی کنید.");
            }
            return out.toString();
        }, new DbCallback() {
            @Override public void ok(String body) { new AlertDialog.Builder(MainActivity.this).setTitle("عیب‌یابی اتصال").setMessage(body).setPositiveButton("باشه", null).show(); if ("health".equals(activePage)) renderConnectionHealthPage(); }
            @Override public void fail(Exception e) { new AlertDialog.Builder(MainActivity.this).setTitle("عیب‌یابی اتصال").setMessage("اتصال کامل نشد:\n" + readableError(e) + "\n\nجزئیات حساس اتصال نمایش داده نمی‌شود.").setPositiveButton("باشه", null).show(); if ("health".equals(activePage)) renderConnectionHealthPage(); }
        });
    }

    private void testConnectionHealth() {
        runDb(() -> { try (Connection c = openConnection(); PreparedStatement ps = c.prepareStatement("SELECT 1")) { try (ResultSet r = ps.executeQuery()) { return r.next() ? "ok" : "empty"; } } }, new DbCallback() {
            @Override public void ok(String body) { Toast.makeText(MainActivity.this, "اتصال سالم است.", Toast.LENGTH_SHORT).show(); if ("health".equals(activePage)) renderConnectionHealthPage(); else renderSettings(); }
            @Override public void fail(Exception e) { Toast.makeText(MainActivity.this, readableError(e), Toast.LENGTH_SHORT).show(); if ("health".equals(activePage)) renderConnectionHealthPage(); else renderSettings(); }
        });
    }

    private void addIconSystemCard() {
        LinearLayout c = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2); cp.setMargins(0, dp(12), 0, 0);
        c.setBackground(gradient(new int[]{alpha(INFO, 24), alpha(GOLD_2, 18), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("پکیج آیکن‌های سه‌بعدی Meelano", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("آیکن‌های داشبورد، گزارش، مشتری، کالا و دستیار با تم آبی روشن هماهنگ و به شکل Native/Glass نمایش داده می‌شوند.", 10.8f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        String[][] icons = {{"◈","داشبورد"},{"✦","میلو"},{"👥","مشتری"},{"◼","کالا"},{"⌁","گزارش"}};
        int[] colors = {GOLD, mix(GOLD_2, INFO, 0.45f), SUCCESS, WARNING, INFO};
        for (int i = 0; i < icons.length; i++) {
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.addView(report3dIcon(icons[i][0], colors[i]), new LinearLayout.LayoutParams(dp(42), dp(42)));
            TextView l = text(icons[i][1], 8.8f, MUTED, Typeface.BOLD); l.setGravity(Gravity.CENTER); l.setSingleLine(true);
            item.addView(l, new LinearLayout.LayoutParams(-1, -2));
            LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(0, -2, 1f); ip.setMargins(dp(2), dp(10), dp(2), 0);
            row.addView(item, ip);
        }
        c.addView(row, new LinearLayout.LayoutParams(-1, -2));
        content.addView(c, cp);
    }

    private void loadAccessManagement() {
        if (!isFullAccessUser()) { renderLockedSection("management"); return; }
        content.removeAllViews();
        addHero("مدیریت دسترسی کاربران", "تعریف نقش و مجوز برای کاربران آتیران، ویزیتورها، ماموران پخش، رانندگان و کارگران");
        addManualRefreshPanel("management", "بروزرسانی مدیریت کاربران", "نقش‌ها و مجوزها پس از ورود بعدی هر کاربر اعمال می‌شود", () -> loadAccessManagement());
        addLoading(content, "در حال فراخوانی کاربران و نقش‌ها…");
        runDb(this::queryAccessManagementSql, new DbCallback() {
            @Override public void ok(String body) {
                try { renderAccessManagement(new JSONObject(body)); markRefresh("management"); }
                catch (Exception e) { showPageError("مدیریت", e, () -> loadAccessManagement()); }
            }
            @Override public void fail(Exception e) { showPageError("مدیریت", e, () -> loadAccessManagement()); }
        });
    }

    private String queryAccessManagementSql() throws Exception {
        try (Connection c = openConnection()) {
            ensureAccessControlTables(c);
            JSONObject out = new JSONObject();
            JSONArray roles = queryAccessRoles(c);
            out.put("roles", roles);
            Map<String, JSONObject> users = new HashMap<>();
            appendConfiguredAccessUsers(c, users);
            appendAtiranUsers(c, users);
            appendVisitorAccessUsers(c, users);
            List<JSONObject> list = new ArrayList<>(users.values());
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override public int compare(JSONObject a, JSONObject b) { return a.optString("display", a.optString("username", "")).compareToIgnoreCase(b.optString("display", b.optString("username", ""))); }
            });
            JSONArray arr = new JSONArray();
            for (JSONObject u : list) {
                String role = canonicalAccessRole(u.optString("role", "user"));
                if (role.isEmpty()) role = "user";
                String perms = stringOr(u.optString("permissions", ""), "").trim();
                String effective = perms.isEmpty() ? accessRolePermissions(c, role) : perms;
                u.put("role", role);
                u.put("roleLabel", accessRoleLabel(role));
                u.put("effectivePermissions", effective);
                u.put("allowed", permissionSet(effective).size());
                arr.put(u);
            }
            out.put("users", arr);
            return out.toString();
        }
    }

    private void ensureAccessControlTables(Connection c) throws Exception {
        try (Statement st = c.createStatement()) {
            st.execute("IF OBJECT_ID(N'dbo.meelano_access_roles',N'U') IS NULL CREATE TABLE dbo.meelano_access_roles (role_key nvarchar(60) NOT NULL PRIMARY KEY, role_label nvarchar(160) NULL, permissions nvarchar(max) NULL, updated_at datetime2 NOT NULL DEFAULT SYSDATETIME())");
            st.execute("IF OBJECT_ID(N'dbo.meelano_access_users',N'U') IS NULL CREATE TABLE dbo.meelano_access_users (username nvarchar(160) NOT NULL PRIMARY KEY, display_name nvarchar(220) NULL, source nvarchar(80) NULL, source_id nvarchar(100) NULL, role_key nvarchar(60) NOT NULL DEFAULT N'user', permissions nvarchar(max) NULL, enabled bit NOT NULL DEFAULT 1, updated_at datetime2 NOT NULL DEFAULT SYSDATETIME())");
        }
        for (String[] role : roleCatalog()) upsertAccessRoleDefault(c, role[0], role[1], defaultPermissionString(role[0]));
    }

    private void upsertAccessRoleDefault(Connection c, String key, String label, String permissions) throws Exception {
        try (PreparedStatement ps = c.prepareStatement("IF NOT EXISTS (SELECT 1 FROM dbo.meelano_access_roles WHERE role_key=?) INSERT INTO dbo.meelano_access_roles(role_key,role_label,permissions) VALUES(?,?,?)")) {
            ps.setString(1, key); ps.setString(2, key); ps.setString(3, label); ps.setString(4, permissions); ps.executeUpdate();
        }
    }

    private JSONArray queryAccessRoles(Connection c) throws Exception {
        JSONArray arr = new JSONArray();
        try (PreparedStatement ps = c.prepareStatement("SELECT role_key,role_label,permissions,CONVERT(nvarchar(19),updated_at,120) FROM dbo.meelano_access_roles ORDER BY CASE role_key WHEN N'admin' THEN 0 WHEN N'manager' THEN 1 WHEN N'senior' THEN 2 WHEN N'visitor' THEN 3 WHEN N'distributor' THEN 4 WHEN N'driver' THEN 5 WHEN N'worker' THEN 6 WHEN N'warehouse' THEN 7 WHEN N'senior_accountant' THEN 8 WHEN N'accountant' THEN 9 WHEN N'employee' THEN 10 WHEN N'collections' THEN 11 WHEN N'sales_employee' THEN 12 ELSE 19 END, role_key")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    JSONObject o = new JSONObject();
                    o.put("role", canonicalAccessRole(r.getString(1)));
                    o.put("label", stringOr(r.getString(2), accessRoleLabel(r.getString(1))));
                    o.put("permissions", stringOr(r.getString(3), defaultPermissionString(r.getString(1))));
                    o.put("updated", stringOr(r.getString(4), ""));
                    o.put("allowed", permissionSet(o.optString("permissions", "")).size());
                    arr.put(o);
                }
            }
        }
        return arr;
    }

    private String accessRolePermissions(Connection c, String role) {
        role = canonicalAccessRole(role);
        try {
            if (c != null && tableExists(c, "meelano_access_roles")) {
                try (PreparedStatement ps = c.prepareStatement("SELECT permissions FROM dbo.meelano_access_roles WHERE role_key=?")) {
                    ps.setString(1, role);
                    try (ResultSet r = ps.executeQuery()) { if (r.next() && r.getString(1) != null && !r.getString(1).trim().isEmpty()) return r.getString(1).trim(); }
                }
            }
        } catch (Exception ignored) { }
        return defaultPermissionString(role);
    }

    private void appendConfiguredAccessUsers(Connection c, Map<String, JSONObject> users) throws Exception {
        if (!tableExists(c, "meelano_access_users")) return;
        try (PreparedStatement ps = c.prepareStatement("SELECT TOP (300) username,display_name,source,source_id,role_key,permissions,enabled FROM dbo.meelano_access_users ORDER BY updated_at DESC")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) addAccessCandidate(users, r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getBoolean(7));
            }
        }
    }

    private void appendAtiranUsers(Connection c, Map<String, JSONObject> users) throws Exception {
        if (!tableExists(c, "sys_users")) return;
        Set<String> cols = columns(c, "sys_users");
        String id = resolveFlexible(cols, "user_id", "UserID", "id", "ID");
        String username = resolveFlexible(cols, "user_name", "username", "UserName", "login", "name");
        if (username == null) return;
        String display = resolveFlexible(cols, "display_name", "full_name", "fullname", "FullName", "name", "Name", "user_name");
        String role = resolveFlexible(cols, "role", "Role", "user_role", "access_role", "AccessRole", "semat", "سمت", "level", "AccessLevel");
        String sql = "SELECT TOP (250) " + (id == null ? "CAST(NULL AS nvarchar(100))" : "TRY_CONVERT(nvarchar(100),[" + id + "])") + ", TRY_CONVERT(nvarchar(160),[" + username + "]), " + (display == null ? "TRY_CONVERT(nvarchar(220),[" + username + "])" : "TRY_CONVERT(nvarchar(220),[" + display + "])") + ", " + (role == null ? "CAST(NULL AS nvarchar(80))" : "TRY_CONVERT(nvarchar(80),[" + role + "])") + " FROM dbo.sys_users ORDER BY 2";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) addAccessCandidate(users, r.getString(2), r.getString(3), "کاربران آتیران", r.getString(1), stringOr(r.getString(4), "user"), "", true);
        }
    }

    private void appendVisitorAccessUsers(Connection c, Map<String, JSONObject> users) throws Exception {
        if (!tableExists(c, "visitors")) return;
        Set<String> cols = columns(c, "visitors");
        String id = resolveFlexible(cols, "vis_rdf", "rdf", "RDF", "ID", "id", "shvis");
        String username = resolveFlexible(cols, "Username", "username", "user_name", "login", "UserName");
        String display = resolveFlexible(cols, "vis_name", "name", "Name", "VisitorName", "moname");
        String role = resolveFlexible(cols, "role", "Role", "vis_role", "access_role", "semat", "سمت", "level", "VisitorRole");
        if (id == null && username == null && display == null) return;
        String userExpr = username == null ? (display == null ? "N'visitor-' + TRY_CONVERT(nvarchar(100),[" + id + "])" : "TRY_CONVERT(nvarchar(160),[" + display + "])") : "TRY_CONVERT(nvarchar(160),[" + username + "])";
        String dispExpr = display == null ? userExpr : "TRY_CONVERT(nvarchar(220),[" + display + "])";
        String sql = "SELECT TOP (250) " + (id == null ? "CAST(NULL AS nvarchar(100))" : "TRY_CONVERT(nvarchar(100),[" + id + "])") + ", " + userExpr + ", " + dispExpr + ", " + (role == null ? "N'visitor'" : "TRY_CONVERT(nvarchar(80),[" + role + "])") + " FROM dbo.visitors " + activeWhere(cols, "") + " ORDER BY 3";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet r = ps.executeQuery()) {
            while (r.next()) addAccessCandidate(users, r.getString(2), r.getString(3), "ویزیتورها", r.getString(1), stringOr(r.getString(4), "visitor"), "", true);
        }
    }

    private void addAccessCandidate(Map<String, JSONObject> users, String username, String display, String source, String sourceId, String role, String permissions, boolean enabled) {
        try {
            String u = stringOr(username, "").trim();
            if (u.isEmpty()) return;
            String key = normalizeIdentity(u);
            JSONObject o = users.get(key);
            if (o == null) { o = new JSONObject(); users.put(key, o); }
            if (!o.has("username") || o.optString("username", "").isEmpty()) o.put("username", u);
            if (!o.has("display") || o.optString("display", "").isEmpty()) o.put("display", stringOr(display, u));
            if (!o.has("source") || o.optString("source", "").isEmpty() || "تنظیم‌شده".equals(o.optString("source", ""))) o.put("source", stringOr(source, "تنظیم‌شده"));
            if (!o.has("sourceId") || o.optString("sourceId", "").isEmpty()) o.put("sourceId", stringOr(sourceId, ""));
            String r = canonicalAccessRole(role);
            if (r == null || r.isEmpty()) r = resolveHeuristicAccessRole(u, display, null);
            if (!o.has("role") || o.optString("role", "user").equals("user") || (permissions != null && !permissions.trim().isEmpty())) o.put("role", r);
            if (permissions != null && !permissions.trim().isEmpty()) o.put("permissions", permissions.trim());
            if (!o.has("permissions")) o.put("permissions", "");
            o.put("enabled", enabled && (!o.has("enabled") || o.optBoolean("enabled", true)));
        } catch (Exception ignored) { }
    }

    private void renderAccessManagement(JSONObject state) throws Exception {
        content.removeAllViews();
        addHero("مدیریت دسترسی کاربران", "تنظیم دقیق نقش و مجوزها؛ تغییرات پس از ورود بعدی همان کاربر اعمال می‌شود.");
        addManualRefreshPanel("management", "بروزرسانی مدیریت", "آخرین بروزرسانی: " + lastRefreshText("management"), () -> loadAccessManagement());
        addAccessManagementIntro(state);
        addAccessRolesSection(state.optJSONArray("roles"));
        addAccessUsersSection(state.optJSONArray("users"));
    }

    private void addAccessManagementIntro(JSONObject state) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(navAccent("command"), 26), alpha(GOLD, 18), alpha(SURFACE, 250)}, GradientDrawable.Orientation.TL_BR, 26));
        c.addView(text("پنل مدیر دسترسی", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("کاربران از sys_users آتیران، جدول visitors و تنظیمات اختصاصی Meelano فراخوانی می‌شوند. می‌توانید برای کل نقش یا برای هر کاربر، مجوزهای جزئی مثل مشتریان، کالا، حضور، مرخصی، مودیان، دوربین، دزدگیر، AI و سلامت اتصال را روشن/خاموش کنید.", 10.5f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(dashboardMiniMetric("نقش‌ها", formatNumber(state == null || state.optJSONArray("roles") == null ? 0 : state.optJSONArray("roles").length()), "قابل تنظیم", INFO), dashboardMiniLp());
        row.addView(dashboardMiniMetric("کاربران", formatNumber(state == null || state.optJSONArray("users") == null ? 0 : state.optJSONArray("users").length()), "آتیران/ویزیتور", SUCCESS), dashboardMiniLp());
        row.addView(dashboardMiniMetric("اعمال", "ورود بعدی", "امن", GOLD), dashboardMiniLp());
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(10), 0, 0); c.addView(row, rp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addAccessRolesSection(JSONArray roles) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(GOLD, 22), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("مجوز پیش‌فرض نقش‌ها", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("برای هر نقش یک الگوی دسترسی بسازید؛ کاربرانی که مجوز اختصاصی ندارند از همین الگو استفاده می‌کنند.", 10.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        if (roles == null || roles.length() == 0) { addEmptyTo(c, "نقشی پیدا نشد."); }
        for (int i = 0; roles != null && i < roles.length(); i++) {
            JSONObject r = roles.optJSONObject(i); if (r == null) continue;
            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); row.setGravity(Gravity.CENTER_VERTICAL); row.setPadding(dp(8), dp(8), dp(8), dp(8));
            int accent = i % 3 == 0 ? INFO : (i % 3 == 1 ? SUCCESS : GOLD);
            row.setBackground(roundedStroke(alpha(accent, 15), 16, alpha(accent, 58)));
            LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL);
            copy.addView(text(r.optString("label", accessRoleLabel(r.optString("role"))), 12.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
            copy.addView(text("مجوزهای فعال: " + formatNumber(r.optInt("allowed", 0)), 10.0f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
            row.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
            Button edit = secondaryButton("تنظیم نقش"); edit.setTextSize(9.5f);
            final JSONObject rr = r;
            edit.setOnClickListener(v -> showPermissionDialog(true, rr.optString("role"), rr.optString("label"), rr.optString("role"), rr.optString("permissions", defaultPermissionString(rr.optString("role"))), true));
            row.addView(edit, new LinearLayout.LayoutParams(dp(104), dp(38)));
            LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2); rp.setMargins(0, dp(8), 0, 0); c.addView(row, rp);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addAccessUsersSection(JSONArray users) {
        LinearLayout c = card();
        c.setBackground(gradient(new int[]{alpha(SUCCESS, 20), alpha(INFO, 12), alpha(SURFACE, 248)}, GradientDrawable.Orientation.RIGHT_LEFT, 24));
        c.addView(text("کاربران و دسترسی اختصاصی", 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        c.addView(text("برای هر کاربر می‌توانید نقش، قفل/فعال بودن و مجوزهای اختصاصی را تعیین کنید. اگر مجوز اختصاصی خالی باشد، الگوی نقش اعمال می‌شود.", 10.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        if (users == null || users.length() == 0) addEmptyTo(c, "کاربری پیدا نشد؛ جدول sys_users یا visitors را بررسی کنید.");
        for (int i = 0; users != null && i < Math.min(120, users.length()); i++) {
            JSONObject u = users.optJSONObject(i); if (u == null) continue;
            addAccessUserRow(c, u, i);
        }
        if (users != null && users.length() > 120) c.addView(text("فقط ۱۲۰ کاربر اول نمایش داده شد؛ برای مدیریت بیشتر نقش‌های پیش‌فرض را تنظیم کنید.", 10.2f, WARNING, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, 0, 0, dp(12)); content.addView(c, lp);
    }

    private void addAccessUserRow(LinearLayout parent, JSONObject u, int index) {
        int accent = u.optBoolean("enabled", true) ? (index % 2 == 0 ? SUCCESS : INFO) : DANGER;
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(8), dp(8), dp(8), dp(8));
        box.setBackground(roundedStroke(alpha(accent, 14), 17, alpha(accent, 58)));
        LinearLayout head = new LinearLayout(this); head.setOrientation(LinearLayout.HORIZONTAL); head.setGravity(Gravity.CENTER_VERTICAL);
        TextView avatar = text(initials(u.optString("display", u.optString("username", "ک"))), 13, onColorFor(accent), Typeface.BOLD);
        avatar.setGravity(Gravity.CENTER); avatar.setBackground(gradient(new int[]{mix(accent, Color.WHITE, 0.20f), accent}, GradientDrawable.Orientation.TL_BR, 16));
        head.addView(avatar, new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout copy = new LinearLayout(this); copy.setOrientation(LinearLayout.VERTICAL); copy.setPadding(dp(8), 0, dp(8), 0);
        copy.addView(text(u.optString("display", "کاربر"), 12.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        copy.addView(text(u.optString("username", "") + " • " + u.optString("source", "") + " • " + u.optString("roleLabel", accessRoleLabel(u.optString("role"))) + " • " + (u.optBoolean("enabled", true) ? "فعال" : "قفل"), 9.4f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        head.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView badge = pill(formatNumber(u.optInt("allowed", 0)) + " مجوز", accent, false);
        head.addView(badge, new LinearLayout.LayoutParams(-2, -2));
        box.addView(head, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
        Button role = secondaryButton("نقش"); role.setTextSize(9.2f);
        Button perms = primaryButton("مجوزها"); perms.setTextSize(9.2f);
        Button enabled = u.optBoolean("enabled", true) ? secondaryButton("قفل") : primaryButton("فعال"); enabled.setTextSize(9.2f);
        final JSONObject user = u;
        role.setOnClickListener(v -> showRolePickerDialog(user));
        perms.setOnClickListener(v -> showPermissionDialog(false, user.optString("username"), user.optString("display"), user.optString("role", "user"), user.optString("permissions", "").trim().isEmpty() ? user.optString("effectivePermissions", defaultPermissionString(user.optString("role"))) : user.optString("permissions"), !user.optString("permissions", "").trim().isEmpty()));
        enabled.setOnClickListener(v -> saveUserEnabled(user.optString("username"), user.optString("display"), user.optString("source"), user.optString("sourceId"), user.optString("role", "user"), !user.optBoolean("enabled", true)));
        actions.addView(role, weightedButtonLp()); actions.addView(perms, weightedButtonLp()); actions.addView(enabled, weightedButtonLp());
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2); ap.setMargins(0, dp(8), 0, 0); box.addView(actions, ap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2); lp.setMargins(0, dp(8), 0, 0); parent.addView(box, lp);
    }

    private void showRolePickerDialog(JSONObject user) {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(14), dp(12), dp(14), dp(8));
        box.addView(text("انتخاب نقش برای " + user.optString("display", user.optString("username", "کاربر")), 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        final AlertDialog[] dlg = new AlertDialog[1];
        for (String[] role : roleCatalog()) {
            Button b = role[0].equals(user.optString("role")) ? primaryButton(role[1]) : secondaryButton(role[1]);
            b.setTextSize(10.2f);
            final String rk = role[0];
            b.setOnClickListener(v -> { if (dlg[0] != null) dlg[0].dismiss(); saveUserRole(user.optString("username"), user.optString("display"), user.optString("source"), user.optString("sourceId"), rk); });
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(42)); bp.setMargins(0, dp(7), 0, 0); box.addView(b, bp);
        }
        dlg[0] = new AlertDialog.Builder(this).setView(box).setNegativeButton("بستن", null).create();
        dlg[0].setOnShowListener(d -> styleMeelanoDialog(dlg[0], navAccent("command")));
        dlg[0].show();
    }

    private void showPermissionDialog(boolean roleTarget, String targetKey, String title, String roleKey, String existingPerms, boolean explicit) {
        Set<String> selected = permissionSet(existingPerms == null || existingPerms.trim().isEmpty() ? defaultPermissionString(roleKey) : existingPerms);
        LinearLayout outer = new LinearLayout(this); outer.setOrientation(LinearLayout.VERTICAL); outer.setPadding(dp(12), dp(10), dp(12), dp(6));
        outer.addView(text((roleTarget ? "مجوزهای نقش: " : "مجوزهای کاربر: ") + stringOr(title, targetKey), 15.5f, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        outer.addView(text(roleTarget ? "این الگو برای همه کاربران همین نقش اعمال می‌شود، مگر اینکه کاربر مجوز اختصاصی داشته باشد." : (explicit ? "این کاربر مجوز اختصاصی دارد." : "در حال حاضر از نقش ارث‌بری می‌کند؛ با ذخیره، مجوز اختصاصی ثبت می‌شود."), 10.2f, MUTED, Typeface.NORMAL), new LinearLayout.LayoutParams(-1, -2));
        LinearLayout quick = new LinearLayout(this); quick.setOrientation(LinearLayout.HORIZONTAL);
        Button defaults = secondaryButton("پیش‌فرض نقش"); defaults.setTextSize(8.6f);
        Button all = secondaryButton("همه"); all.setTextSize(8.6f);
        Button none = secondaryButton("هیچ‌کدام"); none.setTextSize(8.6f);
        quick.addView(defaults, weightedButtonLp()); quick.addView(all, weightedButtonLp()); quick.addView(none, weightedButtonLp());
        LinearLayout.LayoutParams qp = new LinearLayout.LayoutParams(-1, -2); qp.setMargins(0, dp(8), 0, dp(8)); outer.addView(quick, qp);
        ScrollView scroll = new ScrollView(this); styleVerticalScroll(scroll);
        LinearLayout list = new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL);
        List<CheckBox> checks = new ArrayList<>();
        final String[] group = {""};
        for (String[] perm : permissionCatalog()) {
            if (!perm[2].equals(group[0])) {
                group[0] = perm[2];
                TextView g = text(group[0], 12.2f, GOLD, Typeface.BOLD); g.setPadding(0, dp(8), 0, dp(2)); list.addView(g, new LinearLayout.LayoutParams(-1, -2));
            }
            CheckBox cb = new CheckBox(this);
            cb.setText(perm[1]); cb.setTextColor(TEXT); cb.setTextSize(11.2f); cb.setTypeface(Typeface.DEFAULT, Typeface.BOLD); cb.setChecked(selected.contains(perm[0])); cb.setTag(perm[0]);
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> { String k = String.valueOf(buttonView.getTag()); if (isChecked) selected.add(k); else selected.remove(k); });
            checks.add(cb); list.addView(cb, new LinearLayout.LayoutParams(-1, -2));
        }
        defaults.setOnClickListener(v -> { selected.clear(); selected.addAll(permissionSet(defaultPermissionString(roleKey))); for (CheckBox cb : checks) cb.setChecked(selected.contains(String.valueOf(cb.getTag()))); });
        all.setOnClickListener(v -> { selected.clear(); selected.addAll(permissionSet(allPermissionString())); for (CheckBox cb : checks) cb.setChecked(true); });
        none.setOnClickListener(v -> { selected.clear(); for (CheckBox cb : checks) cb.setChecked(false); });
        scroll.addView(list, new ScrollView.LayoutParams(-1, -2));
        outer.addView(scroll, new LinearLayout.LayoutParams(-1, dp(390)));
        AlertDialog dlg = new AlertDialog.Builder(this).setView(outer).setNegativeButton("بستن", null).setPositiveButton("ذخیره", null).create();
        dlg.setOnShowListener(d -> {
            styleMeelanoDialog(dlg, navAccent("command"));
            Button ok = dlg.getButton(AlertDialog.BUTTON_POSITIVE);
            if (ok != null) ok.setOnClickListener(v -> { String csv = permissionCsv(selected); dlg.dismiss(); if (roleTarget) saveRolePermissions(roleKey, title, csv); else saveUserPermissions(targetKey, title, roleKey, csv); });
        });
        dlg.show();
    }

    private void saveRolePermissions(String roleKey, String label, String permissions) {
        runDb(() -> { try (Connection c = openConnection()) { ensureAccessControlTables(c); try (PreparedStatement ps = c.prepareStatement("UPDATE dbo.meelano_access_roles SET role_label=?, permissions=?, updated_at=SYSDATETIME() WHERE role_key=?")) { ps.setString(1, stringOr(label, accessRoleLabel(roleKey))); ps.setString(2, permissions); ps.setString(3, canonicalAccessRole(roleKey)); ps.executeUpdate(); } } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this, "مجوزهای نقش ذخیره شد.", Toast.LENGTH_SHORT).show(); loadAccessManagement(); } @Override public void fail(Exception e){ showPageError("ذخیره نقش", e, () -> loadAccessManagement()); }});
    }

    private void saveUserRole(String username, String display, String source, String sourceId, String roleKey) {
        runDb(() -> { try (Connection c = openConnection()) { ensureAccessControlTables(c); upsertAccessUser(c, username, display, source, sourceId, canonicalAccessRole(roleKey), null, true); } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this, "نقش کاربر ذخیره شد.", Toast.LENGTH_SHORT).show(); loadAccessManagement(); } @Override public void fail(Exception e){ showPageError("ذخیره نقش کاربر", e, () -> loadAccessManagement()); }});
    }

    private void saveUserPermissions(String username, String display, String roleKey, String permissions) {
        runDb(() -> { try (Connection c = openConnection()) { ensureAccessControlTables(c); upsertAccessUser(c, username, display, "تنظیم اختصاصی", "", canonicalAccessRole(roleKey), permissions, true); } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this, "مجوزهای کاربر ذخیره شد.", Toast.LENGTH_SHORT).show(); loadAccessManagement(); } @Override public void fail(Exception e){ showPageError("ذخیره مجوز کاربر", e, () -> loadAccessManagement()); }});
    }

    private void saveUserEnabled(String username, String display, String source, String sourceId, String roleKey, boolean enabled) {
        runDb(() -> { try (Connection c = openConnection()) { ensureAccessControlTables(c); upsertAccessUser(c, username, display, source, sourceId, canonicalAccessRole(roleKey), null, enabled); } return "ok"; }, new DbCallback(){ @Override public void ok(String b){ Toast.makeText(MainActivity.this, enabled ? "کاربر فعال شد." : "کاربر قفل شد.", Toast.LENGTH_SHORT).show(); loadAccessManagement(); } @Override public void fail(Exception e){ showPageError("تغییر وضعیت کاربر", e, () -> loadAccessManagement()); }});
    }

    private void upsertAccessUser(Connection c, String username, String display, String source, String sourceId, String roleKey, String permissions, boolean enabled) throws Exception {
        String u = stringOr(username, "").trim();
        if (u.isEmpty()) throw new DbException("نام کاربری خالی است.");
        boolean exists;
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM dbo.meelano_access_users WHERE username=?")) { ps.setString(1, u); try (ResultSet r = ps.executeQuery()) { exists = r.next(); } }
        if (exists) {
            String sql = permissions == null ? "UPDATE dbo.meelano_access_users SET display_name=?, source=?, source_id=?, role_key=?, enabled=?, updated_at=SYSDATETIME() WHERE username=?" : "UPDATE dbo.meelano_access_users SET display_name=?, source=?, source_id=?, role_key=?, permissions=?, enabled=?, updated_at=SYSDATETIME() WHERE username=?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, stringOr(display, u)); ps.setString(2, stringOr(source, "تنظیم اختصاصی")); ps.setString(3, stringOr(sourceId, "")); ps.setString(4, canonicalAccessRole(roleKey));
                if (permissions == null) { ps.setBoolean(5, enabled); ps.setString(6, u); }
                else { ps.setString(5, permissions); ps.setBoolean(6, enabled); ps.setString(7, u); }
                ps.executeUpdate();
            }
        } else {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO dbo.meelano_access_users(username,display_name,source,source_id,role_key,permissions,enabled) VALUES(?,?,?,?,?,?,?)")) {
                ps.setString(1, u); ps.setString(2, stringOr(display, u)); ps.setString(3, stringOr(source, "تنظیم اختصاصی")); ps.setString(4, stringOr(sourceId, "")); ps.setString(5, canonicalAccessRole(roleKey)); ps.setString(6, permissions == null ? "" : permissions); ps.setBoolean(7, enabled); ps.executeUpdate();
            }
        }
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
        addQuickLoginSettingsCard();
        addLocalSecurityCard();
        addReminderSettingsCard();
        addConnectionHealthCard();
        addExperienceSettingsCard();
        addIconSystemCard();
        addHardwareToolsCard();
        addSecureDistributionCard();

        LinearLayout about = card();
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-1, -2);
        ap.setMargins(0, dp(12), 0, 0);
        about.addView(text("درباره نسخه", 16, TEXT, Typeface.BOLD), new LinearLayout.LayoutParams(-1, -2));
        TextView desc = text("Meelano Android Direct SQL v3.37.0\nاین نسخه گردش نهایی ویزیتور را کامل می‌کند: پیش‌فاکتورهای من، مسیر بازدید، ویرایش سبد، انتخاب قیمت ۱/۲، تخفیف، مالیات، شرایط تسویه، کنترل موجودی، صف آفلاین، PDF، گزارش پایان روز و رفع مشکل اتصال هنگام فعال بودن VPN.", 12, MUTED, Typeface.NORMAL);
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

    private String normalizeColumnName(String value) {
        if (value == null) return "";
        return value.toLowerCase(Locale.US)
                .replace('ي','ی').replace('ك','ک')
                .replace("_", "").replace("-", "").replace(" ", "")
                .replace(".", "").replace("/", "").trim();
    }

    private String resolveFlexible(Set<String> cols, String... candidates) {
        String exact = resolve(cols, candidates);
        if (exact != null) return exact;
        if (cols == null || candidates == null) return null;
        for (String candidate : candidates) {
            String n = normalizeColumnName(candidate);
            if (n.isEmpty()) continue;
            for (String col : cols) if (normalizeColumnName(col).equals(n)) return col;
        }
        return null;
    }

    private List<String> customerAddressColumns(Set<String> cols) {
        List<String> out = new ArrayList<>();
        String[] preferred = {"address", "Address", "ADDRESS", "addr", "ADDR", "adr", "Adress", "adress", "address1", "Address1", "Address_1", "customer_address", "CustomerAddress", "neshani", "Neshani", "NESHANI", "نشانی", "نشاني", "آدرس", "ادرس", "manzel", "Manzel", "mahale", "moadr", "MOADR", "MOADD", "moadd", "moneshan", "MONESHAN", "moneshani", "Mahal", "محل"};
        for (String p : preferred) {
            String col = resolveFlexible(cols, p);
            if (col != null && !out.contains(col)) out.add(col);
        }
        if (cols != null) for (String col : cols) {
            String n = normalizeColumnName(col);
            boolean looksAddress = n.contains("address") || n.contains("addr") || n.contains("adress") || n.contains("adr") || n.contains("neshan") || n.contains("نشانی") || n.contains("نشاني") || n.contains("آدرس") || n.contains("ادرس") || n.contains("manzel") || n.contains("makan") || n.contains("mahale") || n.contains("محل");
            if (looksAddress && !out.contains(col)) out.add(col);
        }
        return out;
    }

    private String customerAddressExpr(Set<String> cols, String alias) {
        List<String> list = customerAddressColumns(cols);
        if (list.isEmpty()) return "CAST(NULL AS nvarchar(500))";
        String prefix = alias == null || alias.trim().isEmpty() ? "" : alias + ".";
        List<String> exprs = new ArrayList<>();
        for (int i = 0; i < Math.min(6, list.size()); i++) {
            String col = list.get(i);
            exprs.add("NULLIF(LTRIM(RTRIM(TRY_CONVERT(nvarchar(500)," + prefix + "[" + col + "]))),N'')");
        }
        return "COALESCE(" + join(exprs, ",") + ",CAST(NULL AS nvarchar(500)))";
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

    private boolean privacyMode() {
        return false;
    }

    private void togglePrivacyMode() {
        Toast.makeText(this, "مدیریت دسترسی کاربران جایگزین دکمه قبلی شده است.", Toast.LENGTH_SHORT).show();
    }

    private String money(Object value) {
        if (privacyMode()) return "•••• ریال";
        return formatNumber(value) + " ریال";
    }

    private String compactMoney(Object value) {
        if (privacyMode()) return "••••";
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
        return "ارتباط برقرار نشد. اینترنت، دسترسی شبکه، روشن بودن سرور و مجوز اتصال SQL را بررسی کنید." + sqlNetworkHint();
    }

    private void addEmptyTo(LinearLayout parent, String message) {
        LinearLayout c = card();
        c.setGravity(Gravity.CENTER_HORIZONTAL);
        int accent = themeAccent(message);
        c.setBackground(gradient(new int[]{alpha(Color.WHITE, isLightTheme() ? 70 : 18), alpha(accent, isLightTheme() ? 20 : 32), alpha(SURFACE, 248)}, GradientDrawable.Orientation.TL_BR, 26));
        FrameLayout art = new FrameLayout(this);
        TextView halo = report3dIcon(message != null && message.contains("جستجو") ? "⌕" : "◇", accent);
        art.addView(halo, new FrameLayout.LayoutParams(dp(64), dp(64), Gravity.CENTER));
        View shadow = new View(this);
        shadow.setBackground(roundedStroke(alpha(accent, 18), 999, alpha(accent, 0)));
        FrameLayout.LayoutParams sh = new FrameLayout.LayoutParams(dp(96), dp(14), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        art.addView(shadow, sh);
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(dp(112), dp(82));
        ap.gravity = Gravity.CENTER_HORIZONTAL;
        c.addView(art, ap);
        TextView t = text(message, 13.0f, TEXT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER);
        t.setLineSpacing(dp(2), 1.04f);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.setMargins(0, dp(6), 0, 0);
        c.addView(t, tp);
        TextView sub = text("فیلتر، جستجو یا تازه‌سازی را تغییر دهید؛ میلو داده را دوباره مرتب و تمیز نمایش می‌دهد.", 10.5f, MUTED, Typeface.NORMAL);
        sub.setGravity(Gravity.CENTER);
        sub.setLineSpacing(dp(2), 1.04f);
        c.addView(sub, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10));
        parent.addView(c, lp);
    }

    private static class UserSession {
        final Integer userId;
        final Integer visitorId;
        final String userName;
        final String accessRole;
        final String permissions;
        UserSession(Integer userId, Integer visitorId, String userName) {
            this(userId, visitorId, userName, "", "");
        }
        UserSession(Integer userId, Integer visitorId, String userName, String accessRole) {
            this(userId, visitorId, userName, accessRole, "");
        }
        UserSession(Integer userId, Integer visitorId, String userName, String accessRole, String permissions) {
            this.userId = userId;
            this.visitorId = visitorId;
            this.userName = userName;
            this.accessRole = accessRole == null ? "" : accessRole;
            this.permissions = permissions == null ? "" : permissions;
        }
    }

    private static class AccessProfile {
        final String role;
        final String permissions;
        final boolean enabled;
        AccessProfile(String role, String permissions, boolean enabled) {
            this.role = role == null ? "user" : role;
            this.permissions = permissions == null ? "" : permissions;
            this.enabled = enabled;
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
        } else if (requestCode == REQ_TTS_CHECK) {
            ttsReady = configurePersianTts();
            if (!ttsReady && resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) openTtsInstaller();
            else if (ttsReady && pendingTtsText != null && !pendingTtsText.trim().isEmpty()) {
                String pending = pendingTtsText;
                pendingTtsText = "";
                speakAssistantText(pending);
            }
        } else if (requestCode == REQ_CHAT_ATTACHMENT && resultCode == RESULT_OK && data != null) {
            handleChatAttachment(data.getData());
        } else if (requestCode == REQ_BARCODE_SCAN && resultCode == RESULT_OK && data != null) {
            String code = data.getStringExtra("SCAN_RESULT");
            if (code == null || code.trim().isEmpty()) code = data.getStringExtra("SCAN_RESULT_BYTES");
            Toast.makeText(this, "کد اسکن‌شده: " + stringOr(code, "—"), Toast.LENGTH_LONG).show();
            if (assistantInput != null) {
                assistantInput.setText("این کد/بارکد را در کالاها بررسی کن: " + stringOr(code, ""));
                assistantInput.setSelection(assistantInput.getText().length());
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
