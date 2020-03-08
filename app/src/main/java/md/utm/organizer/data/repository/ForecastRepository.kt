package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import md.utm.organizer.data.db.entity.CurrentWeatherEntry

interface ForecastRepository {
    var isMetric: Boolean

    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<CurrentWeatherEntry>
}