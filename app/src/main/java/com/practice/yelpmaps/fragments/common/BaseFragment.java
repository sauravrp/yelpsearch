package com.practice.yelpmaps.fragments.common;

import android.app.Fragment;
import android.os.Bundle;

import com.practice.yelpmaps.activities.common.BaseActivity;

/**
 * Created by h125673 on 3/5/16.
 */
public class BaseFragment extends Fragment
{
    protected BaseActivity mBaseActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() != null && getActivity() instanceof BaseActivity)
        {
            mBaseActivity = (BaseActivity) getActivity();
        }
    }



    @Override
    public void onDetach()
    {
        mBaseActivity = null;
        super.onDetach();
    }
}