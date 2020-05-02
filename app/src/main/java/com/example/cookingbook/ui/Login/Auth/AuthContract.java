package com.example.cookingbook.ui.Login.Auth;

import android.app.Activity;

public class AuthContract {
    interface View{
        void onAuthSuccess(String message);
        void onAuthFailure(String message);
    }
    interface Presenter{
        void authorization(Activity activity, String email, String password);
    }
    interface Interactor{
        void performFirebaseAuth(Activity activity, String email, String password);
    }

    interface onAuthListener{
        void onSuccess(String message);
        void onFailure(String message);
    }
}
