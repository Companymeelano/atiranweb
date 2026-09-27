package ir.meelano.android;

import android.graphics.Color;

/**
 * Central semantic design tokens for the native Java/View implementation.
 * This class is intentionally dependency-light so Phase 1 can improve the
 * current architecture without Compose/AndroidX migration.
 */
final class MeelanoTheme {
    private MeelanoTheme() {}

    static final class Tokens {
        final int navy;
        final int surface;
        final int surface2;
        final int primary;
        final int primary2;
        final int success;
        final int info;
        final int warning;
        final int danger;
        final int text;
        final int muted;
        final int border;
        final float cardRadius;
        final float buttonRadius;
        final float chipRadius;
        final int cardElevation;
        final int buttonElevation;

        Tokens(int navy, int surface, int surface2, int primary, int primary2,
               int success, int info, int warning, int danger, int text, int muted, int border,
               float cardRadius, float buttonRadius, float chipRadius, int cardElevation, int buttonElevation) {
            this.navy = navy;
            this.surface = surface;
            this.surface2 = surface2;
            this.primary = primary;
            this.primary2 = primary2;
            this.success = success;
            this.info = info;
            this.warning = warning;
            this.danger = danger;
            this.text = text;
            this.muted = muted;
            this.border = border;
            this.cardRadius = cardRadius;
            this.buttonRadius = buttonRadius;
            this.chipRadius = chipRadius;
            this.cardElevation = cardElevation;
            this.buttonElevation = buttonElevation;
        }

        boolean light() {
            return Color.red(navy) + Color.green(navy) + Color.blue(navy) > 420;
        }
    }

    static Tokens runtime(int navy, int surface, int surface2, int primary, int primary2,
                          int success, int info, int warning, int danger, int text, int muted, int border) {
        boolean light = Color.red(navy) + Color.green(navy) + Color.blue(navy) > 420;
        return new Tokens(navy, surface, surface2, primary, primary2, success, info, warning, danger,
                text, muted, border, light ? 30f : 26f, 999f, 999f, light ? 8 : 10, light ? 5 : 6);
    }
}
