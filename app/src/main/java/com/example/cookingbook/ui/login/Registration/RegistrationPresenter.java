package com.example.cookingbook.ui.login.Registration;

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
import com.example.cookingbook.ui.login.Auth.AuthorizationFragment;
import com.google.android.material.textfield.TextInputLayout;

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
    public void onBackViewWasClicked(AppCompatActivity activity) {
        Fragment authorizationFragment = new AuthorizationFragment();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, authorizationFragment);
        ft.commit();
    }

    @Override
    public boolean validateEmail(TextInputLayout mailInputLayout, EditText emailEditText, Context context) {
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
    public boolean validatePassword(TextInputLayout passInputLayout, EditText passwordEditText, Context context) {
        if (passwordEditText.getText().toString().length() < 6) {
            passInputLayout.setError(context.getString(R.string.err_msg_password));
            requestFocus(passwordEditText,(Activity)context);
            return false;
        } else {
            passInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public boolean validateNickname(TextInputLayout nickInputLayout, EditText nickEditText, Context context) {
        if (nickEditText.getText().toString().trim().isEmpty()) {
            nickInputLayout.setError(context.getString(R.string.err_msg_name));
            requestFocus(nickEditText,(Activity)context);
            return false;
        } else {
            nickInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
