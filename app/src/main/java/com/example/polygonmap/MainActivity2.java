package com.example.polygonmap;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
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
            if (prettyGson != null){
                Toast.makeText(getApplicationContext(), "No se puede guardar un poligono vacio", Toast.LENGTH_LONG).show();
            }else {
                
            }
        });

    }

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

//            GsonBuilder gsonBuilder = new GsonBuilder();
//            Gson gson = gsonBuilder.create();
//            String JSONObject = gson.toJson(latlngList);
//            System.out.println(JSONObject);

            String prettyJson = prettyGson.toJson(latlngList);
            System.out.println(prettyJson);
        });
    }

}
