package com.example.cookingbook.ui.personalcabinet.changeprofile

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

interface ChangeProfileContract {
    interface onChangeSuccessListener{
        fun onSuccess(message:String)
    }
    interface View {
        fun onReadyActivityStartForResult(intent: Intent)
        fun onChangeSuccess(message:String)
    }

    interface Presenter {
        fun onChangeImageButtonWasClicked(packageManager: PackageManager)
        fun onConfirmButtonWasClicked(photo: Uri,displayName: String,phoneNumber: String)
        fun showImage():Uri?
        fun showNickname():String
        fun showPhone():String
    }

    interface Interactor {
        fun getImage():Uri?
        fun getNickname():String
        fun getPhone(callback:FirebaseCallback)
        fun changeUserInfo(photo: Uri, displayName:String,phoneNumber:String )
    }
    interface FirebaseCallback{
        fun onCallBack(value:String)
    }
}