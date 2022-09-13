package diegitsen.test.library.ui.wind

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import diegitsen.test.library.R
import diegitsen.test.library.data.entity.SpecificPredictionResponse
import diegitsen.test.library.data.entity.WindItem
import diegitsen.test.library.ui.WeatherPredictionViewModel
import kotlinx.android.synthetic.main.bottom_sheet_weather_prediction_weekly.*
import kotlinx.android.synthetic.main.bottom_sheet_wind.*
import java.util.*

class WindPredictionBottomSheet(municipalityId: Int): BottomSheetDialogFragment() {

    private var dismissWithAnimation = false
    private val mMunicipalityId = municipalityId
    private lateinit var weatherViewModel: WeatherPredictionViewModel
    private lateinit var weatherPrediction: SpecificPredictionResponse
    private lateinit var currentWindInfo: WindItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_wind, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherPredictionViewModel::class.java)
        weatherViewModel.getWeatherPredictionsPerMunicipality(mMunicipalityId)
        setUpListeners()
        setUpObservers()
    }

    private fun setUpViews() {
        pb_wind.visibility = View.GONE
        ll_wind.visibility = View.VISIBLE

        tv_wind_municipality.text = weatherPrediction.nombre
        tv_wind_province.text = weatherPrediction.provincia

        setWindInfo()
    }

    private fun setWindInfo() {
        tv_wind_km.text = currentWindInfo.velocidad.toString()
        setCompassView()
    }

    private fun setCompassView() {
        when (currentWindInfo.direccion) {
            "O" -> {
                iv_arrow_o.visibility = View.VISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
            "E" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.VISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
            "N" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.VISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
            "S" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.VISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
            "SE" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.VISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
            "SO" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.VISIBLE
            }
            "NE" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.VISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
            "NO" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.VISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
            "C" -> {
                iv_arrow_o.visibility = View.INVISIBLE
                iv_arrow_e.visibility = View.INVISIBLE
                iv_arrow_n.visibility = View.INVISIBLE
                iv_arrow_s.visibility = View.INVISIBLE
                iv_arrow_ne.visibility = View.INVISIBLE
                iv_arrow_no.visibility = View.INVISIBLE
                iv_arrow_se.visibility = View.INVISIBLE
                iv_arrow_so.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpListeners() {
        rg_wind.setOnCheckedChangeListener { _, id ->
            when (id) {
                rb_wind_at_six.id -> {
                    currentWindInfo = weatherPrediction.prediccion.dia[1].viento.first { it.periodo == "00-06" }
                    setUpViews()
                }
                rb_wind_at_twelve.id -> {
                    currentWindInfo = weatherPrediction.prediccion.dia[1].viento.first { it.periodo == "06-12" }
                    setUpViews()
                }
                rb_wind_at_eighteen.id -> {
                    currentWindInfo = weatherPrediction.prediccion.dia[1].viento.first { it.periodo == "12-18" }
                    setUpViews()
                }
                rb_wind_at_twentyfour.id -> {
                    currentWindInfo = weatherPrediction.prediccion.dia[1].viento.first { it.periodo == "18-24" }
                    setUpViews()
                }
            }
        }
    }

    private fun setUpObservers() {
        weatherViewModel.weatherPrediction.observe(this, Observer { it ->
            weatherPrediction = it
            currentWindInfo = it.prediccion.dia[1].viento.first { it.periodo == "00-06" }
            setUpViews()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dismissWithAnimation = arguments?.getBoolean(WindPredictionBottomSheet.ARG_DISMISS_WITH_ANIMATION) ?: false
        (requireDialog() as BottomSheetDialog).dismissWithAnimation = dismissWithAnimation
    }

    companion object {
        const val TAG = "WindPredictionBottomSheet"
        const val ARG_DISMISS_WITH_ANIMATION = "dismiss_with_animation"
        fun newInstance(dismissWithAnimation: Boolean, municipalityId: Int): WindPredictionBottomSheet {
            val modalSimpleListSheet = WindPredictionBottomSheet(municipalityId)
            modalSimpleListSheet.arguments = bundleOf(ARG_DISMISS_WITH_ANIMATION to dismissWithAnimation)
            return modalSimpleListSheet
        }
    }
}