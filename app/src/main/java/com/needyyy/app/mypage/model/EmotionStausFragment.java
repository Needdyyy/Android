package com.needyyy.app.mypage.model;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.HomeActivity;
import com.needyyy.app.R;
import com.needyyy.app.mypage.EmotionData;
import com.needyyy.app.mypage.model.Emotions.Emotion;
import com.needyyy.app.mypage.model.Emotions.Emotions;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmotionStausFragment extends Fragment implements View.OnClickListener{

    private String id;
    public static String searchText = "";
    ArrayAdapter<String> adapter;
    private SearchView search;
    private ListView listView;
    private Activity activity;
    EmotionData emotionData;
    private ImageView crossimage;
    private ImageView backimage;
    private RecyclerView recyclerView;
    private ArrayList<String> list;
    private List<Emotion> product;
    private CardView cartsearch;

    private Button Submitbutton;
    private String emotion="";
    private Boolean check=true;
    public EmotionStausFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emotion_staus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() instanceof HomeActivity)
        {
            ((HomeActivity) getActivity()).manageToolbar("Emotion Status","2");
        }

//        if(getArguments()!=null)
//        {
//            id=getArguments().getString("id");
//            emotion=getArguments().getString("emotion");
//        }
        product=new ArrayList<>();
        getEmotion("1");
        Submitbutton=(Button) view.findViewById(R.id.submit_button);

        list = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv1);
        cartsearch = (CardView) view.findViewById(R.id.cartsearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.Recycleview1);
        search = (SearchView) view.findViewById(R.id.searchView);
    }

    private void getEmotion(String id) {

        WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
        Call<Emotions> call = Service.getEmotions(id);
        call.enqueue(new Callback<Emotions>() {
            @Override
            public void onResponse(Call<Emotions> call, Response<Emotions> response) {

                Emotions emotions=response.body();
                for (int i=0;i<emotions.getData().size();i++)
                {
                    //Emotion emotion=(Emotion) emotions.getData();
                    product.addAll(emotions.getData());
                    list.add(product.get(i).getTitle());
                }
            }

            @Override
            public void onFailure(Call<Emotions> call, Throwable t) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        if (list.contains(searchText) && !searchText.equals("")) {
//            search.setQuery(searchText, true);
//            adapter.getFilter().filter(searchText);
//        }
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchText = query.toString();
                listView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                if (list.contains(query)) {
                    adapter.getFilter().filter(query);
                } else {
                    check=false;
                    list.add(query);
                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                search.setQuery("", true);
                if(check==true) {
                    Bundle bundle = new Bundle();
                    bundle.putString("emotion", "Feeling");
                    bundle.putString("emotionstatus", product.get(position).getTitle());
                    emotionData.emotioncommunication(bundle);
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("emotion", "Feeling");
                    bundle.putString("emotionstatus", adapter.getItem(position));
                    emotionData.emotioncommunication(bundle);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        emotionData=(EmotionData) context;
    }
}
