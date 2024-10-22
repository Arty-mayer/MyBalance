package com.example.mybalance.Utils;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;

import com.example.mybalance.MainActivity;

import java.util.Locale;

public class AppSettings {
    public static String appLang;

    public AppSettings(Context context) {

        Locale appLocale;
        appLocale = context.getResources().getConfiguration().getLocales().get(0);
        AppSettings.appLang = appLocale.getLanguage();
    }
}


