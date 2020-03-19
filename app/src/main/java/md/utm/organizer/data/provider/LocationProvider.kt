package md.utm.organizer.data.provider

import md.utm.organizer.data.db.entity.CurrentWeatherEntry

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherEntry : CurrentWeatherEntry): Boolean
    //return either device or custom location
    suspend fun getCoords() : HashMap<String, String>
    suspend fun getLocation() : String
    fun isUsingDeviceLocation(): Boolean
}