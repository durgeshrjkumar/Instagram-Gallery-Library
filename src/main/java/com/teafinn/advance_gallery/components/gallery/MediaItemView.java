package com.teafinn.advance_gallery.components.gallery;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.teafinn.advance_gallery.R;
import com.teafinn.advance_gallery.commons.modules.ReboundModule;
import com.teafinn.advance_gallery.commons.modules.ReboundModuleDelegate;

import com.teafinn.advance_gallery.model.ImagesSelected;
import com.teafinn.advance_gallery.ui.activity.TGallery;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

public class MediaItemView extends RelativeLayout implements ReboundModuleDelegate {
    Context context;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    ImageView mMediaThumb;
CheckBox cbxSelected;
    private ImagesSelected mCurrentFile;
    private ReboundModule mReboundModule = ReboundModule.getInstance(this);
    private WeakReference<MediaItemViewListener> mWrListener;

    void setListener(MediaItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    public MediaItemView(Context context) {
        super(context);
        this.context=context;
        View v = null;
//        if(position == 0)
//        {
//            v= View.inflate(context,R.layout.gallery_item, this);
////            v = inflater.inflate(R.layout.gallery_item, null);
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    galleryFragment.startIntentGallery();
//                }
//            });
//            mMediaThumb=v.findViewById(R.id.mMediaThumb);
//        }else {
            v= View.inflate(context, R.layout.media_item_view, this);
            mMediaThumb=v.findViewById(R.id.mMediaThumb);
        cbxSelected=v.findViewById(R.id.cbxSelected);
//        }

    }

    public void bind(ImagesSelected file) {


        if(mMediaThumb!=null){
            mCurrentFile = file;
            mReboundModule.init(mMediaThumb);
            if(TGallery.selectMultiple()){
                cbxSelected.setVisibility(VISIBLE);
                cbxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mWrListener.get().onCheckedChange(mCurrentFile,isChecked);
                    }
                });
            }else {
                cbxSelected.setVisibility(GONE);
            }

            Picasso.get()
                    .load(mCurrentFile.getUri())
                    .resize(350, 350)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_media)
                    .error(R.drawable.placeholder_error_media)
                    .noFade()
                    .into(mMediaThumb);

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onTouchActionUp() {


            mWrListener.get().onClickItem(mCurrentFile,position);

    }


}
