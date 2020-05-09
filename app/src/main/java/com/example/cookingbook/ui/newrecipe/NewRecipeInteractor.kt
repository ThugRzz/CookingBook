package com.example.cookingbook.ui.newrecipe

import android.net.Uri
import com.example.cookingbook.model.Recipe
import com.example.cookingbook.util.ViewUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class NewRecipeInteractor : NewRecipeContract.Interactor {

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private val viewUtil: ViewUtil = ViewUtil()
    lateinit var uploadTask: UploadTask
    private val mRef:DatabaseReference = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("recipes")

    private fun createNameForImage(): String {
        val symbols = "qwertyuiopasdfghjklzxcvbnm"
        val randString = StringBuilder()
        val count = 10 + (Math.random() * 30).toInt()
        for (i in 0 until count) {
            randString.append(symbols[(Math.random() * symbols.length).toInt()])
        }
        return randString.toString()
    }

    private fun setImageUrl(localImageUri: Uri){
        val name:String = createNameForImage()
        val imageRef:StorageReference = mStorageRef.child("images/$name.jpg")
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
                }
            }
        }

    }

    override fun pushRecipe(title: String, composition: String, description: String, image: String) {
        val pushRecipe = Recipe(title, composition, description, image)
        mRef.push().setValue(pushRecipe)
    }
}