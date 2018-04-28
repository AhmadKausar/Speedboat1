package com.example.kausar.speedboat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kausar.speedboat.JSONParser;
import com.example.kausar.speedboat.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity {


    EditText editEmail, editPassword, editName;
    Button btnSignIn, btnRegister;
    public static final String tag_username="username";
    public SharedPreferences sharedpreferences;
    Boolean session = false;
    String  username;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    String URL= "https://bandregracing.000webhostapp.com/index.php";
    public static final String tes = "Successfully logged in";
    JSONParser jsonParser=new JSONParser();
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editName = findViewById(R.id.editName);
        editPassword = findViewById(R.id.editPassword);


        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        username = sharedpreferences.getString(tag_username, null);

        if (session) {
            Intent intent = new Intent(Login.this, AndroidDynamic.class);
            intent.putExtra(tag_username, username);
            finish();
            startActivity(intent);
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),"");


               // Intent i =new Intent(getApplicationContext(),AndroidDynamic.class);
                //startActivity(i);
            }



        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(i==0)
                {
                    i=1;
                    editEmail.setVisibility(View.VISIBLE);
                    btnSignIn.setVisibility(View.GONE);
                }
                else{

                    editEmail.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
                    i=0;

                    AttemptLogin attemptLogin= new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),editEmail.getText().toString());

                }

            }
        });


    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog = new ProgressDialog(Login.this);

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            if (!dialog.isShowing()) {


                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();

            }
        }

        @Override

        protected JSONObject doInBackground(String... args) {



            String email = args[2];
            String password = args[1];
            String name= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if(email.length()>0)
                params.add(new BasicNameValuePair("email",email));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);


            return json;

        }



        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    if(result.get("message").equals(tes)){
                        startActivity(new Intent(Login.this,AndroidDynamic.class));

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(tag_username, username);
                        editor.commit();

                        // Memanggil main activity
//                        Intent intent = new Intent(Login.this, AndroidDynamic.class);
                        Intent intent = new Intent(Login.this, List.class);
                        intent.putExtra(tag_username, username);
                        finish();
                        startActivity(intent);
                    }
//                   startActivity(new Intent(Login.this, AndroidDynamic.class));

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}