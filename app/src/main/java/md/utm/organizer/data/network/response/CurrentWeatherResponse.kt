package md.utm.organizer.data.network.response

import com.google.gson.annotations.SerializedName
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.db.entity.WeatherLocation
import md.utm.organizer.data.db.entity.Request


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation,
    val request: Request
)