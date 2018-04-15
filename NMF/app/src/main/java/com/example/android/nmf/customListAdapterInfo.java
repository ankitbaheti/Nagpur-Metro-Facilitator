package com.example.android.nmf;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by karan rajesh tiwari on 18/10/2015.
 */
public class customListAdapterInfo  extends ArrayAdapter{
    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public customListAdapterInfo(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.list_info_station, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_info_station, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.station_name_info_text_view);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.info_image_view);


        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);

        return rowView;

    };

}
