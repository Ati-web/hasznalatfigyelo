package hu.anna.hasznalatfigyelo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import hu.anna.hasznalatfigyelo.R
import hu.anna.hasznalatfigyelo.data.AppUsageDatabase
import hu.anna.hasznalatfigyelo.databinding.ActivityMainBinding
import hu.anna.hasznalatfigyelo.service.UsageStatsService
import hu.anna.hasznalatfigyelo.util.DateHelper
import hu.anna.hasznalatfigyelo.util.PermissionHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: AppUsageAdapter
    private var isGridView = false // false = lista, true = grid
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        checkPermissions()
    }
    
    override fun onResume() {
        super.onResume()
        checkPermissions()
        if (PermissionHelper.hasUsageStatsPermission(this)) {
            collectUsageStats()
        }
    }
    
    private fun setupRecyclerView() {
        adapter = AppUsageAdapter()
        // Alapértelmezett: lista nézet
        binding.recyclerViewAppUsage.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAppUsage.adapter = adapter
    }
    
    private fun toggleViewLayout() {
        isGridView = !isGridView
        binding.recyclerViewAppUsage.layoutManager = if (isGridView) {
            GridLayoutManager(this, 2) // 2 oszlopos grid
        } else {
            LinearLayoutManager(this)
        }
    }
    
    private fun setupObservers() {
        viewModel.appUsageList.observe(this) { usageList ->
            adapter.submitList(usageList)
            binding.textEmptyState.visibility = if (usageList.isEmpty()) View.VISIBLE else View.GONE
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            // TODO: Loading indicator
        }
    }
    
    private fun setupClickListeners() {
        binding.buttonGrantPermission.setOnClickListener {
            PermissionHelper.openUsageStatsSettings(this)
        }
        
        binding.buttonRefresh.setOnClickListener {
            if (PermissionHelper.hasUsageStatsPermission(this)) {
                collectUsageStats()
            } else {
                Snackbar.make(binding.root, R.string.no_usage_stats_permission, Snackbar.LENGTH_LONG).show()
            }
        }
        
        binding.buttonExport.setOnClickListener {
            startActivity(Intent(this, ExportActivity::class.java))
        }
        
        binding.fabSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        // Dátum szűrők
        binding.chipGroupDateFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group.findViewById<Chip>(checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener)
            when (chip.id) {
                R.id.chipToday -> viewModel.loadUsageData(DateHelper.getTodayStart(), DateHelper.getTomorrowStart())
                R.id.chipYesterday -> viewModel.loadUsageData(DateHelper.getYesterdayStart(), DateHelper.getTodayStart())
                R.id.chipThisWeek -> viewModel.loadUsageData(DateHelper.getWeekStart(), DateHelper.getTomorrowStart())
                R.id.chipThisMonth -> viewModel.loadUsageData(DateHelper.getMonthStart(), DateHelper.getTomorrowStart())
                R.id.chipAllTime -> viewModel.loadUsageData(0, Long.MAX_VALUE)
            }
        }
    }
    
    private fun checkPermissions() {
        val hasPermission = PermissionHelper.hasUsageStatsPermission(this)
        binding.cardPermissionWarning.visibility = if (hasPermission) View.GONE else View.VISIBLE
    }
    
    private fun collectUsageStats() {
        val intent = Intent(this, UsageStatsService::class.java).apply {
            action = UsageStatsService.ACTION_COLLECT_USAGE
        }
        startService(intent)
        
        // Várunk egy kicsit, majd frissítjük az adatokat
        lifecycleScope.launch {
            kotlinx.coroutines.delay(1000)
            viewModel.loadUsageData(DateHelper.getTodayStart(), DateHelper.getTomorrowStart())
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_view -> {
                toggleViewLayout()
                item.setIcon(if (isGridView) android.R.drawable.ic_menu_sort_by_size else android.R.drawable.ic_menu_view)
                true
            }
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_data)
            .setMessage(R.string.delete_confirm)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteAllData()
                Snackbar.make(binding.root, "Adatok törölve", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}

