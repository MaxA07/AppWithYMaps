package com.example.appwithymaps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map


class MainActivity : AppCompatActivity(), GeoObjectTapListener, InputListener {

    lateinit var mapView: MapView
    lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("2bdc136b-2fd9-453d-ad00-1d3efa4b23e0")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        getLocationPermission()

        mapView = findViewById(R.id.mapView)
        mapView.map.move(
            CameraPosition(Point(55.682586, 37.546886), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 2f), null
        )

        mapView.map.addTapListener(this)
        mapView.map.addInputListener(this)

    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onObjectTap(geoObjectTapEvent: GeoObjectTapEvent): Boolean {
        val selectionMetadata = geoObjectTapEvent
            .geoObject
            .metadataContainer
            .getItem(GeoObjectSelectionMetadata::class.java)
        if (selectionMetadata != null) {
            mapView.map.selectGeoObject(selectionMetadata.id, selectionMetadata.layerId)
        }
        return selectionMetadata != null
    }

    override fun onMapTap(map: Map, point: Point) {
        mapView.map.deselectGeoObject()

        val mapsObject = mapView.map.mapObjects
        val geo = Geocoder(this)
        val inf = geo.getFromLocation(point.latitude, point.longitude, 1)
        address = inf[0].getAddressLine(0) + "" + "\n" + "(${point.latitude} ${point.longitude})"
        val pin = mapsObject.addPlacemark(point).setText(address)

        findViewById<Button>(R.id.confirm_button).setOnClickListener {
            dataTransmission(address)
        }
    }

    override fun onMapLongTap(map: Map, point: Point) {
    }

    private fun dataTransmission(data: String) {
        val intent2 = Intent(this, StartActivity::class.java)
        intent2.putExtra("key", data)
        startActivity(intent2)
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions,0)
        }
    }


}

