package com.example.cookingbook.ui.Login.Auth;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookingbook.R;
import com.example.cookingbook.ui.Login.Registration.RegistrationFragment;
import com.google.android.material.textfield.TextInputLayout;

public class AuthPresenter implements AuthContract.Presenter, AuthContract.onAuthListener{

    private AuthContract.View mAuthView;
    private AuthContract.Interactor mAuthInteractor;

    public AuthPresenter(AuthContract.View mAuthView) {
        this.mAuthView = mAuthView;
        this.mAuthInteractor = new AuthInteractor(this);
    }

    @Override
    public void authorization(Activity activity, String email, String password) {
        mAuthInteractor.performFirebaseAuth(activity, email, password);
    }

    @Override
    public void showLoggedInstance(Activity activity) {
        mAuthInteractor.onLoggedInstance(activity);
    }

    @Override
    public void onStart() {
        mAuthInteractor.addAuthStateListener();
    }

    @Override
    public void onDestroy() {
        mAuthInteractor.removeAuthStateListener();
    }


    @Override
    public void onSuccess(String message) {
        mAuthView.onAuthSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mAuthView.onAuthFailure(message);
    }


    @Override
    public boolean validateEmail(TextInputLayout mailInputLayout, EditText emailEditText,Context context) {
        if (emailEditText.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            mailInputLayout.setError(context.getString(R.string.err_msg_email));
            requestFocus(emailEditText,(Activity)context);
            return false;
        } else {
            mailInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    @Override
    public boolean validatePassword(TextInputLayout passInputLayout, EditText passwordEditText, Context context){
        if (passwordEditText.getText().toString().length() < 6) {
            passInputLayout.setError(context.getString(R.string.err_msg_password));
            requestFocus(passwordEditText, (Activity) context);
            return false;
        } else {
            passInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public void onSignUpViewWasClicked(AppCompatActivity activity) {
        Fragment registrationFragment = new RegistrationFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, registrationFragment);
        ft.commit();
    }

    private void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
