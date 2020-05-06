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

import com.example.cookingbook.MenuItems;
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

import org.jetbrains.annotations.NotNull;

public class FavoritesRecipesFragment extends Fragment implements FavoritesRecipesContract.View {

    private RecyclerView favoriteList;
    private FirebaseRecyclerAdapter<Recipe, FavoritesViewHolder> adapter;
    private FavoritesRecipesPresenter mFavoritesRecipesPresenter;

    @Override
    public void onStart() {
        super.onStart();
        adapter = new FavoritesRecipesFragmentAdapter(mFavoritesRecipesPresenter.getFirebaseRecyclerOptionsSettings(), getContext());
        favoriteList.setAdapter(adapter);
        adapter.startListening();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites_recipes, container, false);
        favoriteList = root.findViewById(R.id.TEMP);
       // LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //favoriteList.setLayoutManager(layoutManager);
        registerForContextMenu(favoriteList);
        mFavoritesRecipesPresenter=new FavoritesRecipesPresenter(this);
        return root;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MenuItems.MENU_DELETE:
                String currentTitle = ((FavoritesRecipesFragmentAdapter) adapter).getCurrentTitle();
                mFavoritesRecipesPresenter.onDeleteViewWasClicked(currentTitle);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDeleteSuccess(@NotNull String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}