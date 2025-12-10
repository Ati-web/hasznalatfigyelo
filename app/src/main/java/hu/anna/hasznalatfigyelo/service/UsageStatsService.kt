package hu.anna.hasznalatfigyelo.service

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import hu.anna.hasznalatfigyelo.data.AppUsageDatabase
import hu.anna.hasznalatfigyelo.data.AppUsageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Calendar

class UsageStatsService : Service() {
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var database: AppUsageDatabase
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var packageManager: PackageManager
    
    override fun onCreate() {
        super.onCreate()
        database = AppUsageDatabase.getDatabase(applicationContext)
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        packageManager = getPackageManager()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_COLLECT_USAGE -> {
                collectUsageStats()
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun collectUsageStats() {
        serviceScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val endTime = calendar.timeInMillis
                
                // Ma kezdete
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startTime = calendar.timeInMillis
                
                // Használati statisztikák lekérése
                val usageStatsMap = usageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY,
                    startTime,
                    endTime
                )?.associateBy { it.packageName } ?: emptyMap()
                
                // UsageEvents lekérése az első használat időpontjának meghatározásához
                val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
                val firstUsageMap = mutableMapOf<String, Long>()
                
                while (usageEvents.hasNextEvent()) {
                    val event = UsageEvents.Event()
                    usageEvents.getNextEvent(event)
                    if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        val packageName = event.packageName
                        if (!firstUsageMap.containsKey(packageName) || firstUsageMap[packageName]!! > event.timeStamp) {
                            firstUsageMap[packageName] = event.timeStamp
                        }
                    }
                }
                
                val appUsageList = mutableListOf<AppUsageEntity>()
                
                usageStatsMap.values.forEach { usageStats ->
                    try {
                        // Csak azokat az alkalmazásokat adjuk hozzá, amelyeknek van használati ideje
                        if (usageStats.totalTimeInForeground > 0) {
                            val appName = getAppName(usageStats.packageName)
                            if (appName != null) {
                                // A launchCount mezőt a UsageStats API nem biztosítja közvetlenül
                                // Reflection-tel próbáljuk meg elérni, ha nem sikerül, 0-t használunk
                                val launchCount = try {
                                    val field = usageStats.javaClass.getDeclaredField("mLaunchCount")
                                    field.isAccessible = true
                                    field.getInt(usageStats)
                                } catch (e: Exception) {
                                    0
                                }
                                
                                // Első használat időpontja (ha nincs, akkor a nap kezdete)
                                val firstUsed = firstUsageMap[usageStats.packageName] ?: startTime
                                
                                val usageEntity = AppUsageEntity(
                                    packageName = usageStats.packageName,
                                    appName = appName,
                                    usageTimeMillis = usageStats.totalTimeInForeground,
                                    lastUsedTimestamp = usageStats.lastTimeUsed,
                                    firstUsedTimestamp = firstUsed,
                                    launchCount = launchCount,
                                    date = startTime
                                )
                                appUsageList.add(usageEntity)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Hiba az alkalmazás feldolgozásakor: ${usageStats.packageName}", e)
                    }
                }
                
                // Adatok mentése az adatbázisba
                if (appUsageList.isNotEmpty()) {
                    database.appUsageDao().insertAllUsage(appUsageList)
                    Log.d(TAG, "${appUsageList.size} alkalmazás használati adata rögzítve")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Hiba a használati statisztikák gyűjtésekor", e)
            }
        }
    }
    
    private fun getAppName(packageName: String): String? {
        return try {
            val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
    
    companion object {
        private const val TAG = "UsageStatsService"
        const val ACTION_COLLECT_USAGE = "hu.anna.hasznalatfigyelo.COLLECT_USAGE"
    }
}

