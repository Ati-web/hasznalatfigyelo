package com.appusagetracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "app_usage",
    indices = [Index(value = ["packageName", "date"], unique = true)]
)
data class AppUsageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val usageTimeMillis: Long, // használati idő milliszekundumban
    val lastUsedTimestamp: Long, // utolsó használat időbélyege
    val firstUsedTimestamp: Long = 0, // első használat időbélyege (nap kezdete)
    val launchCount: Int, // indítások száma
    val date: Long, // dátum (nap kezdete milliszekundumban)
    val createdAt: Long = System.currentTimeMillis()
)

