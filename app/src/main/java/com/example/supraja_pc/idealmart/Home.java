package com.example.supraja_pc.idealmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supraja_pc.idealmart.Model.Users;
import com.example.supraja_pc.idealmart.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.supraja_pc.idealmart.Login.EXTRA_MESSAGE;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog loadingBar;
    Button sarees,handicrafts,tea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView user = (TextView) navigationView.getHeaderView(0).findViewById(R.id.currentuser);
        final FirebaseUser currentusers = FirebaseAuth.getInstance().getCurrentUser();
        if(currentusers!=null){
            final String currentemail = currentusers.getEmail();
            user.setText(currentemail);
        }else {
            user.setText("Guest");
        }


        sarees = findViewById(R.id.sarees);
        handicrafts=findViewById(R.id.handicrafts);
        tea=findViewById(R.id.tea);

        sarees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,Saree.class);
                startActivity(i);
            }
        });

        handicrafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,Handicrafts.class);
                startActivity(i);
            }
        });

        tea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Home.this,Tea.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();

            Intent i = new Intent(Home.this,Home.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.orders) {
            Intent i = new Intent(Home.this,Myorders.class);
            startActivity(i);
        } else if (id == R.id.cart) {
            Intent i = new Intent(Home.this,CartActivity.class);
            startActivity(i);
        } else if (id == R.id.settings) {
            Intent i = new Intent(Home.this,Settings.class);
            startActivity(i);
        } else if (id == R.id.service) {
            Intent i = new Intent(Home.this,Service.class);
            startActivity(i);
        } else if (id == R.id.sell) {
            Intent i=new Intent(Home.this,Sell.class);
            startActivity(i);

        } else if (id == R.id.signin) {

            final FirebaseUser currentusers = FirebaseAuth.getInstance().getCurrentUser();
            if(currentusers!=null){
                Toast.makeText(this,"Already Signed in",Toast.LENGTH_SHORT).show();
            }else {
                Intent i = new Intent(Home.this,Login.class);
                startActivity(i);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
