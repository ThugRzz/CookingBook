package com.example.cookingbook.ui.Login.Registration;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

public class RegistrationPresenter implements RegistrationContract.Presenter, RegistrationContract.onRegistrationListener{

    private RegistrationContract.View mRegistrationView;
    private RegistrationContract.Interactor mRegistrationInteractor;

    public RegistrationPresenter(RegistrationContract.View mRegistrationView) {
        this.mRegistrationView = mRegistrationView;
        mRegistrationInteractor=new RegistrationInteractor(this);
    }

    @Override
    public void registration(Activity activity, String email, String password,String nickname) {
        mRegistrationInteractor.performFirebaseRegistration(activity,email,password,nickname);
    }

    @Override
    public void onSuccess(FirebaseUser user) {
        mRegistrationView.onRegistrationSuccess(user);
    }

    @Override
    public void onFailure(String message) {
        mRegistrationView.onRegistrationFailure(message);
    }
}
