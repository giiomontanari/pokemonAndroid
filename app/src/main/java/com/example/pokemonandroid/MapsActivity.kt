package com.example.pokemonandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        CheckPermmision();
    }

    var ACESSLOCATION = 123;

    fun CheckPermmision() {
        if (Build.VERSION.SDK_INT>=23) {
            if (ActivityCompat.
                    checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACESSLOCATION)
                return;
            }
        }
        getUserLocation();
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show();
        var myLocation = MyLocationListener();
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation);
        var myThread = MyThread();
        myThread.run();
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            ACESSLOCATION-> {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, "We cannot access to your", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    var location: Location?=null;

    // GET USER LOCATION
    inner class MyLocationListener: LocationListener {

        constructor() {
            location = Location("Start");
            location!!.longitude = 0.0;
            location!!.longitude = 0.0;
        }

        override fun onLocationChanged(p0: Location?) {
            location = p0;
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    inner class MyThread: Thread {
        constructor():super() {}

        override fun run() {
            while (true) {
                try {
                    runOnUiThread {
                        mMap!!.clear();
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Me")
                            .snippet("Here is my location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in São Paulo"))
                            Thread.sleep(1000);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))
                 }

                } catch(ex: Exception) {

                }
            }
        }
    }
}
