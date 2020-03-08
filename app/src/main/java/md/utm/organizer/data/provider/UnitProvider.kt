package md.utm.organizer.data.provider

import md.utm.organizer.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}