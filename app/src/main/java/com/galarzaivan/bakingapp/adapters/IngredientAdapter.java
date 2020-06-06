package com.galarzaivan.bakingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.models.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private static final String TAG = "IngredientAdapter";
    private List<Ingredient> mIngredientsList;

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        mIngredientsList = ingredientsList;
        Log.e(TAG, "setIngredientsList:-----> " + mIngredientsList.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.recipe_ingredient_item, parent, shouldAttachToParentImmediately);
        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterViewHolder holder, int position) {
        Ingredient ingredient = mIngredientsList.get(position);
        holder.loadIngredient(ingredient);
    }

    @Override
    public int getItemCount() {
        if (mIngredientsList == null) {
            return 0;
        }
        return mIngredientsList.size();
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView mIngredientName;
        private TextView mIngredientQuantity;
        private TextView mIngredientMeasure;

        public IngredientAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mIngredientName = (TextView) itemView.findViewById(R.id.tv_ingredient_name);
            mIngredientQuantity = (TextView) itemView.findViewById(R.id.tv_ingredient_quantity);
            mIngredientMeasure = (TextView) itemView.findViewById(R.id.tv_ingredient_measure);
        }

        public void loadIngredient(Ingredient ingredient) {
            mIngredientName.setText(ingredient.getIngredient());
            mIngredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
            mIngredientMeasure.setText(ingredient.getMeasure());
        }
    }

}

