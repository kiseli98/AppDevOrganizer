package md.utm.organizer.data.network.response.currentWeather


import com.google.gson.annotations.SerializedName

data class WeatherDesc(
    val description: String,
    val icon: String,
    val main: String
)