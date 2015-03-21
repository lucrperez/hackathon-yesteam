package yesteam.adoptame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class LostFoundActivity extends FragmentActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.lostPet_map)).getMap();
        float zoom = (float) 10.0;
        LatLng ll = new LatLng(40.41,-3.73);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,zoom));

        Button btn_lost = (Button) findViewById(R.id.btn_lostPet);
        Button btn_found = (Button) findViewById(R.id.btn_foundPet);

        btn_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LostPetActivity.class);
                startActivity(intent);
            }
        });
        
        btn_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), FoundPetActivity.class);
                startActivity(intent);

            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lost_pet, menu);
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
