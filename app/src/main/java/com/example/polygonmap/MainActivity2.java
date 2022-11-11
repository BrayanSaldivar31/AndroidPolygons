package com.example.polygonmap;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.polygonmap.Utility.NetworkChangerListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    GoogleMap gMap;
    Button clear, save;

    Polygon polygon = null;
    ArrayList<LatLng> latlngList = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();

    Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    private RequestQueue queue;
    NetworkChangerListener networkChangerListener = new NetworkChangerListener();


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        clear = findViewById(R.id.btn_clear_polygon);
        save = findViewById(R.id.btn_save_json);

        clear.setOnClickListener(view -> {
            if (polygon != null){
                polygon.remove();
            }
            for (Marker marker : markerList)marker.remove();
            latlngList.clear();
            markerList.clear();
        });

        save.setOnClickListener(view -> {
            if (latlngList != null){
                LatLng firstCoor = latlngList.get(0);
                latlngList.add(firstCoor);
//                System.out.println(latlngList);
                String prettyJson = prettyGson.toJson(latlngList);
                System.out.println(prettyJson);

                polygon.remove();
                for (Marker marker : markerList)marker.remove();

            }
            new SweetAlertDialog(this).setTitleText("Poligono Guardado").show();

//            metodoPost();
        });

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
//                    Toast.makeText(MainActivity2.this, "Predio Guardado: " + mJsonArray, Toast.LENGTH_LONG).show();
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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.324521, -100.958864),
                14f
        ));
        gMap = googleMap;

        gMap.setOnMapLongClickListener(latLng -> {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            Marker marker = gMap.addMarker(markerOptions);
            latlngList.add(latLng);
            markerList.add(marker);

            if (polygon != null) {
                polygon.remove();
            }
            PolygonOptions polygonOptions = new PolygonOptions().addAll(latlngList).clickable(true);
            polygon = gMap.addPolygon(polygonOptions);

            polygon.setFillColor(Color.RED);
            polygon.setStrokeColor(Color.BLACK);
        });
    }

    protected void onStart(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangerListener, filter);
        super.onStart();
    }

    protected void onStop(){
        unregisterReceiver(networkChangerListener);
        super.onStop();
    }

}