package com.example.cookingbook.ui.MyRecipes

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_list_item.view.*

class MyRecipesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

    val MENU_SHARE = 1
    val MENU_DELETE = 2
    val myTitle = itemView.myTitle
    val myComposition = itemView.myComposition
    val myImage = itemView.myImage

    init {
        itemView.setOnCreateContextMenuListener(this)
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu!!.add(0, MENU_SHARE, 0, "Share")
        menu.add(0, MENU_DELETE, 0, "Delete")
    }
}