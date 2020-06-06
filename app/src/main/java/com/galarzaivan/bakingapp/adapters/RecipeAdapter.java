package com.galarzaivan.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private static final String TAG = "RecipeAdapter";

    private List<Recipe> mRecipeList;

    // Variable global que manejara la interface
    private final RecipeAdapterOnClickHandler mClickHandler;

    // Obtenemos el interface y lo almacenamos en una variable global
    public RecipeAdapter(RecipeAdapterOnClickHandler mOnClickHandler) {
        mClickHandler = mOnClickHandler;
    }

    public void setRecipeList(List<Recipe> recipes) {
        mRecipeList = recipes;
        notifyDataSetChanged();
    }

    public interface RecipeAdapterOnClickHandler {
        void RecipeClickListener(Recipe recipe);
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.recipe_card_item, parent, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        holder.loadData(recipe);
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) {
            return 0;
        }
        return mRecipeList.size();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mRecipeImage;
        private TextView mRecipeTitle;
        private TextView mRecipeSubTitle;

        public RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeImage = (ImageView) itemView.findViewById(R.id.cv_recipe);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.cv_title);
            mRecipeSubTitle = (TextView) itemView.findViewById(R.id.cv_subTitle);
            itemView.setOnClickListener(this);
        }

        private void loadData(Recipe recipe) {
            if (recipe.getImage() != null && !recipe.getImage().equals("")) {
                Picasso.get()
                        .load(recipe.getImage())
                        .error(R.drawable.image_not_found)
                        .into(mRecipeImage);
            }
            mRecipeTitle.setText(recipe.getName());
            String newSubTitle = mRecipeSubTitle.getText() + String.valueOf(recipe.getServings());
            mRecipeSubTitle.setText(newSubTitle);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipeList.get(adapterPosition);
            mClickHandler.RecipeClickListener(recipe);
        }
    }
}
