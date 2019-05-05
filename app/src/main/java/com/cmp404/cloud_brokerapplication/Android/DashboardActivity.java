package com.cmp404.cloud_brokerapplication.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cmp404.cloud_brokerapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends Activity {
    private ListView processesListView;
    private TextView processLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        processesListView = (ListView)findViewById(R.id.dashboard_processesListView);

        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();

        String[] arr = {"Renew your insurance plan",
                "Book an appointment to get your car checked",
                "Pay for your traffic fees",
                "Pay your RTA registration fees"};
        for(int i=0; i<4; i++) {
            map.put("name", arr[i]);
            data.add(map);
        }

        int resource = R.layout.list_item_dashboard;
        String[] from = {"name"};
        int[] to = {R.id.dashboard_processLabel};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, resource, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_dashboard, parent, false);
                }

                Map<String, String> entryData = (Map<String, String>) getItem(position);
                processLabel = (TextView) convertView.findViewById(R.id.dashboard_processLabel);

                processLabel.setText(entryData.get("name"));

                return convertView;
            }

        };

        processesListView.setAdapter(simpleAdapter);

        processesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), Item.class);
//                intent.putExtra("taskId", view.getTag().toString());
//                intent.putExtra("projectId", projectId);
//                intent.putExtra("sectionId", section.getId());
//                startActivityForResult(intent, 2);
            }
        });
    }
}
