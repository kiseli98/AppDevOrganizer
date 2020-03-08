package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import md.utm.organizer.data.db.CurrentWeatherDao
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.network.WeatherNetworkDataSource
import md.utm.organizer.data.network.response.CurrentWeatherResponse
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            //doesn't care about lifecycle
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override var isMetric: Boolean = true

    override suspend fun getCurrentWeather(isMetric: Boolean): LiveData<CurrentWeatherEntry> {
        var isTypeChanged: Boolean = this.isMetric != isMetric
        this.isMetric = isMetric
        //as Global launch but returns smth
        return withContext(Dispatchers.IO) {
            initWeatherData(isTypeChanged)
            return@withContext currentWeatherDao.getWeather()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        // Disp.IO - spin up lots of little threads
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData(isTypeChanged: Boolean) {
        if (isTypeChanged || isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()
    }

    //no return. FEtch data -> update liveData downloadedCurrentData -> downloadedCurrentData is observed in init {}
    // -> downloadedCurrentData is updated/fetched -> persist it to local database
    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            "Chisinau",
            if (this.isMetric) "m" else "f"
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}