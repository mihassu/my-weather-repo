package com.android.myweather_v2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

public class OptionsActivity extends AppCompatActivity {

    public final static String OPTIONS_CONDITIONS = "Optionsconditions";
    public final static String SHOWWIND_OPTION = "Showwind";
    public final static String SHOWPRESSURE_OPTION = "Showpressure";

    public SharedPreferences condOptions;

    public CheckBox chB1;
    public CheckBox chB2;

    private boolean showWind = false;
    private boolean showPressure = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);

        chB1 = findViewById(R.id.checkBox1);
        chB2 = findViewById(R.id.checkBox2);

        //Получить файл опций по ключу OPTIONS_CONDITIONS. Если файла нет, то он будет создан
        condOptions = getSharedPreferences(OPTIONS_CONDITIONS, MODE_PRIVATE);

    }


    @Override
    protected void onPause() {
        super.onPause();

        //Соханить состояния CheckBoxes
        SharedPreferences.Editor editor = condOptions.edit();
        editor.putBoolean(SHOWWIND_OPTION, chB1.isChecked());
        editor.putBoolean(SHOWPRESSURE_OPTION, chB2.isChecked());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(condOptions.contains(SHOWPRESSURE_OPTION) && condOptions.contains(SHOWWIND_OPTION)) {
            showWind = condOptions.getBoolean(SHOWWIND_OPTION, false);
            showPressure = condOptions.getBoolean(SHOWPRESSURE_OPTION, false);
        }

        chB1.setChecked(showWind);
        chB2.setChecked(showPressure);
    }


}
