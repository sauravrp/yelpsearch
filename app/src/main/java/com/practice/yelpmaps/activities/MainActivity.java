package com.practice.yelpmaps.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.practice.yelpmaps.R;
import com.practice.yelpmaps.activities.common.BaseActivity;
import com.practice.yelpmaps.fragments.MainFragment;

public class MainActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected Fragment createFragment()
    {
        return new MainFragment();
    }
}
