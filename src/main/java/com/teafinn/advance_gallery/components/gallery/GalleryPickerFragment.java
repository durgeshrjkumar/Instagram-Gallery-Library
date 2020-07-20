package com.teafinn.advance_gallery.components.gallery;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.teafinn.advance_gallery.R;
import com.teafinn.advance_gallery.commons.models.Session;
import com.teafinn.advance_gallery.commons.modules.LoadMoreModule;
import com.teafinn.advance_gallery.commons.modules.LoadMoreModuleDelegate;

import com.teafinn.advance_gallery.model.ImagesSelected;
import com.teafinn.advance_gallery.ui.activity.TGallery;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;



/*
 * Created by Guillaume on 17/11/2016.
 */

public class GalleryPickerFragment extends Fragment implements GridAdapterListener, LoadMoreModuleDelegate {


    RecyclerView mGalleryRecyclerView;

    ImageView mPreview;

    AppBarLayout mAppBarContainer;
    private final int RESULT_LOAD_IMAGE_FROM_GALLERY = 2;

    private static final int PREVIEW_SIZE = 700;
    private static final int MARGING_GRID = 2;
    private static final int RANGE = 50;

    private Session mSession = Session.getInstance();
    private LoadMoreModule mLoadMoreModule = new LoadMoreModule();
    private GridAdapter mGridAdapter;

    private boolean selectMultiple = false;
    private int mOffset;
    private boolean isFirstLoad = true;
    private ArrayList<ImagesSelected> images = new ArrayList<>();
    final private ArrayList<Uri> imagesSelected = new ArrayList<>();
    public static GalleryPickerFragment newInstance() {
        return new GalleryPickerFragment();
    }

    private void initViews() {
        if (isFirstLoad) {
            mGridAdapter = new GridAdapter(getContext());

        }

        mGridAdapter.setListener(this);
        mGalleryRecyclerView.setAdapter( mGridAdapter);
        mGalleryRecyclerView.setHasFixedSize(false);
        mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mGalleryRecyclerView.addItemDecoration(addItemDecoration());
        mLoadMoreModule.LoadMoreUtils(mGalleryRecyclerView, this, getContext());
        mOffset = 0;
//        fetchMedia();
        Log.i(TAG, "initViews: "+mOffset);
        if(isFirstLoad){
            setImagesOnAdapter();
        }

    }

    private RecyclerView.ItemDecoration addItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                outRect.left = MARGING_GRID;
                outRect.right = MARGING_GRID;
                outRect.bottom = MARGING_GRID;
                if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) <= 3) {
                    outRect.top = MARGING_GRID;
                }
            }
        };
    }
    public void setImagesOnAdapter()
    {
        images.clear();

//        images.add(new ImagesSelected(null,false));
        images.addAll(getCameraImages());

//            galleryAdapter.setIndexImageSelected(1);
//            setImageSelected(Uri.parse(images.get(1)));
            displayPreview(images.get(0));
//           List items= getRange();
            mGridAdapter.setItems(images);

        isFirstLoad=false;
//        galleryAdapter.notifyDataSetChanged();
//        getAllShownImagesPath();
    }

    /**
     * find last picture from gallery
     * @return
     */
    public List<ImagesSelected> getCameraImages() {
        List<ImagesSelected> imgs = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = getActivity().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        int i = 0;


            while (cursor.moveToNext()){
                final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                imgs.add(new ImagesSelected(Uri.parse("file:" + cursor.getString(dataColumn)),false));
                i++;
            }

        Log.i(TAG, "getCameraImages: "+imgs.size());

        return imgs;
    }
    public  ArrayList<String> getAllShownImagesPath() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = getActivity().getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        Log.i(TAG, "getAllShownImagesPath: "+listOfAllImages.size());
        return listOfAllImages;
    }






    private void displayPreview(ImagesSelected file) {
//        uri=Uri.fromFile(file);
        Uri uri=file.getUri();
        Log.i(TAG, "onCheckedChange: "+uri);

if(!selectMultiple){
    try{
        imagesSelected.set(0,uri);

    }catch (Exception e){
        imagesSelected.add(uri);

    }
    Log.i(TAG, "displayPreview: imagesSelected"+imagesSelected.size());
}




        Picasso.get()
                .load(file.getUri())
                .noFade()
                .noPlaceholder()
                .resize(PREVIEW_SIZE, PREVIEW_SIZE)
                .centerCrop()
                .into(mPreview);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_picker_view, container, false);
         mGalleryRecyclerView=v.findViewById(R.id.mGalleryRecyclerView);
        mPreview=v.findViewById(R.id.mPreview);
