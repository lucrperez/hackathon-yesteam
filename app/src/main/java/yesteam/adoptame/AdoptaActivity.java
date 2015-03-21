package yesteam.adoptame;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

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

public class AdoptaActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView mRecyclerView;
    private AdoptaAdapter mAdapter;

    private Spinner spnCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String[] categorias = new String[]{"En adopción", "Adoptados", "En observación"};
        SpinnerCategoriaAdapter spinnerAdapter = new SpinnerCategoriaAdapter(this, categorias);

        spnCategoria = (Spinner) toolbar.findViewById(R.id.spnCategoria);
        spnCategoria.setAdapter(spinnerAdapter);
        spnCategoria.setOnItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        ItemPet pet = mAdapter.getItem(position);

                        Intent intent = new Intent(AdoptaActivity.this, DetailPetActivity.class);
                        intent.putExtra("pet", pet);
                        startActivity(intent);
                    }
                }));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.column_count));
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdoptaAdapter(AdoptaActivity.this, new ArrayList<ItemPet>());
        mRecyclerView.setAdapter(mAdapter);

        new DownloadPets().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        new DownloadPets().execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class DownloadPets extends AsyncTask<Void, Void, ArrayList<ItemPet>> {

        @Override
        protected ArrayList<ItemPet> doInBackground(Void... params) {

            InputStream is = null;
            String response = "";

            try {
                String where = "?q=disponible==";

                switch (spnCategoria.getSelectedItemPosition()) {
                    case 0:
                        where += "1";
                        break;

                    case 1:
                        where += "3";
                        break;

                    case 2:
                        where += "8";
                        break;
                }

                URL url = new URL("http://www.zaragoza.es/api/recurso/medio-ambiente/animal-en-adopcion.json" + where);
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

                        ItemPet pet = new ItemPet();

                    if(obj.has("id"))
                        pet.setId(obj.getInt("id"));

                    if(obj.has("ficha"))
                        pet.setFicha(obj.getString("ficha"));

                    if(obj.has("raza"))
                        pet.setRaza(obj.getString("raza"));

                    if(obj.has("fechaIngreso"))
                        pet.setIngreso(obj.getString("fechaIngreso"));

                    if(obj.has("sexo"))
                        pet.setSexo(obj.getString("sexo"));

                    if(obj.has("edad"))
                        pet.setEdad(obj.getString("edad"));

                    if(obj.has("tamagno"))
                        pet.setTamano(obj.getString("tamagno"));

                    if(obj.has("foto"))
                        pet.setFoto("http:" + obj.getString("foto"));

                    if(obj.has("nombre"))
                        pet.setNombre(obj.getString("nombre"));

                    if(obj.has("especie"))
                        pet.setEspecie(obj.getString("especie"));

                    if(obj.has("color"))
                        pet.setColor(obj.getString("color"));

                    if(obj.has("perdido"))
                        pet.setPerdido(obj.getBoolean("perdido"));

                    if(obj.has("disponible"))
                        pet.setDisponible(Integer.valueOf(obj.getString("disponible")));

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
        protected void onPostExecute(ArrayList<ItemPet> itemPets) {
            super.onPostExecute(itemPets);

            mAdapter = new AdoptaAdapter(AdoptaActivity.this, itemPets);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

}
