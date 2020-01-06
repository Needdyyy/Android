package  com.needyyy.app.Chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.needyyy.app.Chat.holder.QbUsersHolder;
import com.needyyy.app.R;

import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

/**
 * Created by Admin on 21-08-2019.
 */

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;

    public ChatMessageAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        this.qbChatMessages = qbChatMessages;
    }



    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView==null)
        {

            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            if(qbChatMessages.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId()))
            {
                view=layoutInflater.inflate(R.layout.list_send_message,null);
                TextView textView= view.findViewById(R.id.tvMessage);
                textView.setText(qbChatMessages.get(position).getBody());
            }
            else
            {
                view=layoutInflater.inflate(R.layout.list_recv_message,null);
                TextView textView= view.findViewById(R.id.tvMessage);
                textView.setText(qbChatMessages.get(position).getBody());
               // TextView txtname=(TextView)view.findViewById(R.id.message_user);
               // txtname.setText(QbUsersHolder.getInstance().getuserId(qbChatMessages.get(position).getSenderId()).getLogin());
            }

        }
        return view;
    }
}
