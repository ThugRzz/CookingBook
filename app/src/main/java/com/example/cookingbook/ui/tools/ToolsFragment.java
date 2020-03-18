package com.example.cookingbook.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cookingbook.Authorization;
import com.example.cookingbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ToolsFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView emailTextView;
    private TextView nickName;
    private ToolsViewModel toolsViewModel;
    private Button signOutButton;
    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        emailTextView= root.findViewById(R.id.emailTextView);
        nickName=root.findViewById(R.id.nickname);
        mAuth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
//        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName("ThugRzz").build();
//        user.updateProfile(changeRequest);
        emailTextView.setText(mAuth.getCurrentUser().getEmail());
        nickName.setText(mAuth.getCurrentUser().getDisplayName());
        signOutButton=root.findViewById(R.id.signOut);
        signOutButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), Authorization.class);
        startActivity(intent);
    }
}