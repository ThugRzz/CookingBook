package com.example.cookingbook.ui.FavoritesRecipes

import com.example.cookingbook.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions

interface FavoritesRecipesContract {
    interface onDeleteListener {
        fun onSuccess(message: String)
    }

    interface View {
        fun onDeleteSuccess(message: String)
    }

    interface Presenter {
        fun onDeleteViewWasClicked(currentTitle: String)
        fun getFirebaseRecyclerOptionsSettings():FirebaseRecyclerOptions<Recipe>
    }


    interface Interactor {
        fun performDeleteItem(currentTitle: String)
        fun performFirebaseRecyclerOptionsSettings():FirebaseRecyclerOptions<Recipe>
    }
}