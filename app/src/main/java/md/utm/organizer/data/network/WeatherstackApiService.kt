package md.utm.organizer.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import md.utm.organizer.data.network.response.CurrentWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "9132aa27a824a1293fa689bd6f20598b"

//  http://api.weatherstack.com/current?access_key=9132aa27a824a1293fa689bd6f20598b&query=New%20York&units=f

interface WeatherstackApiService {

    @GET("current")
    fun getCurrentWeather(
        @Query("query") location: String,
        @Query("unit") units: String = "m"
    ): Deferred<CurrentWeatherResponse>  // await for deferred. Once Response is ready -> start working with it

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
                    .addQueryParameter("access_key", API_KEY)
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
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory()) //because of Deferred
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherstackApiService::class.java)

        }
    }
}