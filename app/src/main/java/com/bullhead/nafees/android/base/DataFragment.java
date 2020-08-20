package com.bullhead.nafees.android.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.databinding.GenericRecyclerLayoutBinding;
import com.bullhead.nafees.android.ui.widget.ErrorView;
import com.bullhead.nafees.android.ui.widget.SearchView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class DataFragment<T> extends BaseFragment {
    private static final String TAG = DataFragment.class.getSimpleName();

    protected RecyclerView recyclerView;
    protected ErrorView errorView;
    protected SwipeRefreshLayout refreshLayout;
    private SearchView searchView;

    private Disposable disposable;
    private GenericRecyclerLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = GenericRecyclerLayoutBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recyclerView;
        errorView = binding.errorView;
        refreshLayout = binding.refreshLayout;
        searchView = binding.searchView;

        refreshLayout.setColorSchemeResources(R.color.primaryColor,
                R.color.primaryDarkColor, R.color.primaryLightColor);
        refreshLayout.setOnRefreshListener(this::loadData);
        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.secondaryColor);
        errorView.setListener(this::loadData);
        searchView.setListener(this::performSearch);
        loadData();
    }

    protected void onSuccess(@NonNull List<T> data) {
        hideProgress();
        searchView.setVisibility(hasSearch() ? View.VISIBLE : View.GONE);
        errorView.setVisibility(View.GONE);
        disposable.dispose();
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(getAdapter(data));
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void performSearch(@Nullable String text) {
        Log.i(TAG, "performSearch: No one is handling search");
    }

    @NonNull
    public abstract RecyclerView.Adapter getAdapter(@NonNull List<T> data);

    @NonNull
    public abstract Observable<List<T>> getCall();

    public boolean hasSearch() {
        return false;
    }

    @DrawableRes
    public int getErrorIcon() {
        return R.drawable.ic_signal_wifi_off_black_24dp;
    }

    private void onError(@NonNull Throwable error) {
        disposable.dispose();
        hideProgress();
        recyclerView.setVisibility(View.GONE);
        errorView.getIcError().setImageResource(getErrorIcon());
        errorView.setErrorText(error.getMessage());
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (disposable != null && !disposable.isDisposed()) {
            if (refreshLayout != null) {
                refreshLayout.setRefreshing(true);
            }
        }
    }

    @Override
    public void onPause() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        super.onPause();
    }

    private void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    private void showProgress() {
        errorView.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);
        refreshLayout.setRefreshing(true);
    }

    private void loadData() {
        showProgress();
        disposable = getCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (recyclerView.getAdapter() == null &&
                errorView.getVisibility() == View.GONE &&
                !refreshLayout.isRefreshing()) {
            loadData();
        }
    }

    @NonNull
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(context);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getContext() != null && recyclerView != null && recyclerView.getAdapter() != null) {
            //if GridLayoutManager maybe need to refresh number of columns
            recyclerView.setLayoutManager(getLayoutManager());
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
