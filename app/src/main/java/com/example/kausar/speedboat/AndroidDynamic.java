package com.example.kausar.speedboat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.kausar.speedboat.Login.my_shared_preferences;
import static com.example.kausar.speedboat.Login.session_status;
import static com.example.kausar.speedboat.Login.tag_username;

public class AndroidDynamic extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String DATA_URL = "https://bandregracing.000webhostapp.com/Android/keberangkatan.php";
    public static final String DATA_TUJUAN = "https://bandregracing.000webhostapp.com/Android/tujuan.php";//API ke php
    public static final String JSON_ARRAY = "result"; //Result database
    private ArrayList<String> keberangkatan;
    private ArrayList<String> tujuan;

    //Variable putIntent
    public String dayOfWeek;
    public String depature;
    public String destinations;

    public static final String departure_extra = "DEPATURE_EXTRA";
    public static final String destination_extra = "DESTINATION_EXTRA";
    public static final String day_extra = "DAY_EXTRA";

    //JSON Array
    private JSONArray result;

    private static final String TAG=AndroidDynamic.class.getSimpleName();
    Button Search;
    TextView tujuanid;
    TextView keberangkatanid;
    EditText mDateEditText;
    Calendar mCurrentDate;
    Spinner spinner;
    Spinner spinner2;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;
    String[] kota = {"Nunukan", "Tarakan", "Tanjung Selor"};
    String[] kota2 = {"Nunukan", "Tarakan", "Tanjung Selor"};
    String defaultPassanger = "-";
    public SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Search = findViewById(R.id.Search);
        tujuanid = findViewById(R.id.tujuanid);
        keberangkatanid = findViewById(R.id.keberangkatanid);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        tujuan = new ArrayList<String>();
        keberangkatan = new ArrayList<String>();
        getData();
        gettujuan();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                keberangkatanid.setText(getkeberangkatanid(position));

                //set variable dengan value position untuk pass intent
                depature = getkeberangkatanid(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                keberangkatanid.setText("");
            }
        });

        mDateEditText = findViewById(R.id.txtDateEntered);

        mDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");


            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                tujuanid.setText(gettujuanid(position));

                //set variable dengan value position untuk pass intent
                destinations = gettujuanid(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tujuanid.setText("");
            }
        });

        mDateEditText = findViewById(R.id.txtDateEntered);

        mDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");


            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent pass variable
                Intent intent = new Intent(AndroidDynamic.this, List.class);
                intent.putExtra(day_extra,dayOfWeek);
                intent.putExtra(departure_extra,depature);
                intent.putExtra(destination_extra,destinations);
                startActivity(intent);

            }
        });

    }


    private void getData() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getkeberangkatan(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getkeberangkatan(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                keberangkatan.add(json.getString("Kota"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(AndroidDynamic.this, android.R.layout.simple_spinner_dropdown_item
                , keberangkatan));
    }

    //Method to get student name of a particular position
    public String getkeberangkatanid(int position) {
        String keberangkatanid = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            keberangkatanid = json.getString("Keberangkatan_ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return keberangkatanid;
    }


    private void gettujuan() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(DATA_TUJUAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            gettujuan(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void gettujuan(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                tujuan.add(json.getString("Kota"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner2.setAdapter(new ArrayAdapter<String>(AndroidDynamic.this, android.R.layout.simple_spinner_dropdown_item
                , tujuan));
    }

    //Method to get student name of a particular position
    public String gettujuanid(int position) {
        String tujuanid = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            tujuanid = json.getString("Tujuan_ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return tujuanid;

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayofMonth);
        if (c.getTime().after(currentDate)) {
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Date date = new Date(year, month, dayofMonth-1);
            dayOfWeek = simpledateformat.format(date);
            Log.e(TAG,"Get Day: " + dayOfWeek);
            EditText txtDateEntered = findViewById(R.id.txtDateEntered);
            txtDateEntered.setText(currentDateString);
        } else {
            Toast.makeText(this, "Cannot back past time", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Intent j = new Intent(AndroidDynamic.this, AboutActivity.class);
                startActivity(j);
        }


        switch (item.getItemId()) {
            case R.id.logout:
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, false);
                editor.putString(tag_username, "");
                editor.commit();
                Intent Intent = new Intent(AndroidDynamic.this, Login.class);
                startActivity(Intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
