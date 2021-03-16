package com.cloutstory.foshoweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cloutstory.foshoweather.models.HourlyMetaData
import com.google.gson.JsonArray

class HourlyCardAdapter(private val hourlyList: Array<HourlyMetaData>):
    RecyclerView.Adapter<HourlyCardAdapter.HourlyCardViewHolder>()
{

    override fun getItemCount(): Int {
        return hourlyList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyCardAdapter.HourlyCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_card_layout, parent, false)
        return HourlyCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyCardAdapter.HourlyCardViewHolder, position: Int) {
        holder.time.text = hourlyList[position].dt.toString()
        holder.temp.text = hourlyList[position].temp?.toInt().toString() + "°"
    }

    class HourlyCardViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val time = itemView.findViewById<TextView>(R.id.hourlyTime)
        val temp = itemView.findViewById<TextView>(R.id.hourlyTemp)
    }
}