package md.utm.organizer.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import md.utm.organizer.data.network.response.currentWeather.CurrentWeatherModel
import md.utm.organizer.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherstackApiService: WeatherstackApiService
) : WeatherNetworkDataSource {

    // backing val for downloadedCurrentWeather is not mutable LiveData
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherModel>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherModel>
        get() = _downloadedCurrentWeather //casting of MutableLiveData to LiveData

    override suspend fun fetchCurrentWeatherByLoc(location: String?, units: String, language: String) {
        try {
            val fetchedCurrentWeather = weatherstackApiService.getCurrentWeatherByLoc(location, units, language).await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }


    override suspend fun fetchCurrentWeatherByCoord(latitude: String?, longitude: String?, units: String, language: String) {
        try {
            val fetchedCurrentWeather = weatherstackApiService.getCurrentWeatherByCoord(latitude, longitude, units, language).await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}