package com.kirishhaa.photonotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kirishhaa.photonotes.data.dao.FoldersDao
import com.kirishhaa.photonotes.data.dao.LocalUsersDao
import com.kirishhaa.photonotes.data.dao.LocationsDao
import com.kirishhaa.photonotes.data.dao.MarkerDao
import com.kirishhaa.photonotes.data.dao.TagDao
import com.kirishhaa.photonotes.data.entity.FolderEntity
import com.kirishhaa.photonotes.data.entity.LocalUserEntity
import com.kirishhaa.photonotes.data.entity.LocationEntity
import com.kirishhaa.photonotes.data.entity.MarkerEntity
import com.kirishhaa.photonotes.data.entity.TagEntity

@Database(
    entities = [FolderEntity::class, LocalUserEntity::class, LocationEntity::class, MarkerEntity::class, TagEntity::class],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun foldersDao(): FoldersDao

    abstract fun localUsersDao(): LocalUsersDao

    abstract fun locationsDao(): LocationsDao

    abstract fun markerDao(): MarkerDao

    abstract fun tagDao(): TagDao

}