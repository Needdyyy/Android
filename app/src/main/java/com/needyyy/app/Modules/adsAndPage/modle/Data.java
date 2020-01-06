
package com.needyyy.app.Modules.adsAndPage.modle;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("pages")
    @Expose
    private Pages pages;
    @SerializedName("target_location")
    @Expose
    private List<TargetLocation> targetLocation = null;
    @SerializedName("budget_calculation")
    @Expose
    private List<BudgetCalculation> budgetCalculation = null;
    @SerializedName("budget")
    @Expose
    private List<Budget> budget = null;

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public List<TargetLocation> getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(List<TargetLocation> targetLocation) {
        this.targetLocation = targetLocation;
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
