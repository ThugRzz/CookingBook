package com.example.cookingbook.ui.PersonalCabinet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookingbook.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ChangeProfileActivity extends Fragment implements View.OnClickListener {

    public static final int REQUEST_IMAGE_GET = 1;
    private Button changeAvatarButton, confirmButton;
    private EditText nameET, phoneET;
    private String name;
    private Uri avatar;
    private FirebaseUser user;
    private ImageView avatarImage;
    private StorageReference mStorageRef;
    private DatabaseReference mRef;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_change_profile,container,false);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbarChangeProfile);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
//        name = getActivity().getIntent().getExtras().getString("NAME");
        phoneET = root.findViewById(R.id.changePhoneNumber);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("userInfo");
        nameET = root.findViewById(R.id.changeNickname);
        nameET.setText(name);
        avatarImage = root.findViewById(R.id.changeAvatar);
        Picasso.get()
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerCrop()
                .into(avatarImage);
        avatar = user.getPhotoUrl();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("number").getValue() == null) {
                } else {
                    phoneET.setText(dataSnapshot.child("number").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        changeAvatarButton = root.findViewById(R.id.changeAvatarButton);
        changeAvatarButton.setOnClickListener(this);
        confirmButton = root.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            final Uri selectedImage = data.getData();
            String name = generateRandomNameForImage();
            final StorageReference imageRef = mStorageRef.child("avatars/" + name + ".jpg");
            UploadTask uploadTask = imageRef.putFile(selectedImage);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            Uri photoLink = downloadUri;
                            setImageUrl(photoLink);
                        }
                    }
                }
            });
        }
    }

    private void setImageUrl(Uri url) {
        avatar = url;
        Picasso.get()
                .load(url.toString())
                .placeholder(R.drawable.defaultimage)
                .fit()
                .centerCrop()
                .into(avatarImage);
    }

    private String generateRandomNameForImage() {
        String symbols = "qwertyuiopasdfghjklzxcvbnm";
        StringBuilder randString = new StringBuilder();

        int count = 10 + (int) (Math.random() * 30);

        for (int i = 0; i < count; i++) {
            randString.append(symbols.charAt((int) (Math.random() * symbols.length())));
        }
        return randString.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeAvatarButton:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(
                        getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
                break;
            case R.id.confirmButton:
                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nameET.getText().toString())
                        .setPhotoUri(avatar).build();
                user.updateProfile(changeRequest);
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("number").setValue(phoneET.getText().toString());
                        dataSnapshot.getRef().child("displayName").setValue(nameET.getText().toString());
                        dataSnapshot.getRef().child("avatar").setValue(avatar.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getActivity(), "Данные успешно изменены!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
