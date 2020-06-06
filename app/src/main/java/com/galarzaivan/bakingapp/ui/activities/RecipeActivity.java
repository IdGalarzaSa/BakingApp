package com.galarzaivan.bakingapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Recipe;
import com.galarzaivan.bakingapp.ui.fragments.RecipeInformationFragment;
import com.galarzaivan.bakingapp.ui.fragments.RecipeIngredientsFragment;
import com.galarzaivan.bakingapp.ui.fragments.RecipeSummaryFragment;
import com.google.gson.Gson;

public class RecipeActivity extends AppCompatActivity {

    private Recipe mRecipe;

    private CardView mIngredientsCard;

    private FragmentManager mFragmentManager;
    private RecipeSummaryFragment mSummaryFragment;
    private RecipeIngredientsFragment mIngredientsFragment;
    private RecipeInformationFragment recipeInformationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);

        initViews();

        if (savedInstanceState == null) {
            getData();
            loadSummaryFragment();
        }
    }

    private void initViews() {
        mFragmentManager = getSupportFragmentManager();
        mIngredientsCard = (CardView) findViewById(R.id.cv_ingredients);
        mIngredientsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIngredientsFragment();
            }
        });
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
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mSummaryFragment)
                .commit();

        showIngredientsCard(true);
    }

    private void showIngredientsCard(boolean b) {
        if (b) {
            mIngredientsCard.setVisibility(View.VISIBLE);
        } else {
            mIngredientsCard.setVisibility(View.GONE);
        }
    }

    private void loadIngredientsFragment() {
        mIngredientsFragment = new RecipeIngredientsFragment();
        Bundle bundle = new Bundle();
        String data = new Gson().toJson(mRecipe.getIngredients());
        bundle.putString(AppConstants.INGREDIENTS_LIST, data);
        mIngredientsFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mIngredientsFragment)
                .commit();
        showIngredientsCard(false);
    }
}
