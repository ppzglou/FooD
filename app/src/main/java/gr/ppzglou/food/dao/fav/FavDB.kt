package gr.ppzglou.food.dao.fav

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavEntity::class], version = 1)
abstract class FavDB : RoomDatabase() {
    abstract fun favDao(): FavDao

    companion object {

        @Volatile
        private var INSTANCE: FavDB? = null

        fun getInstance(context: Context): FavDB {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavDB::class.java,
                        "favTable"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}