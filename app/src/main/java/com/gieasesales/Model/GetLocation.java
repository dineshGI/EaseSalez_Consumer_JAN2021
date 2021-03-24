package com.gieasesales.Model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;

public class GetLocation implements Serializable {


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    LatLng latLng;

}
