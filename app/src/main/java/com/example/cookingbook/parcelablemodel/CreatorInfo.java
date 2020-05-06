package com.example.cookingbook.parcelablemodel;

import android.os.Parcel;
import android.os.Parcelable;

public class CreatorInfo implements Parcelable {

    private String displayName;
    private String phone;
    private String email;
    private String recipesCount;
    private String imageUri;

    public CreatorInfo(String diplayName, String phone, String email, String recipesCount, String imageUri) {
        this.displayName = diplayName;
        this.phone = phone;
        this.email = email;
        this.recipesCount = recipesCount;
        this.imageUri = imageUri;
    }

    public CreatorInfo(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        displayName =data[0];
        phone=data[1];
        email=data[2];
        recipesCount=data[3];
        imageUri=data[4];
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecipesCount() {
        return recipesCount;
    }

    public void setRecipesCount(String recipesCount) {
        this.recipesCount = recipesCount;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public static final Creator<CreatorInfo> CREATOR = new Creator<CreatorInfo>() {
        @Override
        public CreatorInfo createFromParcel(Parcel in) {
            return new CreatorInfo(in);
        }

        @Override
        public CreatorInfo[] newArray(int size) {
            return new CreatorInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                displayName,phone,email,recipesCount,imageUri
        });
    }
}
