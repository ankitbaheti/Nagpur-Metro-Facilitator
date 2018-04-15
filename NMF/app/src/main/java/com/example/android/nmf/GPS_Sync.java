package com.example.android.nmf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GPS_Sync extends ActionBarActivity implements OnMapReadyCallback {

    GoogleMap a_map;
    boolean mapReady=false;
    Double lat=21.141381,lon=79.082568;
    boolean flag=false;
    TextView internet_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps__sync);

        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap map)
    {
           mapReady = true;
           a_map = map;
        karan();
        flyTo(CameraPosition.builder().target(new LatLng(21.141381, 79.082568)).zoom(13).bearing(0).tilt(45).build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    karan();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(flag)
                            a_map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Metro").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)));
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void flyTo(CameraPosition target)
    {
        a_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 2000, null);
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

     private void karan() {

        String tag_string_req = "upload";


        internet_textView=(TextView)findViewById(R.id.internet_message_text_view);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://cgossip.in/karanread.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("asd", "Register Response: " + response.toString());
                internet_textView.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jb= new JSONObject(response);
                    if(lat!=Double.parseDouble(jb.getString("lat"))&&lon!=Double.parseDouble(jb.getString("long"))) {
                        lat = Double.parseDouble(jb.getString("lat"));
                        lon = Double.parseDouble(jb.getString("long"));
                        flag = true;
                    }
                 // Toast.makeText(getApplicationContext(), "Data read are :"+lat+" ,"+lon, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("asda", "Registration Error: " + error.getMessage());
               /* Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
               */
                internet_textView.setVisibility(View.VISIBLE);
                internet_textView.setText("No internet connection");


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Host", "cgossip.in");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
