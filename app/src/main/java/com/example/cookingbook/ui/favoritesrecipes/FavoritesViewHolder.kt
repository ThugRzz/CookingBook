package com.example.cookingbook.ui.favoritesrecipes

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.constant.MenuItems
import kotlinx.android.synthetic.main.favorites_list_item.view.*

class FavoritesViewHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
    val favoriteTitle = itemView.favoriteTitle
    val favoriteComposition = itemView.favoriteComposition
    val favoriteImage = itemView.favoriteImage

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu!!.add(0, MenuItems.MENU_DELETE,0,"Удалить")
    }
}