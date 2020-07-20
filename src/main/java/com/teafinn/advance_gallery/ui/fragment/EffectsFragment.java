package com.teafinn.advance_gallery.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teafinn.advance_gallery.GlobalEventBus;
import com.teafinn.advance_gallery.R;
import com.teafinn.advance_gallery.components.gallery.PathUtils;

import com.teafinn.advance_gallery.model.ThumbnailItem;
import com.teafinn.advance_gallery.ui.activity.TGallery;
import com.teafinn.advance_gallery.ui.adapter.FilterPhotoViewAdapter;
import com.teafinn.advance_gallery.ui.adapter.FiltersAdapter;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;


/**
 * Created by luca1897 on 23/06/17.
 */

public class EffectsFragment extends Fragment {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private static final String ARG_SECTION_NUMBER = "section_number";
//    private PhotoView mImageView;
    private RecyclerView mRecyclerView,recyclerViewPhotoView;
    private FiltersAdapter filtersAdapter;
    private FilterPhotoViewAdapter filterPhotoViewAdapter;
    private ArrayList<ThumbnailItem> images = new ArrayList<>();
    private ArrayList<ThumbnailItem> imagesSelected = new ArrayList<>();
    private TGallery awCamera;
    ProgressBar progressBar;
    private Bitmap originaBitmap;

    public EffectsFragment() {
    }

    public static EffectsFragment newInstance(int sectionNumber) {
        EffectsFragment fragment = new EffectsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_effects, container, false);
        awCamera = (TGallery)getActivity();
        progressBar=rootView.findViewById(R.id.progressBar);
//        mImageView =  rootView.findViewById(R.id.image_view);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerViewPhotoView = rootView.findViewById(R.id.recyclerViewPhotoView);



        filtersAdapter = new FiltersAdapter(this,images);
        filterPhotoViewAdapter = new FilterPhotoViewAdapter(this,imagesSelected);


        LinearLayoutManager llFilters = new LinearLayoutManager(rootView.getContext());
        llFilters.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(llFilters);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(filtersAdapter);


        LinearLayoutManager llFilters1 = new LinearLayoutManager(rootView.getContext());
        llFilters1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPhotoView.setLayoutManager(llFilters1);
        recyclerViewPhotoView.setHasFixedSize(true);
        recyclerViewPhotoView.setAdapter(filterPhotoViewAdapter);
        recyclerViewPhotoView.addItemDecoration(new CirclePagerIndicatorDecoration());
//        recyclerViewPhotoView.add
        recyclerViewPhotoView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    Log.i(TAG, "onScrollStateChanged: position"+position);
                    setImageFilters(imagesSelected.get(position).image,"Original",position);

                }
            }
        });

        return rootView;
    }
    private int getCurrentItem(){
        return ((LinearLayoutManager)recyclerViewPhotoView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }
    private void bindDataToAdapter(final Bitmap bmp) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                ThumbnailItem t1 = new ThumbnailItem("Original",bmp,null);
                ThumbnailItem t2 = new ThumbnailItem("Star Lit", bmp,SampleFilters.getStarLitFilter());
                ThumbnailItem t3 = new ThumbnailItem("Blue Mess", bmp,SampleFilters.getBlueMessFilter());
                ThumbnailItem t4 = new ThumbnailItem("Awe Struck Vibe", bmp,SampleFilters.getAweStruckVibeFilter());
                ThumbnailItem t5 = new ThumbnailItem("Lime Stutter", bmp, SampleFilters.getLimeStutterFilter());
                ThumbnailItem t6 = new ThumbnailItem("Night Wisper", bmp,SampleFilters.getNightWhisperFilter());

                images.clear();
                images.add(t1); // Original Image
                images.add(t2);
                images.add(t3);
                images.add(t4);
                images.add(t5);
                images.add(t6);

                filtersAdapter.notifyDataSetChanged();

            }
        };
        handler.post(r);
    }
    public void addImageToRecycleView(Bitmap bmp) {
        ThumbnailItem t1 = new ThumbnailItem("Original",bmp,null);

            imagesSelected.add(t1);




//        mImageView.setImageBitmap(bmp);
    }
    public void setImage(Bitmap bmp,String name) {
        ThumbnailItem t1 = new ThumbnailItem(name,bmp,null);
        try{
//            if(imagesSelected.size()>0){
            imagesSelected.set(getCurrentItem(),t1);

//            }else {
//                imagesSelected.add(selectedIndex,t1);
//            }
        }catch (Exception e){
            e.printStackTrace();
//            imagesSelected.add(t1);
        }

GlobalEventBus.getBus().post(true);

        filterPhotoViewAdapter.notifyDataSetChanged();

    }
    public void setImageFilters(Bitmap bmp,String name,int position) {
        Log.i(TAG, "setImageFilters: image position"+position);
        ThumbnailItem t1 = new ThumbnailItem(name,bmp,null);
        try{
//            if(imagesSelected.size()>0){
            imagesSelected.set(position,t1);
            bindDataToAdapter(bmp);
//            }else {
//                imagesSelected.add(selectedIndex,t1);
//            }
        }catch (Exception e){
            e.printStackTrace();
//            imagesSelected.add(t1);
        }



        filterPhotoViewAdapter.notifyDataSetChanged();
//        mImageView.setImageBitmap(bmp);
    }
