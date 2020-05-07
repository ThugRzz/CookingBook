package com.example.cookingbook.ui.myrecipes

import com.example.cookingbook.model.Recipe
import com.example.cookingbook.repository.FirebaseRepository
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyRecipesInteractor(_mOnActionListener: MyRecipesContract.onActionListener) : MyRecipesContract.Interactor {
    private val mOnActionListener = _mOnActionListener
    private val userInfoQuery:Query = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("userInfo")
            .orderByChild("phone")
    private val mRef: DatabaseReference = FirebaseDatabase.getInstance()
            .reference
            .child("recipes")
    private val mQuery: Query = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("recipes")
            .orderByChild("title")
    private val infoQuery: Query = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("userInfo")
            .orderByChild("phone")

    override fun performDeleteItem(currentTitle: String) {
        val deleteQuery: Query = mQuery.equalTo(currentTitle)
        deleteQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dss in dataSnapshot.children) {
                    dss.ref.removeValue()
                }
                mOnActionListener.onSuccess("Рецепт удален")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun performShareItem(currentTitle: String) {
        val shareQuery: Query = mQuery.equalTo(currentTitle)
        shareQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dss in dataSnapshot.children) {
                    val title = dss.child("title").value.toString()
                    val composition = dss.child("composition").value.toString()
                    val description = dss.child("description").value.toString()
                    val image = dss.child("image").value.toString()
                    infoQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val uid = dataSnapshot.child("uid").value.toString()
                            val recipe = Recipe(title, composition, description, image, uid)
                            mRef.push().setValue(recipe)
                            mOnActionListener.onSuccess("Вы поделились рецептом")
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun setFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return FirebaseRepository.performOptionsSettings(mQuery)
    }

    override fun setSearchFirebaseRecyclerOptionsSettings(searchString:String): FirebaseRecyclerOptions<Recipe> {
        return FirebaseRepository.performOptionsSettings(mQuery.startAt(searchString).endAt(searchString+"\uf0ff"))
    }

    override fun getRecipesCount() {
        mQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = java.lang.Long.toString(dataSnapshot.childrenCount)
                userInfoQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.child("count").setValue(count)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}