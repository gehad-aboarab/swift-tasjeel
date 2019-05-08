package com.cmp404.cloud_brokerapplication.Helpers;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmp404.cloud_brokerapplication.Android.DubaiPoliceActivity;
import com.cmp404.cloud_brokerapplication.Android.InsuranceActivity;
import com.cmp404.cloud_brokerapplication.Android.RtaActivity;
import com.cmp404.cloud_brokerapplication.Android.TestingCenterActivity;
import com.cmp404.cloud_brokerapplication.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class TimelineViewAdapter extends RecyclerView.Adapter<TimelineViewHolder> {

    public List<TimelineItemModel> items;

    public TimelineViewAdapter(List<TimelineItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int viewType) {
        View view = View.inflate(viewGroup.getContext(), R.layout.list_item_dashboard, null);

        final TimelineViewHolder timelineViewHolder = new TimelineViewHolder(view, viewType);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(timelineViewHolder.nameTextView.getText().equals("Renew your insurance plan")){
                    intent = new Intent(viewGroup.getContext(), InsuranceActivity.class);
                    viewGroup.getContext().startActivity(intent);
                } else if(timelineViewHolder.nameTextView.getText().equals("Get your car checked")){
                    intent = new Intent(viewGroup.getContext(), TestingCenterActivity.class);
                    viewGroup.getContext().startActivity(intent);
                } else if(timelineViewHolder.nameTextView.getText().equals("Pay for your traffic fees")){
                    intent = new Intent(viewGroup.getContext(), DubaiPoliceActivity.class);
                    viewGroup.getContext().startActivity(intent);
                } else if(timelineViewHolder.nameTextView.getText().equals("Pay your RTA registration fees")){
                    intent = new Intent(viewGroup.getContext(), RtaActivity.class);
                    viewGroup.getContext().startActivity(intent);
                }
            }
        });

        return timelineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder timelineViewHolder, int position) {
        TimelineItemModel item = items.get(position);
        timelineViewHolder.nameTextView.setText(item.name);

        switch (item.status) {
            case IN_PROGRESS:
                break;
            case DONE:
                timelineViewHolder.timeline.setMarkerColor(Color.rgb(	88, 185, 153));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(1,3);
    }
}