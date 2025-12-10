package com.appusagetracker.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.appusagetracker.R
import com.appusagetracker.data.AppUsageDatabase
import com.appusagetracker.databinding.ActivitySettingsBinding
import com.appusagetracker.service.UsageStatsService
import com.appusagetracker.util.PermissionHelper
import kotlinx.coroutines.launch
import java.util.Calendar

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val database = AppUsageDatabase.getDatabase(this)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        setupClickListeners()
        updatePermissionStatus()
    }
    
    override fun onResume() {
        super.onResume()
        updatePermissionStatus()
    }
    
    private fun setupClickListeners() {
        binding.buttonGrantPermission.setOnClickListener {
            PermissionHelper.openUsageStatsSettings(this)
        }
        
        binding.buttonCollectNow.setOnClickListener {
            if (PermissionHelper.hasUsageStatsPermission(this)) {
                collectUsageStats()
                Snackbar.make(binding.root, "Adatok gyűjtése elindítva", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, R.string.no_usage_stats_permission, Snackbar.LENGTH_LONG).show()
            }
        }
        
        binding.buttonDeleteOld.setOnClickListener {
            deleteOldData()
        }
        
        binding.buttonDeleteAll.setOnClickListener {
            deleteAllData()
        }
    }
    
    private fun updatePermissionStatus() {
        val hasPermission = PermissionHelper.hasUsageStatsPermission(this)
        binding.textPermissionStatus.text = if (hasPermission) {
            "Engedély megadva ✓"
        } else {
            "Engedély nincs megadva ✗"
        }
        binding.buttonGrantPermission.isEnabled = !hasPermission
    }
    
    private fun collectUsageStats() {
        val intent = android.content.Intent(this, UsageStatsService::class.java).apply {
            action = UsageStatsService.ACTION_COLLECT_USAGE
        }
        startService(intent)
    }
    
    private fun deleteOldData() {
        lifecycleScope.launch {
            try {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -30) // 30 napnál régebbi adatok
                val beforeDate = calendar.timeInMillis
                
                database.appUsageDao().deleteOldUsage(beforeDate)
                Snackbar.make(binding.root, "30 napnál régebbi adatok törölve", Snackbar.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Hiba történt: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
    private fun deleteAllData() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(R.string.delete_data)
            .setMessage("Biztosan törölni szeretné az ÖSSZES adatot? Ez a művelet nem visszavonható!")
            .setPositiveButton(R.string.yes) { _, _ ->
                lifecycleScope.launch {
                    try {
                        database.appUsageDao().deleteAllUsage()
                        Snackbar.make(binding.root, "Minden adat törölve", Snackbar.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Snackbar.make(binding.root, "Hiba történt: ${e.message}", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}

