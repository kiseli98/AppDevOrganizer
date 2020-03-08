package md.utm.organizer.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.data.repository.ForecastRepository


//I s passed to Provider, which will preserve the sate of the view model
// and to not destroy/recreate because of the lifecycle

class CurrentWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(forecastRepository, unitProvider) as T
    }
}