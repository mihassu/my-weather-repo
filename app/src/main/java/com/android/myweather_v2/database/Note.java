package com.android.myweather_v2.database;

public class Note {

    private long id;
    private String cityName;
    private String temperatureValue;
    private String windValue;
    private String pressureValue;

    //Геттеры
    public long getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTemperatureValue() {
        return temperatureValue;
    }

    public String getWindValue() {
        return windValue;
    }

    public String getPressureValue() {
        return pressureValue;
    }

    //Сеттеры
    public void setId(long id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setTemperatureValue(String temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public void setWindValue(String windValue) {
        this.windValue = windValue;
    }

    public void setPressureValue(String pressureValue) {
        this.pressureValue = pressureValue;
    }
}
