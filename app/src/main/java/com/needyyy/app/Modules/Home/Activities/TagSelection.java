package com.needyyy.app.Modules.Home.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.needyyy.AppController;
import com.needyyy.app.Base.BaseActivity;
import com.needyyy.app.Modules.AddPost.Adapters.PeopletagAdapter;
import com.needyyy.app.Modules.AddPost.models.People;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.R;
import com.needyyy.app.utils.CommonUtil;
import com.needyyy.app.utils.Constant;
import com.needyyy.app.views.AppTextView;
import com.needyyy.app.webutils.WebInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagSelection extends BaseActivity implements View.OnClickListener {

    public int firstVisibleItem, visibleItemCount, totalItemCount;
    public int previousTotalItemCount;
    public String last_id = "";
    RecyclerView userListRV;
    AppCompatRadioButton mmeExpertRB, mentorRB, followersRB;
    RadioGroup radioGroup;
    private RelativeLayout rlTag;
    ArrayList<People> followResponseArrayList, mmeExpertArrayList, mentorArrayList;
    ArrayList<People> alreadytaggedpeople;
    PeopletagAdapter mAdapter;
    TextView title, errorTV;
    ImageView back;
    LinearLayoutManager LM;
    int userType;
    private AppTextView next;
    int isalreadyconnected = 0;
    private int visibleThreshold = 2;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_selection);
        followResponseArrayList = new ArrayList<>();
        mmeExpertArrayList = new ArrayList<>();
        mentorArrayList = new ArrayList<>();
