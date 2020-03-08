package md.utm.organizer.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val REQUEST_ID = 0

@Entity(tableName = "request")
data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = REQUEST_ID
}