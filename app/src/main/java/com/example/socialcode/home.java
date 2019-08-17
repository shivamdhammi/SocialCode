package com.example.socialcode;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class home extends Fragment {

    private FloatingActionButton compiler;
    private TextView Name,College;
    private TextView cRating,cSubmission,cConstest;
    private TextView hRating,hubmission,hConstest;

    private Uri uriProfileImage;//uriZProfileImage = data.getData();[inside startActivityForResult()]
    private String profileImageUrl;//To store the Downloaded URL of the image
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private StorageReference storageReference;
    private ImageView pic,verification;
    private String userId;

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
        cRating = view.findViewById(R.id.home_crating);
        cSubmission = view.findViewById(R.id.home_csubmission);
        cConstest = view.findViewById(R.id.home_ccontset);
        hRating = view.findViewById(R.id.home_hrating);
        hubmission = view.findViewById(R.id.home_hsubmission);
        hConstest = view.findViewById(R.id.home_hcontset);

        ref = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userInfo userinfo = dataSnapshot.child(auth.getCurrentUser().getUid()).child("Info").getValue(userInfo.class);

                Name.setText(userinfo.getName());
                College.setText(userinfo.getCollege());
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
                    Glide.with(getContext()).
                            load(uri).
                            into(pic);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"@@@@@@@@@@@@@@@@",Toast.LENGTH_LONG).show();
            return;

        }


    }
}
