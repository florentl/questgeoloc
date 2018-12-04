package fr.wcs.geolocalisation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocationManager mLocationManager = null;
    private LocationListener locationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mLocationManager != null && locationListener != null)
            mLocationManager.removeUpdates( locationListener );
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );

        String myProvider = LocationManager.PASSIVE_PROVIDER;
        if(isGpsEnabled) {
            myProvider = LocationManager.GPS_PROVIDER;
        } else if(isNetworkEnabled) {
            myProvider = LocationManager.NETWORK_PROVIDER;
        }
        final String providerEnabled = myProvider;
        Toast.makeText( getApplicationContext(), "INIT LOCATION WITH " + myProvider + " PROVIDER", Toast.LENGTH_SHORT ).show();

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // TODO : effectuer une action ici !
                String locationStr = mLocationManager.getLastKnownLocation( providerEnabled ).toString();
                Toast.makeText( getApplicationContext(), locationStr, Toast.LENGTH_SHORT ).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        mLocationManager.requestLocationUpdates( providerEnabled, 0, 0, locationListener);
    }

    private void checkPermission() {

        // vérification de l'autorisation d'accéder à la position GPS
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // l'autorisation n'est pas acceptée
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // l'autorisation a été refusée précédemment, on peut prévenir l'utilisateur ici
                Toast.makeText( getApplicationContext(), "GPS access denied", Toast.LENGTH_LONG ).show();
            } else {
                // l'autorisation n'a jamais été réclamée, on la demande à l'utilisateur
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);
            }
        } else {
            // TODO : autorisation déjà acceptée, on peut faire une action ici
            initLocation();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // cas de notre demande d'autorisation
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO : l'autorisation a été donnée, nous pouvons agir
                    initLocation();
                } else {
                    Toast.makeText( getApplicationContext(), "GPS access denied", Toast.LENGTH_LONG ).show();
                    // l'autorisation a été refusée :(
                }
                return;
            }
        }
    }

}
