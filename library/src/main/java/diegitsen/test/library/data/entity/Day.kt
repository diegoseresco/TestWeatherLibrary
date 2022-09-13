package diegitsen.test.library.data.entity

data class Day(
    val estadoCielo: List<SkyStatus>,
    val probPrecipitacion: List<ClimatologyItem>,
    val temperatura: Temperature,
    val viento: List<WindItem>,
    val humedadRelativa: RelativeHumidity
)