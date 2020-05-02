package com.example.cookingbook.ui.Login.Registration;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

public interface RegistrationContract {
    interface View{
        void onRegistrationSuccess(FirebaseUser user);
        void onRegistrationFailure(String message);
    }

    interface Presenter{
        void registration(Activity activity, String email, String password,String nickname);
    }

    interface Interactor{
        void performFirebaseRegistration(Activity activity, String email, String password,String nickname);
    }

    interface onRegistrationListener{
        void onSuccess(FirebaseUser user);
        void onFailure(String message);
    }
}
