package com.galarzaivan.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.models.Step;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryAdapterViewHolder> {

    private static final String TAG = "SummaryAdapter";

    private List<Step> mSteps;

    private final SummaryAdapterOnClickHandler mClickHandler;

    public void setSteps(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public interface SummaryAdapterOnClickHandler {
        void SummaryClickListener(Step step);
    }

    public SummaryAdapter(SummaryAdapterOnClickHandler mOnClickHandler) {
        mClickHandler = mOnClickHandler;
    }

    @NonNull
    @Override
    public SummaryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.recipe_summary_card, parent, shouldAttachToParentImmediately);
        return new SummaryAdapter.SummaryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapterViewHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.loadStep(step);
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }
        return mSteps.size();
    }


    public class SummaryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCardTitle;

        public SummaryAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardTitle = itemView.findViewById(R.id.tv_summary_title);
            itemView.setOnClickListener(this);
        }

        private void loadStep(Step step) {
            mCardTitle.setText(step.getShortDescription());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mClickHandler.SummaryClickListener(step);
        }
    }

}
