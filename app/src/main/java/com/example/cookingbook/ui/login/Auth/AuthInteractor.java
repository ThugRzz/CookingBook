package com.example.cookingbook.ui.login.Auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import androidx.annotation.NonNull;

import com.example.cookingbook.ui.activities.MainActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthInteractor implements AuthContract.Interactor {

    private AuthContract.onAuthListener mOnAuthListener;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public AuthInteractor(AuthContract.onAuthListener mOnAuthListener) {
        this.mOnAuthListener = mOnAuthListener;
    }

    @Override
    public void performFirebaseAuth(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mOnAuthListener.onSuccess("Авторизация прошла успешно!");
                            gotoMain(activity);
                        } else {
                            mOnAuthListener.onFailure("Вы ввели неверный логин или пароль!");
                        }
                    }
                });
    }

    @Override
    public void onLoggedInstance(Activity activity) {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    gotoMain(activity);
                } else {

                }
            }
        };
    }

    @Override
    public void addAuthStateListener() {
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void removeAuthStateListener() {
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
    }


    public void gotoMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
