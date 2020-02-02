package com.example.cookingbook;

public class Recipe {
    private String title;
    private String description;
    private String composition;
    private int image;
    public Recipe(String title, String composition,String description,int image){
        this.title=title;
        this.composition=composition;
        this.description=description;
        this.image=image;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setComposition(String composition){
        this.composition=composition;
    }
    public void setImage(int image){
        this.image=image;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDescription(){
        return this.description;
    }
    public String getComposition(){
        return this.composition;
    }
    public int getImage(){
        return this.image;
    }
}
