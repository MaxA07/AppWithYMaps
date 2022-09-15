package com.example.appwithymaps

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geo
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.layers.GeoObjectTapListener

import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider

class MainActivity : AppCompatActivity(), GeoObjectTapListener, InputListener {

    lateinit var mapView: MapView
    lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("2bdc136b-2fd9-453d-ad00-1d3efa4b23e0")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView.map.move(
            CameraPosition(Point(56.305326, 43.996373), 11.0f, 0.0f, 0.0f),
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

}

