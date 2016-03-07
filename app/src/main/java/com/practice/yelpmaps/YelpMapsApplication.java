package com.practice.yelpmaps;
import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.practice.yelpmaps.util.DisplayUtils;

import java.io.File;

/**
 * Created by h125673 on 3/6/16.
 */
public class YelpMapsApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        /**
         * https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Quick-Setup
         */
        // Create global configuration and initialize ImageLoader with this config
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default.diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(300)
                .defaultDisplayImageOptions(DisplayUtils.displayOptions)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
