package com.teafinn.advance_gallery.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bm.library.PhotoView;
import com.teafinn.advance_gallery.R;
import com.teafinn.advance_gallery.model.ThumbnailItem;
import com.teafinn.advance_gallery.ui.activity.TGallery;
import com.teafinn.advance_gallery.ui.fragment.EffectsFragment;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

/**
 * Created by luca1897 on 25/06/17.
 */

public class FilterPhotoViewAdapter extends RecyclerView.Adapter<FilterPhotoViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    private int selected = 0;
    private List<ThumbnailItem> images;

    private TGallery awCamera;
    private ViewGroup parent;
    private EffectsFragment effectsFragment;


    public FilterPhotoViewAdapter(EffectsFragment effectsFragment, List<ThumbnailItem> images) {
        this.awCamera = (TGallery)effectsFragment.getActivity();
        this.effectsFragment = effectsFragment;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custum_photoview, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.filterName.setText(images.get(position).name);
        holder.image_view.setImageBitmap(images.get(position).image);


//        if(selected == position)
//        {
//            holder.filterName.setTextColor(Color.BLACK);
//        }else{
//            holder.filterName.setTextColor(Color.GRAY);
//        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public PhotoView image_view;

        public ViewHolder(final View v) {
            super(v);
            image_view =  v.findViewById(R.id.image_view);

            view = v;
        }
    }

    public Filter getFilterSelected() {
        return images.get(selected).filter;
    }
}
