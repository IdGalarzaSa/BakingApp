package com.galarzaivan.bakingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.adapters.SummaryAdapter;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Step;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeSummaryFragment extends Fragment implements SummaryAdapter.SummaryAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private SummaryAdapter mSummaryAdapter;
    private Context mContext;
    private List<Step> mSteps;

    private static final String STEPS = "steps";

    public RecipeSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_summary, container, false);

        mContext = rootView.getContext();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recipe_summary);
        configRecyclerView();

        if (savedInstanceState != null && savedInstanceState.containsKey(STEPS)) {
            String previousData = savedInstanceState.getString(STEPS);
            mSteps = Arrays.asList(new Gson().fromJson(previousData, Step[].class));
            mSummaryAdapter.setSteps(mSteps);
        } else {
            if (getArguments() != null && getArguments().containsKey(AppConstants.STEP_LIST)) {
                String data = getArguments().getString(AppConstants.STEP_LIST);
                mSteps = Arrays.asList(new Gson().fromJson(data, Step[].class));
            } else {
                mSteps = null;
            }
        }

        if (mSteps != null && mSteps.size() > 0) {
            mSummaryAdapter.setSteps(mSteps);
        } else {
            mSummaryAdapter.setSteps(null);
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
        mSummaryAdapter = new SummaryAdapter(this);
        mRecyclerView.setAdapter(mSummaryAdapter);
    }

    @Override
    public void SummaryClickListener(Step step) {
        Toast.makeText(mContext, step.getShortDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String data = new Gson().toJson(mSteps);
        outState.putString(STEPS, data);
    }
}
