package md.utm.organizer

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import md.utm.organizer.data.db.ForecastDatabase
import md.utm.organizer.data.db.WeatherLocationDao
import md.utm.organizer.data.network.*
import md.utm.organizer.data.provider.LocationProvider
import md.utm.organizer.data.provider.LocationProviderImpl
import md.utm.organizer.data.provider.UnitProvider
import md.utm.organizer.data.provider.UnitProviderImpl
import md.utm.organizer.data.repository.ForecastRepository
import md.utm.organizer.data.repository.ForecastRepositoryImpl
import md.utm.organizer.ui.weather.current.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class OrganizerApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        //provides instances of context, services, other related to Android
        import(androidXModule(this@OrganizerApplication))

        //BIND ALL IMPLEMENTATIONS TO REPLACE INTERFACES WITH IMPL

        //instance is returned from androidXModule
        bind() from singleton { ForecastDatabase(instance()) } //bind() from - to bind same class type
        //<>instance type, returns whatever from above
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind() from singleton { instance<ForecastDatabase>().requestDao() }
        //injecting dependencies by binding implementations to particular interface types
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) } //bind with() to bind different class types
        bind() from singleton { WeatherstackApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        //one for context, one for location service
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        //needs 4 instances - WeatherNetworkDataSource and currentWeatherDao, Location Provider and WeatherLocationDao
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance(), instance(), instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) } //provider because no singletons are required
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        //set default preferences if not specified
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}