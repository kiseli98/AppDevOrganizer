package md.utm.organizer.data.network.response.currentWeather


import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName("1h")
    val precipitationSnow_1h: Double = 0.0,
    @SerializedName("3h")
    val precipitationSnow_3h: Double = 0.0

)