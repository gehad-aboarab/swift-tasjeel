package com.cmp404.cloud_brokerapplication.Android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cmp404.cloud_brokerapplication.Helpers.TimelineItemModel;
import com.cmp404.cloud_brokerapplication.Helpers.TimelineViewAdapter;
import com.cmp404.cloud_brokerapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends Activity {
    private RecyclerView recyclerView;
    private BrokerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        application = (BrokerApplication) getApplication();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void init(){
        List<TimelineItemModel> items = new ArrayList<>();

        items.add(new TimelineItemModel("Renew your insurance plan", application.currentUser.isInsuranceDone() == true ? TimelineItemModel.Status.DONE : TimelineItemModel.Status.IN_PROGRESS));
        items.add(new TimelineItemModel("Get your car checked", application.currentUser.isBookingDone() == true ? TimelineItemModel.Status.DONE : TimelineItemModel.Status.IN_PROGRESS));
        items.add(new TimelineItemModel("Pay for your traffic fees", application.currentUser.isPaidFines() == true ? TimelineItemModel.Status.DONE : TimelineItemModel.Status.IN_PROGRESS));
        items.add(new TimelineItemModel("Pay your RTA registration fees", application.currentUser.isPaidRenewal() == true ? TimelineItemModel.Status.DONE : TimelineItemModel.Status.IN_PROGRESS));

        recyclerView = findViewById(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                items.size(),
                GridLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new TimelineViewAdapter(items));
    }
}
