package md.utm.organizer.data.network

import androidx.lifecycle.LiveData
import md.utm.organizer.data.network.response.currentWeather.CurrentWeatherModel

//abstraction over WeatherstackApiService
//exception handling here
interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherModel>

    //suspend - can be blocking. Can be called only within coroutines
    // will update the downloaded current weather live data, that will be observed
    suspend fun fetchCurrentWeatherByLoc(
        location: String?,
        units: String,
        language: String = "en"
    )

    suspend fun fetchCurrentWeatherByCoord(
        latitude: String?,
        longitude: String?,
        units: String,
        language: String = "en"
    )

}