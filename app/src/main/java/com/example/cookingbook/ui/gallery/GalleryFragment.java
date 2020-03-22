package com.example.cookingbook.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.DataAdapter;
import com.example.cookingbook.NewRecipe;
import com.example.cookingbook.R;
import com.example.cookingbook.Recipe;
import com.example.cookingbook.RecipeCard;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addNewRecipe;
    private RecyclerView recipeList;
    private DatabaseReference mRef;
    private Query query;
    private FirebaseAuth mAuth;
    private ArrayList<Recipe> list;
    private ArrayList<Recipe> filtered;
    private EditText editText;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        FirebaseRecyclerAdapter<Recipe, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Recipe, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Recipe model) {
                String recipeID = getRef(position).getKey();
                holder.myTitle.setText(model.getTitle());
                holder.myComposition.setText(model.getComposition());
                holder.myDescription.setText(model.getDescription());
                Picasso.get()
                        .load(Uri.parse(model.getImage()))
                        .placeholder(R.drawable.defaultimage)
                        .fit()
                        .centerInside()
                        .into(holder.myImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent recipeIntent = new Intent(getContext(), RecipeCard.class);
                        recipeIntent.putExtra("image", model.getImage());
                        recipeIntent.putExtra("title", model.getTitle());
                        recipeIntent.putExtra("composition", model.getComposition());
                        recipeIntent.putExtra("description", model.getDescription());
                        startActivity(recipeIntent);
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item, parent, false);
                return new MyViewHolder(view);
            }
        };
        recipeList.setAdapter(adapter);
        adapter.startListening();
        Collections.sort(list, new Comparator<Recipe>() { @Override public int compare(Recipe lhs, Recipe rhs) { return lhs.getTitle().compareTo(rhs.getTitle()); } });
        adapter.notifyDataSetChanged();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recipeList = root.findViewById(R.id.myList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipeList.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getUid();
        list = new ArrayList<>();
        editText = root.findViewById(R.id.awd);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    search(s.toString());
                } else {
                    search("");

                }
            }
        });

        query = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");

        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");

        addNewRecipe = root.findViewById(R.id.newRecipe);
        addNewRecipe.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), NewRecipe.class);
        startActivity(intent);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView myTitle, myComposition, myDescription;
        ImageView myImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myTitle = itemView.findViewById(R.id.myTitle);
            myComposition = itemView.findViewById(R.id.myComposition);
            myDescription = itemView.findViewById(R.id.myDescription);
            myImage = itemView.findViewById(R.id.myImage);
        }

    }

    private void search(String s) {
        Query searchQuery = mRef.orderByChild("title")
                .startAt(s)
                .endAt(s + "\uf0ff");
        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    list.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        final Recipe recipe = dss.getValue(Recipe.class);
                        list.add(recipe);
                    }

                    DataAdapter myAdapter = new DataAdapter(list, getContext());
                    recipeList.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}