package com.practice.yelpmaps.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.practice.yelpmaps.R;
import com.practice.yelpmaps.fragments.common.BaseFragment;
import com.practice.yelpmaps.model.SearchEntry;
import com.practice.yelpmaps.model.SearchResult;
import com.practice.yelpmaps.model.SearchResultList;
import com.practice.yelpmaps.network.NetworkManager;
import com.practice.yelpmaps.ui.EndlessRecyclerOnScrollListener;
import com.practice.yelpmaps.ui.SearchResultsAdapter;
import com.practice.yelpmaps.util.NetworkUtils;
import com.yelp.clientlib.entities.SearchResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;


/**
 * Created by h125673 on 3/5/16.
 * https://github.com/Yelp/yelp-android
 */
public class MainFragment extends BaseFragment
{
    private final static String TAG = MainFragment.class.getCanonicalName();

    @Bind(R.id.results_view)
    RecyclerView mListView;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Bind(R.id.instructions)
    TextView mInstructions;

    private GridLayoutManager mGridLayoutManager;
    private SearchResultsAdapter mAdapter;

    private MenuItem mSearchMenuItem;

    private ArrayList<SearchResult> mResults = new ArrayList<>();

    private static final int OFFSET = 20;
    private String mQuery;
    private int mQueryOffset = 0;


    private Call<SearchResponse> mSearchCall;
    private Callback<SearchResponse> mSearchCallback = new Callback<SearchResponse>()
    {
        @Override
        public void onResponse(retrofit.Response<SearchResponse> response, Retrofit retrofit)
        {
            showResults();
            if (response.body().businesses().isEmpty())
            {
                int stringResId = 0;
                if (mResults.isEmpty())
                {
                    stringResId = R.string.result_empty;
                }
                else
                {
                    stringResId = R.string.end_of_result;
                }
                Toast.makeText(getActivity(), stringResId, Toast.LENGTH_LONG).show();
            }
            else
            {
                List<SearchResult> results = SearchResult.convert(response.body().businesses());
                mResults.addAll(results);
                mAdapter.notifyDataSetChanged();
                dumpResults(results);

                String key = getKey(mQueryOffset, mQuery);
                SearchEntry entry = getEntry(key);

                if (entry == null)
                {
                    createEntry(key, results);
                }
                else
                {
                    // does exist just update
                    Gson gson = new GsonBuilder().create();
                    entry.result = gson.toJson(response.body().businesses());
                    updateEntry(entry);
                }
            }
        }

        @Override
        public void onFailure(Throwable throwable)
        {
            showInstructions(R.string.error_quering);
            Log.e(TAG, throwable.toString());
        }
    };

    private String getKey(int offset, String query)
    {
        StringBuilder key = new StringBuilder();
        key.append(query);
        key.append("#");
        key.append(Integer.toString(offset));

        return key.toString();
    }

    private void dumpResults(List<SearchResult> results)
    {
        if (results != null)
        {
            Log.d(TAG, " result count = " + results.size());

            for (SearchResult result : results)
            {
                Log.d(TAG, "result id = " + result.getId() + " , name = " + result.getName());
            }
        }
    }

    private EndlessRecyclerOnScrollListener mListViewScrollListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new SearchResultsAdapter(mResults);
    }

    private void showProgressBar()
    {
        mInstructions.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    private void showInstructions(int stringId)
    {
        mInstructions.setText(stringId);
        mInstructions.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
    }

    private void showResults()
    {
        mInstructions.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        // setup list view
        mGridLayoutManager = new GridLayoutManager(container.getContext(),
                3, GridLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(mGridLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setAdapter(mAdapter);

        mListViewScrollListener = new EndlessRecyclerOnScrollListener(mGridLayoutManager, OFFSET)
        {
            @Override
            public void onLoadMore(int page)
            {
                Log.d(TAG, "loadMore called with offset = " + page);
                search(page * OFFSET);
            }
        };

        mListView.addOnScrollListener(mListViewScrollListener);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        if (searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    if (mQuery == null)
                    {
                        showProgressBar();
                    }

                    if (mQuery != null && mQuery.compareTo(query) != 0)
                    {
                        showProgressBar();
                        mResults.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    mQuery = query;
                    search(0);
                    mSearchMenuItem.collapseActionView();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText)
                {
                    return false;
                }
            });
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mQuery == null)
        {
            showInstructions(R.string.instructions);
        }

        if (!NetworkManager.getInstance().init())
        {
            Toast.makeText(getActivity(), R.string.error_authenticating_to_yelp, Toast.LENGTH_LONG).show();
            mSearchMenuItem.setEnabled(false);
        }
    }

    private void search(int offset)
    {
        mQueryOffset = offset;
        Log.d(TAG, "search called with offset " + offset);

        if (NetworkUtils.IsNetworkConnectionAvailable(getActivity().getApplicationContext()))
        {
            mSearchCall = NetworkManager.getInstance().search(mQuery, offset);

            if (mSearchCall != null)
            {
                mSearchCall.enqueue(mSearchCallback);
            }
        }
        else
        {
            Log.d(TAG, "Offline Mode");

            // store results for caching
            String key = getKey(mQueryOffset, mQuery);

            SearchEntry entry = getEntry(key);
            if (entry == null)
            {
                Log.d(TAG, "No cached entry found for " + key);
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
            else
            {
                List<SearchResult> results = ConvertToResult(entry);
                if (results != null)
                {
                    Log.d(TAG, "cached entry found for " + key + " result size = " + results.size());
                    showResults();
                    mResults.addAll(results);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    public SearchEntry getEntry(String searchKey)
    {
        // get our dao
        RuntimeExceptionDao<SearchEntry, Integer> searchEntryDao = mBaseActivity.getHelper().getSearchEntryDao();
        SearchEntry entry =
                null;
        try
        {
            List<SearchEntry> result = searchEntryDao.queryBuilder().where()
                    .eq(SearchEntry.SEARCHKEY_COL_NAME, searchKey)
                    .query();
            if (!result.isEmpty())
            {
                entry = result.get(0);
            }
        } catch (SQLException e)
        {
            Log.e(TAG, e.toString());
        }

        return entry;
    }

    public void updateEntry(SearchEntry update)
    {
        Log.d(TAG, "DB---> updatring entry in DB for update - id = " + update.searchKey);
        RuntimeExceptionDao<SearchEntry, Integer> searchEntryDao = mBaseActivity.getHelper().getSearchEntryDao();
        searchEntryDao.update(update);
    }

    public void createEntry(String key, List<SearchResult> results)
    {
        RuntimeExceptionDao<SearchEntry, Integer> searchEntryDao = mBaseActivity.getHelper().getSearchEntryDao();

        SearchEntry row = new SearchEntry();
        row.searchKey = key;
        Gson gson = new GsonBuilder().create();
        row.result = gson.toJson(results);

        Log.d(TAG, "key = " + row.searchKey);
        Log.d(TAG, "converted to json, result = " + row.result);
        Log.d(TAG, "DB ---> creating entry in DB for update - id = " + key);

        searchEntryDao.create(row);
    }

    public static List<SearchResult> ConvertToResult(SearchEntry entry)
    {
        List<SearchResult> results = null;
        if (entry.result != null)
        {
            Gson gson = new GsonBuilder().create();
            results = gson.fromJson(entry.result, SearchResultList.class);
        }
        return results;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mSearchCall != null)
        {
            mSearchCall.cancel();
        }
    }

}
