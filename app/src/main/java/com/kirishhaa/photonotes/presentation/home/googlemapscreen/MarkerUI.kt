package com.kirishhaa.photonotes.presentation.home.googlemapscreen

import com.google.android.gms.maps.model.BitmapDescriptor

class MarkerUI(
    val id: Int,
    val image: BitmapDescriptor?,
    val latitude: Double,
    val longitude: Double,
)