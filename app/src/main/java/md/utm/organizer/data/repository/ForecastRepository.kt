package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import md.utm.organizer.data.network.response.currentWeather.CurrentWeatherModel

interface ForecastRepository {

    suspend fun getCurrentWeather(): LiveData<CurrentWeatherModel>
}