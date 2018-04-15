package com.example.android.nmf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class activity_route extends ActionBarActivity implements OnMapReadyCallback {
    GoogleMap a_map;
    boolean mapReady=false;

    StationData stationData=new StationData();

    AutoCompleteTextView  textview,textview1;
    Button btnGo,btnInfo;

    int j=0,temp=-1,temp1=-1,var=0;
    String start_station=null,end_station=null;
    int no_station,inter;
    int index[]=new int[stationData.allStation.length];
    int ind=0;
    int startindex,endindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_route);


        textview=(AutoCompleteTextView) findViewById(R.id.from);
        textview1=(AutoCompleteTextView) findViewById(R.id.to);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,stationData.allStation);

        textview.setThreshold(0);
        textview.setAdapter(adapter);


        textview1.setThreshold(0);
        textview1.setAdapter(adapter);

        start_station=textview.getText().toString();
        end_station=textview1.getText().toString();

        btnInfo=(Button) findViewById(R.id.more_info_button);

        btnGo=(Button) findViewById(R.id.go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                temp = temp1 = -1;
                var = 0;
                ind=0;
                inter=0;
                no_station=0;
                btnInfo.setEnabled(true);
                if (mapReady) {

                    for (int k = 0; k < stationData.allStation.length; k++) {
                        if ((textview.getText().toString()).equalsIgnoreCase(stationData.allStation[k])) {
                            temp = k;
                            startindex = k;
                        }
                        if ((textview1.getText().toString()).equalsIgnoreCase(stationData.allStation[k])) {
                            temp1 = k;
                            endindex = k;
                        }
                    }

                    a_map.clear();
                    for (int i = 0; i < stationData.allStation.length; i++) {
                        if ((textview.getText().toString()).equalsIgnoreCase(stationData.allStation[i])) {
                            flyTo(CameraPosition.builder().target(new LatLng(stationData.lat[i], stationData.lon[i])).zoom(11).bearing(0).tilt(45).build());
                            a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[i], stationData.lon[i])).title(stationData.allStation[i]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));

                            index[ind++]=i;
                            j = i;
                            if (temp == temp1 && temp >= 0 && temp1 >= 0) {
                                inter = 0;
                                ind=0;
                                no_station = 0;
                                Toast.makeText(getApplicationContext(), "Both station name are same",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            }
                            if ((temp <= 16 && temp1 <= 16 && temp >= 0 && temp1 >= 0) || (temp > 16 && temp1 > 16 && temp1 < stationData.allStation.length && temp < stationData.allStation.length)) {

                                if (temp <= temp1) {
                                    //first less than second hence go forward//
                                    while (!((textview1.getText().toString()).equalsIgnoreCase(stationData.allStation[j]))) {


                                        a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[j + 1], stationData.lon[j + 1])).title(stationData.allStation[j + 1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                                        a_map.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(stationData.lat[j], stationData.lon[j])).add(new LatLng(stationData.lat[j + 1], stationData.lon[j + 1])));
                                        index[ind++]=j+1;
                                        j++;
                                        no_station++;

                                    }
                                    break;
                                } else {
                                    //first greater than second hence come back//
                                    while (!((textview1.getText().toString()).equalsIgnoreCase(stationData.allStation[j]))) {

                                        a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[j - 1], stationData.lon[j - 1])).title(stationData.allStation[j - 1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                                        a_map.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(stationData.lat[j], stationData.lon[j])).add(new LatLng(stationData.lat[j - 1], stationData.lon[j - 1])));
                                        index[ind++]=j-1;
                                        j--;
                                        no_station++;
                                    }
                                    break;

                                }
                            }
                            if ((temp <= 16 && temp1 > 16 && temp >= 0 && temp1 >= 0) || (temp > 16 && temp1 <= 16 && temp < stationData.allStation.length && temp1 < stationData.allStation.length)) {
                                inter = 1;
                                while (var < 2) {
                                    //var for looping twice [start to inter] && [inter to end]//
                                    if (temp >= 0 && temp <= 7) {
                                          //go till interchange forward //
                                        while (j != 7) {

                                            a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[j + 1], stationData.lon[j + 1])).title(stationData.allStation[j + 1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                                            a_map.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(stationData.lat[j], stationData.lon[j])).add(new LatLng(stationData.lat[j + 1], stationData.lon[j + 1])));
                                            index[ind++]=j+1;
                                            j++;
                                            no_station++;

                                        }


                                    } else if (temp > 7 && temp <= 16) {
                                        //go till interchange backward//
                                        while (j != 7) {

                                            a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[j - 1], stationData.lon[j - 1])).title(stationData.allStation[j - 1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                                            a_map.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(stationData.lat[j], stationData.lon[j])).add(new LatLng(stationData.lat[j - 1], stationData.lon[j - 1])));
                                            index[ind++]=j-1;
                                            j--;
                                            no_station++;

                                        }
                                    } else if (temp > 16 && temp <= 25) {
                                        //go till interchange forward//
                                        while (j != 25) {

                                            a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[j + 1], stationData.lon[j + 1])).title(stationData.allStation[j + 1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                                            a_map.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(stationData.lat[j], stationData.lon[j])).add(new LatLng(stationData.lat[j + 1], stationData.lon[j + 1])));
                                            index[ind++]=j+1;
                                            j++;
                                            no_station++;

                                        }


                                    } else {
                                        //go till interchange backward//
                                        while (j != 25) {

                                            a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[j - 1], stationData.lon[j - 1])).title(stationData.allStation[j - 1]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                                            a_map.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(stationData.lat[j], stationData.lon[j])).add(new LatLng(stationData.lat[j - 1], stationData.lon[j - 1])));
                                            index[ind++]=j-1;
                                            j--;
                                            no_station++;
                                        }

                                    }
                                    //exchange temp and temp1////                                    temp = temp1;
                                    j = temp1;
                                    a_map.addMarker(new MarkerOptions().position(new LatLng(stationData.lat[j], stationData.lon[j])).title(stationData.allStation[j]).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                                    index[ind++]=j;
                                    var++;
                                }
                                break;


                            }
                            if (temp == -1) {
                                Toast.makeText(getApplicationContext(), "Please enter start station",
                                        Toast.LENGTH_SHORT).show();
                                ind=0;
                                break;

                            }
                            if (temp1 == -1) {
                                Toast.makeText(getApplicationContext(), "Please enter end station",
                                        Toast.LENGTH_SHORT).show();
                                ind=0;
                                break;

                            }


                        }
                        if (temp == -1 || temp1 == -1) {
                            Toast.makeText(getApplicationContext(), "Please enter valid station",
                                    Toast.LENGTH_SHORT).show();
                            ind=0;
                            break;

                        }
                    }

                }


            }
        });

        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void moreInformation(View view)
    {
        Intent intent=new Intent(this,Route_Information.class);
        intent.putExtra("message", startindex);
        intent.putExtra("message1", endindex);
        intent.putExtra("message2", no_station);
        intent.putExtra("message3", inter);
        intent.putExtra("message4", ind);
        intent.putExtra("message5", index);
        startActivity(intent);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onMapReady(GoogleMap map)
    {
        mapReady=true;
        a_map=map;
        LatLng nagpur=new LatLng(21.141381,79.082568);
        CameraPosition ngp=CameraPosition.builder().target(nagpur).zoom(10).build();
        a_map.moveCamera(CameraUpdateFactory.newCameraPosition(ngp));
    }

    public void flyTo(CameraPosition target)
    {
        a_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 2000, null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
