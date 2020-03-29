package com.example.cookingbook.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    final int MENU_SHARE = 1;
    final int MENU_DELETE = 2;

    private FloatingActionButton addNewRecipe;
    private RecyclerView recipeList;
    private DatabaseReference mRef;
    private Query query;
    private FirebaseAuth mAuth;
    private ArrayList<Recipe> list;
    private ArrayList<Recipe> filtered;
    private EditText editText;
    private FirebaseDatabase mDatabase;
    private String recipeID;
    private String currentTitle;
    private DatabaseReference ref;


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
                holder.myDescription.setText(model.getDescription());
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
                        //    Toast.makeText(getContext(), recipeID, Toast.LENGTH_SHORT).show();

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
      /*  Collections.sort(list, new Comparator<Recipe>() { @Override public int compare(Recipe lhs, Recipe rhs) { return lhs.getTitle().compareTo(rhs.getTitle()); } });
        adapter.notifyDataSetChanged();*/
    }

    private void deleteItem(final String currentTitle) {
        Query mQuery = mRef.orderByChild("title").equalTo(currentTitle);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    dss.getRef().removeValue();
                    // dss.getRef().
                }
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
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
                            Recipe recipe = new Recipe(title, composition, description, image,uid);
                            ref = FirebaseDatabase.getInstance().getReference().child("recipes");
                            ref.push().setValue(recipe);
                            Toast.makeText(getContext(), "Отправлено", Toast.LENGTH_SHORT).show();
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

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recipeList = root.findViewById(R.id.myList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipeList.setLayoutManager(layoutManager);
        registerForContextMenu(recipeList);
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

/*
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,MENU_SHARE,0,"Share");
        menu.add(0,MENU_DELETE,0,"Delete");
    }
*/


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), NewRecipe.class);
        startActivity(intent);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView myTitle, myComposition, myDescription;
        ImageView myImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myTitle = itemView.findViewById(R.id.myTitle);
            myComposition = itemView.findViewById(R.id.myComposition);
            myDescription = itemView.findViewById(R.id.myDescription);
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