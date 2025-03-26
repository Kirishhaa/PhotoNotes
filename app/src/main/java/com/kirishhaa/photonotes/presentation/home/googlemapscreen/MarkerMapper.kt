package com.kirishhaa.photonotes.presentation.home.googlemapscreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.net.toUri
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.kirishhaa.photonotes.domain.Marker


class MarkerMapper(
    private val appContext: Context
) {

    fun map(marker: Marker): MarkerUI {
        return MarkerUI(
            id = marker.id,
            image = toBitmapDescriptor(marker.filePath),
            latitude = marker.location.latitude,
            longitude = marker.location.longitude
        )
    }

    private fun toBitmapDescriptor(filePath: String): BitmapDescriptor? {
        appContext.contentResolver.openInputStream(filePath.toUri())?.use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream).scale(80,80)
            val cropped = getCroppedBitmap(bitmap)
            return BitmapDescriptorFactory.fromBitmap(cropped)
        }
        return null
    }

    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
        val output = createBitmap(bitmap.getWidth(), bitmap.getHeight())
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = android.graphics.Rect(0, 0, bitmap.getWidth(), bitmap.getHeight())
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        canvas.drawCircle(
            bitmap.getWidth() / 2f, bitmap.getHeight() / 2f,
            bitmap.getWidth() / 2f, paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

}