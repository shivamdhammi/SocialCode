package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class home extends Fragment {

    private FloatingActionButton compiler;
    private TextView Name,College;
    private String codeforces_id;
    private TextView cRating,cSubmission,cConstest;
    private TextView hRating,hubmission,hConstest;
    private LinearLayout porgress;

    private Uri uriProfileImage;//uriZProfileImage = data.getData();[inside startActivityForResult()]
    private String profileImageUrl;//To store the Downloaded URL of the image
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private StorageReference storageReference;
    private ImageView pic,verification,retrive;
    private String userId;
    private String rating="";

//    private homeListener listener;
//
//    public interface homeListener{
//        void onInputHomeSent(String input);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_home,container,false);

        View view = inflater.inflate(R.layout.activity_home, container, false);
        compiler =  view.findViewById(R.id.home_compiler);

        compiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),compiler.class);
                startActivity(intent);
            }
        });

        Name = view.findViewById(R.id.home_name);
        College = view.findViewById(R.id.home_college);
        pic = view.findViewById(R.id.home_pic);
        verification = view.findViewById(R.id.home_verification);
        retrive = view.findViewById(R.id.home_retrive);
        cRating = view.findViewById(R.id.home_crating);
        cSubmission = view.findViewById(R.id.home_csubmission);
        cConstest = view.findViewById(R.id.home_ccontset);
        hRating = view.findViewById(R.id.home_hrating);
        hubmission = view.findViewById(R.id.home_hsubmission);
        hConstest = view.findViewById(R.id.home_hcontset);

        porgress = view.findViewById(R.id.home_progress);

        ref = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();




        if(getArguments()!=null){
            cRating.setText(getArguments().getString("rating"));
        }

        retrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                porgress.setVisibility(View.VISIBLE);
                fetchdata process = new fetchdata();
                process.execute();
            }
        });





        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        porgress.setVisibility(View.VISIBLE);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userInfo userinfo = dataSnapshot.child(auth.getCurrentUser().getUid()).child("Info").getValue(userInfo.class);

                Name.setText(userinfo.getName());
                College.setText(userinfo.getCollege());
                codeforces_id = userinfo.getCodeforces();
                if(cRating.getText().equals("-NA-")){
                    fetchdata process = new fetchdata();
                    process.execute();
                }

                if(auth.getCurrentUser().isEmailVerified()){
                    verification.setImageResource(R.drawable.ic_verified_user);
                    verification.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(),"yeb email "+auth.getCurrentUser().isEmailVerified(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });


        try {
            storageReference.child("profilepics/"+auth.getCurrentUser().getUid()+".jpg")
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Load image in the image view from Firebase Storage.
                    try {
                        Glide.with(getContext()).
                                load(uri).
                                into(pic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"@@@@@@@@@@@@@@@@",Toast.LENGTH_LONG).show();
            return;

        }
        porgress.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(getContext(),object.getString("comment"),Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // Appropriate error handling code
            }
            cRating.setText(rating);
            porgress.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids){
            try {
                URL url = new URL("http://codeforces.com/api/user.info?handles=+"+codeforces_id);
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
//            return null;
        }
    }




}
