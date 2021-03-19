package gr.ppzglou.food.dao.fav

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favTable")
data class FavEntity(
    @PrimaryKey
    @ColumnInfo(name = "uri") val uri: String
)