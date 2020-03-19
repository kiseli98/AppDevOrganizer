package md.utm.organizer.data.network.response.futureWeather

import com.google.gson.annotations.SerializedName
import md.utm.organizer.data.db.entity.FutureWeatherEntry


data class FutureWeatherResponse(
    @SerializedName("list")
    val futureWeatherEntries: List<FutureWeatherEntry>
)