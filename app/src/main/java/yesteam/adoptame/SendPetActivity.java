package yesteam.adoptame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SendPetActivity extends ActionBarActivity implements View.OnClickListener, GoogleMap.OnMapClickListener {

    private int type = 0;
    private double latitude = 41.6531717f;
    private double longitude = -0.9037133f;

    private GoogleMap map;
    private EditText editText;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pet);

        editText = (EditText) findViewById(R.id.editText);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        type = getIntent().getIntExtra("type", 0);

        if (type == 0) {
            getSupportActionBar().setTitle("Mascota perdida");
        } else {
            getSupportActionBar().setTitle("Mascota encontrada");
        }

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.sendPet_map)).getMap();
        float zoom = (float) 12.0;
        LatLng ll = new LatLng(41.6531717, -0.9037133);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));

        map.setOnMapClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new UploadPet().execute();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;

        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()));
    }

    private class UploadPet extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            InputStream is = null;

            try {
                String paramsUrl = "?text=" + editText.getText().toString() + "&latitude=" + latitude + "&longitude=" + longitude + "&type=" + type;

                URL url = new URL("http://base.kix2902.es/zgzappstore/upload.php" + paramsUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                is = conn.getInputStream();
                is.read();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(SendPetActivity.this, "Aviso guardado correctamente", Toast.LENGTH_SHORT).show();
            SendPetActivity.this.finish();
        }
    }

}