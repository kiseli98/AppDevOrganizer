package md.utm.organizer.data.provider

import md.utm.organizer.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "Chisinau"
    }
}