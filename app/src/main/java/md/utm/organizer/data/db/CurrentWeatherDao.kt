package md.utm.organizer.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import md.utm.organizer.data.db.entity.CURRENT_WEATHER_ID
import md.utm.organizer.data.db.entity.CurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    // update/insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeather(): LiveData<CurrentWeatherEntry> // live data - observer for other components
}