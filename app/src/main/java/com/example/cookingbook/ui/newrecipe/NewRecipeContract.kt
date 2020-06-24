package com.example.cookingbook.ui.newrecipe

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

interface NewRecipeContract {
    interface View{
        fun onReadyActivityStartForResult(intent: Intent)
        fun onPushRecipeSuccess(message:String)
        fun onPushRecipeFailure(message: String)
    }
    interface Presenter{
        fun onConfirmButtonWasClicked(localImageUrl:Uri,title: String,composition: String,description: String)
        fun onChangeImageButtonWasClicked(packageManager: PackageManager?)

    }
    interface Interactor{
        fun performPushRecipe(localImageUrl: Uri,title: String,composition: String,description: String)
    }

    interface onPushRecipeListener{
        fun onSuccess(message:String)
        fun onFailure(message:String)
    }
}