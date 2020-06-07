package com.galarzaivan.bakingapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.ui.widget.RecipeIngredientsWidget;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Recipe;
import com.galarzaivan.bakingapp.models.Step;
import com.galarzaivan.bakingapp.ui.fragments.RecipeInformationFragment;
import com.galarzaivan.bakingapp.ui.fragments.RecipeIngredientsFragment;
import com.galarzaivan.bakingapp.ui.fragments.RecipeSummaryFragment;
import com.google.gson.Gson;

public class RecipeActivity extends AppCompatActivity implements RecipeSummaryFragment.OnStepSelected, RecipeIngredientsFragment.OnIngredientsSaved {

    private static final String CURRENT_FRAGMENT = "current_fragment";
    private Recipe mRecipe;

    private CardView mIngredientsCard;

    private String INGREDIENT_FRAGMENT = "ingredients_fragment";
    private String INFORMATION_FRAGMENT = "information_fragment";

    private FragmentManager mFragmentManager;
    private RecipeSummaryFragment mSummaryFragment;
    private RecipeIngredientsFragment mIngredientsFragment;
    private RecipeInformationFragment mRecipeInformationFragment;
    private Context mContext;
    private boolean isPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);
        mContext = this;
        initViews();
        isPortrait = isPortrait();
        //showIngredientsCard(false);

        if (savedInstanceState == null) {
            getData();
        } else {
            mRecipe = new Gson().fromJson(savedInstanceState.getString(AppConstants.RECIPE_INSTANCE), Recipe.class);
        }

        loadSummaryFragment();

        if (!isPortrait) {
            showIngredientsCard(true);
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

    private void showIngredientsCard(boolean b) {
        if (b) {
            mIngredientsCard.setVisibility(View.VISIBLE);
        } else {
            mIngredientsCard.setVisibility(View.GONE);
        }
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

        if (isPortrait) {
            showIngredientsCard(true);
        }
    }

    private void loadIngredientsFragment() {
        mIngredientsFragment = new RecipeIngredientsFragment();
        Bundle bundle = new Bundle();
        String data = new Gson().toJson(mRecipe);
        bundle.putString(AppConstants.RECIPE, data);
        mIngredientsFragment.setArguments(bundle);

        if (isPortrait) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, mIngredientsFragment)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_second_container, mIngredientsFragment)
                    .commit();
        }
        if (isPortrait) {
            showIngredientsCard(false);
        }
    }

    private void loadStepInstructions(Step step) {
        mRecipeInformationFragment = new RecipeInformationFragment();
        Bundle bundle = new Bundle();
        String data = new Gson().toJson(step);
        bundle.putString(AppConstants.STEP_INFORMATION, data);
        mRecipeInformationFragment.setArguments(bundle);
        if (isPortrait) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, mRecipeInformationFragment)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_second_container, mRecipeInformationFragment)
                    .commit();
        }
        if (isPortrait) {
            showIngredientsCard(false);
        }
    }

    @Override
    public void onStepSelected(Step step) {
        loadStepInstructions(step);
    }

    @Override
    public void onBackPressed() {
        Fragment f = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (f instanceof RecipeIngredientsFragment || f instanceof RecipeInformationFragment)
            loadSummaryFragment();
        else {
            super.onBackPressed();
        }
    }

    private void updateWidget() {
        Intent intent = new Intent(this, RecipeIngredientsWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext, RecipeIngredientsWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onIngredientsSaved() {
        updateWidget();
    }

    private boolean isPortrait() {
        int orientation = this.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String data = new Gson().toJson(mRecipe);
        outState.putString(AppConstants.RECIPE_INSTANCE, data);
    }
}
