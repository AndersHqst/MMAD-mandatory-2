package com.hqst.imageviewer.app;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.util.ArrayList;

public class ImageActivity extends ActionBarActivity implements ImageManager.ICallback {
    
    private ViewPager mViewPager;
    private ArrayList<String> imageUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        ImageManager.getImageUrls(this, this);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return imageUrls.size();
            }

            @Override
            public Fragment getItem(int position) {
                String imageUrl = imageUrls.get(position);
                return ImageFragment.newInstance(imageUrl);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String imageUrl = imageUrls.get(position);
                String imageName =  Uri.parse(imageUrl).getLastPathSegment();
                return imageName;
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String imageUrl = imageUrls.get(position);
                String imageName =  Uri.parse(imageUrl).getLastPathSegment();
                setTitle(imageName);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void imageUrlsUpdated(ArrayList<String> imageUrls) {
        if(this.imageUrls.size() == 0){
            setTitle(Uri.parse(imageUrls.get(0)).getLastPathSegment());
        }
        this.imageUrls = imageUrls;
        this.mViewPager.getAdapter().notifyDataSetChanged();
    }
}
