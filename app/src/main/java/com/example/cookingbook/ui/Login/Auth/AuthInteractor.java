package com.example.cookingbook.ui.Login.Auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.cookingbook.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthInteractor implements AuthContract.Interactor {

    private AuthContract.onAuthListener mOnAuthListener;

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
                        if(task.isSuccessful()){
                            mOnAuthListener.onSuccess("Авторизация прошла успешно!");
                            gotoMain(activity);
                        }else{
                            mOnAuthListener.onFailure("Вы ввели неверный логин или пароль!");
                        }
                    }
                });
    }

    public void gotoMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
