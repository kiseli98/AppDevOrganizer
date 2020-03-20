package md.utm.organizer.ui.weather.future.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.imageView_condition_icon
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_cloudcover
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_condition
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_humidity
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_precipitation
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_precipitation_details
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_temperature
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_wind
import kotlinx.coroutines.launch

import md.utm.organizer.R
import md.utm.organizer.internal.DateNotFoundException
import md.utm.organizer.internal.glide.GlideApp
import md.utm.organizer.ui.base.ScopedFragment
import md.utm.organizer.utils.Converters
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureDetailWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein() //get kodein from global

    private val viewModelFactoryInstanceFactory : ((LocalDateTime) -> FutureDetailWeatherViewModelFactory) by factory()

    private lateinit var viewModel: FutureDetailWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { FutureDetailWeatherFragmentArgs.fromBundle(it) }
        val date = Converters().stringToDateNorm(safeArgs?.dateString) ?:  throw DateNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(date)).get(FutureDetailWeatherViewModel::class.java)

        bindUI()
    }


    private fun bindUI() = launch {
        val futureWeather = viewModel.weather.await() //because is deferred
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@FutureDetailWeatherFragment, Observer {
            if (it == null) return@Observer
            updateLocation(it.name)
        })

        futureWeather.observe(this@FutureDetailWeatherFragment, Observer {

            if (it == null) return@Observer
            updateDate(it.date)
            updateTemperatures(it.temp, it.minTemp, it.maxTemp)
            updateCondition(
                it.weatherDescription[0].main,
                it.weatherDescription[0].description
            ) // returns array with one element
            updatePrecipitation(
                it.precipitationRain_1h,
                it.precipitationRain_3h,
                it.precipitationSnow_1h,
                it.precipitationSnow_3h)
            updateWind(it.windSpeed)
            updateHumidity(it.humidity)
            updateCloudcover(it.cloudsPercentage)
            updatePressure(it.pressure)
            updateIcon(it.weatherDescription[0].icon)
        })
    }

    private fun updateIcon(icon: String) {
        GlideApp.with(this@FutureDetailWeatherFragment)
            .load("http://openweathermap.org/img/w/$icon.png")
            .into(imageView_condition_icon)
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(date: LocalDateTime) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
    }

    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "${String.format("%.1f", temperature).toDouble()}$unitAbbreviation"
        textView_min_max_temperature.text =
            "Min: ${String.format("%.1f", min).toDouble()}$unitAbbreviation, " +
                    "Max: ${String.format("%.1f", max).toDouble()}$unitAbbreviation"
    }

    private fun updateCondition(main: String, desc: String) {
        textView_condition.text = "$main ($desc)"
    }

    private fun updatePrecipitation(
        rain1: Double,
        rain3: Double,
        snow1: Double,
        snow3: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        var temp = " - Total: 0 $unitAbbreviation"
        textView_precipitation.text = "Precipitation:"
        textView_precipitation_details.text =
            " - Rain (1h): $rain1 $unitAbbreviation\n" +
                    " - Rain (3h): $rain3 $unitAbbreviation\n\n" +
                    " - Snow (1h): $snow1 $unitAbbreviation\n" +
                    " - Snow (3h): $snow3 $unitAbbreviation"
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

    private fun updatePressure(pressure: Double) {
        textView_pressure.text = "Pressure: $pressure mb"
    }



}
