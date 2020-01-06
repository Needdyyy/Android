
package com.needyyy.app.mypage.model.masterindex.masterindex;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    public List<Hobbies> getHobbies_master() {
        return hobbies_master;
    }

    public void setHobbies_master(List<Hobbies> hobbies_master) {
        this.hobbies_master = hobbies_master;
    }

    @SerializedName("hobbies_master")
    @Expose
    private List<Hobbies> hobbies_master = null;

    @SerializedName("android_v")
    @Expose
    private String androidV;
    @SerializedName("search_history")
    @Expose
    private List<SearchHistory> searchHistory = null;

    public List<BoostCategory> getBoosycategory() {
        return boosycategory;
    }

    public void setBoosycategory(List<BoostCategory> boosycategory) {
        this.boosycategory = boosycategory;
    }

    @SerializedName("boost_category_master")
    @Expose
    private List<BoostCategory> boosycategory = null;
    public List<ReportPost> getReportPost() {
        return reportPost;
    }

    public void setReportPost(List<ReportPost> reportPost) {
        this.reportPost = reportPost;
    }

    @SerializedName("report_post")
    @Expose
    private List<ReportPost> reportPost = null;

    public String getAndroidV() {
        return androidV;
    }

    public void setAndroidV(String androidV) {
        this.androidV = androidV;
    }

    public List<SearchHistory> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(List<SearchHistory> searchHistory) {
        this.searchHistory = searchHistory;
    }

}
