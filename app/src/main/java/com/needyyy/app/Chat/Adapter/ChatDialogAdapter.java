package  com.needyyy.app.Chat.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.needyyy.app.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 21-08-2019.
 */

public class ChatDialogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatDialogAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view=convertView;
       if(view==null)
       {
           LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
           view=layoutInflater.inflate(R.layout.list_chat_dialogs,null);

           TextView txttittle,txtmessage;
           CircleImageView  image;

           txttittle=(TextView)view.findViewById(R.id.list_chat_dialog_title);
           txtmessage=(TextView)view.findViewById(R.id.list_chat_dialog_message);
           image=(CircleImageView) view.findViewById(R.id.image_chatDialog);
           txttittle.setText(qbChatDialogs.get(position).getName());
           txtmessage.setText(qbChatDialogs.get(position).getLastMessage());
           Glide.with(context)
                   .load(R.drawable.needyy)
                   .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(R.drawable.needyy).error(R.drawable.needyy))
                   .into(image);

       }
       return view;
    }
}
