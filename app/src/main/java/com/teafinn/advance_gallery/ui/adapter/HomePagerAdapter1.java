package com.teafinn.advance_gallery.ui.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.teafinn.advance_gallery.components.gallery.GalleryPickerFragment;
import com.teafinn.advance_gallery.components.photo.CapturePhotoFragment;
import com.teafinn.advance_gallery.ui.activity.TGallery;
import com.teafinn.advance_gallery.ui.fragment.EffectsFragment;

import java.util.ArrayList;

/**
 * Created by luca1897 on 23/06/17.
 */

public class HomePagerAdapter1 extends FragmentPagerAdapter {

    public GalleryPickerFragment mGalleryFragment;
    public CapturePhotoFragment mPhotoFragment;
    public EffectsFragment mEffectsFragment;
//    public VideoFragment mVideoFragment;
    private ArrayList<TGallery.PAGE> listTabs;

    public HomePagerAdapter1(FragmentManager fm, ArrayList<TGallery.PAGE> listTabs) {

        super(fm);
        this.listTabs = listTabs;
        if(mGalleryFragment ==null)
            mGalleryFragment = GalleryPickerFragment.newInstance();
        if(mPhotoFragment ==null)

            mPhotoFragment =  CapturePhotoFragment.newInstance(3);;
        if(mEffectsFragment == null)
            mEffectsFragment = EffectsFragment.newInstance(2);
    }


    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(listTabs.get(position))
        {
            case Gallery:
                if(mGalleryFragment ==null)
                    mGalleryFragment = GalleryPickerFragment.newInstance();
                return mGalleryFragment;
            case Photo:
                if(mPhotoFragment==null)
                    mPhotoFragment =   CapturePhotoFragment.newInstance(3);;
                return mPhotoFragment;
            case Effects:
                if(mEffectsFragment==null)
                    mEffectsFragment = EffectsFragment.newInstance(2);
                return mEffectsFragment;
//            case Video:
//                if(mVideoFragment==null)
//                    mVideoFragment = VideoFragment.newInstance(2);
//                return mVideoFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return listTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTabs.get(position).toString();
    }


}
