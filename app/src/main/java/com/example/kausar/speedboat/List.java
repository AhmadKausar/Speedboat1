package com.example.kausar.speedboat;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.kausar.speedboat.AndroidDynamic.day_extra;
import static com.example.kausar.speedboat.AndroidDynamic.departure_extra;
import static com.example.kausar.speedboat.AndroidDynamic.destination_extra;

public class List extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Adapter adapter;
    TextView testdata;
    ArrayList<ModelItem> arrayList = new ArrayList<>();
    String departure, destination, date;
    public static final String URL = "https://bandregracing.000webhostapp.com/Android/Test.php?keb=";
    RequestQueue requestQueue;

    public static final String TAG = List.class.getSimpleName();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(this,arrayList);
        recyclerView.setAdapter(adapter);
        testdata = findViewById(R.id.tesdata);



        //Intent data
        //+ departure + "&tuj=" + destination + "&hari=" + date
        //String departure, String destination, String date

        date = getIntent().getStringExtra(day_extra);
        departure = getIntent().getStringExtra(departure_extra);
        destination = getIntent().getStringExtra(destination_extra);
        testdata.setText("Hari : " + date + "\n" + "Keberangkatan : " + departure + "\n" + "Tujuan : " + destination);

    }

    private void gatdata(String keb, String tuj, String har) {
        final ProgressBar progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        setProgressBarIndeterminateVisibility(true);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL + keb + "&tuj=" + tuj + "&hari=" + har,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parse(response);
                Log.e(TAG,"DATA : " + response);
                progressBar.setVisibility(View.INVISIBLE);
                testdata.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Unable to retrieve any data from server",Toast.LENGTH_SHORT).show();
            }

         });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void parse(JSONArray response) {
        for(int i=0;i<response.length();i++){

            try {
                ModelItem modelItem = new ModelItem();
                JSONObject jsonObject = response.getJSONObject(i);

                modelItem.setSpeedboat(jsonObject.getString("speedboat"));
                modelItem.setHarga(jsonObject.getString("harga"));
                modelItem.setHari(jsonObject.getString("hari"));
                modelItem.setJam(jsonObject.getString("jam"));
                modelItem.setNo_HP(jsonObject.getString("no_hp"));
                arrayList.add(modelItem);

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
        adapter.notifyDataSetChanged();
    }
}
