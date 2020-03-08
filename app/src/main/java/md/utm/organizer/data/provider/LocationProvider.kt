package md.utm.organizer.data.provider

import md.utm.organizer.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation : WeatherLocation): Boolean
    //return either device or custom location
    suspend fun getPreferredLocationString() : String
}