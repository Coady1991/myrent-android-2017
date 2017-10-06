package com.example.myrent.activities;

import com.example.myrent.R;
import com.example.myrent.app.MyRentApp;
import com.example.myrent.models.Portfolio;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ResidenceListActivity extends AppCompatActivity
{
    private ListView listView;
    private Portfolio portfolio;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_residence_list);

        listView = (ListView) findViewById(R.id.residenceList);

        MyRentApp app = (MyRentApp) getApplication();
        portfolio = app.portfolio;
    }
}
