package com.example.cookingbook.ui.myrecipes

import android.view.ContextMenu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.constant.MenuItems
import kotlinx.android.synthetic.main.my_list_item.view.*

class MyRecipesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

    val myTitle: TextView = itemView.myTitle
    val myComposition: TextView = itemView.myComposition
    val myImage: ImageView = itemView.myImage

    init {
        itemView.setOnCreateContextMenuListener(this)
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu!!.add(0, MenuItems.MENU_SHARE, 0, "Share")
        menu.add(0, MenuItems.MENU_DELETE, 0, "Delete")
    }
}