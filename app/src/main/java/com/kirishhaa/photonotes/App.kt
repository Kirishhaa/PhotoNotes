package com.kirishhaa.photonotes

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.kirishhaa.photonotes.data.db.AppDatabase
import com.kirishhaa.photonotes.data.repos.feedback.FeedbackRepositoryImpl
import com.kirishhaa.photonotes.data.repos.localusers.LocalUserEntityMapper
import com.kirishhaa.photonotes.data.repos.localusers.LocalUsersRepositoryImpl
import com.kirishhaa.photonotes.data.repos.localusers.LoginValidator
import com.kirishhaa.photonotes.data.repos.localusers.PasswordValidator
import com.kirishhaa.photonotes.data.repos.localusers.UsernameValidator
import com.kirishhaa.photonotes.data.repos.location.LocationRepositoryImpl
import com.kirishhaa.photonotes.data.repos.markers.FolderEntityMapper
import com.kirishhaa.photonotes.data.repos.markers.MarkerEntityMapper
import com.kirishhaa.photonotes.data.repos.markers.MarkersRepositoryImpl
import com.kirishhaa.photonotes.data.repos.permissions.PermissionsRepositoryImpl
import com.kirishhaa.photonotes.domain.feedback.FeedbackRepository
import com.kirishhaa.photonotes.domain.location.LocationRepository
import com.kirishhaa.photonotes.domain.markers.MarkersRepository
import com.kirishhaa.photonotes.domain.permissions.PermissionsRepository
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository

fun CreationExtras.toApp() = this[APPLICATION_KEY] as App

class App : Application() {

    private val locationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val feedbackRepository: FeedbackRepository by lazy {
        FeedbackRepositoryImpl(this)
    }

    val locationRepository: LocationRepository by lazy {
        LocationRepositoryImpl(
            locationManager = locationManager,
            appContext = this
        )
    }

    val localUsersRepository: LocalUsersRepository by lazy {
        LocalUsersRepositoryImpl(
            localUsersDao = db.localUsersDao(),
            passwordValidator = PasswordValidator(),
            loginValidator = LoginValidator(),
            usernameValidator = UsernameValidator(),
            localUserEntityMapper = LocalUserEntityMapper(),
            context = this
        )
    }

    val markersRepository: MarkersRepository by lazy {
        MarkersRepositoryImpl(
            markerDao = db.markerDao(),
            locationRepository = locationRepository,
            markerEntityMapper = MarkerEntityMapper(),
            folderEntityMapper = FolderEntityMapper()
        )
    }

    val permissionsRepository: PermissionsRepository by lazy {
        PermissionsRepositoryImpl(this)
    }

    val db by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "app-db")
            .createFromAsset("mydb.db")
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        db
    }

}