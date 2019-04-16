package com.android.myweather_v2;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myweather_v2.database.NoteDataReader;
import com.android.myweather_v2.database.NoteDataSourse;
import com.squareup.picasso.Picasso;
import java.util.List;

import static com.android.myweather_v2.OptionsActivity.OPTIONS_CONDITIONS;
import static com.android.myweather_v2.OptionsActivity.SHOWPRESSURE_OPTION;
import static com.android.myweather_v2.OptionsActivity.SHOWWIND_OPTION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String cityName;
    private int currentPosition = 0;
    private TextView tViewSelectedCity;
    private TextView tViewSensorLight;
    private TextView allSensors;
    private Button selectCityButton;
    private ImageView pictireFromInt;


    public static final int REQUEST_ACCES_TYPE = 1;
    public static final String CURRENT_CITY_POS = "CurrentCity";
    public static final String CITY_NAME = "cityName";
    public static final String CONDITIONS = "Conditions";
    public static final String CURRENT_CITY = "CurrentCity";
    public static final String ALL_CITIES = "AllCities";


    public SharedPreferences condOptions;
    private boolean showWind = false;
    private boolean showPressure = false;

    private Toolbar toolbar;
    private NavigationView navigationView;

    private SensorManager sensorManager;
    private Sensor sensorLight;
    private List<Sensor> sensors;

    private NoteDataSourse noteDataSourse;
    private NoteDataReader noteDataReader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGui();
        initDataBase();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Контекстное меню
        registerForContextMenu(tViewSelectedCity);


        //Получить файл опций ключу OPTIONS_CONDITIONS. Если файла нет, то он будет создан
        condOptions = getSharedPreferences(OPTIONS_CONDITIONS, Context.MODE_PRIVATE);
        //Достать из сохраненных настроек текущий город
        if(condOptions.contains(CURRENT_CITY)){
            cityName = condOptions.getString(CURRENT_CITY, null);
        }


        //Кпопка выбора города вызывает activity со списком городов
        selectCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] cities = noteDataReader.getAllCities();

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CitiesActivity.class);
                intent.putExtra(ALL_CITIES, cities);
                startActivityForResult(intent, REQUEST_ACCES_TYPE);

            }
        });

        //Показать погоду
        showWeatherInfo();


        //Плавающая кнопка
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //Создание неявного Intent(намерения)
                Uri webSite = Uri.parse("https://yandex.ru/pogoda/saint-petersburg");
                Intent intent = new Intent(Intent.ACTION_VIEW, webSite);
                //Создание окна с выбором приложения(если есть несколько приложений, способных обработать данный Intent)
                Intent chooser = Intent.createChooser(intent, "Выберите");
                startActivity(chooser);

            }
        });


        //Кнопка показать все датчики
        Button showAllSensorsButton = findViewById(R.id.button_showAllSensors);
        showAllSensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showSensors();
                }
        });


        //Меню навигации
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Датчик:
        //Менеджер датчиков
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Сам датчик
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        //Получить все датчики
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        //Загрузить картинку из Интернета
        Picasso.with(this).
                load("https://www.bystricak.sk/assets/images/bb/posts/img-1/1528100698-pocasie-sa-vyraznejsie-menit-nebude-vysoke-teploty-a-burky-budu-na-dennom-poriadku.JPG")
                .into(pictireFromInt);

    }

    private void initGui() {
        selectCityButton = findViewById(R.id.buttonSelectCity);
        tViewSelectedCity = findViewById(R.id.tViewSelectedCity);
        tViewSensorLight = findViewById(R.id.sensor_light);
        allSensors = findViewById(R.id.all_sensors);
        pictireFromInt = findViewById(R.id.picture_from_int);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CITY_NAME, cityName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cityName = savedInstanceState.getString(CITY_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Читаем установленное значение размера шрифта
        float fontSize = Float.parseFloat(prefs.getString(getString(R.string.pref_size), "20"));
        //Применяем настройки
        tViewSelectedCity.setTextSize(fontSize);

        //Получить значения установленные в опциях
        if (condOptions.contains(SHOWWIND_OPTION) && condOptions.contains(SHOWPRESSURE_OPTION)){
            showWind = condOptions.getBoolean(SHOWWIND_OPTION, false);
            showPressure = condOptions.getBoolean(SHOWPRESSURE_OPTION, false);
        }

        //Регистрируем слушатель датчика
        sensorManager.registerListener(listenerTemp, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);

        if(cityName != null){
            tViewSelectedCity.setText(cityName);
        }
        showWeatherInfo();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //Если приложение свернуто, то не будем тратить энергию на получение информации по датчикам
        sensorManager.unregisterListener(listenerTemp, sensorLight);
    }

    //Получаем данные из activity для которой вызывали startActivityForResult (кнопка Выбор города)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_ACCES_TYPE){
            if(resultCode == RESULT_OK){
                try {
                    cityName = data.getStringExtra(CITY_NAME);
                    currentPosition = data.getIntExtra(CURRENT_CITY_POS, 0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if(cityName != null){
                    tViewSelectedCity.setText(cityName);
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Кнопка Назад в меню навигации
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Создание меню Action bar. Задаем макет
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Поиск:
        MenuItem search = menu.findItem(R.id.action_search);

        //Строка поиска
        SearchView searchText = (SearchView)search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Реагирует на конец ввода текста
            @Override
            public boolean onQueryTextSubmit(String query) {
//                TextView tv = findViewById(R.id.text_test);
//                tv.setText(query);
                return true;
            }

            //Реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    //Назначаем действия для меню Action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                startActivityUsual(this, SettingsActivity.class);
                return true;

            case R.id.action_options:
                startActivityUsual(this, OptionsActivity.class);
                return true;

            case R.id.action_add_to_db:
                addElementToDB();
                return true;

            case R.id.action_clear_db:
                noteDataSourse.deleteAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //Создание контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }


    //Обработка нажатий контекстного меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.context_select_city:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CitiesActivity.class);
                startActivityForResult(intent, REQUEST_ACCES_TYPE);

            default: return super.onContextItemSelected(item);

        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_creator:
                Snackbar.make(navigationView, "Михаил Папунин", Snackbar.LENGTH_LONG).show();
                return true;

            case R.id.nav_feedback:
                Snackbar.make(navigationView, "mihassu1@gmail.com", Snackbar.LENGTH_LONG).show();
                return true;

            case R.id.nav_options:
                startActivityUsual(this, OptionsActivity.class);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Сохраняем текущий город перед закрытием
        SharedPreferences.Editor editor = condOptions.edit();
        if(tViewSelectedCity != null){
            editor.putString(CURRENT_CITY, tViewSelectedCity.getText().toString());
            editor.apply();
        }
    }


    public boolean[] getConditionsFromCheckBoxes() {
        boolean[] c = {showWind, showPressure};
        return c;
    }

    public void showWeatherInfo() {

        if(cityName == null) {
            return;
        }

        WeatherInfoFragment det = WeatherInfoFragment.create((tViewSelectedCity.getText()).toString(), currentPosition, getConditionsFromCheckBoxes());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(getFragmentManager().findFragmentById(R.id.weather_container) == null) {
            ft.add(R.id.weather_container, det);

        } else {
            ft.replace(R.id.weather_container, det);
        }

        ft.commit();
    }

    public void startActivityUsual(Context context, Class cls){
        Intent intent = new Intent();
        intent.setClass(context, cls);
        startActivity(intent);
    }

    //Слушатель датчика
    SensorEventListener listenerTemp = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            tViewSensorLight.setText("Значение датчика: ");
            tViewSensorLight.append(String.valueOf(event.values[0]));
//            showTempSensor(event);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    //
    public void showTempSensor(SensorEvent event){
        String t = String.valueOf(event.values[0]);
        tViewSensorLight.setText(t);
    }

    public void showSensors() {
        StringBuilder sb = new StringBuilder();
        if (allSensors.getText().toString().length() > 0) {
            allSensors.setText("");
        } else {

            for (Sensor s : sensors) {
                sb.append(s.getName()).append(", ").append(s.getType()).append("\n");
                allSensors.setText(sb);
            }
        }
    }


    //База данных
    private void initDataBase() {
        noteDataSourse = new NoteDataSourse(getApplicationContext());
        noteDataSourse.open();
        noteDataReader = noteDataSourse.getNoteDataReader();
    }

    //Добавить город в базу
    public void addElementToDB() {

        // Выведем диалоговое окно для ввода новой записи
        LayoutInflater factory = LayoutInflater.from(this);

        final View alertView = factory.inflate(R.layout.data_base_activity, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView);
        builder.setTitle(R.string.add_city);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText newCity = alertView.findViewById(R.id.city_name_to_db);
                EditText newTemp = alertView.findViewById(R.id.temperature_to_db);
                // если использовать findViewById без alertView, то всегда будем получать
                // editText = null

                noteDataSourse.addNote(newCity.getText().toString(), newTemp.getText().toString());
                dataUpdated();
            }
        });

        builder.show();
    }

    private void dataUpdated() {
        noteDataReader.refresh(); //без этого БД обновляется только при перезапуске приложения
        CitiesActivity.adapter.notifyDataSetChanged();

    }
}

