package diegitsen.test.library.data.entity

data class WeatherItem (
    val hour: Int,
    val temperature: Int,
    var skyStatus: String
) {
    constructor(hour: Int, temperature: Int) : this(
        hour, temperature, ""
    )
}