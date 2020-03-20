package md.utm.organizer.ui.weather.future.list

import android.util.Log
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import md.utm.organizer.R
import md.utm.organizer.data.db.unitlocalized.list.SimpleFutureWeatherEntry
import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.internal.UnitSystem
import md.utm.organizer.internal.glide.GlideApp
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.time.LocalDateTime

class FutureWeatherItem(
    val weatherEntry: SimpleFutureWeatherEntry,
    val unitProvider: UnitProvider
) : Item() {


    //    no code for viewHolders is required
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text =
                "${weatherEntry.weatherDescription[0].main} (${weatherEntry.weatherDescription[0].description})"
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_future_weather


    // extension function to use with ViewHolder
    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT) // e.g. Mar 15, 2020
        textView_date.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = if (unitProvider.getUnitSystem() == UnitSystem.METRIC) "°C" else "°F"
        textView_temperature.text = "${String.format("%.1f", weatherEntry.temp).toDouble()}$unitAbbreviation"

    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load("http://openweathermap.org/img/w/${weatherEntry.weatherDescription[0].icon}.png")
            .into(imageView_condition_icon)
    }
}