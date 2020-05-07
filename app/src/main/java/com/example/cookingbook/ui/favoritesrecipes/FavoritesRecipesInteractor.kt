package com.example.cookingbook.ui.favoritesrecipes

import com.example.cookingbook.model.Recipe
import com.example.cookingbook.repository.FirebaseRepository
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritesRecipesInteractor(_mOnDeleteListener: FavoritesRecipesContract.OnDeleteListener) : FavoritesRecipesContract.Interactor {
    val mOnDeleteListener: FavoritesRecipesContract.OnDeleteListener = _mOnDeleteListener
    private val mQuery: Query = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("favorites")
            .orderByChild("title")

    override fun performDeleteItem(currentTitle: String) {
        val deleteQuery: Query = mQuery.equalTo(currentTitle)
        deleteQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dss in dataSnapshot.children) {
                    dss.ref.removeValue()
                }
                mOnDeleteListener.onSuccess("Рецепт удален")
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun setFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return FirebaseRepository.performOptionsSettings(mQuery)
    }
}