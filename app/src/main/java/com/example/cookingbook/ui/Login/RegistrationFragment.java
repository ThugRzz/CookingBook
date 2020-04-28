package com.example.cookingbook.ui.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cookingbook.R;
import com.example.cookingbook.ui.Login.AuthorizationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

import static android.view.View.VISIBLE;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    private Context context;
    private FirebaseAuth mAuth;
    private ImageView bookIconImageView;
    private RelativeLayout rootView, afterAnimationView;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private EditText emailEditText, passwordEditText, nickEditText;
    private UserProfileChangeRequest changeRequest;
    private FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration,container,false);
        initViews(root);
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
        mAuth = FirebaseAuth.getInstance();
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        nickEditText.addTextChangedListener(new MyTextWatcher(nickEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        root.findViewById(R.id.registrationButton).setOnClickListener(this);
        root.findViewById(R.id.backTextView).setOnClickListener(this);
        CheckBox eyeCheckbox = root.findViewById(R.id.registrationCheckbox);
        eyeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{

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
                gotoAuth();
        }
    }

    public void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nickEditText.getText().toString()).build();
                    user.updateProfile(changeRequest);
                    Toast.makeText(getContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                    gotoAuth();
                } else {
                    Toast.makeText(getContext(), "Не удалось зарегистрироваться:(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoAuth() {
        Fragment authorizationFragment = new AuthorizationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, authorizationFragment);
        ft.commit();
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

    public UserProfileChangeRequest getNickName() {
        return changeRequest;
    }

    private void initViews(View root) {
        bookIconImageView = root.findViewById(R.id.bookIconImageView1);
        rootView = root.findViewById(R.id.rootView1);
        afterAnimationView = root.findViewById(R.id.afterAnimationView1);
        emailEditText = root.findViewById(R.id.emailEditText1);
        passwordEditText = root.findViewById(R.id.passwordEditText1);
        nickEditText = root.findViewById(R.id.nickNameEditText);
        inputLayoutEmail = root.findViewById(R.id.inputLayoutEmail);
        inputLayoutName = root.findViewById(R.id.inputLayoutName);
        inputLayoutPassword = root.findViewById(R.id.inputLayoutPassword);
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        signUp(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    private boolean validateName() {
        if (nickEditText.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(nickEditText);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty() || !isValidateEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(emailEditText);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (passwordEditText.getText().toString().length() < 6) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(passwordEditText);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidateEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity)getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.nickNameEditText:
                    validateName();
                    break;
                case R.id.emailEditText1:
                    validateEmail();
                    break;
                case R.id.passwordEditText1:
                    validatePassword();
                    break;
            }
        }
    }
}


