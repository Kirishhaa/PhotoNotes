package com.kirishhaa.photonotes.domain.location

import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import com.kirishhaa.photonotes.domain.DomainLocation
import com.kirishhaa.photonotes.extensions.coroutineTryCatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.util.Locale

interface LocationRepository {

    suspend fun getCurrentLocation(): DomainLocation?


    class Mockk(
        private val locationManager: LocationManager,
        private val appContext: Context
    ): LocationRepository {

        private val provider = LocationManager.NETWORK_PROVIDER
        private val provider2 = LocationManager.GPS_PROVIDER

        private val currentLocation = MutableStateFlow<Location?>(null)

        private val locationListener = LocationListener { location ->
            Log.d("LocationRepository", "locationUpdates")
            currentLocation.value = location
        }

        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        override suspend fun getCurrentLocation(): DomainLocation? {
            withContext(Dispatchers.Main) {
                if(locationManager.isLocationEnabled) {
                    locationManager.requestLocationUpdates(
                        provider,
                        1000,
                        5_000f,
                        locationListener
                    )
                    locationManager.requestLocationUpdates(
                        provider2,
                        1000,
                        5_000f,
                        locationListener
                    )
                }
            }
            return withContext(Dispatchers.IO) {
                val deffered = async { currentLocation.mapNotNull { it }.first() }
                val asyncLocation = async {
                    var result: DomainLocation? = null
                    var time = 5_000
                    while (time != 0) {
                        if (deffered.isCompleted) {
                            Log.d("LocationRepository", "completed location")
                            val location = deffered.await()
                            val geocoder = Geocoder(appContext, Locale.getDefault())
                            var adresses: List<Address> = emptyList()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                geocoder.getFromLocation(location.latitude, location.longitude, 1) { adresses = it }
                            } else {
                                coroutineTryCatcher(
                                    tryBlock = { adresses = geocoder.getFromLocation(location.latitude, location.longitude, 1) ?: emptyList() },
                                    catchBlock = { adresses = emptyList() }
                                )
                            }

                            val country = adresses.getOrNull(0)?.countryName ?: DomainLocation.UNKNOWN
                            val town = adresses.getOrNull(0)?.locality ?: DomainLocation.UNKNOWN

                            result = DomainLocation(
                                longitude = location.longitude,
                                latitude = location.latitude,
                                country = country,
                                town = town
                            )
                            break
                        }
                        delay(1_000)
                        time -= 1_000
                    }
                    if(time == 0) {
                        deffered.cancel()
                        Log.d("LocationRepository", "cancelled location")
                    }
                    result
                }
                return@withContext asyncLocation.await()
            }
        }

    }

}