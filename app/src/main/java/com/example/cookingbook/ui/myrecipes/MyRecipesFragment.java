package com.example.cookingbook.ui.myrecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.R;
import com.example.cookingbook.model.Recipe;
import com.example.cookingbook.util.SearchTextWatcher;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.example.cookingbook.constant.MenuItems.MENU_DELETE;
import static com.example.cookingbook.constant.MenuItems.MENU_SHARE;


public class MyRecipesFragment extends Fragment implements View.OnClickListener, MyRecipesContract.View {

    private FloatingActionButton mAddNewRecipe;
    private RecyclerView mRecipeList;
    private EditText mSearchEditText;
    private FirebaseRecyclerAdapter<Recipe, MyRecipesViewHolder> mAdapter;
    private MyRecipesPresenter mPresenter;

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new MyRecipesFragmentAdapter(mPresenter.getFirebaseRecyclerOptionsSettings(), Objects.requireNonNull(getContext()));
        mRecipeList.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        init(root);
        registerForContextMenu(mRecipeList);
        mSearchEditText.addTextChangedListener(new SearchTextWatcher() {
            @Override
            public void search(@NotNull String searchString) {
                mAdapter = new MyRecipesFragmentAdapter(mPresenter.getSearchFirebaseRecyclerOptionsSettings(searchString), Objects.requireNonNull(getContext()));
                mRecipeList.setAdapter(mAdapter);
                mAdapter.startListening();
            }
        });
        mAddNewRecipe.setOnClickListener(this);
        return root;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SHARE:
                String currentTitle = ((MyRecipesFragmentAdapter) mAdapter).getCurrentTitle();
                mPresenter.onShareItemWasClicked(currentTitle);
                break;
            case MENU_DELETE:
                currentTitle = ((MyRecipesFragmentAdapter) mAdapter).getCurrentTitle();
                mPresenter.onDeleteItemWasClicked(currentTitle);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onCreateRecipeViewWasClicked(Objects.requireNonNull(getContext()));
    }

    @Override
    public void onActionSuccess(@NotNull String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void init(View root) {
        mRecipeList = root.findViewById(R.id.myList);
        mPresenter = new MyRecipesPresenter(this);
        mSearchEditText = root.findViewById(R.id.awd);
        mAddNewRecipe = root.findViewById(R.id.newRecipe);
    }
}