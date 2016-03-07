package com.practice.yelpmaps.network;

import android.util.Log;

import com.practice.yelpmaps.util.YelpLoginConstants;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit.Call;

/**
 * Created by h125673 on 3/5/16.
 */
public class NetworkManager
{
    private static final String TAG = NetworkManager.class.getCanonicalName();
    private static NetworkManager sInstance;
    private YelpAPI mYelpAPI;

    public static NetworkManager getInstance()
    {
        if(sInstance == null)
        {
            sInstance = new NetworkManager();
        }
        return sInstance;
    }

    private NetworkManager()
    {

    }

    public boolean init()
    {
        boolean success = true;
        YelpAPIFactory apiFactory = new YelpAPIFactory(YelpLoginConstants.CONSUMER_KEY,
                YelpLoginConstants.CONSUMER_SECRET, YelpLoginConstants.TOKEN,
                YelpLoginConstants.TOKEN_SECRET);
        mYelpAPI = apiFactory.createAPI();

        if(mYelpAPI == null)
        {
            success = false;
            Log.d(TAG, "failed to create API" );
        }

        return success;
    }

    public Call<SearchResponse> search(final String query, final int offset)
    {
        Call<SearchResponse> callback = null;
        if(mYelpAPI != null)
        {
            Map<String, String> params = new HashMap<>();

            params.put("offset", Integer.toString(offset));
            params.put("term", query);
            params.put("limit", "20");

            // language
            params.put("lang", "en");

            callback = mYelpAPI.search("Austin", params);
        }
        return callback;
    }
}
