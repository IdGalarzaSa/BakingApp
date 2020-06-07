package com.galarzaivan.bakingapp.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.adapters.IngredientAdapter;
import com.galarzaivan.bakingapp.adapters.SummaryAdapter;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Ingredient;
import com.galarzaivan.bakingapp.models.Recipe;
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
    private Button mSaveIngredients;
    private Recipe mRecipe;

    private final static String RECIPE = "recipe";

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        mContext = rootView.getContext();
        mSaveIngredients = (Button) rootView.findViewById(R.id.save_ingredients);
        mSaveIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIngredients();
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ingredients);
        configRecyclerView();

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE)) {
            String previousData = savedInstanceState.getString(RECIPE);
            mRecipe = new Gson().fromJson(previousData,Recipe.class);
            mIngredients = mRecipe.getIngredients();
            mIngredientAdapter.setIngredientsList(mIngredients);
        } else {
            if (getArguments() != null && getArguments().containsKey(AppConstants.RECIPE)) {
                String data = getArguments().getString(AppConstants.RECIPE);
                mRecipe = new Gson().fromJson(data,Recipe.class);
                mIngredients = mRecipe.getIngredients();
            } else {
                mIngredients = null;
            }
        }


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
        String data = new Gson().toJson(mRecipe);
        outState.putString(RECIPE, data);
    }

    private void saveIngredients() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = new Gson().toJson(mRecipe);
        editor.putString(AppConstants.RECIPE_WIDGET, data);
        editor.apply();
    }

}
