package com.cmp404.cloud_brokerapplication.Helpers;

public class TimelineItemModel {

    public String name;
    public Status status;

    public TimelineItemModel(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public enum Status {
        IN_PROGRESS, DONE
    }
}