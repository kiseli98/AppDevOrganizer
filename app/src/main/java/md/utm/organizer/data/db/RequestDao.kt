package md.utm.organizer.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import md.utm.organizer.data.db.entity.REQUEST_ID
import md.utm.organizer.data.db.entity.Request

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(request: Request)

    @Query("select * from request where id = $REQUEST_ID")
    fun getRequest(): LiveData<Request>

}