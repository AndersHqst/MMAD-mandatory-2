package com.hqst.imageviewer.app;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.UUID;

/**
 * Created by ahkj on 04/05/14.
 */
public class ImageFragment extends Fragment {
    private static final String IMAGE_URL = "IMAGE_URL";
    private String imageUrl;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.imageUrl = (String)getArguments().getSerializable(IMAGE_URL);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save stuff?
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        mImageView = (ImageView)view.findViewById(R.id.imageView);
        ImageManager.getImage(this.imageUrl, this.mImageView, getActivity());
        return view;
    }


    public static ImageFragment newInstance(String imageUrl){
        Bundle args = new Bundle();
        args.putSerializable(IMAGE_URL, imageUrl);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
