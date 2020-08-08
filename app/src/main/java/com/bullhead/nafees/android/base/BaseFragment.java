package com.bullhead.nafees.android.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import dagger.android.support.DaggerFragment;
import java8.util.Optional;


public abstract class BaseFragment extends DaggerFragment {
    protected Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public Optional<FragmentActivity> activity() {
        return Optional.ofNullable(getActivity());
    }

    public Optional<Context> context() {
        return Optional.ofNullable(getContext());
    }
}
