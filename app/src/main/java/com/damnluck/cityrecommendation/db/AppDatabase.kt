package com.damnluck.cityrecommendation.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.damnluck.cityrecommendation.db.AppDatabase.Companion.DATABASE_VERSION
import com.damnluck.cityrecommendation.db.tables.CityDao
import com.damnluck.cityrecommendation.models.City


@Database(entities = [City::class] , version = DATABASE_VERSION , exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao():CityDao
    companion object {
        const val DATABASE_VERSION = 1

        private const val DATABASE_NAME = "cityRecommendation.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {

                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                )
                        .fallbackToDestructiveMigration()
                        .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}
