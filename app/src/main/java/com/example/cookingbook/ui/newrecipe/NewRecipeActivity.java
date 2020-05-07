package com.example.cookingbook.ui.newrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookingbook.R;
import com.example.cookingbook.model.Recipe;
import com.example.cookingbook.util.ViewUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_IMAGE_GET = 1;
    private String title;
    private String description;
    private String composition;
    private String image;
    private EditText createTitle;
    private EditText createDescription;
    private EditText createComposition;
    private ImageView createImage;
    private Button createRecipeButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private Button changeImageButton;
    private ViewUtil viewUtil;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNewRecipe);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        viewUtil = new ViewUtil();
        createComposition = findViewById(R.id.Composition);
        createDescription = findViewById(R.id.Description);
        createTitle = findViewById(R.id.Title);
        createImage = findViewById(R.id.recipePic);
        changeImageButton = findViewById(R.id.changeImageButton);
        createRecipeButton = findViewById(R.id.confirm);
        createRecipeButton.setOnClickListener(this);
        changeImageButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            final Uri selectedImage = data.getData();
            String name = createNameForImage();
            final StorageReference imageRef = mStorageRef.child("images/" + name + ".jpg");
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
                            String photoStringLink = downloadUri.toString();
                            setImageUrl(photoStringLink);
                        }
                    }
                }
            });
        }
    }

    private void setImageUrl(String url) {
        image = url;
        viewUtil.putPicture(url,createImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (image == null || image.isEmpty() || createTitle.getText().toString().isEmpty() || createDescription.getText().toString().isEmpty() || createComposition.getText().toString().isEmpty()) {
                    Toast.makeText(NewRecipeActivity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                    return;
                }
                title = createTitle.getText().toString();
                composition = createComposition.getText().toString();
                description = createDescription.getText().toString();

                isAdded(title, composition, description, image);

                break;
            case R.id.changeImageButton:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
                break;
        }
    }

    public void pushRecipe(String title, String composition, String description, String image) {
        Recipe pushRecipe = new Recipe(title, composition, description, image);
        mRef = mDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");
        mRef.push().setValue(pushRecipe);
        Toast.makeText(getApplicationContext(), "Рецепт добавлен!", Toast.LENGTH_SHORT).show();
        clearData();
    }

    private String createNameForImage() {
        String symbols = "qwertyuiopasdfghjklzxcvbnm";
        StringBuilder randString = new StringBuilder();
        int count = 10 + (int) (Math.random() * 30);
        for (int i = 0; i < count; i++) {
            randString.append(symbols.charAt((int) (Math.random() * symbols.length())));
        }
        return randString.toString();
    }

    private void clearData() {
        createTitle.setText("");
        createComposition.setText("");
        createDescription.setText("");
        createImage.setImageResource(R.drawable.nophoto);
        image = "";
    }

    private void isAdded(final String title, final String composition, final String description, final String image) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");
        Query myQuery = reference.orderByChild("title").equalTo(title);
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    if (dss.child("title").getValue().toString().equals(title)) {
                        Toast.makeText(NewRecipeActivity.this, "Рецепт с таким названием уже существует!", Toast.LENGTH_SHORT).show();
                        Log.d("INFO", "ALREADY ADDED");
                        return;
                    }
                }
                pushRecipe(title, composition, description, image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