//        mPreview.setOnTouchListener(new Touch());
        mAppBarContainer=v.findViewById(R.id.mAppBarContainer);
        selectMultiple= TGallery.selectMultiple();
        Log.i(TAG, "onCreateView: selectMultiple "+selectMultiple);
        initViews();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.get().cancelRequest(mPreview);
    }

    @Override
    public void onClickMediaItem(ImagesSelected file,int position) {
//        if(position==0){
//startIntentGallery();
//            Toast.makeText(getContext(), "Open gallery", Toast.LENGTH_SHORT).show();
//        }else {
//
//        }

        displayPreview(file);
//            mSession.setFileToUpload(file);

        mAppBarContainer.setExpanded(true, true);
    }

    @Override
    public void onCheckedChange(ImagesSelected file, boolean isChecked) {
        if(selectMultiple){
            Uri uri=file.getUri();
            Log.i(TAG, "onCheckedChange: "+uri);
            if(isChecked){

                imagesSelected.add(uri);
                file.setSelected(true);

            }else {
                imagesSelected.remove(uri);
                file.setSelected(false);
            }

        }else {
            Uri uri=file.getUri();
            Log.i(TAG, "onCheckedChange: "+uri);
            if(isChecked){

             try{
                 imagesSelected.set(0,uri);
                 file.setSelected(true);
             }catch (Exception e){
                 imagesSelected.add(uri);
                 file.setSelected(false);
             }

            }else {
                imagesSelected.remove(uri);
            }
        }
//        mGridAdapter.notifyDataSetChanged();
        Log.i(TAG, "onCheckedChange: "+imagesSelected.size());
    }

    public void startIntentGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_LOAD_IMAGE_FROM_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE_FROM_GALLERY:
                if (data != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};


                    if (data.getData() != null) {
                        Log.i(TAG, "onActivityResult: single image");

                        Uri mImageUri = data.getData();

                        try {
                            String path = PathUtils.getPath(getContext(), mImageUri);
                            Log.i(TAG, "onActivityResult: " + path);
                            images.add(new ImagesSelected(Uri.parse(path),false));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
//                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                    mArrayUri.add(mImageUri);
//                        imagesEncodedList.add(new File(imageEncoded));

                    } else {
                        if (data.getClipData() != null) {
                            Log.i(TAG, "onActivityResult: multi image");

                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                Log.i(TAG, "onActivityResult selected images: " + mClipData.getItemCount());
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayUri.add(uri);
                                try {
                                    String path = PathUtils.getPath(getContext(), uri);
                                    Log.i(TAG, "onActivityResult: " + path);
                                    images.add(new ImagesSelected(Uri.parse(path),false));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                // Get the cursor
//                            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
//                            // Move to first row
//                            if(cursor.moveToFirst()){
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String imageEncoded  = cursor.getString(columnIndex);
//                                Log.i(TAG, "onActivityResult: multi"+imageEncoded);
//                                imagesEncodedList.add(new File(imageEncoded));
//                            }else {
//                                Log.i(TAG, "onActivityResult: no image found");
//                            }
//
//                            cursor.close();


                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
                        //
//                    displayPreview(data.getData());
//                        setImageSelected(data.getData());
//                    galleryAdapter.setIndexImageSelected(-1);
//                    galleryAdapter.notifyDataSetChanged();
                    }
                break;
            default:
                break;
        }
    }
//    Uri pathImageSelected;
    @Override
    public void shouldLoadMore()
    {
        Log.i(TAG, "shouldLoadMore: load more");
    }
//    public void setImageSelected(Uri pathImageSelected) {
//        this.pathImageSelected = pathImageSelected;
//        imagesSelected.add(pathImageSelected);
//        Picasso.get().load(pathImageSelected).fit().centerCrop().into(mPreview);
//    }
    public ArrayList<Uri> getPathImageSelected() {
//        ArrayList<Uri>uriArrayList=new ArrayList<>();
        Log.i(TAG, "getPathImageSelected11: "+imagesSelected.size());
//        int count=0;
//        while (count<imagesSelected.size()){
//            uriArrayList.add(imagesSelected.get(count).getUri());
//            count++;
//        }
        return imagesSelected;
    }
}
