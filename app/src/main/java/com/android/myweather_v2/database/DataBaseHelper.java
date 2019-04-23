package com.android.myweather_v2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

//Класс для создания БД
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db"; // название бд
    private static final int DATABASE_VERSION = 5; // версия базы данных

    public static final String TABLE_WEATHER = "cities_weather"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_WIND = "wind";
    public static final String COLUMN_PRESSURE = "pressure";



//    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(TABLE_WEATHER, "DataBaseHelper - конструктор");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TABLE_WEATHER, "DataBaseHelper - onCreate");

        db.execSQL("CREATE TABLE " + TABLE_WEATHER + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CITY + " TEXT," +
                COLUMN_TEMPERATURE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TABLE_WEATHER, "DataBaseHelper - onUpgrade");
        Log.e(TABLE_WEATHER, "oldVersion: " + oldVersion + ", " + "newVersion: " + newVersion);

        if (oldVersion == 4) {
            Log.e(TABLE_WEATHER, "DataBaseHelper - onUpgrade(oldVersion == 3)");
            String upgradeQuery = "ALTER TABLE " + TABLE_WEATHER +
                    " ADD COLUMN " + COLUMN_PRESSURE + " TEXT";

            db.execSQL(upgradeQuery);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        Log.e(TABLE_WEATHER, "DataBaseHelper - onDowngrade");
        Log.e(TABLE_WEATHER, "oldVersion: " + oldVersion + ", " + "newVersion: " + newVersion);


    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.e(TABLE_WEATHER, "DataBaseHelper - onOpen");

    }
}
