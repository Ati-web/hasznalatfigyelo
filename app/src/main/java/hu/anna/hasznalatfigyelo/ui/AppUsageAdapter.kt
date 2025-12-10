package hu.anna.hasznalatfigyelo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.anna.hasznalatfigyelo.R
import hu.anna.hasznalatfigyelo.data.AppUsageEntity
import hu.anna.hasznalatfigyelo.util.DateHelper
import java.text.SimpleDateFormat
import java.util.Locale

class AppUsageAdapter : ListAdapter<AppUsageEntity, AppUsageAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_usage, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appNameText: TextView = itemView.findViewById(R.id.textAppName)
        private val usageTimeText: TextView = itemView.findViewById(R.id.textUsageTime)
        private val lastUsedText: TextView = itemView.findViewById(R.id.textLastUsed)
        private val launchCountText: TextView = itemView.findViewById(R.id.textLaunchCount)
        
        fun bind(usage: AppUsageEntity) {
            appNameText.text = usage.appName
            usageTimeText.text = DateHelper.formatTime(usage.usageTimeMillis)
            
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            
            // Időintervallum megjelenítése (mettől-meddig)
            if (usage.firstUsedTimestamp > 0 && usage.lastUsedTimestamp > 0) {
                val firstTime = dateFormat.format(java.util.Date(usage.firstUsedTimestamp))
                val lastTime = dateFormat.format(java.util.Date(usage.lastUsedTimestamp))
                lastUsedText.text = "Használat: $firstTime - $lastTime"
            } else {
                lastUsedText.text = "Utolsó: ${dateTimeFormat.format(java.util.Date(usage.lastUsedTimestamp))}"
            }
            
            launchCountText.text = "${usage.launchCount}x"
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<AppUsageEntity>() {
        override fun areItemsTheSame(oldItem: AppUsageEntity, newItem: AppUsageEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: AppUsageEntity, newItem: AppUsageEntity): Boolean {
            return oldItem == newItem
        }
    }
}

