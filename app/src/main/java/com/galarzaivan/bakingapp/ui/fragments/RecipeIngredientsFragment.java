package com.galarzaivan.bakingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.adapters.IngredientAdapter;
import com.galarzaivan.bakingapp.adapters.SummaryAdapter;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Ingredient;
import com.galarzaivan.bakingapp.models.Step;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment {

    private final static String TAG = "RecipeIngredients";

    private Context mContext;
    private RecyclerView mRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    private List<Ingredient> mIngredients;

    private final static String INGREDIENTS = "ingredients";

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        mContext = rootView.getContext();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ingredients);
        configRecyclerView();

        if (savedInstanceState != null && savedInstanceState.containsKey(INGREDIENTS)) {
            String previousData = savedInstanceState.getString(INGREDIENTS);
            mIngredients = Arrays.asList(new Gson().fromJson(previousData, Ingredient[].class));
            mIngredientAdapter.setIngredientsList(mIngredients);
        } else {
            if (getArguments() != null && getArguments().containsKey(AppConstants.INGREDIENTS_LIST)) {
                String data = getArguments().getString(AppConstants.INGREDIENTS_LIST);
                mIngredients = Arrays.asList(new Gson().fromJson(data, Ingredient[].class));
            } else {
                mIngredients = null;
            }
        }

        Log.e(TAG, "onCreateView: ---------->" + mIngredients.size());

        if (mIngredients != null && mIngredients.size() > 0) {
            mIngredientAdapter.setIngredientsList(mIngredients);
        } else {
            mIngredientAdapter.setIngredientsList(null);
        }

        return rootView;
    }

    private void configRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                mContext,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mIngredientAdapter = new IngredientAdapter();
        mRecyclerView.setAdapter(mIngredientAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String data = new Gson().toJson(mIngredients);
        outState.putString(INGREDIENTS, data);
    }

}
