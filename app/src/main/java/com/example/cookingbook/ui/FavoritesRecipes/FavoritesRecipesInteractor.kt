package com.example.cookingbook.ui.FavoritesRecipes

import com.example.cookingbook.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritesRecipesInteractor(_mOnDeleteListener: FavoritesRecipesContract.onDeleteListener) : FavoritesRecipesContract.Interactor {

    val mOnDeleteListener: FavoritesRecipesContract.onDeleteListener = _mOnDeleteListener
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mRef: DatabaseReference = mDatabase.getReference().child("users").child(mAuth.currentUser!!.uid).child("favorites")
    private val optionsQuery:Query = mDatabase.getReference().child("users").child(mAuth.currentUser!!.uid).child("favorites").orderByChild("title")

    override fun performDeleteItem(currentTitle: String) {
        val mQuery: Query = mRef.orderByChild("title").equalTo(currentTitle)
        mQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dss in dataSnapshot.children) {
                    dss.ref.removeValue()
                }
                mOnDeleteListener.onSuccess("Рецепт удален")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun performFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(optionsQuery,Recipe::class.java)
                .build()
    }
}