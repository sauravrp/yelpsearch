package com.practice.yelpmaps.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.practice.yelpmaps.R;

/**
 * Created by h125673 on 3/6/16.
 */
public class DisplayUtils
{
    public static DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(android.R.drawable.presence_offline) // resource or drawable
            .showImageForEmptyUri(android.R.drawable.stat_notify_error) // resource or drawable
            .showImageOnFail(android.R.drawable.stat_notify_error) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
        .build();
}
