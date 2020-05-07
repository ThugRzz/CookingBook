package com.example.cookingbook.util

import android.text.Editable
import android.text.TextWatcher

abstract class SearchTextWatcher :TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        if(s.toString().isNotEmpty()){
            search(s.toString())
        }else{
            search("")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
    abstract fun search(searchString: String)
}