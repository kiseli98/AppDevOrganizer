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
import md.utm.organizer.internal.glide.GlideApp
import md.utm.organizer.ui.base.ScopedFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

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

            group_loading.visibility = View.GONE
            updateDateToToday()
            updateTemperatures(it.main.temp, it.main.feelsLike)
            updateCondition(it.weatherDescription[0].description) // returns array with one element
//            updatePrecipitation(it.precip)
            updateWind(it.wind.speed)
            updateHumidity(it.main.humidity)
            updateCloudcover(it.clouds.percentage)
            updateVisibility(it.visibility)

            //Image is also cached
            //"http://openweathermap.org/img/w/" + iconcode + ".png";
            val radius = context?.resources?.getDimensionPixelSize(R.dimen.corner_radius)
            GlideApp.with(this@CurrentWeatherFragment)
                .load("http://openweathermap.org/img/w/${it.weatherDescription[0].icon}.png")
//                .transform(RoundedCorners(radius!!))
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String) : String {
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

        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private  fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
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
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

}
