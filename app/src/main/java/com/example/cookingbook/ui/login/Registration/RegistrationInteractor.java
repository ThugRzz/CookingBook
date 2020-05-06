package com.example.cookingbook.ui.login.Registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.cookingbook.ui.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationInteractor implements RegistrationContract.Interactor {

    private RegistrationContract.onRegistrationListener onRegistrationListener;
    private FirebaseUser user;
    private UserProfileChangeRequest profileChangeRequest;

    public RegistrationInteractor(RegistrationContract.onRegistrationListener onRegistrationListener) {
        this.onRegistrationListener = onRegistrationListener;
    }

    @Override
    public void performFirebaseRegistration(Activity activity, String email, String password,String nickname) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    profileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(nickname).build();
                    user.updateProfile(profileChangeRequest);
                    onRegistrationListener.onSuccess(task.getResult().getUser());
                    gotoMain(activity);
                }else{
                    onRegistrationListener.onFailure("Не удалось зарегистрироваться!");
                }
            }
        });
    }

    public void onLoggedInstance(){

    }

    public void gotoMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
