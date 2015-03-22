package yesteam.adoptame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LostFoundActivity extends ActionBarActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.lostPet_map)).getMap();
        float zoom = (float) 12.0;
        LatLng ll = new LatLng(41.6531717, -0.9037133);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));

        Button btn_lost = (Button) findViewById(R.id.btn_lostPet);
        Button btn_found = (Button) findViewById(R.id.btn_foundPet);

        btn_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SendPetActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        btn_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SendPetActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lostFound_explication);
        builder.setPositiveButton(R.string.btn_txt_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new DownloadPets().execute();
    }

    private class DownloadPets extends AsyncTask<Void, Void, ArrayList<GeoPet>> {

        @Override
        protected ArrayList<GeoPet> doInBackground(Void... params) {

            InputStream is = null;
            String response = "";

            try {
                URL url = new URL("http://base.kix2902.es/zgzappstore/query.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                is = conn.getInputStream();

                response = readIt(is);

                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray array = new JSONArray(response);

                ArrayList<GeoPet> items = new ArrayList<GeoPet>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    GeoPet pet = new GeoPet();

                    if (obj.has("id"))
                        pet.setId(Integer.valueOf(obj.getString("id")));

                    if (obj.has("text"))
                        pet.setText(obj.getString("text"));

                    if (obj.has("latitude"))
                        pet.setLatitude(Float.valueOf(obj.getString("latitude")));

                    if (obj.has("longitude"))
                        pet.setLongitude(Float.valueOf(obj.getString("longitude")));

                    if (obj.has("type"))
                        pet.setType(Integer.valueOf(obj.getString("type")));

                    items.add(pet);
                }

                return items;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public String readIt(InputStream stream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(ArrayList<GeoPet> itemPets) {
            super.onPostExecute(itemPets);

            for (GeoPet pet : itemPets) {
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(pet.getLatitude(), pet.getLongitude()))
                        .draggable(false)
                        .title(pet.getText())
                        .visible(true);

                if (pet.getType() == 0) {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }

                map.addMarker(marker);
            }
        }
    }
}
