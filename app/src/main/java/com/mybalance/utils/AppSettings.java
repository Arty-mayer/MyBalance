package com.mybalance.utils;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.content.Context;

import java.util.Locale;

public class AppSettings {
    public static String appLang;

    public AppSettings(Context context) {

        Locale appLocale;
        appLocale = context.getResources().getConfiguration().getLocales().get(0);
        AppSettings.appLang = appLocale.getLanguage();
    }
}


