package com.teafinn.advance_gallery.ui.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.teafinn.advance_gallery.ui.activity.AwCamera;
import com.teafinn.advance_gallery.ui.fragment.EffectsFragment;
import com.teafinn.advance_gallery.ui.fragment.GalleryFragment;
import com.teafinn.advance_gallery.ui.fragment.PhotoFragment;

import java.util.ArrayList;

/**
 * Created by luca1897 on 23/06/17.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    public GalleryFragment mGalleryFragment;
    public PhotoFragment mPhotoFragment;
    public EffectsFragment mEffectsFragment;
//    public VideoFragment mVideoFragment;
    private ArrayList<AwCamera.PAGE> listTabs;

    public HomePagerAdapter(FragmentManager fm, ArrayList<AwCamera.PAGE> listTabs) {

        super(fm);
        this.listTabs = listTabs;
        if(mGalleryFragment ==null)
            mGalleryFragment = GalleryFragment.newInstance(0);
        if(mPhotoFragment ==null)
            mPhotoFragment = PhotoFragment.newInstance(1);
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
                    mGalleryFragment = GalleryFragment.newInstance(0);
                return mGalleryFragment;
            case Photo:
                if(mPhotoFragment==null)
                    mPhotoFragment = PhotoFragment.newInstance(1);
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
