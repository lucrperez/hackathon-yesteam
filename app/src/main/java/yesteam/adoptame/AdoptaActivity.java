package yesteam.adoptame;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class AdoptaActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private AdoptaAdapter mAdapter;

    private Spinner spnCategoria, spnEspecie, spnTamano, spnEdad;
    private LinearLayout layFilter;
    private Button btnSearch;

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


        layFilter = (LinearLayout) findViewById(R.id.layFilter);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        spnEspecie = (Spinner) findViewById(R.id.spnEspecie);
        ArrayAdapter<CharSequence> adapterEspecie = ArrayAdapter.createFromResource(this, R.array.array_especie, android.R.layout.simple_spinner_item);
        adapterEspecie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEspecie.setAdapter(adapterEspecie);

        spnTamano = (Spinner) findViewById(R.id.spnTamano);
        ArrayAdapter<CharSequence> adapterTamano = ArrayAdapter.createFromResource(this, R.array.array_tamano, android.R.layout.simple_spinner_item);
        adapterTamano.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTamano.setAdapter(adapterTamano);

        spnEdad = (Spinner) findViewById(R.id.spnEdad);
        ArrayAdapter<CharSequence> adapterEdad = ArrayAdapter.createFromResource(this, R.array.array_edad, android.R.layout.simple_spinner_item);
        adapterEdad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEdad.setAdapter(adapterEdad);


        new DownloadPets().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_adopta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_lost_found:
                startActivity(new Intent(AdoptaActivity.this, LostFoundActivity.class));
                return true;

            case R.id.action_filter:
                if (layFilter.getVisibility() == View.GONE) {
                    layFilter.setVisibility(View.VISIBLE);
                } else {
                    layFilter.setVisibility(View.GONE);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnCategoria:
                new DownloadPets().execute();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                new DownloadPets().execute();
                layFilter.setVisibility(View.GONE);
                break;
        }
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

                switch (spnEspecie.getSelectedItemPosition()) {
                    case 1:
                        where += ";especie==Canina";
                        break;

                    case 2:
                        where += ";especie==Felina";
                        break;

                    case 3:
                        where += ";especie==Ave";
                        break;

                    case 4:
                        where += ";especie==Otros";
                        break;
                }

                switch (spnEdad.getSelectedItemPosition()) {
                    case 1:
                        where += ";edad==Cachorro%20(0-3%20meses)";
                        break;

                    case 2:
                        where += ";edad==joven%20(3-12%20meses)";
                        break;

                    case 3:
                        where += ";edad==Adulto%20(1-5%20a%F1os)";
                        break;

                    case 4:
                        where += ";edad==Mayor%20(%3E%205%20a%F1os)";
                        break;
                }

                switch (spnTamano.getSelectedItemPosition()) {
                    case 1:
                        where += ";tamagno==Peque%F1o%20(%3C%2010%20Kg)";
                        break;

                    case 2:
                        where += ";tamagno==Mediano%20(11-25%20kg)";
                        break;

                    case 3:
                        where += ";tamagno==Grande%20(26-44%20kg)";
                        break;

                    case 4:
                        where += ";tamagno==Gigante%20(>%2045%20Kg)";
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

                    if (obj.has("id"))
                        pet.setId(obj.getInt("id"));

                    if (obj.has("ficha"))
                        pet.setFicha(obj.getString("ficha"));

                    if (obj.has("raza"))
                        pet.setRaza(obj.getString("raza"));

                    if (obj.has("fechaIngreso"))
                        pet.setIngreso(obj.getString("fechaIngreso"));

                    if (obj.has("sexo"))
                        pet.setSexo(obj.getString("sexo"));

                    if (obj.has("edad"))
                        pet.setEdad(obj.getString("edad"));

                    if (obj.has("tamagno"))
                        pet.setTamano(obj.getString("tamagno"));

                    if (obj.has("foto"))
                        pet.setFoto("http:" + obj.getString("foto"));

                    if (obj.has("nombre"))
                        pet.setNombre(obj.getString("nombre"));

                    if (obj.has("especie"))
                        pet.setEspecie(obj.getString("especie"));

                    if (obj.has("color"))
                        pet.setColor(obj.getString("color"));

                    if (obj.has("perdido"))
                        pet.setPerdido(obj.getBoolean("perdido"));

                    if (obj.has("disponible"))
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
