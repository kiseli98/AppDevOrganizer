package md.utm.organizer.data.network.response.currentWeather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val precipitationRain: Double? = null
)