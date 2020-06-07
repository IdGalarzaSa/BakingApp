package com.galarzaivan.bakingapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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
import com.galarzaivan.bakingapp.models.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.net.URL;

public class RecipeInformationFragment extends Fragment {

    private static final String STEP = "step";

    private Step mStep;
    private Context mContext;
    // Views
    private TextView mStepInstructions;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private String mURL;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

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
            mStep = new Gson().fromJson(previousData, Step.class);
            loadData();
            loadMedia();
        } else {
            if (getArguments() != null && getArguments().containsKey(AppConstants.STEP_INFORMATION)) {
                String data = getArguments().getString(AppConstants.STEP_INFORMATION);
                mStep = new Gson().fromJson(data, Step.class);
                loadData();
                loadMedia();
            }
        }

        return rootView;
    }

    private void initViews(View view) {
        mStepInstructions = (TextView) view.findViewById(R.id.tv_step_instructions);
        playerView = view.findViewById(R.id.video_view);
    }

    private void loadMedia() {
        if (mStep.getVideoURL() != null && !mStep.getVideoURL().equals("")) {
            playerView.setVisibility(View.VISIBLE);
            mURL = mStep.getVideoURL();
            initializePlayer();
        } else if (mStep.getThumbnailURL() != null && !mStep.getThumbnailURL().equals("")) {
            playerView.setVisibility(View.VISIBLE);
            mURL = mStep.getThumbnailURL();
            initializePlayer();
        } else {
            playerView.setVisibility(View.GONE);
        }
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(mContext);
        playerView.setPlayer(player);
        Uri uri = Uri.parse(mURL);
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(mContext, getString(R.string.baking_app_name));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void loadData() {
        mStepInstructions.setText(mStep.getDescription());
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String data = new Gson().toJson(mStep);
        outState.putString(STEP, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24 && mURL != null) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null) && mURL != null) {
            initializePlayer();
        }
    }


    /*

        @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
     */

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}
