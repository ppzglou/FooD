package gr.ppzglou.food.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gr.ppzglou.food.dao.fav.FavDB
import gr.ppzglou.food.dao.fav.FavDao
import gr.ppzglou.food.dao.fav.FavDaoImpl
import gr.ppzglou.food.dao.userpin.UserPinDB
import gr.ppzglou.food.dao.userpin.UserPinDao
import gr.ppzglou.food.dao.userpin.UserPinDaoImpl

@Module
@InstallIn(ApplicationComponent::class)
object DaoModule {

    @Provides
    fun provideUserPinDatabase(@ApplicationContext appContext: Context): UserPinDao {
        return UserPinDB.getInstance(appContext).userPinsDao()
    }

    @Provides
    fun provideUserPinDaoImpl(userPinDao: UserPinDao) =
        UserPinDaoImpl(userPinDao)

    @Provides
    fun provideFavDatabase(@ApplicationContext appContext: Context): FavDao {
        return FavDB.getInstance(appContext).favDao()
    }

    @Provides
    fun provideFavDaoImpl(favDao: FavDao) =
        FavDaoImpl(favDao)
}