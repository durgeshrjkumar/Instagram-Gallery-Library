package com.teafinn.advance_gallery.model;

import android.net.Uri;

/**
 * Created by luca1897 on 10/07/17.
 */


public class ImagesSelected {
   Uri uri;
   boolean isSelected;

    public ImagesSelected(Uri uri, boolean isSelected) {
        this.uri = uri;
        this.isSelected = isSelected;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
