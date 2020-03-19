package md.utm.organizer.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.network.response.futureWeather.FutureWeatherResponse
import md.utm.organizer.internal.NoConnectivityException

const val FORECAST_DAYS_COUNT = 10

class WeatherNetworkDataSourceImpl(
    private val weatherstackApiService: WeatherstackApiService
) : WeatherNetworkDataSource {

    // backing val for downloadedCurrentWeather is not mutable LiveData
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherEntry>()
    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherEntry>
        get() = _downloadedCurrentWeather //casting of MutableLiveData to LiveData

    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather //casting of MutableLiveData to LiveData

    override suspend fun fetchCurrentWeatherByLoc(
        location: String?,
        units: String,
        language: String
    ) {
        try {
            val fetchedCurrentWeather = weatherstackApiService
                .getCurrentWeatherByLoc(location, units, language)
                .await()

            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }


    override suspend fun fetchCurrentWeatherByCoord(
        latitude: String?,
        longitude: String?,
        units: String,
        language: String
    ) {
        try {
            val fetchedCurrentWeather = weatherstackApiService
                .getCurrentWeatherByCoord(latitude, longitude, units, language)
                .await()

            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }


    override suspend fun fetchFutureWeatherByCoord(
        latitude: String?,
        longitude: String?,
        units: String,
        language: String
    ) {
        try {
            val fetchedFutureWeather = weatherstackApiService
                .getFutureWeatherByCoord(latitude, longitude, FORECAST_DAYS_COUNT, units, language)
                .await()

            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }


    override suspend fun fetchFutureWeatherByLoc(
        location: String?,
        units: String,
        language: String
    ) {
        try {
            val fetchedFutureWeather = weatherstackApiService
                .getFutureWeatherByLoc(location, FORECAST_DAYS_COUNT, units, language)
                .await()

            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}