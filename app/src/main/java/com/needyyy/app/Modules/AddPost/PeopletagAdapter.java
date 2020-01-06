package com.needyyy.app.Modules.AddPost;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.R;

import java.util.ArrayList;

public class PeopletagAdapter extends RecyclerView.Adapter<PeopletagAdapter.PeopleTagHolder> {
    Activity activity;
    ArrayList<People> showPeopleArrayList;
    int userType;

    public PeopletagAdapter(Activity activity, ArrayList<People> arrayList, int i) {
        this.activity = activity;
        this.showPeopleArrayList = arrayList;
        this.userType = i;
    }

    @NonNull
    @Override
    public PeopleTagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_tag_list, parent, false);
//
        return new PeopleTagHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleTagHolder holder, int position) {
        PeopleTagHolder tagHolder = holder;
        tagHolder.setUsers(showPeopleArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return showPeopleArrayList.size();
    }


    // TODO PEOPLE TAGLIST ADAPTER
    public class PeopleTagHolder extends RecyclerView.ViewHolder {
        AppCompatCheckedTextView NameTV;
        ImageView ImageIV;
        ImageView ImageIVText;
        People people;

        public PeopleTagHolder(final View view) {
            super(view);

            NameTV = (AppCompatCheckedTextView) view.findViewById(R.id.nameTV);
            ImageIV = (ImageView) view.findViewById(R.id.imageIV);
            ImageIVText = (ImageView) view.findViewById(R.id.imageIVText);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        if (showPeopleArrayList.get(getAdapterPosition()).getCategory() == 3) {
//                            showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
//                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()));
//                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
//                            NameTV.setCheckMarkDrawable(R.drawable.check_on);
//                        } else if (showPeopleArrayList.get(getAdapterPosition()).getCategory() == 2) {
//                            showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
//                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()));
//                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
//                            NameTV.setCheckMarkDrawable(R.drawable.check_on);
//                        } else if (showPeopleArrayList.get(getAdapterPosition()).getCategory() == 1) {
//                            showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
//                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()));
//                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
//                            NameTV.setCheckMarkDrawable(R.drawable.check_on);
//                        }
                    if (!showPeopleArrayList.get(getAdapterPosition()).isTagged()) {
                        showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
//                        ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()), showPeopleArrayList.get(getAdapterPosition()).isTagged());
//                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                        NameTV.setCheckMarkDrawable(R.drawable.check_on);
                    } else {
                        showPeopleArrayList.get(getAdapterPosition()).setTagged(false);
//                        ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()), showPeopleArrayList.get(getAdapterPosition()).isTagged());
//                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                        NameTV.setCheckMarkDrawable(R.drawable.check_off);
                    }

                }
            });
        }

        public void setUsers(People people) {
            this.people = people;
            people.setName((people.getName()).toUpperCase());
            NameTV.setText(people.getName());

            if (!TextUtils.isEmpty(people.getProfilePicture())) {
                ImageIV.setVisibility(View.VISIBLE);
                ImageIVText.setVisibility(View.GONE);
//                Ion.with(ImageIV.getContext()).load(people.getProfile_picture())
//                        .asBitmap()
//                        .setCallback(new FutureCallback<Bitmap>() {
//                            @Override
//                            public void onCompleted(Exception e, Bitmap result) {
//                                if (result != null)
//                                    ImageIV.setImageBitmap(result);
//                                else
//                                    ImageIV.setImageResource(R.mipmap.default_pic);
//                            }
//                        });
            } else {
                ImageIV.setVisibility(View.GONE);
                ImageIVText.setVisibility(View.VISIBLE);
//                ImageIVText.setImageDrawable(Helper.GetDrawable(people.getName(), TagSelection.this, people.getId()));
            }

            if (people.isTagged()) {
                NameTV.setCheckMarkDrawable(R.drawable.check_on);
            } else {
                NameTV.setCheckMarkDrawable(R.drawable.check_off);
            }
        }
    }
}