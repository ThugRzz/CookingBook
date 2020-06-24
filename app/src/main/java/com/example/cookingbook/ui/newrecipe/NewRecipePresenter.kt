package com.example.cookingbook.ui.newrecipe

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

class NewRecipePresenter(_mView: NewRecipeContract.View) : NewRecipeContract.Presenter,NewRecipeContract.onPushRecipeListener {
    private val mView: NewRecipeContract.View = _mView
    private val mInteractor: NewRecipeContract.Interactor = NewRecipeInteractor(this)
    override fun onConfirmButtonWasClicked(localImageUrl: Uri, title: String, composition: String, description: String) {
        mInteractor.performPushRecipe(localImageUrl, title, composition, description)
    }

    override fun onChangeImageButtonWasClicked(packageManager:PackageManager?) {
        val changeImageIntent:Intent = Intent(Intent.ACTION_GET_CONTENT)
        changeImageIntent.type = "image/*"
        if(changeImageIntent.resolveActivity(packageManager!!)!=null)
            mView.onReadyActivityStartForResult(changeImageIntent)
    }

    override fun onSuccess(message: String) {
        mView.onPushRecipeSuccess(message)
    }

    override fun onFailure(message: String) {
        mView.onPushRecipeFailure(message)
    }

}