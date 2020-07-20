package com.teafinn.advance_gallery.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.bm.library.PhotoView;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.teafinn.advance_gallery.R;
import com.teafinn.advance_gallery.ui.activity.AwCamera;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by luca1897 on 23/06/17.
 */

public class PhotoFragment extends Fragment {

    private AwCamera awCamera;


    private CameraKitView mCameraView;
    private ImageButton mTakePhoto;
    private ImageButton mSwitchCamera;
    private ImageButton mToggleFlash;
    private PhotoView mImageView;

    private Uri pathImage = null;

    private boolean photoTaken = false;


    private static final String ARG_SECTION_NUMBER = "section_number";

    public PhotoFragment() {
    }

    public static PhotoFragment newInstance(int sectionNumber) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUserVisibleHint(false);
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

        mCameraView = rootView.findViewById(R.id.camera);
        mTakePhoto = rootView.findViewById(R.id.btn_take_photo);
        mSwitchCamera =  rootView.findViewById(R.id.btn_switch_camera);
        mToggleFlash =  rootView.findViewById(R.id.btn_toggle_flash);
        mImageView = rootView.findViewById(R.id.image_view);
        mImageView.enable();

        awCamera = (AwCamera)getActivity();

        initCamera();
        return rootView;
    }
String TAG="PHOTOFRAGMENT";
    public void initCamera()
    {
        mCameraView.setCameraListener(new CameraKitView.CameraListener() {
            @Override
            public void onOpened() {
                Log.i(TAG, "onOpened: camera opened");
//                startCamera();

            }

            @Override
            public void onClosed() {
stopCamera();
            }

        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!photoTaken) {
                    mCameraView.captureImage(new CameraKitView.ImageCallback() {
                        @Override
                        public void onImage(CameraKitView cameraKitView, byte[] picture) {
                            photoTaken = true;
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                            Date now = new Date();
                            String fileName = "IMG_" + formatter.format(now) + ".png";

                            final File file = new File(
                                    awCamera.getGalleryPath(),
                                    fileName
                            );

                            Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);

                            saveBitmapToPng(result,file);

                            pathImage = Uri.fromFile(file);
                            Picasso.get().load(file).fit().centerCrop()
                                    .into(mImageView);
                            mCameraView.setVisibility(View.INVISIBLE);
                            mSwitchCamera.setVisibility(View.GONE);
                            mToggleFlash.setVisibility(View.GONE);
                            awCamera.setContinueButtonVisibility(View.VISIBLE);
                            mImageView.setVisibility(View.VISIBLE);
                            mTakePhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_black));

                        }
                    });
                }
                else{
                    mTakePhoto.setImageDrawable(null);
                    photoTaken = false;
                    mCameraView.setVisibility(View.VISIBLE);
                    mSwitchCamera.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.GONE);
                    mToggleFlash.setVisibility(View.VISIBLE);
                    awCamera.setContinueButtonVisibility(View.GONE);
                }
            }
        });

        mToggleFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlash();
            }
        });

        mSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        mCameraView.setFlash(AwCamera.getDefaultFlashMode());
        setFlashIcon(AwCamera.getDefaultFlashMode());

        mCameraView.setFacing(AwCamera.getDefaultCameraMode());
        setCameraModeIcon(AwCamera.getDefaultCameraMode());
        mCameraView.setCameraListener(new CameraKitView.CameraListener() {
            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
stopCamera();
            }
        });
    }

    private void saveBitmapToPng(Bitmap bmp, File file)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void switchCamera() {
        if(mCameraView != null)
        {
     mCameraView.toggleFacing();

            setCameraModeIcon(mCameraView.getFacing());
        }
    }

    private void toggleFlash()
    {
        if(mCameraView != null)
        {
            int f = mCameraView.getFlash();
            if(f==0){
                mCameraView.setFlash(CameraKit.FLASH_ON);
                setFlashIcon(CameraKit.FLASH_ON);
            }else {
                mCameraView.setFlash(CameraKit.FLASH_OFF);
                setFlashIcon(CameraKit.FLASH_OFF);
            }



        }
    }

    private void setCameraModeIcon(int mode)
    {
        if(mode == CameraKit.FACING_BACK)
        {
            mSwitchCamera.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_camera_rear_white));
        }else{
            mSwitchCamera.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_camera_front_white));
        }
    }

    private void setFlashIcon(int mode)
    {
        if(mode == CameraKit.FLASH_OFF)
        {
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_off_white));
        }else if(mode == CameraKit.FLASH_ON)
        {
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on_white));
        }else if(mode == CameraKit.FLASH_AUTO)
        {
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_auto_white));
        }else{
            mToggleFlash.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on_white));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
        else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCamera();
    }

    public void setCameraViewVisibility(int visible) {
        if(mCameraView != null)
            mCameraView.setVisibility(visible);
    }


    public Uri getPathImage() {
        return pathImage;
    }

    public void startCamera()
    {
        if(mCameraView != null)
        {
//            AwCamera.setDefaultCameraMode(0);
            mCameraView.onStart();
            Log.i(TAG, "startCamera: camera started"+mCameraView.getPermissions());
   }
    }

    public void stopCamera()
    {
        if(mCameraView != null)
        {
            mCameraView.onStop();
         mCameraView=null;


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mCameraView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
