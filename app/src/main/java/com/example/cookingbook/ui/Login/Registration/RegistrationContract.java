package com.example.cookingbook.ui.Login.Registration;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseUser;

public interface RegistrationContract {
    interface View {
        void onRegistrationSuccess(FirebaseUser user);

        void onRegistrationFailure(String message);
    }

    interface Presenter {
        void registration(Activity activity, String email, String password, String nickname);

        void onBackViewWasClicked(AppCompatActivity activity);

        boolean validateEmail(TextInputLayout mailInputLayout, EditText emailEditText, Context context);

        boolean validatePassword(TextInputLayout passInputLayout, EditText passwordEditText, Context context);

        boolean validateNickname(TextInputLayout nickInputLayout, EditText nickEditText, Context context);
    }

    interface Interactor {
        void performFirebaseRegistration(Activity activity, String email, String password, String nickname);
    }

    interface onRegistrationListener {
        void onSuccess(FirebaseUser user);

        void onFailure(String message);
    }
}
