package com.example.cookingbook.ui.favoritesrecipes

import com.example.cookingbook.model.Recipe

import com.firebase.ui.database.FirebaseRecyclerOptions

class FavoritesRecipesPresenter(_mView:FavoritesRecipesContract.View): FavoritesRecipesContract.Presenter,FavoritesRecipesContract.OnDeleteListener {
    private val mView:FavoritesRecipesContract.View = _mView
    private val mInteractor:FavoritesRecipesContract.Interactor
    init {
        mInteractor=FavoritesRecipesInteractor(this)
    }
    override fun onSuccess(message: String) {
        mView.onDeleteSuccess(message)
    }

    override fun onDeleteViewWasClicked(currentTitle:String) {
        mInteractor.performDeleteItem(currentTitle)
    }

    override fun getFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return mInteractor.setFirebaseRecyclerOptionsSettings()
    }
}