package md.utm.organizer.data.db.unitlocalized.list

import androidx.room.ColumnInfo
import md.utm.organizer.data.network.response.currentWeather.WeatherDesc
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class SimpleFutureWeatherEntry(
    @ColumnInfo(name = "dtTxt")
    val date: LocalDateTime,
    @ColumnInfo(name = "temp")
    val temp: Double,
    @ColumnInfo(name = "weatherDescription")
    val weatherDescription: List<WeatherDesc>
    )