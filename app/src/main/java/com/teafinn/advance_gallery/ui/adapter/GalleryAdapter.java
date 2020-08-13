package com.teafinn.advance_gallery.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.teafinn.advance_gallery.R;
import com.teafinn.advance_gallery.ui.activity.TGallery;
import com.teafinn.advance_gallery.ui.fragment.GalleryFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by luca1897 on 25/06/17.
 */

public class GalleryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<String> images;

    private TGallery awCamera;
    private GalleryFragment galleryFragment;

    private int imageSelected = -1;

    public GalleryAdapter(GalleryFragment galleryFragment, List<String> images) {
        this.awCamera = (TGallery)galleryFragment.getActivity();
        mInflater = (LayoutInflater) awCamera.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.galleryFragment = galleryFragment;

        this.images = images;
        if(images.size() > 1) {
            galleryFragment.setImageSelected(Uri.parse(images.get(1)));
            imageSelected = 1;
        }
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // Gets the view and returns it.
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        LayoutInflater inflater = awCamera.getLayoutInflater();
        ImageView imageView;

        if(position == 0)
        {
            vi = inflater.inflate(R.layout.gallery_item, null);
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    galleryFragment.startIntentGallery();
                }
            });
            return vi;
        }

        vi = inflater.inflate(R.layout.single_grid_item, null);

        imageView =  vi.findViewById(R.id.imageView);

        // if it's not recycled, initialize some attributes
        Display display = awCamera.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated

        Resources r = awCamera.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics());

        imageView.setLayoutParams(new RelativeLayout.LayoutParams(px, px));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.get().load(images.get(position)).fit().centerCrop()
                .into(imageView);


        if(imageSelected == position)
            imageView.setAlpha(0.4f);
        else
            imageView.setAlpha(1f);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelected = position;
                notifyDataSetChanged();
                galleryFragment.setImageSelected(Uri.parse(images.get(position)));

            }
        });
        return vi;
    }

    public int getImageSelected() {
        return imageSelected;
    }

    public void setIndexImageSelected(int imageSelected) {
        this.imageSelected = imageSelected;
    }
}
