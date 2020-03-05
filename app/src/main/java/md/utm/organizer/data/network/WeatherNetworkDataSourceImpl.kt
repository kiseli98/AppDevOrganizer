package md.utm.organizer.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import md.utm.organizer.data.network.response.CurrentWeatherResponse
import md.utm.organizer.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherstackApiService: WeatherstackApiService
) : WeatherNetworkDataSource {

    // backing val for downloadedCurrentWeather is not mutable LiveData
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather //casting of MutableLiveData to LiveData

    override suspend fun fetchCurrentWeather(location: String, units: String) {
        try {
            val fetchedCurrentWeather = weatherstackApiService.getCurrentWeather(location, units).await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}