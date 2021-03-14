package gr.ppzglou.food.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserPinEntity::class], version = 1)
abstract class UserPinDB : RoomDatabase() {
    abstract fun userPinsDao(): UserPinDao

    companion object {

        @Volatile
        private var INSTANCE: UserPinDB? = null

        fun getInstance(context: Context): UserPinDB {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserPinDB::class.java,
                        "pinsTable"
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