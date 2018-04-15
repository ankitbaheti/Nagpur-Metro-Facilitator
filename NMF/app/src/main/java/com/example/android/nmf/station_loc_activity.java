package com.example.android.nmf;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class station_loc_activity extends ActionBarActivity implements  GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    SeekBar seekBar1;
    TextView textView_distance,station_text_view1,text_view_2,showTextView;


    protected LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;

        double user_latitude=0.0,user_longitude=0.0;
        float[] results = new float[1];
        int i=0,j=1,t,temp1;
        boolean mRequestingLocationUpdates;
        float temp;
        StationData stationData=new StationData();

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    ArrayAdapter<String> adapter;

    private ListView lv2;


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_loc_activity);

        seekBar1 = (SeekBar) findViewById(R.id.seek_bar_1);
        textView_distance = (TextView) findViewById(R.id.distance_text_view);
        station_text_view1 = (TextView) findViewById(R.id.station_text_view);
        text_view_2=(TextView)findViewById(R.id.stationDistance_text_view);
        showTextView = (TextView) findViewById(R.id.show_distance_text_view);

        createLocationRequest();
        buildGoogleClient();

        }

protected void startLocationUpdates() {
       /* LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);*/
         PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
        }
protected synchronized void buildGoogleClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
        }
@Override
protected void onStart()
        {
        super.onStart();
        mGoogleApiClient.connect();
        }
@Override
protected void onStop()

        {
        super.onStop();
        if(mGoogleApiClient.isConnected())
        {
        mGoogleApiClient.disconnect();
        }
        }
@Override
public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Google API connection has been Failed" + connectionResult.getErrorCode());
}

public void onDisconnected(){
        Log.i(TAG, "Disconnected");
        }
@Override
public void onConnectionSuspended(int i) {
        Log.i(TAG,"Connection has been suspended");
        mGoogleApiClient.connect();
        }
@Override
public void onConnected(Bundle connectionHint) {

        startLocationUpdates();

}

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());
        mCurrentLocation = location;
        final int[] progress1 = {0};
        user_latitude=location.getLatitude();
        user_longitude=location.getLongitude();

        if (mCurrentLocation!=null) {
        if (progress1[0] !=0)
        createAndUpdateUI(progress1[0]);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            progress1[0] = progress;
            createAndUpdateUI(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT);
        }
        });

        }
        else
        {
        Toast.makeText(getApplicationContext(), "waiting for location", Toast.LENGTH_SHORT);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Toast.makeText(getApplicationContext(), "waiting for location", Toast.LENGTH_SHORT);
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT);
        }
        });

        }

        }

protected void createAndUpdateUI(int progress){
        float TemporaryDistance[]=new float[stationData.allStation.length];
        int  TemporaryStation[]=new int[stationData.allStation.length];

        textView_distance.setText(progress + "/" + seekBar1.getMax());
        showTextView.setText(progress*200+" m");
      //  station_text_view1.setText("NEAREST STATION\n");
      //  text_view_2.setText("  DISTANCE(m)\n");
        j = 0;
        t=0;
        for (i = 0; i < stationData.allStation.length; i++) {
        Location.distanceBetween(stationData.lat[i],stationData.lon[i],
        user_latitude, user_longitude,
        results);
        if (results[0] <= (float) (progress * 200)) {
        t++;
        TemporaryStation[j] = i;
        TemporaryDistance[j] = results[0];

        j++;
        }


        }

        for (i = 0; i < t; i++) {
        for (int k = i; k < t; k++) {
        if (TemporaryDistance[i] > TemporaryDistance[k]) {
        temp = TemporaryDistance[i];
        TemporaryDistance[i] = TemporaryDistance[k];
        TemporaryDistance[k] = temp;
        temp1 = TemporaryStation[i];
        TemporaryStation[i] = TemporaryStation[k];
        TemporaryStation[k] = temp1;
        }
        }
        }

        String s;
        String stationName[] = new String[t];
        String  distance[] = new String[t];
        for (i = 0; i < t; i++) {
        if(TemporaryDistance[i]!=0.0) {
            s = String.format("%.3f", TemporaryDistance[i]);
            stationName[i]= stationData.allStation[TemporaryStation[i]];
            distance[i] = s;
        /*
        station_text_view1.setText(station_text_view1.getText()+"\n"+ stationData.allStation[TemporaryStation[i]]);
        text_view_2.setText(text_view_2.getText()+"\n"+s);*/
        }
        }

        CustomListAdapter adapter=new CustomListAdapter(this, stationName, distance);
        lv2=(ListView)findViewById(R.id.list_view_nearest_station);
        lv2.setAdapter(adapter);


}

@Override
protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        }

protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
        mGoogleApiClient, this);
        }

@Override
public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
        startLocationUpdates();
        }
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