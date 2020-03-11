package md.utm.organizer.data.provider

import android.content.Context
import md.utm.organizer.internal.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

//context is required because access to preferences is required
class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider  {

    override var initialUnitSystem: UnitSystem = getUnitSystem()

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }

    override suspend fun hasUnitSystemChanged(): Boolean {
        val hasChanged = initialUnitSystem.abbreviation != getUnitSystem().abbreviation
        if(hasChanged)
            initialUnitSystem = getUnitSystem()
        return hasChanged
    }
}