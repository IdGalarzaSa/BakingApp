package com.galarzaivan.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.galarzaivan.bakingapp.adapters.RecipeAdapter;
import com.galarzaivan.bakingapp.classes.RetrofitController;
import com.galarzaivan.bakingapp.models.Recipe;
import com.galarzaivan.bakingapp.requests.BakingRequests;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String RECIPE_LIST = "recipe_list";

    private Context mContext;
    private Boolean isOnline = false;
    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    // ProgressLayout
    private LinearLayout mProgressLayout;
    // ErrorLayout
    private LinearLayout mErrorLayout;
    private Button mRetryButton;
    private List<Recipe> mRecipeList;
    // Retrofit
    private BakingRequests mBakingRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        netWorkConnection();
        initViews();
        configRecyclerView();
        initRetrofit();

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_LIST)) {
            String previousData = savedInstanceState.getString(RECIPE_LIST);
            mRecipeList = Arrays.asList(new Gson().fromJson(previousData, Recipe[].class));
            mRecipeAdapter.setRecipeList(mRecipeList);
        } else {
            getData();
        }

    }

    private void netWorkConnection() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            if (connectivityManager != null) {
                connectivityManager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
                            @Override
                            public void onAvailable(Network network) {
                                isOnline = true;
                            }

                            @Override
                            public void onLost(Network network) {
                                isOnline = false;
                            }
                        }
                );
            }
            isOnline = false;
        } catch (Exception e) {
            isOnline = false;
        }
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipes);
        mProgressLayout = (LinearLayout) findViewById(R.id.ly_loading);
        mErrorLayout = (LinearLayout) findViewById(R.id.ly_error);
        mRetryButton = (Button) findViewById(R.id.bt_retry);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void configRecyclerView() {
        // Creamos un Layout Manager para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter();
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    private void initRetrofit() {
        Retrofit retrofit = RetrofitController.getInstance();
        mBakingRequests = retrofit.create(BakingRequests.class);
    }


    // Get Data from server
    private void getData() {
        if (isOnline) {
            showError(false);
            showLoading(true);
            mBakingRequests.getRecipes().enqueue(
                    new Callback<List<Recipe>>() {
                        @Override
                        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                            showLoading(false);

                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    mRecipeList = response.body();
                                    mRecipeAdapter.setRecipeList(mRecipeList);
                                } else {
                                    showError(true);
                                    mRecipeAdapter.setRecipeList(null);
                                }
                            } else {
                                showError(true);
                                Log.e(TAG, "Unsuccessful call: " + response.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Recipe>> call, Throwable t) {
                            showLoading(false);
                            showError(true);
                            Log.e(TAG, "onFailure: " + t.toString());
                        }
                    }
            );
        } else {
            showError(true);
        }
    }

    private void showLoading(Boolean isShow) {
        if (isShow) {
            mProgressLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mProgressLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showError(Boolean isShow) {
        if (isShow) {
            mErrorLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mErrorLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String dataMovie = new Gson().toJson(mRecipeList);
        outState.putString(RECIPE_LIST, dataMovie);
    }
}
