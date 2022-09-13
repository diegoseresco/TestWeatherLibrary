package diegitsen.test.library.data.datasource

import android.content.Context
import android.util.Log
import diegitsen.test.library.data.entity.SpecificPredictionResponse
import diegitsen.test.library.service.MeteorologyService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WeatherRemoteDatasource() {

    private var disposable: Disposable? = null
    private val apiService by lazy { MeteorologyService.create() }

    fun getWeatherPrediction(municipalityId: Int, onWeatherRemoteReadyCallback: OnWeatherRemoteReadyCallback) {
        disposable = apiService.getWeatherPredictionsKey(municipalityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        val strs = result.datos.split("/sh/").toTypedArray()
                        val key = strs.last()
                        getWeatherPredictionData(key, onWeatherRemoteReadyCallback)
                    }
                },
                { error ->
                    run {
                        Log.e("WeatherError", error.message.toString())
                    }
                }
            )
    }

    private fun getWeatherPredictionData(keyUrl: String, onWeatherRemoteReadyCallback: OnWeatherRemoteReadyCallback) {
        disposable = apiService.getWeatherPredictions(keyUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        onWeatherRemoteReadyCallback.onRemoteWeatherPredictionsDataReady(result.first())
                    }
                },
                { error ->
                    run {
                        Log.e("WeatherPredictionError", error.message.toString())
                    }
                }
            )
    }

}

interface OnWeatherRemoteReadyCallback {
    fun onRemoteWeatherPredictionsDataReady(predictionData: SpecificPredictionResponse?)
}