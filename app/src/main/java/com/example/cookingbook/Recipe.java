package com.example.cookingbook;

public class Recipe {
    private String title;
    private String description;
    private String composition;
    private String image;
    private String displayName;
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

    public void setUid(String uid){this.uid=uid;}

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

    public String getUid(){return this.uid;}
}
