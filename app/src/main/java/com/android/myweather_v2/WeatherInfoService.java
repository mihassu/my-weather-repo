package com.android.myweather_v2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.myweather_v2.database.CallBackWeather;
import com.android.myweather_v2.database.NoteDataReader;
import com.android.myweather_v2.interfaces.OpenWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.android.myweather_v2.models.WeatherRequest;


import static com.android.myweather_v2.MainActivity.CITY_NAME;


public class WeatherInfoService extends IntentService {

    public static final String TEMPERATURE_VALUE = "temperature_value";
    public static final String WIND_VALUE = "wind_value";
    public static final String PRESSURE_VALUE = "pressure_value";

    public final String TAG = "<<WeatherService>>";
    public static final String ACTION_MYINTENTSERVICE = "my.RESPONSE";

    private static CallBackWeather valuesFromBase;
    private static CallBackWeather getAllCitiesInBase;

    private OpenWeather openWeather;


    public WeatherInfoService() {
        super("WeatherInfoService");
    }


    public static void setValuesFromBase(CallBackWeather valuesFromBase) {
        WeatherInfoService.valuesFromBase = valuesFromBase;
    }

    public static void setGetAllCitiesInBase(CallBackWeather getAllCitiesInBase) {
        WeatherInfoService.getAllCitiesInBase = getAllCitiesInBase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRetorfit();
        Log.e(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String[] weathValues = null;

        //Получаем значение из activity которая запустила сервис
        String city = intent.getStringExtra(CITY_NAME);

        //Получаем города в базе
        String[] citiesInBase = getAllCitiesInBase.callback();

        //Проверка есть ли город в базе
        for (int i = 0; i < citiesInBase.length; i++) {
            if (citiesInBase[i].equals(city)) {

                //получаем погоду из базы по городу
                weathValues = valuesFromBase.callback(city);
            }
        }

        //Есть города нет в базе, берем погоду из инета
        if (weathValues == null) {
            weathValues = requestRetrofit(city, "29fb8d6056fe0f462c0731feda71d342"); //"29fb8d6056fe0f462c0731feda71d342"
                                                                                         // "29bd6056fe0f462c0731feda71d342"
        }

        Log.e(TAG, "onHandleIntent " + city);

        //Отправляем результат в activity
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_MYINTENTSERVICE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);

        responseIntent.putExtra(TEMPERATURE_VALUE, weathValues[0]);
        responseIntent.putExtra(WIND_VALUE, weathValues[1]);
        responseIntent.putExtra(PRESSURE_VALUE, weathValues[2]);

        sendBroadcast(responseIntent);
    }

    private void initRetorfit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        openWeather = retrofit.create(OpenWeather.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    private String[] requestRetrofit(String city, String keyApi) {

        final String[] weath = new String[3];

        openWeather.loadWeather(city, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null)
                            try {
                                weath[0] = (Float.toString(response.body().getMain().getTemp()));
                                weath[1] = (Float.toString(response.body().getWind().getSpeed()));
                                weath[2] = (Float.toString(response.body().getMain().getPressure()));
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        weath[0] = "Error";
                    }
                });


        return weath;
    }
}
