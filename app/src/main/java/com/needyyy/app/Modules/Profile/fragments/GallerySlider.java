package com.needyyy.app.Modules.Profile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.needyyy.app.Modules.Profile.models.UserPicture.Datum;
import com.needyyy.app.R;
import java.util.ArrayList;

public class GallerySlider extends Fragment {
    private ViewPager viewpagerimage;
    private int NUM_PAGES=0;
    private int currentPage;
    private ArrayList<Datum> getUserPicturesArrayList;

    public GallerySlider() {
    }

    public static GallerySlider newInstance(ArrayList<Datum> getUserPicturesArrayList,int i)
    {
        GallerySlider fragment=new GallerySlider();
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",getUserPicturesArrayList);
        bundle.putInt("currentpage",i);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_slider, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null)
        {
            getUserPicturesArrayList= (ArrayList<Datum>) getArguments().getSerializable("data");
            currentPage=getArguments().getInt("currentpage");
        }
        viewpagerimage=view.findViewById(R.id.viewpagerimage);
        init();
    }

    private void init() {
        viewpagerimage.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
      //  viewpagerimage.setPadding(110, 0, 70, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        //mPager.setPageMargin(20);
        viewpagerimage.setPageTransformer(true, new ZoomOutPageTransformer());
        viewpagerimage.setAdapter(new ImageSlider(getUserPicturesArrayList,getContext()));
        NUM_PAGES = getUserPicturesArrayList.size();
        viewpagerimage.setCurrentItem(currentPage);
        viewpagerimage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }

        });
    }
}
