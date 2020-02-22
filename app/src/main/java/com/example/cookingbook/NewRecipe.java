package com.example.cookingbook;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewRecipe extends AppCompatActivity implements View.OnClickListener {

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
    private Uri fullPhotoURI;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        createComposition = findViewById(R.id.Composition);
        createDescription = findViewById(R.id.Description);
        createTitle = findViewById(R.id.Title);
        createImage = findViewById(R.id.recipePic);
        createRecipeButton = findViewById(R.id.confirm);
        createRecipeButton.setOnClickListener(this);
        createImage.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            fullPhotoURI=data.getData();
            createImage.setImageURI(fullPhotoURI);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (createTitle.getText().toString().isEmpty() || createDescription.getText().toString().isEmpty() || createComposition.getText().toString().isEmpty()) {
                    Toast.makeText(NewRecipe.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                }
                title = createTitle.getText().toString();
                composition = createComposition.getText().toString();
                description = createDescription.getText().toString();
                Recipe recipe = new Recipe(
                        title,
                        composition,
                        description,
                        fullPhotoURI.toString());
                mRef = mDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");
                mRef.push().setValue(recipe);
                clearData();

                Toast.makeText(NewRecipe.this, "Рецепт добавлен", Toast.LENGTH_SHORT).show();
                break;
            case R.id.recipePic:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
                break;
        }
    }

    private void clearData() {
        createTitle.setText("");
        createComposition.setText("");
        createDescription.setText("");
        createImage.setImageResource(0);
    }
}
