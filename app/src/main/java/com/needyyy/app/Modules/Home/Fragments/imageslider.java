package com.needyyy.app.Modules.Home.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.needyyy.AppController;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.ImageClasses.ZoomImage.ZoomImage;
import com.needyyy.app.Modules.Home.Activities.ExoPlayerActivity;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.Modules.Home.Activities.VideoPlayerConfig;
import com.needyyy.app.Modules.Home.modle.CommentBase;
import com.needyyy.app.Modules.Home.modle.PostResponse;
import com.needyyy.app.vedio.Vedio;
import com.needyyy.app.webutils.WebInterface;
import com.squareup.picasso.Picasso;
import com.needyyy.app.R;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;

/**
 * Created by Admin on 16-07-2019.
 */

class imageslider extends PagerAdapter {
    //private ArrayList<Products> product;
    private LayoutInflater inflater;
    private PostResponse postResponse;
   // private ArrayList<Stocks> stocks=new ArrayList<>();
    private Context context;
    public imageslider(Context context,PostResponse postResponse) {
        this.postResponse=postResponse;
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return postResponse.getPostMeta().size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.imageslider, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
                 Glide.with(context)
                .load(postResponse.getPostMeta().get(position).getLink().toString())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                         .thumbnail(Glide.with(context).load(postResponse.getPostMeta().get(position).getLink()))
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof HomeActivity) {
                    if (postResponse.getPostMeta().get(position).getFileType().equals("image")) {
                        ((HomeActivity) context).replaceFragment(ZoomImage.newInstance(postResponse.getPostMeta().get(position).getLink().toString()), true);
                    }
                    else if (postResponse.getPostMeta().get(position).getFileType().equals("youtube"))
                    {
                        Intent intent=new Intent(context,Vedio.class);
                        intent.putExtra("vedio_id",postResponse.getPostMeta().get(position).getFilelink());
                        context.startActivity(intent);
                        hitapi(postResponse.getPostMeta().get(position).getId());
                        postResponse.getPostMeta().get(position).setViews(String.valueOf(Integer.parseInt(postResponse.getPostMeta().get(position).getViews())+1));
                    }
                    else if(postResponse.getPostMeta().get(position).getFileType().equals("video"))
                    {
                        Intent mIntent = ExoPlayerActivity.getStartIntent(context,postResponse.getPostMeta().get(position).getLink() );
                        context.startActivity(mIntent);
                        hitapi(postResponse.getPostMeta().get(position).getId());
                        postResponse.getPostMeta().get(position).setViews(String.valueOf(Integer.parseInt(postResponse.getPostMeta().get(position).getViews())+1));
                    }
                }
            }
        });

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    private void hitapi(String id) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<CommonPojo> call = Service.VideoCount(id);
        call.enqueue(new Callback<CommonPojo>() {
            @Override
            public void onResponse(Call<CommonPojo> call, Response<CommonPojo> response) {
                Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                CommonPojo commonPojo = response.body();
                if (commonPojo.getStatus()) {

                   // Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonPojo> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
