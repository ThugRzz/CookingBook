package com.example.cookingbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RecipeCard extends AppCompatActivity {
    private String image;
    private String title;
    private String composition;
    private String description;
    private String recipeRef;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Recipe recipe = new Recipe(title,composition,description,image);
                mRef=mDatabase.getReference().child("recipes");
                mRef.push().setValue(recipe);
                Toast.makeText(RecipeCard.this,"Вы поделились рецептом!",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        image = getIntent().getExtras().getString("image");
        title = getIntent().getExtras().getString("title");
        composition = getIntent().getExtras().getString("composition");
        description = getIntent().getExtras().getString("description");
        recipeRef=getIntent().getExtras().getString("recipe_ref");
        Toast.makeText(this,recipeRef,Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(R.id.pic);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        TextView compositionView = findViewById(R.id.cardComposition);
        TextView descriptionView = findViewById(R.id.cardDescription);
        Picasso.get()
                .load(Uri.parse(image))
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(imageView);
        collapsingToolbarLayout.setTitle(title);
        compositionView.setText(composition);
        descriptionView.setText(description);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
