package diegitsen.test.library.service

import diegitsen.test.library.data.entity.ClimatePredictionResponse
import diegitsen.test.library.data.entity.SpecificPredictionResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

private const val API_KEY: String = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaWVnaXRzZW5AZ21haWwuY29tIiwianRpIjoiMmFiZGQ0NGUtYjgyMi00MDY1LWE2NDItYzY3NmEwMWVmOGE4IiwiaXNzIjoiQUVNRVQiLCJpYXQiOjE2NjA5ODQ5OTAsInVzZXJJZCI6IjJhYmRkNDRlLWI4MjItNDA2NS1hNjQyLWM2NzZhMDFlZjhhOCIsInJvbGUiOiIifQ.fMHq_0qUNMC8MrNJz0IGbTU-8o0ZP-90iDdUBpQkNPM"
private const val BASE_URL: String = "https://opendata.aemet.es/opendata/"

interface MeteorologyService {

    @Headers("api_key: $API_KEY")
    @GET("api/prediccion/especifica/municipio/diaria/{municipality_id}")
    fun getWeatherPredictionsKey(@Path("municipality_id") municipality_id: Int): Observable<ClimatePredictionResponse>

    @GET("sh/{key}")
    fun getWeatherPredictions(@Path("key") key: String): Observable<List<SpecificPredictionResponse>>

    /********************** API SERVICE **********************/

    companion object {
        fun create(): MeteorologyService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(MeteorologyService::class.java)
        }
    }
}