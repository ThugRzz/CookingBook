package com.example.cookingbook.ui.myrecipes

import android.content.Context

import com.example.cookingbook.model.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions

interface MyRecipesContract {
    interface onActionListener{
        fun onSuccess(message: String)
    }
    interface View{
        fun onActionSuccess(message:String)
    }
    interface Presenter{
        fun onDeleteItemWasClicked(currentTitle: String)
        fun onShareItemWasClicked(currentTitle: String)
        fun onRecipeViewWasClicked(context: Context)
        fun getFirebaseRecyclerOptionsSettings():FirebaseRecyclerOptions<Recipe>
        fun getSearchFirebaseRecyclerOptionsSettings(searchString: String):FirebaseRecyclerOptions<Recipe>
        fun onResume()
    }
    interface Interactor{
        fun performDeleteItem(currentTitle:String)
        fun performShareItem(currentTitle: String)
        fun setFirebaseRecyclerOptionsSettings():FirebaseRecyclerOptions<Recipe>
        fun setSearchFirebaseRecyclerOptionsSettings(searchString:String):FirebaseRecyclerOptions<Recipe>
        fun getRecipesCount()
    }
}