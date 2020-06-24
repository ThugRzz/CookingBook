package com.example.cookingbook.ui.personalcabinet.changeprofile

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

class ChangeProfilePresenter(_mView: ChangeProfileContract.View) : ChangeProfileContract.Presenter, ChangeProfileContract.onChangeSuccessListener {
    private val mView: ChangeProfileContract.View = _mView
    private val mInteractor: ChangeProfileContract.Interactor = ChangeProfileInteractor(this)
    override fun onChangeImageButtonWasClicked(packageManager: PackageManager) {
        val changeImageIntent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        changeImageIntent.setType("image/*")
        if (changeImageIntent.resolveActivity(packageManager) != null) {
            mView.onReadyActivityStartForResult(changeImageIntent)
        }
    }

    override fun onConfirmButtonWasClicked(photo: Uri, displayName: String, phoneNumber: String) {
        mInteractor.changeUserInfo(photo, displayName, phoneNumber)
    }

    override fun showImage(): Uri? {
        return mInteractor.getImage()
    }

    override fun showNickname(): String {
        return mInteractor.getNickname()
    }

    override fun showPhone(): String {
        var phoneNumber: String = ""
        mInteractor.getPhone(object : ChangeProfileContract.FirebaseCallback {
            override fun onCallBack(value: String) {
                phoneNumber = value
            }

        })
        return phoneNumber
    }

    override fun onSuccess(message: String) {
        mView.onChangeSuccess(message)
    }
}