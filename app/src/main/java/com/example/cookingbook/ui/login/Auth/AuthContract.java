package com.example.cookingbook.ui.login.Auth;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class AuthContract {
    interface View {
        void onAuthSuccess(String message);

        void onAuthFailure(String message);
    }

    interface Presenter {
        void authorization(Activity activity, String email, String password);

        void showLoggedInstance(Activity activity);

        void onStart();

        void onDestroy();

        boolean validateEmail(TextInputLayout mailInputLayout, EditText emailEditText, Context context);

        boolean validatePassword(TextInputLayout passInputLayout, EditText passwordEditText, Context context);

        void onSignUpViewWasClicked(AppCompatActivity activity);


    }

    interface Interactor {
        void performFirebaseAuth(Activity activity, String email, String password);

        void onLoggedInstance(Activity activity);

        void addAuthStateListener();

        void removeAuthStateListener();
    }

    interface onAuthListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
