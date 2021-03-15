package gr.ppzglou.food.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gr.ppzglou.food.FOOD_PREFS
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(FOOD_PREFS, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext applicationContext: Context): Context =
        applicationContext


    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    @Provides
    fun provideConnectivityLiveData(
        application: Application,
        connectivityManager: ConnectivityManager
    ): ConnectivityLiveData =
        ConnectivityLiveData(application, connectivityManager)

}