package com.example.cookingbook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.cookingbook.R
import com.example.cookingbook.ui.login.Auth.AuthorizationFragment



class LoginActivity : AppCompatActivity() {

    private val authFragment: AuthorizationFragment = AuthorizationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val fTrans:FragmentTransaction = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fragmentContainer, authFragment)
        fTrans.commit()
    }
}
