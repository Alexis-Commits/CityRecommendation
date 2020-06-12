package com.damnluck.cityrecommendation.db.tables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.damnluck.cityrecommendation.models.City

@Dao
interface CityDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city : City)

    @Query("SELECT * FROM City")
    suspend fun getAll(): List<City>

}