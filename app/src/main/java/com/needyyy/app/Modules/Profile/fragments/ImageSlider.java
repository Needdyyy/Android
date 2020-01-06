package com.needyyy.app.Modules.Profile.fragments;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.needyyy.app.Modules.Profile.models.UserPicture.Datum;
import com.needyyy.app.R;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSlider extends PagerAdapter {
    private ArrayList<Datum> getUserPicturesArrayList;
    private LayoutInflater inflater;
    private Context context;
    public ImageSlider(ArrayList<Datum> getUserPicturesArrayList, Context context) {
        this.getUserPicturesArrayList=getUserPicturesArrayList;
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return getUserPicturesArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.fragment_image_slider, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
            Glide.with(context).load(getUserPicturesArrayList.get(position).getLink())
                    .into(imageView);

        view.addView(imageLayout,0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
