package com.example.cookingbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GlobalRecipeCard extends AppCompatActivity {
    private String image;
    private String email;
    private String displayName;
    private String title;
    private String composition;
    private String description;
    private String recipeRef;
    private String avatarURL;
    private String recipesCount;
    private String phoneNumber;
    private String uid;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private DatabaseReference ref;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Recipe recipe = new Recipe(title, composition, description, image);
                mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("favorites");
                mRef.push().setValue(recipe);
                Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutAuthor:
                if (uid.equals("0")) {
                    Intent intent = new Intent(GlobalRecipeCard.this, UserInfo.class);
                    intent.putExtra("dispName", "Ким Андрей");
                    intent.putExtra("count", "Очень много=)");
                    intent.putExtra("phone", "+79050135580");
                    intent.putExtra("avatar", "android.resource://com.example.cookingbook/drawable/avatar");
                    intent.putExtra("email","kummu-97@mail.ru");
                    startActivity(intent);
                } else {
                    Query aboutAuthorQuery = ref.orderByChild("phone");
                    aboutAuthorQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Intent intent = new Intent(GlobalRecipeCard.this, UserInfo.class);
                            if (dataSnapshot.child("displayName").getValue() == null) {
                                intent.putExtra("dispName", "Cooking book");
                            } else {
                                intent.putExtra("dispName", dataSnapshot.child("displayName").getValue().toString());
                            }
                            if(dataSnapshot.child("count").getValue()==null){
                                Toast.makeText(getApplicationContext(),"Пользователь не удален или не существует",Toast.LENGTH_SHORT).show();
                            }else {
                                intent.putExtra("count", dataSnapshot.child("count").getValue().toString());
                            }
                            if(dataSnapshot.child("email").getValue()==null){
                                Toast.makeText(getApplicationContext(),"Пользователь не удален или не существует",Toast.LENGTH_SHORT).show();
                            }else {
                                intent.putExtra("email", dataSnapshot.child("email").getValue().toString());
                            }
                            if(dataSnapshot.child("number").getValue()==null){
                                intent.putExtra("phone","");
                            }
                            else {
                                intent.putExtra("phone", dataSnapshot.child("number").getValue().toString());
                            }
                            if(dataSnapshot.child("avatar").getValue()==null){
                                Toast.makeText(getApplicationContext(),"Пользователь не удален или не существует",Toast.LENGTH_SHORT).show();
                            }else {
                                intent.putExtra("avatar", dataSnapshot.child("avatar").getValue().toString());
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_recipe_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGlobal);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        image = getIntent().getExtras().getString("image");
        title = getIntent().getExtras().getString("title");
        composition = getIntent().getExtras().getString("composition");
        description = getIntent().getExtras().getString("description");
        recipeRef = getIntent().getExtras().getString("recipe_ref");
        displayName = getIntent().getExtras().getString("displayName");
        recipesCount = getIntent().getExtras().getString("recipesCount");
        avatarURL = getIntent().getExtras().getString("avatarURL");
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        email=getIntent().getExtras().getString("email");
        uid = getIntent().getExtras().getString("uid");

        //   Toast.makeText(this, recipeRef, Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(R.id.picGlobal);
        TextView displayNameView = findViewById(R.id.displayNameTextView);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarGlobal);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        TextView compositionView = findViewById(R.id.cardCompositionGlobal);
        TextView descriptionView = findViewById(R.id.cardDescriptionGlobal);
        Picasso.get()
                .load(Uri.parse(image))
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(imageView);
        collapsingToolbarLayout.setTitle(title);
        if (uid.equals("0")) {
            displayNameView.setText("Cooking book");
        } else {
            ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userInfo");
            Query query = ref.orderByChild("phone");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("displayName").getValue()==null){
                        displayNameView.setText("none");
                    }else {
                        displayNameView.setText(dataSnapshot.child("displayName").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        compositionView.setText(composition);
        descriptionView.setText(description);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
