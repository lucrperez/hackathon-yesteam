package yesteam.adoptame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DetailPetActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider;
    private ItemPet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView imgPet = (ImageView) findViewById(R.id.imgDetailPet);
        final TextView txtName = (TextView) findViewById(R.id.txtDetailName);
        TextView txtEspecie = (TextView) findViewById(R.id.txtEspecie);
        TextView txtRaza = (TextView) findViewById(R.id.txtRaza);
        TextView txtSexo = (TextView) findViewById(R.id.txtSexo);
        TextView txtEdad = (TextView) findViewById(R.id.txtEdad);
        TextView txtTamano = (TextView) findViewById(R.id.txtTamano);
        TextView txtColor = (TextView) findViewById(R.id.txtColor);
        TextView txtIngreso = (TextView) findViewById(R.id.txtIngreso);
        TextView txtDisponible = (TextView) findViewById(R.id.txtDisponible);
        TextView txtPerdido = (TextView) findViewById(R.id.txtPerdido);

        pet = (ItemPet) getIntent().getSerializableExtra("pet");

        txtName.setText(pet.getNombre());
        Picasso.with(this).load(pet.getFoto()).placeholder(R.drawable.no_photo).into(imgPet);
        txtEspecie.setText("Especie: " + pet.getEspecie());
        txtRaza.setText("Raza: " + pet.getRaza());
        txtSexo.setText("Sexo: " + pet.getSexo());
        txtEdad.setText("Edad: " + pet.getEdad());
        txtTamano.setText("Tamaño: " + pet.getTamano());
        txtColor.setText("Color: " + pet.getColor());
        txtIngreso.setText("Fecha de ingreso: " + pet.getIngreso().substring(0,10));

        switch (pet.getDisponible()) {
            case 1:
                txtDisponible.setText("Mascota en adopción");
                break;

            case 3:
                txtDisponible.setText("Mascota ya adoptada");
                break;

            case 8:
                txtDisponible.setText("Mascota en observación");
                break;
        }

        if (pet.isPerdido()) {
            txtPerdido.setText("Perdido: Sí");
        } else {
            txtPerdido.setText("Perdido: No");
        }

        Button btn_taxiStops = (Button) findViewById(R.id.btn_details_taxiStops);
        Button btn_doghouse = (Button) findViewById(R.id.btn_details_doghouse);

        btn_taxiStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GoToActivity.class);
                startActivity(intent);
            }
        });

        btn_doghouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DoghouseActivity.class);
                startActivity(intent);
            }
        });


        if (pet.getEspecie().equalsIgnoreCase("canina")) {
            txtName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icono_perro), null);

        } else if (pet.getEspecie().equalsIgnoreCase("felina")) {
            txtName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icono_gato), null);

        } else if (pet.getEspecie().equalsIgnoreCase("ave")) {
            txtName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icono_pajaro), null);

        } else {
            txtName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icono_otros), null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_pet, menu);

        // Set up ShareActionProvider's default share intent
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    private Intent getShareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        URL urlFoto = null;
        try {
            URL url = new URL(pet.getFoto());
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            urlFoto = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        intent.putExtra(Intent.EXTRA_TEXT, pet.getNombre() + " busca un hogar en Zaragoza, adóptalo ya! #AdoptaPetZgz " + urlFoto.toString());
        return intent;
    }
}
