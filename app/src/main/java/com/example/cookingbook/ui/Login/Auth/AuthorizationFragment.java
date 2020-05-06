package com.example.cookingbook.ui.Login.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.animation.Animator;

import android.content.Context;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;

import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookingbook.R;

import com.google.android.material.textfield.TextInputLayout;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AuthorizationFragment extends Fragment implements View.OnClickListener, AuthContract.View {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private AuthPresenter mAuthPresenter;
    private Context context;
    private ImageView bookIconImageView;
    private TextView bookITextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;
    private TextInputLayout mailInputLayout, passInputLayout;
    private EditText emailEditText, passwordEditText;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAuthPresenter.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuthPresenter.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_authorization, container, false);
        init(root);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                bookITextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSplashText));
                bookIconImageView.setImageResource(R.drawable.background_color_book);
                startAnimation();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        mAuthPresenter.showLoggedInstance(this.getActivity());
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        CheckBox eyeCheckbox = root.findViewById(R.id.authCheckbox);
        eyeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {

                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        root.findViewById(R.id.loginButton).setOnClickListener(this);
        root.findViewById(R.id.signUpTextView).setOnClickListener(this);
        root.findViewById(R.id.skipTextView).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                submitForm();
                break;
            case R.id.signUpTextView:
                mAuthPresenter.onSignUpViewWasClicked((AppCompatActivity) this.getActivity());
                break;
        }
    }

    private void init(View root) {
        bookIconImageView = root.findViewById(R.id.bookIconImageView);
        bookITextView = root.findViewById(R.id.bookITextView);
        loadingProgressBar = root.findViewById(R.id.loadingProgressBar);
        rootView = root.findViewById(R.id.rootView);
        afterAnimationView = root.findViewById(R.id.afterAnimationView);
        emailEditText = (EditText) root.findViewById(R.id.emailEditText);
        passwordEditText = root.findViewById(R.id.passwordEditText);
        mailInputLayout = root.findViewById(R.id.inputLayoutMail);
        passInputLayout = root.findViewById(R.id.inputLayoutPass);
        mAuthPresenter = new AuthPresenter(this);
    }


    private void submitForm() {
        if (!mAuthPresenter.validateEmail(mailInputLayout, emailEditText, getContext())) {
            return;
        }
        if (!mAuthPresenter.validatePassword(passInputLayout, passwordEditText, getContext())) {
            return;
        }
        mAuthPresenter.authorization(this.getActivity(), emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();
        viewPropertyAnimator.x(50f);
        viewPropertyAnimator.y(100f);
        viewPropertyAnimator.setDuration(1000);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterAnimationView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void onAuthSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.emailEditText:
                    mAuthPresenter.validateEmail(mailInputLayout, emailEditText, getContext());
                    break;
                case R.id.passwordEditText:
                    mAuthPresenter.validatePassword(passInputLayout, passwordEditText, getContext());
                    break;
            }
        }
    }
}
