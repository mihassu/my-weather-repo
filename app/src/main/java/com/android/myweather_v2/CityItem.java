package com.android.myweather_v2;

public class CityItem {

    private String cityName;
    private int coatOfArms;

    public CityItem(String cityName, int coatOfArms) {
        this.cityName = cityName;
        this.coatOfArms = coatOfArms;
    }


    public String getCityName() {
        return cityName;
    }

    public int getCoatOfArms() {
        return coatOfArms;
    }

    public void setCityName(String  cityName) {
        this.cityName = cityName;
    }

    public void setCoatOfArms(int coatOfArms) {
        this.coatOfArms = coatOfArms;
    }
}
