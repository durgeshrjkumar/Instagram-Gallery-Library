package com.teafinn.advance_gallery.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.teafinn.advance_gallery.GlobalEventBus;
import com.teafinn.advance_gallery.R;
import com.teafinn.advance_gallery.ui.adapter.HomePagerAdapter1;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

public class TGallery extends AppCompatActivity {
public static boolean selectMultiple=false;

    private HomePagerAdapter1 mPagerAdapter;

    public final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 0;
    public final int REQUEST_PERMISSION_CAMERA = 16;

    private final String galleryPath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM;

    private PAGE oldPage = PAGE.Gallery;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView mToolbarTitle;
    private ImageButton mContinue;
    private ImageButton mBack;

    public enum PAGE {
        Gallery,
        Photo,
        Effects
    }

    private static boolean PHOTO_ENABLED = true;
    private static boolean GALLERY_ENABLED = true;
    private static boolean EFFECTS_ENABLED = true;


    private static int DEFAULT_FLASH_MODE = 0;
    private static int DEFAULT_CAMERA_MODE = 0;

    private ArrayList<PAGE> listTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aw_camera);
selectMultiple=getIntent().getBooleanExtra("selectMultiple",false);
        listTabs = new ArrayList<>();
        if (GALLERY_ENABLED)
            listTabs.add(PAGE.Gallery);
        if (PHOTO_ENABLED)
            listTabs.add(PAGE.Photo);
        if (EFFECTS_ENABLED)
            listTabs.add(PAGE.Effects);


        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.container);
        mContinue = findViewById(R.id.toolbar_continue);
        mBack = findViewById(R.id.toolbar_back);
        mToolbarTitle = findViewById(R.id.toolbar_title);

        mPagerAdapter = new HomePagerAdapter1(getSupportFragmentManager(), listTabs);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mViewPager.setAdapter(mPagerAdapter);
        requestPermissionExternalStorage();
       
    }
    public void loadUI(){

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mToolbarTitle.setText(mPagerAdapter.getPageTitle(position));
                ArrayList<Uri> path = getImageSelected();
                if (path == null) {
                    setContinueButtonVisibility(View.GONE);
                } else {
                    setContinueButtonVisibility(View.VISIBLE);
                }

                if (position == 1) {
                    setDefaultCameraMode(1);

                } else {
                    setContinueButtonVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPage = PAGE.values()[mViewPager.getCurrentItem()];
                if (oldPage != PAGE.Effects) {
                    showEffectsFragment();
                } else {
//                    String path = "file:" + ;

                    mPagerAdapter.mEffectsFragment.applyFilter();
//                    setActivityResult(path);

                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

//    ArrayList<File> lstSelectedFiles = new ArrayList();


//    @Override
//    protected void onResume() {
//        super.onResume();
//        GlobalEventBus.getBus().register(this);
//    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalEventBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalEventBus.getBus().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(Uri profilePicUrl) {
        Log.i("AWCAMERA1", "getMessage: broadcast" + profilePicUrl);
        getImageSelected();

    }
 @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(Boolean b) {
        Log.i("AWCAMERA1", "getMessage: broadcast" + b);
        if(b){
            setContinueButtonVisibility(View.VISIBLE);
        }else {
            setContinueButtonVisibility(View.GONE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(ArrayList<File> selectedFile) {
//        ArrayList<String> stringArrayList = new ArrayList<>();
//        int size = selectedFile.size();
//        while (size-- > 0) {
//            stringArrayList.add(selectedFile.get(size).getPath());
//        }
//        Log.i("DATA", "setActivityResult: " + stringArrayList);
        Log.i("AWCAMERA1", "getMessage: selectedFile" + selectedFile);
        Intent data = new Intent();
        data.putExtra("fileList", selectedFile);
        setResult(Activity.RESULT_OK, data);
        finish();


    }

    public ArrayList<Uri> getImageSelected() {
        ArrayList<Uri> path = null;

        switch (listTabs.get(mTabLayout.getSelectedTabPosition())) {
            case Photo:
                path = mPagerAdapter.mPhotoFragment.getPathImage();
                Log.i("AWCAMERA1", "getImageSelected: " + path);
                if (path != null) {
                    setContinueButtonVisibility(View.VISIBLE);
                } else {
                    setContinueButtonVisibility(View.GONE);
                }
                break;
            case Gallery:
                path = mPagerAdapter.mGalleryFragment.getPathImageSelected();
                break;
            default:

                break;
        }

        return path;
    }

    public void setContinueButtonVisibility(final int visibility) {
        mContinue.setVisibility(visibility);
    }

    public void setActivityResult(String filename) {
        if (filename == null)
            return;
        //Toast.makeText(this,filename,Toast.LENGTH_SHORT).show();
        Log.i("DATA", "setActivityResult: " + filename);
        Intent data = new Intent();
        data.setData(Uri.parse(filename));
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == PAGE.Effects.ordinal()) {
            mViewPager.setCurrentItem(oldPage.ordinal(), false);
            mTabLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * check external storage and camera permission
     */
    public void requestPermissionExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                    },
                    REQUEST_PERMISSION_EXTERNAL_STORAGE);

        }else {
            loadUI();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//TODO get data when permission is granted
                
            } else {
                // User refused to grant permission.
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
loadUI();
            } else {
                // User refused to grant permission.
                Toast.makeText(this, "No Camera Permission provided", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showEffectsFragment() {
        mViewPager.setCurrentItem(2, false);
        mTabLayout.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mPagerAdapter.mEffectsFragment != null)
                    mPagerAdapter.mEffectsFragment.initUI();
            }
        }, 150);
    }


    public String getGalleryPath() {
        return galleryPath;
    }


    public static boolean isPhotoEnabled() {
        return PHOTO_ENABLED;
    }

    public static void setPhotoEnabled(boolean photoEnabled) {
        PHOTO_ENABLED = photoEnabled;
    }

    public static boolean isGalleryEnabled() {
        return GALLERY_ENABLED;
    }

    public static void setGalleryEnabled(boolean galleryEnabled) {
        GALLERY_ENABLED = galleryEnabled;
    }

    public static int getDefaultFlashMode() {
        return DEFAULT_FLASH_MODE;
    }

    public static void setDefaultFlashMode(int defaultFlashMode) {
        DEFAULT_FLASH_MODE = defaultFlashMode;
    }

    public static int getDefaultCameraMode() {
        return DEFAULT_CAMERA_MODE;
    }

    public static void setDefaultCameraMode(int defaultCameraMode) {
        DEFAULT_CAMERA_MODE = defaultCameraMode;
    }
    public static boolean selectMultiple() {
        return selectMultiple;
    }
}
