package com.galarzaivan.bakingapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Recipe;
import com.galarzaivan.bakingapp.ui.fragments.RecipeSummaryFragment;
import com.google.gson.Gson;

public class RecipeActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private RecipeSummaryFragment mSummaryFragment;
    private final static String SUMMARY_FRAGMENT = "summary_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);

        if (savedInstanceState == null) {
            getData();
            loadSummaryFragment();
        }
    }

    private void getData() {
        Intent intent = getIntent();
        if (!intent.hasExtra(AppConstants.RECIPE)) {
            finish();
        }
        String data = intent.getStringExtra(AppConstants.RECIPE);
        mRecipe = new Gson().fromJson(data, Recipe.class);

    }

    private void loadSummaryFragment() {
        mSummaryFragment = new RecipeSummaryFragment();

        Bundle bundle = new Bundle();
        String data = new Gson().toJson(mRecipe.getSteps());
        bundle.putString(AppConstants.STEP_LIST, data);

        mSummaryFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.summary_container, mSummaryFragment)
                .commit();
    }
}
