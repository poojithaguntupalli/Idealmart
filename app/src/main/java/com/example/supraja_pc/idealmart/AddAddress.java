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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddAddress extends AppCompatActivity {
    private Button add_address;
    private EditText house_no, street, city, state,pincode;
    private ProgressDialog loadingBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            setContentView(R.layout.activity_add_address);

            add_address = (Button) findViewById(R.id.add_address);
            house_no = (EditText) findViewById(R.id.house_no);
            street = findViewById(R.id.street);
            city = (EditText) findViewById(R.id.city);
            state = (EditText) findViewById(R.id.state);
            pincode = findViewById(R.id.pincode);

            loadingBar = new ProgressDialog(this);
            add_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Addaddress();
                }
            });
        } else {
            setContentView(R.layout.cart_not_signin);
        }
    }

    private void Addaddress() {
        final String house = house_no.getText().toString();
        final String street_name = street.getText().toString();
        final String city_name = city.getText().toString();
        final String state_name = state.getText().toString();
        final String pincode_number = pincode.getText().toString();


        if (TextUtils.isEmpty(house)) {
            Toast.makeText(this, "Please enter your  House Number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(street_name)) {
            Toast.makeText(this, "Please enter your Street Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(city_name)) {
            Toast.makeText(this, "Please enter your City Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(state_name)) {
            Toast.makeText(this, "Please enter your State Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pincode_number)) {
            Toast.makeText(this, "Please enter your Pincode Number...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Adding Address");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            String uid = firebaseAuth.getCurrentUser().getUid();
            final DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("house", house);
            userdataMap.put("street", street_name);
            userdataMap.put("city", city_name);
            userdataMap.put("state", state_name);
            userdataMap.put("pincode", pincode_number);
            ref.child("Address").child(uid).updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                loadingBar.dismiss();
                                Toast.makeText(AddAddress.this, "Congratulations, your address has been added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddAddress.this, Settings.class);
                                startActivity(intent);
                                finish();
                            } else {

                                loadingBar.dismiss();
                                Toast.makeText(AddAddress.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddAddress.this, Settings.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }

    }
}
