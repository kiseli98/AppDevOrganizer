package md.utm.organizer.data.db.entity


import androidx.room.*
import com.google.gson.annotations.SerializedName
import md.utm.organizer.data.network.response.currentWeather.*
import md.utm.organizer.utils.Converters

@Entity(tableName = "future_weather", indices = [Index(value= ["dtTxt"], unique = true)])
@TypeConverters(Converters::class)
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val idn: Int? = null,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    @SerializedName("dt_txt")
    val dtTxt: String,
    @Embedded
    val main: Main,
    @Embedded
    val rain: Rain? = null,
    @Embedded
    val snow: Snow? = null,
    @SerializedName("weather")
    val weatherDescription: List<WeatherDesc>,
    @Embedded(prefix = "wind_")
    val wind: Wind
)