package com.example.cookingbook.ui.newrecipe

import android.content.Intent
import android.content.pm.PackageManager

class NewRecipePresenter(_mView: NewRecipeContract.View) : NewRecipeContract.Presenter {
    private val mView: NewRecipeContract.View = _mView
    private val mInteractor: NewRecipeContract.Interactor = NewRecipeInteractor()

    override fun onChangeImageButtonWasClicked(packageManager:PackageManager) {
        val changeImageIntent:Intent = Intent(Intent.ACTION_GET_CONTENT)
        changeImageIntent.setType("image/*")
        if(changeImageIntent.resolveActivity(packageManager)!=null)
            mView.onReadyActivityStartForResult(changeImageIntent)
    }

}