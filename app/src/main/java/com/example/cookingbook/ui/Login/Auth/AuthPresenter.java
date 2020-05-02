package com.example.cookingbook.ui.Login.Auth;

import android.app.Activity;

public class AuthPresenter implements AuthContract.Presenter, AuthContract.onAuthListener {

    private AuthContract.View mAuthView;
    private AuthContract.Interactor mAuthInteractor;

    public AuthPresenter(AuthContract.View mAuthView) {
        this.mAuthView = mAuthView;
        this.mAuthInteractor = new AuthInteractor(this);
    }

    @Override
    public void authorization(Activity activity, String email, String password) {
        mAuthInteractor.performFirebaseAuth(activity,email,password);
    }

    @Override
    public void onSuccess(String message) {
        mAuthView.onAuthSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mAuthView.onAuthFailure(message);
    }
}
