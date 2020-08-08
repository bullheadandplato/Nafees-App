package com.bullhead.nafees.android.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bullhead.nafees.android.R;
import com.google.android.material.card.MaterialCardView;

public class SearchView extends LinearLayout implements TextWatcher {
    private MaterialCardView searchView;
    private ImageView        btnCloseSearch;
    private EditText         etSearch;
    private QueryListener    listener;


    public SearchView(Context context) {
        super(context);
        init(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.search_view, this, true);
        searchView     = findViewById(R.id.rootView);
        btnCloseSearch = findViewById(R.id.closeIcon);
        etSearch       = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(this);
        btnCloseSearch.setOnClickListener(v -> {
            etSearch.setText("");
            etSearch.clearFocus();
        });
    }

    public void setListener(@Nullable QueryListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = etSearch.getText().toString();
        toggleClose(!TextUtils.isEmpty(text));
        if (listener != null) {
            listener.onQuerySubmitted(etSearch.getText().toString());
        }
    }

    private void toggleClose(boolean enabled) {
        btnCloseSearch.setEnabled(enabled);
        btnCloseSearch.setColorFilter(enabled ?
                ContextCompat.getColor(getContext(), R.color.primaryColor) : Color.GRAY);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface QueryListener {
        void onQuerySubmitted(@Nullable String text);
    }
}
