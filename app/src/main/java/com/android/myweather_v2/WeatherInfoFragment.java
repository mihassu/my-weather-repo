package com.android.myweather_v2;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.android.myweather_v2.MainActivity.CITY_NAME;
import static com.android.myweather_v2.MainActivity.CONDITIONS;
import static com.android.myweather_v2.MainActivity.CURRENT_CITY_POS;


public class WeatherInfoFragment extends Fragment implements WeatherStrings {

    private final String TAG = "WeatherInfoFragment";

    private BroadcastReceiver myBroadcastReceiver;
    private TextView tView;

    //контейнер GridLayout куда будет выводится погода
    GridLayout weather;


    //Фабричный метод - метод который создает объект класса
    public static WeatherInfoFragment create(String cityName, int index, boolean[] conditions) {
        WeatherInfoFragment f = new WeatherInfoFragment();

        //Передача полученных значений во фрагмент
        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
//        args.putInt(CURRENT_CITY_POS, index);
        args.putBooleanArray(CONDITIONS, conditions);
        f.setArguments(args);
        return f;
    }

//    public int getIndex() {
//        return getArguments().getInt(CURRENT_CITY_POS, 0);
//    }

    public String getCityName() {
        return getArguments().getString(CITY_NAME);
    }

    public boolean[] getConditions() {
        return getArguments().getBooleanArray(CONDITIONS);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.weather_info_fragment, container, false);
//        TextView tView = new TextView(getActivity()); //если фрагмент без макета
        tView = v.findViewById(R.id.weather_info);

        //Получить котейнер GridLayout куда будет выводится погода
        weather = v.findViewById(R.id.weather_fragment);


        //Запустить IntentService (Сервис который узнает погоду)
        Intent intentForService = new Intent(getActivity(), WeatherInfoService.class);
        intentForService.putExtra(CITY_NAME, getCityName());
//        intentForService.putExtra(CONDITIONS, getConditions());
//        intentForService.putExtra(CURRENT_CITY_POS, getIndex());

        getActivity().startService(intentForService);

        //Создаем BroadcastReceiver для получения данных от Сервиса
        myBroadcastReceiver = new MyBroadcastReceiver();

        //регистрируем BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(WeatherInfoService.ACTION_MYINTENTSERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(myBroadcastReceiver, intentFilter);

        return v;
    }

    //Новый BroadcastReceiver
    public class MyBroadcastReceiver extends BroadcastReceiver {

        //метод вызывается при получении Broadcast-сообщение от Сервиса
        @Override
        public void onReceive(Context context, Intent intent) {

            tView.setText(intent.getStringExtra(WeatherInfoService.TEMPERATURE_VALUE));

            if (getConditions()[0]) {
                weather.addView(createWeatherCard(Gravity.START, getResources().getText(R.string.windStr).toString(), 16));
                weather.addView(createWeatherCard(Gravity.END, intent.getStringExtra(WeatherInfoService.WIND_VALUE), 16));
            }

            if (getConditions()[1]) {
                weather.addView(createWeatherCard(Gravity.START, getResources().getText(R.string.pressureStr).toString(), 16));
                weather.addView(createWeatherCard(Gravity.END, intent.getStringExtra(WeatherInfoService.PRESSURE_VALUE), 16));
            }
        }
    }

    private View createWeatherCard(int gravity, String weatherValue, float textSize) {
        TextView tempTextView = new TextView(getActivity());
        GridLayout.LayoutParams layoutparams = new GridLayout.LayoutParams();
        layoutparams.setMargins(0, 10, 0, 10);
        layoutparams.setGravity(gravity);
        tempTextView.setText(weatherValue);
        tempTextView.setTextSize(textSize);
        tempTextView.setLayoutParams(layoutparams);

        return tempTextView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //отключаем BroadcastReceiver
        getActivity().unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public String getTemperature() {
        return getResources().getString(R.string.temperatureStr);
    }

    @Override
    public String getWind() {
        return getResources().getString(R.string.windStr);
    }

    @Override
    public String getPressure() {
        return getResources().getString(R.string.pressureStr);
    }
}
