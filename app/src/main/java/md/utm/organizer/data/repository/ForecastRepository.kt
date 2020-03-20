package md.utm.organizer.data.repository

import androidx.lifecycle.LiveData
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.db.unitlocalized.detail.DetailFutureWeatherEntry
import md.utm.organizer.data.db.unitlocalized.list.SimpleFutureWeatherEntry
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface ForecastRepository {

    suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry>
    suspend fun getFutureWeatherByDate(date: LocalDateTime): LiveData<out DetailFutureWeatherEntry>
    suspend fun getFutureWeatherList(startDate: LocalDateTime): LiveData<out List<SimpleFutureWeatherEntry>>
}