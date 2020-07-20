package com.teafinn.advance_gallery.components.gallery;

/*
 * Created by Guillaume on 17/11/2016.
 */

import android.content.Context;
import android.view.ViewGroup;

import com.teafinn.advance_gallery.commons.adapters.RecyclerViewAdapterBase;
import com.teafinn.advance_gallery.commons.adapters.ViewWrapper;
import com.teafinn.advance_gallery.model.ImagesSelected;

import java.lang.ref.WeakReference;

class GridAdapter extends RecyclerViewAdapterBase<ImagesSelected, MediaItemView> implements MediaItemViewListener {

    private final Context context;

    GridAdapter(Context context) {
        this.context = context;
    }

    private WeakReference<GridAdapterListener> mWrListener;

    void setListener(GridAdapterListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    @Override
    protected MediaItemView onCreateItemView(ViewGroup parent, int viewType) {
        return new MediaItemView(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<MediaItemView> viewHolder, final int position) {

            MediaItemView itemView = viewHolder.getView();
            itemView.setListener(this);
            itemView.bind(mItems.get(position));
            itemView.setPosition(position);



    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onClickItem(ImagesSelected file,int position) {
        mWrListener.get().onClickMediaItem(file,position);
    }

    @Override
    public void onCheckedChange(ImagesSelected file, boolean isChecked) {
        mWrListener.get().onCheckedChange(file,isChecked);
    }
}