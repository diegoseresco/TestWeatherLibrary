package diegitsen.test.library.utils

import android.content.Context
import android.location.Geocoder
import androidx.fragment.app.FragmentManager
import diegitsen.test.library.ui.wind.WindPredictionBottomSheet
import seresco.maps.utils.lib.ui.meteorology.precipitation.HumidityBottomSheet
import diegitsen.test.library.ui.precipitation.PrecipitationBottomSheet
import seresco.maps.utils.lib.ui.meteorology.weather.todayPrediction.WeatherPredictionBottomSheet
import seresco.maps.utils.lib.ui.meteorology.weather.WeatherTomorrowPredictionBottomSheet
import seresco.maps.utils.lib.ui.meteorology.weather.WeatherWeeklyPredictionBottomSheet
import java.util.*

class MeteorologyUtils {

    fun openWeatherPredictionSheet(municipalityId: Int, supportFragmentManager: FragmentManager) {
        val trackingSheet = WeatherPredictionBottomSheet.newInstance(true, municipalityId)
        trackingSheet.show(supportFragmentManager, WeatherPredictionBottomSheet.TAG)
    }

    fun openMeteorologySheet(context: Context, meteorologyType: MeteorologyType, latitude: Double, longitude: Double, supportFragmentManager: FragmentManager) {
        val municipalityId = getMunicipalityId(context, latitude, longitude)
        when (meteorologyType) {
            MeteorologyType.WEATHER_TODAY -> {
                val bottomSheet = WeatherPredictionBottomSheet.newInstance(true, municipalityId)
                bottomSheet.show(supportFragmentManager, WeatherPredictionBottomSheet.TAG)
            }
            MeteorologyType.WEATHER_TOMORROW -> {
                val bottomSheet = WeatherTomorrowPredictionBottomSheet.newInstance(true, municipalityId)
                bottomSheet.show(supportFragmentManager, WeatherTomorrowPredictionBottomSheet.TAG)
            }
            MeteorologyType.WEATHER_WEEKLY -> {
                val bottomSheet = WeatherWeeklyPredictionBottomSheet.newInstance(true, municipalityId)
                bottomSheet.show(supportFragmentManager, WeatherWeeklyPredictionBottomSheet.TAG)
            }
            MeteorologyType.PRECIPITATION -> {
                val bottomSheet = PrecipitationBottomSheet.newInstance(true, municipalityId)
                bottomSheet.show(supportFragmentManager, PrecipitationBottomSheet.TAG)
            }
            MeteorologyType.WIND -> {
                val bottomSheet = WindPredictionBottomSheet.newInstance(true, municipalityId)
                bottomSheet.show(supportFragmentManager, WindPredictionBottomSheet.TAG)
            }
            MeteorologyType.HUMIDITY -> {
                val bottomSheet = HumidityBottomSheet.newInstance(true, municipalityId)
                bottomSheet.show(supportFragmentManager, HumidityBottomSheet.TAG)
            }
        }

    }

    fun openWeatherPredictionSheet(context: Context, latitude: Double, longitude: Double, supportFragmentManager: FragmentManager) {
        val municipalityId = getMunicipalityId(context, latitude, longitude)
        val trackingSheet = WeatherPredictionBottomSheet.newInstance(true, municipalityId)
        trackingSheet.show(supportFragmentManager, WeatherPredictionBottomSheet.TAG)
    }

    private fun getMunicipalityId(context: Context, latitude: Double, longitude: Double): Int {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val postalCode = addresses.first().postalCode;
        return postalCode.toInt()
    }

}

enum class MeteorologyType {
    WEATHER_TODAY, WEATHER_TOMORROW, WEATHER_WEEKLY, PRECIPITATION, WIND, HUMIDITY
}