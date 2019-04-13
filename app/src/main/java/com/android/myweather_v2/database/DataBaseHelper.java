package com.android.myweather_v2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

//Класс для создания БД
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db"; // название бд
    private static final int DATABASE_VERSION = 2; // версия базы данных

    public static final String TABLE_WEATHER = "cities_weather"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_WIND = "wind";


//    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WEATHER + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CITY + " TEXT," +
                COLUMN_TEMPERATURE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if ((oldVersion == 1) && (newVersion == 2)) {
//            String upgradeQuery = "ALTER TABLE " + TABLE_WEATHER +
//                    " ADD COLUMN " + COLUMN_WIND + " TEXT DEFAULT 'Title'";
//            db.execSQL(upgradeQuery);
//        }
    }
}
