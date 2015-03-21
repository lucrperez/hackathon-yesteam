package yesteam.adoptame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailPetActivity extends ActionBarActivity {

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

        ItemPet pet = (ItemPet) getIntent().getSerializableExtra("pet");

        txtName.setText(pet.getNombre());
        Picasso.with(this).load(pet.getFoto()).into(imgPet);
        txtEspecie.setText("Especie: " + pet.getEspecie());
        txtRaza.setText("Raza: " + pet.getRaza());
        txtSexo.setText("Sexo: " + pet.getSexo());
        txtEdad.setText("Edad: " + pet.getEdad());
        txtTamano.setText("Tamaño: " + pet.getTamano());
        txtColor.setText("Color: " + pet.getColor());
        txtIngreso.setText("Fecha de ingreso: " + pet.getIngreso());

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
    }
}
