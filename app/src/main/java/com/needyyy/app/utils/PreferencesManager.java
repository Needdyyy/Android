package com.needyyy.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PreferencesManager {

    public String historyType             = "type";
    public String otp                     = "otp";
    public String id                      = "id";
    public String name                    = "name";
    public String email                   = "email";
    public String mobile                  = "mobile";
    public String user_type               = "user_type";
    public String user_name               = "user_name";
    public String profile_image           = "profile_image";
    public String session                 = "session";
    public String CHECKSUMHASH            = "CHECKSUMHASH";
    public String PAYMENT_TOKEN            = "PAYMENT_TOKEN";
    public String Dob                     = "dob";
    public String Address                 = "address";
    public String array                   = "array";
    public String date                    = "date";
    public String timezone                = "timezone";

    public String count                   = "count";
    public String TOCKEN                  = "Tocken";
    public String QUERY                   = "Query";
    public String INTEREST                   = "interest";

    private final String PREF_NAME        = "SystemPreference";
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    public String ssnid                   = "ssnid";
    public String college_name            = "college_name";
    public String school_name             = "school_name";
    public String bio                     = "bio";
    public String nickname                = "nickname";
    public String gender                  = "0";
    public String relation_status         = "0";
    public String interested_in           = "1";
    public String education               = "education";


    public String profession              ="profession";
    public String hometown                = "delhi";
    public String current_city            = "delhi";
    public String open_dating_id          = "1";

    public String  savebtnstatus          = "false";

    @SuppressLint("CommitPrefEdits")
    public PreferencesManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }

    public void clearQuestion(){
        editor.remove(array);
        editor.commit();
    }


    public String getId() {
        return pref.getString(id,"");
    }

    public void setId(String id) {
        editor.putString(this.id,id);
        editor.commit();
    }

    public String getName() {
        return pref.getString(name,"");
    }

    public void setName(String name) {
        editor.putString(this.name,name);
        editor.commit();
    }

    public String getSSNID() {
        return pref.getString(ssnid,"");
    }

    public void setSSNID(String ssnid) {
        editor.putString(this.ssnid,ssnid);
        editor.commit();
    }

    public String getGender() {
        return pref.getString(gender,"1");
    }

    public void setGender(String gender) {
        editor.putString(this.gender,gender);
        editor.commit();
    }

    public String getCollege_name() {
        return pref.getString(college_name,"");
    }

    public void setCollege_name(String college_name) {
        editor.putString(this.college_name,college_name);
        editor.commit();
    }

    public String getSchool_name() {
        return pref.getString(school_name,"");
    }

    public void setSchool_name(String school_name) {
        editor.putString(this.school_name,school_name);
        editor.commit();
    }

    public String getRelation_status() {
        return pref.getString(relation_status,"0");
    }

    public void setRelation_status(String relation_status) {
        editor.putString(this.relation_status,relation_status);
        editor.commit();
    }

    public String getSavebtnstatus() {
        return pref.getString(savebtnstatus,"");
    }

    public void setSavebtnstatus(String savebtnstatus) {
        editor.putString(this.savebtnstatus,savebtnstatus);
        editor.commit();
    }

    public String getBio() {
        return pref.getString(bio,"");
    }

    public void setBio(String bio) {
        editor.putString(this.bio,bio);
        editor.commit();
    }

    public String getNickname() {
        return pref.getString(nickname,"");
    }

    public void setNickname(String nickname) {
        editor.putString(this.nickname,nickname);
        editor.commit();
    }

    public String getInterested_in() {
        return pref.getString(interested_in,"0");
    }

    public void setInterested_in(String interested_in) {
        editor.putString(this.interested_in,interested_in);
        editor.commit();
    }

    public String getOpen_dating_id() {
        return pref.getString(open_dating_id,"1");
    }

    public void setOpen_dating_id(String open_dating_id) {
        editor.putString(this.open_dating_id,open_dating_id);
        editor.commit();
    }

    public String getEducation() {
        return pref.getString(education,"");
    }

    public void setProfession(String profession) {
        editor.putString(this.profession,education);
        editor.commit();
    }
    public String getProfession() {
        return pref.getString(profession,"");
    }

    public void setEducation(String education) {
        editor.putString(this.education,education);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(email,"");
    }

    public void setEmail(String email) {
        editor.putString(this.email,email);
        editor.commit();
    }

    public String getHometown() {
        return pref.getString(hometown,"");
    }

    public void setHometown(String hometown) {
        editor.putString(this.hometown,hometown);
        editor.commit();
    }

    public String getCurrent_city() {
        return pref.getString(current_city,"");
    }

    public void setCurrent_city(String current_city) {
        editor.putString(this.current_city,current_city);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString(mobile,"");
    }

    public void setMobile(String mobile) {
        editor.putString(this.mobile,mobile);
        editor.commit();
    }

    public String getUser_type() {
        return pref.getString(user_type,"");
    }

    public void setUser_type(String user_type) {
        editor.putString(this.user_type,user_type);
        editor.commit();
    }


    public String getUser_name() {
        return pref.getString(user_name,"");
    }

    public void setUser_name(String user_type) {
        editor.putString(this.user_name,user_type);
        editor.commit();
    }
    public String getProfile_image() {
        return pref.getString(profile_image,"");
    }

    public void setProfile_image(String profile_image) {
        editor.putString(this.profile_image,profile_image);
        editor.commit();
    }




    public int getMembership() {
        return pref.getInt("package",0);
    }

    public void setMembership(int membership) {
        editor.putInt("package",membership);
        editor.commit();
    }



    public String getArray() {
        return  pref.getString(this.array,"");
    }

    public void setArray(String array) {
        editor.putString(this.array,array);
        editor.commit();
    }




    public String getDob() {
        return  pref.getString(this.Dob,"");

    }

    public void setDob(String dob) {
        editor.putString(this.Dob,dob);
        editor.commit();
    }


    public String getOtp() {
        return  pref.getString(this.otp,"");
    }

    public void setOtp(String otp) {
        editor.putString(this.otp,otp);
        editor.commit();
    }


    public boolean getSession() {
        return  pref.getBoolean(this.session,false);
    }


    public void setSession(boolean session) {
        editor.putBoolean(this.session,session);
        editor.commit();
    }



    public String getCHECKSUMHASH() {
        return  pref.getString(this.CHECKSUMHASH,"");
    }

    public void setCHECKSUMHASH(String CHECKSUMHASH) {
        editor.putString(this.CHECKSUMHASH,CHECKSUMHASH);
        editor.commit();
    }


    public String getPAYMENT_TOKEN() {
        return  pref.getString(this.PAYMENT_TOKEN,"");
    }

    public void setPAYMENT_TOKEN(String PAYMENT_TOKEN) {
        editor.putString(this.PAYMENT_TOKEN,PAYMENT_TOKEN);
        editor.commit();
    }

    public String getAddress() {
        return  pref.getString(this.Address,"");
    }

    public void setAddress(String address) {
        editor.putString(this.Address,address);
        editor.commit();
    }

    public String getDate() {
        return  pref.getString(this.date,"");
    }

    public void setDate(String date) {
        editor.putString(this.date,date);
        editor.commit();
    }

    public String getTimezone() {
        return  pref.getString(this.timezone,"");
    }

    public void setTimezone(String timezone) {
        editor.putString(this.timezone,timezone);
        editor.commit();
    }


    public int getCount() {
        return  pref.getInt(this.count,0);
    }

    public void setCount(int count) {
        editor.putInt(this.count,count);
        editor.commit();
    }


    public int getHistoryType() {
        return  pref.getInt(this.historyType,0);
    }

    public void setHistoryType(int historyType) {
        editor.putInt(this.historyType,historyType);
        editor.commit();
    }

    public void setTocken(String tocken) {
        editor.putString(this.TOCKEN,tocken);
        editor.commit();
    }
    public String getTocken() {
        return  pref.getString(this.TOCKEN,"");

    }

    public void setInterest(String interest) {
        editor.putString(this.INTEREST,interest);
        editor.commit();
    }
    public String getInterest() {
        return  pref.getString(this.INTEREST,"");

    }

    public void setSearchQuery(String query) {
        editor.putString(this.QUERY,query);
        editor.commit();
    }
    public String getSearchQuery() {
        return  pref.getString(this.QUERY,"");

    }
}
