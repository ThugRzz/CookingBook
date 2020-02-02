package com.example.cookingbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_card);
        int image=getIntent().getExtras().getInt("image");
        String title=getIntent().getExtras().getString("title");
        String composition=getIntent().getExtras().getString("composition");
        String description=getIntent().getExtras().getString("description");
        ImageView imageView = findViewById(R.id.pic);
        TextView titleView = findViewById(R.id.cardTitle);
        TextView compositionView = findViewById(R.id.cardComposition);
        TextView descriptionView = findViewById(R.id.cardDescription);
        imageView.setImageResource(image);
        titleView.setText(title);
        compositionView.setText(composition);
        descriptionView.setText(description);
    }
}
