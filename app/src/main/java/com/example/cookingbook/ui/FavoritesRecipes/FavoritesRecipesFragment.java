package com.example.cookingbook.ui.FavoritesRecipes;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.R;
import com.example.cookingbook.Recipe;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FavoritesRecipesFragment extends Fragment {

    private final int MENU_DELETE = 2;
    private RecyclerView favoriteList;
    private DatabaseReference mRef;
    private Query query;
    private FirebaseAuth mAuth;
    private String currentTitle;
    private FirebaseRecyclerOptions<Recipe> options;
    private FirebaseRecyclerAdapter<Recipe, FavoritesViewHolder> adapter;


    @Override
    public void onStart() {
        super.onStart();
        options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        /*FirebaseRecyclerAdapter<Recipe, FavoritesViewHolder> adapter = new FirebaseRecyclerAdapter<Recipe, FavoritesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position, @NonNull Recipe model) {
                recipeID = getRef(position).getKey();
                holder.getFavoriteTitle().setText(model.getTitle());
                holder.getFavoriteComposition().setText(model.getComposition());
                Picasso.get()
                        .load(Uri.parse(model.getImage()))
                        .placeholder(R.drawable.defaultimage)
                        .fit()
                        .centerInside()
                        .into(holder.getFavoriteImage());

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String a = Integer.toString(holder.getAdapterPosition());
                        currentTitle = getItem(position).getTitle();
                        return false;
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent recipeIntent = new Intent(getContext(), RecipeCard.class);
                        recipeIntent.putExtra("image", model.getImage());
                        recipeIntent.putExtra("title", model.getTitle());
                        recipeIntent.putExtra("composition", model.getComposition());
                        recipeIntent.putExtra("description", model.getDescription());
                        recipeIntent.putExtra("recipe_ref", recipeID);
                        startActivity(recipeIntent);
                    }
                });
            }
            @NonNull
            @Override
            public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list_item, parent, false);
                return new FavoritesViewHolder(view);
            }
        };*/
        adapter = new FavoritesRecipesFragmentAdapter(options, getContext());
        favoriteList.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteItem(final String currentTitle) {
        Query mQuery = mRef.orderByChild("title").equalTo(currentTitle);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    dss.getRef().removeValue();
                }
                Toast.makeText(getContext(), "Рецепт удален", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites_recipes, container, false);
        favoriteList = root.findViewById(R.id.TEMP);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favoriteList.setLayoutManager(layoutManager);
        registerForContextMenu(favoriteList);
        mAuth = FirebaseAuth.getInstance();
        query = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("favorites").orderByChild("title");
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("favorites");
        return root;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                FavoritesRecipesFragmentAdapter ad = (FavoritesRecipesFragmentAdapter)adapter;
                currentTitle = ((FavoritesRecipesFragmentAdapter) adapter).getCurrentTitle();
                deleteItem(currentTitle);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /*public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private static final int MENU_DELETE = 2;
        private TextView favoriteTitle, favoriteComposition, favoriteDescription;
        private ImageView favoriteImage;


        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteTitle = itemView.findViewById(R.id.favoriteTitle);
            favoriteComposition = itemView.findViewById(R.id.favoriteComposition);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
            itemView.setOnCreateContextMenuListener(this);
        }

        public TextView getFavoriteTitle() {
            return favoriteTitle;
        }

        public void setFavoriteTitle(TextView favoriteTitle) {
            this.favoriteTitle = favoriteTitle;
        }

        public TextView getFavoriteComposition() {
            return favoriteComposition;
        }

        public void setFavoriteComposition(TextView favoriteComposition) {
            this.favoriteComposition = favoriteComposition;
        }

        public TextView getFavoriteDescription() {
            return favoriteDescription;
        }

        public void setFavoriteDescription(TextView favoriteDescription) {
            this.favoriteDescription = favoriteDescription;
        }

        public ImageView getFavoriteImage() {
            return favoriteImage;
        }

        public void setFavoriteImage(ImageView favoriteImage) {
            this.favoriteImage = favoriteImage;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, MENU_DELETE, 0, "Удалить");
        }

    }*/
}