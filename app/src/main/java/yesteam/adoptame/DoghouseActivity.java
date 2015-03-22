package yesteam.adoptame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class DoghouseActivity extends ActionBarActivity {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doghouse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.doghouse_map)).getMap();
        float zoom = (float) 12.0;
        LatLng perrera = new LatLng(41.7575551, -0.797337);
        map.addMarker(new MarkerOptions()
                .position(perrera)
                .draggable(false)
                .visible(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Centro de Protección Animal")
                .snippet("Carretera de Montañana a Peñaflor km 9,4")).showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(perrera, zoom));
    }
}
