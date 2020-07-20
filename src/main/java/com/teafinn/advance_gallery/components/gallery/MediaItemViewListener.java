package com.teafinn.advance_gallery.components.gallery;

import com.teafinn.advance_gallery.model.ImagesSelected;

interface MediaItemViewListener {
    void onClickItem(ImagesSelected file, int position);
    void onCheckedChange(ImagesSelected file, boolean isChecked);

}
