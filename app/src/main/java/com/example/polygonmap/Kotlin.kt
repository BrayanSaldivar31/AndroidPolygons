package com.example.polygonmap

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.gson.GsonBuilder
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import java.util.ArrayList

class Kotlin : AppCompatActivity(), OnMapReadyCallback {

    var supportMapFragment: SupportMapFragment? = null
    var gMap: GoogleMap? = null
    var clear: Button? = null
    var save: Button? = null
    var polygon: Polygon? = null
    var latlngList: ArrayList<LatLng>? = ArrayList()
    var markerList = ArrayList<Marker?>()
    var prettyGson: Gson = GsonBuilder().setPrettyPrinting().create()
//    private val queue: RequestQueue? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?

        assert(supportMapFragment != null)
        supportMapFragment!!.getMapAsync(this)

        clear = findViewById(R.id.btn_clear_polygon)
        save = findViewById(R.id.btn_save_json)

        clear?.setOnClickListener {
            if (polygon != null) {
                polygon!!.remove()
            }
            for (marker in markerList) marker!!.remove()
            latlngList!!.clear()
            markerList.clear()
        }
        save?.setOnClickListener {
            if (latlngList != null) {
                val firstCoor = latlngList!![0]
                latlngList!!.add(firstCoor)
                //                System.out.println(latlngList);
                val prettyJson = prettyGson.toJson(latlngList)
                println(prettyJson)
                SweetAlertDialog(applicationContext)
                    .setTitleText("Poligono Guardado")
                    .show()
            }
        }

//        queue = Volley.newRequestQueue(this);
    }

    //    private void metodoPost(){
    //        String url = "Aqui va la URL";
    //
    //        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
    //            @Override
    //            public void onResponse(JSONObject response) {
    //                try {
    //                    JSONArray mJsonArray = response.getJSONArray(String.valueOf(prettyGson));
    //
    //                    Toast.makeText(MainActivity2.this, "Gson Enviado: " + mJsonArray, Toast.LENGTH_LONG).show();
    //
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }, new Response.ErrorListener() {
    //            @Override
    //            public void onErrorResponse(VolleyError error) {
    //
    //            }
    //        });
    //
    //        queue.add(request);
    //    }
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
            latlngList!!.add(latLng)
            markerList.add(marker)
            if (polygon != null) {
                polygon!!.remove()
            }
            val polygonOptions = PolygonOptions().addAll(latlngList!!).clickable(true)
            polygon = gMap!!.addPolygon(polygonOptions)
            polygon!!.fillColor = Color.RED
            polygon!!.strokeColor = Color.BLACK
        }
    }
}