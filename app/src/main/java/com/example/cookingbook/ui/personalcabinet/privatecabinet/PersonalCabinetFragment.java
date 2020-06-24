package com.example.cookingbook.ui.personalcabinet.privatecabinet;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cookingbook.R;
import com.example.cookingbook.ui.activities.LoginActivity;
import com.example.cookingbook.ui.activities.RecipeCreatorInfoActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PersonalCabinetFragment extends Fragment {
    private final static String NAME = "NAME";
    private String count;
    private FirebaseAuth mAuth;
    private TextView emailTextView, nicknameTextView, counterTextView, phoneTextView;
    private ImageView avatarImageView;
    private DatabaseReference mRef;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cabinet_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeProfile:
                navController.navigate(R.id.changeProfileActivity);
                break;
            case R.id.LogOut:
                final AlertDialog callDialog= new AlertDialog.Builder(
                        Objects.requireNonNull(PersonalCabinetFragment.this.getContext())).setMessage("Вы действительно хотите выйти?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

                callDialog.show();
                callDialog.getButton(callDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                callDialog.getButton(callDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        emailTextView.setText(mAuth.getCurrentUser().getEmail());
        nicknameTextView.setText(mAuth.getCurrentUser().getDisplayName());
        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(avatarImageView);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.textView);
        TextView textView1 = headerView.findViewById(R.id.login);
        ImageView avatarImage = headerView.findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        textView.setText(mAuth.getCurrentUser().getEmail());
        textView1.setText(mAuth.getCurrentUser().getDisplayName());
        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(avatarImage);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("number").getValue() == null) {
                } else {
                    phoneTextView.setText(dataSnapshot.child("number").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal_cabinet, container, false);
        mAuth = FirebaseAuth.getInstance();
        navController = NavHostFragment.findNavController(this);
        CollapsingToolbarLayout collapsingToolbarLayout = root.findViewById(R.id.collapsingToolbarCabinet);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        phoneTextView = root.findViewById(R.id.phTextView);
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("userInfo");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("number").getValue() == null) {
                } else {
                    phoneTextView.setText(dataSnapshot.child("number").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getRecipesCount();
        emailTextView = root.findViewById(R.id.emailTextView);
        avatarImageView = root.findViewById(R.id.avatar);
        counterTextView = root.findViewById(R.id.counterTextView);
        emailTextView.setText(mAuth.getCurrentUser().getEmail());
        nicknameTextView = root.findViewById(R.id.nicknameTextView);
        nicknameTextView.setText(mAuth.getCurrentUser().getDisplayName());
        Picasso.get()
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerInside()
                .into(avatarImageView);
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
                        if (mAuth.getCurrentUser().getPhotoUrl() == null) {
                            dataSnapshot.getRef().child("avatar").setValue(R.drawable.nophoto);
                        } else {
                            dataSnapshot.getRef().child("avatar").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                        }
                        dataSnapshot.getRef().child("uid").setValue(mAuth.getCurrentUser().getUid());
                        dataSnapshot.getRef().child("email").setValue(mAuth.getCurrentUser().getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}