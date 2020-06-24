package com.example.cookingbook.ui.newrecipe

import android.net.Uri
import android.util.Log
import com.example.cookingbook.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class NewRecipeInteractor(_mOnPushRecipeListener: NewRecipeContract.onPushRecipeListener) : NewRecipeContract.Interactor {

    private val mListener: NewRecipeContract.onPushRecipeListener = _mOnPushRecipeListener
    private val mStorageRef = FirebaseStorage.getInstance().reference
    lateinit var uploadTask: UploadTask
    private var mRef: DatabaseReference = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("recipes")

    private val mQuery: Query = mRef.orderByChild("title")

    private fun createNameForImage(): String {
        val symbols = "qwertyuiopasdfghjklzxcvbnm"
        val randString = StringBuilder()
        val count = 10 + (Math.random() * 30).toInt()
        for (i in 0 until count) {
            randString.append(symbols[(Math.random() * symbols.length).toInt()])
        }
        return randString.toString()
    }

    override fun performPushRecipe(localImageUri: Uri, title: String, composition: String, description: String) {
        val name: String = createNameForImage()
        val imageRef: StorageReference = mStorageRef.child("images/$name.jpg")
        uploadTask = imageRef.putFile(localImageUri)
        val uriTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                if (downloadUri != null) {
                    val photoStringLink = downloadUri.toString()
                    mQuery.equalTo(title)
                    mQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (dss in dataSnapshot.children) {
                                if (dss.child("title").value.toString() == title) {
                                    mListener.onFailure("Рецепт с таким названием уже существует")
                                    Log.d("INFO", "ALREADY ADDED")
                                    return
                                }
                            }
                            val pushRecipe = Recipe(title, composition, description, photoStringLink)
                            mRef.push().setValue(pushRecipe)
                            mListener.onSuccess("Рецепт добавлен")
                        }
                    })
                }
            }
        }
    }
}