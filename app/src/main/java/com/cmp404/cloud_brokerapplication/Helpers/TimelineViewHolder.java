package com.cmp404.cloud_brokerapplication.Helpers;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cmp404.cloud_brokerapplication.R;
import com.github.vipulasri.timelineview.TimelineView;

public class TimelineViewHolder extends RecyclerView.ViewHolder {

    public TimelineView timeline;
    public TextView nameTextView;

    public TimelineViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);

        timeline = itemView.findViewById(R.id.dashboard_timeline);
        nameTextView = itemView.findViewById(R.id.dashboard_processLabel);

        timeline.initLine(viewType);
    }
}