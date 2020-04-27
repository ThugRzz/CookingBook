package com.example.cookingbook.ui.FavoritesRecipes

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.favorites_list_item.view.*

class FavoritesViewHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
    val MENU_DELETE = 2
    val favoriteTitle = itemView.favoriteTitle
    val favoriteComposition = itemView.favoriteComposition
    val favoriteImage = itemView.favoriteImage

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu!!.add(0,MENU_DELETE,0,"Удалить")
    }
}