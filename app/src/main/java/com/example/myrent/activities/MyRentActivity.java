package com.example.myrent.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.myrent.R;
import com.example.myrent.models.Residence;

public class MyRentActivity extends AppCompatActivity implements TextWatcher {

    private EditText geolocation;
    private Residence residence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrent);

        geolocation = (EditText) findViewById(R.id.geolocation);
        residence = new Residence();

        geolocation.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        residence.setGeolocation(editable.toString());
    }
}
