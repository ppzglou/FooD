package gr.ppzglou.food.dao.fav

import androidx.room.*

@Dao
interface FavDao {

    @Query("SELECT * FROM favTable")
    suspend fun getAll(): MutableList<FavEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favEntity: FavEntity)

    @Query("SELECT * FROM favTable WHERE uri = :uri")
    fun findById(uri: String): FavEntity

    @Delete
    suspend fun delete(favEntity: FavEntity)

    @Query("DELETE FROM favTable")
    suspend fun deleteAll()
}