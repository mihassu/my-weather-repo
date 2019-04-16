package com.android.myweather_v2.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
            DataBaseHelper.COLUMN_TEMPERATURE
    };

    public NoteDataReader(SQLiteDatabase database) {
        this.database = database;
    }

    // Подготовить к чтению таблицу
    public void open() {
        query();
        cursor.moveToFirst(); //Методы Cursor возвращают true или false

        WeatherInfoService.setCityFromBase(new CallBackWeather() {
            @Override
            public String callback(Object... args) {
//                Cursor c = database.query(DataBaseHelper.TABLE_WEATHER,
//                        new String[] {DataBaseHelper.COLUMN_CITY, DataBaseHelper.COLUMN_TEMPERATURE},
//                        DataBaseHelper.COLUMN_CITY + "= ?",
//                        new String[] {args[0].toString()},null, null, null);
//                c.moveToFirst();
//                String city = c.getString(1);
//                c.close();
                cursor.moveToPosition((Integer)args[0]);
                String city = cursor.getString(2);
                return city;
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
        return note;
    }

    // получить количество строк в таблице
    public int getCount() {
        return cursor.getCount();
    }

    public String[] getAllCities() {
        List<String> list = new ArrayList<>();

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
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
