package seresco.maps.utils.lib.ui.meteorology.weather.todayPrediction

import Extensions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import diegitsen.test.library.R
import diegitsen.test.library.data.entity.Day
import diegitsen.test.library.data.entity.SpecificPredictionResponse
import diegitsen.test.library.data.entity.WeatherItem
import diegitsen.test.library.ui.WeatherPredictionViewModel
import kotlinx.android.synthetic.main.bottom_sheet_weather_prediction.*
import java.util.*

class WeatherPredictionBottomSheet(municipalityId: Int): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val mMunicipalityId = municipalityId
    private lateinit var weatherViewModel: WeatherPredictionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_weather_prediction, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherPredictionViewModel::class.java)
        weatherViewModel.getWeatherPredictionsPerMunicipality(mMunicipalityId)
        setUpListeners()
        setUpObservers()
    }

    private fun setUpViews(weatherPrediction: SpecificPredictionResponse) {
        pbWeatherPrediction.visibility = View.GONE
        llWeather.visibility = View.VISIBLE
        tvMunicipalityName.text = weatherPrediction.nombre
        tvProvinceName.text = weatherPrediction.provincia
        val weatherItems = getWeatherItem(weatherPrediction.prediccion.dia.first())
        setWeatherInfo(weatherItems)
        setCurrentTemperature(weatherItems)
    }

    private fun setCurrentTemperature(items: List<WeatherItem>) {
        val currentTimeInHours = Calendar.getInstance().time.hours
        val currentItem = when {
            currentTimeInHours <= 6 -> {
                items[0]
            }
            currentTimeInHours <= 12 -> {
                items[1]
            }
            currentTimeInHours <= 18 -> {
                items[2]
            }
            else -> {
                items[3]
            }
        }
        tvCurrentTemperature.text = "${currentItem.temperature}°"
        val weatherIconAtSix = Extensions().getWeatherIcon(currentItem.skyStatus)
        ivCurrentWeather.setImageResource(weatherIconAtSix)
    }

    private fun getWeatherItem(day: Day): List<WeatherItem> {
        val weatherItems = arrayListOf<WeatherItem>()
        day.temperatura.dato.forEach {
            val weatherItem = WeatherItem(it.hora, it.value)
            weatherItems.add(weatherItem)
        }
        val skyStatus = day.estadoCielo.filter { it.periodo == "00-06" || it.periodo == "06-12" || it.periodo == "12-18" || it.periodo == "18-24" }
        skyStatus.forEach {
            when (it.periodo) {
                "00-06" -> {
                    weatherItems[0].skyStatus = it.descripcion
                }
                "06-12" -> {
                    weatherItems[1].skyStatus = it.descripcion
                }
                "12-18" -> {
                    weatherItems[2].skyStatus = it.descripcion
                }
                "18-24" -> {
                    weatherItems[3].skyStatus = it.descripcion
                }
            }
        }
        return weatherItems
    }

    private fun setWeatherInfo(info: List<WeatherItem>) {
        val atSix = info[0]
        val atTwelve = info[1]
        val atEighteen = info[2]
        val atTwentyfour = info[3]

        tvAtSixTemperature.text = "${atSix.temperature}°"
        val weatherIconAtSix = Extensions().getWeatherIcon(atSix.skyStatus)
        ivAtSixWeather.setImageResource(weatherIconAtSix)

        tvAtTwelveTemperature.text = "${atTwelve.temperature}°"
        val weatherIconAtTwelve = Extensions().getWeatherIcon(atTwelve.skyStatus)
        ivAtTwelveWeather.setImageResource(weatherIconAtTwelve)

        tvAtEighteenTemperature.text = "${atEighteen.temperature}°"
        val weatherIconAtEighteen = Extensions().getWeatherIcon(atEighteen.skyStatus)
        ivAtEighteenWeather.setImageResource(weatherIconAtEighteen)

        tvAtTwentyFourTemperature.text = "${atTwentyfour.temperature}°"
        val weatherIconAtTwentyFour = Extensions().getWeatherIcon(atTwentyfour.skyStatus)
        ivAtTwentyFourWeather.setImageResource(weatherIconAtTwentyFour)
    }

    private fun setUpListeners() {

    }

    private fun setUpObservers() {
        weatherViewModel.weatherPrediction.observe(this, Observer {
            setUpViews(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dismissWithAnimation = arguments?.getBoolean(WeatherPredictionBottomSheet.ARG_DISMISS_WITH_ANIMATION) ?: false
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = dismissWithAnimation
    }

    companion object {
        const val TAG = "weatherPredictionBottomSheet"
        const val ARG_DISMISS_WITH_ANIMATION = "dismiss_with_animation"
        fun newInstance(dismissWithAnimation: Boolean, municipalityId: Int): WeatherPredictionBottomSheet {
            val modalSimpleListSheet = WeatherPredictionBottomSheet(municipalityId)
            modalSimpleListSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSimpleListSheet
        }
    }

    interface WeatherPredictionCallback {

    }
}

enum class WeatherSkyStatus(val status: String) {
    HIGH_CLOUDS("Nubes altas"),
    CLOUDY("Poco nuboso"),
    HIGH_CLOUDY("Muy nuboso"),
    CLEAR("Despejado")
}