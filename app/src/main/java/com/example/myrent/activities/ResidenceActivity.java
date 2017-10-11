package com.example.myrent.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.myrent.R;
import com.example.myrent.app.MyRentApp;
import com.example.myrent.models.Portfolio;
import com.example.myrent.models.Residence;

import static com.example.myrent.android.helpers.ContactHelper.getContact;
import static com.example.myrent.android.helpers.ContactHelper.getEmail;
import static com.example.myrent.android.helpers.IntentHelper.selectContact;
import static com.example.myrent.android.helpers.ContactHelper.sendEmail;
import android.content.Intent;

public class ResidenceActivity extends AppCompatActivity implements TextWatcher, OnCheckedChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText  geolocation;
    private Residence residence;
    private CheckBox  rented;
    private Button    dateButton;
    private Portfolio portfolio;
    private Button    tenantButton;
    private Button    reportButton;
    private String    emailAddress = "";
    private Intent    data;

    private static final int REQUEST_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);

        geolocation = (EditText) findViewById(R.id.geolocation);
        residence   = new Residence();
        geolocation.addTextChangedListener(this);

        dateButton  = (Button)   findViewById(R.id.registration_date);
        dateButton.setOnClickListener(this);

        rented      = (CheckBox) findViewById(R.id.isrented);
        rented.setOnCheckedChangeListener(this);

        MyRentApp app = (MyRentApp) getApplication();
        portfolio     = app.portfolio;

        Long resId = (Long) getIntent().getExtras().getSerializable("RESIDENCE_ID");
        residence  = portfolio.getResidence(resId);
        if (residence != null) {
            updateControls(residence);
        }

        tenantButton = (Button)   findViewById(R.id.tenant);
        tenantButton.setOnClickListener(this);
        reportButton = (Button)   findViewById(R.id.residence_reportButton);
        reportButton.setOnClickListener(this);
    }

    public void updateControls(Residence residence) {
        geolocation.setText(residence.geolocation);
        rented.setChecked(residence.rented);
        dateButton.setText(residence.getDateString());
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Log.i(this.getClass().getSimpleName(), "rented Checked");
        residence.rented = isChecked;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        residence.date = date.getTime();
        dateButton.setText(residence.getDateString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_date      : Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog (this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case R.id.tenant : selectContact(this, REQUEST_CONTACT);
                break;
            case R.id.residence_reportButton :
                sendEmail(this, emailAddress,
                        getString(R.string.residence_report_subject), residence.getResidenceReport(this));
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        portfolio.saveResidences();
    }

    private void readContact() {
        String name = getContact(this, data);
        emailAddress = getEmail(this, data);
        tenantButton.setText(name + " : " + emailAddress);
        residence.tenant = name;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONTACT:
                this.data = data;
                checkContactsReadPermission();
                break;
        }
    }

    //https://developer.android.com/training/permissions/requesting.html
    private void checkContactsReadPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //We can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        }
        else {
            //We already have permission, so go head and read the contact
            readContact();
        }
    }

    //https://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    readContact();
                }
            }
        }
    }
}
