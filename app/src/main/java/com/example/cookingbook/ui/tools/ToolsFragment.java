package com.example.cookingbook.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ToolsFragment extends Fragment implements View.OnClickListener {

    final static String NAME = "NAME";
    private String count="adwa";
    private FirebaseAuth mAuth;
    private TextView emailTextView, nicknameTextView, counterTextView,phoneTextView;
    private TextView nickName;
    private ToolsViewModel toolsViewModel;
    private Button signOutButton, changeProfileButton;
    private FirebaseUser user;
    private ImageView avatarImageView;
    private DatabaseReference mRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        CollapsingToolbarLayout collapsingToolbarLayout = root.findViewById(R.id.collapsingToolbarCabinet);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        phoneTextView=root.findViewById(R.id.phTextView);
//        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName("ThugRzz").build();
//        user.updateProfile(changeRequest);

        mRef=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("userInfo");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               phoneTextView.setText(dataSnapshot.child("number").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getRecipesCount();
        emailTextView = root.findViewById(R.id.emailTextView);
        avatarImageView = root.findViewById(R.id.avatar);
        counterTextView = root.findViewById(R.id.counterTextView);
     //   counterTextView.setText(count);
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

    private void getRecipesCount() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");
        Query myQuery = reference.orderByChild("title");
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = Long.toString(dataSnapshot.getChildrenCount());
                counterTextView.setText(count);
                Query infoQuery = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("userInfo");
                infoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("count").setValue(count);
                        dataSnapshot.getRef().child("displayName").setValue(mAuth.getCurrentUser().getDisplayName());
                        if(mAuth.getCurrentUser().getPhotoUrl()==null){
                            dataSnapshot.getRef().child("avatar").setValue(R.drawable.nophoto);
                        }else {
                            dataSnapshot.getRef().child("avatar").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                        }
                        dataSnapshot.getRef().child("uid").setValue(mAuth.getCurrentUser().getUid());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
          //      Toast.makeText(getContext(),count,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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