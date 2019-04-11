package com.android.myweather_v2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.android.myweather_v2.MainActivity.CITY_NAME;
import static com.android.myweather_v2.MainActivity.CURRENT_CITY_POS;

public class CitiesActivity extends AppCompatActivity {

    private List<CityItem> cities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_activity);

        //Создать список объектов Город
        setInitialData();

        RecyclerView recyclerView = findViewById(R.id.cities_recycler_view);

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Создать менеджер макетов (встроенный)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Установить адаптер
        CitiesAdapter adapter = new CitiesAdapter(cities);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Выполняем действия, после вызова startActivityForResult из MainActivity
                Intent intent = new Intent();
                intent.putExtra(CITY_NAME, ((TextView) v.findViewById(R.id.city_name)).getText().toString());
                intent.putExtra(CURRENT_CITY_POS, position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        //Установить анимацию
//        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        if(savedInstanceState == null) {
//            CitiesFragment details = new CitiesFragment();
//            details.setArguments(getIntent().getExtras());
//            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
//        }
    }

    //Создать список объектов Город
    public void setInitialData() {

        String[] citiesArray = getResources().getStringArray(R.array.Cities);
        TypedArray coatOfarms = getResources().obtainTypedArray(R.array.coat_of_arms_imgs);

        for (int i = 0; i < citiesArray.length; i++) {
            cities.add(new CityItem(citiesArray[i],
                    coatOfarms.getResourceId(i, -1)));

        }
    }


}
