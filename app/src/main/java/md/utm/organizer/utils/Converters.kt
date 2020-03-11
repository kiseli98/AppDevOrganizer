package md.utm.organizer.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import md.utm.organizer.data.network.response.currentWeather.WeatherDesc


class Converters {

    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        return list?.joinToString()
    }

    @TypeConverter
    fun fromStringToList(string: String?): List<String>? {
        return string?.split(", ")
    }


    @TypeConverter
    fun fromCountryLangList(value: List<WeatherDesc>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherDesc>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCountryLangList(value: String): List<WeatherDesc> {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherDesc>>() {}.type
        return gson.fromJson(value, type)
    }
}