package com.practice.yelpmaps.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by h125673 on 3/6/16.
 */
@DatabaseTable(tableName= "SearchEntryTable")
public class SearchEntry
{
    private final static String TAG = SearchEntry.class.getCanonicalName();

    public static final String  SEARCHKEY_COL_NAME = "searchKey";
    public static final String  RESULT_COL_NAME = "result";

    @DatabaseField(id = true, index = true)
    public String searchKey;

    @DatabaseField
    public String result;

}
