package com.example.tracksleep

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracksleep.database.SleepNight

class SleepAdapter : RecyclerView.Adapter<SleepAdapter.SleepViewHolder>() {
    var data = listOf<SleepNight>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class SleepViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.sleep_item_list, parent, false)
        return SleepViewHolder(view)
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val item = data[position]

        val tvQuality = holder.itemView.findViewById<TextView>(R.id.tv_quality)
        val tvStart = holder.itemView.findViewById<TextView>(R.id.tv_start_time)
        val tvEnd = holder.itemView.findViewById<TextView>(R.id.tv_end_time)
        val resources = holder.itemView.context.resources

        tvQuality.text = convertNumericalQualityToString(item.sleepQuality, resources)
        tvStart.text = convertLongToDateString(item.startTimeMilli)
        tvEnd.text = convertLongToDateString(item.endTimeMilli)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}