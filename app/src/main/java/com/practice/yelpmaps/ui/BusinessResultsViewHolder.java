package com.practice.yelpmaps.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.practice.yelpmaps.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by h125673 on 3/6/16.
 */
public class BusinessResultsViewHolder extends RecyclerView.ViewHolder
{
    @Bind(R.id.image_view)
    ImageView mImageView;

    @Bind(R.id.image_view_frame_layout)
    FrameLayout mFrameLayout;

    public BusinessResultsViewHolder(View itemView)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
