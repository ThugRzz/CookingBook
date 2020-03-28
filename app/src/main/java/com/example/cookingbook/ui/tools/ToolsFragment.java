package com.example.cookingbook.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cookingbook.Authorization;
import com.example.cookingbook.ChangeProfileActivity;
import com.example.cookingbook.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class ToolsFragment extends Fragment implements View.OnClickListener {

    final static String NAME = "NAME";
    private FirebaseAuth mAuth;
    private TextView emailTextView, nicknameTextView;
    private TextView nickName;
    private ToolsViewModel toolsViewModel;
    private Button signOutButton, changeProfileButton;
    private FirebaseUser user;
    private ImageView avatarImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        CollapsingToolbarLayout collapsingToolbarLayout = root.findViewById(R.id.collapsingToolbarCabinet);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName("ThugRzz").build();
//        user.updateProfile(changeRequest);
        emailTextView = root.findViewById(R.id.emailTextView);
        avatarImageView=root.findViewById(R.id.avatar);
        emailTextView.setText(mAuth.getCurrentUser().getEmail());
        nicknameTextView = root.findViewById(R.id.nicknameTextView);
        nicknameTextView.setText(mAuth.getCurrentUser().getDisplayName());
        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(avatarImageView);
        signOutButton = root.findViewById(R.id.signOutButton);
        changeProfileButton = root.findViewById(R.id.changeProfileButton);
        signOutButton.setOnClickListener(this);
        changeProfileButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signOutButton:
                mAuth.signOut();
                Intent intent = new Intent(getContext(), Authorization.class);
                startActivity(intent);
                break;
            case R.id.changeProfileButton:
                Intent changeProfileIntent = new Intent(getContext(), ChangeProfileActivity.class);
                changeProfileIntent.putExtra(NAME, mAuth.getCurrentUser().getDisplayName());
                startActivity(changeProfileIntent);
                break;
        }

    }
}