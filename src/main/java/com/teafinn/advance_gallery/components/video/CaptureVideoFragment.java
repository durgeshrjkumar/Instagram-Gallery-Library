package com.teafinn.advance_gallery.components.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.teafinn.advance_gallery.R;


public class CaptureVideoFragment extends Fragment {

    public static CaptureVideoFragment newInstance() {
        return new CaptureVideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.capture_video_view, container, false);
    }

}
