package md.utm.organizer.data.network.response.currentWeather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val precipitationRain_1h: Double = 0.0,
    @SerializedName("3h")
    val precipitationRain_3h: Double = 0.0
)