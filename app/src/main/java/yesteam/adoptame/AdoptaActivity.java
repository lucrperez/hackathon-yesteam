package yesteam.adoptame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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

public class AdoptaActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.column_count));
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdoptaAdapter(AdoptaActivity.this, new ArrayList<ItemPet>());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new DownloadPets().execute();
    }

    private class DownloadPets extends AsyncTask<Void, Void, ArrayList<ItemPet>> {

        @Override
        protected ArrayList<ItemPet> doInBackground(Void... params) {

            InputStream is = null;
            String response = "";

            try {
                URL url = new URL("http://www.zaragoza.es/api/recurso/medio-ambiente/animal-en-adopcion.json");
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
                JSONObject json = new JSONObject(response);
                int total = json.getInt("totalCount");

                JSONArray array = json.getJSONArray("result");

                ArrayList<ItemPet> items = new ArrayList<ItemPet>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    try {
                        ItemPet pet = new ItemPet();

                        pet.setId(obj.getInt("id"));
                        pet.setFicha(obj.getString("ficha"));
                        pet.setRaza(obj.getString("raza"));
                        pet.setIngreso(obj.getString("fechaIngreso"));
                        pet.setSexo(obj.getString("sexo"));
                        pet.setEdad(obj.getString("edad"));
                        pet.setTamano(obj.getString("tamagno"));
                        pet.setFoto("http:" + obj.getString("foto"));
                        pet.setNombre(obj.getString("nombre"));
                        pet.setEspecie(obj.getString("especie"));
                        pet.setColor(obj.getString("color"));
                        pet.setPerdido(obj.getBoolean("perdido"));
                        pet.setDisponible(Integer.valueOf(obj.getString("disponible")));

                        items.add(pet);

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
        protected void onPostExecute(ArrayList<ItemPet> itemPets) {
            super.onPostExecute(itemPets);

            mAdapter = new AdoptaAdapter(AdoptaActivity.this, itemPets);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

}
