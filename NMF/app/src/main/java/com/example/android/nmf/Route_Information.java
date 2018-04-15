package com.example.android.nmf;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Route_Information extends ActionBarActivity {

    StationData stationData=new StationData();

    float price=0;
    int time=0,no_station=0,inter=0;
    int startindex,endindex,ind=0,i,j,temp,k=0;
    int index[]=new int[stationData.allStation.length];

    float[] results = new float[1];


    TextView distance_TextView,price_TextView,time_TextView,inter_TextView,no_station_TextView;

    private TextView mDistanceTextView;



    ArrayAdapter<String> adapter;

    private ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__information);


        Intent mIntent = getIntent();
        startindex = mIntent.getIntExtra("message", 0);
        endindex = mIntent.getIntExtra("message1", 0);
        no_station = mIntent.getIntExtra("message2", 0);
        inter = mIntent.getIntExtra("message3", 0);
        ind = mIntent.getIntExtra("message4", 0);
        index = mIntent.getIntArrayExtra("message5");

        lv1 = (ListView) findViewById(R.id.list_view_info);


        distance_TextView = (TextView) findViewById(R.id.distance_text_view);
        price_TextView = (TextView) findViewById(R.id.price_text_view);
        time_TextView = (TextView) findViewById(R.id.time_text_view);
        inter_TextView = (TextView) findViewById(R.id.inter_change_station_text_view);
        no_station_TextView = (TextView) findViewById(R.id.number_of_station_text_view);

        Location.distanceBetween(stationData.lat[startindex], stationData.lon[startindex],
                stationData.lat[endindex], stationData.lon[endindex],
                results);


        String.format("%.2f", results[0]);

        inter_TextView.setText(inter + "\n" + "INC");
        no_station_TextView.setText(no_station + "\n" + "Stations");
        String s1 = String.format("%.3f", results[0] / 1000);
        distance_TextView.setText(s1 + "\n" + "Km");

        time = (int) (1 * (results[0]) / 1000 + 2 * no_station + inter * 5);
        time_TextView.setText(time + "\n" + "Min");

        price = (float) (3 * results[0]) / 1000;
        String s = String.format("%.2f", price);

        price_TextView.setText(s + "\n" + "Rupees");


        if (ind > 0) {

            if (inter == 0) {
                String station[] = new String[ind];
                Integer imgid[] = new Integer[ind];

                station[0] = stationData.allStation[index[0]];
                imgid[0]=R.drawable.startflag1;

                station[ind-1] = stationData.allStation[index[ind-1]];
                imgid[ind-1]=R.drawable.endflag1;

                for (i = 1; i <ind-1 ; i++) {
                    station[i] = stationData.allStation[index[i]];
                    imgid[i]=R.drawable.station;
                }

                /*adapter = new ArrayAdapter<String>(this, R.layout.list_info_station, R.id.station_name_info_text_view, station);
                lv1.setAdapter(adapter);*/
                customListAdapterInfo adapter=new customListAdapterInfo(this, station, imgid);
                lv1.setAdapter(adapter);


            }

            if (inter == 1) {
                String station[] = new String[ind - 1];
                Integer imgid[] = new Integer[ind];

                station[0] = stationData.allStation[index[0]];
                imgid[0]=R.drawable.startflag1;


                for (i = 1; i < ind - 1; i++) {
                    station[i] = stationData.allStation[index[i]];
                    imgid[i]=R.drawable.station;
                    if (station[i].equalsIgnoreCase("Sitaburdi (Interchange)1") || station[i].equalsIgnoreCase("Sitaburdi (Interchange)2")) {

                          k = i;
                        for (j = ind - 2 ; j > i; j--) {
                            station[++k] = stationData.allStation[index[j]];
                            imgid[k]=R.drawable.station;
                        }
                        imgid[i]=R.drawable.interchange;
                        imgid[i+1]=R.drawable.interchange;
                        imgid[k]=R.drawable.endflag1;

                        break;
                    }
                }

                customListAdapterInfo adapter=new customListAdapterInfo(this, station, imgid);
                lv1.setAdapter(adapter);


            }


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.menu_route:
                intent = new Intent(this, activity_route.class);
                startActivity(intent);
                return true;
            case R.id.menu_near:
                intent = new Intent(this, station_loc_activity.class);
                startActivity(intent);
                return true;
            case R.id.menu_gps:
                intent = new Intent(this, GPS_Sync.class);
                startActivity(intent);
                return true;
            case R.id.menu_eta:
                intent = new Intent(this, ETA.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
