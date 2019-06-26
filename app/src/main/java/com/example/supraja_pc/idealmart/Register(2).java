package com.example.supraja_pc.idealmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword, InputEmail;
    private ProgressDialog loadingBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputEmail = findViewById(R.id.register_email_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_input);
        loadingBar = new ProgressDialog(this);


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        final String name = InputName.getText().toString();
        final String email = InputEmail.getText().toString();
        final String password = InputPassword.getText().toString();
        final String phone = InputPhoneNumber.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your Email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }  else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    boolean check = !task.getResult().getProviders().isEmpty();
                    if(!check){
                       loadingBar.setMessage("Registering Please Wait...");
                        loadingBar.show();
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    loadingBar.dismiss();
                                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                    String uid = firebaseAuth.getCurrentUser().getUid();
                                    final DatabaseReference ref;
                                    ref = FirebaseDatabase.getInstance().getReference();

                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                    userdataMap.put("name", name);
                                    userdataMap.put("email",email);
                                    userdataMap.put("phone", phone);
                                    ref.child("Users").child(uid).updateChildren(userdataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(Register.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();

                                                        Intent intent = new Intent(Register.this, Login.class);
                                                        startActivity(intent);
                                                    }
                                                    else
                                                    {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(Register.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d("hello","error");
                                    loadingBar.dismiss();
                                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Register.this,"Email already exists",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}