package com.example.cookingbook.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.list);
        DataAdapter adapter = new DataAdapter(setInitialData(),getActivity());
        recyclerView.setAdapter(adapter);
        return root;
    }

    private List<Recipe> setInitialData(){
        List<Recipe> recipes = new ArrayList<>();
        for(int i = 0;i<3;i++) {
           /* recipes.add(new Recipe("Pizza", "Cheese\nDough\nMeat", "Description", R.drawable.pizza));
            recipes.add(new Recipe("Burger", "Bread\nBeef\nCheese", "Description", R.drawable.burger));
            recipes.add(new Recipe("Roll", "Rice\nNori\nSalmon", "Description", R.drawable.roll));*/
        }
        return recipes;
    }
}