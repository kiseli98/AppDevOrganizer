package md.utm.organizer.data.network.response.currentWeather


import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName("1h")
    val precipitationSnow: Double? = null
)