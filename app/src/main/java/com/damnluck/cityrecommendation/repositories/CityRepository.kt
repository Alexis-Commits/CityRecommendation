package com.damnluck.cityrecommendation.repositories

import com.damnluck.cityrecommendation.db.tables.CityDao
import com.damnluck.cityrecommendation.models.City


class CityRepository(private val cityDao: CityDao) {

    suspend fun getCities(): List<City> {
        return cityDao.getAll()
    }

    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

}
