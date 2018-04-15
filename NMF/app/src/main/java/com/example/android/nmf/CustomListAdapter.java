package com.example.android.nmf;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by karan rajesh tiwari on 18/10/2015.
 */
public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] station_name;
    private final String[] distance;

    public CustomListAdapter(Activity context, String[] station_name, String[] distance) {
        super(context, R.layout.list_nearest_station_item, station_name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.station_name=station_name;
        this.distance=distance;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_nearest_station_item, null, true);

        TextView txtStationTitle = (TextView) rowView.findViewById(R.id.station_text_view);
        TextView txtDistanceView = (TextView) rowView.findViewById(R.id.stationDistance_text_view);


        txtStationTitle.setText(station_name[position]);
        txtDistanceView.setText(distance[position]);

        return rowView;

    };

}
