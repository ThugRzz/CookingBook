package com.example.cookingbook.ui.globalrecipes

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.GlobalRecipeCard
import com.example.cookingbook.model.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions

class RecipesPresenter : RecipesContract.Presenter {
    private val mInteractor: RecipesContract.Interactor

    init {
        mInteractor = RecipesInteractor()
    }

    override fun onRecipeViewWasClicked(context: Context) {
        val moveToRecipeCardIntent = Intent(context, GlobalRecipeCard::class.java)
        context.startActivity(moveToRecipeCardIntent)
    }

    override fun onSearchViewWasTyping(searchString: String, recyclerView: RecyclerView, context: Context) {
        mInteractor.getRecipeListFound(searchString, recyclerView, context)
    }

    override fun showRecipeList(context: Context, recyclerView: RecyclerView) {
        mInteractor.getRecipeList(context, recyclerView)
    }

    override fun getFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return mInteractor.setFirebaseRecyclerOptionsSettings()
    }

    override fun getSearchFirebaseRecyclerOptionsSettings(searchString: String): FirebaseRecyclerOptions<Recipe> {
        return mInteractor.setSearchFirebaseRecyclerOptionsSettings(searchString)
    }
}