package md.utm.organizer.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.data.network.response.futureWeather.FutureWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "f3bb6b9403f02fe03522df2b11f57bd0"
/*
    OLD Current
    http://api.weatherstack.com/current?access_key=9132aa27a824a1293fa689bd6f20598b&query=New%20York&units=f
 */

/*
    NEW Current
    http://api.openweathermap.org/data/2.5/weather?APPID=f3bb6b9403f02fe03522df2b11f57bd0&q=

    NEW Future
    http://api.openweathermap.org/data/2.5/forecast?APPID=f3bb6b9403f02fe03522df2b11f57bd0&q=Miami&units=metric&cnt=1
 */

interface WeatherstackApiService {

    @GET("weather")
    fun getCurrentWeatherByLoc(
        @Query("q") location: String?,
        @Query("units") units: String,
        @Query("lang") language: String = "en"
    ): Deferred<CurrentWeatherEntry>  // await for deferred. Once Response is ready -> start working with it


    @GET("weather")
    fun getCurrentWeatherByCoord(
        @Query("lat") latitude: String? = null,
        @Query("lon") longitude: String? = null,
        @Query("units") units: String,
        @Query("lang") language: String = "en"
    ): Deferred<CurrentWeatherEntry>


    @GET("forecast")
    fun getFutureWeatherByLoc(
        @Query("q") location: String?,
        @Query("cnt") days: Int,
        @Query("units") units: String,
        @Query("lang") language: String = "en"
    ): Deferred<FutureWeatherResponse>


    @GET("forecast")
    fun getFutureWeatherByCoord(
        @Query("lat") latitude: String? = null,
        @Query("lon") longitude: String? = null,
        @Query("cnt") days: Int,
        @Query("units") units: String,
        @Query("lang") language: String = "en"
    ): Deferred<FutureWeatherResponse>


    //static methods, tied to Class
    companion object {
        //implicit method, similar to constructor in a way -> ClassName()
        //used to initialize the service
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): WeatherstackApiService {
            // Typically interceptors add, remove, or transform headers on the request or response
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("APPID", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor) // injection ( by Kodein)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory()) //because of Deferred
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherstackApiService::class.java)

        }
    }
}