package md.utm.organizer.ui.weather.current

import androidx.lifecycle.ViewModel;
import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.data.repository.ForecastRepository
import md.utm.organizer.internal.UnitSystem
import md.utm.organizer.internal.lazyDeffered

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeffered {
        //lazy init, only if View requires weather
        forecastRepository.getCurrentWeather()
    }

    val weatherLocation by lazyDeffered {
        forecastRepository.getWeaherLocation()
    }

    val request by lazyDeffered {
        forecastRepository.getRequest()
    }
}
