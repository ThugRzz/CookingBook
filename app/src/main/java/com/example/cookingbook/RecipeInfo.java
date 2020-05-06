package com.example.cookingbook;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeInfo implements Parcelable {

    private String title;
    private String composition;
    private String description;
    private String imageUrl;

    public RecipeInfo(String title, String composition, String description, String imageUrl) {
        this.title = title;
        this.composition = composition;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public RecipeInfo(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        title = data[0];
        composition=data[1];
        description=data[2];
        imageUrl=data[3];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                title,composition,description,imageUrl
        });
    }
    public static final Creator<RecipeInfo> CREATOR = new Creator<RecipeInfo>() {
        @Override
        public RecipeInfo createFromParcel(Parcel in) {
            return new RecipeInfo(in);
        }

        @Override
        public RecipeInfo[] newArray(int size) {
            return new RecipeInfo[size];
        }
    };
}
