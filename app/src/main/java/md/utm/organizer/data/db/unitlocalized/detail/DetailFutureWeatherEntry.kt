package md.utm.organizer.data.db.unitlocalized.detail

import androidx.room.ColumnInfo
import md.utm.organizer.data.network.response.currentWeather.WeatherDesc
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class DetailFutureWeatherEntry(
    @ColumnInfo(name = "dtTxt")
    val date: LocalDateTime,
    @ColumnInfo(name = "tempMax")
    val maxTemp: Double,
    @ColumnInfo(name = "tempMin")
    val minTemp: Double,
    @ColumnInfo(name = "temp")
    val temp: Double,
    @ColumnInfo(name = "precipitationRain_1h")
    val precipitationRain_1h: Double = 0.0,
    @ColumnInfo(name = "precipitationRain_3h")
    val precipitationRain_3h: Double = 0.0,
    @ColumnInfo(name = "precipitationSnow_1h")
    val precipitationSnow_1h: Double = 0.0,
    @ColumnInfo(name = "precipitationSnow_3h")
    val precipitationSnow_3h: Double = 0.0,
    @ColumnInfo(name = "clouds_percentage")
    val cloudsPercentage: Double = 0.0,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double = 0.0,
    @ColumnInfo(name = "humidity")
    val humidity: Double,
    @ColumnInfo(name = "pressure")
    val pressure: Double,
    @ColumnInfo(name = "weatherDescription")
    val weatherDescription: List<WeatherDesc>
)
