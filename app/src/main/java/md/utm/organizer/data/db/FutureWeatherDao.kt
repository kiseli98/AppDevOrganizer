package md.utm.organizer.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import md.utm.organizer.data.db.entity.FutureWeatherEntry
import md.utm.organizer.data.db.unitlocalized.detail.DetailFutureWeatherEntry
import md.utm.organizer.data.db.unitlocalized.list.SimpleFutureWeatherEntry
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("select * from future_weather where date(dtTxt) >= date(:startDate)")
    fun getSimpleWeatherForecast(startDate: LocalDateTime): LiveData<List<SimpleFutureWeatherEntry>> // live data - observer for other components

    @Query("select * from future_weather where date(dtTxt) = date(:date)")
    fun getDetailedWeatherByDate(date: LocalDateTime): LiveData<DetailFutureWeatherEntry>

    @Query("select count(idn) from future_weather where date(dtTxt) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDateTime): Int

    @Query("delete from future_weather where date(dtTxt) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDateTime)

}