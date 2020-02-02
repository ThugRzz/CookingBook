package com.example.cookingbook;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.ui.home.HomeFragment;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private Context parent;

    public DataAdapter(List<Recipe> recipes, Context parent) {
        this.recipes = recipes;
        this.parent = parent;
    }

    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.imageView.setImageResource(recipe.getImage());
        holder.titleView.setText(recipe.getTitle());
        holder.compositionView.setText(recipe.getComposition());
        holder.descriptionView.setText(recipe.getDescription());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleView, compositionView, descriptionView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            titleView = view.findViewById(R.id.title);
            compositionView = view.findViewById(R.id.composition);
            descriptionView = view.findViewById(R.id.description);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionIndex = getAdapterPosition();
                    Recipe recipe = recipes.get(positionIndex);
                    String title = recipe.getTitle();
                    int image = recipe.getImage();
                    String composition = recipe.getComposition();
                    String description = recipe.getDescription();
                    Intent intent= new Intent(parent,RecipeCard.class);
                    intent.putExtra("image",image);
                    intent.putExtra("title",title);
                    intent.putExtra("composition",composition);
                    intent.putExtra("description",description);
                    parent.startActivity(intent);
                }
            });
        }
    }
}
