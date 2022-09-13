import diegitsen.test.library.R

class Extensions {
    fun getWeatherIcon(weatherSkyStatus: String): Int {
        return when (weatherSkyStatus) {
            "Despejado" -> {
                R.drawable.ic_clear
            }
            "Poco nuboso" -> {
                R.drawable.ic_cloud
            }
            "Muy nuboso" -> {
                R.drawable.ic_high_cloudy
            }
            "Nubes altas" -> {
                R.drawable.ic_high_cloudy
            }
            "Nuboso con lluvia" -> {
                R.drawable.ic_rainy
            }
            "Muy nuboso con lluvia" -> {
                R.drawable.ic_rainy
            }
            "Intervalos nubosos con lluvia" -> {
                R.drawable.ic_rainy
            }
            "Intervalos nubosos con lluvia escasa" -> {
                R.drawable.ic_rainy
            }
            "Cubierto con lluvia" -> {
                R.drawable.ic_rainy
            }
            "Nuboso con lluvia escasa" -> {
                R.drawable.ic_rainy
            }
            "Muy nuboso con lluvia escasa" -> {
                R.drawable.ic_rainy
            }
            else -> R.drawable.ic_empty
        }
    }
}