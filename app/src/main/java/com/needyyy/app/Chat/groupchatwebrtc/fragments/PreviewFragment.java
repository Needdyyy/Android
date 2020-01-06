package  com.needyyy.app.Chat.groupchatwebrtc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.needyyy.app.Chat.groupchatwebrtc.utils.ResourceUtils;
import com.needyyy.app.R;

public class PreviewFragment extends Fragment {

    public static final String PREVIEW_IMAGE = "preview_image";


    public static Fragment newInstance(int imageResourceId) {
        PreviewFragment previewFragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PREVIEW_IMAGE, imageResourceId);
        previewFragment.setArguments(bundle);
        return previewFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_screen_share, container, false);
        Glide.with(this)
                .load(getArguments().getInt(PREVIEW_IMAGE))
                .into((ImageView) view.findViewById(R.id.image_preview));
        return view;
    }
}
