package com.example.cookingbook.ui.globalrecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingbook.R;
import com.example.cookingbook.util.SearchTextWatcher;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RecipesFragment extends Fragment implements View.OnClickListener,RecipesContract.View {

    private RecyclerView mRecipeList;
    private EditText mSearchEditText;
    private RecipesPresenter mPresenter;
    private RecipesFragmentAdapter mAdapter;

    @Override
    public void onClick(View v) {
        mPresenter.onRecipeViewWasClicked(Objects.requireNonNull(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new RecipesFragmentAdapter(mPresenter.getFirebaseRecyclerOptionsSettings(), Objects.requireNonNull(getContext()));
        mRecipeList.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        init(root);
        mSearchEditText.addTextChangedListener(new SearchTextWatcher() {
            @Override
            public void search(@NotNull String searchString) {
                mAdapter = new RecipesFragmentAdapter(mPresenter.getSearchFirebaseRecyclerOptionsSettings(searchString), Objects.requireNonNull(getContext()));
                mRecipeList.setAdapter(mAdapter);
                mAdapter.startListening();
            }
        });
        return root;
    }

    private void init(View root){
        mPresenter =new RecipesPresenter();
        mRecipeList = root.findViewById(R.id.list);
        mSearchEditText = root.findViewById(R.id.searchEditText);
    }

}