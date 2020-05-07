package com.example.cookingbook.repository

import com.example.cookingbook.model.Recipe
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.Query

object FirebaseRepository {
    fun performOptionsSettings(query: Query): FirebaseRecyclerOptions<Recipe> {
        return FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe::class.java)
                .build()
    }

}