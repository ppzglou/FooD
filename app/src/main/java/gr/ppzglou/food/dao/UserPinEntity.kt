package gr.ppzglou.food.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pinsTable")
data class UserPinEntity(
    @PrimaryKey
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "pin") val pin: String,
    @ColumnInfo(name = "fingerprint") val fingerprint: Boolean = false
)