package com.teafinn.advance_gallery.components.gallery;

import com.teafinn.advance_gallery.model.ImagesSelected;

interface GridAdapterListener {

    void onClickMediaItem(ImagesSelected file, int position);
    void onCheckedChange(ImagesSelected file, boolean isChecked);

}
