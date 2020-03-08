package md.utm.organizer.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import md.utm.organizer.data.db.entity.Request
import md.utm.organizer.internal.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

//context is required because access to preferences is required
class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider  {

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }

    override suspend fun hasUnitSystemChanged(request: Request): Boolean {
        return request.unit != getUnitSystem().abbreviation
    }
}