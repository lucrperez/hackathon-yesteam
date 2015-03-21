package yesteam.adoptame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class GoToActivity extends FragmentActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.goto_map)).getMap();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            final String msg = getResources().getString(R.string.turn_GPS);
            builder.setMessage(msg)
                    .setPositiveButton(R.string.btn_txt_ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(action));
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(R.string.btn_txt_cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
            builder.create().show();
        }

        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng ll = new LatLng(loc.getLatitude(),loc.getLongitude());
        float zoom = (float) 10.0;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng ll = new LatLng(loc.getLatitude(),loc.getLongitude());
        float zoom = (float) 10.0;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_go_to, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
