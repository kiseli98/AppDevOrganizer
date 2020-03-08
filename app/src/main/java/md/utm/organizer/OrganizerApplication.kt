package md.utm.organizer

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import md.utm.organizer.data.db.ForecastDatabase
import md.utm.organizer.data.network.*
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
        //injecting dependencies by binding implementations to particular interface types
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) } //bind with() to bind different class types
        bind() from singleton { WeatherstackApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        //needs 2 instances - WeatherNetworkDataSource and currentWeatherDao
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) } //provider because no singletons are required
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}