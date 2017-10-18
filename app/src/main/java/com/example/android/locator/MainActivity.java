package com.example.android.locator;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class MainActivity extends AppCompatActivity implements LocationListener {
    boolean isNetworkAvailable, isGPSAvailable;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        tv = ( TextView ) findViewById( R.id.textView );
    }
    Location location;
    public void getLocation(View arg) {
        LocationManager lm = ( LocationManager ) getSystemService( LOCATION_SERVICE );
        isGPSAvailable = lm.isProviderEnabled( LocationManager.GPS_PROVIDER );
        isNetworkAvailable = lm.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
        if (!isNetworkAvailable || !isGPSAvailable) {
            Intent intn = new Intent( Settings.ACTION_SETTINGS );
            startActivity( intn );
        } else {
            if (isNetworkAvailable) {
                location = lm.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
                lm.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 1, 5, this );
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Toast.makeText( this, lat + " " + lon, Toast.LENGTH_SHORT ).show();
                    getLocationAddress( lat, lon );
                }
            }
            if (isGPSAvailable) {
                location = lm.getLastKnownLocation( LocationManager.GPS_PROVIDER );
                lm.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1, 5, this );
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Toast.makeText( this, lat + " " + lon, Toast.LENGTH_SHORT ).show();
                    getLocationAddress( lat, lon );
                }
            }
        }
    }

    public void getLocationAddress(double lat, double lon) {
        tv.setText( "" );
        try {
            Geocoder geocoder = new Geocoder( this, Locale.ENGLISH );
            List<Address> list = geocoder.getFromLocation( lat, lon, 1 );
            Address address = list.get( 0 );
            int index = address.getMaxAddressLineIndex();
            for (int i = 0; i < index; i++) {
                tv.append( address.getAddressLine( i ) );
            }
            tv.append( "\n" + address.getSubLocality() );
            tv.append( "\n" + address.getLocality() );
            tv.append( "\n" + address.getCountryName() );
        } catch (Exception e) {
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            getLocationAddress( lat, lon );
        }
    }
        @Override
        public void onStatusChanged (String s,int i, Bundle bundle){

        }

        @Override
        public void onProviderEnabled (String s){

        }

        @Override
        public void onProviderDisabled (String s){

        }
}
