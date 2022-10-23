package com.example.polygonmap

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.polygonmap.R
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.model.*
import java.util.ArrayList

class MainActivity2_2 : AppCompatActivity(), OnMapReadyCallback {
    var supportMapFragment: SupportMapFragment? = null
    var gMap: GoogleMap? = null
    var clear: Button? = null
    var save: Button? = null
    var polygon: Polygon? = null

    var latlngList = ArrayList<LatLng>()
    var markerList = ArrayList<Marker?>()

    var prettyGson = GsonBuilder().setPrettyPrinting().create()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)

        clear = findViewById(R.id.btn_clear_polygon)
        save = findViewById(R.id.btn_save_json)

        clear?.setOnClickListener(View.OnClickListener { view: View? ->
            if (polygon != null) {
                polygon!!.remove()
            }
            for (marker in markerList) marker!!.remove()
            latlngList.clear()
            markerList.clear()
        })
        save?.setOnClickListener(View.OnClickListener { view: View? ->
            if (prettyGson != null) {
                Toast.makeText(
                    applicationContext,
                    "Este es el proyecto Kotlin",
                    Toast.LENGTH_LONG
                ).show()
            } else {
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(29.324521, -100.958864),
                14f
            )
        )
        gMap = googleMap
        gMap!!.setOnMapLongClickListener { latLng: LatLng ->
            val markerOptions = MarkerOptions().position(latLng)
            val marker = gMap!!.addMarker(markerOptions)
            latlngList.add(latLng)
            markerList.add(marker)
            if (polygon != null) {
                polygon!!.remove()
            }
            val polygonOptions = PolygonOptions().addAll(latlngList).clickable(true)
            polygon = gMap!!.addPolygon(polygonOptions)
            polygon!!.fillColor = Color.RED
            polygon!!.strokeColor = Color.BLACK

//            GsonBuilder gsonBuilder = new GsonBuilder();
//            Gson gson = gsonBuilder.create();
//            String JSONObject = gson.toJson(latlngList);
//            System.out.println(JSONObject);
            val prettyJson = prettyGson!!.toJson(latlngList)
            println(prettyJson)
        }
    }
}