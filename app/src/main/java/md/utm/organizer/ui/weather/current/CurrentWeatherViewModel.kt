package md.utm.organizer.ui.weather.current

import androidx.lifecycle.ViewModel;
import md.utm.organizer.data.repository.ForecastRepository
import md.utm.organizer.internal.UnitSystem
import md.utm.organizer.internal.lazyDeffered

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC//get from settings

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeffered { //lazy init, only if View requires weather
        forecastRepository.getCurrentWeather(isMetric)
    }
}
