package com.example.cookingbook.ui.favoritesrecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.constant.MenuItems;
import com.example.cookingbook.R;
import com.example.cookingbook.model.Recipe;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

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