//    public void setImage(Bitmap bmp,int position) {
//        ThumbnailItem t1 = new ThumbnailItem("Original",bmp,null);
//        try{
////            if(imagesSelected.size()>0){
//            imagesSelected.set(position,t1);
////            }else {
////                imagesSelected.add(selectedIndex,t1);
////            }
//        }catch (Exception e){
//            e.printStackTrace();
////            imagesSelected.add(t1);
//        }
//
//
//
//        filterPhotoViewAdapter.notifyDataSetChanged();
////        mImageView.setImageBitmap(bmp);
//    }

    public ArrayList<File> saveImage()
    {
//        ArrayList<Uri>list=awCamera.getImageSelected();
        ArrayList<File>listFiles=new ArrayList<>();
        int count=0;
        while (count<imagesSelected.size()){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date now = new Date();
            String fileName = "IMG_"+count + formatter.format(now) + ".png";

            final File file = new File(
                    awCamera.getGalleryPath(),
                    fileName
            );
            Log.i(TAG, "saveImage: "+file.getAbsolutePath());

//            Bitmap bmp = BitmapFactory.decodeFile(Uri.parse(path).getPath(),opts);
           Bitmap bmp=imagesSelected.get(count).image;
            Filter fSel = imagesSelected.get(count).filter;

            Log.i(TAG, "saveImage: list.get(count)"+imagesSelected.get(count).name);
            Log.i(TAG, "saveImage: selectedFilter"+filterPhotoViewAdapter.getFilterSelected());
            if(fSel != null)
            {
                bmp = fSel.processFilter(bmp);
            }
            saveBmpToFile(file,bmp);
            listFiles.add(file);
            count++;
        }


        return listFiles;
    }
    public void applyFilter(){
        ApplyFilter applyFilter=new ApplyFilter();
        applyFilter.execute();
    }
public class ApplyFilter extends AsyncTask<Void, Void, ArrayList<File>> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {

        return saveImage();
    }

    @Override
    protected void onPostExecute(ArrayList<File> file) {
        super.onPostExecute(file);
        progressBar.setVisibility(View.GONE);

        GlobalEventBus.getBus().post(file);
    }
}
    private void saveBmpToFile(File file, Bitmap bmp)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
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

    public void initUI() {
        ArrayList<Uri>list=awCamera.getImageSelected();
//        ArrayList<File>listFiles=new ArrayList<>();
        int count=0;
        while (count<list.size()){
            String path=null;
            try {
                path = PathUtils.getPath(getContext(), list.get(count));
//             path = Uri..getPath(getContext(), uri);
                Log.i(TAG, "onActivityResult Effect Fragment: " + path);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if(path != null) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inMutable = true;
                opts.inJustDecodeBounds = false;



                originaBitmap = BitmapFactory.decodeFile(path,opts);
                opts.inSampleSize = calculateInSampleSize(opts,300,300);
                originaBitmap = BitmapFactory.decodeFile(path,opts);


                try {
                    ExifInterface exif = new ExifInterface(path);
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                        originaBitmap = Bitmap.createBitmap(originaBitmap,0,0,opts.outWidth,opts.outHeight,matrix,true);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                addImageToRecycleView(originaBitmap);
                if(count==0){
                    bindDataToAdapter(originaBitmap);
                }

            }
            count++;
        }

filterPhotoViewAdapter.notifyDataSetChanged();

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}