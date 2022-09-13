package seresco.maps.utils.lib.ui.meteorology.weather

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
import kotlinx.android.synthetic.main.bottom_sheet_weather_prediction_tomorrow.*
import kotlinx.android.synthetic.main.bottom_sheet_weather_prediction_weekly.*
import java.util.*

class WeatherWeeklyPredictionBottomSheet(municipalityId: Int): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val mMunicipalityId = municipalityId
    private lateinit var weatherViewModel: WeatherPredictionViewModel
    private lateinit var weatherPrediction: SpecificPredictionResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_weather_prediction_weekly, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherPredictionViewModel::class.java)
        weatherViewModel.getWeatherPredictionsPerMunicipality(mMunicipalityId)
        setUpListeners()
        setUpObservers()
    }

    private fun setUpViews() {
        pbWeeklyWeatherPrediction.visibility = View.GONE
        llWeatherWeekly.visibility = View.VISIBLE
        tvMunicipalityNameWeekly.text = weatherPrediction.nombre
        tvProvinceNameWeekly.text = weatherPrediction.provincia
        setWeatherInfo(0)
        setDaysInHeader()
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
        tvCurrentTemperatureWeekly.text = "${currentItem.temperature}°"
        val weatherIconAtSix = Extensions().getWeatherIcon(currentItem.skyStatus)
        ivCurrentWeatherWeekly.setImageResource(weatherIconAtSix)
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

    private fun setWeatherInfo(day: Int) {
        val weatherItems = getWeatherItem(weatherPrediction.prediccion.dia[day])
        if (weatherItems.isNotEmpty()) {
            llWeatherFullInfo.visibility = View.VISIBLE
            llWeatherInfo.visibility = View.GONE

            val atSix = weatherItems[0]
            val atTwelve = weatherItems[1]
            val atEighteen = weatherItems[2]
            val atTwentyfour = weatherItems[3]

            tvAtSixTemperatureWeekly.text = "${atSix.temperature}°"
            val weatherIconAtSix = Extensions().getWeatherIcon(atSix.skyStatus)
            ivAtSixWeatherWeekly.setImageResource(weatherIconAtSix)

            tvAtTwelveTemperatureWeekly.text = "${atTwelve.temperature}°"
            val weatherIconAtTwelve = Extensions().getWeatherIcon(atTwelve.skyStatus)
            ivAtTwelveWeatherWeekly.setImageResource(weatherIconAtTwelve)

            tvAtEighteenTemperatureWeekly.text = "${atEighteen.temperature}°"
            val weatherIconAtEighteen = Extensions().getWeatherIcon(atEighteen.skyStatus)
            ivAtEighteenWeatherWeekly.setImageResource(weatherIconAtEighteen)

            tvAtTwentyFourTemperatureWeekly.text = "${atTwentyfour.temperature}°"
            val weatherIconAtTwentyFour = Extensions().getWeatherIcon(atTwentyfour.skyStatus)
            ivAtTwentyFourWeatherWeekly.setImageResource(weatherIconAtTwentyFour)

            setCurrentTemperature(weatherItems)
        } else {
            llWeatherFullInfo.visibility = View.GONE
            llWeatherInfo.visibility = View.VISIBLE
            
            val midDaySkyStatus = weatherPrediction.prediccion.dia[day].estadoCielo.firstOrNull { it.periodo == "00-12" }
            midDaySkyStatus?.let {
                llMidInfo.visibility = View.VISIBLE
                val weatherIconAtMidDay = Extensions().getWeatherIcon(midDaySkyStatus.descripcion)
                ivAtMiddayWeatherWeekly.setImageResource(weatherIconAtMidDay)
            } ?: run {
                llMidInfo.visibility = View.GONE
            }

            val midNightSkyStatus = weatherPrediction.prediccion.dia[day].estadoCielo.firstOrNull { it.periodo == "12-24" }
            midNightSkyStatus?.let {
                val weatherIconAtMidNight = Extensions().getWeatherIcon(midNightSkyStatus.descripcion)
                ivAtMidnightWeatherWeekly.setImageResource(weatherIconAtMidNight)
            } ?: run {
                llMidInfo.visibility = View.GONE
            }

            tvMaxTemperature.text = "Máxima temperatura: ${weatherPrediction.prediccion.dia[day].temperatura.maxima}°"
            tvMinTemperature.text = "Mínima temperatura: ${weatherPrediction.prediccion.dia[day].temperatura.minima}°"
        }

    }

    private fun setDaysInHeader() {
        val currentDate =  Calendar.getInstance()
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        rbDayOne.text = day.toString()

        currentDate.add(Calendar.DATE, 1)
        rbDayTwo.text = "${currentDate.get(Calendar.DAY_OF_MONTH)}"

        currentDate.add(Calendar.DATE, 1)
        rbDayThree.text = "${currentDate.get(Calendar.DAY_OF_MONTH)}"

        currentDate.add(Calendar.DATE, 1)
        rbDayFour.text = "${currentDate.get(Calendar.DAY_OF_MONTH)}"

        currentDate.add(Calendar.DATE, 1)
        rbDayFive.text = "${currentDate.get(Calendar.DAY_OF_MONTH)}"

        currentDate.add(Calendar.DATE, 1)
        rbDaySix.text = "${currentDate.get(Calendar.DAY_OF_MONTH)}"

        currentDate.add(Calendar.DATE, 1)
        rbDaySeven.text = "${currentDate.get(Calendar.DAY_OF_MONTH)}"
    }

    private fun setUpListeners() {
        rgWeek.setOnCheckedChangeListener { _, id ->
            when (id) {
                rbDayOne.id -> {
                    setWeatherInfo(0)
                }
                rbDayTwo.id -> {
                    setWeatherInfo(1)
                }
                rbDayThree.id -> {
                    setWeatherInfo(2)
                }
                rbDayFour.id -> {
                    setWeatherInfo(3)
                }
                rbDayFive.id -> {
                    setWeatherInfo(4)
                }
                rbDaySix.id -> {
                    setWeatherInfo(5)
                }
                rbDaySeven.id -> {
                    setWeatherInfo(6)
                }
            }
        }
    }

    private fun setUpObservers() {
        weatherViewModel.weatherPrediction.observe(this, Observer {
            weatherPrediction = it
            setUpViews()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dismissWithAnimation = arguments?.getBoolean(WeatherWeeklyPredictionBottomSheet.ARG_DISMISS_WITH_ANIMATION) ?: false
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = dismissWithAnimation
    }

    companion object {
        const val TAG = "weatherWeeklyPredictionBottomSheet"
        const val ARG_DISMISS_WITH_ANIMATION = "dismiss_with_animation"
        fun newInstance(dismissWithAnimation: Boolean, municipalityId: Int): WeatherWeeklyPredictionBottomSheet {
            val modalSimpleListSheet = WeatherWeeklyPredictionBottomSheet(municipalityId)
            modalSimpleListSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSimpleListSheet
        }
    }

    interface WeatherPredictionCallback {

    }
}