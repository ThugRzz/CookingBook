package com.example.cookingbook;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewUtil {
    public void putPicture(String stringUri, ImageView view){
        Picasso.get()
                .load(Uri.parse(stringUri))
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerCrop()
                .into(view);
    }

}
