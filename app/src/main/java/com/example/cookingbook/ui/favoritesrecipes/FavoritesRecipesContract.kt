package com.example.cookingbook.ui.favoritesrecipes

import com.example.cookingbook.model.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions

interface FavoritesRecipesContract {
    interface OnDeleteListener {
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
        fun setFirebaseRecyclerOptionsSettings():FirebaseRecyclerOptions<Recipe>
    }
}