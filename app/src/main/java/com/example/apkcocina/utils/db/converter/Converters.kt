package com.example.apkcocina.utils.db.converter

import androidx.room.TypeConverter
import com.example.apkcocina.utils.model.Alergenos
import com.google.common.reflect.TypeToken
import java.lang.reflect.Type
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromStringToInt(value: String?): Int? {
        val listType: Type = object : TypeToken<Int?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromIntToString(list: Int?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType: Type = object : com.google.gson.reflect.TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringtoInt(value: String?): List<Int> {
        val listType: Type = object : com.google.gson.reflect.TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntstoString(list: List<Int?>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringtoListAlergenos(value: String?): List<Alergenos> {
        val listType: Type = object : com.google.gson.reflect.TypeToken<List<Alergenos?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListAlergenostoString(list: List<Alergenos?>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringtoHashMap(value: String?): HashMap<String,String>? {
        val listType: Type = object : com.google.gson.reflect.TypeToken<HashMap<String,String>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromHashMaptoString(list: HashMap<String,String>?): String {
        return Gson().toJson(list)
    }

}