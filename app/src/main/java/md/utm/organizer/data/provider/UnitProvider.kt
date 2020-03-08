package md.utm.organizer.data.provider

import md.utm.organizer.data.db.entity.Request
import md.utm.organizer.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
    suspend fun hasUnitSystemChanged(request: Request): Boolean

}