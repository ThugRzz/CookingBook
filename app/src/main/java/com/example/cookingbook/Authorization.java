package com.example.cookingbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authorization extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

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
        login = findViewById(R.id.mailText);
        password = findViewById(R.id.passText);

        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.signIn).setOnClickListener(this);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            gotoMain();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn:
                signIn(login.getText().toString(), password.getText().toString());
                break;
            case R.id.signUp:
                signUp(login.getText().toString(), password.getText().toString());
                break;
            default:
                break;
        }
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    gotoMain();
                    Toast.makeText(Authorization.this, "Sign In!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Authorization.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Authorization.this, "Sign up!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Authorization.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoMain() {
        Intent intent = new Intent(Authorization.this, MainActivity.class);
        startActivity(intent);
    }
}
