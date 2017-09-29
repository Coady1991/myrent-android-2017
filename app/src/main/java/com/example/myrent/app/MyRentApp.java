package com.example.myrent.app;


import android.app.Application;
import com.example.myrent.models.Portfolio;

public class MyRentApp extends Application
{
    public Portfolio portfolio;

    @Override
    public void onCreate()
    {
        super.onCreate();
        portfolio = new Portfolio();
    }
}
