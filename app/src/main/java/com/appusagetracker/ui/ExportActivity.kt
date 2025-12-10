package com.appusagetracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.appusagetracker.R
import com.appusagetracker.data.AppUsageDatabase
import com.appusagetracker.data.AppUsageEntity
import com.appusagetracker.databinding.ActivityExportBinding
import com.appusagetracker.util.DateHelper
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExportActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityExportBinding
    private val database = AppUsageDatabase.getDatabase(this)
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            exportData()
        } else {
            Snackbar.make(binding.root, "Az exportáláshoz tárhely engedély szükséges", Snackbar.LENGTH_LONG).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.buttonExportCsv.setOnClickListener {
            checkPermissionAndExport("CSV")
        }
        
        binding.buttonExportJson.setOnClickListener {
            checkPermissionAndExport("JSON")
        }
        
        binding.buttonExportToday.setOnClickListener {
            checkPermissionAndExport("CSV", DateHelper.getTodayStart(), DateHelper.getTomorrowStart())
        }
        
        binding.buttonExportWeek.setOnClickListener {
            checkPermissionAndExport("CSV", DateHelper.getWeekStart(), DateHelper.getTomorrowStart())
        }
    }
    
    private fun checkPermissionAndExport(format: String, startDate: Long = 0, endDate: Long = Long.MAX_VALUE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ - nincs szükség engedélyre
            exportData(format, startDate, endDate)
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                == PackageManager.PERMISSION_GRANTED) {
                exportData(format, startDate, endDate)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
    
    private fun exportData(format: String = "CSV", startDate: Long = 0, endDate: Long = Long.MAX_VALUE) {
        lifecycleScope.launch {
            try {
                val usageList = database.appUsageDao().getUsageByDateRangeSync(startDate, endDate)
                
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "app_usage_export_$timestamp.${format.lowercase()}"
                
                val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // Android 11+ - Downloads mappa
                    File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                } else {
                    // Régebbi Android verziók
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                }
                
                when (format) {
                    "CSV" -> exportToCsv(usageList, file)
                    "JSON" -> exportToJson(usageList, file)
                }
                
                Snackbar.make(
                    binding.root,
                    "Adatok exportálva: ${file.absolutePath}",
                    Snackbar.LENGTH_LONG
                ).show()
                
                Toast.makeText(this@ExportActivity, R.string.export_success, Toast.LENGTH_SHORT).show()
                
            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(binding.root, R.string.export_failed, Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
    private fun exportToCsv(usageList: List<AppUsageEntity>, file: File) {
        FileWriter(file).use { writer ->
            // CSV fejléc
            writer.append("Alkalmazás neve,Package név,Használati idő (ms),Használati idő (olvasható),Utolsó használat,Indítások száma,Dátum\n")
            
            // Adatok
            usageList.forEach { usage ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                writer.append("${usage.appName},")
                writer.append("${usage.packageName},")
                writer.append("${usage.usageTimeMillis},")
                writer.append("${DateHelper.formatTime(usage.usageTimeMillis)},")
                writer.append("${dateFormat.format(Date(usage.lastUsedTimestamp))},")
                writer.append("${usage.launchCount},")
                writer.append("${dateFormat.format(Date(usage.date))}\n")
            }
        }
    }
    
    private fun exportToJson(usageList: List<AppUsageEntity>, file: File) {
        val jsonArray = JSONArray()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        
        usageList.forEach { usage ->
            val jsonObject = JSONObject().apply {
                put("appName", usage.appName)
                put("packageName", usage.packageName)
                put("usageTimeMillis", usage.usageTimeMillis)
                put("usageTimeReadable", DateHelper.formatTime(usage.usageTimeMillis))
                put("lastUsed", dateFormat.format(Date(usage.lastUsedTimestamp)))
                put("launchCount", usage.launchCount)
                put("date", dateFormat.format(Date(usage.date)))
            }
            jsonArray.put(jsonObject)
        }
        
        FileWriter(file).use { writer ->
            writer.write(jsonArray.toString(2))
        }
    }
}

