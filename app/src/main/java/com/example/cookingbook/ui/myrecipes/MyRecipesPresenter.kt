package com.example.cookingbook.ui.myrecipes

import android.content.Context
import android.content.Intent

import com.example.cookingbook.RecipeCard
import com.example.cookingbook.model.Recipe

import com.example.cookingbook.ui.newrecipe.NewRecipeActivity

import com.firebase.ui.database.FirebaseRecyclerOptions


class MyRecipesPresenter(_mView: MyRecipesContract.View) : MyRecipesContract.Presenter, MyRecipesContract.onActionListener {
    private val mView = _mView
    private val mInteractor: MyRecipesInteractor = MyRecipesInteractor(this)

    override fun onDeleteItemWasClicked(currentTitle:String) {
        mInteractor.performDeleteItem(currentTitle)
    }

    override fun onShareItemWasClicked(currentTitle: String) {
        mInteractor.performShareItem(currentTitle)
    }

    override fun onRecipeViewWasClicked(context: Context) {
        val moveToRecipeCardIntent = Intent(context, RecipeCard::class.java)
        context.startActivity(moveToRecipeCardIntent)
    }

    override fun getFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return mInteractor.setFirebaseRecyclerOptionsSettings()
    }

    override fun getSearchFirebaseRecyclerOptionsSettings(searchString: String): FirebaseRecyclerOptions<Recipe> {
        return mInteractor.setSearchFirebaseRecyclerOptionsSettings(searchString)
    }

    override fun onCreateRecipeViewWasClicked(context: Context) {
        val createRecipeIntent = Intent(context,NewRecipeActivity::class.java)
        context.startActivity(createRecipeIntent)
    }

    override fun onResume() {
        mInteractor.getRecipesCount()
    }

    override fun onSuccess(message: String) {
        mView.onActionSuccess(message)
    }
}