package com.example.cookingbook;

public class Recipe {
    private String email;
    private String title;
    private String description;
    private String composition;
    private String image;
    private String displayName;
    private String avatarURL;
    private String recipesCount;
    private String phone;
    private String uid;

    Recipe(){

    }

    public Recipe(String title,String composition,String description, String image, String uid){
        this.title=title;
        this.composition=composition;
        this.description=description;
        this.image=image;
        this.uid=uid;
    }

    public Recipe(String title, String composition, String description,String image,String displayName,String avatarURL,String recipesCount,String phone){
        this.title=title;
        this.composition=composition;
        this.description=description;
        this.image=image;
        this.displayName=displayName;
        this.recipesCount=recipesCount;
        this.avatarURL=avatarURL;
        this.phone=phone;
    }

    public Recipe(String title, String composition, String description, String image) {
        this.title = title;
        this.composition = composition;
        this.description = description;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDisplayName(String displayName) {this.displayName=displayName;}

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getComposition() {
        return this.composition;
    }

    public String getImage() {
        return this.image;
    }

    public String getDisplayName(){return this.displayName;}

    public String getAvatarURL(){return this.avatarURL;}

    public String getRecipesCount(){return this.recipesCount;}

    public String getPhone(){return this.phone;}
    public String getUid(){return this.uid;}
}
