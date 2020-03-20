package md.utm.organizer.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import md.utm.organizer.data.db.CurrentWeatherDao
import md.utm.organizer.data.db.FutureWeatherDao
import md.utm.organizer.data.network.WeatherNetworkDataSource
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.db.unitlocalized.detail.DetailFutureWeatherEntry
import md.utm.organizer.data.db.unitlocalized.list.SimpleFutureWeatherEntry
import md.utm.organizer.data.network.FORECAST_DAYS_COUNT
import md.utm.organizer.data.network.response.futureWeather.FutureWeatherResponse
import md.utm.organizer.data.provider.LocationProvider
import md.utm.organizer.data.provider.UnitProvider
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val locationProvider: LocationProvider,
    private val unitProvider: UnitProvider,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                //doesn't care about lifecycle
                persistFetchedCurrentWeather(newCurrentWeather)
            }

            downloadedFutureWeather.observeForever { newFutureWeather ->
                //doesn't care about lifecycle
                persistFetchedFutureWeather(newFutureWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry> {
        //as Global launch but returns smth
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getFutureWeatherList(startDate: LocalDateTime): LiveData<out List<SimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext futureWeatherDao.getSimpleWeatherForecast(startDate)
        }
    }


    override suspend fun getFutureWeatherByDate(date: LocalDateTime): LiveData<out DetailFutureWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext futureWeatherDao.getDetailedWeatherByDate(date)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherEntry) {
        // Disp.IO - spin up lots of little threads
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather)
        }
    }


    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {

        fun deleteOldForecastData() {
            val today = LocalDateTime.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            futureWeatherDao.upsert(fetchedWeather.futureWeatherEntries)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = currentWeatherDao.getWeatherNonLive()
        //first time launch
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (unitProvider.hasUnitSystemChanged()) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if (isFetchFutureNeeded())
            fetchFutureWeather()

    }

    //no return. FEtch data -> update liveData downloadedCurrentData -> downloadedCurrentData is observed in init {}
    // -> downloadedCurrentData is updated/fetched -> persist it to local database
    private suspend fun fetchCurrentWeather() {
        if (locationProvider.isUsingDeviceLocation()) {
            val coords: HashMap<String, String> = locationProvider.getCoords()

            if (coords.isEmpty())
                weatherNetworkDataSource.fetchCurrentWeatherByLoc(
                    locationProvider.getLocation(),
                    unitProvider.getUnitSystem().abbreviation,
                    Locale.getDefault().language
                )
            else
                weatherNetworkDataSource.fetchCurrentWeatherByCoord(
                    coords["latitude"],
                    coords["longitude"],
                    unitProvider.getUnitSystem().abbreviation,
                    Locale.getDefault().language

                )
        } else
            weatherNetworkDataSource.fetchCurrentWeatherByLoc(
                locationProvider.getLocation(),
                unitProvider.getUnitSystem().abbreviation,
                Locale.getDefault().language
            )
    }


    private suspend fun fetchFutureWeather() {
        if (locationProvider.isUsingDeviceLocation()) {
            val coords: HashMap<String, String> = locationProvider.getCoords()

            if (coords.isEmpty())
                weatherNetworkDataSource.fetchFutureWeatherByLoc(
                    locationProvider.getLocation(),
                    unitProvider.getUnitSystem().abbreviation,
                    Locale.getDefault().language
                )
            else
                weatherNetworkDataSource.fetchFutureWeatherByCoord(
                    coords["latitude"],
                    coords["longitude"],
                    unitProvider.getUnitSystem().abbreviation,
                    Locale.getDefault().language

                )
        } else
            weatherNetworkDataSource.fetchFutureWeatherByLoc(
                locationProvider.getLocation(),
                unitProvider.getUnitSystem().abbreviation
            )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: Long): Boolean {
        val nowFromEpoch = ZonedDateTime.now().toInstant().epochSecond
        return (nowFromEpoch - lastFetchTime) > (30 * 60) // 30 mins
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDateTime.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }
}