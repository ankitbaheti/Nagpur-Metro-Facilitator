package com.example.android.nmf;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class ETA extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private ListView lv;
    ArrayAdapter<String> adapter;
    EditText inputSearch;
    Button btn_cancel, btn_ok;
    TextView txtETA;
    StationData stationData = new StationData();

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    protected LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_et);

        btn_cancel = (Button) findViewById(R.id.cancel_eta_button);
        btn_ok = (Button) findViewById(R.id.ok_eta_button);
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.station_name_list_view, stationData.allStation);
        lv.setAdapter(adapter);

        /*Enabling Search Filter*/
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ETA.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout linearLayoutParent = (LinearLayout) view;

                // Getting the inner Linear Layout
                //LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);

                // Getting the Country TextView
                TextView tvCountry = (TextView) linearLayoutParent.getChildAt(0);

                inputSearch.setText(tvCountry.getText());
            }
        });

        createLocationRequest();
        buildGoogleClient();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText(null);
                lv.setVisibility(View.VISIBLE);
            }
        });


    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    protected synchronized void buildGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()

    {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Google API connection has been Failed" + connectionResult.getErrorCode());
    }

    public void onDisconnected() {
        Log.i(TAG, "Disconnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection has been suspended");
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

        btn_ok.setOnClickListener(new View.OnClickListener() {

            boolean flag = false;

            @Override
            public void onClick(View v) {
                for (int k = 0; k < stationData.allStation.length; k++) {
                    if ((inputSearch.getText().toString()).equalsIgnoreCase(stationData.allStation[k])) {
                        flag = true;

                    }
                }
                if (inputSearch.getText().toString() != null && flag==true) {
                    int temp = 0;
                    for (int k = 0; k < stationData.allStation.length; k++) {
                        if ((inputSearch.getText().toString()).equalsIgnoreCase(stationData.allStation[k])) {
                            temp = k;

                        }

                    }
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + mCurrentLocation.getLatitude() + ","
                            + mCurrentLocation.getLongitude() + "&daddr=" + stationData.lat[temp] + "," + stationData.lon[temp]));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);


                    lv.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Station",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

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
        if (mGoogleApiClient.isConnected()) {
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