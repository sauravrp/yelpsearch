package com.practice.yelpmaps.model;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h125673 on 3/6/16.
 */
public class SearchResult
{
    private String mName;

    private String mId;

    private String mImageUrl;

    public SearchResult(final Business business)
    {
        mName = business.name();
        mId = business.id();
        mImageUrl = business.imageUrl();
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String mName)
    {
        this.mName = mName;
    }

    public String getId()
    {
        return mId;
    }

    public void setId(String mId)
    {
        this.mId = mId;
    }

    public String getImageUrl()
    {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl)
    {
        this.mImageUrl = mImageUrl;
    }

    public static List<SearchResult> convert(List<Business> businesses)
    {
        List<SearchResult> result = new ArrayList<>(businesses.size());

        for(Business business : businesses)
        {
            result.add(new SearchResult(business));
        }

        return result;
    }
}
