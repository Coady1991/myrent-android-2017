package com.example.myrent.app;


import android.app.Application;
import com.example.myrent.models.Portfolio;
import com.example.myrent.models.PortfolioSerializer;

import static com.example.myrent.android.helpers.LogHelpers.info;

public class MyRentApp extends Application {
    private static final String FILENAME = "portfolio.json";
    public Portfolio portfolio;
    protected static MyRentApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        PortfolioSerializer serializer = new PortfolioSerializer(this, FILENAME);
        portfolio = new Portfolio(serializer);
        app = this;

        info(this, "MyRent app launched");
    }

    public static MyRentApp getApp(){
        return app;
    }
}
