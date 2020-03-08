package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.db.entity.Request
import md.utm.organizer.data.db.entity.WeatherLocation

interface ForecastRepository {

    suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry>
    suspend fun getWeaherLocation(): LiveData<WeatherLocation>
    suspend fun getRequest(): LiveData<Request>
}