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

import com.example.cookingbook.R;
import com.example.cookingbook.model.Recipe;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.example.cookingbook.constant.MenuItems.MENU_DELETE;

public class FavoritesRecipesFragment extends Fragment implements FavoritesRecipesContract.View {

    private RecyclerView mRecipeList;
    private FirebaseRecyclerAdapter<Recipe, FavoritesViewHolder> mAdapter;
    private FavoritesRecipesPresenter mPresenter;

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new FavoritesRecipesFragmentAdapter(mPresenter.getFirebaseRecyclerOptionsSettings(), Objects.requireNonNull(getContext()));
        mRecipeList.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites_recipes, container, false);
        init(root);
        registerForContextMenu(mRecipeList);
        return root;
    }

    private void init(View root) {
        mRecipeList = root.findViewById(R.id.TEMP);
        mPresenter = new FavoritesRecipesPresenter(this);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == MENU_DELETE) {
            String currentTitle = ((FavoritesRecipesFragmentAdapter) mAdapter).getCurrentTitle();
            mPresenter.onDeleteViewWasClicked(currentTitle);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDeleteSuccess(@NotNull String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}