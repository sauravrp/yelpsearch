package com.practice.yelpmaps.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.practice.yelpmaps.R;
import com.practice.yelpmaps.model.SearchResult;
import com.practice.yelpmaps.util.DisplayUtils;

import java.util.List;

/**
 * Created by h125673 on 3/6/16.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<BusinessResultsViewHolder>
{
    List<SearchResult> mResults;

    public SearchResultsAdapter(List<SearchResult> results)
    {
        mResults = results;
    }

    @Override
    public BusinessResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_view, parent, false);
        BusinessResultsViewHolder holder = new BusinessResultsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BusinessResultsViewHolder holder, int position)
    {
        if (mResults != null)
        {
            SearchResult result = mResults.get(position);

            if (result != null)
            {
                // Log.d(TAG, "---------------------- position " + position + " ---------------------");
//                    Log.d(TAG, "result id = " + result.id() + " , name = " + result.name());
//                    Log.d(TAG, "Loading image url = " + result.imageUrl());

                ImageLoader.getInstance().displayImage(result.getImageUrl(), holder.mImageView);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mResults.size();
    }
}
