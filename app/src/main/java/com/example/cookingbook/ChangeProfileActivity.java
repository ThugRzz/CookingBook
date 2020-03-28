package com.example.cookingbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookingbook.ui.tools.ToolsFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ChangeProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_IMAGE_GET = 1;
    private Button changeAvatarButton,confirmButton;
    private EditText emailET,phoneET;
    private String email;
    private Uri avatar;
    private FirebaseUser user;
    private ImageView avatarImage;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        user= FirebaseAuth.getInstance().getCurrentUser();
        email=getIntent().getExtras().getString("NAME");
        mStorageRef=FirebaseStorage.getInstance().getReference();
        emailET=findViewById(R.id.changeNickname);
        emailET.setText(email);
        avatarImage=findViewById(R.id.changeAvatar);
        changeAvatarButton=findViewById(R.id.changeAvatarButton);
        changeAvatarButton.setOnClickListener(this);
        confirmButton=findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
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

        for (int i = 0; i < count; i++)
            randString.append(symbols.charAt((int) (Math.random() * symbols.length())));

        return randString.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changeAvatarButton:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(

                        getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
                break;
            case R.id.confirmButton:
                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(emailET.getText().toString())
                        .setPhotoUri(avatar).build();
                user.updateProfile(changeRequest);
                Toast.makeText(this,"Данные успешно изменены!",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
