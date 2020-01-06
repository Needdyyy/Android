package com.needyyy.app.Modules.adsAndPage.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PageDataBase implements Serializable {
    @SerializedName("pages")
    @Expose
    private PageData pages;
    @SerializedName("budget_calculation")
    @Expose
    private List<BudgetCalculation> budgetCalculation = null;
    @SerializedName("budget")
    @Expose
    private List<Budget> budget = null;
    public PageData getPages() {
        return pages;
    }

    public void setPages(PageData pages) {
        this.pages = pages;
    }

    public List<BudgetCalculation> getBudgetCalculation() {
        return budgetCalculation;
    }

    public void setBudgetCalculation(List<BudgetCalculation> budgetCalculation) {
        this.budgetCalculation = budgetCalculation;
    }

    public List<Budget> getBudget() {
        return budget;
    }

    public void setBudget(List<Budget> budget) {
        this.budget = budget;
    }
}
