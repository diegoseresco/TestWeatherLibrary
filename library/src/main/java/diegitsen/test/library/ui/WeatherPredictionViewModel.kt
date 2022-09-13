package diegitsen.test.library.ui

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import diegitsen.test.library.data.entity.SpecificPredictionResponse
import diegitsen.test.library.data.repository.OnWeatherRepositoryReadyCallback
import diegitsen.test.library.data.repository.WeatherRepository
import java.util.*


class WeatherPredictionViewModel(application: Application): AndroidViewModel(application) {

    private var weatherRepository = WeatherRepository()
    var weatherPrediction = MutableLiveData<SpecificPredictionResponse>()

    fun getWeatherPredictionsPerMunicipality(municipalityId: Int) {
        weatherRepository.getWeatherPrediction(municipalityId, object :
            OnWeatherRepositoryReadyCallback {
            override fun onDataReady(predictionData: SpecificPredictionResponse?) {
                weatherPrediction.value = predictionData
            }
        })
    }

}