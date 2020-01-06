package com.needyyy.app.Modules.Dating.suggestions.adapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.needyyy.AppController;
import com.needyyy.app.Chat.ChatActivity;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.Dating.BlurTransformation;
import com.needyyy.app.Modules.Dating.DatingActivity;
import com.needyyy.app.Modules.Dating.suggestions.fragment.SendRequestFragment;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.utils.Constant;

import java.util.ArrayList;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.needyyy.AppController.getContext;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.modelViewHolder>{
    private Context context;
    ArrayList<People> arrfriends = new ArrayList<>();
    private String userid;
    private String username;
    private String iscongnito;
    private String israting;

    public SuggestionAdapter(Context context, ArrayList<People> arrfriends ) {
        this.context = context;
        this.arrfriends = arrfriends;
    }

    @NonNull
    @Override
    public SuggestionAdapter.modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.list_item,viewGroup,false);
        return new SuggestionAdapter.modelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionAdapter.modelViewHolder modelViewHolder, int position) {
        if (arrfriends.size() != 0) {
            People people = arrfriends.get(position);
            String s ;
            if(!people.getFromCognitoRemoveDate().equals("")) {
                s = people.getSsn();
                if (!people.getProfilePicture().isEmpty()) {
                    Glide.with(context).load(people.getProfilePicture())
                            .into(modelViewHolder.imageView);
                } else {
                    Glide.with(context).
                            load(context.getDrawable(R.drawable.xx))
                            .into(modelViewHolder.imageView);
                }
                modelViewHolder.cvSuggestion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            userid=people.getId();
                            username=people.getName();
                            iscongnito=people.getFromCognitoRemoveDate();
                            israting=people.getMy_rate();

                            if (!people.getToCognitoRemoveDate().equals("")) {
                                addMessageDialog();
                            } else {
                                AppController.getManager().setInterest(Constant.DATING);
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("page_id", "");
                                chatIntent.putExtra("user_id", people.getId());
                                chatIntent.putExtra("user_name", people.getName());
                                chatIntent.putExtra("iscongnito", people.getFromCognito());
                                chatIntent.putExtra("israting", people.getMy_rate());
                                chatIntent.putExtra("type", "single");
                                context.startActivity(chatIntent);
                            }
                        }
                    });
                }

            else {
                s = people.getSsn().substring(0, 2);
                for (int i = s.length(); i <people.getSsn().length() ; i++) {
                    s=s+"*";
                }
                if (!people.getProfilePicture().isEmpty()) {
                    Glide.with(context).load(people.getProfilePicture())
                            .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)))
                            .into(modelViewHolder.imageView);
                } else {
                    Glide.with(context).load(context.getDrawable(R.drawable.xx))
                            .apply(bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(25,3)))
                            .into(modelViewHolder.imageView);
                }

                modelViewHolder.cvSuggestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) context).replaceFragment(SendRequestFragment.newInstance(people), true);
                    }
                });

            }
            modelViewHolder.btnssn.setText(s);
            modelViewHolder.ratingBar.setFocusableInTouchMode(false);
           modelViewHolder.ratingBar.setClickable(false);
           modelViewHolder.ratingBar.setFocusable(false);
           modelViewHolder.ratingBar.setIsIndicator(true);
           modelViewHolder.totalrate.setText("Total Rating"+":"+people.getTotal_rate());
           modelViewHolder.totaldistance.setText("Away : "+people.getDistance().substring(0,4)+"km");
            modelViewHolder.ratingBar.setRating(Float.parseFloat(people.getAvg_rating()));
            // CommonUtil.ConvertURLToBitmap(people.getProfilePicture(),context ,modelViewHolder.imageView);
        }
        else
        {
            Toast.makeText(context,"Friends not found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return arrfriends.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button btnssn;
        RatingBar ratingBar;
        CardView cvSuggestion;
        TextView totalrate,totaldistance;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.circuler_profile_image);
            btnssn =itemView.findViewById(R.id.name);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            cvSuggestion = itemView.findViewById(R.id.card_view);
            totalrate=itemView.findViewById(R.id.tvtotalrate);
            totaldistance=itemView.findViewById(R.id.tvdistance);

        }
    }

    public void addMessageDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.checkchat);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView tvNew = dialog.findViewById(R.id.tv_cretenew);
//        TextView tvExiting = dialog.findViewById(R.id.tv_cretefrom_existing);

        Button btnSend = dialog.findViewById(R.id.btn_send);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getManager().setInterest(Constant.SOCIAL);
                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                chatIntent.putExtra("page_id", "");
                chatIntent.putExtra("user_id", userid);
                chatIntent.putExtra("user_name", username);
                chatIntent.putExtra("iscongnito", iscongnito);
                chatIntent.putExtra("israting", israting);
                chatIntent.putExtra("type", "single");
                context.startActivity(chatIntent);
                dialog.dismiss();

//                AppController.getManager().setInterest(Constant.DATING);
//                addCategory(topic);
//                etCreate.setText(editName);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AppController.getManager().setInterest(Constant.DATING);
            }
        });
        dialog.show();
    }
}
