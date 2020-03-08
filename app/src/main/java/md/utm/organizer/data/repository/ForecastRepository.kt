package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.db.entity.WeatherLocation

interface ForecastRepository {
    var isMetric: Boolean

    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<CurrentWeatherEntry>
    suspend fun getWeaherLocation(): LiveData<WeatherLocation>
}