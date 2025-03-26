package com.kirishhaa.photonotes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kirishhaa.photonotes.data.db.dao.LocalUsersDao
import com.kirishhaa.photonotes.data.db.dao.MarkerDao
import com.kirishhaa.photonotes.data.db.entity.FolderEntity
import com.kirishhaa.photonotes.data.db.entity.LanguageEntity
import com.kirishhaa.photonotes.data.db.entity.LocalUserEntity
import com.kirishhaa.photonotes.data.db.entity.LocationEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerEntity
import com.kirishhaa.photonotes.data.db.entity.MarkerTagEntity

@Database(
    entities = [FolderEntity::class, LocalUserEntity::class, LocationEntity::class, MarkerEntity::class, MarkerTagEntity::class, LanguageEntity::class],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun localUsersDao(): LocalUsersDao

    abstract fun markerDao(): MarkerDao

}