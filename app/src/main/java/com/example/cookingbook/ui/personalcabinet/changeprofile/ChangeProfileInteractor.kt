package com.example.cookingbook.ui.personalcabinet.changeprofile

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ChangeProfileInteractor(_mOnChangeSuccessListener: ChangeProfileContract.onChangeSuccessListener) : ChangeProfileContract.Interactor {

    private val mListener: ChangeProfileContract.onChangeSuccessListener = _mOnChangeSuccessListener
    private val mStorageRef:StorageReference = FirebaseStorage.getInstance().reference
    private val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val mRef = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(mUser!!.uid)
            .child("userInfo")

    override fun getImage(): Uri? {
        return mUser?.photoUrl
    }

    override fun getNickname(): String {
        return mUser!!.displayName!!
    }

    override fun getPhone(callback: ChangeProfileContract.FirebaseCallback) {
        mRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("number").value != null) {
                    callback.onCallBack(dataSnapshot.child("number").toString())
                }
            }

        })
    }

    override fun changeUserInfo(photo: Uri,displayName:String,phoneNumber:String ) {
        val name:String = generateRandomNameForImage()
        val imageRef:StorageReference = mStorageRef.child("avatars/$name.jpg")
        val uploadTask = imageRef.putFile(photo)
        val uriTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            imageRef.downloadUrl
        }.addOnCompleteListener{task ->
            if(task.isSuccessful){
                val downloadUri = task.result
                if(downloadUri!=null){
                    val changeRequest:UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .setPhotoUri(downloadUri)
                            .build()
                    mUser!!.updateProfile(changeRequest)
                    mRef.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {

                        }
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.ref.child("number").setValue(phoneNumber)
                            dataSnapshot.ref.child("displayName").setValue(displayName)
                            dataSnapshot.ref.child("avatar").setValue(downloadUri.toString())
                            mListener.onSuccess("Данные успешно изменены")
                        }
                    })
                }
            }
        }
    }

    private fun generateRandomNameForImage(): String {
        val symbols = "qwertyuiopasdfghjklzxcvbnm"
        val randString = StringBuilder()
        val count = 10 + (Math.random() * 30).toInt()
        for (i in 0 until count) {
            randString.append(symbols[(Math.random() * symbols.length).toInt()])
        }
        return randString.toString()
    }
}