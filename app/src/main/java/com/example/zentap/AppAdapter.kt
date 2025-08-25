package com.example.zentap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial // Import the correct class

class AppAdapter(
    private val onToggleClick: (AppInfo, Boolean) -> Unit
) : ListAdapter<AppInfo, AppAdapter.AppViewHolder>(AppDiffCallback()) {

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        val appName: TextView = itemView.findViewById(R.id.appName)
        val appPackage: TextView = itemView.findViewById(R.id.appPackage)
        // --- FIX: Use SwitchMaterial instead of Switch ---
        val blockSwitch: SwitchMaterial = itemView.findViewById(R.id.blockSwitch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = getItem(position)

        holder.appIcon.setImageDrawable(app.icon)
        holder.appName.text = app.name
        holder.appPackage.text = app.packageName

        holder.blockSwitch.setOnCheckedChangeListener(null)
        holder.blockSwitch.isChecked = app.isBlocked
        holder.blockSwitch.setOnCheckedChangeListener { _, isChecked ->
            onToggleClick(app, isChecked)
        }
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}