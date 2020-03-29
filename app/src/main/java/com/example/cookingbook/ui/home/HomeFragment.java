package com.example.cookingbook.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.cookingbook.GlobalRecipeCard;
import com.example.cookingbook.MainActivity;
import com.example.cookingbook.R;
import com.example.cookingbook.Recipe;
import com.example.cookingbook.RecipeCard;
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
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), GlobalRecipeCard.class);
        startActivity(intent);
    }

    private String currentTitle;
    private ArrayList<Recipe> list;
    private RecyclerView recipesRecyclerView;
    private DatabaseReference recipesRef;
    private Query query;
    private FirebaseAuth mAuth;
    private EditText searchEditText;


    @Override
    public void onStart() {
        super.onStart();
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
                        String recipesDescription = dataSnapshot.child("description").getValue().toString();


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
                                recipeIntent.putExtra("recipesCount", model.getRecipesCount());
                                recipeIntent.putExtra("avatarURL", model.getAvatarURL());
                                recipeIntent.putExtra("phoneNumber", model.getPhone());
                                if (model.getUid() != null) {
                                    recipeIntent.putExtra("uid", model.getUid());
                                } else {
                                    recipeIntent.putExtra("uid", "0");
                                }
                                startActivity(recipeIntent);
                            }
                        });


                        holder.title.setText(recipesTitle);
                        holder.composition.setText(recipesComposition);
                        holder.description.setText(recipesDescription);
                        dataSnapshot.getChildrenCount();
                        Picasso.get()
                                .load(Uri.parse(recipesImage))
                                .placeholder(R.drawable.defaultimage)
                                .fit()
                                .centerInside()
                                .into(holder.image);
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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

    public static class RecipesViewHolder extends RecyclerView.ViewHolder {


        TextView title, composition, description;
        ImageView image;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            composition = itemView.findViewById(R.id.composition);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
        }

    }

    private void search(String s) {
        Query searchQuery = recipesRef.orderByChild("title")
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
                    recipesRecyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}