package ir.meelano.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Small reusable UI kit for the existing imperative View code.
 * Keeps typography, cards, buttons and chips visually consistent across pages.
 */
final class MeelanoUiKit {
    private static final Map<String, Typeface> FONT_CACHE = new HashMap<>();
    private MeelanoUiKit() {}

    static Typeface font(Context context, int style) {
        String asset = style == Typeface.BOLD ? "fonts/Vazirmatn-Bold.ttf" : (style == Typeface.ITALIC ? "fonts/Vazirmatn-Medium.ttf" : "fonts/Vazirmatn-Regular.ttf");
        try {
            synchronized (FONT_CACHE) {
                Typeface cached = FONT_CACHE.get(asset);
                if (cached == null && context != null) {
                    cached = Typeface.createFromAsset(context.getAssets(), asset);
                    FONT_CACHE.put(asset, cached);
                }
                if (cached != null) return cached;
            }
        } catch (Exception ignored) { }
        return Typeface.create(Typeface.DEFAULT, style);
    }

    static void applyText(TextView view, Context context, int style) {
        if (view == null) return;
        view.setTypeface(font(context, style));
        view.setIncludeFontPadding(true);
        view.setLineSpacing(0, 1.06f);
    }

    static GradientDrawable cardBg(Context context, MeelanoTheme.Tokens t, int accent, boolean selected) {
        int hero = mix(accent, t.primary2, t.light() ? .10f : .16f);
        int top = mix(t.surface, hero, t.light() ? .035f : .10f);
        int mid = mix(t.surface2, hero, t.light() ? .080f : .17f);
        int bottom = mix(t.navy, hero, t.light() ? .020f : .12f);
        GradientDrawable d = gradient(new int[]{alpha(top, 252), alpha(mid, 248), alpha(bottom, 252)}, GradientDrawable.Orientation.TL_BR, t.cardRadius, context);
        d.setStroke(dp(context, selected ? 2 : 1), alpha(mix(hero, Color.WHITE, t.light() ? .38f : .24f), selected ? 175 : (t.light() ? 118 : 92)));
        return d;
    }

    static GradientDrawable buttonBg(Context context, MeelanoTheme.Tokens t, int accent, boolean primary) {
        int top = primary ? mix(accent, Color.WHITE, t.light() ? .25f : .14f) : alpha(mix(t.surface, accent, t.light() ? .04f : .10f), 245);
        int mid = primary ? accent : alpha(mix(t.surface2, accent, t.light() ? .075f : .14f), 242);
        int bottom = primary ? mix(accent, t.navy, t.light() ? .10f : .26f) : alpha(mix(t.navy, accent, t.light() ? .025f : .12f), 244);
        GradientDrawable d = gradient(new int[]{top, mid, bottom}, GradientDrawable.Orientation.TL_BR, primary ? t.buttonRadius : 20f, context);
        d.setStroke(dp(context, 1), alpha(mix(accent, Color.WHITE, t.light() ? .42f : .28f), primary ? 150 : 94));
        return d;
    }

    static Button actionButton(Context context, String label, MeelanoTheme.Tokens t, int accent, boolean primary, boolean compact) {
        Button b = new Button(context);
        b.setAllCaps(false);
        b.setText(label == null ? "" : label);
        b.setTextSize(compact ? 10.7f : 11.6f);
        b.setTextColor(primary ? onColorFor(accent) : t.text);
        b.setMinHeight(dp(context, 44));
        b.setPadding(dp(context, 9), 0, dp(context, 9), dp(context, 1));
        b.setBackground(buttonBg(context, t, accent, primary));
        applyText(b, context, Typeface.BOLD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            b.setElevation(dp(context, primary ? t.buttonElevation : 3));
            b.setLetterSpacing(0.01f);
        }
        return b;
    }

    static int onColorFor(int color) {
        double luminance = (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color));
        return luminance > 168 ? Color.rgb(18, 28, 52) : Color.WHITE;
    }

    static GradientDrawable gradient(int[] colors, GradientDrawable.Orientation orientation, float radius, Context context) {
        GradientDrawable d = new GradientDrawable(orientation, colors);
        d.setCornerRadius(dp(context, radius));
        return d;
    }

    static int alpha(int color, int a) { return Color.argb(Math.max(0, Math.min(255, a)), Color.red(color), Color.green(color), Color.blue(color)); }
    static int mix(int c1, int c2, float ratio) {
        float r = Math.max(0f, Math.min(1f, ratio));
        return Color.rgb((int)(Color.red(c1) * (1 - r) + Color.red(c2) * r), (int)(Color.green(c1) * (1 - r) + Color.green(c2) * r), (int)(Color.blue(c1) * (1 - r) + Color.blue(c2) * r));
    }
    static int dp(Context context, float v) { return context == null ? (int)v : Math.round(v * context.getResources().getDisplayMetrics().density); }
}
