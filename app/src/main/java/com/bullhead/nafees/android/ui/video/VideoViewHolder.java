package com.bullhead.nafees.android.ui.video;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.base.BaseViewHolder;
import com.bullhead.nafees.android.databinding.ItemVideoBinding;
import com.bullhead.nafees.api.domain.Video;
import com.bumptech.glide.Glide;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoViewHolder extends BaseViewHolder<Video> {
    private static final String TAG = VideoViewHolder.class.getSimpleName();

    private ItemVideoBinding      binding;
    private FavoriteClickListener favoriteClickListener;
    private FavoriteCheckProvider favoriteCheckProvider;
    private Disposable            favoriteDisposable;

    private VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemVideoBinding.bind(itemView);
    }

    @NonNull
    static VideoViewHolder create(@NonNull ViewGroup parent) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false));
    }

    @Override
    public <X extends Video> void bind(@NonNull X t) {
        Glide.with(binding.imageView)
                .load(t.getThumbnail())
                .placeholder(R.drawable.ic_twotone_eco_24)
                .centerCrop()
                .into(binding.imageView);
        binding.titleTextView.setText(t.getTitle());
        binding.favoriteButton.setOnClickListener(v -> {
            if (favoriteClickListener != null) {
                favoriteClickListener.toggleFavorite(t, getAdapterPosition());
            }
        });
        if (favoriteDisposable != null) {
            favoriteDisposable.dispose();
        }
        if (favoriteCheckProvider != null) {
            favoriteDisposable = favoriteCheckProvider.isFavorite(t)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(favorite -> {
                        if (binding != null) {
                            binding.favoriteButton
                                    .setImageResource(favorite ?
                                            R.drawable.ic_round_favorite_24 :
                                            R.drawable.ic_round_favorite_border_24);
                        }
                    }, error -> {
                        Log.e(TAG, "bind: Failed to get " +
                                "favorite status because " + error.getLocalizedMessage());
                    });
        }
    }

    public void setFavoriteCheckProvider(FavoriteCheckProvider favoriteCheckProvider) {
        this.favoriteCheckProvider = favoriteCheckProvider;
    }

    public void setFavoriteClickListener(@Nullable FavoriteClickListener favoriteClickListener) {
        this.favoriteClickListener = favoriteClickListener;
    }

    public interface FavoriteClickListener {
        void toggleFavorite(@NonNull Video video, int pos);
    }

    public interface FavoriteCheckProvider {
        Single<Boolean> isFavorite(@NonNull Video video);
    }
}
