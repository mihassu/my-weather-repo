package com.android.myweather_v2;

public interface WeatherInfo {
//    String getWeatherInfoByCity(String cityName, int index, boolean[] weatherConditions);
    String getTemperatureByCity(String cityName);
    String getWindByCity(String cityName);
    String getPressureByCity(String cityName);

}
