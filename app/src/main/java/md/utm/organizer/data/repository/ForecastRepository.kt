package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.db.unitlocalized.detail.DetailFutureWeatherEntry
import md.utm.organizer.data.db.unitlocalized.list.SimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {

    suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry>
    suspend fun getFutureWeatherByDate(date: LocalDate): LiveData<out DetailFutureWeatherEntry>
    suspend fun getFutureWeatherList(startDate: LocalDate): LiveData<out List<SimpleFutureWeatherEntry>>
}