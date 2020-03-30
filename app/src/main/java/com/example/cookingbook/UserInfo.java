package com.example.cookingbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

public class UserInfo extends AppCompatActivity implements View.OnClickListener {

    private String avatar,phone,count,displayName,email;
    private ImageView avatarView;
    private TextView phoneView,countView,emailView;

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
        email=getIntent().getExtras().getString("email");
        avatarView=findViewById(R.id.avatarUserInfo);
        phoneView=findViewById(R.id.phUserInfoTextView);
        countView=findViewById(R.id.counterUserInfoTextView);
        emailView=findViewById(R.id.postUserInfoTextView);
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
            emailView.setText(email);
        }else {
            Picasso.get()
                    .load(avatar)
                    .placeholder(R.drawable.defaultimage)
                    .fit()
                    .centerInside()
                    .into(avatarView);
            collapsingToolbarLayout.setTitle(displayName);
            phoneView.setText(phone);
            countView.setText(count);
            emailView.setText(email);
        }
        phoneView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phUserInfoTextView:
                final AlertDialog callDialog= new AlertDialog.Builder(
                        UserInfo.this).setMessage("Вы действительно хотите позвонить?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneView.getText().toString()));
                        startActivity(callIntent);
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

                callDialog.show();
                callDialog.getButton(callDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                callDialog.getButton(callDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        }
    }
}
