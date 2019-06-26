package com.example.supraja_pc.idealmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class ProfileEdit extends AppCompatActivity {
    EditText name,phone,email;
    Button profile_btn;
    private ProgressDialog loadingBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference UsersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            setContentView(R.layout.activity_profile_edit);

            name = findViewById(R.id.name);
            phone = findViewById(R.id.phone);
            email = findViewById(R.id.mail);
            profile_btn = findViewById(R.id.edit_profile_btn);

            loadingBar = new ProgressDialog(this);
            profile_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateProfile();
                }
            });
        }else {
            setContentView(R.layout.cart_not_signin);
        }
    }

    private void UpdateProfile(){
        final String username = name.getText().toString();
        final String userphone = phone.getText().toString();
        final String useremail = email.getText().toString();


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your  House Number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(useremail)) {
            Toast.makeText(this, "Please enter your Street Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userphone)) {
            Toast.makeText(this, "Please enter your City Name...", Toast.LENGTH_SHORT).show();
        }  else {
            loadingBar.setTitle("Updating Profile");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null) {
                user.updateEmail(useremail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                    userdataMap.put("Name", username);
                                    userdataMap.put("Email", useremail);
                                    userdataMap.put("Phone", userphone);
                                    String uid = firebaseAuth.getCurrentUser().getUid();

                                    UsersRef = FirebaseDatabase.getInstance().getReference();
                                    UsersRef.child("Users").child(uid).updateChildren(userdataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(ProfileEdit.this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(ProfileEdit.this, Settings.class);
                                                        startActivity(i);
                                                    } else {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(ProfileEdit.this, "Error in updating your profile", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(ProfileEdit.this, Settings.class);
                                                        startActivity(i);
                                                    }
                                                }
                                            });
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(ProfileEdit.this, "Error in updating email", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ProfileEdit.this, Settings.class);
                                    startActivity(i);
                                }
                            }
                        });
            }else{
                Toast.makeText(ProfileEdit.this,"Please sign in to update your profile",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileEdit.this,Settings.class);
                startActivity(i);
            }
        }
    }
}
