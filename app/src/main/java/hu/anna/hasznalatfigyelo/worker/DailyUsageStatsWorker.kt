package hu.anna.hasznalatfigyelo.worker

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import hu.anna.hasznalatfigyelo.service.UsageStatsService
import hu.anna.hasznalatfigyelo.util.PermissionHelper

class DailyUsageStatsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Ellenőrizzük, hogy van-e engedély
            if (!PermissionHelper.hasUsageStatsPermission(applicationContext)) {
                // Ha nincs engedély, akkor is sikeresnek jelöljük, hogy ne próbálja újra folyamatosan
                return Result.success()
            }

            // Meghívjuk a UsageStatsService-t
            val intent = Intent(applicationContext, UsageStatsService::class.java).apply {
                action = UsageStatsService.ACTION_COLLECT_USAGE
            }
            applicationContext.startService(intent)

            Result.success()
        } catch (e: Exception) {
            // Hiba esetén retry-tel próbáljuk újra
            Result.retry()
        }
    }
}

