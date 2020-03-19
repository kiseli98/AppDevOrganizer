package md.utm.organizer.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import md.utm.organizer.data.network.response.currentWeather.*
import md.utm.organizer.utils.Converters
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


const val CURRENT_WEATHER_IDN = 0

@Entity(tableName = "current_weatherNew")
@TypeConverters(Converters::class)
data class CurrentWeatherEntry(
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    @Embedded
    val coord: Coord,
    val dt: Long,
    @Embedded
    val main: Main,
    val timezone: Long,
    val name: String,
    @Embedded
    val rain: Rain? = null,
    @Embedded
    val snow: Snow? = null,
    val visibility: Double,
    @SerializedName("weather")
    val weatherDescription: List<WeatherDesc>,
    @Embedded(prefix = "wind_")
    val wind: Wind
) {
    @PrimaryKey(autoGenerate = false)
    var idn: Int = CURRENT_WEATHER_IDN

    // get proper time
    val zonedDateTime: Long
        get() {
            return  dt + timezone
        }
}