package com.example.cookingbook.ui.globalrecipes

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.model.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions

interface RecipesContract {
    interface View

    interface Presenter {
        fun onRecipeViewWasClicked(context: Context)
        fun onSearchViewWasTyping(searchString: String, recyclerView: RecyclerView, context: Context)
        fun showRecipeList(context: Context, recyclerView: RecyclerView)
        fun getFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe>
        fun getSearchFirebaseRecyclerOptionsSettings(searchString: String): FirebaseRecyclerOptions<Recipe>
    }

    interface Interactor {
        fun getRecipeListFound(searchString: String, recyclerView: RecyclerView, context: Context)
        fun getRecipeList(context: Context, recyclerView: RecyclerView)
        fun setFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe>
        fun setSearchFirebaseRecyclerOptionsSettings(searchString: String): FirebaseRecyclerOptions<Recipe>
    }
}