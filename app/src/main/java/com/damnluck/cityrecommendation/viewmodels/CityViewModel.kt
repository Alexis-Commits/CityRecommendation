package com.damnluck.cityrecommendation.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.damnluck.cityrecommendation.db.AppDatabase
import com.damnluck.cityrecommendation.models.City
import com.damnluck.cityrecommendation.repositories.CityRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


public class CityViewModel public constructor(public val context: Context) : ViewModel() {


    val city: MutableLiveData<List<City>> = MutableLiveData()

    private val cityRepository: CityRepository

    init {
        val cityDao = AppDatabase.getDatabase(context).cityDao()
        cityRepository =
                CityRepository(cityDao)
    }

    suspend fun getAll(){
        GlobalScope.launch {
            city.postValue(cityRepository.getCities())
        }
    }

    suspend fun insert(city: City){
        GlobalScope.launch {
            cityRepository.insert(city)
        }
    }

}