//        networkCall = new NetworkCall(this, TagSelection.this);

        if (getIntent() != null && getIntent().hasExtra(Constant.ALREADY_TAGGED_PEOPLE)) {
            alreadytaggedpeople = (ArrayList<People>) getIntent().getExtras().getSerializable(Constant.ALREADY_TAGGED_PEOPLE);
        } else {
            alreadytaggedpeople = new ArrayList<>();
        }

        initView();
        userType = 1;
        if (mmeExpertArrayList != null && mmeExpertArrayList.size() == 0)
            RefreshList(true);
        mAdapter = new PeopletagAdapter(TagSelection.this, mmeExpertArrayList, 1);
        if (errorTV.getVisibility() == View.VISIBLE) errorTV.setVisibility(View.GONE);
        userListRV.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        userListRV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = LM.getChildCount();
                totalItemCount = LM.getItemCount();

                firstVisibleItem = LM.findFirstVisibleItemPosition();

                if (totalItemCount > 10) {
                    if (loading) {
                        if (totalItemCount > previousTotalItemCount) {
                            loading = false;
                            previousTotalItemCount = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        Log.i("Yaeye!", "end called");
                        if (userType == 1) {
                            last_id = mmeExpertArrayList.get(totalItemCount - 1).getId();
                        } else if (userType == 2) {
//                            last_id = mentorArrayList.get(totalItemCount - 1).getId();
                        } else if (userType == 3) {
//                            last_id = followResponseArrayList.get(totalItemCount - 1).getId();
                        }
                        if (isalreadyconnected == 0) {
                            RefreshList(false); // from the scrolling action
                            loading = true;
                            isalreadyconnected = 1;
                        }
                    }
                }

            }
        });
    }

    private void RefreshList(boolean b) {
        if (CommonUtil.isConnectingToInternet(this)){
            showProgressDialog();
            WebInterface Service = AppController.getRetrofitInstance(false).create(WebInterface.class);
            Call<PeopleBase> call = Service.getFriends(1,20,"","","");
            call.enqueue(new Callback<PeopleBase>() {
                @Override
                public void onResponse(Call<PeopleBase> call, Response<PeopleBase> response) {
                    cancelProgressDialog();
                    Log.e("dssfsfssf", "fsfsfs" + response.body().toString());
                    PeopleBase peopleBase = response.body();
                    if (peopleBase.getStatus()) {
                        followResponseArrayList.addAll(peopleBase.getData());
                        mAdapter = new PeopletagAdapter(TagSelection.this, GetCustomFileterList(3), 3);
                        userListRV.setAdapter(mAdapter);
                    } else {
                        if (peopleBase.getMessage().equals("110110")){
                            AppController.getManager().clearPreference();
                            Intent intent = new Intent(TagSelection.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }else{
                            snackBar(peopleBase.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PeopleBase> call, Throwable t) {
                    cancelProgressDialog();

                }
            });
        }else{

        }

    }

    private void snackBar(String message) {
        Snackbar snackbar = Snackbar.make(rlTag, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    private void initView() {
        rlTag = findViewById(R.id.rltag);
        title = findViewById(R.id.title);
        errorTV = findViewById(R.id.errorTV);
        next = findViewById(R.id.tv_done);
        mmeExpertRB = findViewById(R.id.mmeExpertRB);
        mentorRB = findViewById(R.id.mentorRB);
        followersRB = findViewById(R.id.followingRB);
        radioGroup = findViewById(R.id.usersCategoryRG);
        userListRV = findViewById(R.id.usersListRV);
        back = findViewById(R.id.btn_back);

        LM = new LinearLayoutManager(this);
        LM.setOrientation(LinearLayoutManager.VERTICAL);

        userListRV.setLayoutManager(LM);
//        userListRV.addItemDecoration(new VerticalSpaceItemDecoration(2));
//
//        title.setText(getString(R.string.tag_your_friends));

        mmeExpertRB.setChecked(true);
        userType = 1;
        next.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void setVisibleNextBtn(boolean b) {
        next.setText("Done");
        if (next.getVisibility() == View.GONE) next.setVisibility(View.VISIBLE);
    }
//    private RecyclerView.Adapter getListAdapter() {
////        mAdapter = new PeopleTagAdapter();
////        return mAdapter;
////    }

//    @Override
//    public Builders.Any.B getAPIB(String apitype) {
//        switch (apitype) {
//            case API.API_FOLLOWING_LIST:
//                return (Builders.Any.B) Ion.with(this)
//                        .load(API.API_FOLLOWING_LIST + Const.IS_WATCHER + SharedPreference.getInstance().getLoggedInUser().getId())
//                        .setTimeout(10 * 1000)
//                        .setMultipartParameter(Const.USER_ID, SharedPreference.getInstance().getLoggedInUser().getId());
//
//            case API.API_GET_MENTOR_LIST:
//                return (Builders.Any.B) Ion.with(this)
//                        .load(API.API_GET_MENTOR_LIST + Const.IS_WATCHER + SharedPreference.getInstance().getLoggedInUser().getId())
//                        .setTimeout(10 * 1000)
//                        .setMultipartParameter(Const.USER_ID, SharedPreference.getInstance().getLoggedInUser().getId());
//
//            case API.API_GET_MME_EXPERT_LIST:
//                return (Builders.Any.B) Ion.with(this)
//                        .load(API.API_GET_MME_EXPERT_LIST + Const.IS_WATCHER + SharedPreference.getInstance().getLoggedInUser().getId())
//                        .setTimeout(10 * 1000)
//                        .setMultipartParameter(Const.USER_ID, SharedPreference.getInstance().getLoggedInUser().getId());
//
//            case API.API_GET_USER_LIST_BASED_CATEGORY:
//                if (userType == 1) {
//                    return (Builders.Any.B) Ion.with(this)
//                            .load(API.API_GET_USER_LIST_BASED_CATEGORY + Const.IS_TAG)
//                            .setTimeout(10 * 1000)
//                            .setMultipartParameter(Const.USER_ID, SharedPreference.getInstance().getLoggedInUser().getId())
//                            .setMultipartParameter(Const.USER_TYPE, getString(R.string.expert))
//                            .setMultipartParameter(Const.LAST_ID, last_id);
//                } else if (userType == 2) {
//                    return (Builders.Any.B) Ion.with(this)
//                            .load(API.API_GET_USER_LIST_BASED_CATEGORY + Const.IS_TAG)
//                            .setTimeout(10 * 1000)
//                            .setMultipartParameter(Const.USER_ID, SharedPreference.getInstance().getLoggedInUser().getId())
//                            .setMultipartParameter(Const.USER_TYPE, getString(R.string.mentor))
//                            .setMultipartParameter(Const.LAST_ID, last_id);
//                } else if (userType == 3) {
//                    return (Builders.Any.B) Ion.with(this)
//                            .load(API.API_GET_USER_LIST_BASED_CATEGORY + Const.IS_TAG)
//                            .setTimeout(10 * 1000)
//                            .setMultipartParameter(Const.USER_ID, SharedPreference.getInstance().getLoggedInUser().getId())
//                            .setMultipartParameter(Const.USER_TYPE, "normal")
//                            .setMultipartParameter(Const.LAST_ID, last_id);
//                }
//        }
//        return null;
//    }
//
//    @Override
//    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
//        Gson gson = new Gson();
//        JSONArray dataarray;
//        Log.e("people tag", jsonstring.toString());
//        switch (apitype) {
//            case API.API_FOLLOWING_LIST:
//                if (jsonstring.optString(Const.STATUS).equals(Const.TRUE)) {
//                    followResponseArrayList = new ArrayList<>();
//                    dataarray = jsonstring.getJSONArray(Const.DATA);
//                    if (dataarray.length() > 0) {
//                        int i = 0;
//                        while (i < dataarray.length()) {
//                            JSONObject singledatarow = dataarray.getJSONObject(i);
//                            People people = gson.fromJson(singledatarow.toString(), People.class);
//                            people.setCategory(userType);
//                            followResponseArrayList.add(people);
//                            i++;
//                        }
//                        PostActivity.followResponseArrayList = this.followResponseArrayList;
//                        mAdapter = new PeopletagAdapter(TagSelection.this, followResponseArrayList, 3);
//                        userListRV.setAdapter(mAdapter);
//
//                    }
//                } else {
//                    ErrorCallBack(jsonstring.optString(Const.MESSAGE, getString(R.string.exception_api_error_message)), apitype);
//                }
//                break;
//
//            case API.API_GET_MENTOR_LIST:
//                if (jsonstring.optString(Const.STATUS).equals(Const.TRUE)) {
//                    mentorArrayList = new ArrayList<>();
//                    dataarray = jsonstring.getJSONArray(Const.DATA);
//                    if (dataarray.length() > 0) {
//                        int i = 0;
//                        while (i < dataarray.length()) {
//                            JSONObject singledatarow = dataarray.getJSONObject(i);
//                            People people = gson.fromJson(singledatarow.toString(), People.class);
//                            people.setCategory(userType);
//                            mentorArrayList.add(people);
//                            i++;
//                        }
//                        PostActivity.mentorArrayList = this.mentorArrayList;
//                        mAdapter = new PeopletagAdapter(TagSelection.this, followResponseArrayList, 2);
//                        userListRV.setAdapter(mAdapter);
//                    }
//                } else {
//                    ErrorCallBack(jsonstring.optString(Const.MESSAGE, getString(R.string.exception_api_error_message)), apitype);
//                }
//                break;
//
//            case API.API_GET_MME_EXPERT_LIST:
//                if (jsonstring.optString(Const.STATUS).equals(Const.TRUE)) {
//                    mmeExpertArrayList = new ArrayList<>();
//                    dataarray = jsonstring.getJSONArray(Const.DATA);
//                    if (dataarray.length() > 0) {
//                        int i = 0;
//                        while (i < dataarray.length()) {
//                            JSONObject singledatarow = dataarray.getJSONObject(i);
//                            People people = gson.fromJson(singledatarow.toString(), People.class);
//                            people.setCategory(userType);
//                            mmeExpertArrayList.add(people);
//                            i++;
//                        }
//                        PostActivity.mmeExpertArrayList = this.mmeExpertArrayList;
//                        mAdapter = new PeopletagAdapter(TagSelection.this, followResponseArrayList, 1);
//                        userListRV.setAdapter(mAdapter);
//                    }
//                } else {
//                    ErrorCallBack(jsonstring.optString(Const.MESSAGE, getString(R.string.exception_api_error_message)), apitype);
//                }
//                break;
//
//            case API.API_GET_USER_LIST_BASED_CATEGORY:
//                isalreadyconnected = 0;
//                if (jsonstring.optString(Const.STATUS).equals(Const.TRUE)) {
//                    dataarray = jsonstring.getJSONArray(Const.DATA);
//                    if (errorTV.getVisibility() == View.VISIBLE) errorTV.setVisibility(View.GONE);
//                    if (TextUtils.isEmpty(last_id)) {
//                        if (userType == 1) {
//                            mmeExpertArrayList = new ArrayList<>();
//                        } else if (userType == 2) {
//                            mentorArrayList = new ArrayList<>();
//                        } else if (userType == 3) {
//                            followResponseArrayList = new ArrayList<>();
//                        }
//                    }
//                    if (dataarray.length() > 0) {
//                        int i = 0;
//                        while (i < dataarray.length()) {
//                            JSONObject singledatarow = dataarray.getJSONObject(i);
//                            People people = gson.fromJson(singledatarow.toString(), People.class);
//                            people.setCategory(userType);
//                            if (userType == 1) {
//                                mmeExpertArrayList.add(people);
//                            } else if (userType == 2) {
//                                mentorArrayList.add(people);
//                            } else if (userType == 3) {
//                                followResponseArrayList.add(people);
//                            }
//                            i++;
//                        }
//
//                        if (userType == 1) {
//                            PostActivity.mmeExpertArrayList = this.mmeExpertArrayList;
//                            mAdapter = new PeopletagAdapter(TagSelection.this, GetCustomFileterList(1), 1);
//                        } else if (userType == 2) {
//                            PostActivity.mentorArrayList = this.mentorArrayList;
//                            mAdapter = new PeopletagAdapter(TagSelection.this, GetCustomFileterList(2), 1);
//                        } else if (userType == 3) {
//                            PostActivity.followResponseArrayList = this.followResponseArrayList;
//                            mAdapter = new PeopletagAdapter(TagSelection.this, GetCustomFileterList(3), 1);
//                        }
//                        userListRV.setAdapter(mAdapter);
//                    }
//                } else {
//                    ErrorCallBack(jsonstring.optString(Const.MESSAGE, getString(R.string.exception_api_error_message)), apitype);
//                }
//                break;
//        }
//    }

    public ArrayList<People> GetCustomFileterList(int i) {
        ArrayList<People> TempArray = new ArrayList<>();
        if (i == 2) {
            if (alreadytaggedpeople == null) {
                return mentorArrayList;
            } else {
                TempArray.addAll(mentorArrayList);
                for (People alreadyppl : alreadytaggedpeople) {

                    for (People res : mentorArrayList) {
                        if (alreadyppl.getId().equals(res.getId())) {
                            TempArray.remove(res);
                        }
                    }
                }
                mentorArrayList = TempArray;
            }
            return mentorArrayList;
        } else if (i == 1) {
            if (alreadytaggedpeople == null) {
                return mmeExpertArrayList;
            } else {
                TempArray.addAll(mmeExpertArrayList);
                for (People alreadyppl : alreadytaggedpeople) {

                    for (People res : mmeExpertArrayList) {
                        if (alreadyppl.getId().equals(res.getId())) {
                            TempArray.remove(res);
                        }
                    }
                }
                mmeExpertArrayList = TempArray;
            }
            return mmeExpertArrayList;
        } else if (i == 3) {
            if (alreadytaggedpeople == null) {
                return followResponseArrayList;
            } else {
                TempArray.addAll(followResponseArrayList);
                for (People alreadyppl : alreadytaggedpeople) {
                    for (People res : followResponseArrayList) {
                        if (alreadyppl.getId().equals(res.getId())) {
                            TempArray.remove(res);
                        }
                    }
                }
                followResponseArrayList = TempArray;
            }
            return followResponseArrayList;
        }
        return null;
    }
//
//    @Override
//    public void ErrorCallBack(String jsonstring, String apitype) {
//        switch (apitype) {
//            case API.API_GET_USER_LIST_BASED_CATEGORY:
//                isalreadyconnected = 0;
//                errorTV.setText(jsonstring);
//                errorTV.setVisibility(View.VISIBLE);
//                break;
//        }
//        Toast.makeText(this, jsonstring, Toast.LENGTH_SHORT).show();
//    }

    public void onPeopleAdded(People people, boolean boo) {
        if (alreadytaggedpeople == null) alreadytaggedpeople = new ArrayList<>();
        if (boo) {
            if (!alreadytaggedpeople.contains(people))
                alreadytaggedpeople.add(people);
        } else {
            alreadytaggedpeople.remove(people);
        }
        if (alreadytaggedpeople.size() >= 1) {
            setVisibleNextBtn(true);
        } else {
            setVisibleNextBtn(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        back.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_done:
                if (alreadytaggedpeople != null && alreadytaggedpeople.size() >= 1) {
                    Intent taggedpeople = new Intent();
                    taggedpeople.putExtra(Constant.ALREADY_TAGGED_PEOPLE, alreadytaggedpeople);
                    setResult(RESULT_OK, taggedpeople);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
                break;

            case R.id.btn_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//    }

//    public interface IPeopleSelectionListener {
//        void onpeopletagSelected(People response);
//    }
//
//    private class PeopletagAdapter extends RecyclerView.Adapter<PeopletagAdapter.PeopleTagHolder> {
//        Activity activity;
//        ArrayList<People> showPeopleArrayList;
//        int userType;
//
//        public PeopletagAdapter(Activity activity, ArrayList<People> arrayList, int i) {
//            this.activity = activity;
//            this.showPeopleArrayList = arrayList;
//            this.userType = i;
//        }
//
//        @NonNull
//        @Override
//        public PeopleTagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_tag_list, parent, false);
////
//            return new PeopleTagHolder(v);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull PeopleTagHolder holder, int position) {
//            PeopleTagHolder tagHolder = holder;
//            tagHolder.setUsers(showPeopleArrayList.get(position));
//        }

//        @Override
//        public int getItemCount() {
//            return showPeopleArrayList.size();
//        }


//        // TODO PEOPLE TAGLIST ADAPTER
//        public class PeopleTagHolder extends RecyclerView.ViewHolder {
//            AppCompatCheckedTextView NameTV;
//            ImageView ImageIV;
//            ImageView ImageIVText;
//            People people;
//
//            public PeopleTagHolder(final View view) {
//                super(view);
//
//                NameTV = (AppCompatCheckedTextView) view.findViewById(R.id.nameTV);
//                ImageIV = (ImageView) view.findViewById(R.id.imageIV);
//                ImageIVText = (ImageView) view.findViewById(R.id.imageIVText);
//
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if (showPeopleArrayList.get(getAdapterPosition()).getCategory() == 3) {
////                            showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
////                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()));
////                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
////                            NameTV.setCheckMarkDrawable(R.drawable.check_on);
////                        } else if (showPeopleArrayList.get(getAdapterPosition()).getCategory() == 2) {
////                            showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
////                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()));
////                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
////                            NameTV.setCheckMarkDrawable(R.drawable.check_on);
////                        } else if (showPeopleArrayList.get(getAdapterPosition()).getCategory() == 1) {
////                            showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
////                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()));
////                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
////                            NameTV.setCheckMarkDrawable(R.drawable.check_on);
////                        }
//                        if (!showPeopleArrayList.get(getAdapterPosition()).isTagged()) {
//                            showPeopleArrayList.get(getAdapterPosition()).setTagged(true);
//                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()), showPeopleArrayList.get(getAdapterPosition()).isTagged());
////                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
//                            NameTV.setCheckMarkDrawable(R.drawable.check_on);
//                        } else {
//                            showPeopleArrayList.get(getAdapterPosition()).setTagged(false);
//                            ((TagSelection) activity).onPeopleAdded(showPeopleArrayList.get(getAdapterPosition()), showPeopleArrayList.get(getAdapterPosition()).isTagged());
////                            Toast.makeText(activity, showPeopleArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
//                            NameTV.setCheckMarkDrawable(R.drawable.check_off);
//                        }
//
//                    }
//                });
//            }
//
//            public void setUsers(People people) {
//                this.people = people;
//                people.setName(Helper.CapitalizeText(people.getName()));
//                NameTV.setText(people.getName());
//
//                if (!TextUtils.isEmpty(people.getProfile_picture())) {
//                    ImageIV.setVisibility(View.VISIBLE);
//                    ImageIVText.setVisibility(View.GONE);
//                    Ion.with(ImageIV.getContext()).load(people.getProfile_picture())
//                            .asBitmap()
//                            .setCallback(new FutureCallback<Bitmap>() {
//                                @Override
//                                public void onCompleted(Exception e, Bitmap result) {
//                                    if (result != null)
//                                        ImageIV.setImageBitmap(result);
//                                    else
//                                        ImageIV.setImageResource(R.mipmap.default_pic);
//                                }
//                            });
//                } else {
//                    ImageIV.setVisibility(View.GONE);
//                    ImageIVText.setVisibility(View.VISIBLE);
//                    ImageIVText.setImageDrawable(Helper.GetDrawable(people.getName(), TagSelection.this, people.getId()));
//                }
//
//                if (people.isTagged()) {
//                    NameTV.setCheckMarkDrawable(R.drawable.check_on);
//                } else {
//                    NameTV.setCheckMarkDrawable(R.drawable.check_off);
//                }
//            }
//        }
//    }

//    private class PeopleTagAdapter extends CustomSearchListAdapter<People> {
//
//        @Override
//        protected List<People> getFilteredList(String query) {
//            ArrayList<People> filteredList = new ArrayList<>();
//            if (query == null || query.trim().length() == 0)
//                filteredList = masterItems;
//            else {
//                String PeopleName;
//                int size = masterItems.size();
//                for (int i = 0; i < size; i++) {
//                    PeopleName = masterItems.get(i).getName();
//                    if (PeopleName.toLowerCase().contains(query.toLowerCase())) {
//                        filteredList.add(masterItems.get(i));
//                    }
//                }
//            }
//            return filteredList;
//        }
//
//        @Override
//        protected RecyclerView.ViewHolder createViewholder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_people_tag, parent, false);
//
//            return new TagSelection.PeopleTagAdapter.ViewHolder(v);
//        }
//
//        @Override
//        protected void bindViewholder(RecyclerView.ViewHolder holder, int position) {
//            TagSelection.PeopleTagAdapter.ViewHolder tagHolder = (TagSelection.PeopleTagAdapter.ViewHolder) holder;
//            tagHolder.setUsers(getItem(position));
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            TextView NameTV;
//            ImageView ImageIV;
//            ImageView ImageIVText;
//            People people;
//
//            public ViewHolder(final View view) {
//                super(view);
//
//                NameTV = (TextView) view.findViewById(R.id.nameTV);
//                ImageIV = (ImageView) view.findViewById(R.id.imageIV);
//                ImageIVText = (ImageView) view.findViewById(R.id.imageIVText);
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if (selectionListener != null && people != null) {
////                            selectionListener.onpeopletagSelected(people);
////
////                        }
//                        if (((AppCompatRadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId())).getText()
//                                .equals(getString(R.string.mme_expert))) {
//                            Intent mmeExpert = new Intent();
//                            mmeExpert.putExtra(Const.TITLE_DATA, "MME EXPERT");
//                            setResult(RESULT_OK);
//                        } else if (((AppCompatRadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId())).getText()
//                                .equals(getString(R.string.mentor))) {
//                            Intent mmeExpert = new Intent();
//                            mmeExpert.putExtra(Const.TITLE_DATA, "MENTOR");
//                            setResult(RESULT_OK);
//                        } else if (((AppCompatRadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId())).getText()
//                                .equals(getString(R.string.my_followers))) {
//                            Intent mmeExpert = new Intent();
//                            mmeExpert.putExtra(Const.TITLE_DATA, "Followers");
//                            setResult(RESULT_OK);
//                        }
////                        PeopleTagSelectionFragment.this.dismiss();
//                    }
//                });
//            }
//
//            public void setUsers(People people) {
//                this.people = people;
//                people.setName(Helper.CapitalizeText(people.getName()));
//
////                if (query != null && query.trim().length() > 0) {
////                    Spannable wordtoSpan = new SpannableString(people.getName());
////                    int spanStartIndex = people.getName().toLowerCase().indexOf(query.toLowerCase());
////                    if (spanStartIndex >= 0) {
////                        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), spanStartIndex, spanStartIndex + query.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////                        wordtoSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStartIndex, spanStartIndex + query.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////                    }
////                    NameTV.setText(wordtoSpan);
////                } else {
////                    NameTV.setText(people.getName());
////                }
//                NameTV.setText(people.getName());
//                if (!TextUtils.isEmpty(people.getProfile_picture())) {
//                    ImageIV.setVisibility(View.VISIBLE);
//                    ImageIVText.setVisibility(View.GONE);
//                    Ion.with(ImageIV.getContext()).load(people.getProfile_picture())
//                            .asBitmap()
//                            .setCallback(new FutureCallback<Bitmap>() {
//                                @Override
//                                public void onCompleted(Exception e, Bitmap result) {
//                                    if (result != null)
//                                        ImageIV.setImageBitmap(result);
//                                    else
//                                        ImageIV.setImageResource(R.mipmap.default_pic);
//                                }
//                            });
//                } else {
//                    ImageIV.setVisibility(View.GONE);
//                    ImageIVText.setVisibility(View.VISIBLE);
//                    ImageIVText.setImageDrawable(Helper.GetDrawable(people.getName(), TagSelection.this, people.getId()));
//                }
//            }
//        }
//    }
}
