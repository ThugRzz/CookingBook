package com.example.cookingbook.ui.globalrecipes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cookingbook.*
import com.example.cookingbook.model.Recipe
import com.example.cookingbook.parcelablemodel.RecipeInfo
import com.example.cookingbook.util.ViewUtil
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class RecipesFragmentAdapter(options: FirebaseRecyclerOptions<Recipe>, _context: Context) : FirebaseRecyclerAdapter<Recipe, RecipesViewHolder>(options) {
    private val context: Context = _context
    private val viewUtil: ViewUtil = ViewUtil()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RecipesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int, model: Recipe) {
        holder.title.text = model.title
        holder.composition.text = model.composition
        viewUtil.putPicture(model.image, holder.image)
        holder.itemView.setOnClickListener {
            val recipeIntent = Intent(context, GlobalRecipeCard::class.java)
            recipeIntent.putExtra("RecipeInfo", RecipeInfo(model.title, model.composition, model.description, model.image))
            recipeIntent.putExtra("Uid", model.uid)
            context.startActivity(recipeIntent)
        }
    }
}