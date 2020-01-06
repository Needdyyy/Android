package com.needyyy.app.Modules.Home.Activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.needyyy.app.Modules.Home.Adapters.SearchPlaceAdapter;
import com.needyyy.app.Modules.Home.modle.CommentData;
import com.needyyy.app.Modules.Home.modle.GooglePlaceResult;
import com.needyyy.app.Modules.Home.modle.googlePlace.PlaceBase;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.utils.GPSTracker;
import com.needyyy.app.utils.Progress;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebConstants;
import com.needyyy.app.webutils.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.needyyy.app.constants.Constants.kStatus;

public class CheckInActivity extends AppCompatActivity {
    EditText edt_search;
    private String searchText,searchString;
    private Timer timer;
    private RecyclerView rvPlace ;
    private GPSTracker gpsTracker ;
    private SearchPlaceAdapter searchPlaceAdapter ;
    private LinearLayoutManager linearLayoutManager ;
    private final String TAG = getClass().getSimpleName() + ">>>";
    ArrayList<CommentData> data ;
    private ImageView imgback;
    AppTextView tv_title;
    Progress mprogress;
    private ArrayList<GooglePlaceResult> googlePlaceResultArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        initView();
        tv_title.setText("Search Location");
        setListners();
        setAdapter();
    }

    private void setAdapter() {
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.devider));
        searchPlaceAdapter  = new SearchPlaceAdapter(1,this,googlePlaceResultArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        rvPlace.addItemDecoration(itemDecorator);
        rvPlace.setLayoutManager(linearLayoutManager);
        rvPlace.setAdapter(searchPlaceAdapter);
    }

    private void setListners() {
        addTextListener(edt_search);
        edt_search.setOnEditorActionListener(actionListener);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tv_title=findViewById(R.id.tv_title);
        edt_search = findViewById(R.id.edt_search);
        rvPlace    = findViewById(R.id.rv_Place);
        imgback    = findViewById(R.id.btn_back);
        data =new ArrayList<>();
        gpsTracker = new GPSTracker(this);
        searchString = "name,rating,formatted_phone_number,geometry,formatted_address,icon,photo,place_id,url,website,vicinity";
    }

    TextView.OnEditorActionListener actionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            CommonUtil.closeKeyboard(this);
        }
        return true;
    };

    private void addTextListener(EditText edtSearch) {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
                searchText = s.toString().toLowerCase();

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(this == null)
                            return;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("searchText","searchText"+searchText);
                                if(!searchText.equals("")) {

                                    searchPlace(searchText);
                                    CommonUtil.closeKeyboard(CheckInActivity.this);
                                }
                                else{

                                }

                            }
                        });

                    }
                }, 1000); // 600ms delay before the timer executes the â€žrunâ€œ method from TimerTask

            }
        });

//        edtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                s = s.toString().toLowerCase();
//                if(!s.equals("")) {
//
//                    linear_top.setVisibility(View.GONE);
//
//                    setglobalsearch(String.valueOf(s), 1);
//                  //  switcherListener.switchFragment(GlobalOnlineSearchFragment.newInstance(switcherListener),false,false);
//                    // loadData(String.valueOf(s));
//                }else{
//                    linear_top.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                    emptyViewlist.setVisibility(View.GONE);
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }
    //https://maps.googleapis.com/maps/api/place/autocomplete/json?input=appsquadz&types=establishment&location=28.6290,77.3791&radius=500&key=AIzaSyBNNito4BweuIOPUDzZBoRLDgW44i82sWI
    private void searchPlace(String searchText) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebConstants.kGoogleMapsBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebInterface service = retrofit.create(WebInterface.class);
        service.getPlaces(searchText,"AIzaSyAy9jwmpKuuNJdID26ChQADu0HofAWGZNc")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("response","==="+response);
                        try {
                            JsonObject jsonObject = response.body();
                            JSONObject jsonResponse = new JSONObject(jsonObject.toString());
                            String status = jsonResponse.getString(kStatus);
                            if (status.equals("OK")) {
                                JSONArray jsonArray = jsonResponse.getJSONArray("predictions");
                                if (googlePlaceResultArrayList!=null&&googlePlaceResultArrayList.size()!=0)googlePlaceResultArrayList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jO = jsonArray.getJSONObject(i);
                                    String description = jO.getString(Constant.description);
                                    String id = jO.getString("place_id");
                                    googlePlaceResultArrayList.add(new GooglePlaceResult(id,description));
                                }
                                Log.e("jsonArray","response"+jsonArray);
                                Log.e("jsonArray","response"+response);
                                searchPlaceAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i(TAG, t.toString());
                    }
                });


    }

    public void getPlaceDetails(String placeid, String name) {
        showProgressDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebConstants.kGoogleMapsBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebInterface service = retrofit.create(WebInterface.class);
        service.getPlacesDetails(placeid,searchString,"AIzaSyAy9jwmpKuuNJdID26ChQADu0HofAWGZNc")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        cancelProgressDialog();
                        try {
                            Log.e("response=====","response======="+response);
                            JsonObject jsonObject = response.body();
                            JSONObject jsonResponse = new JSONObject(jsonObject.toString());
                            String status = jsonResponse.getString(kStatus);
                            if (status.equals("OK")) {
                                Gson gson = new Gson();
                                PlaceBase placeBase = gson.fromJson(response.body(),PlaceBase.class);
                                placeBase.getResult().setFullname(name);
                                Intent intent = getIntent();
                                intent.putExtra("key", (Serializable) placeBase);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i(TAG, t.toString());
                        cancelProgressDialog();
                    }
                });
    }
    public void  showProgressDialog() {
        mprogress = new Progress(this);
        mprogress.setCancelable(false);
        mprogress.show();
    }
    public void cancelProgressDialog() {
        if (mprogress!=null)
            if(mprogress.isShowing()) {
                mprogress.dismiss();
            }
    }

//https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJxbi6cfjvDDkRQJYiUX4KfVU&fields=name,rating,formatted_phone_number,address_component,opening_hours,geometry,address_component,adr_address,formatted_address,geometry,icon,name,permanently_closed,photo,place_id,plus_code,type,url,utc_offset,vicinity&key=AIzaSyBNNito4BweuIOPUDzZBoRLDgW44i82sWI
}
