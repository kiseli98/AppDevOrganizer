package md.utm.organizer.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import md.utm.organizer.R
import md.utm.organizer.data.network.response.currentWeather.Rain
import md.utm.organizer.data.network.response.currentWeather.Snow
import md.utm.organizer.internal.glide.GlideApp
import md.utm.organizer.ui.base.ScopedFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

// scoped fragment for coroutines
class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein() //get kodein from global

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(CurrentWeatherViewModel::class.java)
//
//        val apiService = WeatherstackApiService(ConnectivityInterceptorImpl(this.context!!))
//
//        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
//
//        weatherNetworkDataSource.downloadedCurrentWeather.observe(this, Observer {
//            textView_condition.text = it.toString()
//        })
//
//        GlobalScope.launch(Dispatchers.Main) {
//            weatherNetworkDataSource.fetchCurrentWeatherByLoc("London", "metric")
//        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    // @ScopedFragment
    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await() //because is deferred


        // observe from database
        currentWeather.observe(this@CurrentWeatherFragment, Observer {

            //first time call will be null
            if (it == null) return@Observer
            textView_condition.text = it.toString()
            group_loading.visibility = View.GONE
            updateLocation(it.name)
            updateDateToToday()
            updateTemperatures(it.main.temp, it.main.feelsLike)
            updateCondition(
                it.weatherDescription[0].main,
                it.weatherDescription[0].description
            ) // returns array with one element
            updatePrecipitation(it.rain, it.snow)
            updateWind(it.wind.speed)
            updateHumidity(it.main.humidity)
            updateCloudcover(it.clouds.percentage)
            updateVisibility(it.visibility)
            updateIcon(it.weatherDescription[0].icon)


        })
    }

    private fun updateIcon(icon: String) {
        //Image is also cached
        //"http://openweathermap.org/img/w/" + iconcode + ".png";
        val radius = context?.resources?.getDimensionPixelSize(R.dimen.corner_radius)
        GlideApp.with(this@CurrentWeatherFragment)
            .load("http://openweathermap.org/img/w/$icon.png")
//                .transform(RoundedCorners(radius!!))
            .into(imageView_condition_icon)

    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial

    }

    private fun updateLocation(location: String) {
        // get current activity
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "${String.format("%.1f", temperature).toDouble()}$unitAbbreviation"
        textView_feels_like_temperature.text =
            "Feels like ${String.format("%.1f", feelsLike).toDouble()}$unitAbbreviation"
    }

    private fun updateCondition(main: String, desc: String) {
        textView_condition.text = "$main ($desc)"
    }

    private fun updatePrecipitation(rain: Rain?, snow: Snow?) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        var temp = " - Total: 0 $unitAbbreviation"
        textView_precipitation.text = "Precipitation:"
        if (rain != null && snow != null)
            temp = " - Rain (1h): ${rain.precipitationRain_1h} $unitAbbreviation\n" +
                    " - Rain (3h): ${rain.precipitationRain_3h} $unitAbbreviation\n\n" +
                    " - Snow (1h): ${snow.precipitationSnow_1h} $unitAbbreviation\n" +
                    " - Snow (3h): ${snow.precipitationSnow_3h} $unitAbbreviation"
        else if (rain != null)
            temp = " - Rain (1h): ${rain.precipitationRain_1h} $unitAbbreviation\n" +
                    " - Rain (3h): ${rain.precipitationRain_3h} $unitAbbreviation"
        else if (snow != null)
            temp = " - Snow (1h): ${snow.precipitationSnow_1h} $unitAbbreviation\n" +
                    " - Snow (3h): ${snow.precipitationSnow_3h} $unitAbbreviation"

        textView_precipitation_details.text = temp
    }

    private fun updateWind(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km/h", "mph")
        textView_wind.text = "Wind: $windSpeed $unitAbbreviation"
    }

    private fun updateHumidity(humidityPercentage: Double) {
        textView_humidity.text = "Humidity: $humidityPercentage %"
    }

    private fun updateCloudcover(cloudCoverage: Double) {
        textView_cloudcover.text = "Cloud coverage: $cloudCoverage%"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi")
        textView_visibility.text = "Visibility: ${visibilityDistance / 1000} $unitAbbreviation"
    }

}
