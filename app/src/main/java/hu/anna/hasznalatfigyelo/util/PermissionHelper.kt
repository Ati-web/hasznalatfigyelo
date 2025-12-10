package hu.anna.hasznalatfigyelo.util

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

object PermissionHelper {
    
    /**
     * Ellenőrzi, hogy van-e PACKAGE_USAGE_STATS engedély
     */
    fun hasUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }
    
    /**
     * Megnyitja a használati statisztikák engedély beállításait
     */
    fun openUsageStatsSettings(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(intent)
    }
}

