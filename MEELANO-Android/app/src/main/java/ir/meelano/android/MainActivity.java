package ir.meelano.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String PREFS = "meelano_android";
    private static final String KEY_URL = "web_url";
    private static final String DEFAULT_URL = "";

    private static final int NAVY = Color.rgb(8, 10, 17);
    private static final int SURFACE = Color.rgb(18, 22, 31);
    private static final int SURFACE_2 = Color.rgb(24, 30, 42);
    private static final int SURFACE_3 = Color.rgb(31, 38, 52);
    private static final int GOLD = Color.rgb(231, 177, 90);
    private static final int GOLD_2 = Color.rgb(242, 207, 138);
    private static final int TEXT = Color.rgb(246, 248, 252);
    private static final int MUTED = Color.rgb(154, 166, 183);
    private static final int BORDER = Color.argb(36, 255, 255, 255);

    private SharedPreferences prefs;
    private WebView webView;
    private FrameLayout stage;
    private ProgressBar progress;
    private TextView status;
    private TextView title;
    private TextView urlLabel;
    private View setupPanel;
    private View errorPanel;
    private String currentUrl = "";

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        buildShell();
        loadConfiguredUrl();
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int alpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
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

    private TextView label(String text, float size, int color, int style) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextColor(color);
        v.setTextSize(size);
        v.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        v.setIncludeFontPadding(true);
        v.setTypeface(Typeface.DEFAULT, style);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            v.setTextDirection(View.TEXT_DIRECTION_RTL);
        }
        return v;
    }

    private void buildShell() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(NAVY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(10), dp(6), dp(10), dp(6));
        bar.setBackground(gradient(new int[]{Color.rgb(10, 13, 21), Color.rgb(20, 24, 34)}, GradientDrawable.Orientation.LEFT_RIGHT, 0));

        ImageView logo = new ImageView(this);
        logo.setImageResource(ir.meelano.android.R.drawable.meelano_3d);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        logo.setPadding(dp(3), dp(3), dp(3), dp(3));
        logo.setBackground(roundedStroke(alpha(GOLD, 18), 16, alpha(GOLD, 60)));
        bar.addView(logo, new LinearLayout.LayoutParams(dp(46), dp(46)));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(10), 0, dp(10), 0);
        title = label("MEELANO", 16, TEXT, Typeface.BOLD);
        status = label("نسخه نصب‌شدنی اندروید", 11, MUTED, Typeface.NORMAL);
        urlLabel = label("", 10, alpha(TEXT, 150), Typeface.NORMAL);
        titles.addView(title, new LinearLayout.LayoutParams(-1, 0, 1f));
        titles.addView(status, new LinearLayout.LayoutParams(-1, 0, 1f));
        titles.addView(urlLabel, new LinearLayout.LayoutParams(-1, 0, 1f));
        bar.addView(titles, new LinearLayout.LayoutParams(0, dp(48), 1f));

        TextView home = iconButton("⌂", "صفحه شروع");
        home.setOnClickListener(v -> showSetup(false));
        bar.addView(home, new LinearLayout.LayoutParams(dp(42), dp(42)));

        TextView back = iconButton("‹", "بازگشت");
        back.setOnClickListener(v -> {
            if (webView != null && webView.getVisibility() == View.VISIBLE && webView.canGoBack()) webView.goBack();
        });
        bar.addView(back, new LinearLayout.LayoutParams(dp(42), dp(42)));

        TextView refresh = iconButton("↻", "بازخوانی");
        refresh.setOnClickListener(v -> {
            if (webView != null && webView.getVisibility() == View.VISIBLE) webView.reload();
            else loadConfiguredUrl();
        });
        bar.addView(refresh, new LinearLayout.LayoutParams(dp(42), dp(42)));

        TextView settings = iconButton("⚙", "تنظیمات اتصال");
        settings.setOnClickListener(v -> showSettings());
        bar.addView(settings, new LinearLayout.LayoutParams(dp(42), dp(42)));

        root.addView(bar, new LinearLayout.LayoutParams(-1, dp(62)));

        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100);
        progress.setProgress(0);
        progress.setVisibility(View.GONE);
        root.addView(progress, new LinearLayout.LayoutParams(-1, dp(3)));

        stage = new FrameLayout(this);
        stage.setBackgroundColor(NAVY);
        webView = new WebView(this);
        configureWebView();
        webView.setVisibility(View.GONE);
        stage.addView(webView, new FrameLayout.LayoutParams(-1, -1));
        root.addView(stage, new LinearLayout.LayoutParams(-1, 0, 1f));

        setContentView(root);
    }

    private TextView iconButton(String glyph, String description) {
        TextView button = new TextView(this);
        button.setText(glyph);
        button.setTextSize(22);
        button.setTextColor(GOLD_2);
        button.setGravity(Gravity.CENTER);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setBackground(roundedStroke(alpha(Color.WHITE, 10), 14, alpha(GOLD, 42)));
        button.setContentDescription(description);
        button.setClickable(true);
        button.setFocusable(true);
        return button;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setSupportMultipleWindows(false);
        s.setMediaPlaybackRequiresUserGesture(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        webView.setBackgroundColor(NAVY);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
                progress.setVisibility(newProgress >= 100 ? View.GONE : View.VISIBLE);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleExternalUrl(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleExternalUrl(Uri.parse(url));
            }

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                currentUrl = url == null ? currentUrl : url;
                removeErrorPanel();
                status.setText("در حال اتصال…");
                urlLabel.setText(shortUrl(currentUrl));
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                currentUrl = url == null ? currentUrl : url;
                status.setText("اتصال فعال");
                urlLabel.setText(shortUrl(currentUrl));
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError err) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && req.isForMainFrame()) {
                    showOffline("ارتباط با MEELANO برقرار نشد");
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showOffline("ارتباط با MEELANO برقرار نشد");
            }
        });

        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> openUri(Uri.parse(url), "امکان باز کردن خروجی وجود ندارد."));
    }

    private boolean handleExternalUrl(Uri uri) {
        if (uri == null || uri.getScheme() == null) return false;
        String scheme = uri.getScheme().toLowerCase();
        if ("http".equals(scheme) || "https".equals(scheme)) return false;
        openUri(uri, "امکان باز کردن این لینک وجود ندارد.");
        return true;
    }

    private void openUri(Uri uri, String failMessage) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception ignored) {
            Toast.makeText(this, failMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadConfiguredUrl() {
        String url = prefs.getString(KEY_URL, DEFAULT_URL).trim();
        if (url.isEmpty()) {
            showSetup(false);
        } else {
            loadUrl(url);
        }
    }

    private String normalizeUrl(String value) {
        String url = value == null ? "" : value.trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "https://" + url;
        return url;
    }

    private String shortUrl(String value) {
        if (value == null || value.trim().isEmpty()) return "";
        try {
            Uri u = Uri.parse(value);
            String host = u.getHost();
            return host == null ? value : host;
        } catch (Exception ignored) {
            return value;
        }
    }

    private void loadUrl(String rawUrl) {
        String url = normalizeUrl(rawUrl);
        currentUrl = url;
        removeSetupPanel();
        removeErrorPanel();
        status.setText("در حال اتصال…");
        urlLabel.setText(shortUrl(url));
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
    }

    private void removeSetupPanel() {
        if (setupPanel != null) {
            stage.removeView(setupPanel);
            setupPanel = null;
        }
    }

    private void removeErrorPanel() {
        if (errorPanel != null) {
            stage.removeView(errorPanel);
            errorPanel = null;
        }
    }

    private void showSetup(boolean keepWebVisible) {
        removeSetupPanel();
        removeErrorPanel();
        if (!keepWebVisible) webView.setVisibility(View.GONE);
        status.setText("تنظیم اتصال");
        urlLabel.setText("آدرس HTTPS یا شبکه داخلی را وارد کنید");

        FrameLayout backdrop = new FrameLayout(this);
        backdrop.setBackground(gradient(new int[]{Color.rgb(7, 9, 16), Color.rgb(15, 18, 28), Color.rgb(8, 10, 17)}, GradientDrawable.Orientation.TOP_BOTTOM, 0));

        View haloTop = new View(this);
        GradientDrawable h1 = new GradientDrawable();
        h1.setShape(GradientDrawable.OVAL);
        h1.setColor(alpha(GOLD, 26));
        haloTop.setBackground(h1);
        FrameLayout.LayoutParams haloTopParams = new FrameLayout.LayoutParams(dp(220), dp(220), Gravity.TOP | Gravity.RIGHT);
        haloTopParams.setMargins(0, dp(-70), dp(-80), 0);
        backdrop.addView(haloTop, haloTopParams);

        View haloBottom = new View(this);
        GradientDrawable h2 = new GradientDrawable();
        h2.setShape(GradientDrawable.OVAL);
        h2.setColor(Color.argb(22, 93, 196, 255));
        haloBottom.setBackground(h2);
        FrameLayout.LayoutParams haloBottomParams = new FrameLayout.LayoutParams(dp(240), dp(240), Gravity.BOTTOM | Gravity.LEFT);
        haloBottomParams.setMargins(dp(-90), 0, 0, dp(-70));
        backdrop.addView(haloBottom, haloBottomParams);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        outer.setGravity(Gravity.CENTER);
        outer.setPadding(dp(18), dp(20), dp(18), dp(24));
        scroll.addView(outer, new ScrollView.LayoutParams(-1, -1));

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setPadding(dp(22), dp(24), dp(22), dp(22));
        card.setBackground(roundedStroke(alpha(SURFACE, 242), 30, alpha(GOLD, 58)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) card.setElevation(dp(14));
        outer.addView(card, new LinearLayout.LayoutParams(-1, -2));

        ImageView logo = new ImageView(this);
        logo.setImageResource(ir.meelano.android.R.drawable.meelano_3d);
        logo.setAdjustViewBounds(true);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        card.addView(logo, new LinearLayout.LayoutParams(dp(126), dp(126)));

        TextView h = label("MEELANO Android", 25, TEXT, Typeface.BOLD);
        h.setGravity(Gravity.CENTER);
        card.addView(h, new LinearLayout.LayoutParams(-1, -2));

        TextView sub = label("نسخه نصب‌شدنی، لوکس و امن برای سامانه مدیریتی آتیران", 13, MUTED, Typeface.NORMAL);
        sub.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(-1, -2);
        subLp.setMargins(0, dp(7), 0, dp(18));
        card.addView(sub, subLp);

        TextView note = label("آدرس وب یا API منتشرشده MEELANO را وارد کنید. برای استفاده اینترنتی بهتر است آدرس HTTPS باشد؛ در شبکه داخلی آدرس IP نیز پشتیبانی می‌شود.", 12, alpha(TEXT, 205), Typeface.NORMAL);
        note.setGravity(Gravity.CENTER);
        note.setLineSpacing(dp(2), 1.05f);
        note.setPadding(dp(12), dp(12), dp(12), dp(12));
        note.setBackground(roundedStroke(alpha(GOLD, 18), 18, alpha(GOLD, 44)));
        card.addView(note, new LinearLayout.LayoutParams(-1, -2));

        Space gap = new Space(this);
        card.addView(gap, new LinearLayout.LayoutParams(1, dp(16)));

        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(prefs.getString(KEY_URL, ""));
        input.setSelectAllOnFocus(true);
        input.setHint("https://meelano.example.com");
        input.setHintTextColor(Color.rgb(110, 119, 134));
        input.setTextColor(TEXT);
        input.setTextSize(14);
        input.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        input.setPadding(dp(14), 0, dp(14), 0);
        input.setBackground(roundedStroke(SURFACE_2, 17, alpha(Color.WHITE, 38)));
        card.addView(input, new LinearLayout.LayoutParams(-1, dp(54)));

        LinearLayout examples = new LinearLayout(this);
        examples.setOrientation(LinearLayout.HORIZONTAL);
        examples.setGravity(Gravity.CENTER);
        examples.setPadding(0, dp(12), 0, dp(4));
        examples.addView(chip("https://your-domain.com"));
        examples.addView(chip("http://192.168.1.150:5000"));
        card.addView(examples, new LinearLayout.LayoutParams(-1, -2));

        Button save = new Button(this);
        save.setText("اتصال و ورود به MEELANO");
        save.setTextColor(Color.rgb(20, 16, 10));
        save.setTextSize(14);
        save.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        save.setAllCaps(false);
        save.setBackground(gradient(new int[]{GOLD_2, GOLD}, GradientDrawable.Orientation.LEFT_RIGHT, 18));
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, dp(54));
        sp.setMargins(0, dp(12), 0, 0);
        card.addView(save, sp);
        save.setOnClickListener(v -> {
            String url = normalizeUrl(input.getText().toString());
            if (url.length() < 10 || !(url.startsWith("http://") || url.startsWith("https://"))) {
                input.setError("آدرس معتبر وارد کنید");
                return;
            }
            prefs.edit().putString(KEY_URL, url).apply();
            loadUrl(url);
        });

        TextView footer = label("اطلاعات ورود و رمز SQL داخل برنامه ذخیره نمی‌شود؛ اپ فقط پوسته امن WebView برای سرویس MEELANO است.", 11, MUTED, Typeface.NORMAL);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(14), 0, 0);
        card.addView(footer, new LinearLayout.LayoutParams(-1, -2));

        backdrop.addView(scroll, new FrameLayout.LayoutParams(-1, -1));
        setupPanel = backdrop;
        stage.addView(setupPanel, new FrameLayout.LayoutParams(-1, -1));
    }

    private TextView chip(String text) {
        TextView chip = label(text, 10, alpha(TEXT, 205), Typeface.NORMAL);
        chip.setGravity(Gravity.CENTER);
        chip.setSingleLine(true);
        chip.setPadding(dp(9), dp(6), dp(9), dp(6));
        chip.setBackground(roundedStroke(alpha(Color.WHITE, 10), 999, alpha(Color.WHITE, 28)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        chip.setLayoutParams(lp);
        return chip;
    }

    private void showSettings() {
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(prefs.getString(KEY_URL, currentUrl));
        input.setSelectAllOnFocus(true);
        input.setHint("https://meelano.example.com");
        input.setPadding(dp(14), 0, dp(14), 0);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("تنظیمات اتصال MEELANO")
                .setMessage("آدرس وب/سرویس را تغییر دهید. برای استفاده امن در اینترنت از HTTPS استفاده کنید.")
                .setView(input)
                .setNegativeButton("انصراف", null)
                .setNeutralButton("صفحه شروع", (d, w) -> showSetup(false))
                .setPositiveButton("ذخیره", null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String url = normalizeUrl(input.getText().toString());
            if (url.length() < 10) {
                input.setError("آدرس معتبر وارد کنید");
                return;
            }
            prefs.edit().putString(KEY_URL, url).apply();
            dialog.dismiss();
            loadUrl(url);
        }));
        dialog.show();
    }

    private void showOffline(String message) {
        progress.setVisibility(View.GONE);
        status.setText("اتصال برقرار نشد");
        removeSetupPanel();
        removeErrorPanel();
        webView.setVisibility(View.GONE);

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_HORIZONTAL);
        panel.setPadding(dp(24), dp(32), dp(24), dp(24));
        panel.setBackground(gradient(new int[]{NAVY, Color.rgb(13, 17, 26)}, GradientDrawable.Orientation.TOP_BOTTOM, 0));

        ImageView logo = new ImageView(this);
        logo.setImageResource(ir.meelano.android.R.drawable.meelano_3d);
        panel.addView(logo, new LinearLayout.LayoutParams(dp(108), dp(108)));

        TextView h = label(message, 21, TEXT, Typeface.BOLD);
        h.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.setMargins(0, dp(12), 0, dp(6));
        panel.addView(h, hp);

        TextView detail = label("آدرس فعلی: " + (currentUrl == null || currentUrl.isEmpty() ? "تنظیم نشده" : currentUrl) + "\nاتصال اینترنت، VPN، گواهی HTTPS یا روشن بودن سرور MEELANO را بررسی کنید.", 12, MUTED, Typeface.NORMAL);
        detail.setGravity(Gravity.CENTER);
        detail.setLineSpacing(dp(3), 1.05f);
        detail.setPadding(dp(14), dp(14), dp(14), dp(14));
        detail.setBackground(roundedStroke(SURFACE, 18, BORDER));
        panel.addView(detail, new LinearLayout.LayoutParams(-1, -2));

        Button retry = new Button(this);
        retry.setText("تلاش دوباره");
        retry.setAllCaps(false);
        retry.setTextColor(Color.rgb(20, 16, 10));
        retry.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        retry.setBackground(gradient(new int[]{GOLD_2, GOLD}, GradientDrawable.Orientation.LEFT_RIGHT, 17));
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, dp(52));
        rp.setMargins(0, dp(18), 0, 0);
        panel.addView(retry, rp);
        retry.setOnClickListener(v -> loadUrl(currentUrl));

        Button change = new Button(this);
        change.setText("تغییر آدرس اتصال");
        change.setAllCaps(false);
        change.setTextColor(TEXT);
        change.setBackground(roundedStroke(SURFACE_2, 17, alpha(GOLD, 58)));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, dp(52));
        cp.setMargins(0, dp(10), 0, 0);
        panel.addView(change, cp);
        change.setOnClickListener(v -> showSetup(false));

        errorPanel = panel;
        stage.addView(errorPanel, new FrameLayout.LayoutParams(-1, -1));
        Toast.makeText(this, message + " — تنظیمات اتصال را بررسی کنید.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (setupPanel != null && currentUrl != null && !currentUrl.isEmpty()) {
            loadUrl(currentUrl);
            return;
        }
        if (errorPanel != null) {
            showSetup(false);
            return;
        }
        if (webView != null && webView.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
