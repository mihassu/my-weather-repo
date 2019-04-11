package com.android.myweather_v2;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Загрузить настройки
        addPreferencesFromResource(R.xml.preferences);

    }
}
