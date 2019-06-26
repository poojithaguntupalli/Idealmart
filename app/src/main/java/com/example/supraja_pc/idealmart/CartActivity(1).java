package com.example.supraja_pc.idealmart;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supraja_pc.idealmart.Model.Cart;
import com.example.supraja_pc.idealmart.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;
    private int TotalPrice = 0;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            setContentView(R.layout.activity_cart);

            recyclerView = findViewById(R.id.cart_list);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            NextProcessBtn = findViewById(R.id.next_process_btn);
            txtTotalAmount = findViewById(R.id.total_price);

            uid = firebaseAuth.getCurrentUser().getUid();
            NextProcessBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    i.putExtra("Total Price", String.valueOf(TotalPrice));
                    startActivity(i);
                    finish();
                }
            });
        }
        else{
            setContentView(R.layout.cart_not_signin);
        }

    }

    protected void onStart(){
        super.onStart();


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
            FirebaseRecyclerOptions<Cart> options =
                    new FirebaseRecyclerOptions.Builder<Cart>()
                            .setQuery(cartListRef.child(uid), Cart.class)
                            .build();
            FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                    = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                    holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                    holder.txtProductPrice.setText("Price = " + model.getPrice() + "Rupees per 1 quantity");
                    holder.txtProductName.setText(model.getPname());

                    int oneTypeProductPrice = ((Integer.valueOf(model.getQuantity()))) * ((Integer.valueOf(model.getPrice())));
                    TotalPrice = TotalPrice + oneTypeProductPrice;
                    txtTotalAmount.setText("Total Price = " + String.valueOf(TotalPrice) + "rupees");
                    Picasso.get().load(model.getImage()).into(holder.imgProduct);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[] = new CharSequence[]
                                    {
                                            "Edit",
                                            "Remove"
                                    };
                            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                            builder.setTitle("Cart options:");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int p) {
                                    if (p == 0) {
                                        Intent i = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                        i.putExtra("pid", model.getPid());
                                        startActivity(i);
                                    }
                                    if (p == 1) {
                                        cartListRef.child(uid)
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CartActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();

                                                            Intent i = new Intent(CartActivity.this, Home.class);
                                                            startActivity(i);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                }

                @NonNull
                @Override
                public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                    CartViewHolder holder = new CartViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
        else{
            Toast.makeText(this,"Please signin to display your cart items",Toast.LENGTH_SHORT).show();
        }
    }
}
