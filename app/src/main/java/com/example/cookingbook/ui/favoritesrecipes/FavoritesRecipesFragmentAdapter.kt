package com.example.cookingbook.ui.favoritesrecipes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cookingbook.R
import com.example.cookingbook.model.Recipe
import com.example.cookingbook.RecipeCard
import com.example.cookingbook.parcelablemodel.RecipeInfo
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso

class FavoritesRecipesFragmentAdapter(options: FirebaseRecyclerOptions<Recipe>, _context: Context):FirebaseRecyclerAdapter<Recipe,FavoritesViewHolder>(options){

    val context:Context = _context

    private var currentTitle:String = ""

    fun getCurrentTitle():String{
        return currentTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.favorites_list_item,parent,false)
        return FavoritesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int, model: Recipe) {
        holder.favoriteTitle.text = model.title
        holder.favoriteComposition.text = model.composition
        Picasso.get()
                .load((Uri.parse(model.image)))
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(holder.favoriteImage)
        holder.itemView.setOnLongClickListener{
            currentTitle = getItem(position).title
            false
        }

        holder.itemView.setOnClickListener{
            val recipeIntent = Intent(context,RecipeCard::class.java)
            recipeIntent.putExtra("RecipeInfo", RecipeInfo(model.title, model.composition, model.description, model.image))
            context.startActivity(recipeIntent)
        }
    }

}