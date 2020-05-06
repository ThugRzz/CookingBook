package com.example.cookingbook.ui.login.Registration;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cookingbook.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.VISIBLE;

public class RegistrationFragment extends Fragment implements View.OnClickListener, RegistrationContract.View {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private Context context;
    private ImageView bookIconImageView;
    private RelativeLayout rootView, afterAnimationView;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private EditText emailEditText, passwordEditText, nickEditText;
    private RegistrationPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        init(root);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSplashText));
                bookIconImageView.setImageResource(R.drawable.background_color_book);
                startAnimation();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        emailEditText.addTextChangedListener(new RegistrationTextWatcher(emailEditText));
        nickEditText.addTextChangedListener(new RegistrationTextWatcher(nickEditText));
        passwordEditText.addTextChangedListener(new RegistrationTextWatcher(passwordEditText));
        root.findViewById(R.id.registrationButton).setOnClickListener(this);
        root.findViewById(R.id.backTextView).setOnClickListener(this);
        CheckBox eyeCheckbox = root.findViewById(R.id.registrationCheckbox);
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
        return root;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registrationButton:
                submitForm();
                break;
            case R.id.backTextView:
                presenter.onBackViewWasClicked((AppCompatActivity) this.getActivity());
        }
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

    private void init(View root) {
        bookIconImageView = root.findViewById(R.id.bookIconImageView1);
        rootView = root.findViewById(R.id.rootView1);
        afterAnimationView = root.findViewById(R.id.afterAnimationView1);
        emailEditText = root.findViewById(R.id.emailEditText1);
        passwordEditText = root.findViewById(R.id.passwordEditText1);
        nickEditText = root.findViewById(R.id.nickNameEditText);
        inputLayoutEmail = root.findViewById(R.id.inputLayoutEmail);
        inputLayoutName = root.findViewById(R.id.inputLayoutName);
        inputLayoutPassword = root.findViewById(R.id.inputLayoutPassword);
        presenter = new RegistrationPresenter(this);
    }

    private void submitForm() {
        if (!presenter.validateNickname(inputLayoutName, nickEditText, getContext())) {
            return;
        }
        if (!presenter.validateEmail(inputLayoutEmail, emailEditText, getContext())) {
            return;
        }
        if (!presenter.validatePassword(inputLayoutPassword, passwordEditText, getContext())) {
            return;
        }
        presenter.registration(this.getActivity(), emailEditText.getText().toString(), passwordEditText.getText().toString(), nickEditText.getText().toString());
    }

    @Override
    public void onRegistrationSuccess(FirebaseUser user) {
        Toast.makeText(getContext(), "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private class RegistrationTextWatcher implements TextWatcher {

        private View view;

        private RegistrationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.nickNameEditText:
                    presenter.validateNickname(inputLayoutName, nickEditText, getContext());
                    break;
                case R.id.emailEditText1:
                    presenter.validateEmail(inputLayoutEmail, emailEditText, getContext());
                    break;
                case R.id.passwordEditText1:
                    presenter.validatePassword(inputLayoutPassword, passwordEditText, getContext());
                    break;
            }
        }
    }
}


