package com.example.cookingbook.ui.Recipes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class RecipesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val title = itemView.title
    val composition = itemView.composition
    val image = itemView.image
}