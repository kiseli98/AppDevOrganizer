package md.utm.organizer.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import md.utm.organizer.data.network.response.currentWeather.CURRENT_WEATHER_IDN
import md.utm.organizer.data.network.response.currentWeather.CurrentWeatherModel

@Dao
interface CurrentWeatherDao {
    // update/insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherModel)

    @Query("select * from current_weatherN where id = $CURRENT_WEATHER_IDN")
    fun getWeather(): LiveData<CurrentWeatherModel> // live data - observer for other components
}