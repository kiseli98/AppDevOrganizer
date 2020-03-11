package md.utm.organizer.data.provider

import md.utm.organizer.internal.UnitSystem

interface UnitProvider {
    var initialUnitSystem: UnitSystem
    fun getUnitSystem(): UnitSystem
    suspend fun hasUnitSystemChanged(): Boolean

}