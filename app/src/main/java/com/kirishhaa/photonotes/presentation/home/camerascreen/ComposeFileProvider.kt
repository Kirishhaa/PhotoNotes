package com.kirishhaa.photonotes.presentation.home.camerascreen

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.kirishhaa.photonotes.R
import java.io.File

class ComposeFileProvider : FileProvider(R.xml.paths) {

    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.filesDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                directory
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(context, authority, file)
        }
    }

}