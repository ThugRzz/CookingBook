package com.example.cookingbook.ui.globalrecipes

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.model.Recipe
import com.example.cookingbook.repository.FirebaseRepository
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class RecipesInteractor : RecipesContract.Interactor {
    private val mQuery: Query = FirebaseDatabase.getInstance()
            .reference
            .child("recipes")
            .orderByChild("title")

    override fun getRecipeListFound(searchString: String, recyclerView: RecyclerView, context: Context) {
        val searchQuery: Query = mQuery
                .startAt(searchString)
                .endAt(searchString + "\uf0ff")
        val adapter: FirebaseRecyclerAdapter<Recipe, RecipesViewHolder> = RecipesFragmentAdapter(FirebaseRepository.performOptionsSettings(searchQuery), context)
        recyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun getRecipeList(context: Context, recyclerView: RecyclerView) {
        val adapter = RecipesFragmentAdapter(FirebaseRepository.performOptionsSettings(mQuery), context)
        recyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun setFirebaseRecyclerOptionsSettings(): FirebaseRecyclerOptions<Recipe> {
        return FirebaseRepository.performOptionsSettings(mQuery)
    }

    override fun setSearchFirebaseRecyclerOptionsSettings(searchString:String): FirebaseRecyclerOptions<Recipe> {
        return FirebaseRepository.performOptionsSettings(mQuery.startAt(searchString).endAt(searchString+"\uf0ff"))
    }
}