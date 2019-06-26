package com.example.supraja_pc.idealmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supraja_pc.idealmart.Model.Users;
import com.example.supraja_pc.idealmart.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.supraja_pc.idealmart.MESSAGE";

    private EditText InputEmail, InputPassword;
    private Button LoginButton,SignupButton;
    private ProgressDialog loadingBar;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        LoginButton = (Button) findViewById(R.id.login_btn);
        SignupButton = findViewById(R.id.join_now_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputEmail = (EditText) findViewById(R.id.login_email_input);

        loadingBar = new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });
        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login.this,Register.class);
                startActivity(i);
                finish();
            }
        });
    }



    private void LoginUser()
    {
        final String email = InputEmail.getText().toString();
        final String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your Email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
           loadingBar.setMessage("Please Wait...");
            loadingBar.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                loadingBar.dismiss();
                                if (password.length() < 6) {
                                    Toast.makeText(Login.this,getString(R.string.min_password),Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                loadingBar.dismiss();
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra(EXTRA_MESSAGE,email);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }

    public void clicker(View v) {
        Intent i = new Intent(Login.this,ForgotPassword.class);
        startActivity(i);
    }
}
