package diegitsen.test.library.data.entity

data class RelativeHumidity(
    val maxima: String,
    val minima: String,
    val dato: List<TemperatureItem>
)