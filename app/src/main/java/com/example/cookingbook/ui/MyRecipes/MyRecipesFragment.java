package com.example.cookingbook.ui.MyRecipes;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MyRecipesFragment extends Fragment implements View.OnClickListener {

    private final int MENU_SHARE = 1;
    private final int MENU_DELETE = 2;
    private String count;
    private FloatingActionButton addNewRecipe;
    private RecyclerView recipeList;
    private DatabaseReference mRef;
    private Query query;
    private FirebaseAuth mAuth;
    private EditText editText;
    private String recipeID;
    private String currentTitle;
    private DatabaseReference ref;

    @Override
    public void onResume() {
        super.onResume();
        Query tmpQuery = mRef.orderByChild("title");
        tmpQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = Long.toString(dataSnapshot.getChildrenCount());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid())
                        .child("userInfo");
                Query q = reference.orderByChild("phone");
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("count").setValue(count);
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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        FirebaseRecyclerAdapter<Recipe, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Recipe, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Recipe model) {
                recipeID = getRef(position).getKey();
                holder.myTitle.setText(model.getTitle());
                holder.myComposition.setText(model.getComposition());
                Picasso.get()
                        .load(Uri.parse(model.getImage()))
                        .placeholder(R.drawable.defaultimage)
                        .fit()
                        .centerInside()
                        .into(holder.myImage);

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
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
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item, parent, false);
                return new MyViewHolder(view);
            }
        };
        recipeList.setAdapter(adapter);
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

    private void shareItem(final String currentTitle) {
        Query shareQuery = mRef.orderByChild("title").equalTo(currentTitle);
        shareQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    String title = dss.child("title").getValue().toString();
                    String composition = dss.child("composition").getValue().toString();
                    String description = dss.child("description").getValue().toString();
                    String image = dss.child("image").getValue().toString();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("userInfo");
                    Query infoQuery = databaseReference.orderByChild("phone");
                    infoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            Recipe recipe = new Recipe(title, composition, description, image, uid);
                            ref = FirebaseDatabase.getInstance().getReference().child("recipes");
                            ref.push().setValue(recipe);
                            Toast.makeText(getContext(), "Вы поделились рецептом", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        recipeList = root.findViewById(R.id.myList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipeList.setLayoutManager(layoutManager);
        registerForContextMenu(recipeList);
        mAuth = FirebaseAuth.getInstance();
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
        query = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes").orderByChild("title");
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes");
        addNewRecipe = root.findViewById(R.id.newRecipe);
        addNewRecipe.setOnClickListener(this);
        return root;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SHARE:
                shareItem(currentTitle);
                break;
            case MENU_DELETE:
                deleteItem(currentTitle);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), NewRecipe.class);
        startActivity(intent);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView myTitle, myComposition;
        ImageView myImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myTitle = itemView.findViewById(R.id.myTitle);
            myComposition = itemView.findViewById(R.id.myComposition);
            myImage = itemView.findViewById(R.id.myImage);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, MENU_SHARE, 0, "Share");
            menu.add(0, MENU_DELETE, 0, "Delete");
        }
    }


    private void search(String s) {
        query = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("recipes").orderByChild("title")
                .startAt(s)
                .endAt(s + "\uf0ff");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        FirebaseRecyclerAdapter<Recipe, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Recipe, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Recipe model) {
                recipeID = getRef(position).getKey();
                holder.myTitle.setText(model.getTitle());
                holder.myComposition.setText(model.getComposition());
                Picasso.get()
                        .load(Uri.parse(model.getImage()))
                        .placeholder(R.drawable.defaultimage)
                        .fit()
                        .centerInside()
                        .into(holder.myImage);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
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
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item, parent, false);
                return new MyViewHolder(view);
            }
        };
        recipeList.setAdapter(adapter);
        adapter.startListening();
    }
}