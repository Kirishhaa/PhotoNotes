package com.kirishhaa.photonotes.data.repos.location

import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.domain.location.LocationRepository
import com.kirishhaa.photonotes.extensions.coroutineTryCatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import java.util.Locale

class LocationRepository2(
    private val context: Context
): LocationRepository {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocation(): DomainLocation? {
        val flow = MutableSharedFlow<DomainLocation?>(replay = 1)
        val a = LocationServices.getFusedLocationProviderClient(context)
            a.lastLocation
                .addOnFailureListener {
                    it.printStackTrace()
                    flow.tryEmit(null)
                }
                .addOnSuccessListener {
            if (it == null) {
                flow.tryEmit(null)
            } else {
                val geocoder = Geocoder(context, Locale.getDefault())
                var adresses: List<Address> = emptyList()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        it.latitude,
                        it.longitude,
                        1
                    ) { adresses = it }
                } else {
                    try {
                        adresses = geocoder.getFromLocation(
                            it.latitude,
                            it.longitude,
                            1
                        ) ?: emptyList()
                    }catch (e: Exception) {
                        if(e is CancellationException) throw e
                    }
                }
                val country = adresses.getOrNull(0)?.countryName ?: DomainLocation.UNKNOWN
                val town = adresses.getOrNull(0)?.locality ?: DomainLocation.UNKNOWN
                flow.tryEmit(DomainLocation(it.latitude, it.longitude, country, town))
            }
        }
        return flow.first()
    }
}