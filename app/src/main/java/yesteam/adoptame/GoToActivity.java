package yesteam.adoptame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class GoToActivity extends FragmentActivity {

    private GoogleMap map;

    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.goto_map)).getMap();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocList);
        //Location l = mLocList.getLocation();
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        map.setMyLocationEnabled(true);
        //loc.getAccuracy();
        //lm.removeUpdates(mLocList);
        LatLng ll = new LatLng(loc.getLatitude(),loc.getLongitude());
        float zoom = (float) 10.0;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));
        new DownloadParadasTaxis().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    private class DownloadParadasTaxis extends AsyncTask<Void, Void, ArrayList<LatLng>> {

        @Override
        protected ArrayList<LatLng> doInBackground(Void... params) {

             String response = null;

            try {
                String charset = "UTF-8";
                String param1 = "SELECT * from paradasTaxi";
                URLConnection conn = new URL("https://iescities.com:443/IESCities/api/data/query/225/sql").openConnection();
                //conn.setReadTimeout(10000);
                //conn.setConnectTimeout(15000);
                //conn.setRequestMethod("POST");
                //conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setRequestProperty("Content-Type", "text/plain");

                OutputStream output = conn.getOutputStream();
                output.write(param1.getBytes());


                conn.connect();
                InputStream is = conn.getInputStream();

                response = readIt(is);

                is.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json = new JSONObject(String.valueOf(response));
                int total = json.getInt("count");

                JSONArray array = json.getJSONArray("rows");

                ArrayList<LatLng> items = new ArrayList<LatLng>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    try {
                        items.add(new LatLng(obj.getDouble("lat"),obj.getDouble("lng")));

                    } catch (JSONException e) {

                    }
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
                sb.append(line + "n");
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> Listll) {
            super.onPostExecute(Listll);

            for (LatLng ll : Listll)
            {
                map.addMarker(new MarkerOptions()
                .position(ll)
                .draggable(false)
                .visible(true));
            }
        }
    }

}
