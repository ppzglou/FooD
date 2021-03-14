package gr.ppzglou.food.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gr.ppzglou.food.dao.UserPinDB
import gr.ppzglou.food.dao.UserPinDao
import gr.ppzglou.food.dao.UserPinDaoImpl

@Module
@InstallIn(ApplicationComponent::class)
object DaoModule {

    @Provides
    fun provideAnimalActionsDatabase(@ApplicationContext appContext: Context): UserPinDao {
        return UserPinDB.getInstance(appContext).userPinsDao()
    }

    @Provides
    fun provideAnimalActionsDaoImpl(userPinDao: UserPinDao) =
        UserPinDaoImpl(userPinDao)

}