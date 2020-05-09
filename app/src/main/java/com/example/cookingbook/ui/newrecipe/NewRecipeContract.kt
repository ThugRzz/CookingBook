package com.example.cookingbook.ui.newrecipe

import android.content.Intent
import android.content.pm.PackageManager

interface NewRecipeContract {
    interface View{
        fun onReadyActivityStartForResult(intent: Intent)
    }
    interface Presenter{


        fun onChangeImageButtonWasClicked(packageManager: PackageManager)

    }
    interface Interactor{
        fun pushRecipe(title:String, composition:String, description:String, image:String)
    }
}