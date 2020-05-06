package com.example.cookingbook.ui.globalrecipes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.GlobalRecipeCard;
import com.example.cookingbook.R;
import com.example.cookingbook.model.Recipe;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipesFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Recipe> list;
    private RecyclerView recipesRecyclerView;
    private DatabaseReference recipesRef;
    private Query query;
    private FirebaseAuth mAuth;
    private EditText searchEditText;
    private FirebaseRecyclerOptions<Recipe>options;
    private FirebaseRecyclerAdapter<Recipe, RecipesViewHolder> adapter;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), GlobalRecipeCard.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        adapter= new RecipesFragmentAdapter(options,getContext());
        recipesRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        recipesRecyclerView = root.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipesRecyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recipesRecyclerView);
        mAuth = FirebaseAuth.getInstance();
        query = FirebaseDatabase.getInstance().getReference().child("recipes").orderByChild("title");
        recipesRef = FirebaseDatabase.getInstance().getReference().child("recipes");
        list = new ArrayList<>();
        searchEditText = root.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
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
        return root;
    }

    private void search(String s) {
        query = FirebaseDatabase.getInstance().getReference().child("recipes").orderByChild("title")
                .startAt(s)
                .endAt(s + "\uf0ff");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        FirebaseRecyclerAdapter<Recipe, RecipesViewHolder> adapter = new FirebaseRecyclerAdapter<Recipe, RecipesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecipesViewHolder holder, int position, @NonNull Recipe model) {
                String recipeID = getRef(position).getKey();
                recipesRef.child(recipeID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String recipesImage = dataSnapshot.child("image").getValue().toString();
                        String recipesTitle = dataSnapshot.child("title").getValue().toString();
                        String recipesComposition = dataSnapshot.child("composition").getValue().toString();
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent recipeIntent = new Intent(getContext(), GlobalRecipeCard.class);
                                recipeIntent.putExtra("image", model.getImage());
                                recipeIntent.putExtra("title", model.getTitle());
                                recipeIntent.putExtra("composition", model.getComposition());
                                recipeIntent.putExtra("description", model.getDescription());
                                recipeIntent.putExtra("recipe_ref", recipeID);
                                recipeIntent.putExtra("displayName", model.getDisplayName());
                                if (model.getUid() != null) {
                                    recipeIntent.putExtra("uid", model.getUid());
                                } else {
                                    recipeIntent.putExtra("uid", "0");
                                }
                                startActivity(recipeIntent);
                            }
                        });
                        holder.getTitle().setText(recipesTitle);
                        holder.getComposition().setText(recipesComposition);
                        Picasso.get()
                                .load(Uri.parse(recipesImage))
                                .placeholder(R.drawable.defaultimage)
                                .fit()
                                .centerInside()
                                .into(holder.getImage());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new RecipesViewHolder(view);
            }
        };
        recipesRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}