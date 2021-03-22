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
        if (hourlyIcon == "01d") {
            Picasso.get().load("https://drive.google.com/uc?id=1BuMvcNN3-OqM7YdcE3nXHAVe-Lh14xRG").into(holder.icon)
        }
        if (hourlyIcon == "01n") {
            Picasso.get().load("https://drive.google.com/uc?id=1vnLEWuexXdL3ISOl2sgnbUhyyEcetrfP").into(holder.icon)
        }
        if (hourlyIcon == "02d") {
            Picasso.get().load("https://drive.google.com/uc?id=1wR50x7BS739UJXkLJIM95kfNwExZgGXQ").into(holder.icon)
        }
        if (hourlyIcon == "02n") {
            Picasso.get().load("https://drive.google.com/uc?id=1KJiKwPQP3M77iVuhLgDE8oTPaCfCxt_y").into(holder.icon)
        }
        if (hourlyIcon == "03d") {
            Picasso.get().load("https://drive.google.com/uc?id=1wR50x7BS739UJXkLJIM95kfNwExZgGXQ").into(holder.icon)
        }
        if (hourlyIcon == "03n") {
            Picasso.get().load("https://drive.google.com/uc?id=1wR50x7BS739UJXkLJIM95kfNwExZgGXQ").into(holder.icon)
        }
        if (hourlyIcon == "04d") {
            Picasso.get().load("https://drive.google.com/uc?id=1wR50x7BS739UJXkLJIM95kfNwExZgGXQ").into(holder.icon)
        }
        if (hourlyIcon == "04n") {
            Picasso.get().load("https://drive.google.com/uc?id=1wR50x7BS739UJXkLJIM95kfNwExZgGXQ").into(holder.icon)
        }
        if (hourlyIcon == "09d") {
            Picasso.get().load("https://drive.google.com/uc?id=151Sp8yQia6jcjyhpa4pFMMuPyIkKlE8i").into(holder.icon)
        }
        if (hourlyIcon == "09n") {
            Picasso.get().load("https://drive.google.com/uc?id=151Sp8yQia6jcjyhpa4pFMMuPyIkKlE8i").into(holder.icon)
        }
        if (hourlyIcon == "10d") {
            Picasso.get().load("https://drive.google.com/uc?id=1g0kAJG5YiV-ug5IINxhTybsXu3DdWYC-").into(holder.icon)
        }
        if (hourlyIcon == "10n") {
            Picasso.get().load("https://drive.google.com/uc?id=1sn63Y8f_YulJ1TrcVVSpIDkAa5goeScL").into(holder.icon)
        }
        if (hourlyIcon == "11d") {
            Picasso.get().load("https://drive.google.com/uc?id=1dbJVJzzpOoC-RWqQLLpid6VxEvQOSLYK").into(holder.icon)
        }
        if (hourlyIcon == "11n") {
            Picasso.get().load("https://drive.google.com/uc?id=1dbJVJzzpOoC-RWqQLLpid6VxEvQOSLYK").into(holder.icon)
        }
        if (hourlyIcon == "13d") {
            Picasso.get().load("https://drive.google.com/uc?id=1xpxYOgcCBO-PkGOJY72XUeZ7wfJel5nX").into(holder.icon)
        }
        if (hourlyIcon == "13n") {
            Picasso.get().load("https://drive.google.com/uc?id=1xpxYOgcCBO-PkGOJY72XUeZ7wfJel5nX").into(holder.icon)
        }
        if (hourlyIcon == "50d") {
            Picasso.get().load("https://drive.google.com/uc?id=1jJMoA4wxXQrRoKsd8Z5LTJs-WqL_syn7").into(holder.icon)
        }
        if (hourlyIcon == "50n") {
            Picasso.get().load("https://drive.google.com/uc?id=1jJMoA4wxXQrRoKsd8Z5LTJs-WqL_syn7").into(holder.icon)
        }


    }
    class DailyCardViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val day = itemView.findViewById<TextView>(R.id.dayOfWeek)
        val lowTemp = itemView.findViewById<TextView>(R.id.dailyLowTemp)
        val highTemp = itemView.findViewById<TextView>(R.id.dailyHighTemp)
        val icon = itemView.findViewById<ImageView>(R.id.dailyIcon)
    }
}