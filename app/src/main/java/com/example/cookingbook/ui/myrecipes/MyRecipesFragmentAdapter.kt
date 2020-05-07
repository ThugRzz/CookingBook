package com.example.cookingbook.ui.myrecipes

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

class MyRecipesFragmentAdapter(options: FirebaseRecyclerOptions<Recipe>, _context: Context) : FirebaseRecyclerAdapter<Recipe, MyRecipesViewHolder>(options) {
    private val context:Context = _context
    private val viewUtil = ViewUtil()
    private var currentTitle:String = ""

    fun getCurrentTitle():String{
        return currentTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_list_item,parent,false)
        return MyRecipesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyRecipesViewHolder, position: Int, model: Recipe) {
        holder.myTitle.text=model.title
        holder.myComposition.text=model.composition
        viewUtil.putPicture(model.image,holder.myImage)
        holder.itemView.setOnLongClickListener{
            currentTitle=getItem(position).title
            false
        }

        holder.itemView.setOnClickListener{
            val recipeIntent = Intent(context,RecipeCard::class.java)
            recipeIntent.putExtra("RecipeInfo", RecipeInfo(model.title, model.composition, model.description, model.image))
            context.startActivity(recipeIntent)
        }
    }
}