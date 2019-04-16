package com.android.myweather_v2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.myweather_v2.database.CallBackWeather;
import com.android.myweather_v2.database.NoteDataReader;

import static com.android.myweather_v2.MainActivity.CITY_NAME;
import static com.android.myweather_v2.MainActivity.CONDITIONS;
import static com.android.myweather_v2.MainActivity.CURRENT_CITY_POS;

public class WeatherInfoService extends IntentService {

    public static final String TEMPERATURE_VALUE = "temperature_value";
    public static final String WIND_VALUE = "wind_value";
    public static final String PRESSURE_VALUE = "pressure_value";

    public final String TAG = "<<WeatherService>>";
    public static final String ACTION_MYINTENTSERVICE = "my.RESPONSE";

    private static CallBackWeather cityFromBase;

    public static void setCityFromBase(CallBackWeather cityFromBase) {
        WeatherInfoService.cityFromBase = cityFromBase;
    }

    public WeatherInfoService() {
        super("WeatherInfoService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Получаем значение из activity которая запустила сервис
        String city = intent.getStringExtra(CITY_NAME);
        int index = intent.getIntExtra(CURRENT_CITY_POS, 0);
        Log.e(TAG, "onHandleIntent " + city);

        WeatherInfo weatherInCity = new WeatherInformerByService();

        //Отправляем результат в activity
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_MYINTENTSERVICE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);

//        responseIntent.putExtra(TEMPERATURE_VALUE, weatherInCity.getTemperatureByCity(city));
        responseIntent.putExtra(TEMPERATURE_VALUE, cityFromBase.callback(index));

        responseIntent.putExtra(WIND_VALUE, weatherInCity.getWindByCity(city));
        responseIntent.putExtra(PRESSURE_VALUE, weatherInCity.getPressureByCity(city));

        sendBroadcast(responseIntent);
    }
}
