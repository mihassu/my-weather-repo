package com.android.myweather_v2;


import java.util.ArrayList;

public class WeatherInformerByService implements WeatherInfo {

    private ArrayList<City> cities;

    public WeatherInformerByService() {

        cities = new ArrayList<>();

        cities.add(new City("Москва", "5ºC", "1 м/с", "777 мм.рт.ст."));
        cities.add(new City("Санкт-Петербург", "10ºC", "2 м/с", "766 мм.рт.ст."));
        cities.add(new City("Новосибирск", "15ºC", "3 м/с", "744 мм.рт.ст."));
        cities.add(new City("Самара", "20ºC", "4 м/с", "733 мм.рт.ст."));

    }

    @Override
    public String getTemperatureByCity(String cityName) {
        String temperature = " ";
        for (City c: cities) {
            if(c.getCityName().equals(cityName)) {
                temperature = c.getTempValue();
            }
        }
        return temperature;
    }

    @Override
    public String getWindByCity(String cityName) {
        String wind = " ";
        for (City c: cities) {
            if(c.getCityName().equals(cityName)) {
                wind = c.getWindValue();
            }
        }
        return wind;
    }

    @Override
    public String getPressureByCity(String cityName) {
        String pressure = " ";
        for (City c: cities) {
            if(c.getCityName().equals(cityName)) {
                pressure = c.getPressureValue();
            }
        }
        return pressure;
    }

    private class City{

        private String cityName;
        private String tempValue;
        private String windValue;
        private String pressureValue;

        public City(String cityName, String tempValue, String windValue, String pressureValue) {

            this.cityName = cityName;
            this.tempValue = tempValue;
            this.windValue = windValue;
            this.pressureValue = pressureValue;
        }

        public String getCityName() {
            return cityName;
        }

        public String getTempValue() {
            return tempValue;
        }

        public String getWindValue() {
            return windValue;
        }

        public String getPressureValue() {
            return pressureValue;
        }
    }
}
