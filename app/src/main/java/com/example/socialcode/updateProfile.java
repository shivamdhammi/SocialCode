package com.example.socialcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class updateProfile extends Fragment {

    private ImageView pic,verification;
    private static final int CHOOSE_IMAGE = 101;
    private TextView Name,College,Email,Codeforces,Hackerarnk,verify,resetpasword;
    private LinearLayout verifylayout;

    private Uri uriProfileImage;//uriZProfileImage = data.getData();[inside startActivityForResult()]
    private String profileImageUrl;//To store the Downloaded URL of the image
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private StorageReference storageReference;
    private String userId;
    private Button Save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_update_profile, container, false);

        Name = view.findViewById(R.id.uprofile_name);
        College = view.findViewById(R.id.uprofile_college);
        Email = view.findViewById(R.id.uprofile_email);
        Codeforces = view.findViewById(R.id.uprofile_codeforces);
        Hackerarnk = view.findViewById(R.id.uprofile_hackerrank);

        Save = view.findViewById(R.id.uprofile_save);
        pic = view.findViewById(R.id.uprofile_pic);
        verification = view.findViewById(R.id.uprofile_verification);
        verify = view.findViewById(R.id.uprofile_verify);
        verifylayout = view.findViewById(R.id.uprofile_verifylayout);

        auth = FirebaseAuth.getInstance();

//        Email.setText(auth.getCurrentUser().getEmail());
        //Toast.makeText(getApplicationContext(),test,Toast.LENGTH_LONG).show();

        ref = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
                //Store the image and display name in Firebase Storage.
                //we use profileImageURL to store it to Storage.

                //////

                userInfo newInfo = new userInfo
                        (Name.getText().toString(),College.getText().toString(),Email.getText().toString()
                                ,Codeforces.getText().toString(),Hackerarnk.getText().toString());

                ref.child(auth.getCurrentUser().getUid()).child("Info").setValue(newInfo);
                /////


                //Test krne ke liye
                /*Intent intent = new Intent(getApplicationContext(),SProfile.class);
                startActivity(intent);*/
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email.setError("Email can't be changed");
                Email.requestFocus();
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageChooser();

                //to select the image from the device.

            }
        });

//        resetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.sendPasswordResetEmail(Email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(getContext(),"Visit your email to reset your password.",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            }
//        });
//
//
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(auth.getCurrentUser().isEmailVerified()){
                    verify.setText("Verified Account");
                    verification.setImageResource(R.drawable.ic_verified_user);
                }
                else {
                    verify.setText("Not Verified.");
                    verification.setImageResource(R.drawable.ic_info_black_24dp);
                    auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Verification Email has been sent", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });



        return view;
    }

    //onStart will retrieve the data from realTime Database and set the value to the corresponding fields.
    @Override
    public void onStart() {
        super.onStart();
        //Toast.makeText(getApplicationContext(),"Ye chl rha hai",Toast.LENGTH_LONG).show();

        //myRef.child(auth.getCurrentUser().getDisplayName().toString());

        //Log.d("dikkat","Conatct"+myRef.child(auth.getCurrentUser().getUid()).child("contact"));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userInfo userinfo = dataSnapshot.child(auth.getCurrentUser().getUid()).child("Info").getValue(userInfo.class);

                Name.setText(userinfo.getName());
                College.setText(userinfo.getCollege());
                Email.setText(userinfo.getEmail());
                Codeforces.setText(userinfo.getCodeforces());
                Hackerarnk.setText(userinfo.getHackerrank());
                //Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                if(auth.getCurrentUser().isEmailVerified()){
                    verify.setText("Verified Account");
                    verification.setImageResource(R.drawable.ic_verified_user);
//                    Toast.makeText(getContext(),"yeb email "+auth.getCurrentUser().isEmailVerified(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });


        try {
//            Toast.makeText(getContext(),auth.getCurrentUser().getUid().toString(),Toast.LENGTH_LONG).show();
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

    //saves the users info to the Storage.
    private void saveUserInfo() {

        FirebaseUser user = auth.getCurrentUser();

        if(user !=null && profileImageUrl!=null){

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Name.getText().toString())
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();



            //user.updateProfile() default function to update user's info.
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Profile updated",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    //this function let us choose a image from the images in the device.
    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //to Upload a image of our choice.
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }


    //We override the onActivityResult to set the selected image to imageView(in this case , profilePic).
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uriProfileImage);
                pic.setImageBitmap(bitmap);

                //To upload image to Firebase Storage.
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/"+auth.getCurrentUser().getUid()+ ".jpg");

        if(uriProfileImage!=null){

            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    profileImageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}