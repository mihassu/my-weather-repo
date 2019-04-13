package com.android.myweather_v2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

public class NoteDataSourse implements Closeable {

    private final DataBaseHelper dbHelper;
    private SQLiteDatabase database;
    private NoteDataReader noteDataReader;

    public NoteDataSourse(Context context){
        //Для открытия БД:
        //1) создать экземпляр DataBaseHelper
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {

        //2) Вызвать метод getWritableDatabase() или getReadableDatabase()
        database = dbHelper.getWritableDatabase(); //получить БД доступную для записи и чтения

        noteDataReader = new NoteDataReader(database);
        noteDataReader.open();

    }

    public Note addNote(String city, String temperature){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_CITY, city);
        values.put(DataBaseHelper.COLUMN_TEMPERATURE, temperature);

        //insert() - это обертка INSERT INTO. null - как будут отображаться пустые значения
        long insertId = database.insert(DataBaseHelper.TABLE_WEATHER, null, values);
        Note newNote = new Note();
        newNote.setId(insertId);
        newNote.setCityName(city);
        newNote.setTemperatureValue(temperature);
        return newNote;
    }

    public NoteDataReader getNoteDataReader() {
        return noteDataReader;
    }

    @Override
    public void close() throws IOException {
        dbHelper.close();
        noteDataReader.close();
    }
}
