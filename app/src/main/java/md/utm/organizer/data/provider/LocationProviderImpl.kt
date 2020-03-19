package md.utm.organizer.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import md.utm.organizer.data.db.entity.CurrentWeatherEntry
import md.utm.organizer.internal.LocationPermissionNotGrantedException
import md.utm.organizer.internal.asDeferred
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext


    override suspend fun getCoords(): HashMap<String, String> {
        val coords = HashMap<String, String>()
        try {
            val deviceLocation = getLastDeviceLocation().await() ?: return coords
            coords["latitude"] = "${deviceLocation.latitude}"
            coords["longitude"] = "${deviceLocation.longitude}"
            return coords
        } catch (e: LocationPermissionNotGrantedException) {
            return coords
        }
    }

    override suspend fun getLocation(): String {
        return "${getCustomLocationName()}"
    }

    override fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    override suspend fun hasLocationChanged(lastWeatherEntry: CurrentWeatherEntry): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherEntry)
            //custom exception
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherEntry)
    }

    private fun hasCustomLocationChanged(lastWeatherEntry: CurrentWeatherEntry): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherEntry.name
        }
        return false
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherEntry: CurrentWeatherEntry): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastWeatherEntry.coord.lat) > comparisonThreshold &&
                abs(deviceLocation.longitude - lastWeatherEntry.coord.lon) > comparisonThreshold

    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

}