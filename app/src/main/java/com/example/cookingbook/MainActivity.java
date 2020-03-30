package com.example.cookingbook;

import android.os.Bundle;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private String count;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.textView);
        TextView textView1 = headerView.findViewById(R.id.login);
        ImageView avatarImage = headerView.findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        textView.setText(mAuth.getCurrentUser().getEmail());
        textView1.setText(mAuth.getCurrentUser().getDisplayName());
        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(avatarImage);
        updateUserInfo();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_recipes, R.id.nav_my_recipes, R.id.nav_favorites_recipes,
                R.id.nav_personal_cabinet, R.id.nav_about_us)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void updateUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");
        Query myQuery = reference.orderByChild("title");
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = Long.toString(dataSnapshot.getChildrenCount());
                Query infoQuery = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("userInfo");
                infoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("count").setValue(count);
                        dataSnapshot.getRef().child("displayName").setValue(mAuth.getCurrentUser().getDisplayName());
                        if (mAuth.getCurrentUser().getPhotoUrl() == null) {
                            dataSnapshot.getRef().child("avatar").setValue(R.drawable.nophoto);
                        } else {
                            dataSnapshot.getRef().child("avatar").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                        }
                        dataSnapshot.getRef().child("uid").setValue(mAuth.getCurrentUser().getUid());
                        dataSnapshot.getRef().child("email").setValue(mAuth.getCurrentUser().getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
