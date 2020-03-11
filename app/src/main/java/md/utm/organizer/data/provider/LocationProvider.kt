package md.utm.organizer.data.provider

import md.utm.organizer.data.network.response.currentWeather.CurrentWeatherModel

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherEntry : CurrentWeatherModel): Boolean
    //return either device or custom location
    suspend fun getCoords() : HashMap<String, String>
    suspend fun getLocation() : String
    suspend fun isUsingDeviceLocation(): Boolean
}