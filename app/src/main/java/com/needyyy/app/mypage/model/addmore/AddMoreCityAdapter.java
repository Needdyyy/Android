package com.needyyy.app.mypage.model.addmore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.needyyy.app.R;

import java.util.ArrayList;

public class AddMoreCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<String> arrayList;
    public AddMoreCityAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Layout= LayoutInflater.from(context).inflate(R.layout.addmore,null);
        return new AddMoreCityAdapter.Myviewholder(Layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        AddMoreCityAdapter.Myviewholder myviewholder= (AddMoreCityAdapter.Myviewholder) viewHolder;
        myviewholder.setdata(i,viewHolder);

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Tv1,city;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            Tv1  = itemView.findViewById(R.id.Tv1);
            city=  itemView.findViewById(R.id.address);
        }

        @Override
        public void onClick(View v) {

        }

        public void setdata(int i, RecyclerView.ViewHolder viewHolder) {
            Tv1.setText(String.valueOf(i+1));
            city.setText(arrayList.get(i));
        }
    }

}
