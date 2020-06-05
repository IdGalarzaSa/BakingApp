package com.galarzaivan.bakingapp.requests;

import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingRequests {

    @GET(AppConstants.BAKING_DATA)
    Call<List<Recipe>> getRecipes();

}
