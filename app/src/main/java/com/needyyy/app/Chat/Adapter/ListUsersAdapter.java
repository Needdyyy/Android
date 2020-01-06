package  com.needyyy.app.Chat.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.needyyy.app.R;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

/**
 * Created by Admin on 21-08-2019.
 */

public class ListUsersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBUser> qbUsersArrayList;
    public ListUsersAdapter(Context context, ArrayList<QBUser> qbUsersArrayList) {
        this.context = context;
        this.qbUsersArrayList = qbUsersArrayList;
    }



    @Override
    public int getCount() {
        return qbUsersArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return qbUsersArrayList.get(position);
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
            view=layoutInflater.inflate(android.R.layout.simple_list_item_multiple_choice,null);

            TextView textView=(TextView) view.findViewById(android.R.id.text1);
            textView.setText(qbUsersArrayList.get(position).getLogin());

        }
        return view;
    }
}
