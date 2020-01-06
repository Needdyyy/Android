package com.needyyy.app.Chat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Json {
    @SerializedName("task_id")
    @Expose
    private Integer taskId;
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
