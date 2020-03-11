package md.utm.organizer.data.network.response.currentWeather


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val percentage: Double
)