package md.utm.organizer.ui.base

import androidx.lifecycle.ViewModel
import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.data.repository.ForecastRepository
import md.utm.organizer.internal.UnitSystem
import md.utm.organizer.internal.lazyDeffered

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeffered {
        //lazy init, only if View requires weather
        forecastRepository.getCurrentWeather()
    }

}