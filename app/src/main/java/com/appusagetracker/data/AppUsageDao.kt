package com.appusagetracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {
    
    @Query("SELECT * FROM app_usage WHERE date >= :startDate AND date < :endDate AND usageTimeMillis > 0 ORDER BY usageTimeMillis DESC")
    fun getUsageByDateRange(startDate: Long, endDate: Long): Flow<List<AppUsageEntity>>
    
    @Query("SELECT * FROM app_usage WHERE date >= :startDate AND date < :endDate AND usageTimeMillis > 0 ORDER BY usageTimeMillis DESC")
    suspend fun getUsageByDateRangeSync(startDate: Long, endDate: Long): List<AppUsageEntity>
    
    @Query("SELECT * FROM app_usage WHERE packageName = :packageName AND date >= :startDate AND date < :endDate")
    suspend fun getUsageByPackage(packageName: String, startDate: Long, endDate: Long): List<AppUsageEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(usage: AppUsageEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsage(usageList: List<AppUsageEntity>)
    
    @Query("DELETE FROM app_usage")
    suspend fun deleteAllUsage()
    
    @Query("DELETE FROM app_usage WHERE date < :beforeDate")
    suspend fun deleteOldUsage(beforeDate: Long)
    
    @Query("SELECT DISTINCT packageName, appName FROM app_usage ORDER BY appName")
    suspend fun getAllPackages(): List<PackageInfo>
    
    class PackageInfo(
        val packageName: String,
        val appName: String
    )
}

