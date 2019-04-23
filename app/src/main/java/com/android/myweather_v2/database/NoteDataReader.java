package com.android.myweather_v2.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.myweather_v2.WeatherInfo;
import com.android.myweather_v2.WeatherInfoService;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoteDataReader implements Closeable {

    private Cursor cursor; // Курсор: фактически это подготовенный запрос, но сами данные считываются только по необходимости
    private final SQLiteDatabase database;

    //Указываются колонки значения которых будут возвращаться при вызове query() (SELECT)
    private final String[] notesAllColumn = {
            DataBaseHelper.COLUMN_ID,
            DataBaseHelper.COLUMN_CITY,
            DataBaseHelper.COLUMN_TEMPERATURE,
            DataBaseHelper.COLUMN_WIND,
            DataBaseHelper.COLUMN_PRESSURE

    };

    public NoteDataReader(SQLiteDatabase database) {
        this.database = database;
    }

    // Подготовить к чтению таблицу
    public void open() {
        query();
        cursor.moveToFirst(); //Методы Cursor возвращают true или false

        WeatherInfoService.setValuesFromBase(new CallBackWeather() {
            @Override
            public String[] callback(Object... args) {
                String[] weatherValues = new String[3];
                cursor.moveToFirst();
                while (true) {

                    if (cursor.isAfterLast()) {
                        break;
                    }
                    if(cursor.getString(1).equals(args[0].toString())) {
                        weatherValues[0] = cursor.getString(2);
                        weatherValues[1] = cursor.getString(3);
                        weatherValues[2] = cursor.getString(4);
                    }
                    cursor.moveToNext();
                }
                return weatherValues;
            }
        });


    }

    // Перечитать таблицу
    public void refresh() {
        int position = cursor.getPosition();
        query();
        cursor.moveToPosition(position);
    }

    // создание запроса
    // метод query() - это обертка для команды SQL SELECT. Результат - объект Cursor
    private void query() {
        cursor = database.query(DataBaseHelper.TABLE_WEATHER,
                notesAllColumn, null, null,
                null, null, null);
        //в cursor попадают данные после запроса. Чтобы их получить надо вызывать у него методы

        Log.e(DataBaseHelper.TABLE_WEATHER, "version: " + database.getVersion());

        for (int i = 0; i < cursor.getColumnNames().length; i++) {
            Log.e(DataBaseHelper.TABLE_WEATHER, cursor.getColumnNames()[i]);
        }
    }

    // прочитать данные по определенной позиции
    public Note getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    // преобразователь курсора в объект. Возвращается объект Note с данными полученными из Cursor
    private Note cursorToNote() {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setCityName(cursor.getString(1));
        note.setTemperatureValue(cursor.getString(2));
        note.setWindValue(cursor.getString(3));
        note.setPressureValue(cursor.getString(4));
        return note;
    }

    // получить количество строк в таблице
    public int getCount() {
        return cursor.getCount();
    }

    public String[] getAllCities() {
        List<String> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (true) {
                if (cursor.isAfterLast()) {
                    break;
                }
                list.add(cursor.getString(1));
                cursor.moveToNext();

            }
        }

        String[] tempArr = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            tempArr[i] = list.get(i);
        }
        return tempArr;
    }

    @Override
    public void close() throws IOException {
        cursor.close();
    }

}
