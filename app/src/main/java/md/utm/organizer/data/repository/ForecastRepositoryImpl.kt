package md.utm.organizer.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import md.utm.organizer.data.db.CurrentWeatherDao
import md.utm.organizer.data.network.WeatherNetworkDataSource
import md.utm.organizer.data.network.response.currentWeather.CurrentWeatherModel
import md.utm.organizer.data.provider.LocationProvider
import md.utm.organizer.data.provider.UnitProvider
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val locationProvider: LocationProvider,
    private val unitProvider: UnitProvider,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            //doesn't care about lifecycle
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherModel> {
        //as Global launch but returns smth
        return withContext(Dispatchers.IO) {
//            initWeatherData()
            return@withContext currentWeatherDao.getWeather()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherModel) {
        // Disp.IO - spin up lots of little threads
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = currentWeatherDao.getWeather().value
        //first time launch
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if (unitProvider.hasUnitSystemChanged()) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    //no return. FEtch data -> update liveData downloadedCurrentData -> downloadedCurrentData is observed in init {}
    // -> downloadedCurrentData is updated/fetched -> persist it to local database
    private suspend fun fetchCurrentWeather() {
        if (locationProvider.isUsingDeviceLocation()) {
            val coords: HashMap<String, String> = locationProvider.getCoords()

            if (coords.isEmpty())
                weatherNetworkDataSource.fetchCurrentWeatherByLoc(
                    locationProvider.getLocation(),
                    unitProvider.getUnitSystem().abbreviation
                )
            else
                weatherNetworkDataSource.fetchCurrentWeatherByCoord(
                    coords["latitude"],
                    coords["longitude"],
                    unitProvider.getUnitSystem().abbreviation

                )
        }
        else
            weatherNetworkDataSource.fetchCurrentWeatherByLoc(
                locationProvider.getLocation(),
                unitProvider.getUnitSystem().abbreviation
            )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}