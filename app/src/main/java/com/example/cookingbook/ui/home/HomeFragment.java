package com.example.cookingbook.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.DataAdapter;
import com.example.cookingbook.MainActivity;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recipesRecyclerView;
    private DatabaseReference recipesRef;
    private Query query;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query,Recipe.class)
                .build();

        FirebaseRecyclerAdapter<Recipe,RecipesViewHolder> adapter=new FirebaseRecyclerAdapter<Recipe, RecipesViewHolder>(options) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
                return new RecipesViewHolder(view);
            }
        };
        recipesRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recipesRecyclerView=root.findViewById(R.id.list);
        mAuth=FirebaseAuth.getInstance();

        query= FirebaseDatabase.getInstance().getReference().child("recipes");
        recipesRef=FirebaseDatabase.getInstance().getReference().child("recipes");

        return root;
    }

    public static class RecipesViewHolder extends RecyclerView.ViewHolder{

        TextView title,composition,description;
        ImageView image;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            composition=itemView.findViewById(R.id.composition);
            description=itemView.findViewById(R.id.description);
            image=itemView.findViewById(R.id.image);
        }
    }
}