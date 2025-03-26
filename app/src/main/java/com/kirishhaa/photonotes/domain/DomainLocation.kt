package com.kirishhaa.photonotes.domain

class DomainLocation(
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val town: String
) {


    companion object {
        const val UNKNOWN = "UNKNOWN"

        val NONE = DomainLocation(
            latitude = -999.0,
            longitude = -999.0,
            country = UNKNOWN,
            town = UNKNOWN
        )

    }

}