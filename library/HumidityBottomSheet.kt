package seresco.maps.utils.lib.ui.meteorology.precipitation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_humidity.*
import kotlinx.android.synthetic.main.bottom_sheet_precipitation.*
import seresco.maps.utils.lib.R
import seresco.maps.utils.lib.data.entity.Response
import seresco.maps.utils.lib.data.entity.WeatherItem
import seresco.maps.utils.lib.ui.DetailBottomSheet
import seresco.maps.utils.lib.ui.meteorology.WeatherPredictionViewModel
import seresco.maps.utils.lib.utils.Extensions
import java.util.*

class HumidityBottomSheet(municipalityId: Int): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val mMunicipalityId = municipalityId
    private lateinit var weatherViewModel: WeatherPredictionViewModel
    private lateinit var weatherPrediction: Response.SpecificPredictionResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_humidity, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherPredictionViewModel::class.java)
        weatherViewModel.getWeatherPredictionsPerMunicipality(mMunicipalityId)
        setUpListeners()
        setUpObservers()
    }

    private fun setUpViews() {
        pb_humidity.visibility = View.GONE
        ll_humidity.visibility = View.VISIBLE
        tv_humidity_municipality.text = weatherPrediction.nombre
        tv_humidity_province.text = weatherPrediction.provincia
        setWeatherInfo()
        setHumidityInfo()
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
        tv_humidity_current_temperature.text = "${currentItem.temperature}°"
        val weatherIconAtSix = Extensions().getWeatherIcon(currentItem.skyStatus)
        iv_humidity_current_temperature.setImageResource(weatherIconAtSix)
    }

    private fun getWeatherItem(day: Response.Day): List<WeatherItem> {
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

    private fun setWeatherInfo() {
        val weatherItems = getWeatherItem(weatherPrediction.prediccion.dia.first())
        setCurrentTemperature(weatherItems)
    }

    private fun setHumidityInfo() {
        val relativeHumidity = weatherPrediction.prediccion.dia[1].humedadRelativa.dato
        relativeHumidity.forEach {
            when (it.hora) {
                6 -> {
                    tv_humidity_at_six_percentage.text = "${it.value}%"
                    ll_humidity_at_six_bar.updateLayoutParams {
                        height = (it.value * 2)
                    }
                }
                12 -> {
                    tv_humidity_at_twelve_percentage.text = "${it.value}%"
                    ll_humidity_at_twelve_bar.updateLayoutParams {
                        height = (it.value * 2)
                    }
                }
                18 -> {
                    tv_humidity_at_eighteen_percentage.text = "${it.value}%"
                    ll_humidity_at_eighteen_bar.updateLayoutParams {
                        height = (it.value * 2)
                    }
                }
                24 -> {
                    tv_humidity_at_twentyfour_percentage.text = "${it.value}%"
                    ll_humidity_at_twentyfour_bar.updateLayoutParams {
                        height = (it.value * 2)
                    }
                }
            }
        }
    }

    private fun setUpListeners() {

    }

    private fun setUpObservers() {
        weatherViewModel.weatherPrediction.observe(this, Observer {
            weatherPrediction = it
            setUpViews()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dismissWithAnimation = arguments?.getBoolean(DetailBottomSheet.ARG_DISMISS_WITH_ANIMATION) ?: false
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = dismissWithAnimation
    }

    companion object {
        const val TAG = "humidityBottomSheet"
        const val ARG_DISMISS_WITH_ANIMATION = "dismiss_with_animation"
        fun newInstance(dismissWithAnimation: Boolean, municipalityId: Int): HumidityBottomSheet {
            val modalSimpleListSheet = HumidityBottomSheet(municipalityId)
            modalSimpleListSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSimpleListSheet
        }
    }

    interface WeatherPredictionCallback {

    }
}