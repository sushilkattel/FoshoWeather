package com.cloutstory.foshoweather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cloutstory.foshoweather.models.HourlyMetaData
import com.squareup.picasso.Picasso
import java.util.*

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
        val now = System.currentTimeMillis()/1000
        val hourlyTime = hourlyList[position].dt
        val time = (hourlyTime?.minus(now))?.div(60)?.div(60)
        fun getCurrentTime(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.HOUR_OF_DAY)
        }
        //Time Logic
        val hour = getCurrentTime()
        if (time !=null) {
            if (time > 0) {
                val finalTime = hour + time
                if (finalTime <=11) {
                    holder.time.text = finalTime.toString() + "am"
                }
                if (finalTime > 12 ) {
                    if (finalTime < 24) {
                        val finalTimePM = finalTime - 12
                        holder.time.text = finalTimePM.toString() + "pm"
                    }
                    if (finalTime > 24) {
                        if (finalTime < 36) {
                            val finalTimePM = finalTime - 24
                            holder.time.text = finalTimePM.toString() + "am"
                        }
                        if (finalTime > 36) {
                            val finalTimePM = finalTime - 36
                            holder.time.text = finalTimePM.toString() + "pm"
                            if (finalTime > 48) {
                                val finalTimePM = finalTime - 48
                                holder.time.text = finalTimePM.toString() + "am"
                            }
                        }
                    }
                }
                if (finalTime.toInt() == 12) {
                    holder.time.text = "Noon"
                }
                if (finalTime.toInt() == 24) {
                    holder.time.text = "Midnight"
                }
                if (finalTime.toInt() == 36) {
                    holder.time.text = "Noon"
                }
                if (finalTime.toInt() == 48) {
                    holder.time.text = "Midnight"
                }
            } else {
                holder.time.text = "Now"
            }
            holder.temp.text = hourlyList[position].temp?.toInt().toString() + "°"
            }
        //Icon
        val hourlyIcon = hourlyList[position].weather?.get(0)?.icon
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

    class HourlyCardViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val time = itemView.findViewById<TextView>(R.id.hourlyTime)
        val temp = itemView.findViewById<TextView>(R.id.hourlyTemp)
        val icon = itemView.findViewById<ImageView>(R.id.cardWeatherIcon)
    }
}