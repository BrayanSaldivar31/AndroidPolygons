package com.example.polygonmap;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    GoogleMap gMap;
    Button clear, save;

    Polygon polygon = null;
    ArrayList<LatLng> latlngList = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();

    Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    private RequestQueue queue;


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
            }
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
}
