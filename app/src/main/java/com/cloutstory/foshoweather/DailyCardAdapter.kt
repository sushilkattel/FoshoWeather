package com.cloutstory.foshoweather

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.cloutstory.foshoweather.models.DailyMetaData
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class DailyCardAdapter(private val dailyList: Array<DailyMetaData>):
        RecyclerView.Adapter<DailyCardAdapter.DailyCardViewHolder>()
{
    override fun getItemCount(): Int {
        return dailyList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyCardAdapter.DailyCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_card_layout, parent, false)
        return DailyCardAdapter.DailyCardViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DailyCardAdapter.DailyCardViewHolder, position: Int) {
        val netDailyTime = dailyList[position].dt
        val dailyTime = LocalDateTime.ofInstant(netDailyTime?.let { Instant.ofEpochSecond(it) }, ZoneId.systemDefault()).dayOfWeek.toString()
        holder.day.text = dailyTime
        //High/Low Temps
        val dailyTempArray = JSONObject((dailyList[position].temp).toString())
        val dailyTempLow = dailyTempArray.get("min").toString().toFloat().toInt().toString()
        val dailyTempHigh = dailyTempArray.get("max").toString().toFloat().toInt().toString()
        holder.lowTemp.text = dailyTempLow + "°"
        holder.highTemp.text = dailyTempHigh + "°"
        //Icons
        val hourlyIcon = dailyList[position].weather?.get(0)?.icon
        if (hourlyIcon !=null) {
            Picasso.get().load("https://foshoweather.s3.amazonaws.com/icons/$hourlyIcon.png").into(holder.icon)
        }


    }
    class DailyCardViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val day = itemView.findViewById<TextView>(R.id.dayOfWeek)
        val lowTemp = itemView.findViewById<TextView>(R.id.dailyLowTemp)
        val highTemp = itemView.findViewById<TextView>(R.id.dailyHighTemp)
        val icon = itemView.findViewById<ImageView>(R.id.dailyIcon)
    }
}