package md.utm.organizer.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import md.utm.organizer.data.db.entity.CURRENT_WEATHER_IDN
import md.utm.organizer.data.db.entity.CurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    // update/insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weatherNew where idn = $CURRENT_WEATHER_IDN")
    fun getWeather(): LiveData<CurrentWeatherEntry> // live data - observer for other components

    @Query("select * from current_weatherNew where idn = $CURRENT_WEATHER_IDN")
    fun getWeatherNonLive(): CurrentWeatherEntry?

    @Query("select * from current_weatherNew")
    fun getAll(): List<CurrentWeatherEntry>
}