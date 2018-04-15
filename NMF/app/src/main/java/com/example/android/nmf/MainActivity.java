package com.example.android.nmf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private  Button btn_route,btn_eta,btn_gps,btn_near;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        btn_route = (Button) findViewById(R.id.route);
        btn_eta=(Button)findViewById(R.id.eta);
        btn_gps=(Button) findViewById(R.id.gps_synch);
        btn_near=(Button)findViewById(R.id.station_locator);
    }

    public void find_Route(View view) {
        Intent intent = new Intent(this, activity_route.class);
        startActivity(intent);
    }

    public void find_nearest_Station(View view) {
        Intent intent = new Intent(this, station_loc_activity.class);
        startActivity(intent);
    }

    public void gps_sync(View view) {
        Intent intent = new Intent(this, GPS_Sync.class);
        startActivity(intent);
    }

    public void eta(View view) {
        Intent intent = new Intent(this, ETA.class);
        startActivity(intent);
    }

    public void help(View view) {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
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
        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.menu_route:
                 find_Route(btn_route);
                 return true;
            case R.id.menu_near:
                 find_nearest_Station(btn_near);
                 return true;
            case R.id.menu_gps:
                 gps_sync(btn_gps);
                 return true;
            case R.id.menu_eta:
                 eta(btn_eta);
                 return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
