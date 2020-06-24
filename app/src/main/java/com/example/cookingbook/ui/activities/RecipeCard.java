package com.example.cookingbook.ui.activities;


import androidx.appcompat.app.ActionBar;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookingbook.R;
import com.example.cookingbook.base.BaseActivity;
import com.example.cookingbook.parcelablemodel.RecipeInfo;
import com.example.cookingbook.util.ViewUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class RecipeCard extends BaseActivity {
    private String image;
    private String title;
    private String composition;
    private String description;
    private ViewUtil viewUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        viewUtil = new ViewUtil();
        RecipeInfo recipeInfo = (RecipeInfo)getIntent().getParcelableExtra("RecipeInfo");
        title = recipeInfo.getTitle();
        image = recipeInfo.getImageUrl();
        composition = recipeInfo.getComposition();
        description = recipeInfo.getDescription();
        ImageView imageView = findViewById(R.id.pic);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        TextView compositionView = findViewById(R.id.cardComposition);
        TextView descriptionView = findViewById(R.id.cardDescription);
        viewUtil.putPicture(image,imageView);
        collapsingToolbarLayout.setTitle(title);
        compositionView.setText(composition);
        descriptionView.setText(description);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
