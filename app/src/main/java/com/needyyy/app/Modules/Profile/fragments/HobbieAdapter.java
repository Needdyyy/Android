package com.needyyy.app.Modules.Profile.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needyyy.app.R;

import java.util.List;

import androidx.annotation.NonNull;

public class HobbieAdapter extends RecyclerView.Adapter<HobbieAdapter.MyViewHolder> {

    private List<String> arraylist;

    public HobbieAdapter(List<String> arraylist) {
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hobbieadapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String text = arraylist.get(position);
        holder.textView.setText(text);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arraylist.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_search);
            imageView = itemView.findViewById(R.id.deletehob);

        }
    }
}


