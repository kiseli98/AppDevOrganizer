package md.utm.organizer.ui.weather.future.list

import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.data.repository.ForecastRepository
import md.utm.organizer.internal.lazyDeffered
import md.utm.organizer.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    val unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeffered {
        forecastRepository.getFutureWeatherList(LocalDateTime.now())
    }


}
