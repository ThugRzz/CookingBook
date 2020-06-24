package com.example.cookingbook.ui.globalrecipes

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class RecipesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val title: TextView = itemView.title
    val composition: TextView = itemView.composition
    val image:ImageView = itemView.image
}