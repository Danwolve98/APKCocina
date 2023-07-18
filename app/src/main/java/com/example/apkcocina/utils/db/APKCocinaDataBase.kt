package com.example.apkcocina.utils.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.db.converter.Converters
import com.example.apkcocina.utils.db.dao.RecetasBaseDAO

@Database(
    entities = [
        Receta::class
    ]
, version = 2, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class APKCocinaDataBase : RoomDatabase() {

    abstract fun recetasBaseDAO() : RecetasBaseDAO

}