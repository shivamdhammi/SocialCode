package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class splashScreen extends AppCompatActivity {

    Button btnLogin,btnRegister;
    private long time = 1500;
    private CountDownTimer timer;
    private  FirebaseAuth auth;
    private String rating,codeforcesid;
    private LinearLayout progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        btnLogin = findViewById(R.id.ss_login);
        btnRegister = findViewById(R.id.ss_register);
        progress = findViewById(R.id.splash_progress);
        final String user,pass;
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        user = sharedPref.getString("Email","");
        pass = sharedPref.getString("Password","");
        codeforcesid = sharedPref.getString("codeforces_id","");
        auth = FirebaseAuth.getInstance();

        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);

        if(!user.isEmpty() && !pass.isEmpty())
        {
            auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        fetchdata process = new fetchdata();
                        process.execute();
                    }
                    else
                        changeVisibility();
                }
            });
        }
        else
            changeVisibility();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),register.class);
                startActivity(intent);
            }
        });
    }

    private class fetchdata extends AsyncTask<Void,Void,String> {
        String result;

        public fetchdata() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = (JSONObject) new JSONTokener(s).nextValue();
                String status = object.getString("status");
                if(status.equals("OK")){
                    JSONArray res = object.getJSONArray("result");
                        JSONObject mydata = (JSONObject) res.get(0);
                        rating = mydata.getString("rating");

                }
                else{
                    Toast.makeText(getApplicationContext(),object.getString("comment"),Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
            }


            Intent intent = new Intent(getApplicationContext(), profile.class);
            intent.putExtra("rating",rating);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://codeforces.com/api/user.info?handles="+codeforcesid);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
    }





    void changeVisibility(){
        timer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Nothing there, Please Login or Register",Toast.LENGTH_LONG).show();
                btnLogin.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

}
