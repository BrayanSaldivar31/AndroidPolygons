package com.example.polygonmap

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val polyline = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-35.016, 143.321),
                    LatLng(-34.747, 145.592),
                    LatLng(-34.364, 147.891),
                    LatLng(-33.501, 150.217),
                    LatLng(-32.306, 149.248),
                    LatLng(-32.491, 147.309),
                )
        )

        val polygon1 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(29.326241, -100.949772),
                    LatLng(29.322325, -100.954870),
                    LatLng(29.322166, -100.948740),
                    LatLng(29.324763, -100.949577)
                )
        )
        polygon1.tag = "alpha"

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(29.324521, -100.958864),
                12f
            ))
        googleMap.setOnPolygonClickListener(this)
        googleMap.setOnPolygonClickListener(this)
    }

    override fun onPolylineClick(p0: Polyline) {
        TODO("Not yet implemented")
    }

    private val COLOR_BLACK_ARGB = -0x1000000
    private val POLYLINE_STOKE_WIDTH_PX = 12

    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

    override fun onPolygonClick(polygon: Polygon) {
        var color = polygon.strokeColor xor 0x00ffffff
        polygon.strokeColor = color
        color = polygon.fillColor xor 0x00ffffff
        polygon.fillColor = color
        Toast.makeText(this, "Are Type ${polygon.tag?.toString()}", Toast.LENGTH_SHORT).show()
    }

    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_DARK_GREEN_ARGB = -0xc771c4
    private val COLOR_LIGHT_GREEN_ARGB = -0x7e387c
    private val COLOR_DARK_ORANGE_ARGB = -0xa80e9
    private val COLOR_LIGHT_ORANGE_ARGB = -0x657db
    private val POLYGON_STROKE_WIDTH_PX = 8
    private val PATTERN_DASH_LENGTH_PX = 20

    private val DASH : PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())

    private val PATTER_POLYGON_ALPHA = listOf(GAP, DASH)

    private val PATTER_POLYGON_BETA = listOf(DOT, GAP, DASH, GAP)

    private fun stylePolygon(polygon: Polygon){
        val type = polygon.tag?.toString() ?: ""
        var pattern: List<PatternItem>? = null
        var strokeColor = COLOR_BLACK_ARGB
        var fillColor = COLOR_WHITE_ARGB
        when (type){
            "alpha" -> {
                pattern = PATTER_POLYGON_ALPHA
                strokeColor = COLOR_DARK_GREEN_ARGB
                fillColor = COLOR_LIGHT_GREEN_ARGB
            }
            "beta" -> {
                pattern = PATTER_POLYGON_BETA
                strokeColor = COLOR_DARK_ORANGE_ARGB
                fillColor = COLOR_LIGHT_ORANGE_ARGB
            }
        }
        polygon.strokePattern = pattern
        polygon.strokeWidth = POLYGON_STROKE_WIDTH_PX.toFloat()
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }
}