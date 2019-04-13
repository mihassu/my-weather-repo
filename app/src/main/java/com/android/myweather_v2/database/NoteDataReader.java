package com.android.myweather_v2.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

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

    // получить количество строк в таблице
    public int getCount() {
        return cursor.getCount();
    }

    // преобразователь курсора в объект. Возвращается объект Note с данными полученными из Cursor
    private Note cursorToNote() {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setCityName(cursor.getString(1));
        note.setTemperatureValue(cursor.getString(2));
        return note;
    }


    @Override
    public void close() throws IOException {
        cursor.close();
    }
}
