package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import md.utm.organizer.data.db.CurrentWeatherDao
import md.utm.organizer.data.db.RequestDao
import md.utm.organizer.data.db.WeatherLocationDao
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.db.entity.Request
import md.utm.organizer.data.db.entity.WeatherLocation
import md.utm.organizer.data.network.WeatherNetworkDataSource
import md.utm.organizer.data.network.response.CurrentWeatherResponse
import md.utm.organizer.data.provider.LocationProvider
import md.utm.organizer.data.provider.UnitProvider
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val locationProvider: LocationProvider,
    private val unitProvider: UnitProvider,
    private val requestDao: RequestDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            //doesn't care about lifecycle
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry> {
        //as Global launch but returns smth
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeaherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    override suspend fun getRequest(): LiveData<Request> {
        return withContext(Dispatchers.IO) {
            return@withContext requestDao.getRequest()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        // Disp.IO - spin up lots of little threads
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocation().value
        val lastRequest = requestDao.getRequest().value

        //first time launch
        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if(lastRequest == null || unitProvider.hasUnitSystemChanged(lastRequest)) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    //no return. FEtch data -> update liveData downloadedCurrentData -> downloadedCurrentData is observed in init {}
    // -> downloadedCurrentData is updated/fetched -> persist it to local database
    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            unitProvider.getUnitSystem().abbreviation
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}