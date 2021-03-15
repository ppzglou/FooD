package gr.ppzglou.food

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import gr.ppzglou.food.util.get

@HiltAndroidApp
class FoodApplication : Application() {

    val sharedPrefs: SharedPreferences
        get() = applicationContext.getSharedPreferences(FOOD_PREFS, Context.MODE_PRIVATE)


    override fun onCreate() {
        super.onCreate()
        setupTheme()
    }

    private fun setupTheme() {
        when (sharedPrefs[THEME_MODE, ""]) {
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }


}