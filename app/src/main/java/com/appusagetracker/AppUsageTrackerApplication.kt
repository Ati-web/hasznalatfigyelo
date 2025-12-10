package com.appusagetracker

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.appusagetracker.worker.DailyUsageStatsWorker
import java.util.concurrent.TimeUnit

class AppUsageTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDailyUsageStatsCollection()
    }

    private fun setupDailyUsageStatsCollection() {
        // Periodikus munka beállítása - naponta egyszer fut le
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED) // Nincs szükség internetre
            .setRequiresBatteryNotLow(false) // Nem kell, hogy a telefon töltve legyen
            .setRequiresCharging(false) // Nem kell, hogy töltés alatt legyen
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyUsageStatsWorker>(
            24, // 24 óránként
            TimeUnit.HOURS,
            1, // Flex intervallum: 1 óra (24-1=23 óra után már futtatható)
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag("daily_usage_stats_collection")
            .build()

        // Beállítjuk a periodikus munkát (ha már létezik, akkor frissítjük)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_usage_stats_work",
            ExistingPeriodicWorkPolicy.KEEP, // Ha már létezik, megtartjuk
            dailyWorkRequest
        )
    }
}

