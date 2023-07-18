package com.example.apkcocina.utils.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apkcocina.utils.model.Receta

@Dao
interface RecetasBaseDAO {

    @Query("SELECT * FROM recetas")
    suspend fun getAllRecetasBase() : List<Receta>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceta(receta: Receta)


}