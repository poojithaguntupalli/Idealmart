package com.example.supraja_pc.idealmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supraja_pc.idealmart.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sell extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.supraja_pc.idealmart.MESSAGE";

    private EditText InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        Button loginButton = findViewById(R.id.login);
        Button joinButton = findViewById(R.id.join_now_btn);
        InputPassword = findViewById(R.id.login_password_input);
        InputPhoneNumber =  findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Sell.this,Sellsignup.class);
                startActivity(i);
                finish();
            }
        });
    }



    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password);
        }
    }



    private void AllowAccessToAccount(final String phone, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Sellers").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Sellers").child(phone).getValue(Users.class);

                    assert usersData != null;
                    System.out.print(usersData.getPassword());
                        if (password.equals(usersData.getPassword()))
                        {
                                //Toast.makeText(Sell.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Sell.this, Uploadproduct.class);
                                intent.putExtra(EXTRA_MESSAGE,phone);
                                startActivity(intent);
                                finish();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(Sell.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                }
                else
                {
                    Toast.makeText(Sell.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
