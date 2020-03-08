package md.utm.organizer.ui

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LifecycleBoundLocationManager(
    lifecycleOwner: LifecycleOwner, // activity in this case
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationCallback: LocationCallback
) : LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private val locationRequest = LocationRequest().apply {
        interval = 5000 // 5 sec
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY  //Coarse location, not precise (fine)
    }

    //each time on onResume() in main activity
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}