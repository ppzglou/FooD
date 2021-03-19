package gr.ppzglou.food.dao.userpin

import androidx.room.*

@Dao
interface UserPinDao {

    @Query("SELECT * FROM pinsTable")
    suspend fun getAll(): MutableList<UserPinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userPinEntity: UserPinEntity)

    @Query("SELECT * FROM pinsTable WHERE uid = :uid")
    fun findById(uid: String): UserPinEntity

    @Delete
    suspend fun delete(userPinEntity: UserPinEntity)

    @Query("DELETE FROM pinsTable")
    suspend fun deleteAll()
}