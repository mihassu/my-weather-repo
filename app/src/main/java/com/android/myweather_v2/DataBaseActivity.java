package com.android.myweather_v2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.myweather_v2.database.NoteDataReader;
import com.android.myweather_v2.database.NoteDataSourse;

public class DataBaseActivity extends AppCompatActivity {

    private NoteDataSourse noteDataSourse;
    private NoteDataReader noteDataReader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_base_activity);



    }

    private void initDataSource() {
        noteDataSourse = new NoteDataSourse(getApplicationContext());
        noteDataSourse.open();
        noteDataReader = noteDataSourse.getNoteDataReader();
    }
}
