package com.galarzaivan.bakingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Ingredient;
import com.galarzaivan.bakingapp.models.Step;
import com.google.gson.Gson;

import java.util.Arrays;

public class RecipeInformationFragment extends Fragment {

    private static final String STEP = "step";

    private Step mStep;
    private Context mContext;
    // Views
    private TextView mStepInstructions;

    public RecipeInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_information, container, false);
        mContext = rootView.getContext();
        initViews(rootView);

        if (savedInstanceState != null && savedInstanceState.containsKey(STEP)) {
            String previousData = savedInstanceState.getString(STEP);
            Log.e("prueba", "onCreateView------------->" + previousData);
            mStep = new Gson().fromJson(previousData, Step.class);
            loadMedia();
            loadData();
        } else {
            Log.e("prueba", "NO------------->");
            if (getArguments() != null && getArguments().containsKey(AppConstants.STEP_INFORMATION)) {
                String data = getArguments().getString(AppConstants.STEP_INFORMATION);
                mStep = new Gson().fromJson(data, Step.class);
                loadMedia();
                loadData();
            }
        }

        return rootView;
    }

    private void initViews(View view) {
        mStepInstructions = (TextView) view.findViewById(R.id.tv_step_instructions);
    }

    private void loadMedia() {

    }

    private void loadData() {
        mStepInstructions.setText(mStep.getDescription());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String data = new Gson().toJson(mStep);
        outState.putString(STEP, data);
    }
}
