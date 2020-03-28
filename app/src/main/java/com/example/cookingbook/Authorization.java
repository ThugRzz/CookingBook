package com.example.cookingbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Authorization extends AppCompatActivity implements View.OnClickListener {

    private Registration registration = new Registration();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView bookIconImageView;
    private TextView bookITextView, signUpTextView, skipTextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;
    private TextInputLayout mailInputLayout, passInputLayout;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authorization);
        initViews();
        new CountDownTimer(5000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                bookITextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(Authorization.this, R.color.colorSplashText));
                bookIconImageView.setImageResource(R.drawable.background_color_book);
                startAnimation();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    gotoMain();
                } else {

                }
            }
        };

        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));

        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.signUpTextView).setOnClickListener(this);
        findViewById(R.id.skipTextView).setOnClickListener(this);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
        //    user.updateProfile(registration.getNickName());
            gotoMain();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:submitForm(); break;
            case R.id.signUpTextView:gotoSignUp();break;
            case R.id.skipTextView:gotoMain();
        }

    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
/*                    FirebaseUser user = mAuth.getCurrentUser();
                    user.updateProfile(registration.getNickName());*/
                    gotoMain();
                    Toast.makeText(Authorization.this, "Sign In!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Authorization.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.rootView);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpTextView = findViewById(R.id.signUpTextView);
        mailInputLayout=findViewById(R.id.inputLayoutMail);
        passInputLayout=findViewById(R.id.inputLayoutPass);
    }

    private void submitForm(){
        if(!validateEmail()){
            return;
        }
        if(!validatePassword()){
            return;
        }
        signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    private boolean validateEmail(){
        String email = emailEditText.getText().toString().trim();
        if(email.isEmpty()||!isValidateEmail(email)){
            mailInputLayout.setError(getString(R.string.err_msg_email));
            requestFocus(emailEditText);
            return false;
        }else{
            mailInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword(){
        if(passwordEditText.getText().toString().length()<6){
            passInputLayout.setError(getString(R.string.err_msg_password));
            requestFocus(passwordEditText);
            return false;
        }else{
            passInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static boolean isValidateEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
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


    public void gotoMain() {
        Intent intent = new Intent(Authorization.this, MainActivity.class);
        startActivity(intent);
    }

    public void gotoSignUp(){
        Intent intent = new Intent(Authorization.this,Registration.class);
        startActivity(intent);
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
                    validateEmail();
                    break;
                case R.id.passwordEditText:
                    validatePassword();
                    break;
            }
        }
    }
}
