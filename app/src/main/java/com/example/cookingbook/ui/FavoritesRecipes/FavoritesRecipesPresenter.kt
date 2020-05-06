package com.example.cookingbook.ui.FavoritesRecipes

import com.example.cookingbook.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions

class FavoritesRecipesPresenter(_mView:FavoritesRecipesContract.View): FavoritesRecipesContract.Presenter,FavoritesRecipesContract.onDeleteListener {
    val mView:FavoritesRecipesContract.View
    val mInteractor:FavoritesRecipesContract.Interactor
    init {
        mView=_mView
        mInteractor=FavoritesRecipesInteractor(this)
    }
    override fun onSuccess(message: String) {
        mView.onDeleteSuccess(message)
    }

    override fun onDeleteViewWasClicked(currentTitle:String) {
        mInteractor.performDeleteItem(currentTitle)
    }

    override fun getFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return mInteractor.performFirebaseRecyclerOptionsSettings();
    }

}