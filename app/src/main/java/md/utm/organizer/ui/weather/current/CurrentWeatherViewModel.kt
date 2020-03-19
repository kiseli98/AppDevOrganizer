package md.utm.organizer.ui.weather.current

import androidx.lifecycle.ViewModel
import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.data.repository.ForecastRepository
import md.utm.organizer.internal.UnitSystem
import md.utm.organizer.internal.lazyDeffered
import md.utm.organizer.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeffered {
        //lazy init, only if View requires weather
        forecastRepository.getCurrentWeather()
    }

}
