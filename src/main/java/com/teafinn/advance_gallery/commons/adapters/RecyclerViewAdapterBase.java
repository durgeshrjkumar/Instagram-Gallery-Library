package com.teafinn.advance_gallery.commons.adapters;


import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewAdapterBase<String, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<String> mItems = new ArrayList<>();

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    public void setItems(List<String> items) {
        if (items != null) {
            mItems = items;
            notifyDataSetChanged();
        }
    }

    public void addItems(List<String> items, int position) {

        if (items != null) {
//        removeItems(items);
            mItems.addAll(position, items);


//            notifyItemRangeInserted(position, items.size());
        }
    }

    public void addItem(String item, int position) {
        if (item != null) {
            mItems.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void removeItem(String item) {
        if (item != null) {
            int position = mItems.indexOf(item);
            mItems.remove(item);
            notifyItemRemoved(position);
        }
    }

    public void removeItems(List<String> items) {
        if (items != null) {
            mItems.removeAll(items);
//            notifyItemRangeRemoved(mItems.indexOf(items.get(0)), items.size());
        }
    }

    public void removeRangeItem(int firstPosition) {
        int size = mItems.size();
        List<String> removes = new ArrayList<>();
        for (int i = firstPosition; i < size; i++) {
            removes.add(mItems.get(i));
        }
        mItems.removeAll(removes);
        notifyItemRangeRemoved(firstPosition, size);
    }

    public void clearItems() {
        int size = mItems.size();
        mItems.clear();
        notifyItemRangeRemoved(0, size);
    }

    public String getItemAtPosition(int position) {
        return mItems.get(position);
    }


}
