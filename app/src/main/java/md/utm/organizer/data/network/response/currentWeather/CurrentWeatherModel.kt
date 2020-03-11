package md.utm.organizer.data.network.response.currentWeather


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import md.utm.organizer.utils.Converters
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


const val CURRENT_WEATHER_IDN = 0

@Entity(tableName = "current_weatherN")
@TypeConverters(Converters::class)
data class CurrentWeatherModel(
    @Embedded(prefix = "clouds")
    val clouds: Clouds,
    @Embedded
    val coord: Coord,
    val dt: Long,
    @Embedded
    val main: Main,
    val timezone: String,
    val name: String,
    @Embedded
    val rain: Rain,
    @Embedded
    val snow: Snow,
    val visibility: Double,
    @SerializedName("weather")
    val weatherDescription: List<WeatherDesc>,
    @Embedded(prefix = "wind")
    val wind: Wind
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_IDN

    // get proper time
    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(dt)
            val zoneId = ZoneId.of(timezone)
            return  ZonedDateTime.ofInstant(instant, zoneId)
        }
}