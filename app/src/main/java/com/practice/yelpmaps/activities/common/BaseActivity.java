package com.practice.yelpmaps.activities.common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.practice.yelpmaps.R;
import com.practice.yelpmaps.db.DatabaseHelper;

/**
 * Created by h125673 on 3/5/16.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null)
        {
            getFragmentManager().beginTransaction().add(R.id.fragmentContainer, createFragment()).commit();
        }

    }

    protected abstract Fragment createFragment();


    private DatabaseHelper mDatabaseHelper = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
    }

    public DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }
}