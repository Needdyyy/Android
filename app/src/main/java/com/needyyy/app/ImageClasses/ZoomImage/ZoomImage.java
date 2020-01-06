package com.needyyy.app.ImageClasses.ZoomImage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Activities.MainActivity;
import com.needyyy.app.R;


public class ZoomImage extends Fragment {

private String profile;

    private ImageView imageView;
    public ZoomImage() {

    }


    public static ZoomImage newInstance(String profilepicture) {
        ZoomImage fragment = new ZoomImage();
        Bundle args = new Bundle();
        args.putString("profile", profilepicture);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof HomeActivity)
        {
            ((HomeActivity) getActivity()).manageToolbar("Images","2");
        }
        if (getArguments() != null) {
            profile = getArguments().getString("profile");
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=view.findViewById(R.id.imageView);
        Glide.with(getActivity())
                       .load(profile)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                               .placeholder(R.drawable.needyy)
                                .error(R.drawable.needyy))
                        .into(imageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zoom_image, container, false);
    }







}
