package com.example.supraja_pc.idealmart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.supraja_pc.idealmart.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
public class ProductDetailsActivity extends AppCompatActivity {

    public String category1;
    private ImageView productImage;
    private ElegantNumberButton numberBtn;
    private TextView productName, productDescription, productPrice;
    private String productID = "";
    private String imageUrl;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");

        Button addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        numberBtn = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);

        getProductDetails(productID);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList(){
        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            String uid = user.getUid();

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("pid", productID);
            cartMap.put("pname", productName.getText().toString());
            cartMap.put("price", productPrice.getText().toString());
            cartMap.put("date", saveCurrentDate);
            cartMap.put("time", saveCurrentTime);
            cartMap.put("quantity", numberBtn.getNumber());
            cartMap.put("image",imageUrl);

            cartListRef.child(uid).child(productID)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailsActivity.this, "Successfully added to Cart", Toast.LENGTH_SHORT).show();
                                if(category1.equals("Tea and Coffee")){
                                    Intent i = new Intent(ProductDetailsActivity.this, Tea.class);
                                    startActivity(i);
                                }
                                else if(category1.equals("Sarees")){
                                    Intent i = new Intent(ProductDetailsActivity.this, Saree.class);
                                    startActivity(i);
                                }
                                else if(category1.equals("Handicrafts")){
                                    Intent i = new Intent(ProductDetailsActivity.this, Handicrafts.class);
                                    startActivity(i);
                                }
                                finish();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(ProductDetailsActivity.this,"Please Signin To add to your cart",Toast.LENGTH_SHORT).show();
        }

    }

    private void getProductDetails(final String productID){
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    category1=products.getCategory();
                    imageUrl = products.getImage();
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
