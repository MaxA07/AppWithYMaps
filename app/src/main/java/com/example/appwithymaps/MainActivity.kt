package com.example.appwithymaps

import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    lateinit var mapObjects: MapObjectCollection
    lateinit var animationHandler: Handler


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
        mapObjects = mapView.map.mapObjects.addCollection()
        animationHandler = Handler(Looper.getMainLooper())

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
        //.setText(point.latitude.toString() + " " + point.longitude.toString())
        val geo = Geocoder(this)
        val inf = geo.getFromLocation(point.latitude, point.longitude, 1)
        val address = inf[0].getAddressLine(0)
        val pin = mapsObject.addPlacemark(point).setText(address + "" + "\n" + "(${point.latitude} ${point.longitude})" )

    }



    override fun onMapLongTap(map: Map, point: Point) {

    }

}

