package com.example.cookingbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

public class UserInfo extends AppCompatActivity {

    private String avatar,phone,count,displayName;
    private ImageView avatarView;
    private TextView phoneView,countView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = findViewById(R.id.toolbarUserInfo);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        avatar=getIntent().getExtras().getString("avatar");
        phone = getIntent().getExtras().getString("phone");
        count=getIntent().getExtras().getString("count");
        displayName=getIntent().getExtras().getString("dispName");
        avatarView=findViewById(R.id.avatarUserInfo);
        phoneView=findViewById(R.id.phUserInfoTextView);
        countView=findViewById(R.id.counterUserInfoTextView);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarUserInfo);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        if(!displayName.equals("Cooking book")) {
            Picasso.get()
                    .load(Uri.parse(avatar))
                    .placeholder(R.drawable.defaultimage)
                    .fit()
                    .centerInside()
                    .into(avatarView);
            collapsingToolbarLayout.setTitle(displayName);
            phoneView.setText(phone);
            countView.setText(count);
        }else {
            Picasso.get()
                    .load(R.drawable.avatar)
                    .placeholder(R.drawable.defaultimage)
                    .fit()
                    .centerInside()
                    .into(avatarView);
            collapsingToolbarLayout.setTitle("Kim Andrey");
            phoneView.setText("+79050135580");
            countView.setText("Очень много:)");
        }
    }
}
