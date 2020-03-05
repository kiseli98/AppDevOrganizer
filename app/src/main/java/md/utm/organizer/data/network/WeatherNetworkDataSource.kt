package md.utm.organizer.data.network

import androidx.lifecycle.LiveData
import md.utm.organizer.data.network.response.CurrentWeatherResponse

//abstraction over WeatherstackApiService
//exception handling here
interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    //suspend - can be blocking. Can be called only within coroutines
    // will update the downloaded current weather live data, that will be observed
    suspend fun fetchCurrentWeather(
        location: String,
        units: String
    )

}