package md.utm.organizer.ui.weather.future.detail

import androidx.lifecycle.ViewModel;
import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.data.repository.ForecastRepository
import md.utm.organizer.internal.lazyDeffered
import md.utm.organizer.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class FutureDetailWeatherViewModel(
    private val detailDate: LocalDateTime,
    private val forecastRepository: ForecastRepository,
    val unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeffered {
        forecastRepository.getFutureWeatherByDate(detailDate)
    }


}
